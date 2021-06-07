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
    <link rel="stylesheet" href="${ctx}/css/economic/process.css">
</head>
<body>
<#include "${ctx}/include/header.ftl">
<section>
    <div class="process">
        <h3><span onclick="returnClearBidFlow()">返回</span>${bidSection.bidSectionName}</h3>
        <#assign stepNum = 1>
        <ul>
            <li class="sele" onclick="loadFrame('${quantityResultParam.viewTotalOfferAnalysisUrl}')">
                <div class="flow-left" style="opacity: 0;"></div>
                <div class="flow-num" >${stepNum}</div>
                <div class="flow-right"></div>
                <p>总报价</p>
                <#assign stepNum = stepNum + 1>
            </li>
            <li onclick="loadFrame('${quantityResultParam.viewPartialListAnalysisUrl}')">
                <div class="flow-left"></div>
                <div class="flow-num" >${stepNum}</div>
                <div class="flow-right"></div>
                <p>分部分项清单</p>
                <#assign stepNum = stepNum + 1>
            </li>
            <li onclick="loadFrame('${quantityResultParam.viewMeasuresListAnalysisUrl}')">
                <div class="flow-left"></div>
                <div class="flow-num" >${stepNum}</div>
                <div class="flow-right"></div>
                <p>措施项目清单</p>
                <#assign stepNum = stepNum + 1>
            </li>
            <li onclick="loadFrame('${quantityResultParam.viewOtherListAnalysisUrl}')">
                <div class="flow-left"></div>
                <div class="flow-num" >${stepNum}</div>
                <div class="flow-right"></div>
                <p>其他项目清单</p>
                <#assign stepNum = stepNum + 1>
            </li>
            <li onclick="loadFrame('${quantityResultParam.viewFeesAnalysisUrl}')">
                <div class="flow-left"></div>
                <div class="flow-num" >${stepNum}</div>
                <p>规费税金</p>
            </li>
        </ul>
    </div>
    <form id="myForm" method="post" target="myFrame">
        <input type="hidden" name="api_key" value="${quantityResultParam.economyApiKey}"/>
        <input type="hidden" name="platform" value="${quantityResultParam.economyPlatform}"/>
        <input type="hidden" name="overall_analysis_service_serial_number" value="${bidSectionRelate.clearAnalysisUid}"/>
    </form>
    <iframe name="myFrame" style="width: 100%; height: calc(100vh - 334px); min-height: 625px;" frameborder="0" scrolling="auto"></iframe>
</section>
</body>
<script>
    $(function () {
        $(".process ul li").on("click",   function () {
            var $li = $(this) ;
            $li.addClass("sele").siblings().removeClass("sele");
        })
        $(".process ul li:first .flow-num").click();
    })

    function loadFrame(url) {
        $("#myForm").attr("action", url);
        $("#myForm").submit();
    }
</script>
</html>