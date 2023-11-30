package free.lifetime.interceptor;

import free.lifetime.constants.Constant;
import free.lifetime.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @create: 2023/11/13
 * @Description:
 * @FileName: AuthInterceptor
 */
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("path: {}", request.getServletPath());

        String token = getToken(request);
        if (StringUtils.hasText(token)) {
            resetToken(token);

            HttpSession session = request.getSession();
            if (Objects.isNull(session.getAttribute("uid"))) {
                log.info("设置会话uid");
                Long uid = JWTUtil.getUser(token).getId();
                session.setAttribute("uid", uid);
            }
            return true;
        }

        log.info("重定向到login.html");
        response.sendRedirect(request.getContextPath() + "/users/login");
        return false;
    }

    private static String getToken(HttpServletRequest request) {
        log.info("从Session获取Token");
        String token = "" + request.getSession().getAttribute(Constant.TOKEN);
        if (!"null".equals(token) && StringUtils.hasText(token)) return token;

        log.info("Session无Token，尝试从请求头获取");
        token = request.getHeader(Constant.TOKEN);
        if (StringUtils.hasText(token)) return token;

        log.info("请求头无Token，尝试从URL参数获取");
        token = request.getParameter(Constant.TOKEN);
        if (StringUtils.hasText(token)) return token;

        log.info("URL参数无Token，尝试从Cookie获取");
        Cookie[] cookieList = request.getCookies();
        if (Objects.nonNull(cookieList)) {
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals(Constant.TOKEN)) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }

    private void resetToken(String token) {
        log.info("重置Token有效时间");
        redisTemplate.expire(token, 30, TimeUnit.MINUTES);
    }
}
