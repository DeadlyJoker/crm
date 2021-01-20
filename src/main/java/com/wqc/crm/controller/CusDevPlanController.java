package com.wqc.crm.controller;

import com.wqc.crm.base.BaseController;
import com.wqc.crm.base.ResultInfo;
import com.wqc.crm.query.CusDevPlanQuery;
import com.wqc.crm.query.SaleChanceQuery;
import com.wqc.crm.service.CusDevPlanService;
import com.wqc.crm.service.SaleChanceService;
import com.wqc.crm.service.UserService;
import com.wqc.crm.utils.AssertUtil;
import com.wqc.crm.utils.LoginUserUtil;
import com.wqc.crm.vo.CusDevPlan;
import com.wqc.crm.vo.SaleChance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by WQC on 2021/1/13
 */

@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {
    @Resource
    private CusDevPlanService cusDevPlanService;
    @Resource
    private SaleChanceService saleChanceService;

    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> getList(CusDevPlanQuery query) {
        return cusDevPlanService.queryCusDevPlanList(query);
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo add(CusDevPlan cusDevPlan){
        cusDevPlanService.addCusDevPlan(cusDevPlan);
        return success("计划添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo save(CusDevPlan cusDevPlan){
        System.out.println(cusDevPlan);
        cusDevPlanService.saveCusDevPlan(cusDevPlan);
        return success("计划更新成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer id){
        cusDevPlanService.delCusDevPlan(id);
        return success("删除计划成功");
    }



    @RequestMapping("index")
    public String toIndex(){
        return "cusDevPlan/cus_dev_plan";
    }

    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanDataPage(HttpServletRequest request,Integer sid){
        AssertUtil.isTrue(sid == null,"数据异常，请重试");
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(sid);
        AssertUtil.isTrue(saleChance==null,"数据异常，请重试");
        request.setAttribute("saleChance",saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }

    @RequestMapping("addOrUpdateCusDevPlanPage")
    public String addOrUpdateCusDevPlanPage(Integer sid,Integer id,HttpServletRequest request){
        AssertUtil.isTrue(sid == null,"数据异常");
        AssertUtil.isTrue(saleChanceService.selectByPrimaryKey(sid) == null,"数据异常");
        request.setAttribute("sid",sid);
        if(id != null){
            CusDevPlan cusDevPlan = cusDevPlanService.selectByPrimaryKey(id);
            AssertUtil.isTrue(cusDevPlan == null,"数据异常");
            request.setAttribute("cusDevPlan",cusDevPlan);
        }
        return "cusDevPlan/add_update";
    }
}
