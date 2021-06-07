<link rel="stylesheet" type="text/css" href="${ctx}/css/evaluationReport.css">
<script src="${ctx}/js/common.js"></script>
<script src="${ctx}/js/convertMoney.js"></script>
<link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
<script src="${ctx}/layuiAdmin/layui/layui.js"></script>
<script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>

<script src="${ctx}/js/base64.js"></script>
<script src="${ctx}/js/json2.js" defer="defer"></script>
<script src="${ctx}/js/ASAppCom.js" defer="defer"></script>
<script src="${ctx}/js/anysign.js" defer="defer"></script>

<link rel="stylesheet" href="${ctx}/css/colorBase.css">
<link rel="stylesheet" type="text/css" href="${ctx}/css/evaluationReport.css">
<#include "/common/pdfPublicMethod.ftl"/>

<body>
<div class="text-box">
    <#--存放需要清除签名的专家id-->
    <input type="hidden" id="expert_ids">
    <#if bidSection.evalStatus lt 2>
    <div class="report-btns btns">
        <#--有签名未结束的专家，才会显示此按钮-->
        <span class="blove-b signa_tips" onclick="eleSignView()">电子签名</span>
        <#if user.isChairman == 1>
            <span class="blove-b" onclick="reGenerateReport()" style="float: right;">重新生成</span>
        </#if>
    </div>
    </#if>
    <div class="pdf-file-list" style="display: none">
        <div class="choice" fdfsMark="${mark}"></div>
    </div>
    <div class="conts pdf">
        <#--是否帮助按钮-->
        <#assign showHelpBtn="true"/>
        <#--是否启用本地缓存机制-->
        <#assign localCache="false"/>
        <#--是否打开下载按钮-->
        <#assign showSaveAs="true"/>
        <#--是否开启全屏按钮-->
        <#assign fullScreen="true"/>
        <#--是否开启打印按钮-->
        <#assign printPdf="true"/>
        <#--是否开启电子签章按钮-->
        <#assign signatureBtn="true"/>
        <#--1.必须存在class 为(pdf-file-list文件)-->
        <#--2.如果存在投标人切换 请 调用 changBidderView()更新pdf-->
        <#--3.跳转的话请指定文件序号 从0开始 eg:goToPage(10,0）跳转至第一个文件第10页-->
        <#include "/common/showPDFView.ftl"/>
    </div>
</div>
<SCRIPT type="text/javascript" FOR="ASAppCom" EVENT="AS_PenOperation(opType, xpos, ypos)">
    document.getElementById("PenOperation_res").value += $AS_PenOperation(opType, xpos, ypos);
</SCRIPT>

<script>
    /**
     * 重新生成评标报告
     */
    <#--function reGenerateReport() {-->
    <#--    hide_IWeb2018();-->
    <#--    var confirmWin = window.top.layer.confirm('确定重新生成吗？', {-->
    <#--            icon: 3,-->
    <#--            btn: ['确定', '取消'],-->
    <#--            cancel: function () {-->
    <#--                show_IWeb2018();-->
    <#--            }-->
    <#--        }, function () {-->
    <#--            window.top.layer.close(confirmWin);-->
    <#--            // indexLoad = window.top.layer.msg("报告重新生成中，请耐心等待...", {icon: 16, time: 0, shade: [0.3, '#393D49']});-->

    <#--            // 展示当前pdf生成进度-->
    <#--            currentConvertPdf();-->

    <#--            // 重新生成评标报告-->
    <#--            $.ajax({-->
    <#--                url: "${ctx}/expert/otherBidEval/reReport",-->
    <#--                type: "POST",-->
    <#--                cache: false,-->
    <#--                success: function (data) {-->
    <#--                    window.location.reload();-->
    <#--                },-->
    <#--                error: function (e) {-->
    <#--                    window.top.layer.closeAll();-->
    <#--                    console.error(e);-->
    <#--                    if (e.status == 403) {-->
    <#--                        console.warn("用户登录失效！！！")-->
    <#--                        window.top.location.href = "/login.html";-->
    <#--                    }-->
    <#--                }-->
    <#--            });-->
    <#--        }, function () {-->

    <#--        }-->
    <#--    );-->
    <#--}-->

</script>