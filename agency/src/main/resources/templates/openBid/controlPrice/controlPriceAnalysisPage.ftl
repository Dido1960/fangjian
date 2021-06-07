<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>甘肃省房建市政电子辅助开标系统</title>
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

    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/controlPriceAnalysis.css">
    <style>
        body .tltle>div,body #control-price-bidder-list li>div {
            width: 25%;
        }
    </style>
</head>
<body>
<div class="big"><#if bidSection.bidClassifyCode == "A12">最高投标限价：<#else >招标控制价：</#if>${((tenderDoc.controlPrice)?number)?string(",###.00")}（元）</div>
<ul class="con">
    <div class="tltle">
        <div>序号</div>
        <div class="name">投标人名称</div>
        <div class="price">投标报价（元）</div>
        <div class="doing">结果</div>
    </div>
</ul>
<ul class="con" id="control-price-bidder-list"></ul>
<#--<div class="kong"></div>-->
<#--第一步：编写模版。你可以使用一个script标签存放模板，如：-->
<script id="bidderList" type="text/html">
    {{#  layui.each(d, function(index, bidder){
    var bidPrice = parseFloat(bidder.bidderOpenInfo.bidPrice);
    }}
    <li>
        <div>{{index + 1}}</div>
        <div class="name">{{bidder.bidderName}}</div>
        <div class="price">
            {{# if(isNull(bidder.bidderOpenInfo.bidPrice)){ }}
            /
            {{# }else{ }}
            {{(bidPrice).toLocaleString('en-US')}}
            {{# } }}
        </div>
        <div class="doing">
            {{# if(isNull(bidder.bidderOpenInfo.bidPrice) || parseFloat(bidder.bidderOpenInfo.bidPrice) >
            parseFloat('${tenderDoc.controlPrice}')){ }}
            <span class="orange">异常</span>
            {{# }else{ }}
            <span class="green">正常</span>
            {{# } }}
        </div>
    </li>
    {{# }); }}
    <li></li>
</script>
<script>
    showBidders();
    // 移除多余loading
    $("[id^=layui-layer1000]").remove();

    layui.use(['layer', 'element', 'laytpl', 'laypage'], function () {

    });

    /**
     * 请求数据
     */
    function showBidders() {
        doLoading();
        $.ajax({
            url: '${ctx}/staff/bidder/listDecrySuccessBidder',
            type: 'post',
            cache: false,
            async: true,
            data: {
                "bidSectionId": '${bidSection.id}'
            },
            success: function (result) {
                loadComplete();
                if (!isNull(result)) {
                    //第二步：填充数据
                    var data = result;
                    //第三步：渲染模版
                    var getTpl = bidderList.innerHTML
                        , view = document.getElementById('control-price-bidder-list');
                    layui.laytpl(getTpl).render(data, function (html) {
                        view.innerHTML = html;
                    });
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }
</script>
</body>
</html>