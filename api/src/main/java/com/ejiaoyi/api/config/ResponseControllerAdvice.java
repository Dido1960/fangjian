package com.ejiaoyi.api.config;

import com.ejiaoyi.api.dto.RestResultVO;
import com.ejiaoyi.api.exception.APIException;
import com.ejiaoyi.common.enums.DockApiCode;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 控制层数据返回标志格式封装
 *
 * @author Make
 * @since 2020-12-28
 */
@RestControllerAdvice(basePackages = {"com.ejiaoyi.api.controller"})
public class ResponseControllerAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> aClass) {
        // 如果接口返回的类型本身就是ResultVO那就没有必要进行额外的操作，返回false
        return !returnType.getGenericParameterType().equals(RestResultVO.class);
    }
 
    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {

        // String类型不能直接包装，所以要进行些特别的处理
        if (returnType.getGenericParameterType().equals(String.class)) {
            if(data == null){
                throw new APIException(DockApiCode.SUCCESS,"返回数据为空",null);
            }else{
                throw new APIException(DockApiCode.SUCCESS,null,String.valueOf(data));
            }

        }
        if (data instanceof RestResultVO ){
            return data;
        }
        if (data == null){
            return new RestResultVO<>(DockApiCode.SUCCESS,"返回数据为空",null);
        }

        // 将原本的数据包装在ResultVO里
        return new RestResultVO<>(DockApiCode.SUCCESS,data);
    }


}