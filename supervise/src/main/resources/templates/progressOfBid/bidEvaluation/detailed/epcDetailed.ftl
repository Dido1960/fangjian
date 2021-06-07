<link rel="stylesheet" href="${ctx}/css/qualification.css">
<style>
    .right-right ol li{
        width: ${(50/expertUsers?size)?string["0.####"]}%;
    }
    .right-right .right-table tr td{
        width: ${(50/expertUsers?size)?string["0.####"]}%;
    }
</style>

<h3>详细评审</h3>
<div class="right-left">
    <ol>
        <li>序号</li>
        <li class="long">投标单位</li>
        <li>结果</li>
    </ol>
    <ul>
        <#list bidders as bidder>
            <li <#if bidder_index == 0>class="hover"</#if> onclick="selectThis(this,'${bidder.id}')">
                <div>${bidder_index+1}</div>
                <div class="long" title="${bidder.bidderName}">
                    <p>${bidder.bidderName}</p>
                </div>
                <div id="bidder_${bidder.id}"></div>
            </li>
        </#list>
    </ul>
</div>
<div class="right-right">
    <ol>
        <li class="small">评审因素</li>
        <li class="long">评审标准</li>
        <#list expertUsers as expert>
            <li>${expert.expertName}</li>
        </#list>
        <li class="small">汇总</li>
    </ol>
    <div class="right-table">
        <table cellpadding=0 cellspacing=0>
            <#list grades as grade>
                <#list grade.gradeItems as item>
                    <tr>
                    <#if item_index == 0>
                        <td rowspan="${grade.gradeItems?size}" class="small">${grade.name}</td>
                    </#if>
                        <td class="long">
                            &emsp;&emsp;${item_index + 1}、${item.itemContent}(总分：${item.score}分)
                        </td>
                    <#list expertUsers as expert>
                        <td id="result_${item.id}_${expert.id}">
                        </td>
                    </#list>
                        <td class="small" id="avg_${item.id}"></td>
                    </tr>
                </#list>
            </#list>
        </table>
    </div>
</div>
<script>
    $(function () {
        getBiddersGradeResult();
        getBidderDataForResult('${bidders[0].id}');
    });

    function selectThis(obj, bidderId) {
        $(obj).siblings().removeClass("hover");
        $(obj).addClass("hover");

        getBidderDataForResult(bidderId);
    }

    function getBiddersGradeResult() {
        $.ajax({
            url: "${ctx}/gov/bidEval/getEpcDetailedGroupBiddersResult",
            type: "POST",
            cache: false,
            success: function (data) {
                echoGradeResult(data);
            },
            error: function () {
                layerAlert("数据获取失败！");
            }
        });
    }

    function echoGradeResult(data) {
        if (isNull(data)){
            return false;
        }
        for (var i = 0; i < data.length; i++) {
            var result = data[i];
            var $review = $('#bidder_'+result.bidderId);
            $review.text(result.arithmeticScore);
        }
    }

    function getBidderDataForResult(bidderId) {
        $.ajax({
            url: "${ctx}/gov/bidEval/getEpcDetailedGroupBidderResult",
            data: {
                "bidderId": bidderId
            },
            type: "POST",
            cache: false,
            success: function (data) {
                echoScore(data.resultList);
                echoSummary(data.bidderResultDTOS);
            },
            error: function () {
                layerAlert("数据获取失败！");
            }
        });
    }

    /**
     *回显打分数据
     */
    function echoScore(data) {
        if (isNull(data)){
            return false;
        }
        for (var i = 0; i < data.length; i++) {
            var score = data[i];
            var $grade = $('#result_'+score.gradeItemId+'_'+score.expertId);
            $grade.text(score.evalScore);
        }
    }

    function echoSummary(data) {
        if (isNull(data)){
            return false;
        }
        for (var i = 0; i < data.length; i++) {
            var dto = data[i];
            var $grade = $('#avg_'+dto.gradeItemId);
            $grade.text(dto.arithmeticScore);
            $grade.addClass("green-f");
        }
    }
</script>