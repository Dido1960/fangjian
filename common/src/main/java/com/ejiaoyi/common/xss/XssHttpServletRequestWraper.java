package com.ejiaoyi.common.xss;



import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;


/**
 *  转义封装类
 * @return
 * @author lesgod
 * @date 2020-7-7 9:34
 */
public class XssHttpServletRequestWraper extends HttpServletRequestWrapper {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public XssHttpServletRequestWraper(HttpServletRequest request) {
        super(request);
    }
    
    @Override
    public String getParameter(String name) {
        //Constants.MY_LOG.debug("getParameter----->转义处理");
        //return clearXss(super.getParameter(name));// 保留勿删
        return xssEncode(super.getParameter(name));
    }
    
    @Override
    public String getHeader(String name) {
        //Constants.MY_LOG.debug("getHeader----->转义处理");
        //return clearXss(super.getHeader(name)); // 保留勿删
        return xssEncode(super.getParameter(name));
    }
    
    @Override
    public String[] getParameterValues(String name) {
        //Constants.MY_LOG.debug("getParameterValues----->转义处理");
        if(!StringUtils.isEmpty(name)){
            String[] values = super.getParameterValues(name);
            if(values != null && values.length > 0){
                String[] newValues = new String[values.length];
                
                for(int i =0; i< values.length; i++){
                    newValues[i] = clearXss(values[i]);
                   /* newValues[i] = xssEncode(values[i]);*/
                }
                return newValues;
            }
        }
        return null;
    }

    /**
     *  
     * 处理字符转义【勿删，请保留该注释代码】
     * @param value
     * @return*/
    private String clearXss(String value){
        if (value == null || "".equals(value)) {
            return value;
        }
        String valueNew = HtmlUtils.htmlEscape(value);
        if(!valueNew.equals(value)){
            logger.warn("传入参数包含特殊字符，已被转义");
            logger.warn("param:"+value);
        }

        return valueNew;
    }
    
    /** 
     * 将特殊字符替换为全角 
     * @param s 
     * @return 
     */  
   private  String xssEncode(String s) {
        if (s == null || s.isEmpty()) {  
            return s;  
        }  
        StringBuilder sb = new StringBuilder();  
        for (int i = 0; i < s.length(); i++) {  
            char c = s.charAt(i);  
            switch (c) {  
            case '>':
                // 全角大于号
                sb.append('＞');
                break;  
            case '<':
                // 全角小于号
                sb.append('＜');
                break;  
            case '\'':
                // 全角单引号
                sb.append('‘');
                break;  
            case '\"':
                // 全角双引号
                sb.append('“');
                break;  
            case '&':
                // 全角＆
                sb.append('＆');
                break;  
            case '\\':
                // 全角斜线
                sb.append('＼');
                break;  
            case '/':
                // 全角斜线
                sb.append('／');
                break;  
            case '#':
                // 全角井号
                sb.append('＃');
                break;  
            case '(':
                // 全角(号
                sb.append('（');
                break;  
            case ')':
                // 全角)号
                sb.append('）');
                break;
            default:  
                sb.append(c);  
                break;  
            }  
        }  
        return sb.toString();  
    }
}