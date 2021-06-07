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
<link rel="stylesheet" href="${ctx}/css/pointSrc.css">
<link rel="stylesheet" href="${ctx}/css/iframeBidderDetailed.css">
<body class="bidderBody">
<div class="cont-left">
    <form class="layui-form" action="#">
        <div class="layui-form-item">
            <label class="layui-form-label">当前投标单位</label>
            <div class="layui-input-block">
                <select id="select-box" lay-filter="changeBidder" class="bidders-list">
                    <#list bidders as bidder>
                        <option value="${bidder.id}" id="bidderOption_${bidder.id}">${bidder_index +1}、${bidder.bidderName}<#if bidder.expertReviewStatusForBidder>(已评审)</#if></option>
                    </#list>
                </select>
            </div>
        </div>
    </form>
    <div class="score">
        <div class="top">
            <#if grades[0].groupEnd != 1>
                <#if expertReview.enabled != 1>
                    <#if isViolation>
                        <span class="green-b" id="noDeduct" onclick="oneKeyNoDeduct()">一键不扣分</span>
                    </#if>
                    <span class="yellow-b" id="personEnd" onclick="personalReviewEnd()" style="width: 116px;">个人评审结束</span>
                    <#else>
                        <#if expert.isChairman == "1">
                            <span class="green-b" onclick="restartDetailedReview()">环节重评</span>
                            <span class="yellow-b" onclick="validDetailedGroupEnd()" style="width: 116px;">小组评审结束</span>
                        </#if>
                </#if>
            </#if>
            <div class="blove-b" onclick="showExpertEval()">评审结果</div>
        </div>
        <div id="grade-content" class="bottom">
            <#list grades as grade>
                <table cellpadding=0 cellpadding=0>
                    <thead>
                        <tr>
                            <th class="long">${grade.name}（${grade.score}分）</th>
                            <th>打分</th>
                            <th>页码</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list grade.gradeItems as item>
                            <tr>
                                <td class="long">${item_index + 1}、${item.itemContent}(总分：${item.score}分)
                                    <p>（请酌情打分）</p>
                                </td>
                                <td>
                                    <input class="scoreInput" id="item_${item.id}" onkeyup="clearNoNum(this)"
                                           onblur="saveGradeResult(this)"
                                           <#if expertReview.enabled == 1>disabled</#if> type="text"
                                           placeholder="打分">分
                                </td>
                                <td id="point_${item.id}"></td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            </#list>
        </div>
    </div>
</div>
<div class="cont-right">
    <ol class="tit pdf-file-list" id="file-type-li">
        <li data-filetype="1" fdfsMark="/tenderDoc/${tenderDoc.docFileId}/resources/TempConvert/temp.pdf">
            招标文件
        </li>
        <li data-pdf-index="3" class="choice" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/BStemp.pdf">
            商务标
        </li>
        <li data-pdf-index="2" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/TEtemp.pdf">
            技术标
        </li>
        <li data-pdf-index="1" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/QUtemp.pdf">
            资格证明
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
            form.render();
            form.on('select(changeBidder)', function (data) {
                getBidderData(data.value);
                changBidderView();
            });
        });
        getBidderData('${bidders[0].id}');
    });

    /**
     * 获取投标人数据
     * @param bidderId
     */
    function getBidderData(bidderId) {
        $.ajax({
            url: "${ctx}/expert/epcBidEval/getEpcDetailedBidderData",
            data: {
                "bidderId": bidderId
            },
            type: "POST",
            cache: false,
            success: function (data) {
                echoScore(data.scoreList);
                echoPoint(data.bidderReviewPointDTOS);
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
            var score = data[i];
            var $grade = $('#item_'+score.gradeItemId);
            $grade.attr('data-id',score.id);
            $grade.val(score.evalScore);
        }
    }

    /**
     *回显评审点
     */
    function echoPoint(data) {
        if (isNull(data)) {
            return false;
        }
        for (var i = 0; i < data.length; i++) {
            var point = data[i];
            var $pointDiv = $('#point_'+point.gradeItemId);
            $pointDiv.empty();
            var htmlstr = "";
            if (!isNull(point.bsReviewPointPages)) {
                for (var j = 0; j < point.bsReviewPointPages.length; j++) {
                    var page = point.bsReviewPointPages[j];
                    htmlstr += " <span class='' onclick='goToPage(" + page + ", 3)'>" + page + "页<img class='business_img'></span>"
                }
            }
            if (!isNull(point.teReviewPointPages)) {
                for (var j = 0; j < point.teReviewPointPages.length; j++) {
                    var page = point.teReviewPointPages[j];
                    htmlstr += " <span class='' onclick='goToPage(" + page + ", 2)'>" + page + "页<img class='technology_img'></span>"
                }
            }

            if (!isNull(point.quReviewPointPages)) {
                for (var j = 0; j < point.quReviewPointPages.length; j++) {
                    var page = point.quReviewPointPages[j];
                    htmlstr += " <span class='' onclick='goToPage(" + page + ", 1)'>" + page + "页<img class='qualifications_img'></span>"
                }
            }

            $pointDiv.append(htmlstr);
        }
    }

    function formRender() {
        layui.use('form', function () {
            var form = layui.form;
            form.render();
        });
    }

    /**
     * 详细评审，展示评审结果
     */
    function showExpertEval() {
        parent.window.location.href = "/expert/epcBidEval/epcDetailedEvalResultIndex";
    }

    function pageReloade() {
        parent.$(".sele").find("div").click();
    }
</script>

<script>
    function clearNoNum(obj) {
        //清除“数字”和“.”以外的字符
        obj.value = obj.value.replace(/[^\d.]/g, "");
        //验证第一个字符是数字而不是.
        obj.value = obj.value.replace(/^\./g, "");
        //只保留第一个. 清除多余的.
        obj.value = obj.value.replace(/\.{2,}/g, ".");
        obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
        //防止用户输入01类型的整数
        if (obj.value !== '' && obj.value.indexOf('.') === -1) {
            obj.value = parseFloat(obj.value)
        }
    }


    /**
     * 保存评分结果
     * @param obj
     */
    function saveGradeResult(obj) {
        //保存
        var id = $(obj).attr("data-id");
        var score = $(obj).val();
        //防止用户输入0.数据
        if (score == "0."){
            score = 0;
        }
        if (isNull(score)){
            return false;
        }
        $.ajax({
            url: "${ctx}/expert/epcBidEval/saveDetailedResult",
            data: {
                "id": id,
                "score": score
            },
            type: "POST",
            cache: false,
            success: function (result) {
                if (result.code === "1") {
                    $(obj).val(score);
                    validReviewComplete();
                } else if (result.code === "3") {
                    layerAlert(result.msg);
                }else {
                    layer.tips(result.msg,  $(obj), {
                        tips: [3, 'rgba(59, 135, 103, 1)'],
                        time: 4000
                    });
                    $(obj).val("");
                }
            },
            error: function () {
                layerAlert("保存错误！");
            }
        });
    }

    /**
     * 检查评审完成情况
     */
    function validReviewComplete() {
        var bidderId = $("#select-box").val();
        $.ajax({
            url: "${ctx}/expert/epcBidEval/validBidderScoreCompletion",
            data: {
                "bidderId": bidderId
            },
            type: "POST",
            cache: false,
            success: function (result) {
                if (result) {
                    $("#select-box option").each(function () {
                        var bidder_id = $(this).val();
                        if (bidderId === bidder_id) {
                            var name = $(this).text();
                            if (name.indexOf("(已评审)") === -1) {
                                name = name + "(已评审)";
                                $(this).html(name);
                                formRender();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 个人评审结束校验
     */
    function personalReviewEnd() {
        $.ajax({
            url: "${ctx}/expert/epcBidEval/checkEpcDetailedPersonalEnd",
            type: "POST",
            cache: false,
            success: function (result) {
                if (result.code == "1") {
                    supPersonalEnd();
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
     * */
    function supPersonalEnd() {
        layerConfirm("确认要结束个人评审吗？结束后将无法修改评审结果！", function () {
            $.ajax({
                url: "${ctx}/expert/epcBidEval/epcDetailedPersonalEnd",
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
     */
    function restartDetailedReview() {
        layerConfirm("确认要重评该环节吗? 确认后请通知其他评标专家刷新本页面!", function () {
            $.ajax({
                url: "${ctx}/expert/epcBidEval/epcDetailedRestartReview",
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
    function validDetailedGroupEnd() {
        $.ajax({
            url: "${ctx}/expert/epcBidEval/checkEpcDetailedGroupEnd",
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
                url: "${ctx}/expert/epcBidEval/endEpcDetailedGroupEnd",
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
