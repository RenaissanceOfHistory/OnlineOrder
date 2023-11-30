package free.lifetime.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void list() {
        orderService.list(null).forEach(System.out::println);
    }

    @Test
    void querySalesAmount() {
        orderService.querySalesAmount().forEach(System.out::println);
    }
}