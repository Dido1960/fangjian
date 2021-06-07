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
        .layui-table {
            width: 95%;
            margin: 20px auto 0;
        }

        .layui-table thead tr {

            height: 53px;
            background: #F3F3F3;
        }

        .layui-table thead tr th {
            text-align: center;
            font-size: 16px;
            font-family: "Microsoft YaHei";
            font-weight: bold;
            color: #1A2539;
        }

        .layui-table tbody tr td {
            text-align: center;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 400;
            color: #1A2539;
            max-width: 500px;
        }

        .layui-table tbody tr td textarea {
            width: 90%;
            height: 90%;
            min-width: 340px;
            min-height: 120px;
            background: #FFFFFF;
            border: 1px solid #CCCCCC;
            opacity: 1;
            border-radius: 6px;
            box-sizing: border-box;
            padding: 10px;
            resize: none;
        }

        .layui-table tbody tr td:nth-child(2) {
            min-width: 143px;
        }

        .foot {
            width: 100%;
            height: 76px;
            background: #E8EBF1;
            opacity: 1;
            position: fixed;
            bottom: 0;
        }

        .foot .btns {
            width: 300px;
            height: 40px;
            margin: 20px auto 0;
        }

        .btns span {
            display: inline-block;
            width: 100px;
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
<form id='opinionForm'>
    <table class="layui-table" style="margin-bottom: 90px;">
        <thead>
            <tr>
                <th>序号</th>
                <th>投标人</th>
                <th>评审项</th>
                <th>意见</th>
            </tr>
        </thead>
        <tbody>
        <#assign num = 0/>
        <#if bidders ??>
            <#list bidders as bidder>
                <#if bidder.expertReviewSingleItems ??>
                    <#list bidder.expertReviewSingleItems as expertReviewSingleItem>
                        <#if expertReviewSingleItem ??>
                            <tr>
                                <#if expertReviewSingleItem_index == 0>
                                    <#assign num = num + 1>
                                    <td rowspan="${bidder.expertReviewSingleItems?size }">${num }</td>
                                    <td rowspan="${bidder.expertReviewSingleItems?size }">${bidder.bidderName }</td>
                                </#if>
                                <td style="text-align: left">${expertReviewSingleItem.gradeItemContent}</td>
                                <td>
                                    <textarea data-evalid="${expertReviewSingleItem.id}" onblur="checkOpinion(this)"  maxlength="200" >${expertReviewSingleItem.evalComments}</textarea>
                                </td>
                            </tr>
                        </#if>
                    </#list>
                </#if>
            </#list>
        </#if>
        </tbody>
    </table>
</form>
<div class="foot">
    <div class="btns">
        <span onclick="saveOpinion()">确定</span>
        <span onclick="cancle()">取消</span>
    </div>
</div>
<script type="text/javascript">

    function saveOpinion() {
        // 未填写意见的条数
        var num = 0;
        // 意见超长的条数
        var num1 = 0;
        var expertReviewSingleItems = [];
        $("#opinionForm").find("table tbody").find("tr").each(function () {
            var item = {};
            var $textarea = $(this).find("td:last").find("textarea");
            var opinion = $textarea.val().trim();
            var itemId = $textarea.data("evalid");
            if (isNull(opinion)) {
                $textarea.css('background','#FFE7E7');
                num++;
            }
            if(opinion.length >= 255){
                num1++;
                $textarea.css('background','#FFE7E7');

                layerAlert("输入理由过长，系统自动保存前255个字符");
                // 防止理由过长，导致异常
                opinion = opinion.substr(0,255);
                $textarea.val(opinion)
            }
            item.id = itemId;
            item.evalComments = opinion;
            expertReviewSingleItems.push(item);
        });
        if (num>0) {
            layerLoading("请填写意见！", 2, 2);
            return;
        }else{
            $.ajax({
                url: "${ctx}/expert/evalPlan/updateReviewItems",
                contentType : "application/json;charset=utf-8",
                dataType : "json",
                data: JSON.stringify(expertReviewSingleItems),
                type: "POST",
                cache: false,
                success: function (data) {
                    if(!data){
                        layerAlert("意见保存失败！");
                    } else {
                        if(successCallback&&typeof(successCallback)=="function"){
                            successCallback();
                        }
                    }
                },
            });
        }
    }

    function cancle() {
        //先得到当前iframe层的索引
        var index = parent.layer.getFrameIndex(window.name);
        //再执行关闭
        parent.layer.close(index);
    }
    /**
     * 检测意见
     * @param obj
     */
    function checkOpinion(obj){
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

    var successCallback;
    function initSucc(_successCallback) {
        successCallback=_successCallback;
    }
</script>
</body>
</html>