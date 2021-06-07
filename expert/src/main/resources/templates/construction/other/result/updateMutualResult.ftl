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
    <link rel="stylesheet" href="${ctx}/css/updateMutualResult.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
</head>
<body>
<div class="cont">
    <div class="head"><span>投标单位</span>
        <p>${bidder.bidderName}</p>
    </div>
    <div class="document">
        <ol>
            <li>选择</li>
            <li class="long">评审因素</li>
        </ol>
        <form id="form" class="layui-form">
            <div class="document-table">
                <table cellpadding=0 cellspacing=0>
                    <#list currentGrade.gradeItems as item>
                        <tr>
                            <td><input lay-filter="mutualResult" type="radio" name="mutualResult" value="${item.id}" data-score = "${item.score}"
                                       <#if mutual.mutualResult?? && mutual.mutualResult == item.id> checked </#if>
                                       title="" lay-verify="required"></td>
                            <td class="long">${item_index + 1}、${item.itemContent}</td>
                        </tr>
                    </#list>
                </table>
            </div>
            <input name="id" value="${mutual.id}" type="hidden">
            <input name="evalResult" value="${mutual.evalResult}" type="hidden" class="evalResult">
            <button class="layui-btn valid-form" style="display: none" lay-submit lay-filter="*">立即提交</button>
        </form>
    </div>
</div>
<div class="foot">
    <div class="btns">
        <span onclick="updateComment()">确定</span>
        <span onclick="cancle()">取消</span>
    </div>
</div>
<script>
    layui.use('form', function () {
        var form = layui.form;

        form.on('radio(mutualResult)', function (data) {
            $(".evalResult").val($(data.elem).attr("data-score"));
        });

        form.on('submit(*)', function (data) {
            if (isNull($("input[name='mutualResult']:checked").val())){
                layerWarning("请选择加分项！");
                return false;
            }
            $.ajax({
                url: '${ctx}/expert/conBidEval/saveMutualResult',
                type: 'post',
                cache: false,
                async: true,
                data: $("#form").serialize(),
                success: function (data) {
                    if (!isNull(data) && data.code === "1") {
                        layer.msg("修改成功!", {icon: 1,end: function () {
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