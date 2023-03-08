package cn.postwall.blog.pojo.po;

import lombok.Data;
/**
* @author liuhanchao
* @date 2022/12/11 02:34:45
* @Description:
*/
@Data
public class ParamConfigPO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 参数名
     */
    private String configKey;

    /**
     * 参数值
     */
    private String configValue;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改时间
     */
    private String updateTime;

}