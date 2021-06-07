<meta charset="utf-8">
<title></title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
      content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
<!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
<script src="${ctx}/js/common.js"></script>
<script src="${ctx}/js/convertMoney.js"></script>
<link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
<script src="${ctx}/layuiAdmin/layui/layui.js"></script>
<script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>

<script src="${ctx}/js/base64.js"></script>
<link rel="stylesheet" href="${ctx}/css/colorBase.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/candidateResult.css">
<style>
    ol li{
        width: 15% ;
    }
    .cont-table table tr td{
        width: 15%;
    }
</style>
<body style="overflow: auto;">
<div class="text-box">
    <ol>
        <li>排名</li>
        <li style="width: 35%">投标单位</li>
        <li>投标报价（元)</li>
        <li style="width: 35%">理由</li>
    </ol>
    <div class="cont-table">
        <table cellpadding=0 cellspacing=0>
            <tr>
                <td>第一名</td>
                <td style="width: 35%">${firstBidder.bidderName}</td>
                <td>
                    <#if !firstBidder.bidderOpenInfo.bidPriceType?? || firstBidder.bidderOpenInfo.bidPriceType == "总价">
                        <#if firstBidder.bidderOpenInfo.bidPrice?? && firstBidder.bidderOpenInfo.bidPrice != ''>
                            ${((firstBidder.bidderOpenInfo.bidPrice)?number)?string(",###.##")}
                        </#if>
                    <#else>
                        ${firstBidder.bidderOpenInfo.bidPriceType}${firstBidder.bidderOpenInfo.bidPrice}
                    </#if>
                </td>
                <td style="width: 35%">${firstBidder.candidateSuccess.reason}</td>
            </tr>
            <tr>
                <td>第二名</td>
                <td style="width: 35%">${secondBidder.bidderName}</td>
                <td>
                    <#if !secondBidder.bidderOpenInfo.bidPriceType?? || secondBidder.bidderOpenInfo.bidPriceType == "总价">
                        <#if secondBidder.bidderOpenInfo.bidPrice?? && secondBidder.bidderOpenInfo.bidPrice != ''>
                            ${((secondBidder.bidderOpenInfo.bidPrice)?number)?string(",###.##")}
                        </#if>
                    <#else>
                        ${secondBidder.bidderOpenInfo.bidPriceType}${secondBidder.bidderOpenInfo.bidPrice}
                    </#if>
                </td>
                <td style="width: 35%">${secondBidder.candidateSuccess.reason}</td>
            </tr>
            <#if thirdBidder??>
                <tr>
                    <td>第三名</td>
                    <td style="width: 35%">${thirdBidder.bidderName}</td>
                    <td>
                        <#if !thirdBidder.bidderOpenInfo.bidPriceType?? || thirdBidder.bidderOpenInfo.bidPriceType == "总价">
                            <#if thirdBidder.bidderOpenInfo.bidPrice?? && thirdBidder.bidderOpenInfo.bidPrice != ''>
                                ${((thirdBidder.bidderOpenInfo.bidPrice)?number)?string(",###.##")}
                            </#if>
                        <#else>
                            ${thirdBidder.bidderOpenInfo.bidPriceType}${thirdBidder.bidderOpenInfo.bidPrice}
                        </#if>
                    </td>
                    <td style="width: 35%">${thirdBidder.candidateSuccess.reason}</td>
                </tr>
            </#if>
        </table>
    </div>
</div>
</body>