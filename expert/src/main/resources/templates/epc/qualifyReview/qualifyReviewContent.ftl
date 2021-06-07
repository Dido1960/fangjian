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
<link type="text/css" rel="stylesheet" href="${ctx}/css/qualifyReviewContent.css"/>
<body style="overflow: hidden;">
<div class="cont-left">
    <form class="layui-form" action="#">
        <div class="layui-form-item">
            <label class="layui-form-label">当前投标单位</label>
            <div class="layui-input-block">
                <select id="select-box" lay-filter="changeBidder" class="bidders-list">
                    <#list bidders as bidder>
                        <option value="${bidder.id }" data-index="${bidder_index}"
                                data-docid="${bidder.bidDocId}">${bidder_index +1}、${bidder.bidderName}<#if bidder.expertReviewStatusForBidder>(已评审)</#if></option>
                    </#list>
                </select>
            </div>
        </div>
    </form>
    <div class="document">
        <div class="document-btns">
            <#if !currentGrade.groupEnd?? || (currentGrade.groupEnd?? && currentGrade.groupEnd != 1)>
                <#if !expertReview.enabled?? || (expertReview.enabled?? && expertReview.enabled != 1)>
                    <span class="green-b" onclick="oneKeyQualified()">一键合格</span>
                    <span class="yellow-b" id="personEnd" onclick="personalReviewEnd()" style="width: 116px;">个人评审结束</span>
                <#else>
                    <#if expert.isChairman == "1">
                        <span class="green-b" onclick="restartReview()">环节重评</span>
                        <span class="yellow-b" onclick="validDetailedGroupEnd()" style="width: 116px;">小组评审结束</span>
                    </#if>
                </#if>
            </#if>
            <span class="blove-b" style="float: right;" onclick="evalResultPage()">评审结果</span>
        </div>
        <div id="grade-content" class="document-word">
            <ol>
                <li class="long">${currentGrade.name}</li>
                <li>页码</li>
            </ol>
            <div class="document-table">
                <table cellpadding=0 cellspacing=0>
                    <#list currentGrade.gradeItems as item>
                        <tr>
                            <td class="long" id="item_${item.id}">
                                <p>${item_index + 1}、${item.itemContent}</p>
                                <div class="radio">
                                    <div class="review radio-pass <#if expertReview.enabled == 1>disabled</#if>"
                                         data-result="1">
                                        <div class="garden">
                                            <i></i>
                                        </div>
                                        合格
                                    </div>
                                    <div class="review radio-unpass <#if expertReview.enabled == 1>disabled</#if>"
                                         data-result="0">
                                        <div class="garden">
                                            <i></i>
                                        </div>
                                        不合格
                                    </div>
                                </div>
                                <textarea class="reasonText" style="display: none" maxlength="200"
                                          <#if expertReview.enabled == 1>disabled</#if> onblur="saveComment(this)"></textarea>
                            </td>
                            <td id="point_${item.id}"></td>
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
        <li data-pdf-index="3" class="choice" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/BStemp.pdf">
            商务标文件
        </li>
        <li data-pdf-index="2" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/TEtemp.pdf">
            技术标文件
        </li>
        <li data-pdf-index="1" fdfsMark="/bidderFile/{bidDocId}/resources/TempConvert/QUtemp.pdf">
            资格证明文件
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
        $('.pradio-pass').removeClass('checked');
        $('.radio-unpass').removeClass('checked');
        $('.reasonText').slideUp();
        $.ajax({
            url: "${ctx}/expert/epcBidEval/getBidderData",
            data: {
                "gradeId": '${currentGrade.id}',
                "bidderId": bidderId
            },
            type: "POST",
            cache: false,
            success: function (data) {
                echoScore(data.qualifiedList);
                echoPoint(data.pointList);
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
        console.log(data);
        for (var i = 0; i < data.length; i++) {
            var score = data[i];
            var $grade = $('#item_' + score.gradeItemId);
            $grade.attr('data-id', score.id);
            var $textArea = $grade.find("textarea");
            var $unpass = $grade.find(".radio-unpass");
            var $pass = $grade.find(".radio-pass");

            $textArea.val(score.evalComments);

            if (score.evalResult == 0) {
                $unpass.addClass("checked");
                $textArea.slideDown();
            } else if (score.evalResult == 1) {
                $pass.addClass("checked");
                $grade.find(".pass").addClass("checked");
            } else {
                $textArea.val('');
            }
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
            var $pointDiv = $('#point_' + point.gradeItemId);
            $pointDiv.empty();
            var htmlstr = "";
            if (!isNull(point.quReviewPointPages)) {
                for (var j = 0; j < point.quReviewPointPages.length; j++) {
                    var page = point.quReviewPointPages[j];
                    htmlstr += " <span class='' onclick='goToPage(" + page + ", 1)'>" + page + "页<img class='qualifications_img'></span>"
                }
            }

            if (!isNull(point.teReviewPointPages)) {
                for (var j = 0; j < point.teReviewPointPages.length; j++) {
                    var page = point.teReviewPointPages[j];
                    htmlstr += " <span class='' onclick='goToPage(" + page + ", 2)'>" + page + "页<img class='technology_img'></span>"
                }
            }

            if (!isNull(point.bsReviewPointPages)) {
                for (var j = 0; j < point.bsReviewPointPages.length; j++) {
                    var page = point.bsReviewPointPages[j];
                    htmlstr += " <span class='' onclick='goToPage(" + page + ", 3)'>" + page + "页<img class='business_img'></span>"
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
    function evalResultPage() {
        parent.$("#review-result-form").submit();
    }

    function pageReloade() {
        parent.$("#check-number-li").find("a").click();
    }
</script>

<script>
    $(".review").bind("click", function () {
        var $radio = $(this).parents("td");
        var id = $radio.attr("data-id");
        if (isNull(id)){
            return false;
        }
        $(this).addClass("checked").siblings().removeClass("checked");
        var result = $(this).attr("data-result");
        var data = {
            "id": id,
            "evalResult": result
        }
        var $textarea = $radio.children("textarea");
        if ("1" == result) {
            $textarea.slideUp();
        } else {
            $textarea.slideDown();
            $textarea.select();
        }
        var bidderId = $("#select-box").val()
        saveGradeResult(data, bidderId);
    });

    /**
     * 保存不合格理由
     */
    function saveComment(e) {
        var $radio = $(e).parents("td");
        var id = $radio.attr("data-id");
        var evalComments = $(e).val().trim();
        if (isNull(evalComments)) {
            return false;
        }
        if (evalComments.length>200){
            layerAlert("输入理由过长，系统自动保存前200个字符");
        }
        // 防止理由过长，导致异常
        evalComments = evalComments.substr(0,200);
        var data = {
            "id": id,
            "evalComments": evalComments
        }
        var bidderId = $("#select-box").val()
        saveGradeResult(data, bidderId);
    }

    /**
     * 保存评分结果
     * @param data
     * @param bidderId
     */
    function saveGradeResult(data, bidderId) {
        //保存
        $.ajax({
            url: "${ctx}/expert/evalPlan/saveExpertReviewSingleItem",
            data: data,
            type: "POST",
            cache: false,
            success: function (result) {
                if (result.code === "1") {
                    validReviewComplete(bidderId);
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
     * 检查评审完成情况
     */
    function validReviewComplete(bidderId) {
        if (isNull(bidderId)) {
            return;
        }
        $.ajax({
            url: "${ctx}/expert/evalPlan/validBidderReviewComplete",
            data: {
                "bidderId": bidderId,
                "gradeId": '${currentGrade.id}'
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
                                $(this).text(name);
                                formRender();
                            }
                        }
                    });
                }
            },
            error: function () {
                layerAlert("保存错误！");
            }
        });
    }

    /**
     * 一键合格
     */
    function oneKeyQualified() {
        $.ajax({
            url: "${ctx}/expert/evalPlan/passAllQualified",
            data: {
                'gradeId': '${currentGrade.id}',
            },
            type: "POST",
            cache: false,
            success: function () {
                $(".radio").each(function () {
                    var $radio = $(this).parents("td");
                    var pass = $(this).children(".radio-pass");
                    pass.addClass("checked").siblings().removeClass("checked");
                    $radio.children("textarea").slideUp();
                });
                $("#select-box option").each(function () {
                    var name = $(this).text();
                    if (name.indexOf("(已评审)") === -1) {
                        name = name + "(已评审)";
                        $(this).text(name);
                    }
                });
                formRender();
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
            url: "${ctx}/expert/epcBidEval/validQualifiedEnd",
            type: "POST",
            cache: false,
            success: function (result) {
                if (result.code == "1") {
                    checkUnqualified();
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
     * 校验是否有扣分的评分项
     */
    function checkUnqualified() {
        layerConfirm("确认要结束个人评审吗？结束后将无法修改评审结果！", function () {
            $.ajax({
                url: "${ctx}/expert/epcBidEval/validUnqualified",
                type: "POST",
                cache: false,
                success: function (result) {
                    if (!result) {
                        // 有不合格项，需填写意见才能结束
                        setTimeout(opinionPage, 255);
                    } else {
                        // 没有不合格项，直接结束
                        personalEnd();
                    }
                },
                error: function () {
                    layerAlert("操作失败！");
                }
            });
        });
    }

    /**
     * 个人结束
     * */
    function personalEnd() {
        $.ajax({
            url: "${ctx}/expert/epcBidEval/personalReviewEnd",
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
    }

    /**
     * 跳转意见填写页面
     */
    function opinionPage() {
        hide_IWeb2018();
        window.top.layer.open({
            type: 2,
            title: "填写意见",
            content: "${ctx}/expert/epcBidEval/jumpOpinionPage",
            area: ['70%', '70%'],
            success: function (layero, index) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                //执行完成后的回调函数
                iframeWin.initSucc(function () {
                    window.top.layer.close(index);
                    personalEnd();
                });
            },
            end: function () {
                show_IWeb2018();
            }
        });
    }

    /**
     * 评审重评
     * */
    function restartReview() {
        layerConfirm("确认要重评该环节吗? 确认后请通知其他评标专家刷新本页面!", function () {
            $.ajax({
                url: "${ctx}/expert/epcBidEval/callQualifyPersonReview",
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
     * 小组评审结束校验
     */
    function validDetailedGroupEnd() {
        $.ajax({
            url: "${ctx}/expert/epcBidEval/validQualifyGroupReview",
            type: "POST",
            cache: false,
            success: function (data) {
                if (data.code === "2") {
                    layerAlert(data.msg, 2, 2);
                } else {
                    submitResult(data.msg);
                }
            },
            error: function (data) {
                layerLoading("操作失败！", 2, 2);
            }
        })
    }

    /**
     * 小组评审结束
     */
    function submitResult(msg) {
        layerConfirm(msg, function (index) {
            layerLoading('数据提交中, 请稍候...', null, 0);
            $.ajax({
                url: "${ctx}/expert/epcBidEval/endQualifyGroupReview",
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
</body>