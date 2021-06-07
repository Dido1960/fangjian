<html lang="zh">
<head>
    <title>投标人签到表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css">
</head>
<body>
<div class="cen-set">
    <div class="panel pagination">
        <table class="base-normal-table">
            <thead>
            <tr>
                <td colspan="6">
                    <div class="base-header first-title">投标人签到表</div>
                </td>
            </tr>
            <tr>
                <td colspan="6">
                    <div>标段名称:${(bidSection.bidSectionName)!""}</div>
                </td>
            </tr>
            <tr>
                <td colspan="6">
                    <div style="width: 50%;float: left">招标单位:${(tenderProject.tendererName)!""}</div>
                    <div style="width: 49%;float: left">开标时间:${(tenderDoc.bidOpenTime)!""}</div>
                </td>
            </tr>
            <tr>
                <td colspan="6" style="padding-bottom: 15px">
                    <div>开标地点:${(tenderDoc.bidOpenPlace)!""}</div>
                </td>
            </tr>
            <tr class="base-border" style="text-align: center">
                <td  width="10%">序号</td>
                <td>投标单位</td>
                <td>授权委托人</td>
                <td>签到时间</td>
            </tr>
            </thead>
            <tbody>
            <#if allBidders?? && allBidders?size gt 0>
                <#assign num = 0>
                <#list allBidders as bidder>
                    <#if bidder.bidderOpenInfo.signinTime??>
                        <#assign num = num + 1>
                        <tr class="base-border">
                            <td>${num}</td>
                            <td>${(bidder.bidderName)!""}</td>
                            <td>${(bidder.bidderOpenInfo.clientName)!""}</td>
                            <td>${(bidder.bidderOpenInfo.signinTime)!""}</td>
                        </tr>
                    </#if>
                </#list>
            <#else>
                <tr class="base-border">
                    <td>&nbsp;</td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            </#if>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>