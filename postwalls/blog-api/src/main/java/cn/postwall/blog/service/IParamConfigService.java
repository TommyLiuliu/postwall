package cn.postwall.blog.service;

/**
* @author liuhanchao
* @date 2022/12/11 02:34:45
* @Description:
*/
public interface IParamConfigService {

    /**
     * 查询配置
     * @param configKey 配置key值
     * @return
     */
    String findConfigValueByKey(String configKey);
}
