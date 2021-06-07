package com.ejiaoyi.common.dto;

/**
 * 发票需要的常量参数
 *
 * @author lesgod
 * @return
 * @date 2020-6-9 10:46
 */
public interface InvoiceConstant {

    /*## 航信发票请求地址

    ## 开票接口地址
    ##invoice_kp=http://nnfpbox.nuonuocs.cn/shop/buyer/allow/cxfKp/cxfServerKpOrderSync.action
      invoice_kp=http://nnfp.jss.com.cn/shop/buyer/allow/cxfKp/cxfServerKpOrderSync.action
    ## 结果查询接口地址
    ##invoice_query=http://nnfpbox.nuonuocs.cn/shop/buyer/allow/ecOd/queryElectricKp.action
    invoice_query=http://nnfp.jss.com.cn/shop/buyer/allow/ecOd/queryElectricKp.action
    ## 请求流水号查询接口地址
    invoice_query_serial=http://nnfp.jss.com.cn/shop/buyer/allow/ecOd/queryElectricKp.action
    */


    /**
     * 开票地址
     * @return
     * @author lesgod
     * @date 2020-6-9 13:21
     */
     String API_MAKE_OUT_INVOICE="http://nnfpbox.nuonuocs.cn/shop/buyer/allow/cxfKp/cxfServerKpOrderSync.action";

    /**
     * 请求流水号查询接口地址
     *
     * @author lesgod
     * @date 2020-6-9 11:00
     */
    String API_QUERY_SERIAL = "http://nnfpbox.nuonuocs.cn/shop/buyer/allow/ecOd/queryElectricKp.action";

    /**
     *
     * @author lesgod
     * @date 2020-6-9 11:02
     */
    String APP_CARD = "2329CC5F90EDAA8208F1F3C72A0CE72A713A9D425CD50CDE";
}
