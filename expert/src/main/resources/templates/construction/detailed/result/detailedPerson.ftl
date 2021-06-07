<style>
    .review-detail {
        overflow: auto;
        background: #fff;
    }

    .review-detail table {
        width: inherit;
    }


    .cont-bottom .cont-head {
        font-size: 16px;
        font-family: "Microsoft YaHei";
        font-weight: bold;
        color: #333333;
        text-align: center;
        background: #cfdae5;
    }

</style>

<#list grades as grade>
    <div class="cont">
        <div class="cont-top title" data-gradeid="${grade.id }">
            <img src="${ctx}/img/choice.png" alt="">${grade.name }
        </div>
        <div id="review-detail-${grade.id }" class="review-detail cont-bottom" style="display: none;"></div>
    </div>
</#list>


<script>
    $('.cont').on('click', '.title', function () {
        var gradeId = $(this).data("gradeid");
        var $cont = $(this).parent('.cont');
        var $img = $(this).find('img');
        var $table = $cont.find("#review-detail-" + gradeId);
        if ($table.css('display') == 'none') {
            showResultDetailedPage(gradeId);
            $table.fadeIn(300);
            $img.attr("src", "${ctx}/img/choice_down.png");
            $cont.siblings().children('.cont-bottom').hide();
            $cont.siblings().find('img').attr("src", "${ctx}/img/choice.png");
            $cont.siblings().children('.cont-bottom').empty();
        } else {
            $img.attr("src", "${ctx}/img/choice.png");
            $table.fadeOut(300);
            $cont.siblings().children('.cont-bottom').empty();
        }
    })

    /**
     * 个人评审结果
     * @param bidSectionId
     */
    function showResultDetailedPage(gradeId) {
        doLoading();
        $("#review-detail-" + gradeId).load("${ctx}/expert/conBidEval/detailedPersonGradePage",
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
                });
                loadComplete();
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
