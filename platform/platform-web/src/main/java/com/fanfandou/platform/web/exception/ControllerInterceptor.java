package com.fanfandou.platform.web.exception;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by wudi.
 * Descreption:Contrller层访问前的数据日志抓取.
 * Date:2017/7/12
 */
public class ControllerInterceptor implements HandlerInterceptor {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private String requestPath = null; // 请求地址
    private Map<?, ?> inputParamMap = null; // 传入参数

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        //该方法在目标方法之前被调用. 做权限. 日志, 事务等.
        // 获取输入参数
        inputParamMap = httpServletRequest.getParameterMap();
        // 获取请求地址
        requestPath = httpServletRequest.getRequestURI();
        String inputContent = JSON.toJSONString(inputParamMap);
        logger.info("请求内容为:{}", requestPath + ":" + inputContent);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
