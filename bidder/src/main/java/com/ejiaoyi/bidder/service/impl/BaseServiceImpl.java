package com.ejiaoyi.bidder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ejiaoyi.bidder.service.IBaseService;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseServiceImpl implements IBaseService {
    @Override
    public Page getPageForLayUI() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        int pageSize=1;
        int limit=5;

        try {
            pageSize = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e){

        }
        try {
            limit = Integer.parseInt(request.getParameter("limit"));
        } catch (Exception e){

        }

        return new Page(pageSize, limit);
    }

    @Override
    public String initJsonForLayUI(List list, Integer count) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("code", 0);
        map.put("msg", "");
        map.put("count", count);
        map.put("data", list);

        return JSONObject.toJSONString(map);
    }

    @Override
    public String initJsonForLayUI(List list, Integer count, Integer code, String msg) {
        Map<String, Object> map = new HashMap<>(1);
        map.put("code", code);
        map.put("msg", msg);
        map.put("count", list.size());
        map.put("data", list);

        return JSONObject.toJSONString(map);
    }
}
