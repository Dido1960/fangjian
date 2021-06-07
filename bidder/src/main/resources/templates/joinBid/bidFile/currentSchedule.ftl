<!DOCTYPE html>
<html lang="" xmlns:c="http://www.w3.org/1999/XSL/Transform">
<head>
    <title>xxx</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_parameterName" content="${ _csrf.parameterName}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <!--[if lt IE 9]>
    <script src="/js/html5shiv.min.js"></script>
    <script src="/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/css/currentSchedule.css">
    <script>
        var laytpl;
        var initFlag = false;
        var loadingState = false;
        $(function () {
            layui.use(['laytpl', 'form'], function () {

                laytpl = layui.laytpl;
                form = layui.form;
            });
            setInterval(function () {
                if (loadingState) {
                    return;
                }
                loadingState = true;
                $.ajax({
                    url: '${ctx}/bidFile/getCurrentSchedule?bidSectionId=${bidSectionId}&bidderId=${bidderId}',
                    cache: false,
                    asyn: false,
                    success: function (data) {
                        if (!isNull(data)){
                            for(var i = 0; i < data.length; i++){
                                var schedule = data[i];
                                var $thisLi = $("#schedule_" + schedule.fileSchedule);
                                if ( ""+schedule.status !== $thisLi.attr("data-status")){
                                    $thisLi.attr("data-status", schedule.status);
                                    var $span = $thisLi.find("span").eq(0);
                                    var $img =  $thisLi.find("img").eq(0);
                                    if (schedule.status === 0){
                                        $span.text("进行中").removeClass().addClass("");
                                        $img.attr("src", "${ctx}/img/pdf/loading.gif");
                                    }else if (schedule.status === 1){
                                        $span.text("成功").removeClass().addClass("green-f");
                                        $img.attr("src", "${ctx}/img/pdf/ok.png");
                                    }else {
                                        $span.text("失败").removeClass().addClass("red-f");
                                        $img.attr("src", "${ctx}/img/pdf/error.png");
                                    }
                                }
                            }
                        }
                        loadingState = false;
                    }
                });
            }, 1000);
        })


    </script>

</head>
<body>
<div class="layui-from">
    <div class="cont">
        <h3>文件上传</h3>
        <ul class="uncomplete">
            <#list currentScheduleDTOS as schedule>
                <li id="schedule_${schedule.fileSchedule}" data-status="0">
                    <p>${schedule.fileSchedule.textName}</p>
                    <span class="">进行中</span>
                    <img src="${ctx}/img/pdf/loading.gif" alt="">
                </li>
            </#list>
        </ul>
        <div class="foot">提示：文件正在提交中，请耐心等待</div>
    </div>
</div>
</body>
</html>
