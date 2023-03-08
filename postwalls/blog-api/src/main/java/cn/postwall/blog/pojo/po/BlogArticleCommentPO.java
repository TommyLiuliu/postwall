package cn.postwall.blog.pojo.po;

import cn.postwall.blog.pojo.dto.ArticleDTO;
import cn.postwall.blog.pojo.dto.UserInfo;
import lombok.Data;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/31 12:59:00
* @Description:
*/
@Data
public class BlogArticleCommentPO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 面板 id
     */
    private Long bannerId;

    /**
     * 评论时间
     */
    private String commentTime;

    /**
     * 父级评论 id
     */
    private Long parentId;

    /**
     * 文章 id
     */
    private Long articleId;
    private ArticleDTO articleDTO;

    /**
     * 评论用户id
     */
    private Long userId;
    private UserInfo userInfo;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 点赞数
     */
    private int praiseNum;


}
