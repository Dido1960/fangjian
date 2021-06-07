<script src="${ctx}/js/common.js"></script>
<script src="${ctx}/js/base64.js"></script>
<script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
<script src="${ctx}/js/json2.js"  defer="defer"></script>
<script src="${ctx}/js/ASAppCom.js"   defer="defer"></script>
<script src="${ctx}/js/anysign.js"   defer="defer"></script>

<style>
    .conts {
        /*margin-top: 99px;*/
        width: 100%;
        height: 852px;
    }
</style>

<div class="pdf-file-list" style="display: none">
    <div class="choice" fdfsMark="${mark}"></div>
</div>
<div class="conts pdf">
    <#--是否帮助按钮-->
    <#assign showHelpBtn="true"/>
    <#--是否启用本地缓存机制-->
    <#assign localCache="false"/>
    <#--是否开启另存为按钮-->
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
    <#include "${ctx}/viewPdf/showPDFView.ftl"/>
</div>

<SCRIPT type="text/javascript" FOR="ASAppCom" EVENT="AS_PenOperation(opType, xpos, ypos)">
    document.getElementById("PenOperation_res").value += $AS_PenOperation(opType, xpos, ypos);
</SCRIPT>