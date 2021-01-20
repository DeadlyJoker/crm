package com.wqc.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wqc.crm.base.BaseQuery;
import com.wqc.crm.base.BaseService;
import com.wqc.crm.base.ResultInfo;
import com.wqc.crm.dao.CusDevPlanMapper;
import com.wqc.crm.dao.SaleChanceMapper;
import com.wqc.crm.query.CusDevPlanQuery;
import com.wqc.crm.utils.AssertUtil;
import com.wqc.crm.vo.CusDevPlan;
import com.wqc.crm.vo.SaleChance;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WQC on 2021/1/13
 */
@Service
public class CusDevPlanService extends BaseService<CusDevPlan, Integer> {

    @Resource
    private CusDevPlanMapper cusDevPlanMapper;
    @Resource
    private SaleChanceMapper saleChanceMapper;

    public Map<String, Object> queryCusDevPlanList(CusDevPlanQuery query) {
        PageHelper.startPage(query.getPage(), query.getLimit());
        List<CusDevPlan> cusDevPlans = cusDevPlanMapper.selectByParams(query);
        PageInfo<CusDevPlan> pageInfo = PageInfo.of(cusDevPlans);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }


    /**
     * 添加计划项
     * 1. 参数校验
     * 营销机会ID 非空 记录必须存在
     * 计划项内容 非空
     * 计划项时间 非空
     * 2. 设置参数默认值
     * is_valid
     * crateDate
     * updateDate
     * 3. 执行添加，判断结果
     *
     * @return
     */
    public void addCusDevPlan(CusDevPlan cusDevPlan) {
        checkCusDevPlanData(cusDevPlan);
        cusDevPlan.setIsValid(1);
        cusDevPlan.setCreateDate(new Date());
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.insertSelective(cusDevPlan) < 1, "添加失败");
    }

    /**
     * 更新计划项
     *  1.参数校验
     *      id 非空 记录存在
     *      营销机会id 非空 记录必须存在
     *      计划项内容 非空
     *      计划项时间 非空
     *  2.参数默认值设置
     *      updateDate
     *  3.执行更新 判断结果
     */
    public void saveCusDevPlan(CusDevPlan cusDevPlan){
        AssertUtil.isTrue(cusDevPlan.getId() == null && cusDevPlanMapper.selectByPrimaryKey(cusDevPlan.getId()) == null,"待更新计划不存在");
        checkCusDevPlanData(cusDevPlan);
        cusDevPlan.setUpdateDate(new Date());
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKeySelective(cusDevPlan) < 1,"计划更新失败");
    }

    public void delCusDevPlan(Integer id){
        AssertUtil.isTrue(id == null ,"数据异常，请重试");
        CusDevPlan cusDevPlan = cusDevPlanMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(cusDevPlan == null,"待删除计划不存在");
        cusDevPlan.setIsValid(0);
        AssertUtil.isTrue(cusDevPlanMapper.updateByPrimaryKey(cusDevPlan) < 1,"删除计划失败");
    }

    public void checkCusDevPlanData(CusDevPlan cusDevPlan) {
        AssertUtil.isTrue(cusDevPlan.getSaleChanceId() == null, "数据异常，请重试");
        AssertUtil.isTrue(saleChanceMapper.selectByPrimaryKey(cusDevPlan.getSaleChanceId()) == null, "数据异常，请重试");
        AssertUtil.isTrue(cusDevPlan.getPlanItem() == null, "请输入计划内容");
        AssertUtil.isTrue(cusDevPlan.getPlanDate() == null, "请输入计划时间");
    }

}
