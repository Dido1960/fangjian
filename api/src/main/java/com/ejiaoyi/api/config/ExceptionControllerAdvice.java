package com.ejiaoyi.api.config;

import com.ejiaoyi.api.dto.RestResultVO;
import com.ejiaoyi.api.exception.APIException;
import com.ejiaoyi.common.enums.DockApiCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author Make
 * @since 2020-12-28
 */
@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    @ExceptionHandler(APIException.class)
    public RestResultVO APIExceptionHandler(APIException e) {
        // 注意哦，这里返回类型是自定义响应体
        if(e.getCode() != DockApiCode.SUCCESS.getCode()){
            e.printStackTrace();
        }
        RestResultVO resultVO= new RestResultVO<>(DockApiCode.getEnum(e.getCode()));
        resultVO.setMsg(e.getMsg());
        if(e.getData()!=null){
            resultVO.setData(e.getData());
        }else{
            resultVO.setData(null);
        }
        return resultVO;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResultVO MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        // 注意哦，这里返回类型是自定义响应体
        RestResultVO resultVO= new RestResultVO<String>(DockApiCode.AGRS_VALIDATION_FAILS);
        resultVO.setMsg(objectError.getDefaultMessage());
        resultVO.setData(null);
        return resultVO;
    }

    @ExceptionHandler(Exception.class)
    public RestResultVO ExceptionHandler(Exception e) {
        if(e instanceof  APIException){
            return this.APIExceptionHandler((APIException)e);
        }

        e.printStackTrace();
        String errorMsg = e.getMessage().replaceAll(" com.ejiaoyi.","");
        return new RestResultVO<>(DockApiCode.ERROR_OTHER,e.getClass().getSimpleName(),errorMsg);
    }


}