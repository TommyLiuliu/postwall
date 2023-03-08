package cn.postwall.blog.mapper;

import org.apache.ibatis.annotations.Param;
import cn.postwall.blog.pojo.po.BlogUserPO;

import java.util.List;

/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
public interface BlogUserMapper {

    /**
     * 分页查询用户数据
     * @param username
     * @param phone
     * @param email
     * @param curPage
     * @param pageSize
     * @return
     */
    List<BlogUserPO> pageBlogUser(@Param("username") String username,
                                  @Param("phone") String phone,
                                  @Param("email") String email,
                                  @Param("isDelete") int isDelete,
                                  @Param("curPage") int curPage,
                                  @Param("pageSize") int pageSize);

    /**
     * 获取查询用户数
     * @param username
     * @param phone
     * @param email
     * @return
     */
    int countBlogUser(@Param("username") String username,
                      @Param("phone") String phone,
                      @Param("email") String email,
                      @Param("isDelete") int isDelete);

    int insertBlogUser(@Param("blogUser") BlogUserPO blogUser);

    int updateBlogUser(@Param("blogUser") BlogUserPO blogUser);

    /**
     * 通过id 查询用户信息
     * @param userId
     * @return
     */
    BlogUserPO findUserById(@Param("userId") long userId);


    /**
     * 通过用户名查询用户信息
     * @param username  用户名
     * @return
     */
    BlogUserPO findUserByUserName(@Param("username") String username);

    /**
     * 根据手机号查询用户信息
     * @param phone 手机号
     * @return
     */
    BlogUserPO findUserByPhone(@Param("phone") String phone);

    /**
     * 根据邮箱查询用户信息
     * @param email 邮箱
     * @return
     */
    BlogUserPO findUserByEmail(@Param("email") String email);

    /**
     * 更新登录时间
     * @param userId
     * @return
     */
    int updateLoginTime(@Param("userId") long userId);

    /**
     * 软删除用户
     */
    int updateIsDelete(@Param("userId") long userId, @Param("isDelete") int isDelete);

}
