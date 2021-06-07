<link rel="stylesheet" href="${ctx}/css/qualification.css">
<style>
    .right-right ol li{
        width: ${(60/expertUsers?size)?string["0.####"]}%;
    }
    .right-right .right-table tr td{
        width: ${(60/expertUsers?size)?string["0.####"]}%;
    }
</style>

<h3>互保共建</h3>
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
        <li class="long">评审名称</li>
        <#list expertUsers as expert>
            <li title="${expert.expertName}">${expert.expertName}</li>
        </#list>
        <li class="small">汇总</li>
    </ol>
    <div class="right-table">
        <table cellpadding=0 cellspacing=0>
            <#list grade.gradeItems as item>
                <tr>
                    <td class="long">&emsp;&emsp;${item_index+1}、${item.itemContent}</td>
                    <#list expertUsers as expert>
                        <td class="expertTd_${expert.id}" id="result_${item.id}_${expert.id}"></td>
                    </#list>
                    <#if item_index == 0>
                        <td class="small" id="sum" rowspan="${grade.gradeItems?size}"></td>
                    </#if>
                </tr>
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
            url: "${ctx}/gov/bidEval/getGroupMutualResultData",
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
        $.ajax({
            url: "${ctx}/gov/bidEval/getBidderMutualResultData",
            data: {
                "bidderId": bidderId,
                "gradeId": ${grade.id}
            },
            type: "POST",
            cache: false,
            success: function (data) {
                echoScore(data.expertReviewMutuals);
                echoSummary(data.groups);
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