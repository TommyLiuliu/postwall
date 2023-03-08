package cn.postwall.blog.controller.user;

import cn.postwall.blog.pojo.Result;
import cn.postwall.blog.pojo.constant.SysConstant;
import cn.postwall.blog.pojo.po.BlogCategoryPO;
import cn.postwall.blog.service.IBlogCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuhanchao
 * @date 2022/12/20 16:56
 * @Description:
 */
@Controller
public class CategoryUserController {

    @Autowired
    private IBlogCategoryService categoryService;

    @GetMapping("/getCategoryList")
    @ResponseBody
    public Result getCategoryList() {
        int count = categoryService.countBlogCategory(null, SysConstant.IS_DELETE.ENABLE, 1);
        List<BlogCategoryPO> blogCategoryPOS = categoryService.pageBlogCategory(null, SysConstant.IS_DELETE.ENABLE,1, 1, count);
        List<Map> result = new ArrayList<>();
        Map map;
        for (BlogCategoryPO blogCategoryPO : blogCategoryPOS) {
            map = new HashMap();
            map.put("id", blogCategoryPO.getId());
            map.put("name", blogCategoryPO.getName());
            map.put("cover", blogCategoryPO.getCoverUrl());
            result.add(map);
        }
        return Result.success(result);
    }


}
