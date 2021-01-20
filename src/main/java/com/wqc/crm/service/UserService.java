package com.wqc.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wqc.crm.base.BaseService;
import com.wqc.crm.base.ResultInfo;
import com.wqc.crm.dao.UserMapper;
import com.wqc.crm.dao.UserRoleMapper;
import com.wqc.crm.model.UserModel;
import com.wqc.crm.query.UserQuery;
import com.wqc.crm.utils.AssertUtil;
import com.wqc.crm.utils.Md5Util;
import com.wqc.crm.utils.PhoneUtil;
import com.wqc.crm.utils.UserIDBase64;
import com.wqc.crm.vo.User;
import com.wqc.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by WQC on 2021/1/11
 */
@Service
public class UserService extends BaseService<User, Integer> {
    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 登录校验
     *
     * @param userName
     * @param userPwd
     * @return
     */
    public ResultInfo loginCheck(String userName, String userPwd) {
        //检查数据是否为空
        checkLoginData(userName, userPwd);
        //通过用户名查询数据
        User user = userMapper.selectUserByName(userName);
        //判断用户是否存在
        AssertUtil.isTrue(user == null, "用户不存在");
        //检查密码是否相等
        checkLoginPwd(userPwd, user);
        //返回数据

        return handleResultInfo(user);
    }


    /**
     * 在登陆状态下，修改密码
     *
     * @param id
     * @param oldPwd
     * @param newPwd
     * @param confirmPwd
     */
    @Transactional
    public void updateUserPwd(Integer id, String oldPwd, String newPwd, String confirmPwd) {
        //判断数据是否为空
        AssertUtil.isTrue(id == null, "用户未登录");
        //查询数据库
        //校验id是否存在
        User user = userMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(user == null, "用户状态异常");

        //校验密码
        checkUpdatePwdData(oldPwd, newPwd, confirmPwd, user);

        //更新密码
        user.setUserPwd(Md5Util.encode(newPwd));
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(userMapper.updateByPrimaryKey(user) < 1, "更新密码失败");

    }

    /**
     * 获取所有的销售角色
     *
     * @return
     */
    public List<Map<String, Object>> queryAllSaleRole() {
        List<Map<String, Object>> mapList = userMapper.selectAllSaleRole();
        return mapList;
    }

    /**
     * 添加用户
     * 1. 参数校验
     * 用户名 非空 唯一性
     * 邮箱 非空
     * 手机号 非空 格式合法
     * 2. 设置默认参数
     * isValid 1
     * creteDate 当前时间
     * updateDate 当前时间
     * userPwd 123456 -> md5加密
     * 3. 执行添加，判断结果
     */
    @Transactional
    public void addUser(User user) {
        checkUserData(user);
        AssertUtil.isTrue(userMapper.selectUserByName(user.getUserName()) == null,"用户已存在");
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setUserPwd(Md5Util.encode("123456"));
        AssertUtil.isTrue(userMapper.insertHasKey(user) < 1, "用户添加失败");
        relaionUserRole(user.getId(), user.getRoleIds());
    }

    private void relaionUserRole(Integer id, String roleIds) {
        int count = userRoleMapper.countUserRoleByUserId(id);
        if (count > 0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(id) != count, "用户角色分配失败!");
        }

        if (StringUtils.isNotBlank(roleIds)) { //重新添加新的角色
             List<UserRole> userRoles = new ArrayList<UserRole>();
            for (String s : roleIds.split(",")) {
                UserRole userRole = new UserRole();
                userRole.setUserId(id);
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoles.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles) < userRoles.size(), "用户角色分配失败!");
        }
    }

    /**
     * 更新用户
     * 1. 参数校验
     * id 非空 记录必须存在
     * 用户名 非空 唯一性
     * email 非空
     * 手机号 非空 格式合法
     * 2. 设置默认参数
     * updateDate
     * 3. 执行更新，判断结果
     *
     * @param user
     */
    @Transactional
    public void updateUser(User user) {
        User dbUser = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(user.getId() == null && null == dbUser, "数据异常，请重试");
        AssertUtil.isTrue(StringUtils.isBlank(user.getUserName()), "用户名不能为空");
        AssertUtil.isTrue(userMapper.selectUserByName(user.getUserName()) != null && user.getId() != dbUser.getId(), "用户名已存在");
        checkUserData(user);
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户更新失败");
        Integer userId = userMapper.selectUserByName(user.getUserName()).getId();
        relaionUserRole(userId, user.getRoleIds());
    }





    public void deleteUser(Integer[] ids) {
        AssertUtil.isTrue(ids == null || ids.length == 0, "请选择要删除的选项");
        AssertUtil.isTrue(userMapper.deleteBatch(ids) < 1, "用户删除失败");
    }

    private void checkUserData(User user) {

        AssertUtil.isTrue(StringUtils.isBlank(user.getEmail()), "邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(user.getPhone()), "手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()), "请输入正确的手机号");
    }


    private void checkUpdatePwdData(String oldPwd, String newPwd, String confirmPwd, User user) {
        //判断密码不能为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd), "旧密码不能为空");
        //校验原始密码是否一致
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)), "旧密码不正确");
        //旧密码与新密码不能一致
        AssertUtil.isTrue(StringUtils.isBlank(newPwd), "新密码不能为空");
        AssertUtil.isTrue(oldPwd.equals(newPwd), "新密码不能与旧密码一致");

        //新密码与确认密码必须一致
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd), "确认密码不能为空");
        AssertUtil.isTrue(!newPwd.equals(confirmPwd), "新密码与确认密码不一致");
    }


    private void checkLoginPwd(String userPwd, User user) {
        //将前台传回的数据进行加密
        String encode = Md5Util.encode(userPwd);
        AssertUtil.isTrue(!encode.equals(user.getUserPwd()), "密码不正确");
    }

    private void checkLoginData(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "密码不能为空");
    }

    public ResultInfo handleResultInfo(User user) {
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = new UserModel();
        userModel.setUserId(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        resultInfo.setResult(userModel);
        return resultInfo;
    }

    public Map<String, Object> queryUserList(UserQuery query) {
        PageHelper.startPage(query.getPage(), query.getLimit());
        List<User> users = userMapper.selectByParams(query);
        PageInfo<User> pageInfo = PageInfo.of(users);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }


}
