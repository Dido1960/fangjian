<div class="document-left">
    <ol>
        <li>序号</li>
        <li class="long">投标单位</li>
        <#if isAllExpertEnd == 1>
            <li>结果</li>
        </#if>
    </ol>
    <ul id="group-total-result-ul">
        <#list bidders as bidder>
            <li <#if bidder_index == 0>class="bidderSel"</#if> onclick="selectThis(this,'${bidder.id}')">
                <div>${(bidder_index+1)}</div>
                <div class="long" title="${bidder.bidderName}">${bidder.bidderName}</div>
                <#if isAllExpertEnd == 1>
                    <div id="bidder_${bidder.id}_${reviewType}"></div>
                </#if>
            </li>
        </#list>
    </ul>
</div>
<div class="document-right">
    <table cellpadding=0 cellspacing=0>
        <thead>
            <tr>
                <th class="summary">评审因素</th>
                <th class="long">评审标准</th>
                <#list expertUsers as expert>
                    <th>${expert.expertName}</th>
                </#list>
                <th class="summary">
                    <#if reviewType == 3 || reviewType == 5>
                        汇总
                    <#else>
                        平均分
                    </#if>
                </th>
            </tr>
        </thead>
    </table>
    <div class="document-table">
        <table cellpadding=0 cellspacing=0>
            <tbody>
                <#list grades as grade>
                    <#list grade.gradeItems as item>
                        <tr>
                            <#if item_index == 0>
                                <td rowspan="${grade.gradeItems?size}" class="summary">${grade.name}</td>
                            </#if>
                            <td class="long">
                                &emsp;&emsp;${item.itemContent}
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
                            <td class="summary" id="avg_${item.id}"></td>
                    </#list>
                </#list>
            </tbody>
        </table>
    </div>
</div>
<script>
    var endExpertIds = '${endExpertIds}';
    $(function () {
        getBidderDataForResult('${bidders[0].id}');
        if ('${isAllExpertEnd}' == '1'){
            getBiddersReviewResult();
        }
    });

    function selectThis(obj, bidderId) {
        $(obj).siblings().removeClass("bidderSel");
        $(obj).addClass("bidderSel");

        getBidderDataForResult(bidderId)
    }

    function getBiddersReviewResult() {
        $.ajax({
            url: "${ctx}/expert/supBidEval/getBiddersReviewResult",
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
                $review.text("不统一");
                $review.addClass("red-f");
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
            url: "${ctx}/expert/supBidEval/getBidderDataForResult",
            data: {
                "bidderId": bidderId,
                "reviewType": '${reviewType}',
                "isAllExpertEnd": '${isAllExpertEnd}'
            },
            type: "POST",
            cache: false,
            success: function (data) {
                echoScore(data.resultList);
                if ('${isAllExpertEnd}' == '1'){
                    echoAvg(data.bidderResultDTOS);
                }
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
            //当前专家为结束个人评审，不展示其他专家的评审结果！
            if ('${expertReview.enabled}' != 1 && score.expertId != '${expertReview.expertId}'){
                continue;
            }
            //当前数据非个人结束专家则不展示
            if (score.expertId != '${expertReview.expertId}' && endExpertIds.indexOf(score.expertId) <= -1){
                continue;
            }
            var $grade = $('#result_'+score.gradeItemId+'_'+score.expertId);
            $grade.text(score.evalScore);
        }
    }
    function echoAvg(data) {
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
