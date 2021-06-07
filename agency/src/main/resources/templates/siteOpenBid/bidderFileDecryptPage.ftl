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
    <link rel="stylesheet" href="${ctx}/css/siteDecrypt.css">
</head>
<body>
<div class="document">
    <div class="document-top">
        <h3>招标项目信息</h3>
        <form class="top-form" action="javascript:void(0)">
            <div class="check">
                <label for="">标段名称</label>
                <input type="text" value="${bidSection.bidSectionName}" disabled>
            </div>
            <div class="check">
                <label for="">标段类型</label>
                <input type="text" value="${bidSection.bidClassifyName}" disabled>
            </div>
            <div class="check">
                <label for="">标段编号</label>
                <input type="text" value="${bidSection.bidSectionCode}" disabled>
            </div>
            <#if bidSection.bidClassifyCode == 'A08' || bidSection.bidClassifyCode == 'A12'>
                <div class="check">
                    <label for=""><#if bidSection.bidClassifyCode =='A08'>招标控制价<#else>最高投标限价</#if></label>
                    <input type="text"
                           value="<#if tenderDoc.controlPrice?? && tenderDoc.controlPrice != ''>${((tenderDoc.controlPrice)?number)?string(",###.00")}元</#if>"
                           disabled>
                </div>
            </#if>
            <div class="long-check">
                <label for="">开标地点</label>
                <input type="text" placeholder="请输入地点" lay-verify="required" lay-reqText="请填写开标地点"
                       value="${tenderDoc.bidOpenPlace}" class="bidOpenPlace" onblur="validData()">
            </div>
            <button style="visibility: hidden" class="layui-btn" lay-submit="" lay-filter="formSub" id="submitForm">验证表单
            </button>
        </form>
    </div>
    <div class="document-center">
        <div class="center-head">
            <div class="head-btns" id="layerDemo">
                <span class="add" onclick="addBidderFile()">文件添加</span>
                <span class="add" onclick="addBidderByCa()">CA添加</span>
                <span class="add" onclick="addBidder()">手动添加</span>
                <span class="del" onclick="delBidders()">删除</span>
            </div>
            <div class="current">当前共有投标人:<span class="bidder_count">10家</span></div>
        </div>
        <div class="center-list layui-form">
            <ol>
                <li><input type="checkbox" name="" lay-skin="primary" lay-filter="check-all" title="序号"></li>
                <li class="long">投标人名称</li>
                <li>投标解密状态</li>
                <li class="long">上传投标文件</li>
                <li>检查</li>
                <li class="long">操作</li>
            </ol>
            <div class="list-table">
                <table cellpadding=0 cellspacing=0 id="bid-open-list">

                </table>
            </div>
        </div>
    </div>
</div>

</body>
<#--第一步：编写模版。你可以使用一个script标签存放模板，如：-->
<script id="biddingList" type="text/html">
    {{#  layui.each(d, function(index, bidder){ }}
    {{#  var decryptStatus = bidder.bidderOpenInfo.decryptStatus; }}
    {{#  var tenderDecryptStatus = bidder.bidderOpenInfo.tenderDecryptStatus; }}
    {{#  var tenderRejection = bidder.bidderOpenInfo.tenderRejection; }}
    <tr>
        <td><input type="checkbox" name="ids" lay-skin="primary" value="{{bidder.id}}">{{index+1}}</td>
        <td class="long">{{bidder.bidderName}}</td>

        {{# if(decryptStatus == 0){ }}
        <td class="unpass">未解密</td>
        {{# }else if(decryptStatus == 1){ }}
        <td class="pass" style="width: 10%">解密成功</td>
        {{# }else if(decryptStatus == 2){ }}
        <td class="unpass">解密失败</td>
        {{# } }}

        <td class="long">
            {{# if(decryptStatus == 1){ }}
            <span class="green-f">已上传</span>
            {{# }else{ }}
            {{# if(bidder.bidDocType == 0){ }}
            <span class="green-f green-s" onclick="uploadSGefFile(this, '{{bidder.id}}')">投标文件</span>
            <span class="green-f green-s" onclick="uploadGefFile(this, '{{bidder.id}}')">重传备用</span>
            {{# }else if(bidder.bidDocType == 1){ }}
            <span class="green-f green-s" onclick="uploadSGefFile(this, '{{bidder.id}}')">重传投标</span>
            <span class="green-f green-s" onclick="uploadGefFile(this, '{{bidder.id}}')">备用文件</span>
            {{# }else{ }}
            <span class="green-f green-s" onclick="uploadSGefFile(this, '{{bidder.id}}')">投标文件</span>
            <span class="green-f green-s" onclick="uploadGefFile(this, '{{bidder.id}}')">备用文件</span>
            {{# } }}
            {{# } }}
        </td>

        <td>
            {{# if(decryptStatus == 1){ }}
            <span class="green-f">已解密</span>
            {{# }else{ }}
            {{# if(bidder.bidDocType == 0){ }}
            <span class="green-b"
                  onclick="decryptBidderUploadFile('{{bidder.id}}', '{{bidder.bidDocId}}', this, 0)">校验</span>
            {{# }else if(bidder.bidDocType == 1){ }}
            <span class="green-b"
                  onclick="decryptBidderUploadFile('{{bidder.id}}', '{{bidder.bidDocId}}', this, 1)">解密</span>
            {{# }else{ }}
            <span>未上传</span>
            {{# } }}
            {{# } }}
        </td>
        <td class="long">
            {{# if(tenderRejection != 1){ }}
                <span class="red-b" onclick="refuse('{{bidder.id}}')">标书拒绝</span>
                <span class="gray-b">撤销</span>
            {{# }else if(tenderRejection == 1){ }}
                <span class="blove-b" onclick="show_refuse_text('{{bidder.id}}')">拒绝理由</span>
                <span class="oragen-b" onclick="revoke_refuse('{{bidder.id}}')">撤销</span>
            {{# } }}
        </td>
    </tr>

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

    {{# }); }}
    <li></li>
</script>
<script type="text/javascript">
    var form, layer, laytpl;
    layui.use(['form', 'layer', 'element', 'laytpl', 'laypage'], function () {
        form = layui.form;
        layer = layui.layer;
        laytpl = layui.laytpl;
        form.render();
        //监听提交
        form.on('submit(*)', function (data) {
            // 阻止表单提交
            return false;
        });
        // 监听实体类全选
        form.on("checkbox(check-all)", function (data) {
            if (data.elem.checked) {
                $(":checkbox[name=ids]").prop("checked", true);
                form.render("checkbox");
            } else {
                $(":checkbox[name=ids]").prop("checked", false);
                form.render("checkbox");
            }
        });

        listBiddersForDecrypt();
    });

    /**
     * 请求数据
     */
    function listBiddersForDecrypt() {
        $.ajax({
            url: '${ctx}/siteOpenBid/listBiddersForDecrypt',
            type: 'post',
            cache: false,
            async: true,
            data: {
                bidSectionId: '${bidSection.id}'
            },
            success: function (result) {
                console.log(result)
                var data = result.data.bidders;
                if (!isNull(data) && result.code === "1") {
                    // 渲染模版
                    var getTpl = biddingList.innerHTML
                        , view = document.getElementById('bid-open-list');
                    laytpl(getTpl).render(data, function (html) {
                        view.innerHTML = html;
                    });
                    form.render();
                    // 显示投标人个数
                    $(".bidder_count").text(data.length);
                } else {
                    layer.msg(result.msg, {icon: 5});
                }
            },
            error: function (data) {
                console.error(data);
            },
        });
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
            content: '${crx}/siteOpenBid/bidRejectionPage?flag=0&bidSectionId=${bidSection.id}&bidderId=' + bidderId,
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
            async: false,
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
            content: '${crx}/siteOpenBid/bidRejectionPage?flag=1&bidSectionId=${bidSection.id}&bidderId=' + bidderId,
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
            url: '${ctx}/siteOpenBid/getBidderOpenInfo',
            type: 'post',
            cache: false,
            async: false,
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
            async: false,
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
            window.top.layerWarning('请输入开标地点！')
            return;
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
                        window.top.layer.msg("开标地点修改成功！", {icon: 1});
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
            title: '文件新增(*.gef)',
            shadeClose: false,
            area: ['600px', '540px'],
            btn: ['关闭'],
            success: function (layero, index) {
                var body = window.top.layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.initUploadParam(allowType, allowFileSize);
                iframeWin.dropzoneInit("1", function (uploadFile) {
                    window.top.layer.close(index);
                    window.top.layer.msg("解密中，请等待...", {icon: 16, time: 0, shade: [0.3, '#393D49']})
                    setTimeout(function () {
                        // 执行解密环节
                        decryptBidderFile(uploadFile.id);
                    }, 200)
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
        $.ajax({
            url: '${ctx}/siteOpenBid/bidderFileDecrypt',
            type: 'post',
            cache: false,
            data: {
                bidSectionId: '${bidSection.id}',
                bidderFileId: fileId
            },
            async: true,
            success: function (data) {
                loadComplete();
                if (!isNull(data)) {
                    window.top.layer.alert(data.msg, {
                        icon: data.code, end: function () {
                            listBiddersForDecrypt();
                        }
                    })
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    /**
     * 通过ca新增投标人
     */
    function addBidderByCa() {
        window.top.layer.open({
            type: 2,
            title: "数字证书选择",
            offset: 'c',
            content: "/common/data/selectAllCaInfoPage",
            area: ['400px', '300px'],
            btn: ['确定', '关闭'],
            btn1: function (index, layero) {
                //信息内容，参考于 BJCA_CertInfo
                window.top.layer.close(index);
                var body = window.top.layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.getBidderName(function (bidderName) {
                    var data = {};
                    data.bidderName = bidderName;
                    data.bidSectionId = '${bidSection.id}';
                    saveBidderInfo(data);
                })
            },
            btn2: function (index) {
                // 点击取消的回调函数
                layer.close(index);
            }
        });
    }

    /**
     * 手动新增投标人
     */
    function addBidder() {
        window.top.layer.prompt({
            title: '请输入投标人名称',
            formType: 2 ,
            btnAlign: 'c',
            maxlength: 20,// 字符限制20字
        }, function (text, index) {
            var bidderName = text.trim();
            if (isNull(bidderName)) {
                window.top.layer.msg("请输入投标人名称");
                return;
            }
            window.top.layer.close(index);
            var data = {};
            data.bidderName = bidderName;
            data.bidSectionId = '${bidSection.id}';
            saveBidderInfo(data);
        });
    }

    /**
     * 报错投标人信息
     * @param data
     */
    function saveBidderInfo(data) {
        var layerloadindex = window.top.layer.msg('投标人新增中...', {
            icon: 16,
            shade: [0.3, '#393D49'],
            time: 0
        });

        $.ajax({
            url: "${ctx}/siteOpenBid/saveBidderInfo",
            type: "POST",
            cache: false,
            data: data,
            success: function (data) {
                window.top.layer.close(layerloadindex);
                window.top.layer.msg(data.msg, {
                    icon: data.code, time: 2000, end: function () {
                        window.top.location.reload();
                    }
                })
            }
        })
    }

    /**
     * 删除投标人
     */
    function delBidders() {
        if (checkCkBoxStatus('ids')) {
            window.top.layer.confirm('确定要删除所选投标人吗?', {icon: 3, title: '提示'}, function () {
                window.top.layer.prompt({
                    formType: 2,
                    title: '请输入删除原因',
                    area: ['400px', '200px'] //自定义文本域宽高
                }, function (value, index, elem) {
                    var text = value.trim();
                    if (isNull(text)) {
                        window.top.layer.msg("请输入删除原因");
                        return;
                    }
                    if (text.length > 255) {
                        window.top.layer.msg("输入原因过长");
                        return;
                    }
                    var ids = "";
                    var $checkedIds = $("#bid-open-list input:checkbox[name='ids']");
                    $checkedIds.each(function (index) {
                        if ($(this).is(':checked')) {
                            if (index === $checkedIds.length - 1) {
                                ids += $(this).val();
                            } else {
                                ids += $(this).val() + ",";
                            }
                        }
                    });
                    window.top.layer.close(index);
                    var layerloadindex = window.top.layer.msg('投标人删除中...', {
                        icon: 16,
                        shade: [0.3, '#393D49'],
                        time: 0
                    });
                    $.ajax({
                        url: "${ctx}/siteOpenBid/delBidders",
                        type: "POST",
                        cache: false,
                        data: {
                            ids: ids,
                            reason: text
                        },
                        success: function (data) {
                            window.top.layer.close(layerloadindex);
                            window.top.layer.msg(data.msg, {
                                icon: data.code, time: 2000, end: function () {
                                    window.top.location.reload();
                                }
                            })
                        },
                        error: function () {
                            window.top.layer.close(layerloadindex);
                        }
                    })
                });
            }, function (index) {
                window.top.layer.msg("已取消!");
                window.top.layer.close(index);
            });
        } else {
            window.top.layer.msg("请先选择一条记录!", {icon: 2});
        }
    }
</script>
<script>
    /**
     * 上传gef文件
     */
    function uploadGefFile(obj, bidderId) {
        addBidderDoc(0, obj, bidderId)
    }

    /**
     * 上传sgef文件
     */
    function uploadSGefFile(obj, bidderId) {
        addBidderDoc(1, obj, bidderId)
    }

    /**
     * 上传投标文件
     */
    function addBidderDoc(fileType, obj, bidderId) {
        var allowType = "*.gef;*.GEF";
        if (fileType === 1) {
            allowType = "*.sgef;*.SGEF";
        }

        var allowFileSize = "1024M";
        window.top.layer.open({
            type: 2,
            content: '${ctx!}/fdfs/uploadFilePage',
            title: '投标文件上传(' + allowType + ')',
            shadeClose: false,
            area: ['600px', '540px'],
            btn: ['关闭'],
            success: function (layero, index) {
                var body = window.top.layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.initUploadParam(allowType, allowFileSize);
                iframeWin.dropzoneInit("1", function (uploadFile) {
                    window.top.layer.close(index);
                    window.top.layer.msg('文件校验中...', {
                        icon: 16,
                        shade: [0.3, '#393D49'],
                        time: 0
                    });
                    saveUploadBidderFile(fileType, obj, bidderId, uploadFile.id);
                });
            },
            btn1: function (index) {
                window.top.layer.close(index);
            }
        });
    }

    /**
     * 保存上传文件，与投标人建立关联
     * @param fileType 文件类型 1：sgef  0:gef
     * @param obj 对象
     * @param bidderId 投标人id
     * @param bidDocId 投标文件上传的附件id
     */
    function saveUploadBidderFile(fileType, obj, bidderId, bidDocId) {
        if (!bidDocId) {
            return;
        }

        $.ajax({
            url: "${ctx}/siteOpenBid/updateBidder",
            type: "POST",
            data: {
                "bidDocId": bidDocId,
                "bidDocType": fileType,
                "id": bidderId
            },
            success: function (data) {
                window.top.layer.closeAll();
                if (!data) {
                    window.top.layer.msg("文件上传失败!", {icon: 2});
                }
                if (fileType === 0) {
                    $(obj).text("重传备用")
                    $(obj).parent().next().empty()
                    $(obj).parent().next().append("<span class='blove-b' onclick='decryptBidderUploadFile(" + bidderId + "," + bidDocId + ", this, 0)'>校验</span>");
                } else {
                    $(obj).parent().next().empty()
                    $(obj).parent().next().append("<span class='blove-b' onclick='decryptBidderUploadFile(" + bidderId + "," + bidDocId + " ,this, 1)'>解密</span>");
                    $(obj).text("重传投标")
                }
            }
        })
    }

    /**
     * 解密或校验投标文件
     * @param bidderId  投标人id
     * @param bidDocId  附件id
     * @param obj 对象
     * @param fileType 文件类型
     */
    function decryptBidderUploadFile(bidderId, bidDocId, obj, fileType) {
        if (fileType === 1) {
            window.top.layer.open({
                type: 2,
                title: "请选择CA",
                offset: 'c',
                content: "/common/data/selectCaInfoPage",
                area: ['400px', '300px'],
                btn: ['确定', '关闭'],
                btn1: function (index, layero) {
                    //信息内容，参考于 BJCA_CertInfo
                    var body = window.top.layer.getChildFrame('body', index);
                    var iframeWin = window.top[layero.find('iframe')[0]['name']];
                    iframeWin.loginCa(function (certInfo, QSKJInfo, certStatus) {
                        if (!isNull(certStatus) && certStatus) {
                            window.top.layer.msg('文件解密中...', {
                                icon: 16,
                                shade: [0.3, '#393D49'],
                                time: 0
                            });
                            getDecoderCipherInfo(bidDocId, bidderId, iframeWin, certInfo)
                        } else {
                            window.top.layer.close(index);
                            window.top.layer.msg("非正确的加密锁，请插入正确的加密锁进行解密！", {icon: 5, time: 5000});
                        }
                    });
                },
                btn2: function (index) {
                    // 点击取消的回调函数
                    window.top.layer.close(index);
                }
            });
        } else if (fileType === 0) {
            window.top.layer.confirm("确认校验文件吗？", {icon: 3, title: '操作提示'}, function (index) {
                window.top.layer.msg('文件校验中...', {
                    icon: 16,
                    shade: [0.3, '#393D49'],
                    time: 0
                });
                siteDecryptGef(bidDocId, bidderId)
            });
        }
    }

    /**
     * 投标文件解密
     * @param fileId 文件id
     * @param bidderId 投标人id
     */
    function siteDecryptGef(fileId, bidderId) {
        $.ajax({
            url: "${ctx}/siteOpenBid/siteDecryptGef",
            type: "POST",
            cache: false,
            data: {
                fileId: fileId,
                bidderId: bidderId,
                bidSectionId: "${bidSection.id}",
            },
            success: function (data) {
                window.top.layer.closeAll();
                if (!data.nameConsistentStatus && !data.decryptStatus) {
                    var fileBidderName = data.fileBidderName;
                    var dataBaseBidderName = data.dataBaseBidderName;
                    var updateBidder = data.updateBidder;
                    var updateBidderOpenInfo = data.updateBidderOpenInfo;
                    var updateBidderFileInfo = data.updateBidderFileInfo;
                    window.top.layer.open({
                        type: 2,
                        content: '${ctx!}/siteOpenBid/changeBidderNamePage?fileBidderName=' + fileBidderName + '&dataBaseBidderName=' + dataBaseBidderName,
                        title: '信息提示',
                        shadeClose: false,
                        area: ['670px', '540px'],
                        success: function (layero, index) {
                            var body = layer.getChildFrame('body', index);
                            var iframeWin = window.top[layero.find('iframe')[0]['name']];
                            iframeWin.initSucc(updateBidder,
                                updateBidderOpenInfo,
                                updateBidderFileInfo,
                                function () {
                                    window.top.layer.close(index);
                                    window.top.layer.msg("解密成功", {
                                        icon: 1, time: 3000, end: function () {
                                            listBiddersForDecrypt();
                                        }
                                    });
                            });
                        }
                    });
                } else {
                    if (data.decryptStatus) {
                        window.top.layer.msg(data.decryptMsg, {
                            icon: 1, time: 3000, end: function () {
                                listBiddersForDecrypt();
                            }
                        });
                    } else {
                        window.top.layer.msg(data.decryptMsg, {
                            icon: 5, time: 3000, end: function () {
                                listBiddersForDecrypt();
                            }
                        });
                    }
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
                window.top.layer.msg("校验失败", {icon: 5});
            }
        });
    }

    /**
     * 投标文件解密
     * @param fileId 文件id
     * @param bidderId 投标人id
     * @param iframeWin 弹窗
     * @param certInfo 锁信息
     */
    function getDecoderCipherInfo(fileId, bidderId, iframeWin, certInfo) {
        $.ajax({
            url: "${ctx}/siteOpenBid/getDecoderCipherInfo",
            type: "POST",
            cache: false,
            data: {
                fileId: fileId
            },
            success: function (data) {
                if (data.code != "1") {
                    window.top.layer.closeAll();
                    window.top.layer.msg("解密失败", {
                        icon: 5, time: 3000, end: function () {
                            listBiddersForDecrypt();
                        }
                    });
                } else {
                    var decoderCipherInfo = data.data;
                    if (isNull(decoderCipherInfo)) {
                        window.top.layer.closeAll();
                        window.top.layer.msg("解密失败", {
                            icon: 5, time: 3000, end: function () {
                                listBiddersForDecrypt();
                            }
                        });
                    } else {
                        var fileType = decoderCipherInfo.fileType;
                        var cipher = decoderCipherInfo.cipher;
                        if (fileType == "0") {
                            iframeWin.SOF_DecryptData(certInfo.CERT_NO_INDEX, cipher, function (returnInfo) {
                                var privateKey = returnInfo.retVal;
                                if (!isNull(privateKey)) {
                                    siteDecryptSgef(fileId, bidderId, privateKey, "0")
                                } else {
                                    window.top.layer.closeAll();
                                    window.top.layer.msg("非正确的加密锁，请插入正确的加密锁进行解密！", {icon: 5, time: 5000});

                                }
                            })
                        } else if (fileType == "1") {
                            siteDecryptSgef(fileId, bidderId, "", "1")
                        } else {
                            window.top.layer.closeAll();
                        }
                    }
                }
            },
            error: function (data) {
                console.error(data);
                window.top.layer.msg("解密失败", {
                    icon: 5, time: 3000, end: function () {
                        listBiddersForDecrypt();
                    }
                });
            }
        });
    }


    /**
     * 投标文件解密
     * @param fileId 文件id
     * @param bidderId 投标人id
     * @param privateKey 私钥
     * @param isOtherCa 是否是互认加密
     */
    function siteDecryptSgef(fileId, bidderId, privateKey, isOtherCa) {
        $.ajax({
            url: "${ctx}/siteOpenBid/siteDecryptSgef",
            type: "POST",
            cache: false,
            data: {
                fileId: fileId,
                bidderId: bidderId,
                bidSectionId: "${bidSection.id}",
                isOtherCa: isOtherCa,
                privateKey: privateKey
            },
            success: function (data) {
                window.top.layer.closeAll();
                if (!data.decryptStatus && !data.nameConsistentStatus) {
                    var fileBidderName = data.fileBidderName;
                    var dataBaseBidderName = data.dataBaseBidderName;
                    var updateBidder = data.updateBidder;
                    var updateBidderOpenInfo = data.updateBidderOpenInfo;
                    var updateBidderFileInfo = data.updateBidderFileInfo;
                    window.top.layer.open({
                        type: 2,
                        content: '${ctx!}/siteOpenBid/changeBidderNamePage?fileBidderName=' + fileBidderName + '&dataBaseBidderName=' + dataBaseBidderName,
                        title: '信息提示',
                        shadeClose: false,
                        data: {
                            fileBidderName: fileBidderName,
                            dataBaseBidderName: dataBaseBidderName
                        },
                        area: ['670px', '540px'],
                        success: function (layero, index) {
                            var body = layer.getChildFrame('body', index);
                            var iframeWin = window.top[layero.find('iframe')[0]['name']];
                            iframeWin.initSucc(updateBidder,
                                updateBidderOpenInfo,
                                updateBidderFileInfo,
                                function () {
                                    window.top.layer.close(index);
                                    window.top.layer.msg("解密成功", {
                                        icon: 1, time: 3000, end: function () {
                                            listBiddersForDecrypt();
                                        }
                                    });
                                });
                        },
                        btn1: function (index) {
                            window.top.layer.close(index);
                        }
                    });
                } else {
                    if (data.decryptStatus) {
                        window.top.layer.msg(data.decryptMsg, {
                            icon: 1, time: 3000, end: function () {
                                listBiddersForDecrypt();
                            }
                        });
                    } else {
                        window.top.layer.msg(data.decryptMsg, {
                            icon: 5, time: 3000, end: function () {
                                listBiddersForDecrypt();
                            }
                        });
                    }
                }
            },
            error: function (data) {
                window.top.layer.closeAll();
                console.error(data);
                window.top.layer.msg("解密失败", {
                    icon: 5, time: 3000, end: function () {
                        listBiddersForDecrypt();
                    }
                });
            }
        });
    }
</script>
</html>