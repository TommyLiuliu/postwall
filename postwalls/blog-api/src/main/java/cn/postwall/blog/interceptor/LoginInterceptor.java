package cn.postwall.blog.interceptor;

import cn.postwall.blog.pojo.Result;
import cn.postwall.blog.pojo.constant.SysConstant;
import cn.postwall.blog.pojo.po.BlogUserPO;
import cn.postwall.blog.service.IBlogUserService;
import cn.postwall.blog.utils.JwtHelper;
import com.alibaba.fastjson.JSON;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 后台拦截器 判断是否存在token，token是否生效
 * @author: liuhanchao
 * @Date: 2022/12/31 22:58
 */
public class LoginInterceptor implements HandlerInterceptor {

    private IBlogUserService userService;

    public LoginInterceptor() {
    }

    public LoginInterceptor(IBlogUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        if (token == null || "".equals(token) || JwtHelper.verifyToken(token) != 0) {
            Result fail = Result.fail(401, "用户未授权");
            PrintWriter writer = response.getWriter();
            writer.append(JSON.toJSONString(fail));
            return false;
        }
        Map<String, Object> map = JwtHelper.parseToken(token);
        Integer id = (Integer) map.get("id");
        BlogUserPO userById = userService.findUserById(id);
        if (userById == null || SysConstant.ROLE.USER.equals(userById.getRole())) {
            Result fail = Result.fail(401, "用户未授权");
            PrintWriter writer = response.getWriter();
            writer.append(JSON.toJSONString(fail));
            return false;
        }
        return true;
    }
}
