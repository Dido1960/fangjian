package com.ejiaoyi.common.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 大华sdk,xml文件工具类
 *
 * @author Make
 * @since 2020/8/28
 */
public class DpSdkXmlUtil {
    /**
     * 解析大华SDK组织结构信息
     * @param orgInfoXml
     * @param roomName
     * @return
     */
    public static Map<String, List<Object>> parseDpSdkOrgXml(String orgInfoXml, String roomName) {
        Map<String, List<Object>> orgMap = new HashMap<>();

        try {
            Document xml = new SAXReader().read(new ByteArrayInputStream(orgInfoXml.getBytes(StandardCharsets.UTF_8)));

            List<Element> channels = xml.selectNodes("//UnitNodes[@type='1']/Channel[starts-with(@name, '" + roomName + "')]");

            for (Element channelEle : channels) {
                List<Object> channel = new ArrayList<>();
                Element device = (Element) channelEle.selectSingleNode("//parent::Device");

                channel.add(channelEle.attributeValue("id"));
                channel.add(device.attributeValue("status"));
                orgMap.put(channelEle.attributeValue("name"), channel);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return orgMap;
    }
}
