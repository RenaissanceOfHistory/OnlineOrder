package free.lifetime;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @create: 2023/11/12
 * @Description:
 * @FileName: OnlineOrderApp
 */
@MapperScan(basePackages = "free.lifetime.mapper")
@SpringBootApplication
public class OnlineOrderApp {
    public static void main(String[] args) {
        SpringApplication.run(OnlineOrderApp.class, args);
    }
}