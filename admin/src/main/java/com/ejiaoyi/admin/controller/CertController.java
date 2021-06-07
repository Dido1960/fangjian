package com.ejiaoyi.admin.controller;

import com.ejiaoyi.admin.entity.ReviewAuthorityLog;
import com.ejiaoyi.admin.service.impl.ReviewAuthorityLogServiceImpl;
import com.ejiaoyi.admin.support.AuthUser;
import com.ejiaoyi.admin.support.CurrentUserHolder;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.enums.DMLType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 * <p>
 * 数字证书详细信息 前端控制器
 * </p>
 *
 * @author Mike
 * @since 2021-01-13
 */
@RestController
@RequestMapping("/certInfo")
public class CertController {

    @Autowired
    ReviewAuthorityLogServiceImpl reviewAuthorityLogService;

    /**
     * 跳转审核权限写入记录页面
     *
     * @return
     */
    @RequestMapping(value = "/writeReviewAuthority")
    public ModelAndView writeReviewAuthority() {
        return new ModelAndView("/cert/writeReviewAuthorityPage");
    }

    /**
     * 写入离线审核日志
     * @param keyNo 锁序列号
     * @param certName 证书名称
     * @return
     */
    @RequestMapping(value = "/recordReviewAuthorityLog")
    @UserLog(value = "'写入离线审核锁信息 keyNo:'+#keyNo+' certName:'+#certName+' operateType:'+#operateType", dmlType = DMLType.INSERT)
    public boolean recordReviewAuthorityLog(String keyNo, String certName, Integer operateType) {
        AuthUser user = CurrentUserHolder.getUser();
        return reviewAuthorityLogService.saveReviewAuthorityLog(ReviewAuthorityLog.builder()
                .userId(user.getUserId())
                .userName(user.getName())
                .certName(certName)
                .keyNo(keyNo)
                .operateType(operateType)
                .build());
    }


    /**
     * 返回用户日志json
     *
     * @param username   用户名
     * @param dmlType    操作类型
     * @param content    内容
     * @param searchTime 搜索时间
     * @return 用户日志json字符串
     */
    @RequestMapping("/pagedReviewAuthorityLog")
    public String pagedReviewAuthorityLog(String username, Integer dmlType, String content, String searchTime) {
        return reviewAuthorityLogService.pagedReviewAuthorityLog(username, dmlType, content, searchTime);
    }

}
