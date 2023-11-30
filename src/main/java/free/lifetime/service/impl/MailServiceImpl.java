package free.lifetime.service.impl;

import free.lifetime.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @create: 2023/11/23
 * @Description:
 * @FileName: MailServiceImpl
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private JavaMailSender sender;

    @Override
    public boolean sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        try {
            sender.send(message);
            log.info("发送邮件成功");
            return true;
        } catch (MailException ex) {
            log.error("发送邮件失败", ex);
            return false;
        }
    }

    @Override
    public boolean sendHTMLEmail(String to, String subject, String content) {
        return false;
    }

    @Override
    public boolean sendAttachmentEmail(String to, String subject, String content, String filePath) {
        return false;
    }

    @Override
    public boolean sendInlineResourceEmail(String to, String subject, String content, String rscPath, String rscId) {
        return false;
    }
}
