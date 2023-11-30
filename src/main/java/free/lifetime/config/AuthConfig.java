package free.lifetime.config;

import free.lifetime.interceptor.AuthInterceptor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @create: 2023/11/13
 * @Description:
 * @FileName: AuthConfig
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "user")
public class AuthConfig implements WebMvcConfigurer {
    /** 白名单 */
    private String[] whiteList;

    /** 将拦截器注入容器
     * @Description:
     * @date: 2023/11/30
     * @param
     * @return: free.lifetime.interceptor.AuthInterceptor
     */
    @Bean
    public AuthInterceptor authInterceptor() {
        return new AuthInterceptor();
    }

    /** 添加拦截器
     * @Description:
     * @date: 2023/11/30
     * @param registry
     * @return: void
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(whiteList);
    }
}
