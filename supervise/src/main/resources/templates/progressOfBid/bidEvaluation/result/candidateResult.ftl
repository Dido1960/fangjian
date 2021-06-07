<link rel="stylesheet" href="${ctx}/css/candidates.css">
<style>
    .right ol li {
        width: 16.66%;
    }

    .right .right-table table tr td {
        width: 16.66%;
    }
</style>
<ol>
    <li>候选人推荐</li>
    <li class="long">投标人名称</li>
    <li>报价得分</li>
    <li>理由</li>
</ol>
<div class="right-table">
    <table cellpadding=0 cellspacing=0>
        <#if bidders?size gt 0>
            <tr id="tr_1">
                <td class="ranking">第一名</td>
                <td class="bidder_name long"></td>
                <td class="price"></td>
                <td class="reason"></td>
            </tr>
        </#if>
        <#if bidders?size gt 1>
            <tr id="tr_2">
                <td class="ranking">第二名</td>
                <td class="bidder_name long"></td>
                <td class="price"></td>
                <td class="reason"></td>
            </tr>
        </#if>
        <#if bidders?size gt 2>
            <tr id="tr_3">
                <td class="ranking">第三名</td>
                <td class="bidder_name long"></td>
                <td class="price"></td>
                <td class="reason"></td>
            </tr>
        </#if>
    </table>
</div>
<script>
    $(function () {
        getCandidates();
    });

    function getCandidates() {
        $.ajax({
            url: "${ctx}/gov/bidEval/getCanCandidatesResult",
            type: "POST",
            cache: false,
            success: function (data) {
                echoCandidates(data);
            },
            error:function (e) {
                console.error(e);
                layerAlert("数据获取失败！");
            }
        });
    }

    function echoCandidates(data) {
        if (isNull(data)){
            return false;
        }
        for (var i = 0; i < data.length; i++) {
            var result = data[i];
            var $tr = $('#tr_' + result.ranking);
            $tr.find(".bidder_name").text(result.bidderName);
            $tr.find(".price").text(formatCurrency(result.bidderPrice));
            $tr.find(".reason").text(result.reason);
        }
    }
</script>