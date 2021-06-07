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
<body style="overflow: auto;">
<div class="text-box">
    <div class="cont-btn">
        专家认定
    </div>
    <ol>
        <li>候选人推荐</li>
        <li class="long">投标人</li>
        <li>综合得分</li>
    </ol>
    <div class="cont-table">
        <table cellpadding=0 cellspacing=0>
            <#if list?size gt 0>
                <tr>
                    <td>第一名</td>
                    <td class="long" title="${list[0].bidderName}">${list[0].bidderName}</td>
                    <td>${list[0].totalScore}</td>
                </tr>
            </#if>
            <#if list?size gt 1>
                <tr>
                    <td>第二名</td>
                    <td class="long" title="${list[1].bidderName}">${list[1].bidderName}</td>
                    <td>${list[1].totalScore}</td>
                </tr>
            </#if>
            <#if list?size gt 2>
                <tr>
                    <td>第三名</td>
                    <td class="long" title="${list[2].bidderName}">${list[2].bidderName}</td>
                    <td>${list[2].totalScore}</td>
                </tr>
            </#if>
        </table>
    </div>
</div>
</body>