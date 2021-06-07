package com.ejiaoyi.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ejiaoyi.common.entity.OnlineInfo;
import com.ejiaoyi.common.mapper.OnlineInfoMapper;
import com.ejiaoyi.common.service.IOnlineInfoService;
import com.ejiaoyi.common.util.BDIPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 在线用户信息表 服务实现类
 * </p>
 *
 * @author lesgod
 * @since 2021-01-16
 */
@Service
public class OnlineInfoServiceImpl extends ServiceImpl<OnlineInfoMapper, OnlineInfo> implements IOnlineInfoService {

    @Autowired
    OnlineInfoMapper onlineInfoMapper;

    @Override
    public void saveOrUpdateOnline(OnlineInfo onlineInfo) {
        onlineInfo.setId(onlineInfoMapper.selectOnlineBySessionId(onlineInfo.getSessionId()));
        if(onlineInfo.getId()==null){
            onlineInfo.setAddressInfo(BDIPUtils.addressDescribe(onlineInfo.getIpInfo()));
        }
        saveOrUpdate(onlineInfo);
    }

    @Override
    public void removeBySessionId(String sessionId) {
        onlineInfoMapper.removeBySessionId(sessionId);
    }

    @Override
    public String pagedOnlineInfo() {
        Page page = this.getPageForLayUI();
        List<OnlineInfo> list = onlineInfoMapper.selectPage(page, null).getRecords();
        return this.initJsonForLayUI(list, (int) page.getTotal());
    }

    public Page getPageForLayUI() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        int pageSize = 1;
        int limit = 7;

        try {
            pageSize = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {

        }
        try {
            limit = Integer.parseInt(request.getParameter("limit"));
        } catch (Exception e) {

        }
        return new Page(pageSize, limit);
    }

    public String initJsonForLayUI(List list, Integer count) {
        Map<String, Object> map = new HashMap<String, Object>(1) {{
            put("code", 0);
            put("msg", "");
            put("count", count);
            put("data", list);
        }};

        return JSONObject.toJSONString(map);
    }

    public String initJsonForLayUI(List list, Integer count, Integer code, String msg) {
        Map<String, Object> map = new HashMap<String, Object>(1) {{
            put("code", code);
            put("msg", msg);
            put("count", list.size());
            put("data", list);
        }};

        return JSONObject.toJSONString(map);
    }
}
