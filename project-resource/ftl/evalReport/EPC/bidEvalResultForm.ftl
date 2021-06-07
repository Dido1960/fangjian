<head>
    <title>评标得分汇总表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <div class="show">
        <#-- 显示的投标人数量-->
        <#assign showBidderCount = 9/>
        <#assign bidderNum =evalResults?size/>
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
                    <#if tableNum == 0>
                        <tr>
                            <td colspan="4" align="center">
                                <div style="text-align: center"
                                     class="base-header-mid<#if tableNum ==0> first-title</#if>">评标得分汇总表
                                </div>
                            </td>
                        </tr>
                    </#if>
                    <tr>
                        <td colspan="4" align="left">标段名称：${bidSection.bidSectionName}</td>
                    </tr>
                    <tr>
                        <td colspan="4" align="left">
                            <div style="width: 79%; float: left; text-align: left">标段编号：${bidSection.bidSectionCode}</div>
                            <div style="width: 20%; float: left; text-align: right">日期：${date[0]}年${date[1]}月${date[2]}日</div>
                        </td>
                    </tr>
                    <tr class="base-border">
                        <td style="width: 50%;text-align: center">投标人名称</td>
                        <td style="width: 25%;text-align: center">投标报价(元)</td>
                        <td style="width: 15%;text-align: center">总得分</td>
                        <td style="width: 10%;text-align: center">名次</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#if evalResults>
                        <#list i..j as num>
                            <#if evalResults[num]??>
                                <tr class="base-border">
                                    <td align="left">
                                        ${evalResults[num].bidderName}
                                    </td>
                                    <td align="center">
                                        ${evalResults[num].bidPrice}
                                    </td>
                                    <td align="center">
                                        ${evalResults[num].totalScore}
                                    </td>
                                    <td align="center">
                                        ${evalResults[num].orderNo}
                                    </td>
                                </tr>
                            </#if>
                        </#list>
                    <#else >
                        <tr class="base-border">
                            <td align="center">&emsp;</td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                        </tr>
                        <tr class="base-border">
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                        </tr>
                        <tr class="base-border">
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                            <td align="center"></td>
                        </tr>
                    </#if>
                    </tbody>
                </table>
            </div>
        </#list>
    </div>
</#escape>