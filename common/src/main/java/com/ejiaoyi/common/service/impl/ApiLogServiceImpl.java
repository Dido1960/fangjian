package com.ejiaoyi.common.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.ApiLog;
import com.ejiaoyi.common.mapper.ApiLogMapper;
import com.ejiaoyi.common.service.IApiLogService;
import com.ejiaoyi.common.support.DataSourceKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@DS(DataSourceKey.LOG)
public class ApiLogServiceImpl extends BaseServiceImpl implements IApiLogService {

    @Autowired
    private ApiLogMapper apiLogMapper;

    @Override
    public Integer addLog(ApiLog apiLog) {
        int result = apiLogMapper.insert(apiLog);

        return result == 1 ? apiLog.getId() : -1;
    }

    @Override
    public void updateLog(ApiLog apiLog) {
        apiLogMapper.updateById(apiLog);
    }

    @Override
    public String pagedApiLog(String methodName, String searchTime, String param, String platform, String apiKey, String responseTime) {
        Page page = this.getPageForLayUI();

        String startTime = "";
        String endTime = "";
        if (StringUtils.isNotEmpty(searchTime)) {
            final String[] timeArr = searchTime.split(" ~ ");

            startTime = timeArr[0];
            endTime = timeArr[1];
        }

        QueryWrapper<ApiLog> apiLogQueryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotEmpty(methodName)) {
            apiLogQueryWrapper.like("METHOD_NAME", methodName);
        }

        if (StringUtils.isNotEmpty(param)) {
            apiLogQueryWrapper.like("PARAMS", param);
        }

        if (StringUtils.isNotEmpty(platform)) {
            apiLogQueryWrapper.like("PLATFORM", platform);
        }

        if (StringUtils.isNotEmpty(apiKey)) {
            apiLogQueryWrapper.like("API_KEY", apiKey);
        }

        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            apiLogQueryWrapper.between("CREATE_API_TIME", startTime, endTime);
        }

        if (StringUtils.isNotEmpty(responseTime)) {
            apiLogQueryWrapper.like("RESPONSE_TIME_CONSUME", responseTime);
        }

        apiLogQueryWrapper.orderByDesc("ID");

        List<ApiLog> list = apiLogMapper.selectPage(page, apiLogQueryWrapper).getRecords();
        return this.initJsonForLayUI(list, (int) page.getTotal());
    }
}
