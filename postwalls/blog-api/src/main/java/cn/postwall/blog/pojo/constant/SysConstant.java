package cn.postwall.blog.pojo.constant;

/**
 * @author liuhanchao
 * @date 2022/12/11 2:58
 * @Description:
 */
public class SysConstant {

    /**
     * 角色
     */
    public interface ROLE {
        /** 用户 **/
        String USER = "user";

        /** 管理员 **/
        String ADMIN = "admin";

        /** 系统管理员 **/
        String SYS_ADMIN = "sysadmin";
    }

    /**
     * 是否删除
     */
    public interface IS_DELETE {
        /** 可用 **/
        int ENABLE = 0;
        /** 删除 **/
        int DISABLE = 1;
    }

    public static final String AUTHORIZATION = "Authorization";

    /**
     * 视频Api前缀
     */
    public static final String API_IMAGE = "/api/image/";
}
