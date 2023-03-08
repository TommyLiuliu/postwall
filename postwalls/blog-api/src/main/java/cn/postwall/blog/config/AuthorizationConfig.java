package cn.postwall.blog.config;

import cn.postwall.blog.interceptor.LoginInterceptor;
import cn.postwall.blog.service.IBlogUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 后台拦截器
 * @author: liuhanchao
 * @Date: 2022/12/31 22:58
 */
@Configuration
public class AuthorizationConfig implements WebMvcConfigurer {

    @Autowired
    private IBlogUserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 后台拦截器
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new LoginInterceptor(userService));
        interceptorRegistration.addPathPatterns("/admin/**");
        interceptorRegistration.excludePathPatterns("/admin/captcha.jpg","/admin/login");
    }
}
