package cn.postwall.blog.pojo.po;

import cn.postwall.blog.annotation.CopyIgnore;
import lombok.Data;
/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
@Data
public class BlogTagPO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 创建人
     */
    private Long createUser;

    @CopyIgnore
    private String createUserName;

    /**
     * 创建时间
     */
    private String createTime;

}
