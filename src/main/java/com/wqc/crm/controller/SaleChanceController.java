package com.wqc.crm.controller;

import com.wqc.crm.annotation.RequirePermission;
import com.wqc.crm.base.BaseController;
import com.wqc.crm.base.ResultInfo;
import com.wqc.crm.query.SaleChanceQuery;
import com.wqc.crm.service.SaleChanceService;
import com.wqc.crm.service.UserService;
import com.wqc.crm.utils.AssertUtil;
import com.wqc.crm.utils.CookieUtil;
import com.wqc.crm.utils.LoginUserUtil;
import com.wqc.crm.vo.SaleChance;
import com.wqc.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * Created by WQC on 2021/1/12
 */
@Controller
@RequestMapping("sale_chance")
public class SaleChanceController extends BaseController {
    @Resource
    private SaleChanceService saleChanceService;
    @Resource
    private UserService userService;


    @RequestMapping("list")
    @ResponseBody
    @RequirePermission(code = "101001")
    public Map<String, Object> getList(SaleChanceQuery query,Integer flag,HttpServletRequest request) {
        if(flag != null && flag == 1){
            int id = LoginUserUtil.releaseUserIdFromCookie(request);
            AssertUtil.isTrue(userService.selectByPrimaryKey(id)==null,"数据异常");
            query.setAssignMan(id);
        }
        return saleChanceService.querySaleChanceList(query);
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveSaleChance(HttpServletRequest request,  SaleChance saleChance) {
        int id = LoginUserUtil.releaseUserIdFromCookie(request);
        User user = userService.selectByPrimaryKey(id);
        saleChance.setCreateMan(user.getUserName());
        saleChanceService.saveSaleChance(saleChance);

        return success();
    }


    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){
        saleChanceService.updateSaleChance(saleChance);
        return success();
    }

    @RequestMapping("updateSaleChanceDevResult")
    @ResponseBody
    public ResultInfo updateSaleChanceDevResult(Integer id,Integer devResult){
        saleChanceService.updateSaleChanceDevResult(id,devResult);
        return success();
    }

    /**
     * 进入营销机会页面
     *
     * @return
     */
    @RequestMapping("index")
    public String index() {
        return "saleChance/sale_chance";
    }


    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdateSaleChancePage(Integer id, HttpServletRequest request) {
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
        request.setAttribute("saleChance",saleChance);
        return "saleChance/add_update";
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteSaleChance(Integer[] ids){
        System.out.println(ids);
        saleChanceService.deleteSaleChanceByIds(ids);
        return success();
    }

}
