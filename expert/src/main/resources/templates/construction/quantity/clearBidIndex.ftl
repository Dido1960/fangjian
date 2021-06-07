<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>甘肃省房建市政电子辅助评标系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/plugin/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${ctx}/plugin/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/header.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/quantity/clearing.css">
</head>

<body>
<#include "${ctx}/include/header.ftl">
<section>
    <div class="head">
        <div>
            <p>标段名称</p><span>${bidSection.bidSectionName}</span>
        </div>
        <div>
            <p>标段编号</p><span>${bidSection.bidSectionCode}</span>
        </div>
    </div>
    <div class="cont">
        <ul>
            <li onclick="clearBidResult()">
                <img src="${ctx}/img/clear.png" alt="">
                <p>清标分析</p>
            </li>
            <li onclick="economicAnalysis()">
                <img src="${ctx}/img/rnb.png" alt="">
                <p>经济标分析</p>
            </li>
        </ul>
        <div class="foot">
            <button class="blove-b " style="padding: 1px;width: 90px" title="点击回退主页" onclick="window.location.href='/expert/startEval'">
                <i class="layui-icon layui-icon-home"></i>
                回到主页
            </button>
            清标完成！用时<span>${bidSectionRelate.clearTotalTime}</span>
        </div>
    </div>
</section>
</body>
<script>
    /**
     * 清标分析
     */
    function clearBidResult() {
        window.location.href = "${ctx}/clearBid/clearBidResultIndex";
    }

    /**
     * 经济标分析
     */
    function economicAnalysis() {
        window.location.href = "${ctx}/clearBid/economicAnalysisIndex";
    }
</script>
</html>