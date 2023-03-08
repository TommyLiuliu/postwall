package cn.postwall.blog.mapper;

import org.apache.ibatis.annotations.Param;
import cn.postwall.blog.pojo.po.BlogArticleCommentPO;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/31 12:59:00
* @Description:
*/
public interface BlogArticleCommentMapper {

    List<BlogArticleCommentPO> pageBlogArticleComment(@Param("curPage") int curPage, @Param("pageSize") int pageSize);

    int countBlogArticleComment();

    int insertBlogArticleComment(@Param("blogArticleComment") BlogArticleCommentPO blogArticleComment);

    int updateBlogArticleComment(@Param("blogArticleComment") BlogArticleCommentPO blogArticleComment);

    /**
     * 通过id 查询评论
     * @param commentId
     * @return
     */
    BlogArticleCommentPO findArticleCommentById(@Param("commentId") long commentId);

    /**
     * 通过文章id 查询文章评论（父评论）
     * @param articleId
     * @return
     */
    List<BlogArticleCommentPO> findArticleCommentParentByArticleId(@Param("articleId") long articleId,
                                                                   @Param("curPage") Integer curPage,
                                                                   @Param("pageSize") Integer pageSize);

    /**
     * 查询文章的评论数
     * @param articleId
     * @return
     */
    Integer countArticleCommentParentByArticleId(@Param("articleId") long articleId);

    /**
     * 通过面板id 查询同一评论下的所有回复
     * @param bannerId
     * @return
     */
    List<BlogArticleCommentPO> findArticleCommentByBannerId(@Param("bannerId") long bannerId);

    /**
     * 查询评论下的回复数
     * @param commentId
     * @return
     */
    Integer countArticleCommentParent(@Param("commentId") long commentId);

    /**
     * 查询评论列表
     * @param isDelete
     * @param curPage
     * @param pageSize
     * @return
     */
    List<BlogArticleCommentPO> pageArticleComment(@Param("isDelete") Integer isDelete,
                                              @Param("curPage") int curPage,
                                              @Param("pageSize") int pageSize);

    /**
     * 查询评论数量
     * @param isDelete
     * @return
     */
    Integer countArticleComment(@Param("isDelete") Integer isDelete);

    /**
     * 删除评论
     */
    int updateIsDelete(@Param("id") long id, @Param("isDelete") int isDelete);
}
