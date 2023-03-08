package cn.postwall.blog.config;

import cn.postwall.blog.service.IRedisRefresh;
import cn.postwall.blog.utils.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author liuhanchao
 * @date 2022/12/11 2:41
 * @Description: 服务启动后执行的方法，主要用于更新redis缓存
 */
@Component
public class StartupRunner implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(StartupRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("加载数据到redis缓存...");
        long startTime = System.currentTimeMillis();
        Map<String, IRedisRefresh> serviceMap = SpringContext.getBeansOfType(IRedisRefresh.class);
        ArrayList<IRedisRefresh> services = new ArrayList<>(serviceMap.values());
        for (IRedisRefresh service : services) {
            service.refreshRedis();
        }
        log.info("数据加载完成,耗时：{}ms", System.currentTimeMillis() - startTime);
    }
}
