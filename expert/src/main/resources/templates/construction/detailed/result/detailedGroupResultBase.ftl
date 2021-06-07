<#list grades as grade>
    <div class="document-center">
        <div class="document-top title" data-gradeid="${grade.id }">
            <img src="${ctx}/img/choice.png" alt="">${grade.name }
        </div>
        <div id="review-detail-${grade.id }" class="center document-value" style="display: none;"></div>
    </div>
</#list>
<script>
    $('.document-center').on('click', '.title', function () {
        var gradeId = $(this).data("gradeid");
        var $cont = $(this).parent('.document-center');
        var $img = $(this).find('img');
        var $table = $cont.find("#review-detail-" + gradeId );
        if ($table.css('display') == 'none') {
            showGroupTotalResultPage(gradeId);
            $table.fadeIn(300);
            $img.attr("src", "${ctx}/img/choice_down.png");
            $cont.siblings().children('.document-value').hide();
            $cont.siblings().find('img').attr("src", "${ctx}/img/choice.png");
            $cont.siblings().children('.cont-bottom').empty();
        } else {
            $img.attr("src", "${ctx}/img/choice.png");
            $table.fadeOut(300);
            $cont.siblings().children('.cont-bottom').empty();
        }
    });

    /**
     * 小组评审结果
     * @param bidSectionId
     */
    function showGroupTotalResultPage(gradeId) {
        $("#review-detail-" + gradeId).load("${ctx}/expert/conBidEval/detailedGroupResultBidderPage",
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

    /**
     * 数字转换为汉字
     */
    $(".needBigChange").each(function () {
        $(this).after(numberToChinese($(this).text().trim()));
        $(this).remove();
    })
</script>
