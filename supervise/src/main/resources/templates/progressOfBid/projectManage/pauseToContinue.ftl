<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>甘肃省电子开标评标平台</title>
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
    <script src="${ctx}/js/base64.js"></script>
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            user-select: none;
        }

        ol,
        ul {
            list-style: none;
        }

        .cont {
            width: 90%;
            margin: 0px auto;
            padding-top: 20px;
            box-sizing: border-box;
        }

        .cont .head {
            width: 100%;
            height: 64px;
            background: #F2F2F2;
            opacity: 1;
            border-radius: 6px;
            font-size: 16px;
            font-family: "Microsoft YaHei";
            font-weight: bold;
            line-height: 64px;
            color: #1B2A3D;
            padding: 0 20px;
            box-sizing: border-box;
        }

        .cont .head .head-btn {
            width: 190px;
            height: 64px;
            float: right;
        }

        .cont .head .head-btn span {
            display: inline-block;
            width: 85px;
            height: 40px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 400;
            line-height: 40px;
            color: #FFFFFF;
            text-align: center;
            margin: 0;
            margin-top: 12px;
            cursor: pointer;
        }

        .cont .head .head-btn span+span {
            margin-left: 10px;
        }

        .cont form {
            width: 100%;
        }

        .cont form div {
            width: 45%;
            height: 40px;
            float: left;
            margin-top: 20px;
        }

        .cont form div:nth-child(2n) {
            float: right;
        }

        .cont form div label {
            display: inline-block;
            width: 15%;
            height: 40px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 400;
            line-height: 40px;
            color: #1B2A3D;
        }

        .cont form div input {
            width: 84%;
            height: 40px;
            background: #F2F2F2;
            border: 1px solid #CECECE;
            opacity: 1;
            border-radius: 4px;
            box-sizing: border-box;
        }

        .cont h3 {
            width: 100%;
            height: 40px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: bold;
            line-height: 40px;
            color: #1B2A3D;
        }

        .cont textarea {
            width: 100%;
            height: 160px;
            background: #FFFFFF;
            border: 1px solid #CECECE;
            opacity: 1;
            border-radius: 6px;
            resize: none;
            padding: 10px;
            box-sizing: border-box;
        }

        textarea:disabled{
            background-color:#F2F2F2;
            cursor: no-drop;
        }
    </style>
</head>

<body>
    <div class="cont">
        <div class="head">
            标段信息<div class="head-btn">
                <#if projectPause.pauseStatus ==0>
                    <#--继续-->
                    <span class="green-b" onclick="pauseProject(1)">项目继续</span>
                <#else >
                    <#--暂停-->
                    <span class="yellow-b" onclick="pauseProject(0)">项目暂停</span>
                </#if>
            </div>
        </div>
        <form action="">
            <div>
                <label for="">标段名称</label>
                <input type="text" value="${bidSection.bidSectionName!}" disabled>
            </div>
            <div>
                <label for="">标段编号</label>
                <input type="text" value="${bidSection.bidSectionCode!}" disabled>
            </div>
            <div>
                <label for="">招标人</label>
                <input type="text" value="${tenderProject.tendererName!}" disabled>
            </div>
            <div>
                <label for="">标段类型</label>
                <input type="text" value="${bidSection.bidClassifyName}" disabled>
            </div>
            <div>
                <label for="">是否网上开标</label>
                <input type="text" value="<#if bidSection.bidOpenOnline ==1 >是<#else> 否</#if>" disabled>
            </div>
            <div>
                <label for="">开标时间</label>
                <input type="text" value="${tenderDoc.bidOpenTime}" disabled>
            </div>
        </form>
        <h3>暂停理由</h3>
        <textarea name="" id="reasons" placeholder="请输入"  <#if projectPause.pauseStatus ==0>disabled</#if>>${projectPause.pauseReason}</textarea>
    </div>
</body>
<script>
    var layer;
    layui.use(['element', 'layer'], function () {
        var element = layui.element;
        layer = layui.layer;
    });


    /**
     * 项目暂停
     */
    function pauseProject(val) {
       var res_str =  $("#reasons").val()
        if (res_str.length===0){
            layer.alert("请输入暂停理由",{icon:2});
            return
        }

        var successTip = "";
        var confirmTip = "";
        if (val === 0) {
            confirmTip = "是否暂停项目？";
        }
        if (val === 1) {
            confirmTip = "是否启动项目？";
        }
        var confirmWin = window.top.layer.confirm(confirmTip,
            {icon: 3, title: '操作提示'}
            , function () {
                var loadIndex = window.top.layer.load();
                $.ajax({
                    url: '${ctx}/gov/bidEval/pauseProject',
                    type: 'post',
                    cache: false,
                    data: {
                        bidSectionId: ${bidSection.id},
                        pauseStatus: val,
                        pauseReason: res_str
                    },
                    success: function (data) {
                        window.top.layer.close(loadIndex);
                        if (data) {
                            window.top.layer.closeAll();
                            window.top.location.reload();
                        } else {
                            layer.msg("操作失败", {icon: 5});
                        }
                    },
                    error: function (data) {
                        layer.close(loadIndex);
                        layer.msg("操作失败", {icon: 5});
                        console.error(data);
                    },
                });
            });
    }
</script>
</html>