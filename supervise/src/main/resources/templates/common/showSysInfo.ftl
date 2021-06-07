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
            width: 800px;
            min-height: 450px;
            background: #FFFFFF;
            opacity: 1;
            border-radius: 10px;
            padding-top: 20px;
            margin: 0 auto;
            box-sizing: border-box;
        }

        .cont .document {
            width: 760px;
            min-height: 357px;
            border: 1px solid #E2E2E2;
            opacity: 1;
            border-radius: 6px;
            margin: 0 auto;
            box-sizing: border-box;
        }

        .cont .document ol {
            width: 100%;
            height: 60px;
            background: #E2E2E2;
            opacity: 1;
            border-radius: 6px 6px 0px 0px;
        }

        .cont .document ol li {
            width: 45%;
            height: 60px;
            font-size: 16px;
            font-family: Microsoft YaHei;
            font-weight: bold;
            line-height: 60px;
            color: #1B2A3D;
            text-align: center;
            float: left;
        }

        .cont .document ol .small {
            width: 10%;
        }

        .document table {
            width: 100%;
        }

        .document table tr {
            width: 100%;
        }

        .document table tr td {
            width: 40%;
            height: 60px;
            min-height: 60px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: #1B2A3D;
            text-align: center;
            border-bottom: 1px solid #e2e2e2;
            border-right: 1px solid #e2e2e2;
            box-sizing: border-box;
        }

        .document table tr .small {
            width: 10% !important;
        }

        .document table tr td img {
            display: inline-table;
            margin: 0 auto;
        }

        .cont p {
            width: 100%;
            height: 40px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            line-height: 40px;
            color: #CC2B00;
            padding: 0 20px;
            box-sizing: border-box;
        }
    </style>
    <script>
        var laytpl;
        var initFlag = false;
        var loadingState = 0;
        $(function () {
            layui.use(['laytpl', 'form'], function () {
                laytpl = layui.laytpl;
                form = layui.form;
            });
            setInterval(function () {
                if (loadingState != 0) {
                    return;
                }
                loadingState++;
                $.ajax({
                    url: '${ctx}/gov/bidEval/nowBackPdf',
                    cache: false,
                    success: function (data) {
                        var experts = data.currentBack;
                        if (experts.length > 0) {
                            $("#expertUserName").text(experts[0].expertName);
                        }
                        console.log(data);

                        if ($(".uncomplete td").length == 1) {
                            var getTpl = document.getElementById("searchTemplate").innerHTML;
                            laytpl(getTpl).render(experts, function (html) {
                                $('.uncomplete').html(html);
                                form.render();
                            });
                        }else{
                            if (!isNull(experts)) {
                                for (var i = 0; i < experts.length; i++) {
                                    console.log(experts[i].nowConvertPdfs);
                                    for (var j = 0; j <experts[i].nowConvertPdfs.length; j++) {
                                        var pdf=experts[i].nowConvertPdfs[j];
                                        console.log("status:"+pdf.status+"::"+$(".uncomplete   td[expertname='"+experts[i].expertName+"'][templatename='"+pdf.templateName+"'][status=0]").length);
                                         if($(".uncomplete  td[expertname='"+experts[i].expertName+"'][templatename='"+pdf.templateName+"'][status=0]").length==1){
                                             if (pdf.status == 2) {
                                                 $("td[expertname='"+experts[i].expertName+"'][templatename='"+pdf.templateName+"']")
                                                     .html("<img class=\"err\" src=\"${ctx}/img/pdf/error.png\" alt=\"\">")
                                                 .attr("status",pdf.status);
                                             } else if (pdf.status == 1) {
                                                 $("td[expertname='"+experts[i].expertName+"'][templatename='"+pdf.templateName+"']")
                                                     .html("<img class=\"fin\" src=\"${ctx}/img/pdf/ok.png\" alt=\"\">")
                                                     .attr("status",pdf.status);
                                             }
                                         }
                                    }

                                }
                            }
                        }
                        setTimeout(function () {
                            loadingState--;
                        }, 200);
                    }
                });
            }, 200);
        })
    </script>

</head>
<body>
<div class="cont layui-from">
    <div class="cont">
        <div class="document">
            <ol>
                <li>评审专家</li>
                <li>文件名称</li>
                <li class="small">操作</li>
            </ol>
            <table class="uncomplete">
                <tr>
                    <td colspan="3">暂无回退数据</td>
                </tr>
            </table>
        </div>
    </div>
    <p>提示：数据正在回退中，请耐心等待</p>
</div>
</body>
<script id="searchTemplate" type="text/html">
    {{# if(isNull(d)||d.length==0){ }}
    {{# }else { }}

    {{# layui.each(d,function(i, experts){ }}
    {{# layui.each(experts.nowConvertPdfs,function(j, pdf){ }}
    <tr >
        {{#if(experts.expertName==pdf.expertName && j ==0 ){ }}
        <td rowspan="{{experts.nowConvertPdfs.length}}">{{experts.expertName}}</td>
        {{# } }}
        <td>{{pdf.templateName}}</td>
        <td class="small" status="{{pdf.status}}" expertname="{{experts.expertName}}" templatename="{{pdf.templateName}}">
            {{#if(pdf.status==2){ }}
            <img class="err" src="${ctx}/img/pdf/error.png" alt="">
            {{# }else if(pdf.status==1){ }}
            <img class="fin" src="${ctx}/img/pdf/ok.png" alt="">
            {{# }else{ pdf.status==0}}
                <img class="start" src="${ctx}/img/pdf/loading.gif" alt="">
            {{# } }}
        </td>
    </tr>
    {{#  }); }}
    {{#  }); }}
    {{# } }}
</script>
</html>
