package com.ejiaoyi.admin.controller;


import com.ejiaoyi.common.entity.LoggingEvent;
import com.ejiaoyi.common.service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.List;

/**
 * <p>
 * 用户日志 前端控制器
 * </p>
 *
 * @author fengjunhong
 * @since 2020-03-25
 */
@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private UserLogServiceImpl userLogService;

    @Autowired
    private NetworkLogServiceImpl networkService;

    @Autowired
    private ApiLogServiceImpl apiLogService;

    @Autowired
    private RuntimeLogServiceImpl runtimeLogService;

    @Autowired
    private WordbookServiceImpl wordbookService;

    /**
     * 用户日志页面
     *
     * @return
     */
    @RequestMapping("/userLogPage")
    public ModelAndView userLogPage() {
        return new ModelAndView("/log/userlog");
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
    @RequestMapping("/pagedUser")
    public String pagedUser(String username, String dmlType, String content, String searchTime) {
        String dmlTypeName = wordbookService.getValue("dmlType", dmlType);
        return userLogService.pagedUser(username, dmlType, content, searchTime, dmlTypeName);
    }

    /**
     * 系统日志页面
     *
     * @return
     */
    @RequestMapping("/networkLogPage")
    public ModelAndView networkLogPage() {
        return new ModelAndView("/log/networkLog");
    }

    /**
     * 返回系统日志json
     *
     * @param userName       用户名
     * @param searchTime     查询时间
     * @param requestURI     请求地址
     * @param requestMethod  请求方法
     * @param processingTime 处理耗时
     * @return 系统日志JSON
     */
    @RequestMapping("/pagedWebLog")
    public String pagedWebLog(String userName, String searchTime, String requestURI, String requestMethod, String processingTime) {
        return networkService.pagedWebLog(userName, searchTime, requestURI, requestMethod, processingTime);
    }

    /**
     * 系统日志页面
     *
     * @return
     */
    @RequestMapping("/apiLogPage")
    public ModelAndView apiLogPage() {
        return new ModelAndView("/log/apiLog");
    }

    /**
     * 获取接口日志JSON
     *
     * @param methodName   请求方法名称
     * @param searchTime   日志时限
     * @param params       请求参数
     * @param platform     平台授权码
     * @param apiKey       API授权码
     * @param responseTime 响应时间
     * @return 接口日志JSON
     */
    @RequestMapping("/pagedApiLog")
    public String pagedApiLog(String methodName, String searchTime, String params, String platform, String apiKey, String responseTime) {
        return apiLogService.pagedApiLog(methodName, searchTime, params, platform, apiKey, responseTime);
    }

    /**
     * 运行日志页面
     *
     * @return
     */
    @RequestMapping("/runtimeLogPage")
    public ModelAndView runtimeLogPage() {
        ModelAndView mav = new ModelAndView("/log/runtimeLog");
        return mav;
    }

    /**
     * 获取运行日志JSON
     *
     * @param searchTime 搜索时间
     * @param logLevel   日志级别
     * @return 运行日志JSON
     */
    @RequestMapping("/pagedRuntimeLog")
    public String pagedRuntimeLog(String searchTime, String logLevel) {
        return runtimeLogService.pagedRuntimeLog(searchTime, logLevel);
    }

    /**
     * 获取运行日志列表
     *
     * @return
     */
    @RequestMapping("/list")
    public List<LoggingEvent> list() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(calendar.DATE, -5);
        //时间差
        long timeDiff = calendar.getTimeInMillis();

        return runtimeLogService.listRuntimeLog(timeDiff);
    }

    /**
     * 按照时间间隔删除运行日志
     *
     * @param timeDiff 时间间隔
     * @return 操作成功返回true, 否则返回false
     */
    @RequestMapping("/delRuntimeLog")
    public boolean delRuntimeLog(long timeDiff) {
        return runtimeLogService.delRuntimeLog(timeDiff);
    }

}

