package cn.postwall.blog.service.impl;

import cn.postwall.blog.mapper.BlogArticleMapper;
import cn.postwall.blog.mapper.BlogArticleTagMapper;
import cn.postwall.blog.pojo.po.*;
import cn.postwall.blog.service.IBlogArticleTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/26 11:10:03
* @Description:
*/
@Service
public class BlogArticleTagServiceImpl implements IBlogArticleTagService {

    @Autowired
    private BlogArticleTagMapper blogArticleTagMapper;

    @Autowired
    private BlogArticleMapper blogArticleMapper;

    @Override
    public boolean saveArticleTag(BlogArticleTagPO articleTagPO) {
        return blogArticleTagMapper.insertBlogArticleTag(articleTagPO) > 0;
    }

    @Override
    public int countBlogArticleTag(Long tagId) {
        return blogArticleTagMapper.countBlogArticleTag(tagId);
    }

    @Override
    public List<BlogArticleTagPO> findTagByArticle(long articleId) {
        return blogArticleTagMapper.findTagByArticle(articleId);
    }

    @Override
    public boolean deleteTagById(long id) {
        return blogArticleTagMapper.deleteTagById(id) > 0;
    }

}
