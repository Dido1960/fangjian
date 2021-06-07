<link rel="stylesheet" href="${ctx}/css/floatPoint.css">
<div class="flow">
    <#if tenderDoc.controlPrice?if_exists>
        <h3>随机抽取的浮动点为</h3>
        <p>${tenderDoc.floatPoint!0}%</p>
    <#else >
        <h3></h3>
        <p>暂未抽取浮动点</p>
    </#if>
</div>



