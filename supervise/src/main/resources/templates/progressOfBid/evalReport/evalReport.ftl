<link rel="stylesheet" href="${ctx}/css/information.css">
<div class="information">
    <h3>项目信息</h3>
    <form action="" class="content-form">
        <div>
            <label for="">项目名称</label>
            <input type="text" value="${tenderProject.tenderProjectName!}" disabled>
        </div>
        <div>
            <label for="">项目编号</label>
            <input type="text" value="${tenderProject.tenderProjectCode!}" disabled>
        </div>
        <div>
            <label for="">标段名称</label>
            <input type="text" value="${bidSection.bidSectionName!}" disabled>
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
            <label for="">招标方式</label>
            <input type="text" value="${tenderProject.tenderMode!}" disabled>
        </div>
        <div>
            <label for="">评标办法</label>
            <input type="text" value="${tenderDoc.evaluationMethod!}" disabled>
        </div>
        <div>
            <label for="">递交时间</label>
            <input type="text" value="${tenderDoc.submitTime!}" disabled>
        </div>
    </form>
</div>
<div class="content-bottom">
    <h3>投标单位信息
        <span onclick="tenderCheck()">标书查看</span>
    </h3>
    <ol>
        <li class="small">序号</li>
        <li>投标单位名称</li>
        <li>投标报价金额（元）</li>
        <li class="small">否决流程</li>
        <li class="small">否决状态</li>
        <li>否决理由</li>
    </ol>
    <ul>
        <#if bidders??>
            <#list bidders as bidder>
                <li>
                    <div class="small">${bidder_index+1}</div>
                    <div>
                        <p>${bidder.bidderName}</p>
                    </div>
                    <div>
                        <#if bidder.bidderOpenInfo.bidPrice?? && bidder.bidderOpenInfo.bidPrice != ''>
                            ${bidder.bidderOpenInfo.bidPrice?number?string(",###.##")}
                        </#if>
                    </div>
                    <div class="small">
                        <#if bidder.vetoFlow == 1>
                            开标
                        <#elseif  bidder.vetoFlow == 2>
                            评标
                        </#if>

                    </div>
                    <#if bidder.vetoFlow == 0>
                        <div class="small">未被否决</div>
                    <#else >
                        <div class="small red-f">否决</div>
                    </#if>
                    <div class="red-f">
                        <p>${bidder.vetoReason}</p>
                    </div>
                </li>
            </#list>
        </#if>

    </ul>
</div>
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