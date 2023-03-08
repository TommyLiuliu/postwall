package cn.postwall.blog.controller.user;

import cn.postwall.blog.pojo.Result;
import cn.postwall.blog.pojo.po.BlogTagPO;
import cn.postwall.blog.service.IBlogTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuhanchao
 * @date 2022/12/19 9:44
 * @Description:
 */
@Controller
public class TagUserAdmin {

    @Autowired
    private IBlogTagService tagService;


    @GetMapping("/getTagList")
    @ResponseBody
    public Result getTagList() {
        List<Map> result = new ArrayList<>();
        int count = tagService.countBlogTag(null);
        List<BlogTagPO> blogTagPOS = tagService.pageBlogTag(null, 1, count);
        Map map;
        for (BlogTagPO blogTagPO : blogTagPOS) {
            map = new HashMap();
            map.put("id", blogTagPO.getId());
            map.put("name", blogTagPO.getName());
            result.add(map);
        }
        return Result.success(result);
    }
}
