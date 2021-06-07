<#--<meta name="_csrf" content="${_csrf.token}"/>-->
<#--<meta name="_csrf_header" content="${_csrf.headerName}"/>-->
<#--<script src="${ctx}/js/common.js"></script>-->
<#--<iframe id="showPdfIframe" src="${ctx}/bidFile/showPdfByMarkPage?mark=${mark}" width="100%" height="100%"></iframe>-->
<div class="cont-right">
    <ol class="tit pdf-file-list" style="display: none">
        <li class="choice"  fdfsMark="${mark}" >招标文件</li>
<#--        <li fdfsMark="/tenderDoc/{bidderId}/tenderFile.pdf">资格证明文件</li>-->
    </ol>
    <div class="cont-text">
        <#--是否帮助按钮-->
        <#assign showHelpBtn="false"/>
        <#--是否启用本地缓存机制-->
        <#assign localCache="false"/>
        <#--是否开启另存为按钮-->
        <#assign showSaveAs="true"/>
        <#--是否开启打印按钮-->
        <#assign printPdf="true"/>
        <#--是否开启全屏查看按钮-->
        <#assign fullScreen="false"/>
        <#--1.必须存在class 为(pdf-file-list文件)-->
        <#--2.如果存在投标人切换 请 调用 changBidderView()更新pdf-->
        <#--3.跳转的话请指定文件序号 从0开始 eg:goToPage(10,0）跳转至第一个文件第10页-->
        <#include "/common/showPDFView.ftl"/>
    </div>
</div>