package com.ejiaoyi.api.filter;

import ejiaoyi.crypto.SM4Util;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 参数加解密过滤器
 *
 * @author Mike
 * @since 2021-01-04
 */
@Slf4j
public class DataFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
// TODO Auto-generated method stub

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        String contentType = request.getContentType();
        WrapperedResponse wrapResponse = new WrapperedResponse((HttpServletResponse) response);
        WrapperedRequest wrapRequest;
        // 表单请求数据自行处理解密
        if (!contentType.contains("multipart/form-data")) {
            String requestBody = getRequestBody((HttpServletRequest) request);
            log.info("请求数据：" + requestBody);
            //解密请求报文
            String requestBodyMw = SM4Util.decryptCBC(requestBody);
            log.info("解密请求数据：" + requestBodyMw);
            wrapRequest = new WrapperedRequest((HttpServletRequest) request, requestBodyMw);
            chain.doFilter(wrapRequest, wrapResponse);
        } else {
            chain.doFilter(request, wrapResponse);
        }

        byte[] data = wrapResponse.getResponseData();
        String responseBody = new String(data, StandardCharsets.UTF_8);
        log.info("原始返回数据： " + responseBody);
        // 加密返回报文
        String responseBodyMw = SM4Util.encryptCBC(responseBody);
        log.info("加密返回数据： " + responseBodyMw);
        writeResponse(response, responseBodyMw);
    }

    @Override
    public void destroy() {
// TODO Auto-generated method stub

    }


    private String getRequestBody(HttpServletRequest req) {
        try {
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            log.info("请求体读取失败" + e.getMessage());
        }
        return "";
    }

    private void writeResponse(ServletResponse response, String responseString)
            throws IOException {
        PrintWriter out = response.getWriter();
        out.print(responseString);
        out.flush();
        out.close();
    }

}