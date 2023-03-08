package cn.postwall.blog.controller.admin;

import cn.postwall.blog.mapper.BlogTagMapper;
import cn.postwall.blog.pojo.Result;
import cn.postwall.blog.pojo.po.BlogTagPO;
import cn.postwall.blog.service.IBlogTagService;
import cn.postwall.blog.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuhanchao
 * @date 2022/12/11 15:38
 * @Description:
 */

@Controller
@RequestMapping("/admin")
public class TagAdminController {

    @Autowired
    private IBlogTagService tagService;

    @PostMapping("/pageTags")
    @ResponseBody
    public Result pageTags(String name, @RequestParam("curPage") int curPage, @RequestParam("pageSize") int pageSize) {
        List<BlogTagPO> blogTagPOS = tagService.pageBlogTag(name, curPage, pageSize);
        if (blogTagPOS.size() > 0) {
            int count = tagService.countBlogTag(name);
            Map map = new HashMap<>();
            map.put("tags", blogTagPOS);
            map.put("count", count);
            return Result.success(map);
        }
        return Result.fail("没有找到数据");
    }

    @PostMapping("/insertTag")
    @ResponseBody
    public Result insertTag(@RequestParam("name") String name) {
        if (name == null || "".equals(name)) {
            return Result.fail("标签名称不能为空");
        }
        BlogTagPO tagByName = tagService.findTagByName(name);
        if (tagByName != null) {
            return Result.fail("标签【"+tagByName.getName()+"】已存在，请重新输入");
        }

        BlogTagPO blogTagPO = new BlogTagPO();
        blogTagPO.setName(name);
        blogTagPO.setCreateUser(JwtHelper.getCurrentUserId());

        boolean flag = tagService.insertBlogTag(blogTagPO);
        if (flag) {
            return Result.success("标签【"+ name +"】添加成功");
        }
        return Result.fail("标签【"+ name +"】添加失败，请重试");
    }

    @GetMapping("/deleteTag")
    @ResponseBody
    public Result deleteTag(@RequestParam("id") long id) {
        BlogTagPO tagById = tagService.findTagById(id);
        if (tagById == null) {
            return Result.fail("标签不存在");
        }
        boolean flag = tagService.deleteBlogTag(id);
        if (flag) {
            return Result.success("标签【"+ tagById.getName() +"】删除成功");
        }
        return Result.fail("标签【"+ tagById.getName() +"】删除失败，请重试");
    }

}
