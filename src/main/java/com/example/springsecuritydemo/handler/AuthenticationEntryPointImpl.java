package com.example.springsecuritydemo.handler;

import com.alibaba.fastjson.JSON;
import com.example.springsecuritydemo.response.ResponseResult;
import com.example.springsecuritydemo.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
//自定义异常处理器 - 认证异常
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(), "认证失败请重新登录");
        String json  = JSON.toJSONString(result);

        WebUtils.renderString(httpServletResponse, json);
    }
}
