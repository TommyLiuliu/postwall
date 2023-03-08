package cn.postwall.blog.service;

import cn.postwall.blog.pojo.po.BlogCategoryPO;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
public interface IBlogCategoryService {

    /**
     * 分类列表
     * @param name
     * @param curPage
     * @param pageSize
     * @return
     */
    List<BlogCategoryPO> pageBlogCategory(String name, int isDelete, Integer state, int curPage, int pageSize);

    /**
     * 分类数
     * @param name
     * @return
     */
    int countBlogCategory(String name, int isDelete, Integer state);

    /**
     * 添加分类
     * @param categoryPO
     * @return
     */
    boolean insertBlogCategory(BlogCategoryPO categoryPO);

    /**
     * 通过名称查询分类
     */
    BlogCategoryPO findCategoryByName(String name);

    /**
     * 通过ID查询分类
     */
    BlogCategoryPO findCategoryById(long id);


    /**
     * 修改删除状态
     */
    boolean updateIsDelete(long id, int isDelete);

    /**
     * 修改状态
     */
    boolean updateState(long id, int state);

    /**
     * 修改分类
     */
    boolean updateCategory(BlogCategoryPO categoryPO);
}
