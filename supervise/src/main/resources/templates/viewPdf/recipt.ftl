<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<script src="${ctx}/js/jquery-3.4.1.min.js"></script>
<script src="${ctx}/js/common.js"></script>
<#--回执单查看-->
<ol class="pdf-file-list" style="display: none">
   <li class="choice" fdfsMark="${mark}">招标文件</li>
</ol>
<div id="pdfShow" style="width: 100%;height: 100%;">
    <div class="cont" id="loadPdfLeftDiv">
        <#--是否帮助按钮-->
        <#assign showHelpBtn="false"/>
        <#--是否启用本地缓存机制-->
        <#assign localCache="false"/>
        <#--是否开启另存为按钮-->
        <#assign showSaveAs="true"/>
        <#--是否开启另存为按钮-->
        <#assign fullScreen="true"/>
        <#--1.必须存在class 为(pdf-file-list文件)-->
        <#--2.如果存在投标人切换 请 调用 changBidderView()更新pdf-->
        <#--3.跳转的话请指定文件序号 从0开始 eg:goToPage(10,0）跳转至第一个文件第10页-->
        <#include "/viewPdf/showPDFView.ftl"/>
    </div>
</div>