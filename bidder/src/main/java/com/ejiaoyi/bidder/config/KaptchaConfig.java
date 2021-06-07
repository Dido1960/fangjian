package com.ejiaoyi.bidder.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 *
 */
@Configuration
public class KaptchaConfig {


    @Bean("customKaptcha")
    public DefaultKaptcha defaultKaptcha() {
        Properties properties = new Properties();
        //是否使用边框  YES/NO
        properties.setProperty("kaptcha.border", "no");
        //边框颜色
        properties.setProperty("kaptcha.border.color", "105,179,90");
        //字体颜色
        properties.setProperty("kaptcha.textproducer.font.color", "30,159,255");
        //图片宽度
        properties.setProperty("kaptcha.image.width", "110");
        //图片高度
        properties.setProperty("kaptcha.image.height", "40");
        //文字间隔
        properties.setProperty("kaptcha.textproducer.char.space", "3");
        //干扰颜色
        properties.setProperty("kaptcha.noise.color", "255,255,255");
        //开始颜色
        properties.setProperty("kaptcha.background.clear.from", "255,255,255");
        //结束颜色
        properties.setProperty("kaptcha.background.clear.to", "255,255,255");
        //字体大小
        properties.setProperty("kaptcha.textproducer.font.size", "30");
        //字符集
        properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
        //字符串长度
        properties.setProperty("kaptcha.textproducer.char.length", "4");
        //字体样式
        properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");

        DefaultKaptcha captchaProducer = new DefaultKaptcha();
        Config config = new Config(properties);
        captchaProducer.setConfig(config);
        return captchaProducer;
    }


}