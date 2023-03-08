package cn.postwall.blog.service.impl;

import cn.postwall.blog.mapper.BlogCategoryMapper;
import cn.postwall.blog.mapper.BlogUserMapper;
import cn.postwall.blog.pojo.po.BlogCategoryPO;
import cn.postwall.blog.pojo.po.BlogUserPO;
import cn.postwall.blog.service.IBlogCategoryService;
import cn.postwall.blog.utils.CacheHelper;
import cn.postwall.blog.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
@Service
public class BlogCategoryServiceImpl implements IBlogCategoryService {

    @Autowired
    private BlogCategoryMapper blogCategoryMapper;

    @Autowired
    private BlogUserMapper userMapper;

    @Override
    public List<BlogCategoryPO> pageBlogCategory(String name, int isDelete, Integer state, int curPage, int pageSize) {
        List<BlogCategoryPO> blogCategoryPOS = blogCategoryMapper.pageBlogCategory(name, isDelete, state,(curPage - 1) * pageSize, pageSize);
        for (BlogCategoryPO blogCategoryPO : blogCategoryPOS) {
            // 创建人
            BlogUserPO createUser = userMapper.findUserById(blogCategoryPO.getCreateUser());
            blogCategoryPO.setCreateUserName(createUser.getUsername());
            //修改人
            BlogUserPO updateUser  = userMapper.findUserById(blogCategoryPO.getUpdateUser());
            blogCategoryPO.setUpdateUserName(updateUser.getUsername());
        }
        return blogCategoryPOS;
    }

    @Override
    public int countBlogCategory(String name, int isDelete, Integer state) {
        return blogCategoryMapper.countBlogCategory(name, isDelete, state);
    }

    @Override
    public boolean insertBlogCategory(BlogCategoryPO categoryPO) {
        if (categoryPO.getSort() == null) {
            categoryPO.setSort(99);
        }
        if (categoryPO.getCoverUrl() == null || "".equals(categoryPO.getCoverUrl())) {
            String defaultCover = (String) CacheHelper.get("CATEGORY_DEFAULT_COVER");
            categoryPO.setCoverUrl(defaultCover);
        }

        long currentUserId = JwtHelper.getCurrentUserId();

        // 创建人和修改人
        categoryPO.setCreateUser(currentUserId);
        categoryPO.setUpdateUser(currentUserId);
        return blogCategoryMapper.insertBlogCategory(categoryPO) > 0;
    }

    @Override
    public BlogCategoryPO findCategoryByName(String name) {
        return blogCategoryMapper.findCategoryByName(name);
    }

    @Override
    public BlogCategoryPO findCategoryById(long id) {
        return blogCategoryMapper.findCategoryById(id);
    }

    @Override
    public boolean updateIsDelete(long id, int isDelete) {
        return blogCategoryMapper.updateIsDelete(id, isDelete) > 0;
    }

    @Override
    public boolean updateState(long id, int state) {
        return blogCategoryMapper.updateState(id, state) > 0;
    }

    @Override
    public boolean updateCategory(BlogCategoryPO categoryPO) {
        if (categoryPO.getSort() == null) {
            categoryPO.setSort(99);
        }
        if (categoryPO.getCoverUrl() == null || "".equals(categoryPO.getCoverUrl())) {
            String defaultCover = (String) CacheHelper.get("CATEGORY_DEFAULT_COVER");
            categoryPO.setCoverUrl(defaultCover);
        }
        long currentUserId = JwtHelper.getCurrentUserId();
        categoryPO.setUpdateUser(currentUserId);
        return blogCategoryMapper.updateBlogCategory(categoryPO) > 0;
    }
}
