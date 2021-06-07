package com.ejiaoyi.common.controller;

import com.alibaba.fastjson.JSON;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.util.CertUtil;
import com.ejiaoyi.common.util.CommonUtil;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * <p>
 * CA数据加解密 控制器
 * </p>
 *
 * @author fengjunhong
 * @since 2020-05-29
 */
@RestController
@RequestMapping("/common/data")
public class CertDataController {

    /**
     * CA数据加密
     *
     * @param content 原文数据
     * @return
     * @throws Exception
     */
    @UserLog(value = "'CA数据加密（原文）：'+#content+'加密因子：'+#keyNum",dmlType = DMLType.SELECT)
    //@PostMapping("/encrypt")
    public String encrypt(String content,String keyNum) throws Exception {
        String password = CertUtil.encrypt(content,keyNum);
        System.out.println("加密后:" + password);
        return password;
    }

    /**
     * CA数据解密
     *
     * @param content 密文数据
     * @return
     * @throws Exception
     */
    @UserLog(value = "'CA数据解密（密文）：'+#content+'加密因子：'+#keyNum",dmlType = DMLType.SELECT)
    @PostMapping("/decrypt")
    public Map decrypt(String content,String keyNum) throws Exception {
        content = StringEscapeUtils.unescapeHtml4(content);
        Map<String, String> map = (Map) JSON.parse(content);
        for (Map.Entry<String, String> m : map.entrySet()) {
            if (!CommonUtil.isEmpty(m.getValue())) {
                map.put(m.getKey(), CertUtil.decrypt(m.getValue(), keyNum));
            }
        }
        return map;
    }

    /**
     * 锁的弹出层选择器(BJCA)
     **/
    @RequestMapping("/selectCaInfoPage")
    public ModelAndView selectCaInfoPage() {
        return new ModelAndView("/caInfo/selectCert");
    }


    /**
     * 锁的弹出层选择器（其他CA）
     **/
    @RequestMapping("/selectOtherCaInfoPage")
    public ModelAndView selectOtherCaInfoPage() {
        return new ModelAndView("/caInfo/selectOtherCert");
    }


    /**
     * 锁的弹出层选择器（其他CA）
     **/
    @RequestMapping("/selectAllCaInfoPage")
    public ModelAndView selectAllCaInfoPage() {
        return new ModelAndView("/caInfo/selectAllCertPage");
    }
}
