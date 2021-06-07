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
    <script id="cfca_js"></script>
    <script id="my_cfca_js"></script>

    <script src="${ctx}/js/base64.js"></script>
    <link rel="stylesheet" href="${ctx}/css/baseStaff.css">
    <script type="text/javascript">
        /**
         * 获取IE版本号
         *
         * @returns {*}
         */
        function getIEVersion() {
            var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
            var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判断是否IE<11浏览器
            var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判断是否IE的Edge浏览器
            var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
            if (isIE) {
                var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
                reIE.test(userAgent);
                var fIEVersion = parseFloat(RegExp["$1"]);
                if (fIEVersion === 7) {
                    return 7;
                } else if (fIEVersion === 8) {
                    return 8;
                } else if (fIEVersion === 9) {
                    return 9;
                } else if (fIEVersion === 10) {
                    return 10;
                } else {
                    return 6;//IE版本<=7
                }
            } else if (isEdge) {
                return 'edge';//edge
            } else if (isIE11) {
                return 11; //IE11
            } else {
                return -1;//不是ie浏览器
            }
        }

        // 判断当前浏览器 == > 添加不同的暂停事件
        if (getIEVersion() !== -1 && getIEVersion() > 8) {
            // IE内核浏览器
            $("#cfca_js").attr("src","${ctx}/js/cfca/IE_CryptoKit.js");
            $("#my_cfca_js").attr("src","${ctx}/js/cfca/IE_FileDec.js");
        } else {
            //非IE内核浏览器
            $("#cfca_js").attr("src","${ctx}/js/cfca/Chrome_CryptoKit.js");
            $("#my_cfca_js").attr("src","${ctx}/js/cfca/Chrome_FileDec.js");
        }
    </script>

</head>
<body onload="javascript:OnLoad();" onunload="javascript:OnUnLoad()">
<a id="CryptoKitExtension">Install CFCA CryptoKit Extension</a>
<header>
    <div class="text">
        <div class="name">CFCA文件解密</div>
        <div class="bao">
            <div class="try">权晟测试 <i></i></div>
        </div>
    </div>
</header>
<section>
    <div class="content">

        <button class="layui-btn" onclick="$('#demo01').hide()">最新版(整合版)</button>

        <div id="demo01">
            <div style="border: 2px solid cadetblue">
                <h2 style="text-align: center">从服务器获取信封加密结果</h2>
                <div>
                    <button class="layui-btn" onclick="requestFileDec()">请求解密</button>
                    <label>信封加密结果：</label>
                    <textarea class="file_envelope_val" rows="10" style="width: 700px"></textarea>
                </div>
                <div style="margin-top: 20px">
                    <button class="layui-btn layui-btn-danger" onclick="startFileEnc()">开始解密</button>
                    <label>解密数据:</label>
                    <input type="text" class="envelope_enc_val" style="width: 650px">
                </div>
            </div>
            <div style="margin-top: 30px;border: 2px solid cornflowerblue">
                <h2 style="text-align: center">使用企业证书加密返回给服务端</h2>
                <div style="margin-top: 20px">
                    <button class="layui-btn" onclick="setCert()">设置证书</button>
                    <label>公司证书:</label>
                    <textarea class="qs_cert_val" rows="10" style="width: 700px"></textarea>
                </div>
                <div style="margin-top: 20px">
                    <button class="layui-btn layui-btn-warm" onclick="startFileDec()">开始加密</button>
                    <label>加密数据:</label>
                    <textarea id="envelope_dec_val" rows="10" style="width: 700px"></textarea>
                </div>
            </div>
        </div>

        <div id="demo02">
            <div style="margin-top: 20px">
                <button class="layui-btn layui-btn-warm" onclick="fileEnc()">开始解密</button>
                <#--     1、获取信封加密-->
                <input type="text" class="file_envelope_val">
                <#--     2、企业证书-->
                <input type="text" class="qs_cert_val">
                <#--    3、获取解密数据-->
                <input type="text" class="envelope_enc_val">
                <#--     4、使用权晟证书，信封加密的数据-->
                <input type="text" class="envelope_dec_val">

            </div>
        </div>

    </div>
</section>
</body>
<script>
    layui.use('form', function () {
        var form = layui.form;
    });
    function requestFileDec() {
        var indexLoad = layer.load();
        // 客户端请求解密
        $.ajax({
            url: '${ctx}/cfca/clientDecRequest',
            type: 'post',
            cache: false,
            async: false,
            data: {
                bidSectionId: 1, // 标段主键
                bidderId: 1 // 投标人主键
            },
            success: function (data) {
                layer.close(indexLoad)
                if (!isNull(data)) {
                    // 信封加密结果
                    $(".file_envelope_val").val(data.data.userEnvelopeVal);
                    // 企业证书
                    $(".qs_cert_val").val(data.data.qsCertVal);
                } else {
                    layer.msg("")
                }
            },
            error: function (data) {
                console.error(data);
                layer.close(indexLoad)
            },
        });

    }

    /**
     * 开始解密
     */
    function startFileEnc(resultBack) {
        var envelope_val = $(".file_envelope_val").val();
        if (envelope_val.length > 0) {
            new MsgEnvelopeDecryptOnClick(envelope_val, ".envelope_enc_val",resultBack);
        }
    }

    /**
     * 设置加密证书
     */
    function setCert() {
        var qsCert = $(".qs_cert_val").val();
        if (qsCert.length > 0) {
            new SetBase64EncryptCertButtonOnClick(qsCert);
        } else {
            layer.msg("请先获取企业证书");
        }
    }

    /**
     * 数字信封加密
     */
    function startFileDec() {
        // 1、设置企业证书
        setCert();
        // 2、原文
        var enc_val = $(".envelope_enc_val").val();
        new EncryptMsgEnvelopeOnClick(enc_val, ".envelope_dec_val");
    }



    /**
     * 投标人解密
     */
    function fileEnc() {
        layerLoading("数据加载中..")
        // 从server获取数字信封加密结果
        requestFileDec();
        // 开始解密
        startFileEnc(function (result) {
            loadComplete();
            // chrome,返回errorcode  ie,返回error
            if (result.errorcode===0 || result.error===0){
                // 信封加密
                startFileDec();
                setTimeout(function () {
                    var envelopeDecVal = $(".envelope_dec_val").val();
                    if (envelopeDecVal.length>0){
                        responseEnvelopeDec(envelopeDecVal,indexLoad)
                    } else {
                        layer.msg("解密失败");
                    }
                },1000)
            } else {
                layer.msg("解密失败");
            }
        });
    }

    /**
     * 返回给服务器，信封加密结果
     */
    function responseEnvelopeDec(envelopeDecVal,indexLoad) {
        $.ajax({
          url:'${ctx}/cfca/clientDecResponse',
          type:'post',
          cache:false,
          async: false,
          data:{
              bidSectionId: 1 // 标段主键
              ,bidderId: 1 // 投标人主键
              ,encryptionData: JSON.stringify(envelopeDecVal)
          },
          success:function(data){
             if(!isNull(data)){
                 if (data){
                     layer.msg("恭喜您，解密成功",{icon:1})
                     window.top.layer.close(indexLoad)
                 }
             }
          },
          error:function(data){
             console.error(data);
              window.top.layer.close(indexLoad)
          },
        });
    }

</script>
</html>