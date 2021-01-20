package com.wqc.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wqc.crm.base.BaseService;
import com.wqc.crm.dao.SaleChanceMapper;
import com.wqc.crm.query.SaleChanceQuery;
import com.wqc.crm.utils.AssertUtil;
import com.wqc.crm.utils.PhoneUtil;
import com.wqc.crm.vo.SaleChance;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WQC on 2021/1/12
 */
@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {
    @Resource
    private SaleChanceMapper saleChanceMapper;

    public Map<String, Object> querySaleChanceList(SaleChanceQuery query) {
        PageHelper.startPage(query.getPage(), query.getLimit());
        List<SaleChance> saleChances = saleChanceMapper.selectByParams(query);
        PageInfo<SaleChance> pageInfo = PageInfo.of(saleChances);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());

        return map;
    }

    /**
     * @param saleChance
     */
    @Transactional
    public void saveSaleChance(SaleChance saleChance) {
        checkCustomer(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());

        saleChance.setState(0);
        saleChance.setDevResult(0);
        if (StringUtils.isNotBlank(saleChance.getAssignMan())) {
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }

        saleChance.setIsValid(1);
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance) < 1, "添加失败");
    }

    /**
     * 更新营销机会数据
     *      1.参数校验
     *          id:记录必须存在
     *          customerName:非空
     *          linkMan:非空
     *          linkPhone:非空，11位手机号
     *      2. 设置相关参数值
     *          updateDate:系统当前时间
     *              原始记录 未分配 修改后改为已分配(由分配人决定)
     *                  state 0->1
     *                  assginTime 系统当前时间
     *                  devResult 0-->1
     *              原始记录 已分配 修改后 为未分配
     *                  state 1-->0
     *                  assignTime 待定 null
     *                  devResult 1-->0
     *      3.执行更新 判断结果
     *
     * @param saleChance
     */
    public void updateSaleChance(SaleChance saleChance) {
        //校验数据
        AssertUtil.isTrue(saleChance.getId() == null,"数据异常，请重试");
        checkCustomer(saleChance.getCustomerName(),saleChance.getLinkMan(),saleChance.getLinkPhone());
        //获取原数据
        SaleChance dbSalChance = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(dbSalChance == null,"数据异常，请重试");

        if(StringUtils.isBlank(dbSalChance.getAssignMan())){
            if(StringUtils.isNotBlank(saleChance.getAssignMan())){
                saleChance.setState(1);
                saleChance.setDevResult(1);
                saleChance.setAssignTime(new Date());
                saleChance.setAssignMan(saleChance.getAssignMan());
            }
        }else{
            if(StringUtils.isNotBlank(saleChance.getAssignMan())){
                if(saleChance.getAssignMan().equals(dbSalChance.getAssignMan())){
                    saleChance.setAssignTime(new Date());
                }else {
                    saleChance.setAssignTime(null);
                    saleChance.setAssignMan(null);
                    saleChance.setState(1);
                    saleChance.setDevResult(1);
                }
            }else{
                saleChance.setState(0);
                saleChance.setDevResult(0);
            }
            saleChance.setUpdateDate(new Date());
        }
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance) < 1,"更新失败");
    }


    public void updateSaleChanceDevResult(Integer id, Integer devResult) {
        AssertUtil.isTrue(id == null,"数据异常，请重试");
        SaleChance saleChance = saleChanceMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(saleChance == null,"待更新数据不存在");
        saleChance.setDevResult(devResult);
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKey(saleChance) < 1,"开发状态更新失败");
    }



    private void checkCustomer(String customerName, String linkMan, String linkPhone) {
        //customerName:非空
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "客户名称不能为空");
        //linkMan:非空
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "联系人不能为空");
        //linkPhone:非空 11位手机号
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone), "手机号格式不正确");
    }

    @Transactional
    public void deleteSaleChanceByIds(Integer[] ids) {
        AssertUtil.isTrue(ids == null || ids.length == 0,"请选择要删除的选项");
        AssertUtil.isTrue(saleChanceMapper.deleteBatch(ids) < 0,"删除失败");
    }
}
