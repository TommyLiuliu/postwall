package cn.postwall.blog.service;

import cn.postwall.blog.pojo.po.BlogArticleCommentPO;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/31 12:59:00
* @Description:
*/
public interface IBlogArticleCommentService {
    /**
     * 评论文章
     * @param commentPO
     * @return
     */
    boolean comment(BlogArticleCommentPO commentPO);

    /**
     * 通过id 查询评论
     * @param commentId
     * @return
     */
    BlogArticleCommentPO findArticleCommentById(long commentId);


    List<BlogArticleCommentPO> findCommentByArticleId(Integer articleId, Integer curPage, Integer pageSize);

    Integer countArticleCommentParentByArticleId(Integer articleId);

    /**
     * 查询评论列表
     * @param isDelete
     * @param curPage
     * @param pageSize
     * @return
     */
    List<BlogArticleCommentPO> pageArticleComment(Integer isDelete, int curPage, int pageSize);

    /**
     * 查询评论数量
     * @param isDelete
     * @return
     */
    Integer countArticleComment(Integer isDelete);

    /**
     * 修改评论删除状态
     */
    boolean updateIsDelete(long id, int isDelete);
}
