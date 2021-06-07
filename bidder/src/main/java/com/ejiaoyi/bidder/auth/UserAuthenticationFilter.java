package com.ejiaoyi.bidder.auth;

import com.alibaba.fastjson.JSONObject;
import com.ejiaoyi.common.util.CommonUtil;
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
    public static final String FORM_QR_CODE_KEY = "qrCodeKey";
    public static final String FORM_LOGIN_CERT_SERIAL_NUMBER_KEY = "cert_serial_number";
    public static final String FORM_LOGIN_UKEY_SERIAL_NUMBER_KEY = "ukey_serial_number";

    public static final String FORM_KEY_NUM_KEY = "casn";

    //游客
    public static final String FORM_VISITOR_LOGIN = "visitorLogin";
    public static final String FORM_VRIFY_CODE = "vrifyCode";

    // 默认拦截地址
    public static final String DEFAULT_FILTER_URL = "/login";

    public UserAuthenticationFilter() {
        super(new AntPathRequestMatcher(DEFAULT_FILTER_URL, HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String username = httpServletRequest.getParameter(FORM_USERNAME_KEY);
        String password = httpServletRequest.getParameter(FORM_PASSWORD_KEY);
        String qrCodeKey = httpServletRequest.getParameter(FORM_QR_CODE_KEY);
        String casn = httpServletRequest.getParameter(FORM_KEY_NUM_KEY);
        String certSerialNumber = httpServletRequest.getParameter(FORM_LOGIN_CERT_SERIAL_NUMBER_KEY);
        String ukeySerialNumber = httpServletRequest.getParameter(FORM_LOGIN_UKEY_SERIAL_NUMBER_KEY);
        String vrifyCode = httpServletRequest.getParameter(FORM_VRIFY_CODE);
        String visitorLogin = httpServletRequest.getParameter(FORM_VISITOR_LOGIN);

        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }

        if (qrCodeKey == null) {
            qrCodeKey = "";
        }


        if (casn == null) {
            casn = "";
        }

        if (certSerialNumber == null) {
            certSerialNumber = "";
        }

        if (ukeySerialNumber == null) {
            ukeySerialNumber = "";
        }
        if (vrifyCode == null) {
            vrifyCode = "";
        }
        if (visitorLogin == null) {
            visitorLogin = "";
        }
        //游客登录设置
        if (!CommonUtil.isEmpty(visitorLogin)) {
            password = vrifyCode;
        }
        username = username.trim();
        password = password.trim();
        qrCodeKey = qrCodeKey.trim();
        certSerialNumber = certSerialNumber.trim();
        ukeySerialNumber = ukeySerialNumber.trim();


        // 构建JSON
        Map<String, String> paramMap = new HashMap<>(1);
        paramMap.put("username", username);
        paramMap.put("password", password);
        paramMap.put("qrCodeKey", qrCodeKey);
        paramMap.put("casn", casn);
        paramMap.put("cert_serial_number", certSerialNumber);
        paramMap.put("ukey_serial_number", ukeySerialNumber);
        paramMap.put("vrifyCode", vrifyCode);
        paramMap.put("visitorLogin", visitorLogin);

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
