package cn.postwall.blog.utils;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheHelper {

    // 缓存信息
    private Map<String, Object> cacheMap =  new ConcurrentHashMap<>();

    /**
     * 存放数据
     * @param key
     * @param value
     */
    public static void set(String key, Object value) {
        CacheHelper cache = SpringContext.getBean(CacheHelper.class);
        cache.cacheMap.put(key, value);
    }

    /**
     * 返回数据
     */
    public static Object get(String key) {
        CacheHelper cache = SpringContext.getBean(CacheHelper.class);
        Object value = cache.cacheMap.get(key);
        return value;
    }

    /**
     * 删除数据
     */
    public static void del(String key) {
        CacheHelper cache = SpringContext.getBean(CacheHelper.class);
        cache.cacheMap.remove(key);
    }

    /**
     * 设置可过期缓存
     * @param key
     * @param value
     * @param time
     */
    public static void setExpire(String key, Object value, long time) {
        Date now = new Date();
        Date expDate = new Date(now.getTime() + (time * 1000));
        Map<String, Object> map = new HashMap();
        map.put("value", value);
        map.put("expDate", expDate.getTime());

        CacheHelper cache = SpringContext.getBean(CacheHelper.class);
        cache.cacheMap.put(key, map);
    }

    /**
     * 获取可过期缓存
     * @param key
     * @return
     */
    public static String getExpire(String key) {
        CacheHelper cache = SpringContext.getBean(CacheHelper.class);
        Map<String, Object> map = (Map<String, Object>) cache.cacheMap.get(key);
        if (map == null) {
            return null;
        }
        long expDate = (long) map.get("expDate");
        long time = System.currentTimeMillis();
        // 判断是否过期
        if (time > expDate) {
            cache.cacheMap.remove(key);
            return null;
        }
        return (String) map.get("value");
    }

}
