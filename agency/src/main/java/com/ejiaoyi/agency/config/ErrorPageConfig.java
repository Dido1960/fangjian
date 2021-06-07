package com.ejiaoyi.agency.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 错误页面配置
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Configuration
public class ErrorPageConfig implements ErrorPageRegistrar {

    @Override
    public void registerErrorPages(ErrorPageRegistry registry) {
        ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/errorPages/400");
        ErrorPage error403Page = new ErrorPage(HttpStatus.FORBIDDEN, "/errorPages/403");
        ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/errorPages/404");
        ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/errorPages/500");
        ErrorPage error502Page = new ErrorPage(HttpStatus.BAD_GATEWAY, "/errorPages/502");

        registry.addErrorPages(error400Page, error403Page, error404Page, error500Page, error502Page);
    }
}
