package cn.postwall.blog.service.impl;

import cn.postwall.blog.mapper.*;
import cn.postwall.blog.pojo.constant.SysConstant;
import cn.postwall.blog.pojo.po.*;
import cn.postwall.blog.service.IBlogArticleService;
import cn.postwall.blog.utils.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
@Service
public class BlogArticleServiceImpl implements IBlogArticleService {

    @Autowired
    private BlogArticleMapper blogArticleMapper;

    @Autowired
    private BlogCategoryMapper categoryMapper;

    @Autowired
    private BlogUserMapper userMapper;

    @Autowired
    private BlogArticleTagMapper articleTagMapper;

    @Autowired
    private BlogTagMapper tagMapper;

    @Override
    public boolean insertArticle(BlogArticlePO blogArticlePO) {
        return blogArticleMapper.insertBlogArticle(blogArticlePO) > 0;
    }

    @Override
    public boolean updateBlogArticle(BlogArticlePO blogArticlePO) {
        return blogArticleMapper.updateBlogArticle(blogArticlePO) > 0;
    }

    @Override
    public List<BlogArticlePO> pageUserArticles(String title, Long categoryId, Long userId, int curPage, int pageSize) {
        List<BlogArticlePO> blogArticlePOS = blogArticleMapper.pageBlogArticle(title, categoryId, userId, null, null, SysConstant.IS_DELETE.ENABLE, (curPage - 1) * pageSize, pageSize);
        for (BlogArticlePO blogArticlePO : blogArticlePOS) {
            // 分类
            BlogCategoryPO categoryById = categoryMapper.findCategoryById(blogArticlePO.getCategoryId());
            blogArticlePO.setCategoryName(categoryById.getName());
            // 创建人
            BlogUserPO createUser = userMapper.findUserById(blogArticlePO.getCreateUser());
            blogArticlePO.setCreateUserName(createUser.getNickname());
            blogArticlePO.setCreateUserAvatar(createUser.getAvatar());
            // 标签
            List<String> tags = new ArrayList<>();
            List<BlogArticleTagPO> tagByArticle = articleTagMapper.findTagByArticle(blogArticlePO.getId());
            for (BlogArticleTagPO articleTagPO : tagByArticle) {
                BlogTagPO tagById = tagMapper.findTagById(articleTagPO.getTagId());
                tags.add(tagById.getName());
            }
            blogArticlePO.setTags(tags.toArray(new String[0]));
        }
        return blogArticlePOS;
    }

    @Override
    public List<BlogArticlePO> pageAdminArticles(String title, Long categoryId, String startTime, String endTime, int isDelete, int curPage, int pageSize) {
        List<BlogArticlePO> blogArticlePOS = blogArticleMapper.pageBlogArticle(title, categoryId, null, startTime, endTime, isDelete, (curPage - 1) * pageSize, pageSize);
        for (BlogArticlePO blogArticlePO : blogArticlePOS) {
            // 分类
            BlogCategoryPO categoryById = categoryMapper.findCategoryById(blogArticlePO.getCategoryId());
            blogArticlePO.setCategoryName(categoryById.getName());
            // 创建人
            BlogUserPO createUser = userMapper.findUserById(blogArticlePO.getCreateUser());
            blogArticlePO.setCreateUserName(createUser.getNickname());
            // 修改人
            BlogUserPO userById = userMapper.findUserById(blogArticlePO.getUpdateUser());
            blogArticlePO.setUpdateUserName(userById.getUsername());
        }
        return blogArticlePOS;
    }

    @Override
    public int countUserArticles(String title, Long categoryId, Long userId) {
        return blogArticleMapper.countBlogArticle(title, categoryId, userId, null, null, SysConstant.IS_DELETE.ENABLE);
    }

    @Override
    public int countAdminArticles(String title, Long categoryId, String startTime, String endTime, int isDelete) {
        return blogArticleMapper.countBlogArticle(title, categoryId, null, startTime, endTime, isDelete);
    }

    @Override
    public List<BlogArticlePO> pageBlogArticleTag(Long tagId, int curPage, int pageSize) {
        List<BlogArticleTagPO> blogArticleTagPOS = articleTagMapper.pageBlogArticleTag(tagId, (curPage - 1) * pageSize, pageSize);
        List<BlogArticlePO> result = new ArrayList<>();
        for (BlogArticleTagPO blogArticleTagPO : blogArticleTagPOS) {
            BlogArticlePO articleById = blogArticleMapper.findArticleById(blogArticleTagPO.getArticleId());
            if (articleById != null && SysConstant.IS_DELETE.ENABLE == articleById.getIsDelete()) {
                // 分类
                BlogCategoryPO categoryById = categoryMapper.findCategoryById(articleById.getCategoryId());
                articleById.setCategoryName(categoryById.getName());
                // 创建人
                BlogUserPO createUser = userMapper.findUserById(articleById.getCreateUser());
                articleById.setCreateUserName(createUser.getNickname());
                articleById.setCreateUserAvatar(createUser.getAvatar());
                // 标签
                List<String> tags = new ArrayList<>();
                List<BlogArticleTagPO> tagByArticle = articleTagMapper.findTagByArticle(articleById.getId());
                for (BlogArticleTagPO articleTagPO : tagByArticle) {
                    BlogTagPO tagById = tagMapper.findTagById(articleTagPO.getTagId());
                    tags.add(tagById.getName());
                }
                articleById.setTags(tags.toArray(new String[0]));
                result.add(articleById);
            }
        }
        return result;
    }

    @Override
    public BlogArticlePO findArticleByLink(String link) {
        BlogArticlePO articleByLink = blogArticleMapper.findArticleByLink(link);
        if (articleByLink == null) {
            return null;
        }
        // 分类
        BlogCategoryPO categoryById = categoryMapper.findCategoryById(articleByLink.getCategoryId());
        articleByLink.setCategoryName(categoryById.getName());
        // 创建人
        BlogUserPO createUser = userMapper.findUserById(articleByLink.getCreateUser());
        articleByLink.setCreateUserName(createUser.getNickname());
        articleByLink.setCreateUserAvatar(createUser.getAvatar());
        // 标签
        List<String> tags = new ArrayList<>();
        List<BlogArticleTagPO> tagByArticle = articleTagMapper.findTagByArticle(articleByLink.getId());
        for (BlogArticleTagPO articleTagPO : tagByArticle) {
            BlogTagPO tagById = tagMapper.findTagById(articleTagPO.getTagId());
            tags.add(tagById.getName());
        }
        articleByLink.setTags(tags.toArray(new String[0]));
        return articleByLink;
    }

    @Override
    public BlogArticlePO findArticleById(long articleId) {
        return blogArticleMapper.findArticleById(articleId);
    }

    @Override
    public int findArticleByTitle(String title) {
        return blogArticleMapper.findArticleByTitle(title);
    }

    @Override
    public boolean updateIsDelete(long articleId, int isDelete) {
        return blogArticleMapper.updateIsDelete(articleId, isDelete) > 0;
    }

    @Override
    public boolean increaseReading(long articleId) {
        return blogArticleMapper.increaseReading(articleId) > 0;
    }

    @Override
    public boolean increasePraise(long articleId, boolean flag) {
        return blogArticleMapper.increasePraise(articleId, flag) > 0;
    }

    @Override
    public boolean updateCommentNum(long articleId, boolean flag) {
        return blogArticleMapper.updateCommentNum(articleId, flag) > 0;
    }

    @Override
    public boolean updateArticleState(long articleId, Integer publishState, Integer commentState, Integer topState) {
        return blogArticleMapper.updateArticleState(articleId, publishState, commentState, topState) > 0;
    }
}
