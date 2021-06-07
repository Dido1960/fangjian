<html lang="zh">
<head>
    <title>开标记录表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css">
</head>
<body>
<div class="cen-set">
    <div class="panel pagination">
        <table class="base-normal-table">
            <thead>
            <tr>
                <td colspan="6">附件二</td>
            </tr>
            <tr>
                <td colspan="6">
                    <div class="base-header first-title">开标记录表</div>
                </td>
            </tr>
            <tr>
                <td colspan="6">
                    <div>标段名称:${(bidSection.bidSectionName)!""}</div>
                </td>
            </tr>
            <tr>
                <td colspan="6">
                    <div>标段编号:${(bidSection.bidSectionCode)!""}</div>
                </td>
            </tr>
            <tr>
                <td colspan="6">
                    <div style="width: 50%;float: left">招标人:${(tenderProject.tendererName)!""}</div>
                    <div style="width: 49%;float: left">控制价:<#if tenderDoc.controlPrice?? && tenderDoc.controlPrice!=''>
                        ${((tenderDoc.controlPrice)?number)?string(",###.00")}元</#if>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="6">
                    <div style="width: 50%;float: left">浮动点:${(tenderDoc.floatPoint)!""}%</div>
                    <div style="width: 49%;float: left">开标时间:${(tenderDoc.bidOpenTime)!""}</div>
                </td>
            </tr>
            <tr>
                <td colspan="6" style="padding-bottom: 15px">
                    <div>开标地点:${(tenderDoc.bidOpenPlace)!""}</div>
                </td>
            </tr>
            <tr class="base-border" style="text-align: center">
                <td width="10%">序号</td>
                <td width="28%">投标人</td>
                <td width="20%">投标总价（元）</td>
                <td width="12%">投标工期<br>（日历天）</td>
                <td width="10%">质量</td>
                <td width="20%">法定代表人<br>或委托代理人签字</td>
            </tr>
            </thead>
            <tbody>
            <#if bidders?? && bidders?size gt 0>
                <#list bidders as bidder>
                    <tr class="base-border" style="text-align: center">
                        <td style="text-align: center">${bidder_index+1}</td>
                        <td style="text-align: left">${(bidder.bidderName)!""}</td>
                        <td>
                            <#if !bidder.bidderOpenInfo.bidPriceType?? || bidder.bidderOpenInfo.bidPriceType == "总价">
                                <#if bidder.bidderOpenInfo.bidPrice?? && bidder.bidderOpenInfo.bidPrice != ''>
                                    ${(bidder.bidderOpenInfo.bidPrice)?number?string(",###.##")!""}
                                </#if>
                            <#else>
                                ${bidder.bidderOpenInfo.bidPriceType}${bidder.bidderOpenInfo.bidPrice}
                            </#if>
                        </td>
                        <td>${(bidder.bidderOpenInfo.timeLimit)!""}</td>
                        <td>${(bidder.bidderOpenInfo.quality)!""}</td>
                        <td>
                            <#if bidSection.bidOpenOnline != 0>
                                ${(bidder.bidderOpenInfo.clientName)!""}
                            </#if>
                        </td>
                    </tr>
                </#list>
            <#else>
                <tr class="base-border">
                    <td>1</td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </#if>
            <tr class="base-border" style="text-align: center">
                <td>备注：</td>
                <td colspan="5" style="text-align: left">
                    <#if tenderDoc.openBidRecordDes??>
                        ${tenderDoc.openBidRecordDes}
                    <#else>
                        <#if biddersFail??>
                            <#list biddersFail as bidderFail>
                                <#if bidderFail.bidderOpenInfo??>
                                    <div>
                                        <span>${bidderFail_index+1}.${bidderFail.bidderName}:</span>
                                        <#if bidderFail.bidderOpenInfo.tenderRejection==1>
                                            标书拒绝,标书拒绝理由：${bidderFail.bidderOpenInfo.tenderRejectionReason} ;
                                        <#elseif bidderFail.bidderOpenInfo.notCheckin==1>
                                            未签到,未签到原因:迟到
                                        <#elseif bidderFail.bidderOpenInfo.notCheckin==2>
                                            未签到,未签到原因:弃标
                                        <#elseif bidderFail.bidderOpenInfo.notCheckin==9>
                                            拒绝原因：未递交
                                            <#if bidderFail.bidderOpenInfo.notCheckinReason?? && bidderFail.bidderOpenInfo.notCheckinReason != ''>
                                                ，${bidderFail.bidderOpenInfo.notCheckinReason}
                                            </#if>
                                        </#if>
                                    </div>
                                </#if>
                            </#list>
                        </#if>
                    </#if>
                </td>
            </tr>
            </tbody>
            <tfoot>
            <tr>
                <td colspan="6">
                    <div style="width: 24%;float: left">招标人:</div>
                    <div style="width: 24%;float: left">招标代理:</div>
                    <div style="width: 24%;float: left">监标人:</div>
                    <div style="width: 24%;float: left">时间：${date[0]}年${date[1]}月${date[2]}日</div>
                </td>
            </tr>
            </tfoot>
        </table>
    </div>
</div>
</body>
</html>