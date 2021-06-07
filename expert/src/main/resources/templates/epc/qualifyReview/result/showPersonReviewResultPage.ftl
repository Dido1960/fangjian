<style>
    #grade_base .layui-layer-tips {
        position: absolute !important;
    }

    .tipsTd {
        position: relative !important;
    }
</style>
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
                            <div class="tipsTd itemTd"
                                 id="item_${item.id}_${bidder.id}"
                                    <#if expertReview.enabled != 1>
                                        onclick="updatePersonResult(this)"
                                    <#else >
                                        onclick="showReasonTips(this)"
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
        $.ajax({
            url: "${ctx}/expert/evalPlan/getResultData",
            data: {
                "gradeId": '${grade.id}'
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
            var $grade = $('#item_'+score.gradeItemId+'_'+score.bidderId);
            $grade.attr('data-id',score.id);
            $grade.attr('data-val',score.evalResult);
            $grade.attr('data-reason',score.evalComments);
            $grade.empty();
            if (score.evalResult == 0){
                $grade.append("<span class='red-f red-s'>不合格</span>");
            }else if(score.evalResult == 1){
                $grade.append("<span class='green-s green-f'>合格</span>");
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
        // 取消上次延时未执行的方法
        clearTimeout(TimeFn);
        var bidder_name = $(e).attr("data-bidder");
        var singId = $(e).attr("data-id");
        var open_form=layer.open({
            type: 2,
            title: "<span style='font-weight: bold;color: #333333;font-size: 16px;display: block;width: 100%;text-align: center;'>"+bidder_name+"</spa>",
            content: "${ctx}/expert/evalPlan/updatePersonResult?singId="+singId,
            area: ['650px', '510px'],
        });
    }



    function showReasonTips(obj) {
        // 取消上次延时未执行的方法
        clearTimeout(TimeFn);

        //执行延时
        TimeFn = setTimeout(function(){
            //do function在此处写单击事件要执行的代码
            if ($(obj).attr("data-val") == "0" && !isNull($(obj).attr("data-reason"))){
                layer.tips("<p>" + $(obj).attr("data-reason") + "</p>",  $(obj).children("span"), {
                    tips: [3, 'rgba(0, 102, 204, 1)'],
                    time: 4000,
                    success: function (layero, index, e) {
                        layero.css("top", $(obj).height() / 1.5);
                        layero.css("left", $(obj).width() / 3);
                        layero.css("width", "50%");
                        //把 tips 的 dom 从 body 中移动到你自定义滚动条的元素中
                        $(obj).append(layero);
                    }
                });
            }
        },200);
    }
</script>
