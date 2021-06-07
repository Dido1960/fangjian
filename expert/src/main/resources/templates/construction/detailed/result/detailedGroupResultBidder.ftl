<style>
    .tableHead thead tr th{
        width: ${(65/expertUsers?size)?string["0.####"]}%;
    }
    .tableBody tbody tr td{
        width: ${(65/expertUsers?size)?string["0.####"]}%;
    }
    .document-table table tbody tr td span {
        cursor: pointer;
    }
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
            <li <#if bidder_index == 0>class="bidderSel"</#if> onclick="selectThis(this,'${bidder.id}')" >
                <div>${bidder_index+1}</div>
                <div class="long" title="${bidder.bidderName}">${bidder.bidderName}</div>
                <#if isAllExpertEnd == 1>
                    <div id="bidder_${bidder.id}_${grade.id}"></div>
                </#if>
            </li>
        </#list>
    </ul>
</div>
<div class="document-right">
    <table class="tableHead" cellpadding=0 cellspacing=0>
        <thead>
        <tr>
            <th class="long">评审因素</th>
            <#list expertUsers as expert>
                <th>${expert.expertName}</th>
            </#list>
            <th class="summary">汇总</th>
        </tr>
        </thead>
    </table>
    <div class="document-table">
        <table class="tableBody" cellpadding=0 cellspacing=0>
            <tbody>
                <#list grade.gradeItems as item>
                    <tr>
                        <td class="long">${item_index+1}、${item.itemContent}</td>
                        <#list expertUsers as expert>
                            <td id="result_${item.id}_${expert.id}"  onclick="showReasonTips(this)"></td>
                        </#list>
                        <td class="summary" id="summary_${item.id}" data-score="${item.score}"></td>
                    </tr>
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
            getBiddersGradeResult();
        }
    })

    function selectThis(obj, bidderId) {
        $(obj).siblings().removeClass("bidderSel");
        $(obj).addClass("bidderSel");

        getBidderDataForResult(bidderId);
    }

    function getBiddersGradeResult() {
        $.ajax({
            url: "${ctx}/expert/conBidEval/getBiddersGradeResult",
            data: {
                "gradeId": ${grade.id}
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
            var $grade = $('#bidder_'+result.bidderId+'_'+result.gradeId);
            $grade.removeClass();
            if ('${isConGrade}'){
                if (!result.isConsistent){
                    $grade.text("专家意见不统一");
                    $grade.addClass("red-f");
                    continue;
                }
            }
            if (result.deductScore != 0.00){
                $grade.text("扣"+ result.deductScore +"分");
                $grade.addClass("red-f");
            }else{
                $grade.text("不扣分");
                $grade.addClass("green-f");
            }
        }
    }

    function getBidderDataForResult(bidderId) {
        doLoading();
        $.ajax({
            url: "${ctx}/expert/conBidEval/getBidderDataForResult",
            data: {
                "bidderId": bidderId,
                "gradeId": ${grade.id},
                "isAllExpertEnd": '${isAllExpertEnd}'
            },
            type: "POST",
            cache: false,
            success: function (data) {
                echoScore(data.deducts);
                if ('${isAllExpertEnd}' == '1'){
                    echoSummary(data.bidderResultDTOS);
                }
                loadComplete();
            },
            error: function () {
                loadComplete();
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
            $grade.removeClass();
            $grade.attr('data-reason',score.deductComments);
            $grade.attr('data-val',score.evalResult);
            $grade.empty();
            if (score.evalResult == 0){
                $grade.html("<span class='red-f'>不合格</span>");
            }else if(score.evalResult == 1){
                $grade.html("<span class='green-f'>合格</span>");
            }
        }
    }

    function echoSummary(data) {
        if (isNull(data)){
            return false;
        }
        for (var i = 0; i < data.length; i++) {
            var dto = data[i];
            var $summary = $('#summary_'+dto.gradeItemId);
            var score = $summary.attr("data-score");
            $summary.removeClass("red-f");
            $summary.removeClass("green-f");
            if ('${isConGrade}'){
                if (!dto.isConsistent){
                    $summary.text("专家意见不统一");
                    $summary.addClass("red-f");
                    continue;
                }
            }
            if (dto.passResult == 0){
                $summary.text("扣"+ score +"分");
                $summary.addClass("red-f");
            }else if(dto.passResult == 1){
                $summary.text("不扣分");
                $summary.addClass("green-f");
            }
        }
    }

    function showReasonTips(obj) {
        if ($(obj).attr("data-val") == "0"  && !isNull($(obj).attr("data-reason"))){
            layer.tips( $(obj).attr("data-reason"),  $(obj).children("span"), {
                tips: [3, 'rgba(0, 102, 204, 1)'],
                time: 4000
            });
        }
    }
</script>
