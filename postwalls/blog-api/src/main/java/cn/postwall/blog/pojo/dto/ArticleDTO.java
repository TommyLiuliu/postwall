package cn.postwall.blog.pojo.dto;

import cn.postwall.blog.annotation.CopyIgnore;
import lombok.Data;

/**
 * @author liuhanchao
 * @date 2022/12/26 10:24
 * @Description:
 */
@Data
public class ArticleDTO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 分类 id
     */
    private Long categoryId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 封面图片地址
     */
    private String coverUrl;

    /**
     * 链接
     */
    private String link;

    /**
     * Base64 编码
     */
    @CopyIgnore
    private String base64;

    /**
     * 标签
     */
    @CopyIgnore
    private String[] tags;
}
