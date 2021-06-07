package com.ejiaoyi.admin.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.admin.entity.ReviewAuthorityLog;
import com.ejiaoyi.admin.mapper.ReviewAuthorityLogMapper;
import com.ejiaoyi.admin.service.IReviewAuthorityLogService;
import com.ejiaoyi.common.service.impl.BaseServiceImpl;
import com.ejiaoyi.common.support.DataSourceKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 离线审核权限写入日志 服务实现类
 * </p>
 *
 * @author Mike
 * @since 2021-01-13
 */
@Service
@DS(DataSourceKey.LOG)
public class ReviewAuthorityLogServiceImpl extends BaseServiceImpl implements IReviewAuthorityLogService {

    @Autowired
    private ReviewAuthorityLogMapper reviewAuthorityLogMapper;

    @Override
    public boolean saveReviewAuthorityLog(ReviewAuthorityLog reviewAuthorityLog) {
        return reviewAuthorityLogMapper.insert(reviewAuthorityLog) == 1;
    }

    @Override
    public String pagedReviewAuthorityLog(String username, Integer dmlType, String content, String searchTime) {
        Page page = this.getPageForLayUI();

        String startTime = "";
        String endTime = "";
        if (StringUtils.isNotEmpty(searchTime)) {
            final String[] timeArr = searchTime.split(" ~ ");
            startTime = timeArr[0];
            endTime = timeArr[1];
        }

        QueryWrapper<ReviewAuthorityLog> reviewAuthorityLogQueryWrapper = new QueryWrapper<>();
        reviewAuthorityLogQueryWrapper.like(StringUtils.isNotEmpty(username),"USER_NAME", username);
        reviewAuthorityLogQueryWrapper.eq(dmlType != null,"OPERATE_TYPE", dmlType);
        reviewAuthorityLogQueryWrapper.between(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime),"INSERT_TIME", startTime, endTime);
        reviewAuthorityLogQueryWrapper.orderByDesc("ID");

        List<ReviewAuthorityLog> list = reviewAuthorityLogMapper.selectPage(page, reviewAuthorityLogQueryWrapper).getRecords();

        return this.initJsonForLayUI(list, (int) page.getTotal());
    }
}
