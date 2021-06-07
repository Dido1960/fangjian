<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
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
    <script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/uploadDecryptFile.css">
    <style>
        <#if bidSection.bidClassifyCode == "A10">
        .tltle li {
            width: 16.5% !important;
        }

        .neiRong li div {
            width: 16.1% !important;
        }

        </#if>
    </style>
</head>
<body>

<div class="big">
    <h3>招标项目信息</h3>
    <form class="layui-form" action="javascript:void(0)">
        <div class="inp"><label for="">标段名称</label><input type="text" placeholder="请输入" class="layui-bg-gray" readonly
                                                          value="${bidSection.bidSectionName}"></div>
        <div class="inp"><label for="">标段类型</label><input type="text" placeholder="请输入" class="layui-bg-gray" readonly
                                                          value="${bidSection.bidClassifyName}"></div>
        <div class="inp"><label for="">标段编号</label><input type="text" placeholder="请输入" class="layui-bg-gray" readonly
                                                          value="${bidSection.bidSectionCode}"></div>
        <#if bidSection.bidClassifyCode == 'A08' || bidSection.bidClassifyCode == 'A12'>
            <div class="inp">
                <label for=""><#if bidSection.bidClassifyCode =='A08'>招标控制价<#else>最高投标限价</#if></label>
                <input type="text" placeholder="请输入" class="layui-bg-gray" readonly
                       value="<#if tenderDoc.controlPrice?? && tenderDoc.controlPrice != ''>${((tenderDoc.controlPrice)?number)?string(",###.00")}元</#if>">
            </div>
        </#if>
        <div class="long"><label for="">开标地点</label>
            <input type="text" class="bidOpenPlace" onblur="validData()" placeholder="请输入"
                   value="${tenderDoc.bidOpenPlace}" lay-verify="required" lay-reqText="请填写开标地点">
        </div>
        <button style="visibility: hidden" class="layui-btn" lay-submit="" lay-filter="formSub" id="submitForm">验证表单
        </button>
    </form>
</div>
<div class="shang">
    <div style="display: inline-block">
        <div class="layui-btn layui-btn-warm" onclick="toggleDecBidder(this,'1')">已解密</div>
        <div class="layui-btn layui-btn-primary" onclick="toggleDecBidder(this,'0')" style="margin-left: -10px">未解密
        </div>
    </div>
    当前共有投标人: <span class="bidder_count">0</span> 家&emsp;&emsp;
    <span style="color: rgba(19, 97, 254, 1)" class="totalTime">总用时:0分0秒</span>
    <#--    <button class="layui-btn" onclick="addBidderFile()">添加投标文件</button>-->
    <span class="green"><span id="real_time_count">已解密0家，还剩0家未解密</span>
        <#if lineStatus.bidderCheckStatus==2>
            <#if lineStatus.decryptionStatus??>
                <#if lineStatus.decryptionStatus == 1>
                    <span class="layui-btn layui-btn-warm dec-end" onclick="switchDecryptStatus(2)">解密结束</span>
                <#elseif lineStatus.decryptionStatus == 2>
                    <span class="red" onclick="switchDecryptStatus(1)">重启解密</span>
                    <#-- 资格预审项目开启质询-->
                    <#if bidSection.bidClassifyCode == "A10">
                        <#if lineStatus.questionStatus??>
                            <#if lineStatus.questionStatus == 1>
                                <span class="layui-btn layui-btn-warm" onclick="switchQuestionStatus(2)">结束质询</span>
                            <#elseif lineStatus.questionStatus == 2>
                                <span class="red" onclick="switchQuestionStatus(1)">重启质询</span>
                            </#if>
                        <#else>
                            <span class="layui-btn layui-btn-danger" onclick="switchQuestionStatus(1)">开启质询</span>
                        </#if>
                    </#if>
                </#if>
            <#else>
                <span class="red" onclick="switchDecryptStatus(1)">开始解密</span>
            </#if>

        </#if>
    </span>
</div>
<ul class="tltle">
    <li>序号</li>
    <li>投标人名称</li>
    <li>投标解密状态</li>
    <#--    <li>招标解密状态</li>-->
    <li>解密用时</li>
    <#if bidSection.bidClassifyCode == "A10">
        <li>质询状态</li>
    </#if>
    <li>操作</li>
</ul>
<ul class="neiRong" id="bid-open-success-list">

</ul>
<ul class="neiRong" id="bid-open-fail-list" style="display: none">

</ul>

<div class="kong"></div>
</body>
<#--解密成功 模板渲染-->
<script id="successBidderListTemplate" type="text/html">
    {{#  var successIndex = 0 }}
    {{#  layui.each(d, function(index, bidder){ }}
    {{#  var decryptStatus = bidder.bidderOpenInfo.decryptStatus; }}
    {{#  var tenderDecryptStatus = bidder.bidderOpenInfo.tenderDecryptStatus; }}
    {{#  var tenderRejection = bidder.bidderOpenInfo.tenderRejection; }}
    {{#  var dissentStatus = bidder.bidderOpenInfo.dissentStatus; }}
    {{#if(decryptStatus ==1 && tenderDecryptStatus == 1){}}
    {{# successIndex ++; }}
    <li class="bidder_{{bidder.id}}">
        <div>{{successIndex}}</div>
        <div>
            <p>{{bidder.bidderName}}</p>
        </div>
        <div class="green-f">解密成功</div>

        <#--  招标解密状态  -->
        <#--        {{# if(decryptStatus == 0){ }}-->
        <#--        <div>未解密</div>-->
        <#--        {{# }else if(decryptStatus == 2){ }}-->
        <#--        <div class="red-f">解密失败</div>-->
        <#--        {{# }else if(decryptStatus == 1){ }}-->
        <#--        {{# if(tenderDecryptStatus == 0){ }}-->
        <#--        <div>未解密</div>-->
        <#--        {{# }else if(tenderDecryptStatus == 1){ }}-->
        <#--        <div class="green-f">解密成功</div>-->
        <#--        {{# }else if(tenderDecryptStatus == 2){ }}-->
        <#--        <div class="red-f">解密失败</div>-->
        <#--        {{# } }}-->
        <#--        {{# } }}-->

        <div class="green-f">
            <p>{{bidder.decryptTimeMinute}}分{{bidder.decryptTimeSecond}}秒</p>
        </div>

        {{# if("${bidSection.bidClassifyCode}" == "A10"){ }}
        {{# if(dissentStatus == "1"){ }}
        <div class="qsChinese">无异议</div>
        {{# }else if(dissentStatus == "2"){ }}
        <div class="red-f qsChinese">有异议</div>
        {{# }else { }}
        <div class="bidder qsChinese">-</div>
        {{# } }}
        {{# } }}

        {{# if(tenderRejection != 1){ }}
        <div>
            <span class="red-b" onclick="refuse('{{bidder.id}}')">标书拒绝</span>
            <span class="gray-b">撤销</span>
        </div>
        {{# }else if(tenderRejection == 1){ }}
        <div>
            <span class="blove-b" onclick="show_refuse_text('{{bidder.id}}')">拒绝理由</span>
            <span class="oragen-b" onclick="revoke_refuse('{{bidder.id}}')">撤销</span>
        </div>
        {{# } }}
    </li>
    {{# } }}
    {{# }); }}
    <li></li>
</script>

<#--未解密+解密失败 模板渲染-->
<script id="failBidderListTemplate" type="text/html">
    {{#  var failIndex = 0 }}
    {{#  layui.each(d, function(index, bidder){ }}
    {{#  var decryptStatus = bidder.bidderOpenInfo.decryptStatus; }}
    {{#  var tenderDecryptStatus = bidder.bidderOpenInfo.tenderDecryptStatus; }}
    {{#  var tenderRejection = bidder.bidderOpenInfo.tenderRejection; }}
    {{#  var dissentStatus = bidder.bidderOpenInfo.dissentStatus; }}
    {{#if(decryptStatus != 1 || tenderDecryptStatus != 1){}}
    {{# failIndex ++; }}
    <li class="bidder_{{bidder.id}}">
        <div>{{failIndex}}</div>
        <div>
            <p>{{bidder.bidderName}}</p>
        </div>
        {{# if(decryptStatus == 0){ }}
        <div>未解密</div>
        {{# }else { }}
        <div class="yes-tou-status decrypt-error">解密失败</div>
        {{# } }}

        <#--  招标解密状态  -->
        <#--        {{# if(decryptStatus == 0){ }}-->
        <#--        <div>未解密</div>-->
        <#--        {{# }else if(decryptStatus == 2){ }}-->
        <#--        <div class="red-f">解密失败</div>-->
        <#--        {{# }else if(decryptStatus == 1){ }}-->
        <#--        {{# if(tenderDecryptStatus == 0){ }}-->
        <#--        <div>未解密</div>-->
        <#--        {{# }else if(tenderDecryptStatus == 1){ }}-->
        <#--        <div class="green-f">解密成功</div>-->
        <#--        {{# }else if(tenderDecryptStatus == 2){ }}-->
        <#--        <div class="red-f">解密失败</div>-->
        <#--        {{# } }}-->
        <#--        {{# } }}-->

        {{# if(bidder.decryptStatus==0){ }}
        <div class="red-f">未解密</div>
        {{# }else if(decryptStatus == 2){ }}
        <div class="red-f">解密失败</div>
        {{# }else if(bidder.decryptStatus == 1 && decryptStatus != 2){ }}
        <div class="red-f">解密中</div>
        {{# }else if(tenderDecryptStatus == 2){ }}
        <div class="red-f">招标解密失败</div>
        {{# }else if(bidder.decryptStatus==2){ }}
        <div class="green-f">
            <p>{{bidder.decryptTimeMinute}}分{{bidder.decryptTimeSecond}}秒</p>
        </div>
        {{# } }}

        {{# if("${bidSection.bidClassifyCode}" == "A10"){ }}
        {{# if(dissentStatus == "1"){ }}
        <div class="qsChinese">无异议</div>
        {{# }else if(dissentStatus == "2"){ }}
        <div class="red-f qsChinese">有异议</div>
        {{# }else { }}
        <div class="bidder qsChinese">-</div>
        {{# } }}
        {{# } }}

        {{# if(tenderRejection != 1){ }}
        <div>
            <span class="red-b" onclick="refuse('{{bidder.id}}')">标书拒绝</span>
            <span class="gray-b">撤销</span>
        </div>
        {{# }else if(tenderRejection == 1){ }}
        <div>
            <span class="blove-b" onclick="show_refuse_text('{{bidder.id}}')">拒绝理由</span>
            <span class="oragen-b" onclick="revoke_refuse('{{bidder.id}}')">撤销</span>
        </div>
        {{# } }}
    </li>
    {{# } }}
    {{# }); }}
    <li></li>
</script>

<script type="text/javascript">
    var form;
    // 未解密的投标人个数
    var undecrypt_count = 0;
    // 心跳加载投标人
    var timerDecrypt;
    // 解密时间定时器
    var decryptTimeInterval;
    //质询状态
    var checkQuestionStatusCount = 0;

    //查询解密投标人轮询次数
    var listBiddersForDecryptCount = 0;

    var layer, laytpl;
    layui.use(['form', 'layer', 'element', 'laytpl', 'laypage'], function () {
        form = layui.form;
        layer = layui.layer;
        laytpl = layui.laytpl;
        //监听提交
        form.on('submit(*)', function (data) {
            // 阻止表单提交
            return false;
        });
        // 身份检查结束后，加载投标人
        if ('${lineStatus.bidderCheckStatus}' == 2) {
            listBiddersForDecrypt();
        }

        //检查质询状态 开启情况下更新投标人质询状态
        if ('${lineStatus.questionStatus}' == 1) {
            setInterval(checkQuestionStatus, 2000);
        }
    });

    $(function () {
        //解密结束计算总用时（秒）
        var totalTime = '${lineStatus.decryptionTime}';
        if (isNull(totalTime)) {
            totalTime = 0;
        }
        totalTime = parseInt(totalTime);
        //最后一次解密开始时间
        var lastDecryptStartTime = '${lastDecryptStartTime}';
        //系统当前时间
        var nowTime = new Date('${nowTime}'.replace(/-/g, "/"));
        var startTime = new Date(lastDecryptStartTime.replace(/-/g, "/"));
        var timeDiff = (nowTime.getTime() - startTime.getTime()) / 1000;
        // 当前解密总时间（页面加载时）
        var nowTotalTime = totalTime + parseInt(timeDiff + "");

        // 刷新页面后，如果正在解密中
        if ('${lineStatus.decryptionStatus}' == "1") {
            // 开启心跳解密
            timerDecrypt = setInterval(listBiddersForDecrypt, 1500);
            decryptTimeInterval = setInterval(function () {
                calDecryptTime(nowTotalTime);
                nowTotalTime++;
            }, 1000);
        } else {
            calDecryptTime(totalTime);
        }
    });

    /**
     * 计算总的解密时间
     */
    function calDecryptTime(decryptTime) {
        var minutes = Math.floor(decryptTime / 60);
        var seconds = decryptTime % 60;
        $(".totalTime").text("总用时:" + minutes + '分' + seconds + '秒');
    }

    /**
     * 请求数据
     */


    function listBiddersForDecrypt() {
        if (listBiddersForDecryptCount != 0) {
            return;
        }
        listBiddersForDecryptCount++;
        $.ajax({
            url: '${ctx}/staff/listBiddersForDecrypt',
            type: 'post',
            cache: false,
            async: true,
            data: {
                bidSectionId: ${bidSection.id}
            },
            success: function (result) {
                var data = result.data.bidders;
                if (!isNull(data) && result.code === "1") {
                    // 渲染解密成功投标人
                    var getSuccessTpl = successBidderListTemplate.innerHTML
                        , successView = document.getElementById('bid-open-success-list');
                    laytpl(getSuccessTpl).render(data, function (html) {
                        successView.innerHTML = html;
                    });
                    // 渲染未解密的投标人模版
                    var getfailTpl = failBidderListTemplate.innerHTML
                        , failView = document.getElementById('bid-open-fail-list');
                    laytpl(getfailTpl).render(data, function (html) {
                        failView.innerHTML = html;
                    });
                    // 显示投标人个数
                    $(".bidder_count").text(data.length);
                    //解密人数情况统计
                    decryptSituation(result);
                } else {
                    layer.msg(result.msg, {icon: 5});
                }
                listBiddersForDecryptCount--;
            },
            error: function (data) {
                console.error(data);
                listBiddersForDecryptCount--;
            },
        });
    }

    /**
     * 解密人数情况统计
     */
    function decryptSituation(result) {
        var data = result.data.bidders;
        // 已解密人数
        var succ_count = 0;
        // 排队解密的人数
        var queueCount = parseInt(result.data.queueCount);
        // 解密中的人数
        var decryptingCount = parseInt(result.data.decryptingCount);
        var wait_count = queueCount + decryptingCount;
        // 总的解密人数
        var count = data.length;
        // 遍历投标人
        for (var i = 0; i < count; i++) {
            var bidder = data[i];
            if (bidder.bidderOpenInfo.decryptStatus === 1 &&
                bidder.bidderOpenInfo.tenderDecryptStatus === 1) {
                succ_count++;
            }
        }
        // 未解密人数
        undecrypt_count = count - succ_count;
        $("#real_time_count").text("已解密:" + succ_count + "家，未解密:" +
            undecrypt_count + "家，等待:" + wait_count + "家");

    }

    /**
     * 切换解密状态
     */
    function switchDecryptStatus(ds) {
        var msg = '';
        if (ds === 1) {
            msg = "解密环节已开启";
        } else {
            window.clearInterval(timerDecrypt);
            window.clearInterval(decryptTimeInterval);
            msg = "解密环节已结束";
        }
        // 更新解密状态
        window.top.updateLineStatus({
            "bidSectionId": '${bidSection.id}',
            "decryptionStatus": ds,
            "msg": msg,
        }, msg, '${ctx}/staff/bidderFileDecryptPage', $("#fileDecLi"));
    }

    /**
     * 切换质询状态
     */
    function switchQuestionStatus(qs) {
        var msg = '';
        if (qs === 1) {
            msg = "质询环节已开启";
        } else {
            msg = "质询环节已结束";
        }
        // 更新质询状态
        window.top.updateLineStatus({
            "bidSectionId": '${bidSection.id}',
            "questionStatus": qs,
            "msg": msg
        }, msg, '${ctx}/staff/bidderFileDecryptPage', window.top.$("#fileDecLi"));
    }

    /**
     * 拒绝confirm弹窗
     */
    function refuse(bidderId) {
        var _index = window.top.layer.confirm('确定要拒绝该标书吗？', {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            window.top.layer.close(_index)
            open_dom(bidderId);

        }, function () {
        });
    }

    /**
     * 标书拒绝弹窗
     */
    function open_dom(bidderId) {
        window.top.layer.open({
            type: 2,
            offset: 'auto',
            title: ['标书拒绝', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            id: 'layerDemo',
            content: '${crx}/staff/bidRejectionPage?flag=0&bidSectionId=${bidSection.id}&bidderId=' + bidderId,
            area: ['600px', '360px'],
            btn: ['确认', '取消'],
            btnAlign: 'c',
            shade: 0.3,
            yes: function (index, layero) {
                var body = window.top.layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.submitReason(function () {
                    listBiddersForDecrypt();
                    window.top.layer.closeAll();
                });
            }
        });
    }

    /**
     * 修改当前投标人的标段信息
     * @param bidderId 投标人主键
     */
    function update(bidderId, reasonText) {
        doLoading();
        $.ajax({
            url: '${ctx}/staff/updateBidderOpenInfo',
            type: 'post',
            cache: false,
            data: {
                // 标段主键
                'bidSectionId': ${bidSection.id},
                // 投标人主键
                'bidderId': bidderId,
                // 标书拒绝标识 1
                'tenderRejection': 1,
                // 拒绝理由
                'tenderRejectionReason': reasonText
            },
            success: function (data) {
                loadComplete();
                if (data) {
                    listBiddersForDecrypt();
                    parent.layer.msg("拒绝成功!", {icon: 1});
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    /**
     * 展示标书拒绝理由
     */
    function show_refuse_text(bidderId) {
        // 显示拒绝理由
        window.top.layer.open({
            type: 2,
            offset: 'auto',
            title: ['标书拒绝', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            id: 'layerDemo',
            content: '${crx}/staff/bidRejectionPage?flag=1&bidSectionId=${bidSection.id}&bidderId=' + bidderId,
            area: ['600px', '360px'],
            btnAlign: 'c',
            shade: 0.3
        });
    }

    /**
     * 获取投标人信息
     * @param bidderId
     */
    function getBidderOpenInfo(bidderId) {
        doLoading();
        $.ajax({
            url: '${ctx}/staff/getBidderOpenInfo',
            type: 'post',
            cache: false,
            async: true,
            data: {
                'bidSectionId': ${bidSection.id},
                'bidderId': bidderId
            },
            success: function (data) {
                loadComplete();
                if (!isNull(data)) {
                    // 展示拒绝理由
                    $("#refuse-text2").val(data.tenderRejectionReason);
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    /**
     * 撤销拒绝
     */
    function revoke_refuse(bidderId) {
        var _index = window.top.layer.confirm('确定要撤销吗？', {
            icon: 3,
            btn: ['确定', '取消'] //按钮
        }, function () {
            window.top.layer.close(_index)
            revoke_status(bidderId);
        }, function () {
        });
    }

    /**
     * 执行撤销标书拒绝操作
     * @param bidderId
     */
    function revoke_status(bidderId) {
        doLoading();
        $.ajax({
            url: '${ctx}/staff/updateBidderOpenInfo',
            type: 'post',
            cache: false,
            async: true,
            data: {
                'bidSectionId': ${bidSection.id},
                'bidderId': bidderId,
                'tenderRejection': 0,
                'tenderRejectionReason': ''
            },
            success: function (data) {
                loadComplete();
                if (data) {
                    listBiddersForDecrypt();
                    parent.layer.msg("撤销成功!", {icon: 1});
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    /**
     * 执修改开标地点
     */
    function validData() {
        // 触发表单验证
        $("#submitForm").trigger("click");
        var open_place = $(".bidOpenPlace").val();
        if (open_place.trim().length <= 0) {
            return
        }
        // 保存开标地点
        doLoading();
        $.ajax({
            url: '${ctx}/staff/updateTenderDoc',
            type: 'post',
            cache: false,
            async: true,
            data: {
                bidSectionId:${bidSection.id},
                bidOpenPlace: open_place
            },
            success: function (data) {
                loadComplete();
                if (!isNull(data)) {
                    if (data) {
                        layer.msg("开标地点修改成功！", {icon: 1});
                    }
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    /**
     * 上传投标文件
     */
    function addBidderFile() {
        var allowType = "*.gef;*.GEF";
        var allowFileSize = "1024M";
        window.top.layer.open({
            type: 2,
            content: '${ctx!}/fdfs/uploadFilePage',
            title: '招标文件上传(*.gef)',
            shadeClose: true,
            area: ['600px', '540px'],
            btn: ['关闭'],
            success: function (layero, index) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.initUploadParam(allowType, allowFileSize);
                iframeWin.dropzoneInit(function (uploadFile) {
                    layer.msg("文件上传成功，正在解密")
                    window.top.layer.close(index);
                    // 执行解密环节
                    decryptBidderFile(uploadFile.id);
                });
            },
            btn1: function (index) {
                window.top.layer.close(index);
            }
        });
    }

    /**
     * 解密投标文件
     */
    function decryptBidderFile(fileId) {
        doLoading();
        $.ajax({
            url: '${ctx}/staff/bidderFileDecrypt',
            type: 'post',
            cache: false,
            async: true,
            data: {
                bidSectionId: ${bidSection.id},
                bidderFileId: fileId
            },
            success: function (data) {
                loadComplete();
                if (!isNull(data)) {
                    if (data.code === "200") {
                        layer.msg(data.msg, {icon: 1})
                    } else {
                        layer.msg(data.msg, {icon: 2})
                    }
                    listBiddersForDecrypt();
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    /**
     * 查询更新质询状态
     */
    function checkQuestionStatus() {
        if (checkQuestionStatusCount != 0) {
            return;
        }
        checkQuestionStatusCount++;
        $.ajax({
            url: "${ctx}/staff/listBiddersForDecrypt",
            type: "POST",
            cache: false,
            data: {
                bidSectionId: '${bidSection.id}'
            },
            success: function (result) {
                if (result) {
                    var data = result.data.bidders;
                    for (var i = 0; i < data.length; i++) {
                        var question = data[i].bidderOpenInfo.dissentStatus;
                        if (isNull(question)) {
                            continue;
                        }
                        var qsBox = $(".bidder_" + data[i].id).find(".qsChinese");
                        if (question == 1) {
                            question = "无异议";
                            qsBox.removeClass('red-f');
                        } else if (question == 2) {
                            question = "有异议";
                            qsBox.addClass('red-f');
                        } else {
                            question = "-";
                        }

                        if (question != qsBox.text().trim()) {
                            qsBox.text(question);
                        }
                    }
                }
                checkQuestionStatusCount--;
            },
            error: function (e) {
                console.error(e);
                checkQuestionStatusCount++;
            }
        });
    }


    /**
     * 解密成功和未解密按钮 切换
     */
    function toggleDecBidder(event, type) {
        $(event)
            .addClass("layui-btn-warm")
            .removeClass("layui-btn-primary")
            .siblings()
            .addClass("layui-btn-primary")
            .removeClass("layui-btn-warm");
        if (type === "1") {
            $("#bid-open-success-list").css("display", "block");
            $("#bid-open-fail-list").css("display", "none");
        } else {
            $("#bid-open-fail-list").css("display", "block");
            $("#bid-open-success-list").css("display", "none");
        }
    }

</script>
</body>
</html>