package cn.postwall.blog.service.impl;

import cn.postwall.blog.mapper.BlogUserMapper;
import cn.postwall.blog.pojo.constant.SysConstant;
import cn.postwall.blog.pojo.po.BlogUserPO;
import cn.postwall.blog.service.IBlogUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
@Service
public class BlogUserServiceImpl implements IBlogUserService {

    @Autowired
    private BlogUserMapper blogUserMapper;

    @Override
    public List<BlogUserPO> pageBlogUser(String username, String phone, String email, int isDelete, int curPage, int pageSize) {
        List<BlogUserPO> blogUserPOS = blogUserMapper.pageBlogUser(username, phone, email, isDelete, (curPage - 1) * pageSize, pageSize);
        for (BlogUserPO blogUserPO : blogUserPOS) {
            blogUserPO.convertRole();
        }
        return blogUserPOS;
    }

    @Override
    public int countBlogUser(String username, String phone, String email) {
        return blogUserMapper.countBlogUser(username, phone, email, SysConstant.IS_DELETE.ENABLE);
    }

    @Override
    public BlogUserPO findUserById(long userId) {
        return blogUserMapper.findUserById(userId);
    }

    @Override
    public BlogUserPO findUserByUserName(String username) {
        return blogUserMapper.findUserByUserName(username.trim());
    }

    @Override
    public BlogUserPO findUserByPhone(String phone) {
        return blogUserMapper.findUserByPhone(phone.trim());
    }

    @Override
    public BlogUserPO findUserByEmail(String email) {
        return blogUserMapper.findUserByEmail(email.trim());
    }

    @Override
    public boolean insertUser(BlogUserPO userPO) {
        return blogUserMapper.insertBlogUser(userPO) > 0;
    }

    @Override
    public boolean updateLoginTime(long userId) {
        return blogUserMapper.updateLoginTime(userId) > 0;
    }

    @Override
    public boolean updateIsDelete(long userId, int isDelete) {
        return blogUserMapper.updateIsDelete(userId, isDelete) > 0;
    }

    @Override
    public boolean updateBlogUser(BlogUserPO userPO) {
        return blogUserMapper.updateBlogUser(userPO) > 0;
    }
}
