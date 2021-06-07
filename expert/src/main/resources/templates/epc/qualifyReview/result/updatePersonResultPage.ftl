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
    <link rel="stylesheet" href="${ctx}/css/updatePersonResult.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
</head>
<body>
<div class="cont">
    <form class="layui-form" id="form">
        <h3>您正在修改此项评审结果：</h3>
        <p style="margin-top: 25px">${single.gradeItemContent}</p>
        <div class="radio">
            <div onclick="chioceThis(this,1)" class="pass <#if single.evalResult==1>chioce</#if>">
                <div class="circular">
                    <i></i>
                </div>
                <b>合格</b>
            </div>
            <div onclick="chioceThis(this,0)" class="unpass <#if single.evalResult==0>chioce</#if>">
                <div class="circular">
                    <i></i>
                </div>
                <b>不合格</b>
            </div>
            <input name="evalResult" value="${single.evalResult}" type="hidden" lay-verify="required">
        </div>
        <textarea name="evalComments" onblur="checkEvalComments(this)"  placeholder="请输入"<#if single.evalResult==1> style="display: none" </#if>
            lay-verify="required" maxlength="200"
        >${single.evalComments}</textarea>
        <input name="id" value="${single.id}" type="hidden">
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
        var $textArea = $("textarea[name='evalComments']");
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

        form.on('submit(*)', function (data) {
            $.ajax({
                url: '${ctx}/expert/updateExpertComment',
                type: 'post',
                cache: false,
                async: true,
                data: $("#form").serialize(),
                success: function (data) {
                    if (!isNull(data) && data) {
                        layer.msg("修改意见成功!", {icon: 1,end: function () {
                                cancle();
                                parent.getResultData();
                            }});
                    } else {
                        layer.msg("修改失败!", {icon: 2})
                    }
                },
                error: function (data) {
                    console.error(data);
                    layer.msg("修改失败!", {icon: 2})
                    cancle();
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

    /**
     * 限制输入长度
     * @param e
     */
    function checkEvalComments(obj){
        var opinion = $(obj).val().trim();
        if(isNull(opinion)){
            $(obj).css('background','#FFE7E7');
            layer.msg("请填写意见!");
            return;
        }
        if (opinion.length > 200){
            $(obj).css('background','#FFE7E7');
            layerAlert("输入理由过长，系统自动保存前200个字");
            // 防止理由过长，导致异常
            $(obj).val(opinion.substr(0,200))
        }
        $(obj).css('background','#FFFFFF');

    }
</script>
</body>
</html>