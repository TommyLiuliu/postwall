package cn.postwall.blog.pojo.po;

import lombok.Data;
/**
* @author liuhanchao
* @date 2022/12/26 11:10:03
* @Description:
*/
@Data
public class BlogArticleTagPO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 文章 id
     */
    private Long articleId;

    /**
     * 标签 id
     */
    private Long tagId;

    /**
     * 创建时间
     */
    private String createTime;

}