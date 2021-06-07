<head>
    <title>评标得分汇总表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <div class="show">
        <#-- 显示的投标人数量-->
        <#assign showBidderCount = 9/>
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
            <div class="panel pagination">
                <table width="100%" class="base-normal-table">
                    <thead>
                    <tr>
                        <#assign colspannum= 10/>
                        <td colspan="${colspannum}" align="left">
                            评标报告附件
                            <#if tenderDoc.mutualSecurityStatus == 1>
                                九<#assign colspannum= 11/>
                            <#else >
                                八
                            </#if>
                        </td>
                    </tr>
                    <#if tableNum == 0>
                        <tr>
                            <td colspan="${colspannum}" align="center">
                                <div style="text-align: center"
                                     class="base-header-mid<#if tableNum ==0> first-title</#if>">评标得分汇总表
                                </div>
                            </td>
                        </tr>
                    </#if>
                    <tr>
                        <td colspan="${colspannum}" align="left">标段名称：${bidSection.bidSectionName}</td>
                    </tr>
                    <tr>
                        <td colspan="${colspannum}" align="left">
                            <div style="width: 79%; float: left; text-align: left">标段编号：${bidSection.bidSectionCode}</div>
                            <div style="width: 20%; float: left; text-align: right">日期：${date[0]}年${date[1]}月${date[2]}日</div>
                        </td>
                    </tr>
                    <tr class="base-border">
                        <td style="width: 5%;text-align: center">序号</td>
                        <td style="width: 10%;text-align: center">投标人名称</td>
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
                    <#list i..j as num>
                        <#if evalResultSgs[num]??>
                            <tr class="base-border">
                                <td align="center" width="5%">
                                    ${num + 1}
                                </td>
                                <td align="center" width="10%">
                                    ${evalResultSgs[num].bidderName}
                                </td>
                                <td align="center" width="10%">
                                    <#if evalResultSgs[num].bidPrice??>
                                        ${((evalResultSgs[num].bidPrice)?number)?string(",###.00")}
                                    </#if>
                                </td>
                                <td align="center" width="10%">
                                    ${evalResultSgs[num].businessScore}
                                </td>
                                <td align="center" width="10%">
                                    ${evalResultSgs[num].ability}
                                </td>
                                <td align="center" width="10%">
                                    ${evalResultSgs[num].organize}
                                </td>
                                <td align="center" width="10%">
                                    ${evalResultSgs[num].quality}
                                </td>
                                <td align="center" width="10%">
                                    ${evalResultSgs[num].badRecord}
                                </td>
                                <#if tenderDoc.mutualSecurityStatus?? && tenderDoc.mutualSecurityStatus == 1>
                                    <td align="center" width="10%">
                                        ${evalResultSgs[num].mutualSecurity}
                                    </td>
                                </#if>
                                <td align="center" width="7%">
                                    ${evalResultSgs[num].totalScore}
                                </td>
                                <td align="center" width="7%">
                                    ${evalResultSgs[num].orderNo}
                                </td>
                            </tr>
                        </#if>
                    </#list>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="${colspannum}" style="padding-top: 20px;">
                                <div style="width: 20%; float: left; text-align: left">评标委员会成员签字：</div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="${colspannum}" style="padding-top: 20px;">
                                <div style="width: 20%; float: left">监标人签字：</div>
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </div>
        </#list>
    </div>
</#escape>