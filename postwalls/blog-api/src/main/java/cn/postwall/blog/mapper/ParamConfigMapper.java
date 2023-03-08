package cn.postwall.blog.mapper;

import org.apache.ibatis.annotations.Param;
import cn.postwall.blog.pojo.po.ParamConfigPO;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 02:34:45
* @Description:
*/
public interface ParamConfigMapper {
    /**
     * 获取所有配置
     * @return
     */
    List<ParamConfigPO> listConfig();

    /**
     * 通过key值查询Value
     * @param configKey
     * @return
     */
    String findConfigValueByKey(@Param("configKey") String configKey);

    /**
     * 通过key修改配置
     * @param key
     * @param value
     * @return
     */
    Integer updateValueByKey(@Param("key") String key, @Param("value") String value);
}
