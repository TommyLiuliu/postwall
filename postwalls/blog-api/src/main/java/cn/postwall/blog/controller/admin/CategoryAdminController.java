package cn.postwall.blog.controller.admin;

import cn.postwall.blog.pojo.Result;
import cn.postwall.blog.pojo.constant.SysConstant;
import cn.postwall.blog.pojo.dto.CategoryDTO;
import cn.postwall.blog.pojo.po.BlogCategoryPO;
import cn.postwall.blog.service.IBlogCategoryService;
import cn.postwall.blog.utils.BeanHelper;
import cn.postwall.blog.utils.CacheHelper;
import cn.postwall.blog.utils.FileHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuhanchao
 * @date 2022/12/11 11:30
 * @Description:
 */

@Controller
@RequestMapping("/admin")
public class CategoryAdminController {

    @Autowired
    private IBlogCategoryService categoryService;


    @PostMapping("/pageCategory")
    @ResponseBody
    public Result pageCategory(String categoryName, int isDelete, @RequestParam("curPage") int curPage, @RequestParam("pageSize") int pageSize) {
        List<BlogCategoryPO> blogCategoryPOS = categoryService.pageBlogCategory(categoryName, isDelete, null, curPage, pageSize);
        if (blogCategoryPOS.size() > 0) {
            int count = categoryService.countBlogCategory(categoryName, isDelete , null);
            Map map = new HashMap<>();
            map.put("categories", blogCategoryPOS);
            map.put("count", count);
            return Result.success(map);
        }
        return Result.fail("没有找到数据");
    }

    @PostMapping("/insertCategory")
    @ResponseBody
    public Result insertCategory(@RequestBody CategoryDTO categoryDTO) {
        BlogCategoryPO categoryPO = BeanHelper.copyProperties(categoryDTO, BlogCategoryPO.class);
        if (categoryPO.getName() == null || "".equals(categoryPO.getName())) {
            return Result.fail("分类名称不能为空");
        }

        BlogCategoryPO categoryByName = categoryService.findCategoryByName(categoryPO.getName());
        if (categoryByName != null) {
            return Result.fail("分类【"+categoryPO.getName()+"】已存在，请重新输入");
        }

        // 判断上传的图片是否为base64编码
        String fileSuffix = FileHelper.isBase64(categoryDTO.getBase64());
        if (fileSuffix != null) {
            // 保存base64图片
            String coverUrl = FileHelper.saveBase64Image(categoryDTO.getBase64());
            if (coverUrl != null) {
                categoryPO.setCoverUrl(coverUrl);
            }
        }

        boolean flag = categoryService.insertBlogCategory(categoryPO);
        if (flag) {
            return Result.success("分类【"+categoryPO.getName()+"】添加成功");
        }

        return Result.fail(500, "分类【"+categoryPO.getName()+"】添加失败，请重试");
    }

    @PostMapping("/updateIsDelete")
    @ResponseBody
    public Result updateIsDelete(@RequestParam("id") long id, @RequestParam("isDelete") int isDelete) {
        BlogCategoryPO categoryById = categoryService.findCategoryById(id);
        if (categoryById == null) {
            return Result.fail("分类不存在");
        }

        boolean flag = categoryService.updateIsDelete(id, isDelete);

        String message;
        if (isDelete == SysConstant.IS_DELETE.ENABLE) {
            message = "分类[" + categoryById.getName() +"]恢复";
        } else {
            message = "分类[" + categoryById.getName() +"]删除";
        }

        if (flag) {
            return Result.success(message + "成功");
        }
        return Result.fail( message + "失败");
    }

    @PostMapping("/updateState")
    @ResponseBody
    public Result updateState(@RequestParam("id") long id, @RequestParam("state") int state) {
        BlogCategoryPO categoryById = categoryService.findCategoryById(id);
        if (categoryById == null) {
            return Result.fail("分类不存在");
        }

        boolean flag = categoryService.updateState(id, state);

        if (flag) {
            return Result.success("分类[" + categoryById.getName() +"]状态修改成功");
        }
        return Result.fail( "分类[" + categoryById.getName() +"]状态修改失败");
    }

    @PostMapping("/updateCategory")
    @ResponseBody
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO) {
        BlogCategoryPO categoryPO = BeanHelper.copyProperties(categoryDTO, BlogCategoryPO.class);
        if (categoryPO.getName() == null || "".equals(categoryPO.getName())) {
            return Result.fail("分类名称不能为空");
        }

        BlogCategoryPO categoryById = categoryService.findCategoryById(categoryDTO.getId());
        if (categoryById == null) {
            return Result.fail("分类不存在");
        }

        if (!categoryById.getName().equals(categoryPO.getName().trim())) {
            BlogCategoryPO categoryByName = categoryService.findCategoryByName(categoryPO.getName().trim());
            if (categoryByName != null) {
                return Result.fail(500, "分类【"+categoryPO.getName()+"】已存在，请重新输入");
            }
        }

        String fileSuffix = FileHelper.isBase64(categoryDTO.getBase64());
        // 保存Base64编码文件
        if (fileSuffix != null) {
            String coverUrl = FileHelper.saveBase64Image(categoryDTO.getBase64());
            // 删除原有的图片
            String srcCoverUrl = categoryPO.getCoverUrl();
            if (srcCoverUrl.contains(SysConstant.API_IMAGE)) {
                String defaultCover = (String) CacheHelper.get("CATEGORY_DEFAULT_COVER");
                // 判断不是默认图片
                if (!defaultCover.equals(srcCoverUrl)) {
                    String imagesPath = String.valueOf(CacheHelper.get("IMAGES_PATH"));
                    FileHelper.deleteFile(imagesPath + File.separator + srcCoverUrl.replace(SysConstant.API_IMAGE, ""));
                }
            }
            categoryPO.setCoverUrl(coverUrl);
        }

        Boolean flag = categoryService.updateCategory(categoryPO);
        if (flag) {
            if (categoryById.getName().equals(categoryPO.getName().trim())) {
                return Result.success("分类【" + categoryById.getName() + "】更新成功");
            }
            return Result.success("分类【"+categoryById.getName()+"】成功修改为【"+categoryPO.getName()+"】");
        }
        return Result.fail("分类【" + categoryById.getName() + "】更新失败");

    }
}
