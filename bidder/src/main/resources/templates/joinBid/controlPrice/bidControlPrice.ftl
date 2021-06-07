<link rel="stylesheet" href="${ctx}/css/controlPrice.css">
<div class="right-center">
    <#if tenderDoc.controlPrice?if_exists>
        <#if bidSection.bidClassifyCode == "A12">
            <h3>最高投标限价</h3>
        <#else >
            <h3>招标控制价</h3>
        </#if>
        <p>${((tenderDoc.controlPrice)?number)?string(",###.00")}元</p>
        <#else >
            <#if bidSection.bidClassifyCode == "A12">
                <h3>暂未设置最高投标限价</h3>
            <#else >
                <h3>暂未设置控制价</h3>
            </#if>
    </#if>
</div>