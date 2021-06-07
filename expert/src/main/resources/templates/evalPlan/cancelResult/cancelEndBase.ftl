<html>
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
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>

    <link rel="stylesheet" type="text/css" href="${ctx}/css/utils.css">
    <link rel="stylesheet" type="text/css" href="${ctx}/css/endBase.css">
    <#include "${ctx}/common/pdfPublicMethod.ftl"/>
    <style>
        .process ul li {
            width: 50%;
        }
    </style>
</head>
<body>
<#include "${ctx}/include/header.ftl">
<section>
    <div class="process">
        <h3>
            <span onclick="returnUpFlow()" >返回</span>
            <p title="${bidSection.bidSectionName}">${bidSection.bidSectionName}</p>
        </h3>
        <ul class="cancel-end-base-ul">
            <li id="evaluationReport" onclick="checkSelect(this)">
                <a href="javascript:void(0)">
                    <div class="flow-left" style="opacity: 0;"></div>
                    <div class="flow-num" id="reportDiv">1</div>
                    <div class="flow-right"></div>
                    <p>评标报告</p>
                </a>
            </li>
            <li id="evaluationEnd"  onclick="checkReport(this)">
                <a href="javascript:void(0)">
                    <div class="flow-left"></div>
                    <div class="flow-num">2</div>
                    <p>评标结束</p>
                </a>
            </li>
        </ul>
    </div>
    <div class="loadBox cont-candidate">
        <iframe id="contIframe" style="height: 100%" width="100%" frameborder="0" border="0"></iframe>
    </div>
</section>
<script>
    $(function () {
       $("#reportDiv").click();
    });

    /**
     * 加载局部div
     * @param targetUrl 目标路由
     * @param e
     */
    function goToUrl(targetUrl, e, msg) {
        // 添加load
        var indexLoad;
        if (msg != null){
            hide_IWeb2018()
            indexLoad = window.top.layer.msg(msg, {icon: 16, time: 0, shade: [0.3, '#393D49']});
        }else {
            hide_IWeb2018()
            indexLoad = layer.load();
        }

        var objId = $(e).parents("li").attr("id")
        baseCssChange(objId);

        var iframe = $(".loadBox iframe")[0];
        iframe.src = targetUrl;
        if (iframe.attachEvent) {
            iframe.attachEvent("onload", function () {
                layer.close(indexLoad);
            });
        } else {
            iframe.onload = function () {
                layer.close(indexLoad);
            };
        }
        //显示当前进行的流程
        showProcessNow($(e));
    }

    function baseCssChange(id) {
        if (id == 'evaluationReport'){
            if('${bidSection.evalStatus}' < 2){
                $(".cont-candidate").css("height", "1031px");
            }else {
                $(".cont-candidate").css("height", "931px");
            }
        } else{
            $(".cont-candidate").css("height", "1031px");
        }
    }

    function showProcessNow(obj) {
        $(obj).siblings("li").removeClass("sele");
        $(obj).addClass("sele");
    }

    //去评标报告的页面条件判断
    function checkSelect(obj) {
        $.ajax({
            url: "${ctx}/expert/evalPlan/checkReport",
            type: "POST",
            cache: false,
            success: function (data) {
                if (data.code == 2) {
                    goPdfToUrl('${ctx}/expert/evalPlan/evaluationReportPage',obj,"正在生成评标报告...");
                }else {
                    goToUrl('${ctx}/expert/evalPlan/evaluationReportPage',obj,null);
                }
            },
            error:function (e) {
                console.error(e);
            }
        });
    }

    function checkReport(obj) {
        $.ajax({
            url: "${ctx}/expert/evalPlan/checkReport",
            type: "POST",
            cache: false,
            success: function (data) {
                if (data.code === "2") {
                    layerLoading(data.msg,2,2,null,null,null);
                }else if (data.code === "1") {
                    goToUrl('${ctx}/expert/evalPlan/evaluationEndPage', obj, null)
                }
            },
            error:function (e) {
                console.error(e);
            }
        });
    }

</script>
</body>
</html>
