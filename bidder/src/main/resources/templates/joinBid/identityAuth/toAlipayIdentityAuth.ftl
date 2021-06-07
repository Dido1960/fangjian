<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>支付宝认证页面</title>
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>
    <style>
        * {
            margin: 0;
            padding: 0;
        }

        .box {
            width: 100%;
        }

        .box h3 {
            width: 100%;
            height: 40px;
            font-size: 18px;
            font-family: Microsoft YaHei;
            font-weight: bold;
            line-height: 40px;
            text-align: center;
            color: rgba(34, 49, 101, 1);
            border-bottom: 1px solid #eee;
        }

        .box img {
            display: block;
            width: 20%;
            height: 20%;
            margin: 30px auto 0;
        }

        .box div {
            width: 100%;
            height: 40px;
            font-size: 18px;
            font-family: Microsoft YaHei;
            font-weight: bold;
            line-height: 40px;
            text-align: center;
            color: rgba(34, 49, 101, 1);
        }

        .box p {
            width: 100%;
            height: 40px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 500;
            line-height: 40px;
            text-align: center;
            color: red;
        }
    </style>
</head>
<body>
<input type="hidden" id="invalid" value="${invalid}">
<input type="hidden" id="completeAuth" value="${completeAuth}">
<#if !invalid?? && !completeAuth??>
    <#if url??>
        <form name='punchout_form' method='post' action='${url}'>
            <input type='hidden' name='biz_content' value='{&quot;certify_id&quot;:&quot;"${certifyId}";}'>
            <input type='submit' value='立即支付' style='display:none'>
        </form>

        <script>document.forms[0].submit();</script>
    </#if>
<#elseif invalid?? && invalid == 0>
    <div class="box">
        <h3>身份认证失败</h3>
        <img src="${ctx}/img/mistake.png" alt="">
        <div>二维码已失效</div>
        <p>请重试</p>
    </div>
<#elseif completeAuth?? && completeAuth == 1>
    <div class="box">
        <h3>身份认证成功</h3>
        <img src="${ctx}/img/correct.png" alt="">
        <div>您已身份认证完成，无需重复认证！</div>
        <p>恭喜你，完成了身份认证</p>
    </div>
</#if>

</body>
<script>

    var matchResult = window.navigator.userAgent.match(/AliApp\(AP\/([\d\.]+)\)/i);
    var apVersion = (matchResult && matchResult[1]) || '';  // 如: 10.1.58.00000170

    function ready(callback) {
        // 如果jsbridge已经注入则直接调用
        if (window.AlipayJSBridge) {
            callback && callback();
        } else {
            // 如果没有注入则监听注入的事件
            document.addEventListener('AlipayJSBridgeReady', callback, false);
        }
    }

    // startBizService 接口仅在支付宝 10.0.15 及以上支持
    // 需要接入者自行做下版本兼容处理 ！！
    function startAPVerify(options, callback) {
        AlipayJSBridge.call('startBizService', {
            name: 'open-certify',
            param: JSON.stringify(options),
        }, callback);
    }

    /**
     * 唤起认证流程
     * 参数: certifyId、url 需要通过支付宝 openapi 开放平台网关接口获取
     * 详细说明可查看文档下方的参数说明
     **/
    if ( isNull('${invalid}') && isNull('${completeAuth}')){
        ready(function () {
            // 需要确保在 AlipayJSBridge ready 之后才调用
            startAPVerify({
                certifyId: '${certifyId}',
                url: '${url}'
            }, function (verifyResult) {
                // 认证结果回调触发, 以下处理逻辑为示例代码，开发者可根据自身业务特性来自行处理
                if (verifyResult.resultStatus === '9000') {
                    // 验证成功，接入方在此处处理后续的业务逻辑
                    // ...
                    return;
                }

                // 用户主动取消认证
                if (verifyResult.resultStatus === '6001') {
                    // 可做下 toast 弱提示
                    return;
                }
                var errorCode = verifyResult.result && verifyResult.result.errorCode;
                // 其他结果状态码判断和处理 ...
            });
        });
    }
</script>
</html>