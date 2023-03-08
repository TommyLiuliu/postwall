package cn.postwall.blog.service;

import cn.postwall.blog.pojo.po.BlogArticleTagPO;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/26 11:10:03
* @Description:
*/
public interface IBlogArticleTagService {

    /**
     * 保存标签
     * @param articleTagPO
     * @return
     */
    boolean saveArticleTag(BlogArticleTagPO articleTagPO);

    int countBlogArticleTag(Long tagId);

    List<BlogArticleTagPO> findTagByArticle(long articleId);

    boolean deleteTagById(long id);

}
