package com.ejiaoyi.bidder.config;

import com.ejiaoyi.bidder.filter.KaptchaFilter;
import com.ejiaoyi.bidder.auth.CustomUserDetailsService;
import com.ejiaoyi.bidder.auth.UserAuthenticationFilter;
import com.ejiaoyi.common.crypto.SM2PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * spring security 安全配置
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers().permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login")
                .failureUrl("/login.html?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/index")
                .permitAll()
                .and()
                .cors()
                .and()
                .headers()
                .frameOptions().sameOrigin();

        //登出
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login.html")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll();

        // 在认证用户名之前认证验证码，如果验证码错误，将不执行用户名和密码的认证
        http.addFilterBefore(new KaptchaFilter("/login"), UsernamePasswordAuthenticationFilter.class);

        // 添加过滤器
        http.addFilterBefore(userAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers(WebMvcConfig.STATIC_PATH);
    }

    @Bean
    public SM2PasswordEncoder passwordEncoder() {
        return new SM2PasswordEncoder();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public UserAuthenticationFilter userAuthenticationFilter() {
        UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter();
        userAuthenticationFilter.setAuthenticationManager(this.authenticationManager);
        return userAuthenticationFilter;
    }


}
