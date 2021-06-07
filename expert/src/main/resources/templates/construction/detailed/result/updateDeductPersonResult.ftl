<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>甘肃省房建市政电子辅助评标系统</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/plugin/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${ctx}/plugin/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/updatePersonResult.css">
</head>
<body>
<div class="cont">
    <form class="layui-form" id="form">
        <h3>您正在修改此项评审结果：</h3>
        <p style="text-indent: 2em;margin-top: 20px;">${deduct.gradeItem.itemContent}</p>
        <div class="radio">
            <div onclick="chioceThis(this,1)" class="pass <#if deduct.evalResult==1>chioce</#if>">
                <div class="circular">
                    <i></i>
                </div>
                <b>不扣分</b>
            </div>
            <div onclick="chioceThis(this,0)" class="unpass <#if deduct.evalResult==0>chioce</#if>">
                <div class="circular">
                    <i></i>
                </div>
                <b>扣分</b>
            </div>
            <input name="evalResult" value="${deduct.evalResult}" type="hidden" lay-verify="required">
        </div>
        <textarea name="deductComments" onblur="checkEvalComments(this)"  placeholder="请输入"<#if deduct.evalResult==1> style="display: none" </#if>
            lay-verify="required"  maxlength="200"
        >${deduct.deductComments}</textarea>
        <input name="id" value="${deduct.id}" type="hidden">
        <button class="layui-btn valid-form" style="display: none" lay-submit lay-filter="*">立即提交</button>
    </form>
</div>
<div class="foot">
    <div class="btns">
        <span onclick="updateComment()">确定</span>
        <span onclick="cancle()">取消</span>
    </div>
</div>
<script>
    function chioceThis(obj, val){
        $(obj).addClass("chioce");
        var $textArea = $("textarea[name='deductComments']");
        if (val == 1){
            $(".unpass").removeClass("chioce");
            $textArea.slideUp();
        }else if (val == 0){
            $(".pass").removeClass("chioce");
            $textArea.slideDown();
        }
        $("input[name='evalResult']").val(val);
    }

    layui.use('form', function () {
        var form = layui.form;

        form.on('submit(*)', function () {
            $.ajax({
                url: '${ctx}/expert/conBidEval/saveExpertReviewSingleItemDeduct',
                type: 'post',
                cache: false,
                async: true,
                data: $("#form").serialize(),
                success: function (data) {
                    if (!isNull(data) && data.code === "1") {
                        layer.msg("修改意见成功!", {icon: 1,end: function () {
                                cancle();
                                parent.showResultDetailedPage('${deduct.gradeId}');
                            }});
                    } else {
                        layer.msg("修改失败!", {icon: 2})
                    }
                },
                error: function (data) {
                    layer.msg("修改失败!", {icon: 2})
                    console.error(data);
                },
            });
            return false;
        });

        form.on('select(result)', function (data) {
            if (data.value === "1") {
                $(".eval_comments_box").slideUp();
            } else {
                $(".eval_comments_box").slideDown();
            }
        });
        form.render();
    });

    /**
     * 修改专家评审意见
     */
    function updateComment() {
        $(".valid-form").click();
    }

    function cancle() {
        //先得到当前iframe层的索引
        var index = parent.layer.getFrameIndex(window.name);
        //再执行关闭
        parent.layer.close(index);
    }
</script>
</body>
</html>