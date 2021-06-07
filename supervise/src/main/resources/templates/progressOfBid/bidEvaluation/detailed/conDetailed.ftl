<link rel="stylesheet" href="${ctx}/css/conDetailed.css">
<#list grades as grade>
    <div class="document">
        <div class="document-top" data-gradeid="${grade.id}">${grade.name}
            <img src="${ctx}/img/down_right.png" alt="">
        </div>
        <div id="review-detail-${grade.id }" class="document-bottom" style="display: none;"></div>
    </div>
</#list>
<script>
    $('.document').on('click', '.document-top', function () {
        var gradeId = $(this).attr("data-gradeid");
        var $cont = $(this).parent('.document');
        var $img = $(this).find('img');
        var $table = $cont.find("#review-detail-" + gradeId );
        if ($table.css('display') == 'none') {
            showGroupTotalResultPage(gradeId);
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
     * @param bidSectionId
     */
    function showGroupTotalResultPage(gradeId) {
        $("#review-detail-" + gradeId).load("${ctx}/gov/bidEval/conGradeDetailedPage",
            {
                'gradeId': gradeId
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