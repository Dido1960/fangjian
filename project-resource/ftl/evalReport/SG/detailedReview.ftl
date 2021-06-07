<head>
    <title>详细评审</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <#list gradeDtos as gradeDto>
        <div class="show">
            <#-- 显示的投标人数量-->
            <#assign showBidderCount = 6/>
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
                            <td colspan="9" align="left">
                                评标报告附件
                                <#if gradeDto_index == 0 || gradeDto_index == 1>
                                    <#--施工能力、施工方案或施工组织设计扣分表-->
                                    四
                                <#elseif gradeDto_index = 2>
                                    <#--安全质量事故扣分表-->
                                    五
                                <#elseif gradeDto_index == 3>
                                    <#--建筑市场不良记录扣分表-->
                                    六
                                </#if>
                            </td>
                        </tr>
                        <#if tableNum == 0 && gradeDto_index == 0>
                            <tr>
                                <td colspan="9" align="center">
                                    <div style="text-align: center" class="base-header-mid<#if tableNum ==0> first-title</#if>">详细评审</div>
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

                        <tr>
                            <td colspan="9" align="left">标段编号：${bidSection.bidSectionCode}</td>
                        </tr>
                        <tr class="base-border">
                            <td rowspan="2" style="width: 7%;text-align: center">序号</td>
                            <td rowspan="2" style="width: 44%;text-align: center">
                                评审项目
                            </td>
                            <td rowspan="2" style="width: 7%;text-align: center">扣分分值</td>
                            <#list i..j as num>
                                <td width="7%" align="center">
                                    <#if bidders[num]??>
                                        ${num + 1}
                                    </#if>
                                </td>
                            </#list>
                        </tr>
                        <tr class="base-border">
                            <#list i..j as num>
                                <td align="center">
                                    <#if bidders[num]??>${bidders[num].bidderName}</#if>
                                </td>
                            </#list>
                        </tr>
                        </thead>
                        <tbody>
                        <#--  此处遍历评分项-->
                        <#list gradeDto.gradeItemDTOs as item>
                            <tr class="base-border">
                                <td style="line-height: 1.6rem;text-align: center">${item_index + 1}</td>
                                <td style="line-height: 1.6rem;text-align: left">${item.gradeItemName}</td>
                                <td style="line-height: 1.6rem;text-align: center">${item.score}</td>
                                <#list i..j as num>
                                    <td align="center">
                                        <#if bidders[num]??>
                                            <#if item.bidderDTOs[num].gradeItemResult>
                                               ${item.bidderDTOs[num].gradeItemResult}
                                            </#if>
                                        </#if>
                                    </td>
                                </#list>
                            </tr>
                        </#list>
                        <tr class="base-border">
                            <td align="center" colspan="3">投标人扣分合计</td>
                            <#list i..j as num>
                                <#--扣分总分-->
                                <#assign deductSocre = 0/>
                                <#list gradeDto.gradeItemDTOs as item>
                                    <#if item.bidderDTOs[num].gradeItemResult>
                                        <#assign deductSocre = deductSocre?number + (item.bidderDTOs[num].gradeItemResult)?number />
                                    </#if>
                                </#list>
                                <td align="center">
                                    <#if bidders[num] ??>
                                        ${deductSocre}
                                    </#if>
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
                                <div style="width: 20%; float: left; text-align: left">监标人签字：</div>
                            </td>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </#list>
        </div>
    </#list>
</#escape>