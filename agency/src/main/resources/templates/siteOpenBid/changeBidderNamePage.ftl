<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>甘肃省房建市政电子辅助开标系统</title>
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
    <link rel="stylesheet" href="${ctx}/css/fileCursor.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            user-select: none;
        }

        .cont {
            width: 80%;
            margin: 0 auto;
            padding-top: 40px;
            box-sizing: border-box;
        }

        .cont h3 {
            width: 100%;
            height: 60px;
            font-size: 14px;
            font-family: Source Han Sans CN;
            font-weight: 400;
            line-height: 60px;
            color: #F53923;
            text-align: center;
        }

        .cont .layui-form {
            width: 100%;
            height: 40px;
        }

        .cont .layui-form .line {
            width: 100%;
            height: 40px;
            margin-bottom: 20px;
        }

        .cont .layui-form label {
            display: block;
            width: 150px;
            height: 40px;
            font-size: 14px;
            font-family: Source Han Sans CN;
            font-weight: 400;
            line-height: 40px;
            color: #223165;
            text-align: center;
            float: left;
        }

        .cont .layui-form input {
            width: calc(100% - 250px);
            height: 40px;
            background: #EFF4F7;
            border: 1px solid #D5D5D5;
            opacity: 1;
            padding: 0 10px;
            box-sizing: border-box;
            float: left;
        }

        .cont .layui-form-radio {
            float: left;
            margin: 5px 10px;
        }

        .cont .layui-form-radio>i:hover,
        .cont .layui-form-radioed>i {
            color: #0066cc;
        }

        .foot {
            width: 100%;
            height: 76px;
            background: #E8EBF1;
            opacity: 1;
            position: fixed;
            bottom: 0;
        }

        .foot .btns {
            width: 180px;
            height: 40px;
            margin: 20px auto 0;
        }

        .btns span {
            display: inline-block;
            width: 80px;
            height: 40px;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 400;
            line-height: 40px;
            text-align: center;
            cursor: pointer;
            border-radius: 5px;
        }

        .btns span:hover {
            opacity: 0.7;
        }

        .btns span:nth-child(1) {
            color: #FFFFFF;
            background: #0066cc;
            float: left;
        }

        .btns span:nth-child(2) {
            color: #0066cc;
            border: 1px solid #0066cc;
            float: right;
            box-sizing: border-box;
            margin-left: 20px;
        }
    </style>
</head>
<body>
    <div class="cont">
        <h3>经系统检测，当前投标单位名称与文件解析出的投标单位名称不一致，请进行选择确认</h3>
        <form action="" class="layui-form">
            <div class="line">
                <input type="radio" lay-filter="bidderName" name="bidderName" checked="checked" value="${dataBaseBidderName}">
                <label for="">当前名称</label>
                <input type="text" value="${dataBaseBidderName}">
            </div>
            <div class="line">
                <input type="radio" lay-filter="bidderName" name="bidderName" value="${fileBidderName}">
                <label for="">文件解析名称</label>
                <input type="text" value="${fileBidderName}">
            </div>
        </form>
    </div>
    <div class="foot">
        <div class="btns">
            <span onclick="getBidderName()">保存</span>
            <span onclick="cancle()">取消</span>
        </div>
    </div>
</body>
<script>
    var successCallback;
    var bidder;
    var bidderFile;
    var bidderOpen;
    var bidderName = '${dataBaseBidderName}';
    layui.use('form', function () {
        var form = layui.form;
        form.on('radio(bidderName)', function (data) {
            //  当前选中的value值
            bidderName = data.value;
        });
        form.render();
    });

    function initSucc(_bidder, _bidderOpen, _bidderFile, _successCallback) {
        bidder = _bidder;
        bidderFile = _bidderFile;
        bidderOpen = _bidderOpen;
        successCallback = _successCallback;
    }

    function getBidderName() {
        if (isNull(bidderName)) {
            window.top.layer.alert("请选择投标人名称！");
            return;
        } else {
            bidder.bidderName = bidderName;
            var current_msg = window.top.layer.msg('数据保存中...', {
                icon: 16,
                shade: [0.3, '#393D49'],
                time: 0
            });
            var siteBidDecryptDto = {};
            siteBidDecryptDto.updateBidder = bidder;
            siteBidDecryptDto.updateBidderOpenInfo = bidderOpen;
            siteBidDecryptDto.updateBidderFileInfo = bidderFile;
            $.ajax({
                url: "${ctx}/siteOpenBid/saveSiteBidDecryptInfo",
                contentType : "application/json;charset=utf-8",
                dataType : "json",
                type: "POST",
                cache: false,
                data: JSON.stringify(siteBidDecryptDto),
                success: function (data) {
                    if(!data){
                        window.top.layer.msg("数据保存失败", {icon: 5, time: 3000});
                    } else {
                        window.top.layer.close(current_msg);
                        if(successCallback && typeof(successCallback)=="function"){
                            successCallback();
                        }
                    }
                },
                error: function (data) {
                    window.top.layer.close(current_msg);
                    console.error(data);
                    window.top.layer.msg("数据保存失败", {icon: 5, time: 3000});
                }
            });
        }
    }

    function cancle() {
        //先得到当前iframe层的索引
        var index = parent.layer.getFrameIndex(window.name);
        //再执行关闭
        parent.layer.close(index);
    }
</script>
</html>