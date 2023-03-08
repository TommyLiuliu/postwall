package cn.postwall.blog.controller.admin;

import cn.postwall.blog.pojo.Result;
import cn.postwall.blog.pojo.constant.SysConstant;
import cn.postwall.blog.pojo.po.BlogArticlePO;
import cn.postwall.blog.service.IBlogArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuhanchao
 * @date 2022/12/27 17:12
 * @Description:
 */
@Controller
@RequestMapping("/admin")
public class ArticleAdminController {

    @Autowired
    private IBlogArticleService articleService;

    @PostMapping("/pageAdminArticles")
    @ResponseBody
    public Result pageAdminArticles(String title, String startTime, String endTime, int isDelete, @RequestParam("curPage") int curPage, @RequestParam("pageSize") int pageSize) {
        List<BlogArticlePO> blogArticlePOS = articleService.pageAdminArticles(title, null, startTime, endTime, isDelete, curPage, pageSize);
        if (blogArticlePOS.size() > 0) {
            int count = articleService.countAdminArticles(title, null, startTime, endTime, isDelete);
            Map map = new HashMap<>();
            map.put("articles", blogArticlePOS);
            map.put("count", count);
            return Result.success(map);
        }
        return Result.fail("没有找到数据");
    }

    @GetMapping("/updateArticleIsDelete")
    @ResponseBody
    public Result updateArticleIsDelete(@RequestParam("id") long id, @RequestParam("isDelete") int isDelete) {
        BlogArticlePO articleById = articleService.findArticleById(id);
        if (articleById == null) {
            return Result.fail("文章不存在，无法删除");
        }

        String message;
        if (isDelete == SysConstant.IS_DELETE.ENABLE) {
            message = "文章[" + articleById.getTitle() +"]恢复";
        } else {
            message = "文章[" + articleById.getTitle() +"]删除";
        }

        boolean flag = articleService.updateIsDelete(id, isDelete);

        if (flag) {
            return Result.success(message + "成功");
        }
        return Result.fail( message + "失败");
    }

    @GetMapping("/updateArticleState")
    @ResponseBody
    public Result updateArticleState(@RequestParam("articleId") long articleId, Integer publishState, Integer commentState, Integer topState) {
        BlogArticlePO articleById = articleService.findArticleById(articleId);
        if (articleById == null) {
            return Result.fail("文章不存在");
        }

        boolean flag = false;
        String message = "";

        if (publishState != null) {
            flag = articleService.updateArticleState(articleId, publishState, null, null);
            message = "发表状态";
        } else if (commentState != null){
            flag = articleService.updateArticleState(articleId, null, commentState, null);
            message = "评论状态";
        } else if (topState != null) {
            flag = articleService.updateArticleState(articleId, null, null, topState);
            message = "置顶状态";
        } else {
            return  Result.fail("参数有误");
        }

        if (flag) {
            return Result.success("文章["+articleById.getTitle()+"]" + message +"修改成功");
        }
        return Result.fail("文章["+articleById.getTitle()+"]" + message +"修改失败");
    }

}
