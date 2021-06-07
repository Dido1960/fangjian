<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>数据上传评标</title>
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
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>

    <link rel="stylesheet" href="${ctx}/css/bidFileUpload.css">

    <style>
        .document ul li {
            <#if bidSection.bidClassifyCode == 'A08'>
                width: 20%;
            <#else>
                width: 33.3%;
            </#if>
        }
    </style>
</head>
<body>

<div class="title">
    <div class="much">当前共有投标人： ${data.bidderNum}家</div>
    <div class="progress">
        <div class="layui-progress layui-progress-big" lay-showPercent="true">
            <div class="layui-progress-bar layui-bg-blue"
                 lay-percent="<#if data.bidderNum?? && data.bidderNum != 0>${(data.uploadSuccessNum/data.bidderNum*100)?string["0.##"]}%<#else>0%</#if>"></div>
        </div>
    </div>
    <div class="complete" data-value="${data.uploadSuccessNum}">已上传${data.uploadSuccessNum}家</div>
</div>
<#if data.bidderFileInfoList??>
    <#list data.bidderFileInfoList as bidder>
        <div class="document">
            <h3 title="${bidder.bidderName}">${bidder.bidderName}
                <#if bidder.otherStatus == 2>
                    <img src="${ctx}/img/error.png" onclick="otherFileReUload(this, '${bidder.bidderId}', 6)">
                </#if>
            <#if bidder.allFileStatus == 1>
                <div class="success" id="all_${bidder.bidderId}" data-value="${bidder.allFileStatus}">
                    已完成
                    <img src="${ctx}/img/right.png" >
                </div>
            <#elseif bidder.allFileStatus == 2>
                <div class="error" id="all_${bidder.bidderId}" data-value="${bidder.allFileStatus}">
                    上传失败
                    <img src="${ctx}/img/error.png">
                </div>
            <#else>
                <div class="type" id="all_${bidder.bidderId}" data-value="${bidder.allFileStatus}">
                    上传中
                    <img src="${ctx}/img/waiting.png" class="waiting">
                </div>
            </#if>
            </h3>
            <ul>
                <li data-id="${bidder.bidderId}" data-type="1">
                    <img src="${ctx}/img/file.png" alt="">
                    <p>商务文件</p>
                    <#if bidder.businessStatus == 1>
                        <div class="finish" id="bus_${bidder.bidderId}" data-value="${bidder.businessStatus}">已上传</div>
                    <#elseif bidder.businessStatus == 2>
                        <div class="fail" id="bus_${bidder.bidderId}" data-value="${bidder.businessStatus}">失败</div>
                    <#else>
                        <div class="refresh" id="bus_${bidder.bidderId}" data-value="${bidder.businessStatus}"></div>
                    </#if>
                </li>
                <li data-id="${bidder.bidderId}" data-type="2">
                    <img src="${ctx}/img/technology.png" alt="">
                    <p>技术文件</p>
                    <#if bidder.technicalStatus == 1>
                        <div class="finish" id="tec_${bidder.bidderId}" data-value="${bidder.technicalStatus}">已上传</div>
                    <#elseif bidder.technicalStatus == 2>
                        <div class="fail" id="tec_${bidder.bidderId}" data-value="${bidder.technicalStatus}">失败</div>
                    <#else>
                        <div class="refresh" id="tec_${bidder.bidderId}" data-value="${bidder.technicalStatus}"></div>
                    </#if>
                </li>
                <li data-id="${bidder.bidderId}" data-type="3">
                    <img src="${ctx}/img/starFile.png" alt="">
                    <p>资格证明</p>
                    <#if bidder.qualificationsStatus == 1>
                        <div class="finish" id="qua_${bidder.bidderId}" data-value="${bidder.qualificationsStatus}">已上传</div>
                    <#elseif bidder.qualificationsStatus == 2>
                        <div class="fail" id="qua_${bidder.bidderId}" data-value="${bidder.qualificationsStatus}">失败</div>
                    <#else>
                        <div class="refresh" id="qua_${bidder.bidderId}" data-value="${bidder.qualificationsStatus}"></div>
                    </#if>
                </li>
                <#if bidSection.bidClassifyCode == 'A08'>
                    <li data-id="${bidder.bidderId}" data-type="4" >
                        <img src="${ctx}/img/list.png" alt="">
                        <p>工程量清单PDF</p>
                        <#if bidder.checklistStatus == 1>
                            <div class="finish" id="che_${bidder.bidderId}" data-value="${bidder.checklistStatus}">已上传</div>
                        <#elseif bidder.checklistStatus == 2>
                            <div class="fail" id="che_${bidder.bidderId}" data-value="${bidder.checklistStatus}">失败</div>
                        <#else>
                            <div class="refresh" id="che_${bidder.bidderId}" data-value="${bidder.checklistStatus}"></div>
                        </#if>
                    </li>
                    <li data-id="${bidder.bidderId}" data-type="5">
                        <img src="${ctx}/img/file.png" alt="">
                        <p>工程量清单XML</p>
                        <#if bidder.checklistXmlStatus == 1>
                            <div class="finish" id="xml_${bidder.bidderId}" data-value="${bidder.checklistXmlStatus}">已上传</div>
                        <#elseif bidder.checklistXmlStatus == 2>
                            <div class="fail" id="xml_${bidder.bidderId}" data-value="${bidder.checklistXmlStatus}">失败</div>
                        <#else>
                            <div class="refresh" id="xml_${bidder.bidderId}" data-value="${bidder.checklistXmlStatus}"></div>
                        </#if>
                    </li>
                </#if>
            </ul>
        </div>
    </#list>
</#if>

<script>
    var dataInterval = null;
    $(function () {
        layui.use('element', function () {
            var element = layui.element;
            element.render();
        });

        if ('${data.bidderNum}' != '${data.uploadSuccessNum}') {
            dataInterval = setInterval(getBidderFileUploadData, 1000)
        }
    })

    var uploadCount = 0

    function getBidderFileUploadData() {
        if (uploadCount != 0) {
            return;
        }
        uploadCount++;
        $.ajax({
            url: "${ctx}/staff/getBidderFileUploadData",
            type: "POST",
            cache: false,
            data: {
                "bidSectionId": '${bidSection.id}'
            },
            success: function (data) {
                if (data) {
                    if (!isNull(dataInterval) && data.bidderNum == data.uploadSuccessNum){
                        clearInterval(dataInterval);
                    }
                    showData(data);
                }
                uploadCount--;
            },
            error: function (e) {
                console.error(e);
                uploadCount--;
            }
        });
    }

    function showData(data) {
        //如果全部上传 直接刷新页面
        // if (data.uploadSuccessNum == data.bidderNum){
        //     window.location.reload();
        // }
        var $success = $(".complete");
        if ($success.attr("data-value") != data.uploadSuccessNum){
            $success.attr("data-value", data.uploadSuccessNum);
            $success.text("已上传"+ data.uploadSuccessNum +"家");
            var progess = 0;
            if (data.bidderNum != 0){
                progess = Number(data.uploadSuccessNum) / Number(data.bidderNum) * 100;
                progess = parseInt(progess);
            }
            $(".layui-progress-bar").attr("lay-percent", progess + '%');
            // layui.element.progress('progress',  progess + '%');
            layui.use('element', function () {
                var element = layui.element;
                element.render();
            });
        }

        if (isNull(data.bidderFileInfoList)){
            return false;
        }

        for (var i = 0; i < data.bidderFileInfoList.length; i++){
            var bidder = data.bidderFileInfoList[i];
            var $bidderAll = $("#all_" + bidder.bidderId);
            var $bidderBus = $("#bus_" + bidder.bidderId);
            var $bidderTec = $("#tec_" + bidder.bidderId);
            var $bidderQua = $("#qua_" + bidder.bidderId);
            if ($bidderAll.attr("data-value") != bidder.allFileStatus){
                changeAllStatus($bidderAll, bidder.allFileStatus);
            }
            if ($bidderBus.attr("data-value") != bidder.businessStatus){
                changeStatus($bidderBus, bidder.businessStatus);
            }
            if ($bidderTec.attr("data-value") != bidder.technicalStatus){
                changeStatus($bidderTec, bidder.technicalStatus);
            }
            if ($bidderQua.attr("data-value") != bidder.qualificationsStatus){
                changeStatus($bidderQua, bidder.qualificationsStatus);
            }
            if ('${bidSection.bidClassifyCode}' == 'A08'){
                debugger
                var $bidderChe = $("#che_" + bidder.bidderId);
                var $bidderXml = $("#xml_" + bidder.bidderId);
                if ($bidderChe.attr("data-value") != bidder.checklistStatus){
                    changeStatus($bidderChe, bidder.checklistStatus);
                }
                if ($bidderXml.attr("data-value") != bidder.checklistXmlStatus){
                    changeStatus($bidderXml, bidder.checklistXmlStatus);
                }
                if (bidder.businessStatus == 1 &&
                    bidder.technicalStatus == 1 &&
                    bidder.qualificationsStatus == 1 &&
                    bidder.checklistStatus == 1 &&
                    bidder.checklistXmlStatus == 1 &&
                    bidder.allFileStatus != 1){
                    updateAllStatus(bidder.id, function () {
                        changeAllStatus($bidderAll, 1);
                    });
                }
            }
        }
    }

    /**
     * 更新所有文件为成功
     */
    function updateAllStatus(id, success) {
        $.ajax({
            url: "${ctx}/staff/updateAllStatus",
            type: "POST",
            cache: false,
            data: {
                "id": id,
                "allFileStatus": 1
            },
            success: function (data) {
                if (data){
                    if (success && typeof (success) === "function") {
                        success();
                    }
                }
            },
            error:function (e) {
                console.error(e);
            }
        });
    }

    function changeStatus($obj, status) {
        $obj.attr("data-value", status);

        $obj.removeClass();
        if (status == 1){
            $obj.addClass("finish");
            $obj.text("已上传");
        }else if (status = 2){
            $obj.addClass("fail");
            $obj.text("失败");
        }else {
            $obj.addClass("refresh");
            $obj.text("");
        }
    }

    function changeAllStatus($obj, status) {
        $obj.attr("data-value", status);
        $obj.removeClass();
        if (status == 1){
            $obj.addClass("success");
            $obj.html("已完成<img src='${ctx}/img/right.png'>");
        }else if (status = 2){
            $obj.addClass("error");
            $obj.html("上传失败<img src='${ctx}/img/error.png'>");
        }else {
            $obj.addClass("type");
            $obj.html("上传中<img src='${ctx}/img/waiting.png' class='waiting'>");
        }
    }

    // 文件上传 绑定
    $("li").on("click", ".fail", function () {
        var $li = $(this).parent("li");
        var bidderId = $li.attr("data-id");
        var fileType = $li.attr("data-type");
        var baseId = fileType == 1 ? "bus_" : fileType == 2 ? "tec_" : fileType == 3 ? "qua_" : fileType == 4 ? "che_" : fileType == 5 ? "xml_" : "other_";
        $.ajax({
            url: "${ctx}/staff/bidderFileReUpload",
            type: "POST",
            cache: false,
            data: {
                "bidderId": bidderId,
                "fileType": fileType
            },
            success: function (data) {
                if(data){
                    $("#" + baseId + bidderId).attr("data-value", 0);
                    $("#" + baseId + bidderId).removeClass()
                    $("#" + baseId + bidderId).addClass("refresh").html("");
                    $("#all_"+ bidderId).attr("data-value", 0)
                        .removeClass().addClass("type")
                            .html("上传中<img src='${ctx}/img/waiting.png' class='waiting'>");
                }else {
                    doLoading("网络异常！", 2);
                }
            },
            error:function (e) {
                console.error(e);
            }
        });
    })

    function otherFileReUload(obj, bidderId, fileType) {
        $.ajax({
            url: "${ctx}/staff/bidderFileReUpload",
            type: "POST",
            cache: false,
            data: {
                "bidderId": bidderId,
                "fileType": fileType
            },
            success: function (data) {
                if(data){
                    $(obj).remove();
                    $("#all_"+ bidderId).attr("data-value", 0)
                        .removeClass().addClass("type")
                        .html("上传中<img src='${ctx}/img/waiting.png' class='waiting'>");
                }else {
                    doLoading("网络异常！", 2);
                }
            },
            error:function (e) {
                console.error(e);
            }
        });
    }
</script>
</body>
</html>