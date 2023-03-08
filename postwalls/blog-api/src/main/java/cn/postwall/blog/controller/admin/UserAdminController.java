package cn.postwall.blog.controller.admin;

import cn.postwall.blog.pojo.Result;
import cn.postwall.blog.pojo.constant.SysConstant;
import cn.postwall.blog.pojo.po.BlogUserPO;
import cn.postwall.blog.service.IBlogUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuhanchao
 * @date 2022/12/11 4:10
 * @Description:
 */

@Controller
@RequestMapping("/admin")
public class UserAdminController {

    @Autowired
    private IBlogUserService userService;



    @GetMapping("/pageUsers")
    @ResponseBody
    public Result pageUsers(String username, String phone, String email, int isDelete, @RequestParam("curPage") int curPage, @RequestParam("pageSize") int pageSize) {
        List<BlogUserPO> blogUserPOS = userService.pageBlogUser(username, phone, email, isDelete, curPage, pageSize);
        if (blogUserPOS.size() > 0) {
            int count = userService.countBlogUser(username, phone, email);
            Map map = new HashMap();
            map.put("users", blogUserPOS);
            map.put("count", count);
            return Result.success(map);
        }
        return Result.fail("没有找到数据");
    }


    @GetMapping("/updateUserIsDelete")
    @ResponseBody
    public Result updateUserIsDelete(@RequestParam("userId") long userId, @RequestParam("isDelete") int isDelete) {
        BlogUserPO userById = userService.findUserById(userId);
        if (userById == null) {
            return Result.fail("用户不存在");
        }
        if (SysConstant.ROLE.SYS_ADMIN.equals(userById.getRole())) {
            return Result.fail("无法删除系统管理员");
        }
        boolean flag = userService.updateIsDelete(userId, isDelete);

        String message;
        if (isDelete == SysConstant.IS_DELETE.ENABLE) {
            message = "用户[" + userById.getUsername() +"]恢复";
        } else {
            message = "用户[" + userById.getUsername() +"]删除";
        }

        if (flag) {
            return Result.success(message + "成功");
        }
        return Result.fail( message + "失败");
    }


}
