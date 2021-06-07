<style>
    <#--thead tr th{-->
    <#--    width: ${(60/expertUsers?size)?string["0.####"]}%;-->
    <#--}-->
    <#--tbody tr td{-->
    <#--    width: ${(60/expertUsers?size)?string["0.####"]}%;-->
    <#--}-->
</style>
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
                    <div id="bidder_${bidder.id}"></div>
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
                <th class="summary">平均分</th>
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
                                &emsp;&emsp;${item.itemContent}(总分：${item.score}分)
                                <b style="color: #cc2b00;">（酌情打分）</b>
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
            url: "${ctx}/expert/epcBidEval/getEpcDetailedGroupBiddersResult",
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
            $review.removeClass();
            $review.text(result.arithmeticScore);
            $review.addClass("green-f");
        }
    }

    function getBidderDataForResult(bidderId) {
        $.ajax({
            url: "${ctx}/expert/epcBidEval/getEpcDetailedGroupBidderResult",
            data: {
                "bidderId": bidderId,
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
            $grade.text(dto.arithmeticScore);
            $grade.addClass("green-f");
        }
    }
</script>
