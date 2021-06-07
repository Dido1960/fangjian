package com.ejiaoyi.api.config;

import com.ejiaoyi.common.enums.DockApiCode;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Knife4j 配置
 *
 * @author Z0001
 * @since 2020-03-17
 */
@Component
@Configuration
@EnableKnife4j
@EnableSwagger2
@ConditionalOnProperty(value = {"knife4j.enable"}, matchIfMissing = true)
public class SwaggerConfig implements WebMvcConfigurer {

    @Bean
    public Docket servicePlatformDock() {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        Arrays.stream(DockApiCode.values()).forEach(errorEnum -> {
            responseMessageList.add(
                    new ResponseMessageBuilder().code(errorEnum.getCode()).message(errorEnum.getMsg()).responseModel(
                            new ModelRef(errorEnum.getMsg())).build()
            );
        });
        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .apiInfo(apiInfo("Mike", "", "甘肃省房建市政与第三方服务平台对接接口"))
                .groupName("servicePlatformDock")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ejiaoyi.api.controller.v2"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket statisticalData() {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        Arrays.stream(DockApiCode.values()).forEach(errorEnum -> {
            responseMessageList.add(
                    new ResponseMessageBuilder().code(errorEnum.getCode()).message(errorEnum.getMsg()).responseModel(
                            new ModelRef(errorEnum.getMsg())).build()
            );
        });
        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .apiInfo(apiInfo("Mike", "", "数据统计对接接口"))
                .groupName("statisticalData")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ejiaoyi.api.controller.statistical"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket phone() {
        List<ResponseMessage> responseMessageList = new ArrayList<>();
        Arrays.stream(DockApiCode.values()).forEach(errorEnum -> {
            responseMessageList.add(
                    new ResponseMessageBuilder().code(errorEnum.getCode()).message(errorEnum.getMsg()).responseModel(
                            new ModelRef(errorEnum.getMsg())).build()
            );
        });
        return new Docket(DocumentationType.SWAGGER_2)
                .globalResponseMessage(RequestMethod.GET, responseMessageList)
                .globalResponseMessage(RequestMethod.POST, responseMessageList)
                .globalResponseMessage(RequestMethod.PUT, responseMessageList)
                .globalResponseMessage(RequestMethod.DELETE, responseMessageList)
                .apiInfo(apiInfo("Mike", "", "手机端数据对接接口"))
                .groupName("phone")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ejiaoyi.api.controller.mobilePhone"))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo(String author, String email, String title) {
        return new ApiInfoBuilder().title(title)
                .description("   1、数据不规范\n" +
                        "    \n" +
                        "    2、数据成功的数据\n" +
                        "    \n" +
                        "    3、请求失败的数据\n" +
                        "    \n" +
                        "    4、重定向、请求方法不合适\n" +
                        "\n" +
                        "    5 .服务器内部错误")
                .version("1.0")
                .contact(new Contact(author, "无", email))
                .build();
    }

}
