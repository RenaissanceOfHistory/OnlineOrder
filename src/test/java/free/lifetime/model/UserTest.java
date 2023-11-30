package free.lifetime.model;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

class UserTest {

    @Test
    void testStateEnum() {
        User.State state = User.State.Ordinary;
        System.out.println(state);

        User user = User.builder()
                .state(User.State.Ordinary)
                .build();
        System.out.println(user);
    }

    @Test
    void test01() {
        System.out.println(User.State.Root.getValue().equals("1"));
    }

    @Test
    void test02() throws InterruptedException {
        Supplier<String> supplier = () -> System.currentTimeMillis() + "";
        for (int i = 0; i < 3; i++) {
            System.out.println(supplier.get());
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
