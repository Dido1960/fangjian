<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<script src="${ctx}/js/common.js"></script>
<style>
    .pdfIframe{
        width: 100%;
        height: calc(100vh - 250px);
        border: 0px;
    }
</style>
<#if isLeft>
    <iframe id="showPdfIframeLeft" class="pdfIframe" src="${ctx}/viewPdf/showPdfByIdPageLeft?fileId=${fileId}"></iframe>
    <#else >
        <iframe id="showPdfIframeRight" class="pdfIframe"  src="${ctx}/viewPdf/showPdfByIdPageRight?fileId=${fileId}"></iframe>
</#if>

