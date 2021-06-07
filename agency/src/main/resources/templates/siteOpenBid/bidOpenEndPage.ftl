<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<link rel="stylesheet" href="${ctx}/css/timmeOver.css">
<link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
<script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
<script src="${ctx}/js/jquery-3.4.1.min.js"></script>
<script src="${ctx}/js/common.js"></script>
<style>
    .time-div {
        width: 80%;
        display: flex;
        justify-content: space-around;
        margin: 51px auto 0;
        line-height: 40px;
        font-weight: 900;
    }

    #bidOpenEndTime {
        width: 75%;
        height: 40px;
        background: rgba(255, 255, 255, 1);
        opacity: 1;
        float: right;
    }

    .cont {
        width: 50%;
        height: 40px;
        margin: 13% auto;
        line-height: 40px;
        font-size: 16px;
    }
</style>
<div class="lit-cont">
    <div class="lit-name">标段名称<span>${bidSection.bidSectionName }</span></div>
    <div class="lit-num">标段编号<span>${bidSection.bidSectionCode }</span></div>
    <#if bidSection.bidOpenStatus != 2>
        <div class="over-btn" onclick="endBidOpen('${bidSection.id}')">结束开标</div>
    </#if>
</div>
<div class="hidden-div" style="display: none;">
    <div class="tan">
        <h3>
            项目还未完成，确定结束开标吗？
        </h3>
    </div>
</div>
<script>
    /**
     * 结束开标
     * @param id 标段id
     */
    function endBidOpen(id) {
        if ('${bidSection.paperEval}' == '1') {
            checkFinishBidOpen(id, "");
        } else {
            window.top.layer.open({
                type: 2,
                title: "请选择监管锁",
                offset: 'c',
                content: "/common/data/selectCaInfoPage",
                area: ['400px', '300px'],
                btn: ['确定', '关闭'],
                btn1: function (index, layero) {
                    //信息内容，参考于 BJCA_CertInfo
                    var body = window.top.layer.getChildFrame('body', index);
                    var iframeWin = window.top[layero.find('iframe')[0]['name']];
                    iframeWin.loginCa(function (cert_info, qskj_info) {
                        if (isNull(cert_info.CERT_SERIAL_NUMBER)) {
                            return;
                        }
                        var certNo = cert_info.CERT_NO_INDEX;
                        $.ajax({
                            url: "${ctx}/login/getGovUser",
                            type: "POST",
                            cache: false,
                            data: {
                                keyNo: certNo
                            },
                            success: function (data) {
                                window.top.layer.close(index);
                                if (isNull(data)) {
                                    window.top.layer.alert("该锁未绑定监管权限，不能结束开标！")
                                } else {
                                    var userId = data.id;
                                    checkFinishBidOpen(id, userId);
                                }

                            },
                            error: function (e) {
                                layer.close(index);
                                window.top.layer.msg("开标结束失败");
                            }
                        });
                    });
                },
                btn2: function (index) {
                    // 点击取消的回调函数
                    layer.close(index);
                },
                end: function () {

                }
            });
        }
    }

    function checkFinishBidOpen(id, userId) {
        parent.layer.open({
            type: 2,
            title: ['开始检查', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
            content: "${ctx}/siteOpenBid/checkFinishBidOpenPage?id=" + id,
            area: ['800px', '550px'],
            btnAlign: 'c',
            shade: 0.3,
            btn: ['立即结束', '取消'],
            btn1: function (index, layero) {
                var body = parent.layer.getChildFrame('body', index);
                var iframeWin = parent.window[layero.find('iframe')[0]['name']];
                iframeWin.checkProcessStatus(function (status, problemNum) {
                    if (status) {
                        if (problemNum !== 0) {
                            window.top.layer.open({
                                type: 2,
                                title: ['结束检查', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
                                content: "${ctx}/siteOpenBid/endSure",
                                btn: ['确认', '取消'],
                                area: ['600px', '300px'],
                                btnAlign: 'c',
                                shade: 0.01,
                                btn1: function () {
                                    // 确认的回调函数
                                    window.top.layer.closeAll();
                                    finishBidOpen(id, userId);
                                },
                                btn2: function (index) {
                                    // 取消的回调函数
                                    window.top.layer.msg("已取消!");
                                    layer.close(index);
                                }
                            });
                        } else {
                            finishBidOpen(id, userId);
                        }
                    } else {
                        parent.layer.alert("检查尚未结束，请等待...");
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
    /*/!**
     * 结束开标
     * @param id 标段id
     *!/
    function endBidOpen(id) {
        window.top.layer.open({
            type: 2,
            title: "请选择监管锁",
            offset: 'c',
            content: "/common/data/selectCaInfoPage",
            area: ['400px', '300px'],
            btn: ['确定', '关闭'],
            btn1: function (index, layero) {
                //信息内容，参考于 BJCA_CertInfo
                var body = window.top.layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.loginCa(function (cert_info, qskj_info) {
                    if (isNull(cert_info.CERT_SERIAL_NUMBER)) {
                        return;
                    }
                    var certNo = cert_info.CERT_NO_INDEX;
                    $.ajax({
                        url: "${ctx}/login/getGovUser",
                        type: "POST",
                        cache: false,
                        data: {
                            keyNo: certNo
                        },
                        success: function (data) {
                            window.top.layer.close(index);
                            if (isNull(data)) {
                                window.top.layer.alert("该锁未绑定监管权限，不能结束开标！")
                            } else {
                                var userId = data.id;
                                parent.layer.open({
                                    type: 2,
                                    title: ['开始检查', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
                                    content: "${ctx}/siteOpenBid/checkFinishBidOpenPage?id=" + id,
                                    area: ['800px', '550px'],
                                    btnAlign: 'c',
                                    shade: 0.3,
                                    btn: ['立即结束', '取消'],
                                    btn1: function (index, layero) {
                                        var body = parent.layer.getChildFrame('body', index);
                                        var iframeWin = parent.window[layero.find('iframe')[0]['name']];
                                        iframeWin.checkProcessStatus(function (status, problemNum) {
                                            if (status) {
                                                if (problemNum !== 0) {
                                                    window.top.layer.open({
                                                        type: 2,
                                                        title: ['结束检查', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
                                                        content: "${ctx}/siteOpenBid/endSure",
                                                        btn: ['确认', '取消'],
                                                        area: ['600px', '300px'],
                                                        btnAlign: 'c',
                                                        shade: 0.01,
                                                        btn1: function () {
                                                            // 确认的回调函数
                                                            window.top.layer.closeAll();
                                                            finishBidOpen(id, userId);
                                                        },
                                                        btn2: function (index) {
                                                            // 取消的回调函数
                                                            window.top.layer.msg("已取消!");
                                                            layer.close(index);
                                                        }
                                                    });
                                                } else {
                                                    finishBidOpen(id, userId);
                                                }
                                            } else {
                                                parent.layer.alert("检查尚未结束，请等待...");
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

                        },
                        error: function (e) {
                            layer.close(index);
                            window.top.layer.msg("开标结束失败");
                        }
                    });
                });
            },
            btn2: function (index) {
                // 点击取消的回调函数
                layer.close(index);
            },
            end: function () {

            }
        });

    }*/

    /**
     * 完成开标，更新数据库（此处加入可以加入主管部门锁权限）
     * @param id 标段id
     * @param userId 监管id
     */
    function finishBidOpen(id, userId) {
        doLoading();
        $.ajax({
            url: '${ctx}/staff/bidSection/endBidOpen',
            type: 'post',
            cache: false,
            async: false,
            data: {
                "id": id,
                "userId": userId
            },
            success: function (data) {
                loadComplete();
                if (data) {
                    parent.layer.confirm('开标已结束！', {
                        icon: 3,
                        shade: 0.01,
                        title: '开标结束',
                        btn: ['确定']
                    }, function (index) {
                        // 确认的回调函数
                        layer.close(index);
                        parent.window.location.reload();
                    });
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

</script>

