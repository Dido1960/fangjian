<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<script src="${ctx}/js/common.js"></script>
<style>
  .pdfIframe{
    width: 100%;
    height: 624px;
    border: 0px;
  }
</style>
<iframe id="showPdfIframe" class="pdfIframe" src="${ctx}/clientCheck/showPdfByIdPage?fileId=${fileId}"></iframe>