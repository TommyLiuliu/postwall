package cn.postwall.blog.service;

/**
 * @author liuhanchao
 * @date 2022/12/11 2:38
 * @Description:
 */
public interface IRedisRefresh {
    /**
     * 刷新缓存
     */
    void refreshRedis();
}
