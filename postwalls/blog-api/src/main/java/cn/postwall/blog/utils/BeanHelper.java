package cn.postwall.blog.utils;

import cn.postwall.blog.annotation.CopyIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuhanchao
 */
public class BeanHelper {
    private static final Logger logger = LoggerFactory.getLogger(BeanHelper.class);

    /**
     * Java实体类转Map：方法一
     * @param obj
     * @return
     */
    public static Map<String, Object> entityToMap(Object obj){
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for(Field field : clazz.getDeclaredFields()){
            field.setAccessible(true);
            String fieldName = field.getName();
            Object object = null;
            try {
                object = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.info(e.getMessage());
            }
            map.put(fieldName, object);
        }
        return map;
    }


    /**
     * 给目标类的相同字段赋值
     * @param object
     * @param clazz
     * @param <E>
     * @return
     */
    public static <E> E copyProperties(Object object, Class<E> clazz) {
        // 初始化
        E e = null;
        try {
            e = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.error("实例化失败");
            return null;
        }
        E finalE = e;
        // 遍历所有变量
        ReflectionUtils.doWithFields(e.getClass(), declaredField -> {
            ReflectionUtils.makeAccessible(declaredField);
            // 判断是否忽略映射
            CopyIgnore annotation = declaredField.getDeclaredAnnotation(CopyIgnore.class);
            if (annotation != null) {
                return;
            }
            // 获取目标值
            Field targetField = ReflectionUtils.findField(object.getClass(), declaredField.getName());
            if (targetField == null) {
                return;
            }
            ReflectionUtils.makeAccessible(targetField);
            Object field = ReflectionUtils.getField(targetField, object);
            // 赋值
            ReflectionUtils.setField(declaredField, finalE, field);
        });
        return e;
    }

}
