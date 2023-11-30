package free.lifetime.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class MailServiceTest {

    @Autowired
    private MailService mailService;

    private static String to;
    private static String subject;
    private static String content;

    @BeforeAll
    static void beforeAll() {
        to = "wyyx_1365@163.com";
        subject = "wyyx";
        content = UUID.randomUUID().toString();
    }

    @Test
    void sendSimpleEmail() {
        boolean success = mailService.sendSimpleEmail(to, subject, content);
        System.out.println("success = " + success);
    }
}