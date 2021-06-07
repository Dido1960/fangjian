<link rel="stylesheet" href="${ctx}/css/clearResult.css">
<link rel="stylesheet" href="${ctx}/css/utils.css">
<script src="${ctx}/js/jquery-3.4.1.min.js"></script>
<div class="right">
    <div class="tips">
        <div>清标开始时间
            <span>${bidSectionRelate.startClearTime}</span>
        </div>
        <div>清标用时
            <span>${bidSectionRelate.clearTotalTime}</span>
        </div>
    </div>
    <form id="clearResultForm" method="post" target="clearResultForm" action="${quantityResultParam.viewQuantityResultUrl}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <input type="hidden" name="api_key" value="${quantityResultParam.clearApiKey}"/>
        <input type="hidden" name="platform" value="${quantityResultParam.clearPlatform}"/>
        <input type="hidden" name="overall_analysis_service_serial_number" value="${bidSectionRelate.clearAnalysisUid}"/>
        <input type="hidden" name="structure_status" value="${tenderDoc.structureStatus}">
        <input type="hidden" name="price_status" value="${tenderDoc.priceStatus}">
        <input type="hidden" name="fund_basis_status" value="${tenderDoc.fundBasisStatus}">
    </form>
    <iframe name="clearResultForm" style="width: 100%; height: calc(100vh - 334px);" frameborder="0" scrolling="auto"></iframe>
</div>
<script>
    $(function () {
        $("#clearResultForm").submit();
    })
</script>