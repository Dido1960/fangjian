<style>
    .bottom-right ol li {
        width: ${(60/expertUsers?size)?string["0.####"]}% !important;
    }

    .bottom-right table tr td {
        width: ${(60/expertUsers?size)?string["0.####"]}% !important;
    }
    .bottom-right .layui-layer-tips {
        position: absolute !important;
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
                <div id="bidder_${bidder.id}_${grade.id}"></div>
            </li>
        </#list>
    </ul>
</div>
<div class="bottom-right">
    <ol>
        <li class="long">评审名称</li>
        <#list expertUsers as expert>
            <li title="${expert.expertName}">${expert.expertName}</li>
        </#list>
        <li class="small">汇总</li>
    </ol>
    <div class="bottom-table">
        <table cellpadding=0 cellspacing=0>
            <#list grade.gradeItems as item>
                <tr>
                    <td class="long">
                        &emsp;&emsp;${item_index + 1}、${item.itemContent}(扣分：${item.score}分)
                    </td>
                    <#list expertUsers as expert>
                        <td id="result_${item.id}_${expert.id}" onclick="showReasonTips(this)"  style="position: relative !important;"></td>
                    </#list>
                    <td class="small" id="summary_${item.id}" data-score="${item.score}"></td>
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
            url: "${ctx}/gov/bidEval/getConDetailedGroupBiddersResult",
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
        $.ajax({
            url: "${ctx}/gov/bidEval/getBidderDataForResult",
            data: {
                "bidderId": bidderId,
                "gradeId": ${grade.id}
            },
            type: "POST",
            cache: false,
            success: function (data) {
                echoScore(data.deducts);
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
            $grade.removeClass();
            $grade.attr('data-reason',score.deductComments);
            $grade.attr('data-val',score.evalResult);
            $grade.empty();
            if (score.evalResult == 0){
                $grade.append("<span>扣分</span>");
                $grade.addClass("red-f");
            }else if(score.evalResult == 1){
                $grade.append("<span>不扣分</span>");
                $grade.addClass("green-f");
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
        if ($(obj).attr("data-val") == "0" && !isNull($(obj).attr("data-reason"))){
            layer.tips( $(obj).attr("data-reason"),  $(obj).children("span"), {
                tips: [3, 'rgba(0, 102, 204, 1)'],
                time: 4000,
                success: function (layero, index, e) {
                    layero.css("top", $(obj).height() / 1.5);
                    layero.css("left", $(obj).width() / 3);
                    // layero.css("width", "50%");
                    // 把 tips 的 dom 从 body 中移动到你自定义滚动条的元素中
                    $(obj).append(layero);
                }
            });
        }
    }
</script>