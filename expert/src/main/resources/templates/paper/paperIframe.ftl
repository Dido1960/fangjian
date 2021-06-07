<div class="cont-right" style="width: 100%;height: 100%">
    <ol class="tit pdf-file-list" style="display: none">
        <#if isTender == 1>
            <li class="choice"  fdfsMark="${mark}">招标文件</li>
        <#else >
            <li class="choice"  fdfsMark="${mark}">投标文件</li>
        </#if>
    </ol>
    <div class="cont-text" style="width: 100%;height: 100%">
        <#--是否帮助按钮-->
        <#assign showHelpBtn="false"/>
        <#--是否启用本地缓存机制-->
        <#assign localCache="true"/>
        <#--是否开启另存为按钮-->
        <#assign showSaveAs="true"/>
        <#--是否开启打印按钮-->
        <#assign printPdf="true"/>
        <#--是否开启全屏查看按钮-->
        <#assign fullScreen="true"/>
        <#--1.必须存在class 为(pdf-file-list文件)-->
        <#--2.如果存在投标人切换 请 调用 changBidderView()更新pdf-->
        <#--3.跳转的话请指定文件序号 从0开始 eg:goToPage(10,0）跳转至第一个文件第10页-->
        <#include "${ctx}/common/showPDFView.ftl"/>
    </div>
</div>