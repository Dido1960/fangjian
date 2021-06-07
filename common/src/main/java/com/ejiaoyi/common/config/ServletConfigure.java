package com.ejiaoyi.common.config;

import com.ejiaoyi.common.servlet.SignarServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Mike
 */
@Configuration
public class ServletConfigure {

	/**
	 * 代码注册servlet(不需要@ServletComponentScan注解)
	 *
	 * @return
	 */
	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		// ServletName默认值为首字母小写，即myServlet1
		return new ServletRegistrationBean(new SignarServlet(), "/sigar/uploadPdfSignar");
	}
}
	
 
