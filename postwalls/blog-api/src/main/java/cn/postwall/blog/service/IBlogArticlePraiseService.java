package cn.postwall.blog.service;

import cn.postwall.blog.pojo.po.BlogArticlePraisePO;

/**
* @author liuhanchao
* @date 2023/02/20 10:27:24
* @Description:
*/
public interface IBlogArticlePraiseService {

    int insertBlogArticlePraise(BlogArticlePraisePO blogArticlePraise);

    /**
     * 通过文章id 获取全部全部点赞数
     */
    int countPraiseByArticleId(Long articleId);

    /**
     * 通过用户id 和 文章id 获取点赞
     */
    BlogArticlePraisePO findArticlePraise(Long articleId, Long userId);

    /**
     * 取消点赞
     */
    boolean cancelPraise(Long id);
}
