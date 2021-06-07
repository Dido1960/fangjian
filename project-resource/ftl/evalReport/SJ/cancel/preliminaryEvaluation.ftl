<head>
    <title>初步评审表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <#list gradeDtos as gradeDto>
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
                <div class="panel pagination">
                    <table width="100%" class="base-normal-table">
                        <thead>
                        <#if tableNum == 0 && gradeDto_index == 0>
                            <tr>
                                <td colspan="9" align="left">
                                    评标报告附件三
                                </td>
                            </tr>
                            <tr>
                                <td colspan="9" align="center">
                                    <div style="text-align: center" class="base-header-mid<#if tableNum ==0> first-title</#if>">初步评审表</div>
                                </td>
                            </tr>
                        </#if>
<#--                        <tr>-->
<#--                            <td colspan="9" align="center">-->
<#--                                <div class="<#if tableNum == 0>second-title </#if>base-header-small">${gradeDto.gradeName}</div>-->
<#--                            </td>-->
<#--                        </tr>-->
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
                                <td style="line-height: 1.6rem">${item.gradeItemName}</td>
                                <#list i..j as num>
                                    <td align="center">
                                        <#if bidders[num]??>
                                            <#if item.bidderDTOs[num].gradeItemResult?? && item.bidderDTOs[num].gradeItemResult == 1>
                                                合格
                                            <#else>
                                                不合格
                                            </#if>
                                        </#if>
                                    </td>
                                </#list>
                            </tr>
                        </#list>
                        <tr class="base-border">
                            <td align="center" colspan="2">结论</td>
                            <#list i..j as num>
                                <#assign nopass = 0/>
                                <#list gradeDto.gradeItemDTOs as item>
                                    <#if item.bidderDTOs[num].gradeItemResult?? && item.bidderDTOs[num].gradeItemResult != 1>
                                        <#assign nopass = nopass + 1 />
                                    </#if>
                                </#list>
                                <td align="center">
                                    <#if bidders[num] ??>
                                        <#if nopass gt 0>
                                            不通过
                                        <#else>
                                            通过
                                        </#if>
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