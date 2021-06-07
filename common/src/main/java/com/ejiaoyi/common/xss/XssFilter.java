package com.ejiaoyi.common.xss;

import com.ejiaoyi.common.xss.XssHttpServletRequestWraper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;


/**
 * xss 拦截器
 *
 * @author lesgod
 * @return
 * @date 2020-7-7 9:32
 */
public class XssFilter implements Filter {
    public static final String PARAM_NAME_EXCLUSIONS = "exclusions";
    public static final String SEPARATOR = ",";
    private String[] excludesUrls = new String[]{
            "/kaptcha/verification", ".gif", ".js"
            , ".jpg", ".png", ".css", ".ico", ".html"
    };


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String requestURI = httpRequest.getRequestURI();
        if (!isExclusion(requestURI)) {
            XssHttpServletRequestWraper xssRequest = new XssHttpServletRequestWraper((HttpServletRequest) request);
            chain.doFilter(xssRequest, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    public boolean isExclusion(String requestURI) {
        if (this.excludesUrls == null) {
            return false;
        }
        for (String url : this.excludesUrls) {
            if (url.equals(requestURI) || requestURI.endsWith(url) || requestURI.startsWith("/api")) {
                return true;
            }
        }
        return false;
    }


}