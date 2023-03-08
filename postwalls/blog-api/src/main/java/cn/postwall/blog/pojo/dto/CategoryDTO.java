package cn.postwall.blog.pojo.dto;

import cn.postwall.blog.annotation.CopyIgnore;
import lombok.Data;

/**
 * @author liuhanchao
 * @date 2022/12/11 11:52
 * @Description:
 */
@Data
public class CategoryDTO {
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
     * 上传的图片
     */
    @CopyIgnore
    private String base64;
}
