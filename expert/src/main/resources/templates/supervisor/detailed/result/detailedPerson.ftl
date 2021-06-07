<#list reviewTypes as type>
    <div class="cont">
        <div class="cont-top title" data-reviewType="${type.code}">
            <img src="${ctx}/img/choice.png" alt="">
            ${type.textName}
            <#if type.code != 5><span>总分：${type.gradeScore}</span><#else><span style="color: #cc2b00;">扣分</span></#if>
        </div>
        <div id="review-detail-${type.code}" class="cont-bottom review-detail" style="display: none"></div>
    </div>
</#list>
<script>
    $('.cont').on('click', '.title', function () {
        var reviewType = $(this).attr("data-reviewType");
        var $cont = $(this).parent('.cont');
        var $img = $(this).find('img');
        var $table = $cont.find("#review-detail-" + reviewType );
        if ($table.css('display') == 'none') {
            showResultDetailedPage(reviewType);
            $table.fadeIn(300)
            $img.attr("src", "${ctx}/img/choice_down.png");
            $cont.siblings().children('.cont-bottom').hide();
            $cont.siblings().find('img').attr("src", "${ctx}/img/choice.png");
            $cont.siblings().children('.cont-bottom').empty();
        } else {
            $img.attr("src", "${ctx}/img/choice.png");
            $table.fadeOut(300)
            $cont.siblings().children('.cont-bottom').empty();
        }
    })

    /**
     * 个人评审结果
     * @param bidSectionId
     */
    function showResultDetailedPage(reviewType) {
        $("#review-detail-" + reviewType).load("${ctx}/expert/supBidEval/detailedPersonGradePage",
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
    }
</script>
