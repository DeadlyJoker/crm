package com.wqc.crm.interceptors;

import com.wqc.crm.exceptions.NoLoginException;
import com.wqc.crm.service.UserService;
import com.wqc.crm.utils.LoginUserUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ⾮法访问拦截
 * Created by WQC on 2021/1/11
 */
public class NoLoginInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private UserService userService;

    /**
     *  判断⽤户是否是登录状态
     *  获取Cookie对象，解析⽤户ID的值
     *  如果⽤户ID不为空，且在数据库中存在对应的⽤户记录，表示请求合法
     *  否则，请求不合法，进⾏拦截，重定向到登录⻚⾯
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Integer id = LoginUserUtil.releaseUserIdFromCookie(request);

        if(id == null || null == userService.selectByPrimaryKey(id) ){
            throw new NoLoginException();
        }
        return true;
    }
}
