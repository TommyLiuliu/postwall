package cn.postwall.blog.service.impl;

import cn.postwall.blog.mapper.ParamConfigMapper;
import cn.postwall.blog.pojo.po.ParamConfigPO;
import cn.postwall.blog.service.IParamConfigService;
import cn.postwall.blog.service.IRedisRefresh;
import cn.postwall.blog.utils.CacheHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 02:34:45
* @Description:
*/
@Service
public class ParamConfigServiceImpl implements IParamConfigService, IRedisRefresh {

    @Autowired
    private ParamConfigMapper paramConfigMapper;

    @Override
    public String findConfigValueByKey(String configKey) {
        String value = String.valueOf(CacheHelper.get(configKey));
        if (value == null) {
            value = paramConfigMapper.findConfigValueByKey(configKey);
            if (value != null) {
                CacheHelper.set(configKey, value);
            }
        }
        return value;
    }

    @Override
    public void refreshRedis() {
        List<ParamConfigPO> configPOS = paramConfigMapper.listConfig();
        for (ParamConfigPO configPO : configPOS) {
            String configValue = configPO.getConfigValue();
            if (configValue != null && !"".equals(configValue)) {
                CacheHelper.set(configPO.getConfigKey(), configPO.getConfigValue());
            }
        }
    }
}
