package com.wqc.crm.controller;

import com.wqc.crm.base.BaseController;
import com.wqc.crm.service.PermissionService;
import com.wqc.crm.service.UserService;
import com.wqc.crm.utils.CookieUtil;
import com.wqc.crm.utils.LoginUserUtil;
import com.wqc.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private PermissionService permissionService;

    /**
     * 系统登录页
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "index";
    }


    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome() {
        return "welcome";
    }


    /**
     * 后端管理主页面
     *
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest req) {
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        req.setAttribute("user", userService.selectByPrimaryKey(userId));
        List<String> permissions = permissionService.queryUserHasRolesHasPermissions(userId);
        req.getSession().setAttribute("permissions", permissions);
        return "main";
    }
}
