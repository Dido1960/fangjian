package com.ejiaoyi.admin.controller;

import com.ejiaoyi.admin.support.AuthUser;
import com.ejiaoyi.admin.support.CurrentUserHolder;
import com.ejiaoyi.common.service.IMonitorService;
import com.ejiaoyi.common.annotation.UserLog;
import com.ejiaoyi.common.entity.Monitor;
import com.ejiaoyi.common.entity.Reg;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.service.IRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description:
 * @Auther: liuguoqiang
 * @Date: 2020/7/14 09:41
 */
@RestController
@RequestMapping("/monitor")
public class MonitorController {
    @Autowired
    private IMonitorService monitorService;
    @Autowired
    private IRegService regService;

    /**
     * 场地框架页
     * @return
     */
    @RequestMapping("/frameMonitorPage")
    public ModelAndView frameMonitorPage() {
        return new ModelAndView("/monitor/frameMonitor");
    }

    /**
     * 区域树页
     * @return
     */
    @RequestMapping("/treeMonitorPage")
    public ModelAndView treeMonitorPage(){
        ModelAndView mav = new ModelAndView("/monitor/treeMonitor");
        AuthUser user = CurrentUserHolder.getUser();
        mav.addObject("regId", user.getRegId());
        return mav;
    }

    /**
     * 场地表页
     * @param monitor
     * @return
     */
    @RequestMapping("/monitorTablePage")
    public ModelAndView MonitorTablePage(Monitor monitor){
        ModelAndView mav = new ModelAndView("/monitor/monitorTable");
        mav.addObject("monitor",monitor);
        return mav;
    }


    /**
     * 分页查询场地
     * @param monitor 部门对象
     * @return
     */
    @RequestMapping("/pagedMonitor")
    public String pagedMonitor(Monitor monitor) {
        return monitorService.pagedMonitor(monitor);
    }

    /**
     * 修改
     * @param monitor
     * @return
     */
    @RequestMapping("/updateMonitor")
    @UserLog(value = "'修改监控信息: monitor='+#monitor.toString()", dmlType = DMLType.INSERT)
    public Boolean updateMonitor(Monitor monitor){
            return 1 == monitorService.updateMonitor(monitor);
    }

    /**
     * 添加页
     * @param monitor
     * @return
     */
    @RequestMapping("/addMonitorPage")
    public ModelAndView addMonitorPage(Monitor monitor){
        ModelAndView mav = new ModelAndView("/monitor/addMonitor");
        Reg reg = regService.getRegById(monitor.getRegId());
        monitor.setRegName(reg.getRegName());
        monitor.setRegCode(reg.getRegNo());
        mav.addObject("monitor",monitor);
        return mav;
    }

    /**
     * 添加
     * @param monitor
     * @return
     */
    @RequestMapping("/addMonitor")
    @UserLog(value = "'添加监控信息: monitor='+#monitor.toString()", dmlType = DMLType.INSERT)
    public Boolean addMonitor(Monitor monitor){
        return 1==monitorService.addMonitor(monitor);
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @RequestMapping("/deleteMonitor")
    @UserLog(value = "'删除监控信息: ids='+#ids", dmlType = DMLType.DELETE)
    public Boolean deleteMonitor(Integer[] ids){
        if (ids!=null){
            return ids.length == monitorService.deleteMonitor(ids);
        }
        return false;
    }

    /**
     * 修改页
     * @param id
     * @return
     */
    @RequestMapping("/updateMonitorPage")
    public ModelAndView updateMonitorPage(Integer id){
        ModelAndView mav = new ModelAndView("/monitor/updateMonitor");
        mav.addObject("monitor",monitorService.getMonitorById(id));
        return mav;
    }

    /**
     * 查看页面
     * @param id
     * @return
     */
    @RequestMapping("/showMonitorPage")
    public ModelAndView showMonitorPage(Integer id){
        ModelAndView mav = new ModelAndView("/monitor/showMonitor");
        mav.addObject("monitor",monitorService.getMonitorById(id));
        return mav;
    }
}
