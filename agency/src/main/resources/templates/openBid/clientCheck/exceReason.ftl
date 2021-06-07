<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>甘肃省市政房建电子开标辅助系统</title>
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
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <style>
        html .layui-layer-title {
            padding: 0;
            border-bottom: 1px solid rgba(213, 213, 213, 1);
        }

        html .layui-layer-btn .layui-layer-btn0 {
            background-color: rgba(19, 97, 254, 1);
        }

        .layui-layer-btn,
        .layui-layer-btn-c {
            background: rgba(239, 243, 244, 1);
        }

        .tan table {
            width: 90%;
            margin: 20px auto;
        }

        .tan table tr {
            height: 74px;
        }

        .tan table td,
        .tan table th {
            text-align: center;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            color: rgba(34, 49, 101, 1);
        }

        .tan table input {
            width: 357px;
            height: 36px;
            border: 1px solid rgba(213, 213, 213, 1);
            opacity: 1;
            outline: none;
            padding: 0 10px;
            box-sizing: border-box;
        }
    </style>
</head>
<body>

<div class="tan layui-form">
    <table class="layui-table">
        <thead>
        <tr>
            <th>投标人名称</th>
            <th>检查类型</th>
            <th>原因</th>
        </tr>
        </thead>
        <tbody>
        <#list endingCheck.boiList as boi>
            <#if endingCheck.isA10Bid>
                <tr>
                    <td>${boi.bidderName}</td>
                    <td>投标人身份</td>
                    <td>
                        <input lay-verify="required" type="text" placeholder="请输入.." class="identityReason" value="${boi.identityReason.exceptionReason}"
                               reasonId="${boi.identityReason.id}" bidderId="${boi.bidderId}">
                    </td>
                </tr>
            <#else >
                <#if boi.bidderIdentityStatus==0&&boi.marginPayStatus==0>
                    <tr>
                        <td rowspan="2">${boi.bidderName}</td>
                        <td>投标人身份</td>
                        <td>
                            <input lay-verify="required"  type="text" placeholder="请输入.." class="identityReason" value="${boi.identityReason.exceptionReason}"
                                   reasonId="${boi.identityReason.id}" bidderId="${boi.bidderId}" >
                        </td>
                    </tr>
                    <tr>
                        <td>保证金</td>
                        <td>
                            <input lay-verify="required" type="text" placeholder="请输入.." class="marginPayReason" value="${boi.marginPayReason.exceptionReason}"
                                   reasonId="${boi.marginPayReason.id}" bidderId="${boi.bidderId}" >
                        </td>
                    </tr>
                <#else >
                    <#if boi.bidderIdentityStatus==0>
                        <tr>
                            <td>${boi.bidderName}</td>
                            <td>投标人身份</td>
                            <td>
                                <input lay-verify="required" type="text" placeholder="请输入.." class="identityReason" value="${boi.identityReason.exceptionReason}"
                                       reasonId="${boi.identityReason.id}" bidderId="${boi.bidderId}" >
                            </td>
                        </tr>
                    <#elseif boi.marginPayStatus==0>
                        <tr>
                            <td>${boi.bidderName}</td>
                            <td>保证金</td>
                            <td>
                                <input lay-verify="required" type="text" placeholder="请输入.." class="marginPayReason" value="${boi.marginPayReason.exceptionReason}"
                                       reasonId="${boi.marginPayReason.id}" bidderId="${boi.bidderId}" >
                            </td>
                        </tr>
                    </#if>
                </#if>
            </#if>
        </#list>
        </tbody>
    </table>
</div>
<script>
    var successFun = null;
    var data = [];

    function notNull() {
        data = [];
        var flag = true;
        $(".identityReason").each(function () {
            var id1 = $(this).attr("reasonId");
            var bidderId1 = $(this).attr("bidderId");
            var reason1 = $(this).val();
            if (reason1 == null || reason1 == ""){
                flag = false;
                return false;
            }
            var entity ={"id":id1,"bidSectionId":'${endingCheck.bidSectionId}',"bidderId":bidderId1,"exceptionType":1,"exceptionReason":reason1};
            data.push(entity);
        })
        $(".marginPayReason").each(function () {
            var id2 = $(this).attr("reasonId");
            var reason2 = $(this).val();
            var bidderId2 = $(this).attr("bidderId");
            if (reason2 == null || reason2 == ""){
                flag = false;
                return false;
            }
            var entity ={"id":id2,"bidSectionId":'${endingCheck.bidSectionId}',"bidderId":bidderId2,"exceptionType":2,"exceptionReason":reason2};
            data.push(entity);
        })
        return flag;
    }

    function updateReason(func) {
        successFun = func;
        if (notNull()){
            var params = JSON.stringify(data);
            // console.log(data);
            sendDate(params);
        }else {
            layer.msg("原因不可为空！", {icon: 2});
        }
    }

    var l1 = false;
    function sendDate(params) {
        if (l1){
            return false;
        }
        l1 = true;
        loadComplete();
        $.ajax({
            url: "${ctx}/clientCheck/saveReasonList",
            type: "POST",
            cache: false,
            data : {"str":params},
            success: function (data) {
                loadComplete();
                l1 = false;
                if (data) {
                    window.top.layer.msg("保存成功！", {icon: 1,end: successFun});
                }
            },
            error:function (e) {
                loadComplete();
                l1 = false;
                console.error(e);
            }
        });
    }
</script>
</body>


