<style>
    .bottom-right ol li{
        width: ${(49.5/expertUsers?size)?string["0.##"]}% !important;
    }
    .bottom-table table tr td{
        width: ${(49.5/expertUsers?size)?string["0.##"]}% !important;
    }
</style>
<div class="bottom-left">
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
                <div id="bidder_${bidder.id}_${reviewType}"></div>
            </li>
        </#list>
    </ul>
</div>
<div class="bottom-right">
    <ol>
        <li class="small">评审因素</li>
        <li class="long">评审标准</li>
        <#list expertUsers as expert>
            <li title="${expert.expertName}"><p>${expert.expertName}</p></li>
        </#list>
        <li class="small">汇总</li>
    </ol>
    <div class="bottom-table">
        <table cellpadding=0 cellspacing=0>
            <#list grades as grade>
                <#list grade.gradeItems as item>
                    <tr>
                        <#if item_index == 0>
                            <td rowspan="${grade.gradeItems?size}" class="small">${grade.name}</td>
                        </#if>
                        <td class="long">
                            &emsp;&emsp;${item_index + 1}、${item.itemContent}<br>
                            <#if item.scoreType == 'fixed'>
                                <b style="color: #cc2b00;">（取值范围：${item.scoreRange?replace(",", ",&nbsp;&nbsp;")}）</b>
                            <#else >
                                <b style="color: #cc2b00;">（酌情打分）</b>
                            </#if>
                        </td>
                        <#list expertUsers as expert>
                            <td id="result_${item.id}_${expert.id}">
                            </td>
                        </#list>
                        <td class="summary small" id="avg_${item.id}"></td>
                    </tr>
                </#list>
            </#list>
        </table>
    </div>
</div>
<script>
    $(function () {
        getBidderDataForResult('${bidders[0].id}');
        getBiddersGradeResult();
    });

    function selectThis(obj, bidderId) {
        $(obj).siblings().removeClass("hover");
        $(obj).addClass("hover");

        getBidderDataForResult(bidderId);
    }

    function getBiddersGradeResult() {
        $.ajax({
            url: "${ctx}/gov/bidEval/getSupDetailedGroupBiddersResult",
            data: {
                "reviewType": ${reviewType}
            },
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
            var $review = $('#bidder_'+result.bidderId+'_${reviewType}');
            $review.removeClass();
            if (!result.isConsistent){
                $grade.text("专家意见不统一");
                $grade.addClass("red-f");
                continue;
            }
            $review.text(result.arithmeticScore);
            if ('${reviewType}' === '5'){
                if (result.arithmeticScore != 0){
                    $review.addClass("red-f");
                }else{
                    $review.addClass("green-f");
                }
                continue;
            }
            $review.addClass("green-f");
        }
    }

    function getBidderDataForResult(bidderId) {
        $.ajax({
            url: "${ctx}/gov/bidEval/getSupDetailedGroupBidderResult",
            data: {
                "bidderId": bidderId,
                "reviewType": '${reviewType}'
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
            $grade.removeClass("yellow-f");
            $grade.removeClass("green-f");
            $grade.removeClass("red-f");
            if ('${reviewType}' == '3' || '${reviewType}' == '5' ){
                if (!dto.isConsistent){
                    $grade.addClass("yellow-f");
                    $grade.text("打分不一致");
                    continue;
                }
                if ('${reviewType}' == '5' && dto.arithmeticScore != '0.00'){
                    $grade.addClass("red-f");
                    $grade.text(dto.arithmeticScore);
                    continue;
                }
            }
            $grade.addClass("green-f");
            $grade.text(dto.arithmeticScore);
        }
    }
</script>