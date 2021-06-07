package com.ejiaoyi.common.service;


import com.ejiaoyi.common.entity.LatiLongitude;

import java.util.Map;

/**
 * @Description:
 * @Auther: samzqr
 * @Date: 2020-12-11 11:07
 */
public interface LatiLongitudeService {

    /**
     * 存入坐标
     * @param latiLongitude
     * @return
     */
    Boolean saveLatiLongitude(LatiLongitude latiLongitude);


    /**
     * 获取位置
     * @param bidSectionId
     * @return
     */
    Map<String, String> getBidderPoint(Integer bidSectionId);
//    String getBidderData(Integer bidSectionId);




}
