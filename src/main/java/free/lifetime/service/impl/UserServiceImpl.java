package free.lifetime.service.impl;

import free.lifetime.mapper.UserDetailMapper;
import free.lifetime.mapper.UserMapper;
import free.lifetime.model.User;
import free.lifetime.model.UserDetail;
import free.lifetime.service.FileService;
import free.lifetime.service.UserService;
import free.lifetime.utils.EncryptUtil;
import free.lifetime.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @create: 2023/11/12
 * @Description:
 * @FileName: UserServiceImpl
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private static final String SAVE_PATH = "avatar";
    private static final int MAX_BATCH_SIZE = 100;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserDetailMapper userDetailMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private FileService fileService;

    @Override
    public String login(User user) {
        log.info("查询用户：{}", user);
        user.setPassword(EncryptUtil.encode(user.getPassword(), EncryptUtil.EncryptType.MD5));
        Long uid = userMapper.selectPrimaryKeyByNameAndPwd(user.getUsername(), user.getPassword());

        if (Objects.nonNull(uid)) {
            user.setId(uid);
            log.info("查询成功，设置Token：{}", user);
            String token = JWTUtil.token(user);
            log.info("设置token缓存：{}", token);
            redisTemplate.opsForValue().set(token, "", 30, TimeUnit.MINUTES);
            return token;
        }

        log.info("用户不存在");
        return null;
    }

    @Override
    @Transactional
    public Long addUser(User user) {
        if (hasUser(user)) return null;

        log.info("添加用户：{}", user);
        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(User.DEFAULT_PASSWORD);
        }

        user.setPassword(EncryptUtil.encode(user.getPassword(), EncryptUtil.EncryptType.MD5));
        userMapper.insertSelective(user);
        UserDetail userDetail = UserDetail.builder().uid(user.getId()).build();
        userDetailMapper.insertSelective(userDetail);
        return userDetail.getId();
    }

    private boolean hasUser(User user) {
        log.info("查询用户：{}", user);
        return userMapper.selectCount(user) > 0;
    }

    @Transactional
    @Override
    public Integer removeUser(Long uid) {
        log.info("删除用户：id({})", uid);
        cleanupImage(uid);
        userDetailMapper.deleteByUid(uid);
        return userMapper.deleteByPrimaryKey(uid);
    }

    @Transactional
    @Override
    public int removeUser(List<Long> idList) {
        log.info("批量删除用户：size({})", idList.size());
        idList.forEach(uid -> {
            cleanupImage(uid);
            userDetailMapper.deleteByUid(uid);
        });

        int delete = 0, index = 0;
        while (idList.size() > MAX_BATCH_SIZE && index + MAX_BATCH_SIZE < idList.size()) {
            delete += userMapper.deleteBatch(idList.subList(index, index + MAX_BATCH_SIZE));
            index += MAX_BATCH_SIZE;
        }
        delete += userMapper.deleteBatch(idList.subList(index, idList.size()));
        return delete;
    }

    @Override
    public boolean uploadAvatar(String base64, Long uid) {
        cleanupImage(uid);
        String filename = fileService.uploadImage(base64);
        return userDetailMapper.updateAvatarByUid(uid, filename) > 0;
    }

    @Override
    public byte[] downloadAvatar(Long uid) {
        log.info("查询用户头像：id({})", uid);
        String avatar = userDetailMapper.selectAvatarByUid(uid);
        if (StringUtils.isEmpty(avatar)) return new byte[0];

        fileService.setSavePath(SAVE_PATH);
        return fileService.downloadImage(avatar);
    }

    @Override
    public boolean isRoot(Long uid) {
        log.info("查询用户身份：id({})", uid);
        return User.State.Root.getValue().equals(userMapper.selectState(uid));
    }

    @Override
    public UserDetail queryUserDetail(Long uid) {
        return userDetailMapper.selectByUid(uid);
    }

    @Override
    public Integer updateUser(User user) {
        log.info("查询是否存在同名用户：{}", user);
        if (hasUser(user)) return null;

        log.info("修改用户：{}", user);
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public int updateUserDetail(UserDetail userDetail) {
        setImage(userDetail);
        return userDetailMapper.updateByPrimaryKeySelective(userDetail);
    }

    @Override
    public List<User> list(User user) {
        return userMapper.selectList(user);
    }

    private void cleanupImage(Long uid) {
        String avatar = userDetailMapper.selectAvatarByUid(uid);
        if (StringUtils.hasText(avatar)) {
            fileService.setSavePath(SAVE_PATH);
            fileService.removeImage(avatar);
        }
    }

    private void setImage(UserDetail userDetail) {
        if (StringUtils.isEmpty(userDetail.getAvatar())) {
            userDetail.setAvatar(null);
            return;
        }

        fileService.setSavePath(SAVE_PATH);
        cleanupImage(userDetail.getUid());
        userDetail.setAvatar(fileService.uploadImage(userDetail.getAvatar()));
    }

    @Override
    public User queryById(Long id) {
        log.info("查询用户：id({})", id);
        return userMapper.selectByPrimaryKey(id);
    }
}
