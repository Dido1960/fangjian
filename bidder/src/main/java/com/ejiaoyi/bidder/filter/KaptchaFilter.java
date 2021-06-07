package com.ejiaoyi.bidder.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

/**
 * Kaptcha 拦截器
 *
 * @author lesgod
 * @date 2020/5/7 13:46
 */
public class KaptchaFilter extends AbstractAuthenticationProcessingFilter {

    // SESSION 关于 验证码
    private static final String VRIFYCODE = "vrifyCode";

    // 扫码登录唯一标识
    private static final String QR_CODE_KEY = "qrCodeKey";

    // 拦截请求地址
    private String servletPath;

    public KaptchaFilter(String servletPath) {
        super(servletPath);
        this.servletPath = servletPath;

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        if (HttpMethod.POST.name().equalsIgnoreCase(req.getMethod()) && servletPath.equals(req.getServletPath())) {
            String expect = (String) req.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
            String qrCode = req.getParameter(QR_CODE_KEY);
            if (StringUtils.isEmpty(qrCode)) {
                System.out.println(req.getParameter(VRIFYCODE));
                if (expect == null || !expect.equals(req.getParameter(VRIFYCODE))) {
                    unsuccessfulAuthentication(req, res, new BadCredentialsException("输入的验证码不正确"));
                    return;
                }
            }
        }
        chain.doFilter(req, res);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", failed);
    }

}