<link rel="stylesheet" href="${ctx}/css/information.css">
<style>
    <#if bidSection.bidClassifyCode == 'A10'>
    .cont-text ol li {
        width: 20% !important;
    }
    .cont-table table tr td{
        width: 20% !important;
    }
    </#if>
</style>
<script>
    /**
     * 标书查看
     */
    function tenderCheck() {
        window.layer.open({
            type: 2,
            title: '标书查看',
            shadeClose: true,
            area: ['45%', '100%'],
            btn: 0,
            resize: false,
            move: false,
            offset: 'rb',
            content: "${ctx}/gov/viewPdf/bidViewPage",
            btn1: function (index) {
                window.layer.close(index);
            }
        });
    }
</script>
<div class="information">
    <h3>项目信息</h3>
    <form action="" class="content-form">
        <div>
            <label for="">项目名称</label>
            <input type="text" title="${tenderProject.tenderProjectName!}" value="${tenderProject.tenderProjectName!}" disabled>
        </div>
        <div>
            <label for="">项目编号</label>
            <input type="text" value="${tenderProject.tenderProjectCode!}" disabled>
        </div>
        <div>
            <label for="">标段名称</label>
            <input type="text" title="${bidSection.bidSectionName!}" value="${bidSection.bidSectionName!}" disabled>
        </div>
        <div>
            <label for="">标段编号</label>
            <input type="text" value=" ${bidSection.bidSectionCode!} " disabled>
        </div>
        <div>
            <label for="">招标人</label>
            <input type="text" value="${tenderProject.tendererName!}" disabled>
        </div>
        <div>
            <label for="">招标代理</label>
            <input type="text" value="${tenderProject.tenderAgencyName!}" disabled>
        </div>
        <div>
            <label for="">招标方式</label>
            <#if bidSection.bidClassifyCode == 'A10'>
                <input type="text" value="公开招标" disabled>
                <#else >
                    <input type="text" value="${tenderProject.tenderMode!}" disabled>
            </#if>
        </div>
        <#if !bidSection.paperEval?? || bidSection.paperEval != "1">
            <div>
                <label for="">评标办法</label>
                <input type="text" value="${tenderDoc.evaluationMethod!}" disabled>
            </div>
        <#else >
            <div>
                <label for="">招标组织形式</label>
                <input type="text" value="${tenderProject.tenderOrganizeForm!}" disabled>
            </div>
        </#if>
        <div>
            <label for="">开标时间</label>
            <input type="text" value="${tenderDoc.bidOpenTime!}" disabled>
        </div>
        <div>
            <label for="">评标开始时间</label>
            <#assign evalStartTime = "--"/>
            <#if bidSection.evalStartTime?? && bidSection.evalStartTime != "">
                <#assign evalStartTime = bidSection.evalStartTime/>
            </#if>
            <input type="text" value="${evalStartTime!}" disabled>
        </div>
        <div>
            <label for="">开标结束时间</label>
            <#assign bidOpenEndTime = "--"/>
            <#if bidSection.bidOpenEndTime?? && bidSection.bidOpenEndTime != "">
                <#assign bidOpenEndTime = bidSection.bidOpenEndTime/>
            </#if>
            <input type="text" value="${bidOpenEndTime }" disabled>
        </div>
        <div>
            <label for="">评标结束时间</label>
            <#assign evalEndTime = "--"/>
            <#if bidSection.evalEndTime?? && bidSection.evalEndTime != "">
                <#assign evalEndTime = bidSection.evalEndTime/>
            </#if>
            <input type="text" value="${evalEndTime!}" disabled>
        </div>
        <div>
            <label for="">开标总用时</label>
            <input type="text" value="${bidOpenTotalTime!}" disabled>
        </div>
        <div>
            <label for="">评标总用时</label>
            <input type="text" value="${bidEvalTotalTime!}" disabled>
        </div>
    </form>
</div>
<div class="cont">
    <h3>投标单位信息
        <span onclick="tenderCheck()">标书查看</span>
    </h3>
    <div class="cont-text">
        <ol>
            <li class="small">序号</li>
            <li class="long">投标单位名称</li>
            <#if bidSection.bidClassifyCode != 'A10'>
               <li>投标报价金额（元）</li>
            </#if>
            <li>否决流程</li>
            <li>否决状态</li>
            <li>否决理由</li>
        </ol>
        <div class="cont-table">
            <table cellpadding=0 cellspacing=0>
                <#if bidders??>
                <#list bidders as bidder>
                <tr>
                    <td class="small">${bidder_index+1}</td>
                    <td class="long">${bidder.bidderName}</td>
                    <#if bidSection.bidClassifyCode != 'A10'>
                        <td>
                            <#if !bidder.bidderOpenInfo.bidPriceType?? || bidder.bidderOpenInfo.bidPriceType == "总价">
                                <#if bidder.bidderOpenInfo.bidPrice??>
                                    ${bidder.bidderOpenInfo.bidPrice?number?string(",###.##")}
                                </#if>
                            <#else>
                                ${bidder.bidderOpenInfo.bidPriceType}${bidder.bidderOpenInfo.bidPrice}
                            </#if>
                        </td>
                    </#if>
                    <td>
                        <#if bidder.vetoFlow == 1>
                            开标
                        <#elseif  bidder.vetoFlow == 2>
                            评标 
                        </#if>
                    </td>
                    <td>
                        <#if bidder.vetoFlow == 0>
                            <div>未被否决</div>
                        <#else >
                            <div class="red-f">否决</div>
                        </#if>
                    </td>
                    <td>
                        <div class="red-f">
                             ${bidder.vetoReason}
                        </div>
                    </td>
                </tr>
                </#list>
                </#if>
            </table>
        </div>
    </div>
</div>
