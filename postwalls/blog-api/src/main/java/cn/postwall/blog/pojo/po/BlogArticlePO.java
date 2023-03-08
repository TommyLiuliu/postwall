package cn.postwall.blog.pojo.po;

import cn.postwall.blog.annotation.CopyIgnore;
import lombok.Data;
/**
* @author liuhanchao
* @date 2022/12/11 00:52:04
* @Description:
*/
@Data
public class BlogArticlePO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 分类 id
     */
    private Long categoryId;

    @CopyIgnore
    private String categoryName;

    /**
     * 标题
     */
    private String title;

    /**
     * 文章链接
     */
    private String link;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 内容
     */
    private String content;

    /**
     * 封面图片地址
     */
    private String coverUrl;

    /**
     * 发表年份
     */
    private String year;

    /**
     * 发表月份
     */
    private String month;

    /**
     * 发表日期
     */
    private String day;

    /**
     * 发表时间（yyyy-MM-dd）
     */
    private String publishDate;

    /**
     * 是否发表
     */
    private Integer isPublish;

    /**
     * 是否允许评论
     */
    private Integer isComment;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 阅读数
     */
    private Long readNum;

    /**
     * 点赞数
     */
    private Long praiseNum;

    /**
     * 评论数
     */
    private Long commentNum;

    /**
     * 置顶时间
     */
    private String topTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 创建人
     */
    private Long createUser;
    @CopyIgnore
    private String createUserName;
    @CopyIgnore
    private String createUserAvatar;

    /**
     * 修改人
     */
    private Long updateUser;
    @CopyIgnore
    private String updateUserName;

    /**
     * 修改时间
     */
    private String updateTime;

    @CopyIgnore
    private String[] tags;

}
