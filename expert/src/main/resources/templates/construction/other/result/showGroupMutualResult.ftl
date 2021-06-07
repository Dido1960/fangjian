<style>
    thead tr th{
        width: ${(60/expertUsers?size)?string["0.####"]}%;
    }
    tbody tr td{
        width: ${(60/expertUsers?size)?string["0.####"]}%;
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
            <th class="long">评审因素</th>
            <#list expertUsers as expert>
                <th title="${expert.expertName}">${expert.expertName}</th>
            </#list>
            <th class="summary">汇总</th>
        </tr>
        </thead>
    </table>
    <div class="document-table">
        <table cellpadding=0 cellspacing=0>
            <tbody>
                <#list grade.gradeItems as item>
                    <tr>
                        <td class="long">${item_index+1}、${item.itemContent}</td>
                        <#list expertUsers as expert>
                            <td class="expertTd_${expert.id}" id="result_${item.id}_${expert.id}"></td>
                        </#list>
                        <#if item_index == 0>
                            <td class="summary" id="sum" rowspan="${grade.gradeItems?size}"></td>
                        </#if>
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
            url: "${ctx}/expert/conBidEval/getGroupMutualResultData",
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
            var $grade = $('#bidder_'+result.bidderId);
            $grade.removeClass();
            if (!result.isConsistent){
                $grade.text("专家意见不统一");
                $grade.addClass("yellow-f");
            }else {
                $grade.text("加"+ result.score +"分");
                $grade.addClass("green-f");
            }

        }
    }

    function getBidderDataForResult(bidderId) {
        doLoading();
        $.ajax({
            url: "${ctx}/expert/conBidEval/getBidderMutualResultData",
            data: {
                "bidderId": bidderId,
                "gradeId": ${grade.id},
                "isAllExpertEnd": '${isAllExpertEnd}'
            },
            type: "POST",
            cache: false,
            success: function (data) {
                echoScore(data.expertReviewMutuals);
                if ('${isAllExpertEnd}' == '1'){
                    echoSummary(data.groups);
                }
                loadComplete();
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
            var $expert = $(".expertTd_" + score.expertId);
            $expert.empty();
            if (!isNull(score.mutualResult)){
                var $grade = $('#result_'+score.mutualResult+'_'+score.expertId);
                $grade.text("符合");
            }
        }
    }

    function echoSummary(data) {
        if (isNull(data)){
            return false;
        }
        var $sum = $("#sum")
        $sum.removeClass("green-f");
        $sum.removeClass("yellow-f");
        if (data.length == 1){
            $sum.addClass("green-f");
            $sum.text("加"+ data[0].evalResult +"分");
        }else {
            $sum.addClass("yellow-f");
            $sum.text("专家意见不统一");
        }
    }
</script>
