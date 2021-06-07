<head>
    <title>评标得分汇总表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <div class="show">
        <#-- 显示的投标人数量-->
        <#assign showBidderCount = 7/>
        <#assign bidderNum =bidders?size/>
        <#assign isShow = true/>
        <#if bidderNum<showBidderCount>
            <#assign endNum=1/>
        <#elseif bidderNum%showBidderCount==0 >
            <#assign endNum=(bidderNum/showBidderCount)/>
        <#else>
            <#assign endNum=((bidderNum/showBidderCount)+1)/>
        </#if>
        <#assign count =bidderNum/>
        <#list 0..(endNum-1) as tableNum>
            <#assign i=(tableNum*showBidderCount) j=(tableNum*showBidderCount+(showBidderCount-1))>
        <#--初步评审表-->
            <#assign colspannum= 10/>
            <#if tenderDoc.mutualSecurityStatus == 1>
                <#assign colspannum= 11/>
            </#if>
            <div class="panel pagination">
                <table width="100%" class="base-normal-table">
                    <thead>
                    <#if tableNum == 0>
                        <tr>
                            <td colspan="${colspannum}" align="center">
                                <div style="text-align: center"
                                     class="base-header-mid<#if tableNum ==0> second-title</#if>">评标得分汇总表
                                </div>
                            </td>
                        </tr>
                    </#if>
                    <tr>
                        <td colspan="${colspannum}" align="left">标段名称：${bidSection.bidSectionName}</td>
                    </tr>
                    <tr>
                        <td colspan="${colspannum}" align="left">日期：${date[0]}年${date[1]}月${date[2]}日</td>
                    </tr>
                    <tr class="base-border">
                        <td style="width: 5%;text-align: center">序号</td>
                        <td style="width: 10%;text-align: center">
                            投标人名称
                        </td>
                        <td style="width: 10%;text-align: center">投标报价(元)</td>
                        <td style="width: 10%;text-align: center">投标报价得分</td>
                        <td style="width: 10%;text-align: center">施工能力扣分</td>
                        <td style="width: 10%;text-align: center">施工组织设计扣分</td>
                        <td style="width: 10%;text-align: center">安全质量事故扣分</td>
                        <td style="width: 10%;text-align: center">建筑市场不良记录扣分</td>
                        <#if tenderDoc.mutualSecurityStatus?? && tenderDoc.mutualSecurityStatus == 1>
                            <td style="width: 10%;text-align: center">互保共建</td>
                        </#if>
                        <td style="width: 7%;text-align: center">总得分</td>
                        <td style="width: 7%;text-align: center">名次</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#if evalResultSgs>
                        <#list evalResultSgs as evalResult>
                            <tr class="base-border">
                                <td align="center" width="10%">
                                    ${evalResult_index+1}
                                </td>
                                <td align="center" width="10%">
                                    ${evalResult.bidderName}
                                </td>
                                <td align="center" width="10%">
                                    <#if evalResult.bidPrice??>
                                        ${((evalResult.bidPrice)?number)?string(",###.00")}
                                    </#if>
                                </td>
                                <td align="center" width="10%">
                                    ${evalResult.businessScore}
                                </td>
                                <td align="center" width="10%">
                                    ${evalResult.ability}
                                </td>
                                <td align="center" width="10%">
                                    ${evalResult.organize}
                                </td>
                                <td align="center" width="10%">
                                    ${evalResult.quality}
                                </td>
                                <td align="center" width="10%">
                                    ${evalResult.badRecord}
                                </td>
                                <#if tenderDoc.mutualSecurityStatus?? && tenderDoc.mutualSecurityStatus == 1>
                                    <td align="center" width="10%">
                                        ${evalResult.mutualSecurity}
                                    </td>
                                </#if>
                                <td align="center" width="10%">
                                    ${evalResult.totalScore}
                                </td>
                                <td align="center" width="10%">
                                    ${evalResult.orderNo}
                                </td>
                            </tr>
                        </#list>
                    <#else >
                        <tr class="base-border">
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <#if tenderDoc.mutualSecurityStatus?? && tenderDoc.mutualSecurityStatus == 1>
                                <td align="center" width="10%"></td>
                            </#if>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                        </tr>
                        <tr class="base-border">
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <#if tenderDoc.mutualSecurityStatus?? && tenderDoc.mutualSecurityStatus == 1>
                                <td align="center" width="10%"></td>
                            </#if>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                        </tr>
                        <tr class="base-border">
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                            <#if tenderDoc.mutualSecurityStatus?? && tenderDoc.mutualSecurityStatus == 1>
                                <td align="center" width="10%"></td>
                            </#if>
                            <td align="center" width="10%"></td>
                            <td align="center" width="10%"></td>
                        </tr>
                    </#if>
                    </tbody>
                </table>
            </div>
        </#list>
    </div>
</#escape>