package cn.postwall.blog.service;

import cn.postwall.blog.pojo.po.BlogArticlePO;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
public interface IBlogArticleService {

    /**
     * 添加文章
     * @param blogArticlePO
     * @return
     */
    boolean insertArticle(BlogArticlePO blogArticlePO);

    /**
     * 修改文章
     */
    boolean updateBlogArticle(BlogArticlePO blogArticlePO);

    /**
     * 查询文章
     */
    List<BlogArticlePO> pageUserArticles(String title, Long categoryId, Long userId, int curPage, int pageSize);

    List<BlogArticlePO> pageAdminArticles(String title, Long categoryId, String startTime, String endTime, int isDelete, int curPage, int pageSize);

    /**
     * 文章总数
     */
    int countUserArticles(String title, Long categoryId, Long userId);

    int countAdminArticles(String title, Long categoryId, String startTime, String endTime, int isDelete);

    /**
     * 获取标签文章列表
     * @param tagId
     * @param curPage
     * @param pageSize
     * @return
     */
    List<BlogArticlePO> pageBlogArticleTag(Long tagId, int curPage, int pageSize);

    /**
     * 通过链接查询文章
     */
    BlogArticlePO findArticleByLink(String link);

    /**
     * 通过id 查询文章
     */
    BlogArticlePO findArticleById(long articleId);

    /**
     * 查询文章标题是否存在
     */
    int findArticleByTitle(String title);

    /**
     * 删除/恢复 文章
     * @return
     */
    boolean updateIsDelete(long articleId, int isDelete);

    /**
     * 增加文章阅读数
     * @param articleId
     * @return
     */
    boolean increaseReading(long articleId);

    /**
     * 增加文章点赞数
     */
    boolean increasePraise(long articleId, boolean flag);

    /**
     * 评论数
     */
    boolean updateCommentNum(long articleId, boolean flag);

    /**
     * 修改文章状态
     */
    boolean updateArticleState(long articleId, Integer publishState, Integer commentState, Integer topState);

}
