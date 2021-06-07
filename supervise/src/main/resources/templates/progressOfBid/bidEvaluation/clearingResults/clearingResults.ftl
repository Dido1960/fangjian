<link rel="stylesheet" href="${ctx}/css/clearingResults.css">
<style>
    .right-process ul li {
    <#assign subNum = 0>
    <#if !(tenderDoc.structureStatus?? && tenderDoc.structureStatus == 1)>
    <#assign subNum = subNum + 1>
    </#if>
    <#if !(tenderDoc.priceStatus?? && tenderDoc.priceStatus == 1)>
    <#assign subNum = subNum + 1>
    </#if>
    <#if !(tenderDoc.fundBasisStatus?? && tenderDoc.fundBasisStatus == 1)>
    <#assign subNum = subNum + 1>
    </#if>
        width: ${(100/(8-subNum))?string["0.####"]}%;
    }
</style>
    <div class="right-process">
        <#assign stepNum = 1>
        <ul id="process-ul">
            <li class="sele">
                <div class="flow-left" style="opacity: 0;"></div>
                <div class="flow-num" onclick="loadFrame('${ctx}/gov/bidEval/clearResultAnalysisPage')">${stepNum}</div>
                <div class="flow-right"></div>
                <p>清标结果</p>
                <#assign stepNum = stepNum + 1>
            </li>
            <li>
                <div class="flow-left"></div>
                <div class="flow-num" onclick="loadFrame('${ctx}/gov/bidEval/controlPriceAnalysisPage')">${stepNum}</div>
                <div class="flow-right"></div>
                <p>控制价分析</p>
                <#assign stepNum = stepNum + 1>
            </li>
            <#if tenderDoc.structureStatus?? && tenderDoc.structureStatus == 1>
                <li>
                    <div class="flow-left"></div>
                    <div class="flow-num" onclick="loadFrame('${quantityResultParam.viewStructureAnalysisUrl}')">${stepNum}</div>
                    <div class="flow-right"></div>
                    <p>错漏项分析</p>
                    <#assign stepNum = stepNum + 1>
                </li>
            </#if>
            <#if tenderDoc.priceStatus?? && tenderDoc.priceStatus == 1>
                <li>
                    <div class="flow-left"></div>
                    <div class="flow-num" onclick="loadFrame('${quantityResultParam.viewPriceAnalysisUrl}')">${stepNum}</div>
                    <div class="flow-right"></div>
                    <p>零负报价检查</p>
                    <#assign stepNum = stepNum + 1>
                </li>
            </#if>
            <#if tenderDoc.fundBasisStatus?? && tenderDoc.fundBasisStatus == 1>
                <li>
                    <div class="flow-left"></div>
                    <div class="flow-num" onclick="loadFrame('${quantityResultParam.viewRuleAnalysisUrl}')">${stepNum}</div>
                    <div class="flow-right"></div>
                    <p>取费基础分析</p>
                    <#assign stepNum = stepNum + 1>
                </li>
            </#if>
            <li>
                <div class="flow-left"></div>
                <div class="flow-num" onclick="loadFrame('${quantityResultParam.viewArithmeticAnalysisUrl}')">${stepNum}</div>
                <div class="flow-right"></div>
                <p>算术性错误</p>
                <#assign stepNum = stepNum + 1>
            </li>
            <li>
                <div class="flow-left"></div>
                <div class="flow-num" onclick="loadFrame('${quantityResultParam.viewPartialListItemAnalysisUrl}')">${stepNum}</div>
                <div class="flow-right"></div>
                <p>清单报价分析</p>
                <#assign stepNum = stepNum + 1>
            </li>
            <li>
                <div class="flow-left"></div>
                <div class="flow-num" onclick="loadFrame('${quantityResultParam.viewMaterialAnalysisUrl}')">${stepNum}</div>
                <p>材料报价分析</p>
            </li>
        </ul>
    </div>
    <form id="myForm" method="post" target="myFrame">
        <input type="hidden" name="api_key" value="${quantityResultParam.clearApiKey}"/>
        <input type="hidden" name="platform" value="${quantityResultParam.clearPlatform}"/>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <input type="hidden" name="overall_analysis_service_serial_number" value="${bidSectionRelate.clearAnalysisUid}"/>
    </form>
    <div class="right-cont">
        <iframe name="myFrame" style="width: 100%; height: calc(100vh - 334px)" frameborder="0" scrolling="auto"></iframe>
    </div>
<script>
    $(function () {
        $(".right-process ul li").on("click", ".flow-num", function () {
            var $li = $(this).parent();
            $li.addClass("sele").siblings().removeClass("sele");
        });

        $("#process-ul li:first .flow-num").click();
    })

    function loadFrame(url) {
        if(url == '${ctx}/gov/bidEval/clearResultAnalysisPage'){
            $(".right-cont").css("height","464px");
        }else {
            $(".right-cont").css("height","");
        }

        $("#myForm").attr("action", url);
        $("#myForm").submit();
    }
</script>