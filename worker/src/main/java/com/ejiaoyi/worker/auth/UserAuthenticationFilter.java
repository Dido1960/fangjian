package com.ejiaoyi.worker.auth;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * spring security 参数预处理拦截器
 *
 * @author Z0001
 * @since 2020-05-07
 */
public class UserAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 定义参数 对应前端登陆name的值传递
     */
    public static final String FORM_USERNAME_KEY = "username";
    public static final String FORM_PASSWORD_KEY = "password";
    public static final String FORM_AUTH_KEY = "auth";
    public static final String FORM_LOGIN_TYPE_KEY = "loginType";
    public static final String FORM_LOGIN_UKEY_SERIAL_NUMBER_KEY = "ukey_serial_number";
    public static final String FORM_KEY_NUM_KEY = "casn";

    // 默认拦截地址
    public static final String DEFAULT_FILTER_URL = "/login";

    public UserAuthenticationFilter() {
        super(new AntPathRequestMatcher(DEFAULT_FILTER_URL, HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String username = httpServletRequest.getParameter(FORM_USERNAME_KEY);
        String password = httpServletRequest.getParameter(FORM_PASSWORD_KEY);
        String auth = httpServletRequest.getParameter(FORM_AUTH_KEY);
        String loginType = httpServletRequest.getParameter(FORM_LOGIN_TYPE_KEY);
        String casn = httpServletRequest.getParameter(FORM_KEY_NUM_KEY);
        String ukeySerialNumber = httpServletRequest.getParameter(FORM_LOGIN_UKEY_SERIAL_NUMBER_KEY);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        if (auth == null) {
            auth = "";
        }

        if (loginType == null) {
            loginType = "";
        }
        if (casn == null) {
            casn = "";
        }

        if (ukeySerialNumber == null) {
            ukeySerialNumber = "";
        }

        username = username.trim();
        password = password.trim();
        auth = auth.trim();
        loginType = loginType.trim();
        ukeySerialNumber = ukeySerialNumber.trim();

        // 构建JSON
        Map<String, String> paramMap = new HashMap<>(1);
        paramMap.put("username", username);
        paramMap.put("password", password);
        paramMap.put("auth", auth);
        paramMap.put("loginType", loginType);
        paramMap.put("casn", casn);
        paramMap.put("ukey_serial_number", ukeySerialNumber);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(JSONObject.toJSONString(paramMap), password);

        authRequest.setDetails(authenticationDetailsSource.buildDetails(httpServletRequest));

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
        request.getSession().setAttribute("SPRING_SECURITY_LAST_EXCEPTION", failed);
    }
}
