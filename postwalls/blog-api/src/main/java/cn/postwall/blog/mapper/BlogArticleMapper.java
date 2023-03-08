package cn.postwall.blog.mapper;

import org.apache.ibatis.annotations.Param;
import cn.postwall.blog.pojo.po.BlogArticlePO;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
public interface BlogArticleMapper {

    List<BlogArticlePO> pageBlogArticle(@Param("title") String title,
                                        @Param("categoryId") Long categoryId,
                                        @Param("userId") Long userId,
                                        @Param("startTime") String startTime,
                                        @Param("endTime") String endTime,
                                        @Param("isDelete") int isDelete,
                                        @Param("curPage") int curPage,
                                        @Param("pageSize") int pageSize);

    int countBlogArticle(@Param("title") String title,
                         @Param("categoryId") Long categoryId,
                         @Param("userId") Long userId,
                         @Param("startTime") String startTime,
                         @Param("endTime") String endTime,
                         @Param("isDelete") int isDelete);

    int insertBlogArticle(@Param("blogArticle") BlogArticlePO blogArticle);

    int updateBlogArticle(@Param("blogArticle") BlogArticlePO blogArticle);

    BlogArticlePO findArticleByLink(@Param("link") String link);

    BlogArticlePO findArticleById(@Param("id") long id);

    int findArticleByTitle(@Param("title") String title);

    int updateIsDelete(@Param("articleId") long articleId, @Param("isDelete") int isDelete);

    /**
     * 增加文章阅读数
     * @param articleId
     * @return
     */
    int increaseReading(@Param("articleId") long articleId);

    /**
     * 增加文章点赞数
     */
    int increasePraise(@Param("articleId") long articleId, @Param("flag") boolean flag);

    /**
     * 评论数
     */
    int updateCommentNum(@Param("articleId") long articleId, @Param("flag") boolean flag);

    /**
     * 修改文章状态
     */
    int updateArticleState(@Param("articleId") long articleId,
                    @Param("publishState") Integer publishState,
                    @Param("commentState") Integer commentState,
                    @Param("topState") Integer topState);

}
