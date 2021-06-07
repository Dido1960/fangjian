package com.ejiaoyi.common.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.common.entity.NetworkLog;
import com.ejiaoyi.common.mapper.NetworkLogMapper;
import com.ejiaoyi.common.service.INetworkService;
import com.ejiaoyi.common.support.DataSourceKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 网络日志处理逻辑
 * </p>
 *
 * @author fengjunhong
 * @date 2020/4/16
 */
@Service
@DS(DataSourceKey.LOG)
public class NetworkLogServiceImpl extends BaseServiceImpl implements INetworkService {

    @Autowired
    NetworkLogMapper networkLogMapper;

    @Override
    public void addLog(NetworkLog networkLog) {
        networkLogMapper.insert(networkLog);
    }

    @Override
    public String pagedWebLog(String userName, String searchTime, String requestURI, String requestMethod, String processingTime) {
        Page page = this.getPageForLayUI();
        String startTime = "";
        String endTime = "";
        if (StringUtils.isNotEmpty(searchTime)) {
            final String[] timeArr = searchTime.split(" ~ ");

            startTime = timeArr[0];
            endTime = timeArr[1];
        }

        QueryWrapper<NetworkLog> networkQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(userName)) {
            networkQueryWrapper.like("USER_NAME", userName);
        }
        if (StringUtils.isNotEmpty(searchTime)) {
            networkQueryWrapper.like("SEARCH_TIME", searchTime);
        }
        if (StringUtils.isNotEmpty(requestURI)) {
            networkQueryWrapper.like("REQUEST_URI", requestURI);
        }
        if (StringUtils.isNotEmpty(requestMethod)) {
            networkQueryWrapper.like("REQUEST_METHOD", requestMethod);
        }
        if (StringUtils.isNotEmpty(processingTime)) {
            networkQueryWrapper.gt("PROCESSING_TIME", processingTime);
        }

        if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
            networkQueryWrapper.between("CREATE_TIME", startTime, endTime);
        }

        networkQueryWrapper.orderByDesc("ID");


        List<NetworkLog> list = networkLogMapper.selectPage(page, networkQueryWrapper).getRecords();
        return this.initJsonForLayUI(list, (int) page.getTotal());
    }
}
