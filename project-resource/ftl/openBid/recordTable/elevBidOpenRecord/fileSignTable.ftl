<html lang="zh">
<head>
    <title>投标文件递交时间签字表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css">
</head>
<body>
<div class="cen-set">
    <div class="panel pagination">
        <table class="base-normal-table">
            <thead>
            <tr>
                <td colspan="4">
                    <div class="base-header first-title">投标文件递交时间签字表</div>
                </td>
            </tr>
            <tr>
                <td colspan="4">
                    <div>标段名称:${(bidSection.bidSectionName)!""}</div>
                </td>
            </tr>
            <tr>
                <td colspan="4" style="padding-bottom: 15px">
                    <div>招标单位:${(tenderProject.tendererName)!""}</div>
                </td>
            </tr>
            <tr class="base-border" style="text-align: center">
                <td width="10%">序号</td>
                <td width="40%">投标单位</td>
                <td width="30%">递交时间</td>
                <td width="20%">签字</td>
            </tr>
            </thead>
            <tbody>
            <#if allBidders?? && allBidders?size gt 0>
                <#assign num = 0>
                <#list allBidders as bidder>
                    <#if bidder.bidDocId??>
                        <#assign num = num + 1>
                        <tr class="base-border">
                            <td style="text-align: center">${num}</td>
                            <td style="text-align: left">${(bidder.bidderName)!""}</td>
                            <td>${(bidder.bidderOpenInfo.upfileTime)!""}</td>
                            <td>${(bidder.bidderOpenInfo.clientName)!""}</td>
                        </tr>
                    </#if>
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
            <tfoot>
            <tr>
                <td colspan="4">
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