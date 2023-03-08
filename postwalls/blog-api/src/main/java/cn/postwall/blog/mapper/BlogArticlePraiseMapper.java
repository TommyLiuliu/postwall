package cn.postwall.blog.mapper;

import cn.postwall.blog.pojo.po.BlogArticlePO;
import org.apache.ibatis.annotations.Param;
import cn.postwall.blog.pojo.po.BlogArticlePraisePO;

import java.util.List;

/**
* @author liuhanchao
* @date 2023/02/20 10:27:24
* @Description:
*/
public interface BlogArticlePraiseMapper {

    List<BlogArticlePraisePO> pageBlogArticlePraise(@Param("curPage") int curPage, @Param("pageSize") int pageSize);

    int countBlogArticlePraise();

    int insertBlogArticlePraise(@Param("blogArticlePraise") BlogArticlePraisePO blogArticlePraise);

    int updateBlogArticlePraise(@Param("blogArticlePraise") BlogArticlePraisePO blogArticlePraise);

    /**
     * 通过文章id 获取全部全部点赞数
     */
    int countPraiseByArticleId(@Param("articleId") Long articleId);

    /**
     * 通过用户id 和 文章id 获取点赞
     */
    BlogArticlePraisePO findArticlePraise(@Param("articleId") Long articleId, @Param("userId") Long userId);

    /**
     * 取消点赞
     */
    int cancelPraise(@Param("id") Long id);

}
