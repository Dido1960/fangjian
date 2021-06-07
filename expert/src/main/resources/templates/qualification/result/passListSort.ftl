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
<link rel="stylesheet" type="text/css" href="${ctx}/css/rankingSummary.css">
<body style="overflow: auto;">
<div class="text-box">
    <ol>
        <li>序号</li>
        <li>投标单位</li>
        <li>初步评审</li>
        <li>详细评审</li>
        <li>资格预审结果</li>
    </ol>
    <table cellpadding=0 cellspacing=0>
        <#list bidders as bidder>
            <tr>
                <td>${bidder_index+1}</td>
                <td title="${bidder.bidderName}">${bidder.bidderName}</td>
                <#if bidder.preReviewResult == "1">
                    <td>通过</td>
                    <#if bidder.detailReviewResult == "1">
                        <td>通过</td>
                    <#else >
                        <td><span class="red-f">不通过</span></td>
                    </#if>
                <#else >
                    <td><span class="red-f">不通过</span></td>
                    <td>--</td>
                </#if>

                <#if bidder.reviewResult == "1">
                    <td>通过</td>
                <#else >
                    <td><span class="red-f">不通过</span></td>
                </#if>
            </tr>
        </#list>
    </table>
</div>
</body>