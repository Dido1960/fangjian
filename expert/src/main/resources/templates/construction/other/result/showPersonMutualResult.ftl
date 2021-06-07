<div class="table">
    <div class="table-left">
        <div class="left-top">评审因素(${bidders?size}家)</div>
        <div class="left-bottom">
            <div class="left-bottom-content">
                <#list grade.gradeItems as item>
                    <div>
                        <p>${item_index + 1}、${item.itemContent}</p>
                    </div>
                </#list>
            </div>
        </div>
    </div>
    <div class="table-right">
        <div class="right-top">
            <#list bidders as bidder>
                <div>
                    <p title="${bidder.bidderName}">(${bidder_index+1})${bidder.bidderName}</p>
                </div>
            </#list>
        </div>
        <div class="right-bottom">
            <ul>
                <#list grade.gradeItems as item>
                    <li>
                        <#list bidders as bidder>
                            <div setNullFalg="" class="tipsTd itemTd_${bidder.id}" id="item_${item.id}_${bidder.id}"
                                    <#if expertReview.enabled != 1>
                                        ondblclick="updatePersonResult(this)"
                                    </#if>
                                 data-bidder="${bidder.bidderName}">
                            </div>
                        </#list>
                    </li>
                </#list>
            </ul>
        </div>
    </div>
</div>

<script>
    // 滚动元素外层的高度
    var parentHeight = $('.table-right').height();
    // 滚动元素内部的总高度
    var contentHeight = 100 * $('.right-bottom li').length;
    if(parentHeight - 60 > contentHeight) {
        $('.right-bottom').css('height',(contentHeight + 17) + "px");
    } else {
        $('.right-bottom').css('height',parentHeight - 60);
    }
    // 滚动条的跟随
    $(".right-bottom").scroll(function (event) {
        var scrollTop = event.target.scrollTop;
        var scrollLeft = event.target.scrollLeft;
        $(".left-bottom-content").css(
            "transform",
            "translateY(-" + scrollTop + "px)"
        );
        $(".right-top").css(
            "transform",
            "translateX(-" + scrollLeft + "px)"
        );
    });
    // 当个数小于9的时候 自适应页面的宽度
    $(function () {
        var length = $('.right-top').children('div').length
        var a = $('.right-top')
        if ($('.right-top').children('div').length <= '10') {
            $('.right-top').children('div').width((a[0].clientWidth / length) + 'px')
            $('.right-bottom>ul>li').children('div').width((a[0].clientWidth / length) + 'px')
        }
    });
</script>

<script>

    $(function () {
        getResultData();
    })

    function getResultData() {
        doLoading();
        $(".cont-center tbody td[setNullFalg]").html("-");
        $.ajax({
            url: "${ctx}/expert/conBidEval/getPersonMutualResultData",
            data: {
                "gradeId": '${grade.id}'
            },
            type: "POST",
            cache: false,
            success: function (data) {
                echoScore(data);
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
        if (isNull(data)) {
            return false;
        }
        for (var i = 0; i < data.length; i++) {
            var result = data[i];
            var $bidderTd = $('.itemTd_' + result.bidderId);
            $bidderTd.empty();
            $bidderTd.attr('data-id', result.id);
            if (!isNull(result.mutualResult)) {
                var $mutualResult = $('#item_' + result.mutualResult + '_' + result.bidderId);
                $mutualResult.html("<span class='green-s green-f'>符合</span>");
            }
        }
    }
</script>
<script>

    var TimeFn = null;

    /**
     * 修改专家个人评审结果
     */
    function updatePersonResult(e) {
        var mutualId = $(e).attr("data-id");
        var open_form = layer.open({
            type: 2,
            title: "<span style='font-weight: bold;color: #333333;font-size: 16px;display: block;width: 100%;text-align: center;'>修改结果</spa>",
            content: "${ctx}/expert/conBidEval/updateMutualResultPage?mutualId=" + mutualId,
            area: ['650px', '510px'],
        });
    }
</script>
