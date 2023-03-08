package cn.postwall.blog.controller.user;

import cn.postwall.blog.pojo.Result;
import cn.postwall.blog.pojo.constant.CacheConstant;
import cn.postwall.blog.pojo.constant.SysConstant;
import cn.postwall.blog.pojo.dto.RegisterDTO;
import cn.postwall.blog.pojo.dto.UserInfo;
import cn.postwall.blog.pojo.po.BlogUserPO;
import cn.postwall.blog.service.IBlogUserService;
import cn.postwall.blog.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuhanchao
 * @date 2022/12/11 1:07
 * @Description:
 */
@Controller
public class LoginController {

    @Autowired
    private IBlogUserService userService;

    @GetMapping("/getMailVerify")
    @ResponseBody
    public Result getMailVerify(@RequestParam("email") String email) {
        // 生成4位的验证码
        String random = ""+Math.random();
        String verify = random.substring(random.length() - 4);

        // 判断Redis中是否存在验证码
        String redisVerify = (String) CacheHelper.get(CacheConstant.EMAIL_KEY + email);
        if (redisVerify != null) {
            return Result.fail("验证码还在有效期内");
        }

        // 发送邮箱
        //boolean flag = EmailHelper.sendMail(email, verify); //暂时注解
        if (true) {
            System.out.println("发送至：" + email+"的验证码为："+verify);
            CacheHelper.setExpire(CacheConstant.EMAIL_KEY + email, verify, 10 * 60);
        }
        return Result.success();
    }


    @PostMapping("/register")
    @ResponseBody
    public Result register(@RequestBody RegisterDTO register) {
        if (!register.isNotEmpty()) {
            return Result.fail("表单输入不正确，请重新输入");
        }
        // 判断验证码是否正确
        String redisVerify = CacheHelper.getExpire(CacheConstant.EMAIL_KEY + register.getEmail());
        if (redisVerify == null || !redisVerify.equalsIgnoreCase(register.getEmailVerify())) {
            return Result.fail("验证码不正确，请重新输入");
        }
        // 删除验证码
        CacheHelper.del(CacheConstant.EMAIL_KEY + register.getEmail());


        // 判断邮箱是否存在
        BlogUserPO userByEmail = userService.findUserByEmail(register.getEmail());
        if (userByEmail != null) {
            return Result.fail("邮箱已被注册");
        }

        // 判断用户名是否存在
        BlogUserPO userByUserName = userService.findUserByUserName(register.getUsername());
        if (userByUserName != null) {
            return Result.fail("用户名已存在");
        }

        // 判断手机号是否存在
        BlogUserPO userByPhone = userService.findUserByPhone(register.getPhone());
        if (userByPhone != null) {
            return Result.fail("手机号已被注册");
        }

        BlogUserPO userPO = BeanHelper.copyProperties(register, BlogUserPO.class);
        userPO.setPassword(DigestUtils.md5DigestAsHex(userPO.getPassword().getBytes()));
        userPO.setNickname("用户_" + DateHelper.formatDate(new Date(), "yyMMddHHmmss"));
        userPO.setAvatar(CacheHelper.get("USER_DEFAULT_AVATAR") + "");
        userPO.setIsDelete(SysConstant.IS_DELETE.ENABLE);
        userPO.setRole(SysConstant.ROLE.USER);

        boolean flag = userService.insertUser(userPO);
        if (flag) {
            return Result.success("注册成功，请登录");
        }

        return Result.fail("注册失败");
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(@RequestParam("username") String username, @RequestParam("password") String password) {
        // 查询username是否为用户名
        BlogUserPO login = userService.findUserByUserName(username);
        if (login == null) {
            //  如果为空再查手机号
            login = userService.findUserByPhone(username);
            if (login == null) {
                // 在查邮箱
                login = userService.findUserByEmail(username);
            }
        }
        // 如果都没有 或 为删除 则返回不存在
        if (login == null || login.getIsDelete() != SysConstant.IS_DELETE.ENABLE) {
            return Result.fail("账号不存在");
        }

        // 校验密码
        if (!login.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
            return Result.fail("用户名或密码不正确");
        }

        //更新登陆时间
        userService.updateLoginTime(login.getId());
        // 登录成功，返回用户信息和token
        UserInfo userInfo = BeanHelper.copyProperties(login, UserInfo.class);
        String token = JwtHelper.createToken(BeanHelper.entityToMap(login));

        Map map = new HashMap<>();
        map.put("userInfo", userInfo);
        map.put("token", token);

        return Result.success(map, "登录成功");
    }

    @PostMapping("/userInfo")
    @ResponseBody
    public Result getUserInfo(@RequestParam("token") String token){
        // 判断账号是否过期
        if (JwtHelper.verifyToken(token) != 0){
            return Result.fail("页面过期，请重新登陆");
        }
        // 解析Token
        Map<String, Object> map = null;
        try{
            map = JwtHelper.parseToken(token);
        }catch (Exception e){
            return Result.fail("未登录或登陆过期");
        }
        Integer id = (Integer) map.get("id");
        // 通过token记录的ID查询用户信息
        BlogUserPO userById = userService.findUserById(id);
        //判断用户是否可用状态
        if (userById.getIsDelete() == SysConstant.IS_DELETE.DISABLE){
            return Result.fail("该用户不存在");
        }
        UserInfo userInfo = BeanHelper.copyProperties(userById, UserInfo.class);
        return Result.success(userInfo,"登陆成功");
    }

}
