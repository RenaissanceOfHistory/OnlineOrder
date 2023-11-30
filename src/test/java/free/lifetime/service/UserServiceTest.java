package free.lifetime.service;

import free.lifetime.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void login() {
        User user = User.builder()
                .username("Nicholas")
                .password("one_peace")
                .build();
        userService.login(user);
    }

    @Test
    void enroll() {
        User user = User.builder()
                .username("Nicholas")
                .password("one_peace")
                .state(User.State.Root)
                .build();
        Long enroll = userService.addUser(user);
        System.out.println("addUser = " + enroll);
    }

    @Test
    void uploadAvatar() {
        User.State root = User.State.Root;
        System.out.println(root.getValue());
        System.out.println(root.getName());
        System.out.println(root);
    }

    @Test
    void downloadAvatar() {
    }
}