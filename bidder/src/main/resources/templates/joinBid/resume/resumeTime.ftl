<link rel="stylesheet" href="${ctx}/css/resumeTime.css">
<div class="right-center">
    <#if resumeTime?if_exists>
        <h3>复会时间</h3>
        <h3>${resumeTime!""}</h3>
    <#else >
        <h3>暂未公布复会时间</h3>
    </#if>
</div>

<#--<div class="foot" style="display: none">-->
<#--    <div class="foot-btns site-demo-button" id="layerDemo">-->
<#--        <span class="oragen-b layui-btn layui-btn-normal" onclick="resumeDissent()">发表异议</span>-->
<#--        <span class="blove-b" onclick="unDissent()">无异议</span>-->
<#--        <span class="green-b" onclick="showBriefReport()">查看报告</span>-->
<#--    </div>-->
<#--</div>-->

<#--<div style="display: none">-->
<#--    <input type="hidden" id="tenderRejection" value="${bidderOpenInfo.tenderRejection!}">-->
<#--    <input type="hidden" id="decryptStatus" value="${bidderOpenInfo.decryptStatus}">-->
<#--    <input type="hidden" id="resumeStatus" value="${lineStatus.resumeStatus}">-->
<#--</div>-->

<#--<script>-->
<#--    // $(function () {-->
<#--    //     initPages();-->
<#--    // });-->

<#--    /**-->
<#--     * 初始化页面-->
<#--     */-->
<#--    function initPages() {-->
<#--        var tenderRejection = $("#tenderRejection").val();-->
<#--        var decryptStatus = $("#decryptStatus").val();-->
<#--        var resumeStatus = $("#resumeStatus").val();-->

<#--        if (tenderRejection != 1 && decryptStatus == 1 && resumeStatus == 1) {-->
<#--            $(".foot").show();-->
<#--        }-->
<#--    }-->

<#--    /**-->
<#--     * 查看评标结果报告-->
<#--     */-->
<#--    function showBriefReport() {-->
<#--        window.layer.open({-->
<#--            type: 2,-->
<#--            title: '查看评标结果',-->
<#--            shadeClose: true,-->
<#--            area: ['60%', '80%'],-->
<#--            btn: ['取消'],-->
<#--            btnAlign: 'c',-->
<#--            offset: 'auto',-->
<#--            content: '${briefReportUrl.url}',-->
<#--            btn2: function (index) {-->
<#--                window.layer.close(index);-->
<#--            }-->
<#--        });-->
<#--    }-->

<#--    /**-->
<#--     * 复会异议-->
<#--     */-->
<#--    function resumeDissent() {-->
<#--        window.layer.open({-->
<#--            type: 2,-->
<#--            offset: 'c',-->
<#--            title: ['填写复会异议信息', 'text-align:center;'],-->
<#--            shadeClose: true,-->
<#--            area: ['30%', '43%'],-->
<#--            btn: ['确定', '取消'],-->
<#--            btnAlign: 'c',//按钮居中-->
<#--            content: "${ctx}/bidderModel/resumeDissentPage?id=${RequestParameters.bidderId}",-->
<#--            btn1: function (index, layero) {-->
<#--                var body = window.layer.getChildFrame('body', index);-->
<#--                var iframeWin = window[layero.find('iframe')[0]['name']];-->
<#--                iframeWin.addDissent(function () {-->
<#--                    window.layer.closeAll();-->
<#--                    layer.msg("操作成功！", {icon: 1, time: 2000})-->
<#--                });-->
<#--            },-->
<#--            btn2: function (index) {-->
<#--                window.layer.close(index);-->
<#--            }-->
<#--        });-->

<#--    }-->

<#--    /**-->
<#--     * 无异议-->
<#--     */-->
<#--    function unDissent() {-->
<#--        layer.confirm("确认无异议？",-->
<#--            {icon: 3, title: '操作提示'},-->
<#--            function () {-->
<#--                doLoading();-->
<#--                $.ajax({-->
<#--                    url: '${ctx}/bidderModel/addDissent',-->
<#--                    type: 'POST',-->
<#--                    cache: false,-->
<#--                    data: {-->
<#--                        roleType: 1,-->
<#--                        resume:0,-->
<#--                        bidderId: "${bidder.id}",-->
<#--                        bidSectionId: "${bidder.bidSectionId}",-->
<#--                        sendName: "${bidder.bidderName}",-->
<#--                    },-->
<#--                    success: function (data) {-->
<#--                        loadComplete();-->
<#--                        if (data) {-->
<#--                            var spans = $(".foot").find("span");-->
<#--                            spans.eq(1).removeAttr("onclick").css("opacity", "0.7");-->
<#--                            spans.eq(0).remove();-->
<#--                            layer.msg("操作成功！", {icon: 1})-->
<#--                        } else {-->
<#--                            layer.msg("操作失败！", {icon: 5})-->
<#--                        }-->
<#--                    },-->
<#--                    error: function (data) {-->
<#--                        loadComplete();-->
<#--                        console.error(data);-->
<#--                        layer.msg("操作失败！", {icon: 5})-->
<#--                    },-->
<#--                });-->
<#--            });-->

<#--    }-->


<#--</script>-->