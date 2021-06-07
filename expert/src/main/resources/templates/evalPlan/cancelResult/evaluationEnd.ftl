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
<script src=${ctx}"/js/html5shiv.min.js"></script>
<script src=${ctx}"/js/respond.min.js"></script>
<![endif]-->
<script src="${ctx}/js/common.js"></script>
<script src="${ctx}/js/convertMoney.js"></script>
<link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
<script src="${ctx}/layuiAdmin/layui/layui.js"></script>
<script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>

<script src="${ctx}/js/base64.js"></script>
<link rel="stylesheet" href="${ctx}/css/colorBase.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/evaluationEnd.css">
<body style="overflow: auto;">
<div class="text-box">
<div class="cont">
    <div>
        <p>标段名称：</p><span><b>${bidSection.bidSectionName}</b></span>
    </div>
    <div>
        <p>标段编号：</p><span><b>${bidSection.bidSectionCode}</b></span>
    </div>
    <#if bidSection.evalStatus == 2>
        <div>
            <p>评标结束：</p><span><b>${bidSection.evalEndTime}</b></span>
        </div>
    <#else>
        <div>
            <p>评标状态</p><span class="green-f"><b>评标暂未结束</b></span>
        </div>
    </#if>
</div>
</div>
<#if bidSection.evalStatus != 2>
    <#if expert.isChairman>
        <div class="btn over-btn" onclick="endEvaluation()">评标结束</div>
    <#else>
        <div class="btn over-btn disabled" style="width: 100px">等待评标结束</div>
    </#if>
</#if>
<script>
    var checkSignar = '${checkSignar}';
    function endEvaluation() {
        if (checkSignar != 1){
            layerConfirm("专家尚未完成电子签名，是否确定结束评审? 如果确定，将无法再进行电子签名！",function () {
                end();
            });
        }else {
            end();
        }
    }
    function end() {
        var layerLoadIndex = layer.load(1, {shade: [0.1, '#fff']});
        $.ajax({
            url: "${ctx}/expert/evalPlan/endEvaluation",
            type: "POST",
            cache: false,
            success: function (data) {
                layer.close(layerLoadIndex);
                if (data.code === "2") {
                    layer.msg(data.msg, {icon: 2, time: 2000});
                } else {
                    layer.msg(data.msg,{icon: 1,time: 2000,end:function () {
                            window.location.reload();
                        }});
                }
            },
            error: function (e) {
                console.error(e);
                layer.close(layerLoadIndex);
            }
        });
    }
</script>
</body>