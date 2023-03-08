package cn.postwall.blog.mapper;

import org.apache.ibatis.annotations.Param;
import cn.postwall.blog.pojo.po.BlogCategoryPO;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
public interface BlogCategoryMapper {

    List<BlogCategoryPO> pageBlogCategory(@Param("name") String name,
                                          @Param("isDelete") int isDelete,
                                          @Param("state") Integer state,
                                          @Param("curPage") int curPage,
                                          @Param("pageSize") int pageSize);

    int countBlogCategory(@Param("name") String name, @Param("isDelete") int isDelete, @Param("state") Integer state);

    int insertBlogCategory(@Param("blogCategory") BlogCategoryPO blogCategory);

    int updateBlogCategory(@Param("blogCategory") BlogCategoryPO blogCategory);

    /**
     * 通过ID查询分类
     */
    BlogCategoryPO findCategoryById(@Param("id") long id);

    /**
     * 通过名称查询分类
     */
    BlogCategoryPO findCategoryByName(@Param("name") String name);

    /**
     * 修改删除状态
     */
    int updateIsDelete(@Param("id") long id, @Param("isDelete") int isDelete);

    /**
     * 修改状态
     */
    int updateState(@Param("id") long id, @Param("state") int state);


}
