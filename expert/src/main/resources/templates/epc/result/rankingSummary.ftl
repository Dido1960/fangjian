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
<style>
    ol li {
        width: 11.66%;
    }
    table tr td{
        width: 11.66%;
    }
</style>
<body style="overflow: auto;">
<div class="text-box">
    <ol>
        <li>序号</li>
        <li class="long">投标单位</li>
        <li>投标报价（元)</li>
        <li>详细评审得分</li>
        <li>投标报价得分</li>
        <li>综合得分</li>
        <li>评分排序</li>
    </ol>
    <table cellpadding=0 cellspacing=0>
        <#list list as rs>
            <tr>
                <td>${rs_index + 1}</td>
                <td class="long" title="${rs.bidderName}">${rs.bidderName}</td>
                <td>${rs.bidPrice?number?string(",###.00")}</td>
                <td>${rs.detailedScore}</td>
                <td>${rs.businessScore}</td>
                <td>${rs.totalScore}</td>
                <td>${rs.orderNo}</td>
            </tr>
        </#list>
    </table>
</div>
</body>