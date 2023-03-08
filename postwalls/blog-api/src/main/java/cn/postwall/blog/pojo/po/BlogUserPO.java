package cn.postwall.blog.pojo.po;

import cn.postwall.blog.pojo.constant.SysConstant;
import lombok.Data;
/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
@Data
public class BlogUserPO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

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
     * 是否删除
     */
    private Integer isDelete;

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

    /**
     * 修改时间
     */
    private String updateTime;

    public void convertRole() {
        this.role = RoleEnum.getRoleName(this.role);
    }


}

/**
 * 角色枚举
 */
enum RoleEnum {

    SYS_ADMIN(SysConstant.ROLE.SYS_ADMIN, "系统管理员"),
    ADMIN(SysConstant.ROLE.ADMIN, "管理员"),
    USER(SysConstant.ROLE.USER, "用户");



    private String role;

    private String roleName;

    RoleEnum(String role, String roleName) {
        this.role = role;
        this.roleName = roleName;
    }

    static String getRoleName(String role) {
        for (RoleEnum value : RoleEnum.values()) {
            if (value.role.equals(role)) {
                return value.roleName;
            }
        }
        return "";
    }

}
