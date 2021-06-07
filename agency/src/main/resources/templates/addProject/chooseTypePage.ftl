<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>选择新增方式</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <style>
        html .layui-layer-title {
            padding: 0;
            border-bottom: 1px solid rgba(213, 213, 213, 1);
        }

        .newTan ul {
            width: 100%;
            height: 160px;
            float: left;
            margin-top: 50px;
            overflow: hidden;
        }

        .newTan ul li {
            width: 49.9%;
            height: 160px;
            float: left;
        }

        .newTan ul li img {
            display: block;
            width: 100px;
            height: 100px;
            margin: 0 auto;
        }

        .newTan ul li .btn {
            width: 96px;
            height: 40px;
            background: linear-gradient(257deg, rgba(19, 97, 254, 1) 0%, rgba(78, 138, 255, 1) 100%);
            filter: progid:DXImageTransform.Microsoft.gradient(GradientType=1 ,startColorstr=#1361fe, endColorstr=#4e8aff);
            opacity: 1;
            font-size: 16px;
            font-family: Microsoft YaHei;
            font-weight: 400;
            line-height: 40px;
            color: rgba(255, 255, 255, 1);
            text-align: center;
            cursor: pointer;
            margin: 20px auto
        }

        .newTan ul li .btn:hover {
            background: rgba(12, 113, 255, 1);
        }
    </style>
</head>
<body style="overflow: hidden">
<div class="newTan">
    <ul>
        <li onclick="syncProject()">
            <img src="${ctx}/img/new_1.png" alt="">
            <div class="btn">同步项目</div>
        </li>
        <li onclick="addProjectByGef()">
            <img src="${ctx}/img/new_2.png" alt="">
            <div class="btn">GEF添加</div>
        </li>
        <#--<li onclick="paperProject()">
            <img src="${ctx}/img/new_3.png" alt="">
            <div class="btn">纸质标</div>
        </li>-->
    </ul>
</div>
</body>
<script>
    /**
     * 同步项目信息（用于第三方对接）
     */
    function syncProject() {
        parent.layer.msg("用于对接同步第三方项目信息！");
    }

    /**
     * 通过导入招标文件（gef）新增项目信息
     */
    function addProjectByGef() {
        parent.layer.open({
            type: 2,
            title: ['添加项目', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
            content: "${ctx}/staff/addProjectByGefPage",
            area: ['600px', '520px'],
            btnAlign: 'c',
            shade: 0.3 ,
            btn: ['确认', '取消'],
            btn1: function (index, layero) {
                var body = parent.layer.getChildFrame('body', index);
                var iframeWin = parent.window[layero.find('iframe')[0]['name']];
                iframeWin.parseProjectInfo(function (result) {
                    if (result.code === "1") {
                        parent.layer.msg(result.msg, {
                            icon: result.code, end: function () {
                                showTenderInfo();
                            }
                        });
                    } else {
                        parent.layer.alert(result.msg, {
                            icon: result.code, yes: function () {
                                parent.layer.closeAll();
                                parent.location.reload();
                            }
                        });
                    }
                });
            },
            btn2: function (index) {
                // 点击取消的回调函数
                parent.layer.msg("已取消");
                parent.layer.close(index);
            }
        })
    }

    /**
     * 纸质标项目信息创建
     */
    function paperProject() {
        parent.layer.open({
            type: 2,
            title: "新建纸质标项目",
            shadeClose: false,
            area: ['1000px', '760px'],
            content: "/staff/createPaperProjectPage",
            btn: ['确认', '取消'],
            btn1: function (index, layero) {
                var body = parent.layer.getChildFrame('body', index);
                var iframeWin = parent.window[layero.find('iframe')[0]['name']];
                iframeWin.saveProjectInfo();
            },
            btn2: function (index) {
                // 点击取消的回调函数
                layer.msg("已取消");
                layer.close(index);
            }
        });
    }

    /**
     * 显示招标项目信息
     */
    function showTenderInfo() {
        parent.layer.open({
            type: 2,
            title: "招标项目信息",
            shadeClose: false,
            area: ['1000px', '760px'],
            content: "/staff/confirmProjectInfoPage",
            btn: ['确认', '取消'],
            btn1: function (index, layero) {
                var body = parent.layer.getChildFrame('body', index);
                var iframeWin = parent.window[layero.find('iframe')[0]['name']];
                iframeWin.saveProjectInfo();
            },
            btn2: function (index) {
                // 点击取消的回调函数
                layer.msg("已取消");
                layer.close(index);
            }
        });
    }

</script>

</html>