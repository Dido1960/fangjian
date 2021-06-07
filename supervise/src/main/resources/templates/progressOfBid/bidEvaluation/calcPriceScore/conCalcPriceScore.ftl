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
    .long {
        width: 40% !important;
    }

    ol li {
        width: 20%;
    }

    table tr td {
        width: 20%;
    }

</style>
<body style="overflow: auto;">
<div class="text-box">
    <table cellpadding=0 cellspacing=0>
        <thead style="  width: 100%;
    height: 61px;
    background: #cfdae5;
    opacity: 1;
    border-radius: 8px 8px 0px 0px;">
        <tr>
            <td style="width: 20%">序号</td>
            <td style="width: 40%">投标单位</td>
            <td style="width: 20%"> 投标报价</td>
            <td style="width: 20%">得分</td>
        </tr>
        </thead>
        <tbody>
        <#list list as rs>
            <tr>
                <td>${rs_index + 1}</td>
                <td title="${rs.bidderName}">${rs.bidderName}</td>
                <td>${rs.bidPrice}</td>
                <td>${rs.businessScore}</td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>