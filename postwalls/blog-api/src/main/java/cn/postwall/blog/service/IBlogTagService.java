package cn.postwall.blog.service;

import cn.postwall.blog.pojo.po.BlogTagPO;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
public interface IBlogTagService {

    List<BlogTagPO> pageBlogTag(String name, int curPage, int pageSize);

    int countBlogTag(String name);

    boolean insertBlogTag(BlogTagPO tagPO);

    boolean deleteBlogTag(long id);

    BlogTagPO findTagById(long id);

    BlogTagPO findTagByName(String name);

}
