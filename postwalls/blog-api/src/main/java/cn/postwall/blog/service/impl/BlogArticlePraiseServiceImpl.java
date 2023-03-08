package cn.postwall.blog.service.impl;

import cn.postwall.blog.mapper.BlogArticlePraiseMapper;
import cn.postwall.blog.pojo.po.BlogArticlePraisePO;
import cn.postwall.blog.service.IBlogArticlePraiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author liuhanchao
* @date 2023/02/20 10:27:24
* @Description:
*/
@Service
public class BlogArticlePraiseServiceImpl implements IBlogArticlePraiseService {

    @Autowired
    private BlogArticlePraiseMapper blogArticlePraiseMapper;

    @Override
    public int insertBlogArticlePraise(BlogArticlePraisePO blogArticlePraise) {
        return blogArticlePraiseMapper.insertBlogArticlePraise(blogArticlePraise);
    }

    @Override
    public int countPraiseByArticleId(Long articleId) {
        return blogArticlePraiseMapper.countPraiseByArticleId(articleId);
    }

    @Override
    public BlogArticlePraisePO findArticlePraise(Long articleId, Long userId) {
        return blogArticlePraiseMapper.findArticlePraise(articleId, userId);
    }

    @Override
    public boolean cancelPraise(Long id) {
        return blogArticlePraiseMapper.cancelPraise(id) > 0;
    }
}
