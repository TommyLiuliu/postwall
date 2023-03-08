package cn.postwall.blog.controller.user;

import cn.postwall.blog.pojo.Result;
import cn.postwall.blog.pojo.constant.CacheConstant;
import cn.postwall.blog.pojo.constant.SysConstant;
import cn.postwall.blog.pojo.dto.UserInfo;
import cn.postwall.blog.pojo.po.BlogUserPO;
import cn.postwall.blog.service.IBlogUserService;
import cn.postwall.blog.utils.BeanHelper;
import cn.postwall.blog.utils.CacheHelper;
import cn.postwall.blog.utils.FileHelper;
import cn.postwall.blog.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;

/**
 * @author liuhanchao
 * @date 2022/12/29 10:29
 * @Description:
 */
@Controller
public class UserController {

    @Autowired
    private IBlogUserService userService;


    @PostMapping("/updateEmail")
    @ResponseBody
    public Result updateEmail(@RequestParam("email") String email, @RequestParam("verify") String verify) {
        String redisVerify = CacheHelper.getExpire(CacheConstant.EMAIL_KEY + email);
        if (redisVerify == null || !redisVerify.equalsIgnoreCase(verify)) {
            return Result.fail("验证码不正确，请重新输入");
        }
        long currentUserId = JwtHelper.getCurrentUserId();
        if (currentUserId == -1) {
            return Result.fail("请先登录");
        }
        BlogUserPO userById = userService.findUserById(currentUserId);
        if (userById == null) {
            return Result.fail("用户不存在");
        }
        userById.setEmail(email);
        boolean flag = userService.updateBlogUser(userById);
        if (flag) {
            UserInfo userInfo = BeanHelper.copyProperties(userById, UserInfo.class);
            return Result.success(userInfo,"邮箱更新成功");
        }
        return Result.fail("邮箱更新失败");
    }

    @PostMapping("/modifyPassword")
    @ResponseBody
    public Result modifyPassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        long currentUserId = JwtHelper.getCurrentUserId();
        if (currentUserId == -1) {
            return Result.fail("请先登录");
        }
        BlogUserPO userById = userService.findUserById(currentUserId);
        if (userById == null ) {
            return Result.fail("用户不存在");
        }
        if (!userById.getPassword().equals(DigestUtils.md5DigestAsHex(oldPassword.getBytes()))) {
            return Result.fail("原密码不正确");
        }
        userById.setPassword(DigestUtils.md5DigestAsHex(newPassword.getBytes()));
        boolean flag = userService.updateBlogUser(userById);
        if (flag) {
            return Result.success("修改密码成功");
        }
        return Result.fail("修改密码失败");
    }

    @PostMapping("/updateUserInfo")
    @ResponseBody
    public Result updateUserInfo(@RequestBody UserInfo userInfo) {
        long currentUserId = JwtHelper.getCurrentUserId();
        if (currentUserId == -1) {
            return Result.fail("请先登录");
        }
        BlogUserPO userById = userService.findUserById(currentUserId);
        if (userById == null ) {
            return Result.fail("用户不存在");
        }

        userById.setNickname(userInfo.getNickname());
        userById.setPhone(userInfo.getPhone());

        // 头像
        String fileSuffix = FileHelper.isBase64(userInfo.getBase64());
        if (fileSuffix != null) {
            String avatar = FileHelper.saveBase64Image(userInfo.getBase64());
            // 删除原有的图片
            String srcAvatar = userById.getAvatar();
            if (srcAvatar.contains(SysConstant.API_IMAGE)) {
                String defaultCover = (String) CacheHelper.get("USER_DEFAULT_AVATAR");
                // 判断不是默认图片
                if (!defaultCover.equals(srcAvatar)) {
                    String imagesPath = String.valueOf(CacheHelper.get("IMAGES_PATH"));
                    FileHelper.deleteFile(imagesPath + File.separator + srcAvatar.replace(SysConstant.API_IMAGE, ""));
                }
            }
            userById.setAvatar(avatar);
        }

        boolean flag = userService.updateBlogUser(userById);
        if (flag) {
            UserInfo updateUpdateInfo = BeanHelper.copyProperties(userById, UserInfo.class);
            return Result.success(updateUpdateInfo,"用户信息更新成功");
        }

        return Result.fail("用户信息更新失败");
    }

}
