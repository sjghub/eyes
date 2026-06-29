package hangaram.eyes;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AccessLoggingInterceptor accessLoggingInterceptor;

    public WebConfig(AccessLoggingInterceptor accessLoggingInterceptor) {
        this.accessLoggingInterceptor = accessLoggingInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(accessLoggingInterceptor);
    }
}
