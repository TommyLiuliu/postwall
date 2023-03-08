package cn.postwall.blog.service.impl;

import cn.postwall.blog.mapper.BlogArticleCommentMapper;
import cn.postwall.blog.mapper.BlogArticleMapper;
import cn.postwall.blog.pojo.dto.ArticleDTO;
import cn.postwall.blog.pojo.dto.UserInfo;
import cn.postwall.blog.pojo.po.BlogArticleCommentPO;
import cn.postwall.blog.pojo.po.BlogArticlePO;
import cn.postwall.blog.pojo.po.BlogUserPO;
import cn.postwall.blog.service.IBlogArticleCommentService;
import cn.postwall.blog.service.IBlogUserService;
import cn.postwall.blog.utils.BeanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/31 12:59:00
* @Description:
*/
@Service
public class BlogArticleCommentServiceImpl implements IBlogArticleCommentService {

    @Autowired
    private BlogArticleCommentMapper articleCommentMapper;

    @Autowired
    private IBlogUserService userService;

    @Autowired
    private BlogArticleMapper articleMapper;

    @Override
    public boolean comment(BlogArticleCommentPO commentPO) {
        return articleCommentMapper.insertBlogArticleComment(commentPO) > 0;
    }

    @Override
    public BlogArticleCommentPO findArticleCommentById(long commentId) {
        return articleCommentMapper.findArticleCommentById(commentId);
    }

    @Override
    public List<BlogArticleCommentPO> findCommentByArticleId(Integer articleId, Integer curPage, Integer pageSize) {
        List<BlogArticleCommentPO> articleCommentParentByArticleId = articleCommentMapper.findArticleCommentParentByArticleId(articleId, (curPage - 1) * pageSize, pageSize);
        for (BlogArticleCommentPO blogarticleCommentPO : articleCommentParentByArticleId) {
            // 用户信息
            BlogUserPO userById = userService.findUserById(blogarticleCommentPO.getUserId());
            blogarticleCommentPO.setUserInfo(BeanHelper.copyProperties(userById, UserInfo.class));
        }
        return articleCommentParentByArticleId;
    }

    @Override
    public Integer countArticleCommentParentByArticleId(Integer articleId) {
        return articleCommentMapper.countArticleCommentParentByArticleId(articleId);
    }

    @Override
    public List<BlogArticleCommentPO> pageArticleComment(Integer isDelete, int curPage, int pageSize) {
        List<BlogArticleCommentPO> blogArticleCommentPOS = articleCommentMapper.pageArticleComment(isDelete, (curPage - 1) * pageSize, pageSize);
        for (BlogArticleCommentPO blogArticleCommentPO : blogArticleCommentPOS) {
            BlogArticlePO articleById = articleMapper.findArticleById(blogArticleCommentPO.getArticleId());
            blogArticleCommentPO.setArticleDTO(BeanHelper.copyProperties(articleById, ArticleDTO.class));
            BlogUserPO userById = userService.findUserById(blogArticleCommentPO.getUserId());
            blogArticleCommentPO.setUserInfo(BeanHelper.copyProperties(userById, UserInfo.class));
        }
        return blogArticleCommentPOS;
    }

    @Override
    public Integer countArticleComment(Integer isDelete) {
        return articleCommentMapper.countArticleComment(isDelete);
    }

    @Override
    public boolean updateIsDelete(long id, int isDelete) {
        return articleCommentMapper.updateIsDelete(id, isDelete) > 0;
    }
}
