package cn.postwall.blog.pojo.dto;

import lombok.Data;

/**
 * @author liuhanchao
 * @date 2022/12/11 1:08
 * @Description:
 */
@Data
public class RegisterDTO {

    private String username;

    private String password;

    private String phone;

    private String email;

    private String emailVerify;

    public boolean isNotEmpty() {
        if (this.username == null || "".equals(this.username)) {
            return false;
        }
        if (this.password == null || "".equals(this.password)) {
            return false;
        }
        if (this.phone == null || "".equals(this.phone)) {
            return false;
        }
        if (this.email == null || "".equals(this.email)) {
            return false;
        }
        if (this.emailVerify == null || "".equals(this.emailVerify)) {
            return false;
        }
        return true;
    }

}
