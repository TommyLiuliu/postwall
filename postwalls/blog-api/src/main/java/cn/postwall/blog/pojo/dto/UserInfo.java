package cn.postwall.blog.pojo.dto;

import cn.postwall.blog.annotation.CopyIgnore;
import lombok.Data;

/**
 * @author liuhanchao
 * @date 2022/12/11 3:17
 * @Description:
 */
@Data
public class UserInfo {

    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 角色 admin = 管理员  user = 用户
     */
    private String role;

    /**
     * 最近登录时间
     */
    private String lastTimeLogin;

    /**
     * 创建时间
     */
    private String createTime;

    @CopyIgnore
    private String base64;
}
