package cn.postwall.blog.pojo.po;

import lombok.Data;
/**
* @author liuhanchao
* @date 2023/02/20 10:27:24
* @Description:
*/
@Data
public class BlogArticlePraisePO {

    /**
     * id
     */
    private Long id;

    /**
     * 文章id
     */
    private Long articleId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 点赞时间
     */
    private String praiseTime;

    public BlogArticlePraisePO() {
    }

    public BlogArticlePraisePO(Long articleId, Long userId) {
        this.articleId = articleId;
        this.userId = userId;
    }
}
