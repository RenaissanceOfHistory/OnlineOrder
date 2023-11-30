package free.lifetime.service;

/**
 * @create: 2023/11/23
 * @Description:
 * @FileName: MailService
 */
public interface MailService {
    boolean sendSimpleEmail(String to, String subject, String content);

    boolean sendHTMLEmail(String to, String subject, String content);

    boolean sendAttachmentEmail(String to, String subject, String content, String filePath);

    boolean sendInlineResourceEmail(String to, String subject, String content, String rscPath, String rscId);

}
