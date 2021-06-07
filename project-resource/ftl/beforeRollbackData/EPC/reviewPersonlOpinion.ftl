<head>
    <title>评标委员会成员个人评审意见表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <div class="show">
<#--一行显示的投标人数量-->
    <#assign showBidderCount = 3/>
<#--投标人数量-->
    <#assign bidderNum = bidders?size/>
<#--计算循环完所有投标人的次数（横向循环次数）-->
    <#if bidderNum lt showBidderCount>
        <#assign endNum=1/>
    <#elseif bidderNum % showBidderCount == 0>
        <#assign endNum=(bidderNum/showBidderCount)/>
    <#else>
        <#assign endNum=((bidderNum/showBidderCount)+1)/>
    </#if>

    <#list expertUsers as expert>
        <#list 0..(endNum-1) as tableNum>
            <#assign i=(tableNum*showBidderCount) j=(tableNum*showBidderCount+(showBidderCount-1))>
            <div class="panel pagination">
                <table height="100%" class="base-normal-table base-write">
                    <thead>
                    <tr>
                        <td colspan="4" align="center">
                            <div class="base-header-mid second-title base-bold">评标委员会成员个人评审意见表</div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" align="left">标段名称：${bidSection.bidSectionName}</td>
                    </tr>
                    <tr class="base-border base-bold">
                        <td align="center" width="20%">投标人名称</td>
                        <td align="center" width="20%">评审步骤</td>
                        <td align="center" width="20%">评审标段名称</td>
                        <td align="center" width="40%">评审意见</td>
                    </tr>
                    </thead>
                    <tbody>
                    <#if bidders>
                        <#list bidders as bidder>
                            <#list bidder.gradeItemMap?values as gradeItem>
                                <tr class="base-border">
                                    <#if gradeItem_index == 0>
                                        <td align="center" rowspan="${bidder.gradeItemMap?size}">
                                            ${bidder.bidderName}
                                        </td>
                                        <td>
                                            评审环节：${gradeItem.reviewProcess} <br>
                                            <#if gradeItem.reviewType>
                                                评审类型：${gradeItem.reviewType}
                                            </#if>
                                        </td>
                                    </#if>
                                    <td style="text-align: left;line-height: 1.6rem">${gradeItem.gradeItemName}</td>
                                    <td align="left">
                                        <#list gradeItem.expertReviewDetailDTOS as result>
                                            <#if expert.id == result.expertId>
                                                ${result.evalComments}
                                            </#if>
                                        </#list>
                                    </td>
                                </tr>
                            </#list>
                        </#list>
                    <#else>
                        <tr class="base-border">
                            <td>&emsp;</td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                    </#if>
                    </tbody>
                </table>
            </div>
        </#list>
        </div>
    </#list>
</#escape>