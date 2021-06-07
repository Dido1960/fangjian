package com.ejiaoyi.common.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.UserLog;
import com.ejiaoyi.common.enums.DMLType;
import com.ejiaoyi.common.enums.TimeFormatter;
import com.ejiaoyi.common.mapper.UserLogMapper;
import com.ejiaoyi.common.service.IUserLogService;
import com.ejiaoyi.common.support.DataSourceKey;
import com.ejiaoyi.common.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * <p>
 * 用户日志处理逻辑
 * </p>
 *
 * @author fengjunhong
 * @since 2020/4/16
 */
@Service
@DS(DataSourceKey.LOG)
public class UserLogServiceImpl extends BaseServiceImpl implements IUserLogService {

    @Autowired
    private UserLogMapper userLogMapper;

    @Override
    public void addLog(UserLog userLog) {
        userLogMapper.insert(userLog);
    }

    @Override
    public void addUserLog(DMLType dmlType, String content, String userName, Integer userId) {
        UserLog userLog = UserLog.builder().
                content(content).
                dmlType(dmlType.getName()).
                createTime(LocalDateTime.parse(DateTimeUtil.getInternetTime(TimeFormatter.YYYY_HH_DD_HH_MM_SS), DateTimeFormatter.ofPattern(TimeFormatter.YYYY_HH_DD_HH_MM_SS.getCode(), Locale.CHINA)))
                .build();
            userLog.setUsername(userName);
            userLog.setUserId(userId);

        // 用户日志，插入数据库
        userLogMapper.insert(userLog);
    }

    @Override
    public String pagedUser(String username, String dmlType, String content, String searchTime, String dmlTypeName) {
        Page page = this.getPageForLayUI();

        String startTime = "";
        String endTime = "";
        if (StringUtils.isNotEmpty(searchTime)) {
            final String[] timeArr = searchTime.split(" ~ ");

            startTime = timeArr[0];
            endTime = timeArr[1];
        }

        QueryWrapper<UserLog> userLogQueryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(username)) {
            userLogQueryWrapper.like("USERNAME", username);
        }

        if (StringUtils.isNotEmpty(content)) {
            userLogQueryWrapper.like("CONTENT", content);
        }

        if (StringUtils.isNotEmpty(dmlTypeName)) {
            userLogQueryWrapper.eq("DML_TYPE", dmlTypeName);
        }

        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            userLogQueryWrapper.between("CREATE_TIME", startTime, endTime);
        }

        userLogQueryWrapper.orderByDesc("ID");

        List<UserLog> list = userLogMapper.selectPage(page, userLogQueryWrapper).getRecords();

        return this.initJsonForLayUI(list, (int) page.getTotal());
    }
}
