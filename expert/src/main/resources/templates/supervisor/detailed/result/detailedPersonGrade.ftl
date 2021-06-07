<div class="table">
    <div class="table-row">
        <div class="table-row_head">评审因素(${bidders?size}家)</div>
        <div class="table-row_body">
            <div class="body-box">
                <#list grades as grade>
                    <div data-row = "${grade.gradeItems?size}"><p>${grade.name}</p></div>
                </#list>
            </div>
        </div>
    </div>
    <div class="table-left">
        <div class="left-top">评审标准</div>
        <div class="left-bottom">
            <div class="left-bottom-content">
                <#list grades as grade>
                    <#list grade.gradeItems as item>
                        <div>
                            <p>${item.itemContent}(总分：${item.score}分)<#if item.scoreType == 'fixed'>
                                <span style="color: #cc2b00;">（取值范围：${item.scoreRange?replace(",", ",&nbsp;&nbsp;")}）</span>
                            <#else >
                                <span style="color: #cc2b00;">（酌情打分）</span>
                                </#if></p>
                        </div>
                    </#list>
                </#list>
            </div>
        </div>
    </div>
    <div class="table-right">
        <div class="right-top">
            <#list bidders as bidder>
                <div>
                    <p title="${bidder.bidderName}">${bidder.bidderName}</p>
                </div>
            </#list>
        </div>
        <div class="right-bottom">
            <ul>
                <#list grades as grade>
                    <#list grade.gradeItems as item>
                        <li>
                            <#list bidders as bidder>
                                <div class="disTouch scoreTd">
                                    <input class="result" id="result_${item.id}_${bidder.id}"
                                           <#if expertReview.enabled?? && expertReview.enabled == 1>disabled</#if>
                                           onkeyup="clearNoNum(this)"
                                           onfocus="modifyScore(this)"
                                           onblur="saveGradeResult(this)"
                                    />分
                                </div>
                            </#list>
                        </li>
                    </#list>
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
    // 滚动跟随效果
    var itemHeight = 100; // 每一行的高度
    $(".body-box div").each(function (index, obj) {
        var row= $(obj).attr("data-row");
        if (isNull(row)){
            row = 1;
        }
        $(obj).css("height", itemHeight * row + "px");
        $(obj).css("lineHeight", itemHeight * row + "px");
    });

    $(".right-bottom").scroll(function (event) {
        var scrollTop = event.target.scrollTop;
        var scrollLeft = event.target.scrollLeft;
        $(".left-bottom-content").css(
            "transform",
            "translateY(-" + scrollTop + "px)"
        );
        $(".body-box").css(
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
    // 加载layui组件
    $(function () {
        getResultData();
    });
    function getResultData() {
        $.ajax({
            url: "${ctx}/expert/supBidEval/getResultData",
            data: {
                "reviewType": '${reviewType}'
            },
            type: "POST",
            cache: false,
            success: function (data) {
                echoScore(data.data);
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
            var score = data[i];
            var $grade = $('#result_'+score.gradeItemId+'_'+score.bidderId);
            $grade.attr('data-id',score.id);
            $grade.val(score.evalScore);
            $grade.attr('data-oldScore',score.evalScore);
        }
    }
</script>
<script>
    function clearNoNum(obj) {
        //清除“数字”和“.”以外的字符
        obj.value = obj.value.replace(/[^\d.]/g, "");
        //验证第一个字符是数字而不是.
        obj.value = obj.value.replace(/^\./g, "");
        //只保留第一个. 清除多余的.
        obj.value = obj.value.replace(/\.{2,}/g, ".");
        obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
        //防止用户输入01类型的整数
        if (obj.value !== '' && obj.value.indexOf('.') === -1) {
            obj.value = parseFloat(obj.value)
        }
    }

    /**
     *聚焦显示边框
     */
    function modifyScore(obj) {
        $(obj).parent("div").removeClass("disTouch");
    }

    /**
     * 保存评分结果
     */
    function saveGradeResult(obj) {
        //保存
        var id = $(obj).attr("data-id");
        var score = $(obj).val();
        //防止用户输入0.数据
        if (score == "0."){
            score = 0;
        }
        var oldScore = $(obj).attr("data-oldScore");
        $(obj).parent("div").addClass("disTouch");
        if (isNull(score)){
            $(obj).val(oldScore);
            return false;
        }
        $.ajax({
            url: "${ctx}/expert/supBidEval/saveDetailedResult",
            data: {
                "id": id,
                "score": score,
                "reviewType": '${reviewType}'
            },
            type: "POST",
            cache: false,
            success: function (result) {
                if (result.code === "1") {
                    $(obj).val(score);
                    $(obj).attr("data-oldScore", score);
                } else if (result.code === "3") {
                    layerAlert(result.msg);
                }else {
                    layer.tips(result.msg,  $(obj), {
                        tips: [3, 'rgba(59, 135, 103, 1)'],
                        time: 4000
                    });
                    $(obj).val(oldScore);
                }
            },
            error: function () {
                layerAlert("保存错误！");
            }
        });
    }
</script>
