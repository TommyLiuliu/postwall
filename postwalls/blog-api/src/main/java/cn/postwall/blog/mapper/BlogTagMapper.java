package cn.postwall.blog.mapper;

import org.apache.ibatis.annotations.Param;
import cn.postwall.blog.pojo.po.BlogTagPO;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
public interface BlogTagMapper {

    List<BlogTagPO> pageBlogTag(@Param("name") String name,
                                @Param("curPage") int curPage,
                                @Param("pageSize") int pageSize);

    int countBlogTag(@Param("name") String name);

    int insertBlogTag(@Param("blogTag") BlogTagPO blogTag);

    int deleteBlogTag(@Param("id") long id);

    BlogTagPO findTagById(@Param("id") long id);

    BlogTagPO findTagByName(@Param("name") String name);
}
