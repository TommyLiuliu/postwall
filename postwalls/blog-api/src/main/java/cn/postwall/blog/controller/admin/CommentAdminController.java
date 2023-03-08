package cn.postwall.blog.controller.admin;

import cn.postwall.blog.pojo.Result;
import cn.postwall.blog.pojo.constant.SysConstant;
import cn.postwall.blog.pojo.po.BlogArticleCommentPO;
import cn.postwall.blog.service.IBlogArticleCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuhanchao
 * @date 2023/1/1 13:12
 * @Description:
 */
@Controller
@RequestMapping("/admin")
public class CommentAdminController {

    @Autowired
    private IBlogArticleCommentService commentService;

    @PostMapping("/pageArticleComment")
    @ResponseBody
    public Result pageArticleComment(Integer isDelete, @RequestParam("curPage") Integer curPage, @RequestParam("pageSize") Integer pageSize) {
        List<BlogArticleCommentPO> articleComments = commentService.pageArticleComment(isDelete, curPage, pageSize);
        if (articleComments.size() > 0) {
            Integer count = commentService.countArticleComment(isDelete);
            Map<String, Object> map = new HashMap<>();
            map.put("comments", articleComments);
            map.put("count", count);
            return Result.success(map);
        }
        return Result.fail("没有找到数据");
    }

    @GetMapping("/updateCommentIsDelete")
    @ResponseBody
    public Result updateCommentIsDelete(@RequestParam("id") long id, @RequestParam("isDelete") int isDelete) {
        BlogArticleCommentPO articleCommentById = commentService.findArticleCommentById(id);
        if (articleCommentById == null) {
            return Result.fail("评论不存在");
        }

        boolean flag = commentService.updateIsDelete(id, isDelete);

        String message;
        if (isDelete == SysConstant.IS_DELETE.ENABLE) {
            message = "评论恢复";
        } else {
            message = "评论删除";
        }

        if (flag) {
            return Result.success(message + "成功");
        }
        return Result.fail( message + "失败");
    }
}
