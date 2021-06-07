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
    <style>
        html .layui-form-selected dl {
            width: 734px !important;
        }
    </style>
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
                        <select lay-filter="changeBidder" data-rank="1">
                                <#--<#if firstBidder?? && firstBidder.bidderFrom == 1>disabled</#if>>-->
                            <option value="">请选择</option>
                            <#if bidders??>
                                <#list bidders as bidder>
                                    <option value="${bidder.id}"
                                            <#if bidder.id == firstBidder.bidderId>selected</#if>>${bidder_index +1}、${bidder.bidderName}（第一名：${bidder.voteNums[0]}票 第二名：${bidder.voteNums[1]}票 第三名：${bidder.voteNums[2]}票）
                                    </option>
                                </#list>
                            </#if>
                        </select>
                    </div>
                </div>
            </td>
            <td>
                <textarea class="opinion1" placeholder="请输入意见" maxlength="200" onblur="updateOpinion(1,this)"></textarea>
            </td>
        </tr>
        <tr>
            <td class="small">第二名选择</td>
            <td class="layui-form">
                <div class="layui-form-item">
                    <div class="layui-input-block">
                        <select lay-filter="changeBidder" data-rank="2">
                                <#--<#if secondBidder?? && secondBidder.bidderFrom == 1>disabled</#if>>-->
                            <option value="">请选择</option>
                            <#list bidders as bidder>
                                <option value="${bidder.id}"
                                        <#if bidder.id == secondBidder.bidderId>selected</#if>>${bidder_index +1}、${bidder.bidderName}（第一名：${bidder.voteNums[0]}票 第二名：${bidder.voteNums[1]}票 第三名：${bidder.voteNums[2]}票）
                                </option>
                            </#list>
                        </select>
                    </div>
                </div>
            </td>
            <td>
                <textarea class="opinion2" placeholder="请输入意见" maxlength="200" onblur="updateOpinion(2,this)"></textarea>
            </td>
        </tr>
        <#if bidders?size gt 2>
            <tr>
                <td class="small">第三名选择</td>
                <td class="layui-form">
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <select lay-filter="changeBidder" data-rank="3">
                                    <#--<#if thirdBidder?? && thirdBidder.bidderFrom == 1>disabled</#if>>-->
                                <option value="">请选择</option>
                                <#list bidders as bidder>
                                    <option value="${bidder.id}"
                                            <#if bidder.id == thirdBidder.bidderId>selected </#if>>${bidder_index +1}、${bidder.bidderName}（第一名：${bidder.voteNums[0]}票 第二名：${bidder.voteNums[1]}票 第三名：${bidder.voteNums[2]}票）
                                    </option>
                                </#list>
                            </select>
                        </div>
                    </div>
                </td>
                <td>
                    <textarea class="opinion3" placeholder="请输入意见" maxlength="200" onblur="updateOpinion(3,this)"></textarea>
                </td>
            </tr>
        </#if>

    </table>
</div>
<div class="foot">
    <div class="btns">
        <span onclick="validLeaderRecommend()">确定</span>
        <span onclick="parent.layer.closeAll();">取消</span>
    </div>
</div>

<script>
    $(function () {
        changeBidder();
        $(".opinion1").val("${firstBidder.reason?jString}");
        $(".opinion2").val("${secondBidder.reason?jString}");
        $(".opinion3").val("${thirdBidder.reason?jString}");

        // 动态为字体设置下拉框加title
        $("select").siblings("div.layui-form-select").find('dl dd').each(function () {
            var titleValue = $(this).html();
            $(this).attr('title', titleValue);
        });
    });



    /**
     * 修改投标人
     */
    function changeBidder() {
        layui.use('form', function () {
            var form = layui.form;
            form.on('select(changeBidder)', function (data) {
                var bidderId = data.value;
                var ranking = data.elem.getAttribute("data-rank");
                var reason = $(".opinion" + ranking).val().trim();
                if (isNull(bidderId)) {
                    return false;
                }
                $.ajax({
                    url: "${ctx}/expert/otherBidEval/addCandidateSuccess",
                    data: {
                        "bidderId": bidderId,
                        "ranking": ranking,
                        "reason": reason,
                        "bidderFrom": 2
                    },
                    type: "POST",
                    cache: false,
                    success: function (result) {
                    }
                });
                form.render();
            });
            form.render();
        });
    }

    /**
     * 验证专家组长推选结果是否符合要求
     */
    function validLeaderRecommend() {
        var kongFlag = false;
        $("select,textarea").each(function (index) {
            if (isNull($(this).val())) {
                kongFlag = true;
                return false;
            }
        });
        if (kongFlag) {
            layerAlert("请完善推选候选人信息！");
            return;
        }

        //候选人校验
        $.ajax({
            url: "${ctx}/expert/otherBidEval/validLeaderRecommend",
            type: "POST",
            cache: false,
            success: function (data) {
                if (data.code == '1') {
                    // 结束小组评审
                     submitResult();
                } else {
                    layerAlert(data.msg);
                }
            }
        });
    }

    /**
     * 结束小组评审
     */
    function submitResult() {
        layerConfirm("确定结束小组评审吗?", function (index) {
            layerLoading('数据提交中, 请稍候...', null, 0);
            $.ajax({
                url: "${ctx}/expert/otherBidEval/endGroupReview",
                type: "POST",
                data: {
                    "evalProcess": 2,
                    "bidSectionId": '${bidSectionId}'
                },
                success: function (data) {
                    setTimeout(function () {
                        loadComplete();
                        // 关闭自身
                        layerSuccess("操作成功", function () {
                            parent.location.reload();
                        });
                    }, 400);
                }
            });
        });
    }

    /**
     * 动态修改推选意见
     */
    function updateOpinion(ranking, e) {
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
        if (isNull(reason) || isNull(bidderId)) {
            return;
        }
        $.ajax({
            url: "${ctx}/expert/otherBidEval/addCandidateSuccess",
            type: "POST",
            cache: false,
            data: {
                "bidderId": bidderId,
                "ranking": ranking,
                "reason": reason,
                "bidderFrom": 2
            },
            success: function (data) {

            }
        });
    }

</script>

</body>
</html>