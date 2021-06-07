<#list reviewTypes as type>
    <div class="document-center cont">
        <div class="document-top tlt" data-reviewType="${type.code}">
            <img src="${ctx}/img/choice.png" alt="">
            ${type.textName}
            <#if type.code != 5><span>总分：${type.gradeScore}</span><#else><span style="color: #cc2b00;">扣分</span></#if>
        </div>
        <div id="review-detail-${type.code}" class="document-value center" style="display: none;"></div>
    </div>
</#list>
<script>
    $('.cont').on('click', '.tlt', function () {
        var reviewType = $(this).attr("data-reviewType");
        var $cont = $(this).parent('.cont');
        var $img = $(this).find('img');
        var $table = $cont.find("#review-detail-" + reviewType );
        if ($table.css('display') == 'none') {
            showGroupResultPage(reviewType);
            $table.fadeIn('300');
            $img.attr("src", "${ctx}/img/choice_down.png");
            $cont.siblings().children('.document-value').hide();
            $cont.siblings().find('img').attr("src", "${ctx}/img/choice.png");
            $cont.siblings().children('.cont-bottom').empty();
        } else {
            $img.attr("src", "${ctx}/img/choice.png");
            $table.fadeOut('300')
            $cont.siblings().children('.cont-bottom').empty();
        }
    });

    /**
     * 小组评审结果
     */
    function showGroupResultPage(reviewType) {
        $("#review-detail-" + reviewType).load("${ctx}/expert/supBidEval/groupResultPage",
            {
                'reviewType': reviewType
            },
            function () {
                // 添加跨域参数
                $.ajaxSetup({
                    type: "POST",
                    cache: false,
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader(csrf_header, csrf_token);
                    }
                })
            }
        );
        layui.table.render();
    }
</script>
