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
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <script src="${ctx}/js/calc.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/otherRecomendCandidate.css">

</head>
<body>
<div class="box">
    <ol>
        <li class="small">候选人推荐</li>
        <li>投标人</li>
        <li>原因</li>
    </ol>
    <table cellpadding=0 cellspacing=0>
        <tr>
            <td class="small">第一名选择</td>
            <td class="layui-form">
                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <input type="hidden" value="1">
                        <select class="bidders-list" lay-filter="changeBidder" data-rank="1">
                            <option value="">请选择</option>
                            <#list bidders as bidder>
                                <option value="${bidder.id }" data-index="${bidder_index}" data-docid="${bidder.bidDocId}"
                                        <#if bidder.id == recommendOne.bidderId>selected</#if>>${bidder_index +1}、${bidder.bidderName}
                                </option>
                            </#list>
                        </select>
                    </div>
                </div>
            </td>
            <td>
                <textarea class="opinion1" maxlength="200" placeholder="请输入意见" onblur="updateOpinion(1,'${recommendOne.id}',this)"></textarea>
            </td>
        </tr>
        <tr>
            <td class="small">第二名选择</td>
            <td class="layui-form">
                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <select class="bidders-list" lay-filter="changeBidder" data-rank="2">
                            <option value="">请选择</option>
                            <#list bidders as bidder>
                                <option value="${bidder.id }" data-index="${bidder_index}"
                                        <#if bidder.id==recommendTwo.bidderId>selected </#if>
                                        data-docid="${bidder.bidDocId}">${bidder_index +1} 、${bidder.bidderName}
                                </option>
                            </#list>
                        </select>
                    </div>
                </div>
            </td>
            <td>
                <textarea name="remark" maxlength="200" class="opinion2" placeholder="请输入意见" onblur="updateOpinion(2,'${recommendTwo.id}',this)"></textarea>
            </td>
        </tr>
        <#if bidders?size gt 2>
            <tr>
                <td class="small">第三名选择</td>
                <td class=" layui-form">
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <select class="bidders-list" lay-filter="changeBidder" data-rank="3">
                                <option value="">请选择</option>
                                <#list bidders as bidder>
                                    <option value="${bidder.id }" data-index="${bidder_index}"
                                            <#if bidder.id==recommendThree.bidderId>selected </#if>
                                            data-docid="${bidder.bidDocId}">${bidder_index +1} 、${bidder.bidderName}
                                    </option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </td>
                <td>
                    <textarea class="opinion3" maxlength="200" placeholder="请输入意见" autocomplete="off" onblur="updateOpinion(3,'${recommendThree.id}',this)"></textarea>
                </td>
            </tr>
        </#if>
    </table>
</div>
<div class="foot">
    <div class="btns">
        <span onclick="validRecommend()">确定</span>
        <span onclick="parent.layer.closeAll();">取消</span>
    </div>
</div>

<script>
    $(function () {

        $(".opinion1").val("${recommendOne.reason?jString}");
        $(".opinion2").val("${recommendTwo.reason?jString}");
        $(".opinion3").val("${recommendThree.reason?jString}");

        layui.use('form', function () {
            var form = layui.form;
            form.on('select(changeBidder)', function (data) {
                var bidderId = data.value;
                var ranking = data.elem.getAttribute("data-rank");
                var reason = $(".opinion" + ranking).val();
                if (isNull(bidderId)){
                    return false;
                }
                $.ajax({
                    url: "${ctx}/expert/otherBidEval/addCandidate",
                    type: "POST",
                    cache: false,
                    data: {
                        "bId": bidderId,
                        "ranking": ranking,
                        "why": reason
                    },
                    success: function (result) {
                    },
                    error: function () {
                        layerAlert("操作失败！");
                    }
                });
                form.render();
            });
            form.render();
        });
    });

    /**
     * 动态修改推选意见
     */
    function updateOpinion(ranking,id,e) {
        var reason = $(e).val().trim();
        if (isNull(reason)){
            layer.msg("理由不能为空哦!")
            return;
        }
        if (reason.length>200){
            layerAlert("输入理由过长，系统自动保存前200个字符");
        }
        // 防止理由过长，导致异常
        reason = reason.substr(0,200);
        var bidderId = $("select[data-rank='" + ranking + "']").val();
        $.ajax({
            url: "${ctx}/expert/otherBidEval/updateCandidate",
            type: "POST",
            cache: false,
            data: {
                "id": id,
                "bidderId": bidderId,
                "ranking": ranking,
                "reason": reason
            },
            success: function (data) {
            }
        });
    }

    /**
     * 验证专家推选结果是否符合要求
     */
    function validRecommend() {
        var kongFlag = false;
        $("select,textarea").each(function (index) {
             if (isNull($(this).val())){
                 kongFlag = true;
                 return false;
             }
        });
        if (kongFlag){
            layerAlert("请完善推选候选人信息！");
            return;
        }
        //候选人校验
        $.ajax({
            url: "${ctx}/expert/otherBidEval/validRecommend",
            type: "POST",
            cache: false,
            success: function (data) {
                if (data){
                    layerSuccess("操作成功", function () {
                        parent.layer.closeAll();
                    });
                }else {
                    layerAlert("候选人推荐存在重复！");
                }
            }
        });
    }

</script>

</body>
</html>