//package com.ejiaoyi.common.service;
//
//import com.ejiaoyi.common.dto.ServiceResultDTO;
//import com.ejiaoyi.common.dto.quantity.DoParseQuantity;
//import com.ejiaoyi.common.dto.quantity.DoService;
//import com.ejiaoyi.common.dto.quantity.GetServiceResult;
//import com.ejiaoyi.common.dto.quantity.QuantityBidder;
//import com.ejiaoyi.common.enums.quantity.FloatPoint;
//import com.ejiaoyi.common.enums.quantity.PathType;
//import com.ejiaoyi.common.enums.quantity.QuantityServiceType;
//import com.ejiaoyi.common.enums.quantity.QuantityServiceVersion;
//import com.ejiaoyi.common.exception.CustomException;
//
//import java.util.List;
//
///**
// * 工程量清单处理 服务类
// *
// * @author Kevin
// * @since 2020-11-30
// */
//public interface IQuantityService {
//
//    /**
//     * 注册解析工程量清单文件
//     *
//     * @param path                   XML文件地址
//     * @param fileMd5                XML文件md5值
//     * @param pathType               XML文件类型
//     * @param quantityServiceVersion 服务版本序列号
//     * @return 工程量清单文件标识码
//     * @throws CustomException
//     */
//    String registerQuantity(String path, String fileMd5, PathType pathType, QuantityServiceVersion quantityServiceVersion) throws CustomException;
//
//    /**
//     * 获取TOKEN
//     *
//     * @return token
//     * @throws CustomException
//     */
//    String getToken() throws CustomException;
//
//    /**
//     * 获取TICKET
//     *
//     * @param token                  TOKEN
//     * @param quantityServiceVersion 服务版本号
//     * @param quantityServiceType    服务类型
//     * @return ticket
//     * @throws CustomException
//     */
//    String getTicket(String token, QuantityServiceVersion quantityServiceVersion, QuantityServiceType quantityServiceType) throws CustomException;
//
//    /**
//     * 注册工程量清单解析服务
//     *
//     * @param param 参数
//     * @return 工程量清单解析服务序列号
//     * @throws CustomException
//     */
//    String registerParseQuantity(DoParseQuantity param) throws CustomException;
//
//    /**
//     * 执行工程量清单文件解析
//     *
//     * @param serviceSerialNumber    注册工程量清单返回的服务号
//     * @return 执行工程量清单服务是否成功
//     * @throws CustomException
//     */
//    boolean doQuantity(String serviceSerialNumber) throws CustomException;
//
//    /**
//     * 获取工程量清单文件标识码
//     *
//     * @param param 参数
//     * @return 工程量清单文件标识码
//     * @throws CustomException
//     */
//    String getParseQuantityResult(GetServiceResult param) throws CustomException;
//
//    /**
//     * 创建整体分析服务
//     *
//     * @param tenderXmlUid           招标工程量清单文件标示码
//     * @param quantityBidderList     投标人列表
//     * @param quantityServiceVersion 服务版本序列号
//     * @return 整体分析服务序列号
//     * @throws CustomException
//     */
//    String createOverallAnalysis(String tenderXmlUid, List<QuantityBidder> quantityBidderList, QuantityServiceVersion quantityServiceVersion) throws CustomException;
//
//    /**
//     * 执行工程量清单报价得分计算服务
//     *
//     * @param serialNumber           整体分析服务序列号
//     * @param floatPoint             浮动点
//     * @param quantityServiceVersion 服务版本序列号
//     * @return 工程量清单报价得分计算服务序列号
//     * @throws CustomException
//     */
//    String calcQuantityScore(String serialNumber, FloatPoint floatPoint, QuantityServiceVersion quantityServiceVersion) throws CustomException;
//
//    /**
//     * 获取工程量清单报价得分计算结果
//     *
//     * @param serialNumber           工程量清单报价得分计算服务序列号
//     * @param quantityServiceVersion 服务版本序列号
//     * @return 结果JSON
//     * @throws CustomException
//     */
//    String getQuantityScoreResult(String serialNumber, QuantityServiceVersion quantityServiceVersion) throws CustomException;
//
//    /**
//     * 获取服务结果状态
//     *
//     * @param serialNumber           服务序列号
//     * @param quantityServiceVersion 服务版本序列号
//     * @return 服务结果状态 (带进度的)对应QuantityServiceState枚举
//     * @throws CustomException
//     */
//    ServiceResultDTO getServiceResult(String serialNumber, QuantityServiceVersion quantityServiceVersion) throws CustomException;
//
//    /**
//     * 获取服务结果状态
//     *
//     * @param serialNumberList       服务序列号组
//     * @param quantityServiceVersion 服务版本序列号
//     * @return 服务结果状态 (1: 完成 0: 未完成)
//     * @throws CustomException
//     */
//    String listServiceResult(List<String> serialNumberList, QuantityServiceVersion quantityServiceVersion) throws CustomException;
//}
