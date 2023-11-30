package free.lifetime.service;

import free.lifetime.model.User;
import free.lifetime.model.UserDetail;

import java.util.List;

/**
 * @create: 2023/11/12
 * @Description:
 * @FileName: UserService
 */
public interface UserService {

    String login(User user);

    Long addUser(User user);

    Integer removeUser(Long uid);

    UserDetail queryUserDetail(Long uid);

    Integer updateUser(User user);

    int updateUserDetail(UserDetail userDetail);

    boolean uploadAvatar(String base64, Long uid);

    byte[] downloadAvatar(Long uid);

    boolean isRoot(Long uid);

    List<User> list(User user);

    int removeUser(List<Long> idList);

    User queryById(Long id);
}
