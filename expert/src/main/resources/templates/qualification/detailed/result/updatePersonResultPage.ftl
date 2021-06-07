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
    <style>
        * {
            margin: 0;
            padding: 0;
        }

        h3 {
            width: 95%;
            height: 36px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: bold;
            line-height: 36px;
            color: #1A2539;
            margin: 0 auto;
        }

        .standard {
            width: 95%;
            min-height: 24px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 400;
            line-height: 24px;
            color: #1A2539;
            padding: 10px;
            box-sizing: border-box;
            margin: 0 auto;
            background: #F3F3F3;
            border: 1px solid #CCCCCC;
            opacity: 1;
            border-radius: 4px;
        }

        .layui-form {
            width: 100%;
            margin-top: 50px;
        }

        .layui-form .layui-input-block {
            width: 95%;
            margin: 0 auto;
        }

        .layui-form-select dl dd.layui-this {
            background-color: rgba(59, 135, 103, 1);
        }

        .layui-form textarea {
            display: block;
            width: 95%;
            height: 120px;
            margin: 20px auto 0;
            background: #FFFFFF;
            border: 1px solid #CCCCCC;
            opacity: 1;
            border-radius: 4px;
            padding: 10px;
            box-sizing: border-box;
        }

        .foot {
            width: 100%;
            height: 76px;
            background: #E8EBF1;
            opacity: 1;
            position: absolute;
            bottom: 0;
        }

        .foot .btns {
            width: 35%;
            height: 40px;
            margin: 20px auto 0;
        }

        .btns span {
            display: inline-block;
            width: 45%;
            height: 40px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 400;
            line-height: 40px;
            text-align: center;
            cursor: pointer;
            border-radius: 5px;
        }

        .btns span:hover {
            opacity: 0.7;
        }

        .btns span:nth-child(1) {
            color: #FFFFFF;
            background: rgba(0, 102, 204, 1);
            float: left;
        }

        .btns span:nth-child(2) {
            color: rgba(0, 102, 204, 1);
            border: 1px solid rgba(0, 102, 204, 1);
            float: right;
            box-sizing: border-box;
        }
    </style>
</head>
<body>
<div class="cont">
    <h3>评审名称</h3>
    <div class="standard">${gradeItem.itemContent}</div>
    <form class="layui-form" id="form">
        <div class="layui-input-block">
            <select lay-verify="required" class="eval_result" name="evalResult" lay-filter="result">
                <option value="" <#if !single.evalResult??>selected</#if>>请选择</option>
                <option value="0" <#if single.evalResult == 0>selected</#if>>不合格</option>
                <option value="1" <#if single.evalResult == 1>selected</#if>>合格</option>
            </select>
        </div>
        <textarea class="eval_comment eval_comments_box" name="evalComments" maxlength="200" onblur="checkEvalComments(this)"
                  style="display: none">${single.evalComments}</textarea>
        <input name="id" value="${single.id}" type="hidden">
        <button class="layui-btn valid-form" style="display: none" lay-submit lay-filter="*">立即提交</button>
    </form>
</div>
<div class="foot">
    <div class="btns">
        <span onclick="updateComment()">确定</span>
        <span onclick="parent.layer.closeAll();">取消</span>
    </div>
</div>
<script>
    <#if single.evalResult == 0>
    $(".eval_comments_box").show();
    </#if>

    layui.use('form', function () {
        var form = layui.form;

        form.on('submit(*)', function (data) {
            var indexLoad = layer.load();
            $.ajax({
                url: '${ctx}/expert/updateExpertComment',
                type: 'post',
                cache: false,
                async: true,
                data: $("#form").serialize(),
                success: function (data) {
                    if (!isNull(data) && data) {
                        layer.msg("修改意见成功!", {
                            icon: 1, end: function () {
                                parent.layer.closeAll();
                                parent.location.reload();
                            }
                        });
                    } else {
                        layer.msg("修改失败!", {icon: 2})
                    }
                },
                error: function (data) {
                    console.error(data);
                    parent.layer.closeAll();
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