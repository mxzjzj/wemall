package com.wemall.core.security;

import com.wemall.apis.jdutil.WeixinUtil;

import org.springframework.security.AuthenticationException;
import org.springframework.security.ui.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginUrlEntryPoint implements AuthenticationEntryPoint {
    public void commence(ServletRequest req, ServletResponse res, AuthenticationException authException) throws IOException, ServletException {
        String targetUrl = null;
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        String url = request.getRequestURI();

        String redirectUrl = request.getScheme() + "://" + request.getServerName() + "/xihao/user_wechat_login.htm";
        if ((request.getQueryString() != null) &&
                (!request.getQueryString().equals(""))) {
            url = url + "?" + request.getQueryString();
        }
        request.getSession(false).setAttribute("refererUrl", url);

        if (url.indexOf("/admin/") >= 0)
            targetUrl = request.getContextPath() + "/admin/login.htm";
        else {
            targetUrl = request.getContextPath() + "/user/login.htm";
        }
        response.sendRedirect(targetUrl);
    }
}

