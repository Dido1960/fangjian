package com.ejiaoyi.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ejiaoyi.common.entity.LatiLongitude;
import com.ejiaoyi.common.mapper.LatiLongitudeMapper;
import com.ejiaoyi.common.service.LatiLongitudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Auther: samzqr
 * @Date: 2020-12-11 11:09
 */
@Service
public class LatiLongitudeServiceImpl extends BaseServiceImpl implements LatiLongitudeService {

    @Autowired
    LatiLongitudeMapper latiLongitudeMapper;

    @Override
    public Boolean saveLatiLongitude(LatiLongitude latiLongitude) {

            QueryWrapper<LatiLongitude> wrapper = new QueryWrapper<>();
            wrapper.eq("BIDDER_NAME", latiLongitude.getBidderName());
            wrapper.eq("BID_SECTION_ID", latiLongitude.getBidSectionId());
            List<LatiLongitude> list = latiLongitudeMapper.selectList(wrapper);

            if (list != null && list.size() > 0) {

                int update = latiLongitudeMapper.update(latiLongitude, wrapper);
                if (update == 1) {
                    return true;
                } else {
                    return false;
                }
            }

        int insert = latiLongitudeMapper.insert(latiLongitude);

        if (insert == 1) {
            return true;
        }


        return false;
    }

    @Override
    public Map<String, String> getBidderPoint(Integer bidSectionId) {
        Map<String, String> map = new HashMap<>();

        QueryWrapper<LatiLongitude> wrapper = new QueryWrapper<>();
        wrapper.eq("BID_SECTION_ID", bidSectionId);
        List<LatiLongitude> list = latiLongitudeMapper.selectList(wrapper);

//        坐标点json串
        StringBuilder res = new StringBuilder();
//        坐标数据json串
        StringBuilder res1 = new StringBuilder();
//        总人数
        String total="0";

//        最小id
        String min="0";
//        最大id
        String max="0";
//        判断list是否为空
        if (list != null && list.size() > 0) {
            total=list.size()+"";

//           最小值
            min=list.get(0).getId()+"";
//            最大值
            max=list.get(list.size()-1).getId()+"";

            res= res.append("{");
            res1=res1.append("[");
//            遍历构建json
            for (int i = 0; i < list.size(); i++) {
//                构建坐标串
                res = res.append("\"" + list.get(i).getBidderName() + "\"").append(":").append("[" + list.get(i).getLatiLongitude() + "]");
//              构建数据json
                res1 = res1.append("{name:\"" + list.get(i).getBidderName() + "\",").append("value:" + list.get(i).getId() + "}");
//          如果为最后一个数据
                if (i == list.size() - 1) {
                    res = res.append("}");
                    res1 = res1.append("]");
                } else {
                    res = res.append(",");
                    res1 = res1.append(",");
                }

            }
        }


            String point = JSON.toJSONString(res);
            String data = JSON.toJSONString(res1);
            map.put("point", point);
            map.put("data", data);
            map.put("total", total);
            map.put("min", min);
            map.put("max", max);
            return map;

        }





}
