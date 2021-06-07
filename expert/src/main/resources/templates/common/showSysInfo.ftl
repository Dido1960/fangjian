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

    <style>
        * {
            margin: 0;
            padding: 0;
            user-select: none;
        }

        ul,
        ol {
            list-style: none;
        }

        .cont {
            width: 90%;
            margin: 0 auto;
            padding-top: 20px;
            box-sizing: border-box;
        }

        .cont h3 {
            width: 100%;
            height: 60px;
            font-size: 16px;
            font-family: Microsoft YaHei;
            font-weight: bold;
            line-height: 60px;
            color: #1B2A3D;
            text-align: center;
            border-bottom: 1px solid #e2e2e2;
            box-sizing: border-box;
        }

        .cont ul {
            width: 100%;
            height: 500px;
        }

        .cont ul li {
            display: inline-block;
            width: 100%;
            min-height: 40px;
        }

        .cont ul li p {
            display: inline-block;
            width: 50%;
            height: 40px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            line-height: 40px;
            color: #1B2A3D;

        }

        .cont ul li span {
            display: inline-block;
            width: 49%;
            height: 40px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            line-height: 40px;
            color: #CECECE;
            text-align: right;
        }

        .cont ul li span img {
            display: inline-block;
            vertical-align: middle;
            margin-left: 20px;
        }

        .cont ul li .green-f {
            color: #01B078;
        }

        .foot {
            width: 100%;
            height: 60px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            line-height: 60px;
            color: #CC2B00;
            padding: 0 20px;
            border-top: 1px solid #dbdbdb;
            box-sizing: border-box;
        }
    </style>
    <script>
        var laytpl;
        var queryCount = 0;
        $(function () {
            layui.use(['laytpl', 'form'], function () {
                laytpl = layui.laytpl;
                form = layui.form;
            });
            var myInterVal = setInterval(function () {
                if (queryCount != 0) {
                    queryCount++;
                }
                $.ajax({
                    url: '${ctx}/expert/nowBackPdf',
                    cache: false,
                    success: function (data) {
                        var reportDtos = data.reportDtos;
                        if (!isNull(reportDtos) && $(".uncomplete li").length == 0) {
                            // 专家名称
                            var getTpl = document.getElementById("searchTemplate").innerHTML;
                            laytpl(getTpl).render(reportDtos, function (html) {
                                $('.uncomplete').html(html);
                                form.render();
                            });
                        } else {
                            if (!isNull(reportDtos)){
                                for (var i = 0; i < reportDtos.length; i++) {
                                    var report = reportDtos[i];
                                    var obj = $(".uncomplete li[templateName='" + report.templateName + "']");

                                    if (report.status != $(obj).attr("status")) {
                                        if (report.status == 0) {
                                            $(obj).find("span").remove();
                                            $(obj).append("<span>正在生成<img src=\"${ctx}/img/pdf/loading.gif\" alt=\"\"></span>")
                                        } else if (report.status == 1) {
                                            //生成完成
                                            $(obj).find("span").remove();
                                            $(obj).append("<span class=\"green-f\">生成成功<img src=\"${ctx}/img/pdf/ok.png\" alt=\"\"></span>")
                                        } else if (report.status == 2) {
                                            //生成失败
                                            $(obj).find("span").remove();
                                            $(obj).append("<span class=\"red-b\">生成失败<img src=\"${ctx}/img/pdf/error.png\" alt=\"\"></span>");
                                        }
                                        $(obj).attr("status",report.status);
                                    }
                                }
                            }
                        }
                        // 生成成功的个数
                        var succCount = 0;
                        $.each(reportDtos, function (index, report) {
                            if (report.status == 1) {
                                succCount++;
                            }
                        })
                        // 进度成功 关闭弹窗
                        if (isNull(reportDtos) || succCount == reportDtos.length) {
                            window.clearInterval(myInterVal);
                            window.top.layer.closeAll();
                            doLoading('评标报告合成中，请稍等片刻...','',30)
                        }
                        queryCount--;
                    }
                });
            }, 2000);
        })

    </script>

</head>
<body>
<div class="cont">
    <h3>正在生成评标报告</h3>
    <ul class="uncomplete">
    </ul>
    <div class="foot">
        <span class="layui-badge">提示</span> 系统正在生成评标报告,请耐心等待。
    </div>
</div>
</body>
<script id="searchTemplate" type="text/html">
    {{# layui.each(d,function(index, report){ }}
    <li templateName="{{report.templateName}}" status="{{report.status}}">
        <p>{{report.templateName}}</p>
        {{#if(report.status==2){ }}
        <span class="red-b">生成失败<img src="${ctx}/img/pdf/error.png" alt=""></span>
        {{# }else if(report.status==1){ }}
        <span class="green-f">生成成功<img src="${ctx}/img/pdf/ok.png" alt=""></span>
        {{# }else{ report.status==0}}
        <span>正在生成<img src="${ctx}/img/pdf/loading.gif" alt=""></span>
        {{# } }}
    </li>
    {{#  }); }}
</script>
</html>
