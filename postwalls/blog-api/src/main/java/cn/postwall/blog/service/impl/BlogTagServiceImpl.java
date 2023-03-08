package cn.postwall.blog.service.impl;

import cn.postwall.blog.mapper.BlogArticleTagMapper;
import cn.postwall.blog.mapper.BlogTagMapper;
import cn.postwall.blog.mapper.BlogUserMapper;
import cn.postwall.blog.pojo.po.BlogTagPO;
import cn.postwall.blog.pojo.po.BlogUserPO;
import cn.postwall.blog.service.IBlogTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
@Service
public class BlogTagServiceImpl implements IBlogTagService {

    @Autowired
    private BlogTagMapper blogTagMapper;

    @Autowired
    private BlogUserMapper userMapper;

    @Autowired
    private BlogArticleTagMapper articleTagMapper;

    @Override
    public List<BlogTagPO> pageBlogTag(String name, int curPage, int pageSize) {
        List<BlogTagPO> blogTagPOS = blogTagMapper.pageBlogTag(name, (curPage - 1) * pageSize, pageSize);
        for (BlogTagPO blogTagPO : blogTagPOS) {
            BlogUserPO createUser = userMapper.findUserById(blogTagPO.getCreateUser());
            blogTagPO.setCreateUserName(createUser.getUsername());
        }
        return blogTagPOS;
    }

    @Override
    public int countBlogTag(String name) {
        return blogTagMapper.countBlogTag(name);
    }

    @Override
    public boolean insertBlogTag(BlogTagPO tagPO) {
        return blogTagMapper.insertBlogTag(tagPO) > 0;
    }

    @Override
    public boolean deleteBlogTag(long id) {
        articleTagMapper.deleteTagByTagId(id);
        return blogTagMapper.deleteBlogTag(id) > 0;
    }

    @Override
    public BlogTagPO findTagById(long id) {
        return blogTagMapper.findTagById(id);
    }

    @Override
    public BlogTagPO findTagByName(String name) {
        return blogTagMapper.findTagByName(name);
    }
}
