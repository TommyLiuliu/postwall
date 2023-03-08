package cn.postwall.blog.mapper;

import org.apache.ibatis.annotations.Param;
import cn.postwall.blog.pojo.po.BlogArticleTagPO;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/26 11:10:03
* @Description:
*/
public interface BlogArticleTagMapper {

    List<BlogArticleTagPO> pageBlogArticleTag(@Param("tagId") Long tagId, @Param("curPage") int curPage, @Param("pageSize") int pageSize);

    int countBlogArticleTag(@Param("tagId") Long tagId);

    int insertBlogArticleTag(@Param("blogArticleTag") BlogArticleTagPO blogArticleTag);

    int updateBlogArticleTag(@Param("blogArticleTag") BlogArticleTagPO blogArticleTag);

    int deleteTagByTagId(@Param("tagId") long tagId);

    int deleteTagByArticle(@Param("articleId") long articleId);

    int deleteTagById(@Param("id") long id);

    List<BlogArticleTagPO> findTagByArticle(@Param("articleId") long articleId);

}
