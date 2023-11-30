package free.lifetime.service.impl;

import free.lifetime.service.FileService;
import free.lifetime.utils.CommonUtil;
import free.lifetime.utils.ImageUtil;
import free.lifetime.utils.StringUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * @create: 2023/11/12
 * @Description:
 * @FileName: FileServiceImpl
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService {
    private static final String LOCAL_PATH = System.getProperty("user.dir") + "/static/upload/";
    private String savePath = LOCAL_PATH;

    @Override
    public void setSavePath(String savePath) {
        CommonUtil.require(IllegalArgumentException.class,
                StringUtils.hasText(savePath), "savePath cannot be null");
        this.savePath = LOCAL_PATH + StringUtil.wrap(savePath, "/");
    }

    @SneakyThrows
    @Override
    public String uploadImage(String base64) {
        log.info("上传图片：{}", savePath);
        return ImageUtil.saveImage(base64, savePath, null);
    }

    @SneakyThrows
    @Override
    public byte[] downloadImage(String filename) {
        log.info("请求图片：filename({})--({})", filename, savePath);
        return ImageUtil.getImage(savePath + filename);
    }

    @Override
    public boolean removeImage(String filename) {
        if (StringUtils.isEmpty(filename)) return true;

        File avatar = new File(savePath + filename);
        if (avatar.exists() && avatar.isFile()) {
            log.info("删除图片：{}，删除状态：{}", filename, avatar.delete());
        }
        return !avatar.exists();
    }
}
