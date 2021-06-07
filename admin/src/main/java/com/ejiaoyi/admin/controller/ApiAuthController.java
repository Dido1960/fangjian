package com.ejiaoyi.admin.controller;


import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.service.impl.ApiAuthServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <p>
 * API接口认证信息 前端控制器
 * </p>
 *
 * @author Z0001
 * @since 2020-04-01
 */
@RestController
@RequestMapping("/apiAuth")
public class ApiAuthController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApiAuthServiceImpl apiAuthService;

    /**
     * 接口授权管理页面
     *
     * @return
     */
    @RequestMapping("/apiAuthPage")
    public ModelAndView apiAuthPage() {
        return new ModelAndView("/api/apiAuth");
    }

    /**
     * 分页获取API授权信息
     *
     * @param apiName  接口名称
     * @param platform 平台授权码
     * @param apiKey   API授权码
     * @param remark   授权说明
     * @param enabled  启用状态
     * @return API授权信息列表JSON
     */
    @RequestMapping("/pagedApiAuth")
    public String pagedApiAuth(String apiName, String platform, String apiKey, String remark, Integer enabled) {
        return apiAuthService.pagedApiAuth(apiName, platform, apiKey, remark, enabled);
    }

    /**
     * 生成接口授权信息页面
     *
     * @return
     */
    @RequestMapping("/addApiAuthPage")
    public ModelAndView addApiAuthPage() {
        return new ModelAndView("/api/addApiAuth");
    }

    /**
     * 生成接口授权信息
     *
     * @param apiName 接口名称
     * @param remark  启用状态
     * @return 是否生成成功
     */
    @PostMapping("/addApiAuth")
    @UserLog(value = "'生成接口授权信息: apiName='+#apiName+', remark='+#remark", dmlType = DMLType.INSERT)
    public boolean addApiAuth(@RequestParam String apiName, @RequestParam String remark) {
        try {
            return apiAuthService.addApiAuth(apiName, remark);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 修改接口授权信息启用状态
     *
     * @param id      主键
     * @param enabled 启用状态
     * @return 是否修改成功
     */
    @PostMapping("/updateApiAuthEnabled")
    @UserLog(value = "'修改接口授权信息启用状态: id='+#id+', enabled='+#enabled", dmlType = DMLType.UPDATE)
    public boolean updateApiAuthEnabled(@RequestParam Integer id, @RequestParam Integer enabled) {
        try {
            return apiAuthService.updateApiAuthEnabled(id, enabled);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 批量删除授权信息
     *
     * @param ids 角色信息
     * @return 删除结果
     */
    @RequestMapping("/deleteApiAuth")
    @UserLog(value = "'批量删除角色信息: ids='+#ids", dmlType = DMLType.DELETE)
    public Boolean deleteApiAuth(Integer[] ids) {
        return apiAuthService.deleteApiAuth(ids);
    }
}
