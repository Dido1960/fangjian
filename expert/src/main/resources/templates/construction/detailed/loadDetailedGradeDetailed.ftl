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
<link rel="stylesheet" href="${ctx}/css/conDetailedCont.css">
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
                        <span class="green-b" id="noDeduct" onclick="oneKeyNoDeduct()">一键不扣分</span>
                        <span class="yellow-b" id="personEnd" onclick="personalReviewEnd()" style="width: 116px;">个人评审结束</span>
                    <#else>
                        <#if expert.isChairman == "1">
                            <span class="green-b" onclick="restartDetailedReview()">环节重评</span>
                            <span class="yellow-b" onclick="validDetailedGroupEnd()" style="width: 116px;">小组评审结束</span>
                        </#if>
                    </#if>
                </#if>
                <div class="blove-b" onclick="evalResultPage()">评审结果</div>
            </div>
            <div class="bottom">
                <ol>
                    <li class="long">${currentGrade.name}（${currentGrade.score}分）</li>
                    <li>页码</li>
                </ol>
                <div  class="gradeContent">
                    <ul>
                        <#list currentGrade.gradeItems as item>
                            <li>
                                <div class="long lis">
                                    <p>${item_index + 1}、${item.itemContent}(扣分：${item.score}分)</p>
                                    <div class="inps" id="item_${item.id}">
                                        <div class="radio">
                                            <div class="review pass <#if expertReview.enabled == 1>disabled</#if>" data-result="1">
                                                <div class="circular">
                                                    <i></i>
                                                </div>
                                                <b>不扣分</b>
                                            </div>
                                            <div class="review unpass <#if expertReview.enabled == 1>disabled</#if>" data-result="0">
                                                <div class="circular">
                                                    <i></i>
                                                </div>
                                                <b>扣分</b>
                                            </div>
                                        </div>
                                        <textarea class="reasonText" style="display: none" <#if expertReview.enabled == 1>disabled</#if> onblur="saveComment(this)" maxlength="200"></textarea>
                                    </div>
                                </div>
                                <div class="lis" id="point_${item.id}"></div>
                            </li>
                        </#list>
                    </ul>
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
        $('.pass').removeClass('choice');
        $('.unpass').removeClass('choice');
        $('.reasonText').slideUp();
        $.ajax({
            url: "${ctx}/expert/conBidEval/getBidderData",
            data: {
                "gradeId": '${currentGrade.id}',
                "bidderId": bidderId
            },
            type: "POST",
            cache: false,
            success: function (data) {
                echoScore(data.deductList);
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
        for (var i = 0; i < data.length; i++) {
            var score = data[i];
            var $grade = $('#item_'+score.gradeItemId);
            $grade.attr('data-id',score.id);
            var $textArea = $grade.find("textarea");
            var $unpass = $grade.find(".unpass");
            var $pass = $grade.find(".pass");

            $textArea.val(score.deductComments);

            if (score.evalResult == 0){
                $unpass.addClass("choice");
                $textArea.slideDown();
            }else if (score.evalResult == 1){
                $pass.addClass("chose");
                $grade.find(".pass").addClass("choice");
            }else{
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
            var $pointDiv = $('#point_'+point.gradeItemId);
            $pointDiv.empty();
            var htmlstr = "";
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
    function evalResultPage() {
        parent.window.location.href = "${ctx}/expert/conBidEval/evalResultDetailedIndex";
        /*parent.$("#review-result-form").submit();*/
    }

    function pageReloade() {
        parent.$(".sele").find(".flow-num").click();
    }
</script>

<script>
    $(".review").bind("click", function(){
        var $radio = $(this).parents(".inps");
        var deductId = $radio.attr("data-id");
        $(this).addClass("choice").siblings().removeClass("choice");
        var result = $(this).attr("data-result");
        var data = {
            "id": deductId,
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
        var $radio = $(e).parent();
        var deductId = $radio.attr("data-id");
        var deductComments = $(e).val().trim();
        if (isNull(deductComments)){
            return false;
        }
        if(deductComments.length > 200){
            layerAlert("输入理由过长，系统自动保存前200个字符");
            // 防止理由过长，导致异常
            deductComments = deductComments.substr(0,200);
        }
        var data = {
            "id": deductId,
            "deductComments": deductComments
        }
        var bidderId = $("#select-box").val()
        saveGradeResult(data, bidderId);;
    }

    /**
     * 保存评分结果
     * @param data
     * @param bidderId
     */
    function saveGradeResult(data, bidderId) {
        //保存
        $.ajax({
            url: "${ctx}/expert/conBidEval/saveExpertReviewSingleItemDeduct",
            data: data,
            type: "POST",
            cache: false,
            success: function (result) {
                if(result.code === "1"){
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
            url: "${ctx}/expert/conBidEval/validBidderDeductCompletion",
            data: {
                "bidderId": bidderId,
                "gradeId": '${currentGrade.id}'
            },
            type: "POST",
            cache: false,
            success: function (result) {
                if(result){
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
                }
            },
            error: function () {
                layerAlert("保存错误！");
            }
        });
    }

    //一键不扣分
    function oneKeyNoDeduct() {
        $.ajax({
            url: "${ctx}/expert/conBidEval/oneKeyNoDeduct",
            data: {
                'gradeId': '${currentGrade.id}',
            },
            type: "POST",
            cache: false,
            success: function (result) {
                $(".radio").each(function () {
                    var $radio = $(this).parents(".inps");
                    var $noDeduct = $(this).children(".pass");
                    $noDeduct.addClass("choice").siblings().removeClass("choice");
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
            url: "${ctx}/expert/conBidEval/checkPersonalDeductEnd",
            type: "POST",
            cache: false,
            success: function (result) {
                if (result.code == "1") {
                    checkDeduct();
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
    function checkDeduct() {
        layerConfirm("确认要结束个人评审吗？结束后将无法修改评审结果！", function () {
            $.ajax({
                url: "${ctx}/expert/conBidEval/checkDeduct",
                type: "POST",
                cache: false,
                success: function (result) {
                    if (result) {
                        // 有不合格项，需填写意见才能结束
                        setTimeout(deductOpinionPage,200);
                    } else {
                        // 没有不合格项，直接结束
                        conPersonalEnd();
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
    function conPersonalEnd() {
        $.ajax({
            url: "${ctx}/expert/conBidEval/personalDetailedReviewEnd",
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
    function deductOpinionPage() {
        hide_IWeb2018();
        window.top.layer.open({
            type: 2,
            title: "填写意见",
            content: "${ctx}/expert/conBidEval/deductOpinionPage",
            area: ['70%', '70%'],
            success: function (layero, index) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                //执行完成后的回调函数
                iframeWin.initSucc(function () {
                    window.top.layer.close(index);
                    conPersonalEnd();
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
    function restartDetailedReview() {
        layerConfirm("确认要重评该环节吗? 确认后请通知其他评标专家刷新本页面!", function () {
            $.ajax({
                url: "${ctx}/expert/conBidEval/restartDetailedReview",
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
            url: "${ctx}/expert/conBidEval/validDetailedGroupEnd",
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
                url: "${ctx}/expert/conBidEval/endDetailedGroupReview",
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
