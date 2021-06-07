<head>
    <title>报价得分表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <div class="show">
        <#-- 显示的投标人数量-->
        <#assign showBidderCount = 7/>
        <#assign bidderNum =qualifiedBidders?size/>
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
                        <td colspan="9" align="left">
                            评标报告附件七
                        </td>
                    </tr>
                    <#if tableNum == 0>
                        <tr>
                            <td colspan="9" align="center">
                                <div style="text-align: center" class="base-header-mid<#if tableNum ==0> first-title</#if>">报价得分表</div>
                            </td>
                        </tr>
                    </#if>
                    <tr>
                        <td colspan="9" align="center">
                            <div class="<#if tableNum == 0>second-title </#if>base-header-small">${gradeDto.gradeName}</div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="9" align="left">标段名称：${bidSection.bidSectionName}</td>
                    </tr>
                    <tr class="base-border">
                        <td rowspan="2" style="width: 7%;text-align: center">序号</td>
                        <td rowspan="2" style="width: 20%;text-align: center">
                            名称
                        </td>
                        <td colspan="${showBidderCount}" style="text-align: center">各投标人报价得分</td>
                    </tr>
                    <tr class="base-border">
                        <#list i..j as num>
                            <td align="center" width="10%">
                                <#if bidders[num]??>${bidders[num].bidderName}</#if>
                            </td>
                        </#list>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="base-border">
                        <td align="center">
                            1
                        </td>
                        <td align="center">
                            分部分项工程量清单报价
                        </td>
                        <#list i..j as num>
                            <td align="center">
                                ${bidders[num].scoreA}
                            </td>
                        </#list>
                    </tr>
                    <tr class="base-border">
                        <td align="center">
                            2
                        </td>
                        <td align="center">
                            措施项目清单报价1
                        </td>
                        <#list i..j as num>
                            <td align="center">
                                ${bidders[num].scoreB1}
                            </td>
                        </#list>
                    </tr>
                    <tr class="base-border">
                        <td align="center">
                            3
                        </td>
                        <td align="center">
                            措施项目清单报价2
                        </td>
                        <#list i..j as num>
                            <td align="center">
                                ${bidders[num].scoreB2}
                            </td>
                        </#list>
                    </tr>
                    <tr class="base-border">
                        <td align="center">
                            4
                        </td>
                        <td align="center">
                            总承包服务费
                        </td>
                        <#list i..j as num>
                            <td align="center">
                                ${bidders[num].scoreC}
                            </td>
                        </#list>
                    </tr>
                    <tr class="base-border">
                        <td align="center">
                            5
                        </td>
                        <td align="center">
                            规费清单报价
                        </td>
                        <#list i..j as num>
                            <td align="center">
                                ${bidders[num].scoreD}
                            </td>
                        </#list>
                    </tr>
                    <tr class="base-border">
                        <td align="center">
                            6
                        </td>
                        <td align="center">
                            税金清单报价
                        </td>
                        <#list i..j as num>
                            <td align="center">
                                ${bidders[num].scoreE}
                            </td>
                        </#list>
                    </tr>
                    <tr class="base-border">
                        <td align="center">
                            7
                        </td>
                        <td align="center">
                            综合单价
                        </td>
                        <#list i..j as num>
                            <td align="center">
                                ${bidders[num].scoreF}
                            </td>
                        </#list>
                    </tr>
                    <tr class="base-border">
                        <td align="center">
                            8
                        </td>
                        <td align="center">
                            主要材料设备单价
                        </td>
                        <#list i..j as num>
                            <td align="center">
                                ${bidders[num].scoreG}
                            </td>
                        </#list>
                    </tr>
                    <tr class="base-border">
                        <td align="center">
                            9
                        </td>
                        <td align="center">
                            报价总分
                        </td>
                        <#list i..j as num>
                            <td align="center">
                                ${bidders[num].totalScore}
                            </td>
                        </#list>
                    </tr>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td colspan="9" style="padding-top: 20px;">
                            <div style="width: 20%; float: left; text-align: left">评标委员会成员签字：</div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="9" style="padding-top: 20px;">
                            <div style="width: 20%; text-align: left">监标人签字：</div>
                            <div style="float: right">日期：${date[0]}年${date[1]}月${date[2]}日</div>
                        </td>
                    </tr>
                    </tfoot>
                </table>
            </div>
        </#list>
    </div>
</#escape>