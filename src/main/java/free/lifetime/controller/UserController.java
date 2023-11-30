package free.lifetime.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import free.lifetime.common.Result;
import free.lifetime.constants.Constant;
import free.lifetime.constants.ServiceConstant;
import free.lifetime.model.User;
import free.lifetime.model.UserDetail;
import free.lifetime.model.UserModel;
import free.lifetime.model.UserPassword;
import free.lifetime.service.UserService;
import free.lifetime.utils.EncryptUtil;
import free.lifetime.utils.JWTUtil;
import free.lifetime.utils.RedisTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * @create: 2023/11/12
 * @Description:
 * @FileName: UserController
 */
@Slf4j
@Controller
@RequestMapping(ServiceConstant.USER_SERVICE)
public class UserController {
    private static final LineCaptcha captcha = CaptchaUtil.createLineCaptcha(200, 100);

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplateUtil redisTemplateUtil;

    @GetMapping("/login")
    public String toLogin() {
        return "login";
    }

    @PostMapping("/login/{code}")
    @ResponseBody
    public Result login(@RequestBody User user,
                        @PathVariable("code") String code,
                        HttpSession session) {
        if (!captcha.getCode().equals(code)) {
            return Result.error(HttpStatus.BAD_REQUEST.value(), "验证码错误");
        }

        String token = userService.login(user);
        if (Objects.isNull(token)) {
            return Result.error(HttpStatus.NO_CONTENT.value(), "用户不存在");
        }

        session.setAttribute("username", user.getUsername());
        session.setAttribute("isRoot", userService.isRoot(user.getId()));
        session.setAttribute(Constant.TOKEN, token);
        session.setAttribute("uid", user.getId());
        return Result.ok("登录成功", token);
    }

    @PostMapping("/enroll/{code}")
    @ResponseBody
    public Result enroll(@RequestBody User user,
                         @PathVariable("code") String code) {
        if (!captcha.getCode().equals(code)) {
            return Result.error(HttpStatus.BAD_REQUEST.value(), "验证码错误");
        }

        Long res = userService.addUser(user);
        return Objects.isNull(res) ?
                Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "用户已存在") :
                Result.ok("注册成功", res);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        redisTemplateUtil.expire((String) session.getAttribute(Constant.TOKEN), 0, TimeUnit.SECONDS);
        session.removeAttribute(Constant.TOKEN);
        return "redirect:/";
    }

    @PostMapping("/avatar/upload")
    @ResponseBody
    public Result uploadAvatar(@RequestBody UserDetail userDetail,
                               HttpSession session) {
        Long uid = (Long) session.getAttribute("uid");
        boolean res = userService.uploadAvatar(userDetail.getAvatar(), uid);
        return !res ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "上传文件失败") :
                Result.ok("上传文件成功");
    }

    @GetMapping("/avatar/download")
    public void downloadAvatar(HttpSession session,
                               HttpServletResponse response) {
        Long uid = (Long) session.getAttribute("uid");
        try (BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            output.write(userService.downloadAvatar(uid));
        } catch (Exception e) {
            log.error("下载用户头像失败", e);
        }
    }

    @GetMapping("/code")
    public void getCode(HttpServletResponse response) throws IOException {
        Boolean hasKey = redisTemplateUtil.hasKey("code::" + captcha.getCode());
        if (!Objects.equals(hasKey, Boolean.TRUE)) {
            captcha.createCode();
            log.info("设置验证码缓存");
            redisTemplateUtil.setValue("code::" + captcha.getCode(), "", 5, TimeUnit.SECONDS);
        }

        log.info("从缓存获取验证码：{}", captcha.getCode());
        try (OutputStream output = response.getOutputStream()) {
            captcha.write(output);
        }
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) Integer pageNum,
                       @RequestParam(required = false) Integer pageSize,
                       User user,
                       Model model) {
        // 预处理分页值
        Predicate<Integer> predicate = i -> Objects.isNull(i) || i < 1;
        if (predicate.test(pageNum)) pageNum = 1;
        if (predicate.test(pageSize)) pageSize = 5;

        // 启动分页
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userService.list(user);
        PageInfo<User> pageInfo = new PageInfo<>(userList);

        model.addAttribute("page", pageInfo);
        return "forward:/?funcIdx=1";
    }

    @GetMapping("/image/download/{uid}")
    @ResponseBody
    public void downloadImage(@PathVariable("uid") Long uid,
                              HttpServletResponse response) {
        try (BufferedOutputStream output = new BufferedOutputStream(response.getOutputStream())) {
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            output.write(userService.downloadAvatar(uid));
        } catch (IOException e) {
            log.error("用户图片下载失败", e);
        }
    }

    @DeleteMapping("/remove/{id}")
    @ResponseBody
    public Result remove(@PathVariable("id") Long id) {
        int res = userService.removeUser(id);
        return (res < 1) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "用户删除失败") :
                Result.ok("用户删除成功");
    }

    @DeleteMapping("/remove/id-list")
    @ResponseBody
    public Result deleteUserBatch(@RequestBody List<Long> idList) {
        int res = userService.removeUser(idList);
        return Result.ok("删除成功", res);
    }

    @PostMapping("/add")
    @ResponseBody
    public Result addUser(@RequestBody UserModel userModel) {
        User user = User.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .build();
        Long userDetailID = userService.addUser(user);
        if (Objects.isNull(userDetailID)) {
            return Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "已存在同名用户");
        }

        UserDetail userDetail = UserDetail.builder()
                .id(userDetailID)
                .email(userModel.getEmail())
                .phone(userModel.getPhone())
                .address(userModel.getAddress())
                .avatar(userModel.getAvatar())
                .build();
        int res = userService.updateUserDetail(userDetail);

        return (res < 1) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "用户添加失败") :
                Result.ok("用户添加成功");
    }

    @PutMapping("/update")
    @ResponseBody
    public Result updateUser(@RequestBody UserModel userModel) {
        User user = User.builder()
                .id(userModel.getUid())
                .username(userModel.getUsername())
                .build();
        Integer update = userService.updateUser(user);
        if (Objects.isNull(update)) {
            return Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "已存在同名用户");
        }

        UserDetail userDetail = UserDetail.builder()
                .id(userModel.getId())
                .email(userModel.getEmail())
                .phone(userModel.getPhone())
                .address(userModel.getAddress())
                .avatar(userModel.getAvatar())
                .build();
        update = userService.updateUserDetail(userDetail);
        return (update < 1) ?
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), "用户修改失败") :
                Result.ok("用户修改成功");
    }

    @GetMapping("/form")
    public String userForm(@RequestParam(required = false) Long uid,
                           Model model) {
        model.addAttribute("user", userService.queryById(uid));
        model.addAttribute("userDetail", userService.queryUserDetail(uid));
        return "views/user/user";
    }

    @GetMapping("/password")
    public String password() {
        return "forward:/?funcIdx=7";
    }

    @PutMapping("/password/change")
    @ResponseBody
    public Result changePassword(@RequestBody UserPassword userPassword,
                                 HttpSession session) {
        String token = (String) session.getAttribute(Constant.TOKEN);
        User user = JWTUtil.getUser(token);

        userPassword.setOldPassword(EncryptUtil.encode(userPassword.getOldPassword(), EncryptUtil.EncryptType.MD5));
        if (!user.getPassword().equals(userPassword.getOldPassword())) {
            return Result.error(HttpStatus.NOT_ACCEPTABLE.value(), "密码验证错误");
        }

        user.setPassword(EncryptUtil.encode(userPassword.getNewPassword(), EncryptUtil.EncryptType.MD5));
        Integer update = userService.updateUser(user);

        redisTemplateUtil.expire(token, 0, TimeUnit.SECONDS);
        session.removeAttribute(Constant.TOKEN);
        return Result.ok("密码修改成功", update);
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        String token = (String) session.getAttribute(Constant.TOKEN);
        User user = JWTUtil.getUser(token);
        UserDetail userDetail = userService.queryUserDetail(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("userDetail", userDetail);
        return "views/user/profile";
    }
}
