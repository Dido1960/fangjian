<meta charset="utf-8">
<title></title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
      content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="${ctx}/js/jquery-migrate-3.1.0.min.js"></script>
<!--[if lt IE 9]>
    <script src="${ctx}/js/html5shiv.min.js"></script>
    <script src="${ctx}/js/respond.min.js"></script>
    <![endif]-->
<script src="${ctx}/js/common.js"></script>
<script src="${ctx}/js/convertMoney.js"></script>
<link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
<script src="${ctx}/layuiAdmin/layui/layui.js"></script>
<script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>

<script src="${ctx}/js/base64.js"></script>
<link rel="stylesheet" href="${ctx}/css/colorBase.css">
<link rel="stylesheet" href="${ctx}/css/otherMutualContent.css">
<body style="overflow: hidden;">
<div class="cont-left">
    <form class="layui-form" action="#">
        <div class="layui-form-item">
            <label class="layui-form-label">当前投标单位</label>
            <div class="layui-input-block">
                <select id="select-box" lay-filter="changeBidder" class="bidders-list layui-input-block">
                    <#list bidders as bidder>
                        <option value="${bidder.id }" data-index="${bidder_index}" data-docid="${bidder.bidDocId}"
                                id="bidderOption_${bidder.id}">${bidder_index +1}、${bidder.bidderName}<#if bidder.expertReviewStatusForBidder>(已评审)</#if></option>
                    </#list>
                </select>
            </div>
        </div>
    </form>
    <div class="score">
        <div class="top">
            <#if !currentGrade.groupEnd?? || (currentGrade.groupEnd?? && currentGrade.groupEnd != 1)>
                <#if expertReview.enabled != 1>
                    <span class="yellow-b" id="personEnd" onclick="personalReviewEnd()" style="width: 116px;">个人评审结束</span>
                <#else>
                    <#if expert.isChairman == "1">
                        <span class="green-b" onclick="restartDetailedReview()">环节重评</span>
                        <span class="yellow-b" onclick="validOtherMutualGroupEnd()" style="width: 116px;">小组评审结束</span>
                    </#if>
                </#if>
            </#if>
            <div class="blove-b" onclick="evalResultPage()">评审结果</div>
        </div>
        <div class="bottom">
            <ol>
                <li class="long">${currentGrade.name}</li>
                <li>选择</li>
            </ol>
            <div class="bottom-table layui-form">
                <table cellpadding=0 cellspacing=0>
                    <#list currentGrade.gradeItems as item>
                        <tr>
                            <td class="long">
                                ${item_index + 1}、${item.itemContent}
                            </td>
                            <td>
                                <input class="radios" id="mutualResult_${item.id}" lay-filter="mutualResult" type="radio"
                                       name="mutualResult" value="${item.id}" data-score="${item.score}"
                                       <#if expertReview.enabled == 1> disabled</#if> title="符合">
                            </td>
                        </tr>
                    </#list>
                </table>
            </div>
        </div>
    </div>
</div>
<div class="cont-right">
    <ol class="tit pdf-file-list" id="file-type-li">
        <li data-filetype="1" fdfsMark="/tenderDoc/${tenderDoc.docFileId}/resources/TempConvert/temp.pdf">
            招标文件
        </li>
        <li fdfsMark="/tenderDoc/${tenderDoc.docFileId}/resources/Quantities/quantities.pdf">
            招标工程量清单
        </li>
        <li data-pdf-index="3" class="choice" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/BStemp.pdf">
            商务标文件
        </li>
        <li data-pdf-index="2" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/TEtemp.pdf">
            技术标文件
        </li>
        <li data-pdf-index="1" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/QUtemp.pdf">
            资格证明文件
        </li>
        <li fdfsMark="/bidderFile/{bidDocId}/resources/Quantities/quantities.pdf">
            投标工程量清单
        </li>
    </ol>
    <div id="view-pdf-div" class="cont-text">
        <#--是否帮助按钮-->
        <#assign showHelpBtn="true"/>
        <#--是否启用本地缓存机制-->
        <#assign localCache="true"/>
        <#--是否开启另存为按钮-->
        <#assign showSaveAs="false"/>
        <#--是否开启另存为按钮-->
        <#assign fullScreen="false"/>
        <#--1.必须存在class 为(pdf-file-list文件)-->
        <#--2.如果存在投标人切换 请 调用 changBidderView()更新pdf-->
        <#--3.跳转的话请指定文件序号 从0开始 eg:goToPage(10,0）跳转至第一个文件第10页-->
        <#include "${ctx}/common/showPDFView.ftl"/>
    </div>
</div>
</body>
<script>
    $(function () {
        layui.use('form', function () {
            var form = layui.form;
            form.on('select(changeBidder)', function (data) {
                getBidderData(data.value);
                changBidderView();
            });
            form.on('radio(mutualResult)', function (data) {
                saveMutualResult(data.elem);
            });
            form.render();
        });
        getBidderData('${bidders[0].id}');
    });

    /**
     * 获取投标人数据
     * @param bidderId
     */
    function getBidderData(bidderId) {
        // 清除radio选中
        $('.radios').prop('checked', false);
        layui.form.render('radio');
        $.ajax({
            url: "${ctx}/expert/conBidEval/getOtherMutualBidderData",
            data: {
                "gradeId": '${currentGrade.id}',
                "bidderId": bidderId
            },
            type: "POST",
            cache: false,
            success: function (data) {
                echoScore(data);
            },
            error: function () {
                layerAlert("数据获取失败！");
            }
        });
    }

    /**
     *回显打分数据
     */
    function echoScore(data) {
        if (isNull(data)) {
            return false;
        }
        for (var i = 0; i < data.length; i++) {
            var result = data[i];
            $('.radios').attr('data-id',result.id);
            if (!isNull(result.mutualResult)){
                var $radio = $('#mutualResult_'+result.mutualResult);
                $radio.prop("checked","checked");
                formRender();
            }
        }
    }

    function formRender() {
        layui.form.render();
    }

    /**
     * 详细评审，展示评审结果
     */
    function evalResultPage() {
        parent.window.location.href = "${ctx}/expert/conBidEval/evalResultMutualIndex";
    }

    function pageReloade() {
        parent.$(".sele").find(".flow-num").click();
    }
</script>

<script>
    function saveMutualResult(obj) {
        var $radio = $(obj);
        var mutualResult = $radio.val();
        var id = $radio.attr("data-id");
        var score = $radio.attr("data-score");

        var data = {
            "id": id,
            "mutualResult": mutualResult,
            "evalResult": score
        }

        $.ajax({
            url: "${ctx}/expert/conBidEval/saveMutualResult",
            data: data,
            type: "POST",
            cache: false,
            success: function (result) {
                if(result.code === "1"){
                    var bidderId = $("#select-box").val();
                    $("#select-box option").each(function () {
                        var bidder_id = $(this).val();
                        if (bidderId === bidder_id) {
                            var name = $(this).text();
                            if(name.indexOf("(已评审)") === -1) {
                                name = name + "(已评审)";
                                $(this).text(name);
                                formRender();
                            }
                        }
                    });
                } else {
                    layerAlert("保存错误！");
                }
            },
            error: function () {
                layerAlert("保存错误！");
            }
        });
    }

    /**
     * 个人评审结束校验
     */
    function personalReviewEnd() {
        $.ajax({
            url: "${ctx}/expert/conBidEval/checkPersonalOtherMutualEnd",
            data:{"gradeId": '${currentGrade.id}'},
            type: "POST",
            cache: false,
            success: function (result) {
                if (result.code == "1") {
                    personalOtherMutualEnd();
                } else {
                    layerAlert(result.msg);
                }
            },
            error: function () {
                layerAlert("操作失败！");
            }
        });
    }

    /**
     * 个人结束
     */
    function personalOtherMutualEnd() {
        layerConfirm("确认要结束个人评审吗？结束后将无法修改评审结果！", function () {
            $.ajax({
                url: "${ctx}/expert/conBidEval/personalOtherMutualEnd",
                type: "POST",
                cache: false,
                success: function (result) {
                    if (result) {
                        pageReloade();
                    } else {
                        layerAlert("个人评审结束失败！");
                    }
                },
                error: function () {
                    layerAlert("操作失败！");
                }
            });
        });
    }

    /**
     * 评审重评
     * */
    function restartDetailedReview() {
        layerConfirm("确认要重评该环节吗? 确认后请通知其他评标专家刷新本页面!", function () {
            $.ajax({
                url: "${ctx}/expert/conBidEval/restartOtherMutualReview",
                type: "POST",
                cache: false,
                success: function () {
                    pageReloade();
                },
                error: function () {
                    layerAlert("操作失败！");
                }
            });
        });
    }


    /**
     * 小组评审结束
     */
    function validOtherMutualGroupEnd() {
        $.ajax({
            url: "${ctx}/expert/conBidEval/validOtherMutualGroupEnd",
            type: "POST",
            cache: false,
            success: function (data) {
                if (data.code === "2") {
                    layerAlert(data.msg, 2, 2);
                }else {
                    submitResult(data.msg);
                }
            },
            error: function (data) {
                layerLoading("操作失败！", 2, 2);
            }
        })
    }

    function submitResult(msg) {
        layerConfirm(msg, function (index) {
            layerLoading('数据提交中, 请稍候...', null, 0);
            $.ajax({
                url: "${ctx}/expert/conBidEval/endGroupMutualResult",
                data:{"gradeId": '${currentGrade.id}'},
                type: "POST",
                success: function (data) {
                    setTimeout(function () {
                        loadComplete();
                        if (data) {
                            pageReloade();
                        } else {
                            layerLoading("结束小组评审失败！", 2, 2);
                        }
                    }, 400);
                }
            });
        });
    }
</script>
