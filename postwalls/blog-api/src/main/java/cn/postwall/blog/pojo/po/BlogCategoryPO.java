package cn.postwall.blog.pojo.po;

import cn.postwall.blog.annotation.CopyIgnore;
import lombok.Data;
/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
@Data
public class BlogCategoryPO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 封面地址
     */
    private String coverUrl;

    /**
     * 状态 1：可用 0：禁用
     */
    private Integer state;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 创建人
     */
    private Long createUser;

    @CopyIgnore
    private String createUserName;
    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 修改人
     */
    private Long updateUser;

    @CopyIgnore
    private String updateUserName;

    /**
     * 修改时间
     */
    private String updateTime;

}
