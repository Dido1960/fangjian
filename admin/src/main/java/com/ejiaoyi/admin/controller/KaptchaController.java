package com.ejiaoyi.admin.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 验证码控制器 该类需要越过登录获取
 *
 * @author lesgod
 * @date 2020/5/7 13:24
 */
@RestController
@RequestMapping("/kaptcha")
public class KaptchaController {

    @Autowired
    @Qualifier("customKaptcha")
    private Producer captchaProducer;

    /**
     * @param request
     * @param response
     * @return void
     * @author lesgod
     * @date 2020/5/7 13:16
     */
    @GetMapping("verification")
    public void verification(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");
        // create the text for the image
        String capText = captchaProducer.createText();
        // store the text in the session
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }


    /**
     * 验证码验证是否正常
     *
     * @return
     * @author lesgod
     * @date 2020/5/7 13:26
     */
    @RequestMapping("/validationResult")
    public boolean validationResult(HttpServletRequest request, String verifyCodeActual) {
        //获取生成的验证码
        String verifyCodeExpected = (String) request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        //获取用户输入的验证码
        if (verifyCodeActual == null || !verifyCodeActual.equals(verifyCodeExpected)) {
            return false;
        }
        return true;
    }
}