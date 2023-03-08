package cn.postwall.blog.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpringContext<T> implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContext.applicationContext = applicationContext;
    }

    public static <T> T  getBean(Class<T> clazz) {
        return applicationContext != null ? applicationContext.getBean(clazz) : null;
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return applicationContext != null ? applicationContext.getBeansOfType(clazz) : null;
    }
}
