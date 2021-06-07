<link rel="stylesheet" href="${ctx}/css/conDetailed.css">
<#list reviewTypes as type>
    <div class="document">
        <div class="document-top" data-type="${type.code}">${type.textName}
            <img src="${ctx}/img/down_right.png" alt="">
        </div>
        <div id="review-detail-${type.code}" class="document-bottom" style="display: none;"></div>
    </div>
</#list>
<script>
    $('.document').on('click', '.document-top', function () {
        var review = $(this).attr("data-type");
        var $cont = $(this).parent('.document');
        var $img = $(this).find('img');
        var $table = $cont.find("#review-detail-" + review );
        if ($table.css('display') == 'none') {
            showGroupTotalResultPage(review);
            $table.fadeIn(300);
            $img.attr("src", "${ctx}/img/down.png");
            $cont.siblings().children('.document-bottom').hide();
            $cont.siblings().find('img').attr("src", "${ctx}/img/down_right.png");
            $cont.siblings().children('.cont-bottom').empty();
        } else {
            $img.attr("src", "${ctx}/img/down_right.png");
            $table.fadeOut(300);
            $cont.siblings().children('.cont-bottom').empty();
        }
    });

    /**
     * 小组评审结果
     * @param reviewType
     */
    function showGroupTotalResultPage(reviewType) {
        $("#review-detail-" + reviewType).load("${ctx}/gov/bidEval/supReviewDetailedPage",
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