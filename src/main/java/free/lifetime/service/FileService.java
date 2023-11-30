package free.lifetime.service;

/**
 * @create: 2023/11/12
 * @Description:
 * @FileName: FileService
 */
public interface FileService {

    String uploadImage(String base64);

    byte[] downloadImage(String filename);

    boolean removeImage(String filename);

    void setSavePath(String savePath);
}
