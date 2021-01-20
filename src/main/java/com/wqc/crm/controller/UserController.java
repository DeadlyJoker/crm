package com.wqc.crm.controller;

import com.wqc.crm.base.BaseController;
import com.wqc.crm.base.ResultInfo;
import com.wqc.crm.query.UserQuery;
import com.wqc.crm.service.UserService;
import com.wqc.crm.utils.LoginUserUtil;
import com.wqc.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by WQC on 2021/1/11
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;

    @PostMapping("login")
    @ResponseBody
    public ResultInfo login(String userName, String userPwd) {

        return userService.loginCheck(userName, userPwd);
    }

    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updatePwd(HttpServletRequest req, String oldPwd, String newPwd, String confirmPwd) {
        ResultInfo resultInfo = new ResultInfo();
        int id = LoginUserUtil.releaseUserIdFromCookie(req);
        userService.updateUserPwd(id,oldPwd,newPwd,confirmPwd);
        return resultInfo;
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> getList(UserQuery query){
       return userService.queryUserList(query);
    }

    @RequestMapping("allRole")
    @ResponseBody
    public List<Map<String,Object>> queryAllSaleRole(){
        return userService.queryAllSaleRole();
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success();
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success();
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteUser(ids);
        return success();
    }

    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

    @RequestMapping("index")
    public String toIndex(){
        return "user/user";
    }

    @RequestMapping("toAddOrUpdatePage")
    public String toAddOrUpdatePage(Integer id,HttpServletRequest request){
        if (id != null){
            User user = userService.selectByPrimaryKey(id);
            request.setAttribute("user",user);
        }
        return "user/add_update";
    }
}
