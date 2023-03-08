package cn.postwall.blog.service;

import cn.postwall.blog.pojo.po.BlogUserPO;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
public interface IBlogUserService {


    /**
     *  分页查询用户
     */
    List<BlogUserPO> pageBlogUser(String username, String phone, String email, int isDelete, int curPage, int pageSize);

    /**
     * 分页查询总数
     */
    int countBlogUser(String username, String phone, String email);

    /**
     * 通过id 查询用户信息
     * @param userId
     * @return
     */
    BlogUserPO findUserById(long userId);


    /**
     * 通过用户名查询用户信息
     * @param username  用户名
     * @return
     */
    BlogUserPO findUserByUserName(String username);

    /**
     * 根据手机号查询用户信息
     * @param phone 手机号
     * @return
     */
    BlogUserPO findUserByPhone(String phone);

    /**
     * 根据邮箱查询用户信息
     * @param email 邮箱
     * @return
     */
    BlogUserPO findUserByEmail(String email);


    /**
     * 添加用户
     */
    boolean insertUser(BlogUserPO userPO);

    /**
     * 更新登录时间
     * @param userId
     * @return
     */
    boolean updateLoginTime(long userId);

    /**
     * 软删除用户
     */
    boolean updateIsDelete(long userId, int isDelete);

    /**
     * 修改用户
     */
    boolean updateBlogUser(BlogUserPO userPO);

}
