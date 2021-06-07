<head>
    <meta charset="UTF-8">
    <title>甘肃省房建市政电子辅助评标系统</title>
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
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/verifySignature.css">
</head>
<body>
<div class="left">
    <div class="left-title">
        投标单位
    </div>
    <div class="left-list">
        <#list bidders as bidder>
            <div <#if bidder_index == 0>class="check"</#if> data-bidderid="${bidder.id}">${bidder_index + 1}.${bidder.bidderName}</div>
        </#list>
    </div>
</div>
<div class="right">
    <div class="right-nav">
        <div fdfsMark="/tenderDoc/${tenderDoc.docFileId}/resources/TempConvert/temp.pdf">招标文件</div>
        <#if bidSection.bidClassifyCode == 'A08'>
            <div fdfsMark="/tenderDoc/${tenderDoc.docFileId}/resources/Quantities/quantities.pdf">招标工程量清单</div>
        </#if>
        <div id="init-view-pdf" class="check-nav" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/BStemp.pdf">商务标文件</div>
        <div fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/TEtemp.pdf">技术标文件</div>
        <div fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/QUtemp.pdf">资格证明文件</div>
        <#if bidSection.bidClassifyCode == 'A08'>
            <div fdfsMark="/bidderFile/{bidDocId}/resources/Quantities/quantities.pdf">投标工程量清单</div>
        </#if>
    </div>
    <div id="bjca-view-pdf-div" class="right-pdf" >
        <#include "${ctx}/viewPdf/viewBJCAPdf.ftl"/>
    </div>
</div>
<script defer="defer">
    $(function () {
        var mark = $(".right-nav div[class='check-nav']:eq(0)").attr("fdfsMark");
        loadBjcaPdf(mark);
    })

    $(".left-list div").bind("click", function () {
        $(this).addClass("check").siblings().removeClass("check");
        $(".right-nav #init-view-pdf").addClass("check-nav").siblings().removeClass("check-nav");
        var mark = $("#init-view-pdf").attr("fdfsMark");
        loadBjcaPdf(mark);
    });

    $(".right-nav div").bind("click", function () {
        $(this).addClass("check-nav").siblings().removeClass("check-nav");
        var mark = $(this).attr("fdfsMark");
        loadBjcaPdf(mark);
    });
</script>
</body>