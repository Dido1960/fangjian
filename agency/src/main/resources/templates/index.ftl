<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <title>项目管理界面</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/plugin/bootstrap-3.3.7-dist/css/bootstrap.min.css">
    <#--<script src="${ctx}/js/audioplayer.js"></script>-->
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" href="${ctx}/css/index.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <script src="${ctx}/plugin/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/webService.js"></script>
    <style>
        .postion-out {
            top: 9999px !important;
            left: 9999px !important;
            z-index: -88 !important;
            opacity: 0;
        }

        .liveTan h3 {
            min-width: 190px;
            height: 24px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 900;
            line-height: 24px;
            color: rgba(34, 49, 101, 1);
            opacity: 1;
            margin: 91px auto 20px;
            text-align: center;
        }

        .liveTan p {
            min-width: 226px;
            height: 24px;
            font-size: 14px;
            font-family: Microsoft YaHei;
            font-weight: 900;
            line-height: 24px;
            color: rgba(34, 49, 101, 1);
            opacity: 1;
            margin: 0 auto;
            text-align: center;
        }
    </style>
    <#--第一步：编写模版。你可以使用一个script标签存放模板，如：-->
    <script id="biddingList" type="text/html">
        {{# var startNum= ($("#curPage").val()-1)*($("#pageSize").val())}}
        {{#  layui.each(d, function(index, bid){ }}
        <li>
            <div class="small">{{=startNum+index+1}}</div>
            <div>
                <p>{{bid.bidSectionName}}</p>
            </div>
            <div>
                <p>{{# if(!isNull(bid.bidSectionCode)){ }}
                    {{bid.bidSectionCode}}
                    {{# } }}</p>
            </div>
            <div>
                {{# if(!isNull(bid.bidOpenTime)){ }}
                {{bid.bidOpenTime}}
                {{# } }}
            </div>
            <#-- 0 未开标 1开标中 2开标结束-->
            {{# if(bid.bidOpenStatus==0){ }}
            <div class="small oragen-f">{{bid.bidOpenStatusName}}</div>
            {{# }else if(bid.bidOpenStatus==1){ }}
            <div class="small green-f">{{bid.bidOpenStatusName}}</div>
            {{# }else if(bid.bidOpenStatus==2){ }}
            <div class="small red-f">{{bid.bidOpenStatusName}}</div>
            {{# } }}
            <div>
                <span class="blove-f" onmouseover="showMore(this)" onmouseout="hideMore(this)">更多<img src="${ctx}/img/down.png" alt="">
                    <ol style="display: none;">
                        {{# if(bid.bidOpenStatus==0){ }}
                            <li onclick="modifyProject('{{=bid.id}}')">修改项目</li>
                            {{# if(bid.bidOpenOnline == 1){ }}
                                <li onclick="signSet('{{=bid.id}}')">签到设置</li>
                            {{# } }}
                        {{# } }}

                        {{# if(bid.bidOpenStatus<2){ }}
                            <li onclick="uploadClarify('{{=bid.id}}','{{=bid.regId}}')">澄清上传</li>
                            {{# if(bid.remoteEvaluation == 1){ }}
                                <li onclick="selectChangeSite('{{=bid.id}}','{{=bid.regId}}')">场地选择</li>
                            {{# } }}
                        {{# } }}

                        {{# if(bid.bidOpenStatus==2 && bid.bidOpenOnline == 1 ){ }}
                            <li onclick="resumptionMeeting('{{=bid.id }}')">开始复会</li>
                        {{# } }}

                        {{# if(bid.evalStatus ==0 && bid.bidClassifyCode != 'A10'){ }}
                            <li onclick="preRelated('{{=bid.id}}')">预审关联</li>
                        {{# } }}

                        {{# if(bid.bidOpenOnline == 1){ }}
                            <li onclick="liveInfo('{{=bid.id }}', '{{=bid.liveRoom }}')">直播信息</li>
                            {{# if(bid.bidOpenStatus > 0){ }}
                                <li onclick="msgExport('{{=bid.id }}')">消息导出</li>
                            {{# } }}
                        {{# } }}
                    </ol>
                </span>
                {{# if(bid.bidOpenStatus==2){ }}
                    {{# if(!isNull(bid.paperEval) && bid.paperEval == '1'){ }}
                        <span class="green-b" onclick="downFiles('{{=bid.id }}')">文件下载</span>
                    {{# }else{ }}
                        {{# if(bid.cancelStatus==1 || bid.evalStatus==2){ }}
                           {{# if(bid.startDownLoad==1){ }}
                            <span class="green-b" onclick="downFiles('{{=bid.id }}')">文件下载</span>
                           {{# } }}
                        {{# } }}
                    {{# } }}
                    {{# if(bid.resumeStatus!=2){ }}
                        <#--                        <span class="blove-b" onclick="resumptionMeeting('{{=bid.id }}')">进入复会</span>-->
                        <#--                        <span class="blove-b" onclick="joinBidOpen('{{=bid.id}}')">进入开标</span>-->
                    {{# } }}
                {{# }else{ }}
                    <span class="blove-b" onclick="joinBidOpen('{{=bid.id}}')">进入开标</span>
                {{# } }}
            </div>
        </li>
        {{# }); }}

        {{# if(isNull(d)||d.length==0){}}
        <li style="text-align: center;line-height: 96px;font-size: 16px;font-weight: 500">
            暂无数据
        </li>
        {{# }}}
    </script>

</head>
<body>
<#assign hideProjectBtn=1 />
<#include "${ctx}/common/baseTitile.ftl"/>
<section>
    <div class="content">
        <div class="kong"></div>
        <#-- 记录招标项目类型-->
        <ul class="depend project-type">
            <li data-classify-code="" class="blue">全部</li>
            <li data-classify-code="A10">资格预审</li>
            <li data-classify-code="A08">施工</li>
            <li data-classify-code="A05">监理</li>
            <li data-classify-code="A03">勘察</li>
            <li data-classify-code="A04">设计</li>
            <li data-classify-code="A11" style="width: 130px;">电梯采购与安装</li>
            <li data-classify-code="A12">工程总承包</li>
        </ul>
        <div class="head">
            <div class="add-btn" onclick="addProject()">添加项目</div>
            <form action="javascript:void(0)">
                <div class="seach layui-form">
                    <select name="status" lay-filter="status">
                        <option value="">全部状态</option>
                        <option value="0">未开始</option>
                        <option value="1">正在开标</option>
                        <option value="2">开标结束</option>
                    </select>
                    <input type="text" class="seach-text" id="search-value" placeholder="搜索关键词">
                    <img src="${ctx}/img/seach.png" alt="" onclick="page_search()">
                </div>
            </form>
        </div>
        <ol class="kind">
            <li class="small">序号</li>
            <li>标段名称</li>
            <li>标段编号</li>
            <li>开标时间</li>
            <li class="small">开标状态</li>
            <li>操作</li>
        </ol>
        <ul class="document" id="show-bidding-project" style=" "></ul>
        <div class="foot" id="page-temp"></div>
    </div>
    <#--设置自定义分页-->
    <!--保存总记录数-->
    <input type="hidden" id="count">
    <input id="curPage" type="hidden" value="1"/>
    <input id="pageSize" type="hidden" value="4"/>
    <div class="liveTan" style="display: none;">
        <h3>服务器：<span class="service-ip">rtmp://push.lzggzyjy.cn/gslzggzyjy </span></h3>
        <p>串流密钥：<span class="stream-key"> </span></p>
    </div>
</section>
<a style="position: fixed;border-bottom:0px; right: 0px">电话：18280320686</a>
</body>
<script>
    var bidOpenStatus = "";
    var bidClassifyCode = "";
    $(function () {
        $(".depend li").bind('click', function () {
            bidClassifyCode = $(this).data("classify-code");
            $(this).addClass("blue").siblings().removeClass("blue");
            // 重新渲染模板
            page_search();
        });
        chooseUserProject();
    });

    /**
     * 加载Layui组件
     */
    var form, layer, laytpl, laypage;
    layui.use(['form', 'layer', 'laytpl', 'laypage'], function () {
        layer = layui.layer;
        laypage = layui.laypage;
        laytpl = layui.laytpl;
        form = layui.form;
        form.on("select(status)", function (data) {
            bidOpenStatus = data.value;
            page_search();
        });
        page_search();
        form.render();
    });

    /**
     * 分页查询
     */
    function page_search() {
        showProject();
        pageTemp();
    }

    /**
     * 请求数据
     */
    function showProject(curr, pageSize) {
        var index = doLoading();
        var searchValue = $("#search-value").val().trim();
        $.ajax({
            url: '${ctx}/staff/bidSection/mapBidSection',
            type: 'post',
            cache: false,
            async: false,
            data: {
                page: curr,
                limit: pageSize,
                bidClassifyCode: bidClassifyCode,
                bidSectionCode: searchValue,
                bidSectionName: searchValue,
                bidOpenStatus: bidOpenStatus
            },
            success: function (data) {
                window.top.layer.close(index);
                if (!isNull(data.count)) {
                    //第二步：填充数据
                    // 保存总数
                    $("#count").val(data.count);
                    //第三步：渲染模版
                    var getTpl = biddingList.innerHTML
                        , view = document.getElementById('show-bidding-project');
                    laytpl(getTpl).render(data.list, function (html) {
                        view.innerHTML = html;
                    });
                }
            },
            error: function (data) {
                loadComplete();
                console.error(data);
            },
        });
    }

    /**
     * 分页模板
     */
    function pageTemp() {
        // 动态加载时，数据分页
        laypage.render({
            elem: 'page-temp',
            limit: 5,
            count: $("#count").val(),
            prev: "<i class='layui-icon'>上一页</i>",
            next: "<i class='layui-icon'>下一页</i>",
            limits: [5, 10, 15, 20],
            groups: 5,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip'],
            jump: function (obj, first) {
                // 展示当前页数据
                // 当前页
                var curr = obj.curr;
                // 页大小
                var pageSize = obj.limit;

                $("#curPage").val(curr);
                $("#pageSize").val(pageSize);

                // 分页搜索
                showProject(curr, pageSize);
            }
        })
    }

    function showMore(e) {
        var $_next_div = $(e).find("ol");
        $_next_div.show();
    }

    function hideMore(e) {
        var $_next_div = $(e).find("ol");
        $_next_div.hide();
    }

    $(function (){
        //示范一个公告层
        layer.open({
            type: 1
            ,title: "咨询电话" //不显示标题栏
            ,area: '200px;'
            , offset: 'rb'
            ,shade: 0
            ,content: '<div style="padding: 30px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;"> ' +
                '<i class="layui-icon layui-icon-cellphone"></i>&nbsp;4006131390</div>'
        });
    })

</script>
<script>
    //修改项目
    function modifyProject(id) {
        // 校验是否开标之前
        $.ajax({
            url: '${ctx}/staff/checkModify',
            type: 'post',
            cache: false,
            async: false,
            data: {"id": id},
            success: function (data) {
                if (data.code === "1") {
                    jumpModifyPage(id);
                } else {
                    layer.alert(data.msg);
                }
            },
            error: function (data) {
                console.error(data);
            },
        });
    }

    /**
     * 跳转修改项目页面
     *
     */
    function jumpModifyPage(id) {
        layer.open({
            type: 2,
            title: ['修改项目', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
            content: "${ctx}/staff/editProject?bidSectionId=" + id,
            area: ['600px', '450px'],
            btnAlign: 'c',
            shade: 0.3,
            btn: ['确认', '取消'],
            btn1: function (index, layero) {
                var body = parent.layer.getChildFrame('body', index);
                var iframeWin = parent.window[layero.find('iframe')[0]['name']];
                iframeWin.modifyProjectInfo(function (result) {
                    if (result.code === "1") {
                        parent.layer.msg(result.msg, {
                            icon: result.code, end: function () {
                                parent.layer.closeAll();
                                showTenderInfo(id);
                            }
                        });
                    }
                    if (result.code === "2") {
                        parent.layer.msg(result.msg, {
                            icon: 1, end: function () {
                                parent.layer.closeAll();
                                parent.location.reload();
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
                parent.layer.close(index);
            }
        });
    }


    function addProject() {
        layer.open({
            type: 2,
            title: ['选择添加方式', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
            content: "${ctx}/staff/chooseTypePage",
            area: ['600px', '300px'],
            btnAlign: 'c',
            shade: 0.3,
            /*btn: ['关闭'],*/
            end: function (index, layero) {
                // 点击取消的回调函数
                layer.msg("已关闭");
                layer.close(index);
            }
        })
    }

    /**
     * 进入开标
     */
    function joinBidOpen(id) {
        doLoading();
        setBidSectionId(id, function () {
            // 移除倒计时缓存
            localStorage.removeItem("countDownTime_" + id);
            window.location.href = '/staff/joinBidOpen'
        });
    }

    /**
     * 显示招标项目信息
     */
    function showTenderInfo(id) {
        parent.layer.open({
            type: 2,
            title: "招标项目信息",
            shadeClose: false,
            area: ['1000px', '760px'],
            content: "/staff/confirmProjectInfoPage?flag=1&bidSectionId=" + id,
            btn: ['确认', '取消'],
            btn1: function (index, layero) {
                var body = parent.layer.getChildFrame('body', index);
                var iframeWin = parent.window[layero.find('iframe')[0]['name']];
                iframeWin.saveProjectInfo();
            },
            btn2: function (index) {
                // 点击取消的回调函数
                layer.msg("已取消", {icon: 1});
                layer.close(index);
            }
        });
    }

    function selectChangeSite(id, regId) {
        if (regId == null || regId === "") {
            window.top.layer.msg("未配置主场地区！", {icon: 2, time: 2000});
        } else {
            layer.open({
                type: 2,
                offset: 'auto',
                title: ['主客场设置', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
                content: '${ctx}/bidSectionRelate/selectChangeSitePage?id=' + id + '&regId=' + regId,
                btn: ['确认', '取消'],
                area: ['600px', '530px'],
                btnAlign: 'c',
                shade: 0.3,
                btn1: function (index, layero) {
                    var body = window.layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    iframeWin.selSuccess(function () {
                        layer.msg("选择成功！");
                        window.layer.close(index);
                    });
                }
                , btn2: function (index) {
                    window.layer.close(index);
                }
            });
        }
    }


    function liveInfo(id, room) {
        $(".stream-key").text(room);
        window.top.layer.open({
            type: 1,
            title: ['直播信息', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
            id: 'liveInfo' + id,
            content: $('.liveTan'),
            area: ['600px', '360px'],
            btn: ['关闭'],
            btnAlign: 'c',
            shade: 0.3,
            yes: function () {
                $('.liveTan').hide();
                layer.closeAll();
            }
        });
    }

    // 选中用户新增的项目分类
    function chooseUserProject() {
        var userBidClassifyCode = localStorage.getItem("userBidClassifyCode");
        $(".project-type").find("li").each(function () {
            var dataID = $(this).data("classify-code");
            if (userBidClassifyCode === dataID) {
                $(this).click();
            }
        })
    }

    /**
     * 设置操作的标段ID
     * ***/
    function setBidSectionId(bidSectionId, callback) {
        $.ajax({
            url: '/staff/setBidSectionId',
            type: 'post',
            cache: false,
            data: {
                bidSectionId: bidSectionId
            },
            success: function (data) {
                callback();
            },
            error: function (data) {
                console.warn(data);
                layer.msg("操作失败！")
            },
        });

    }

    /**
     * 文件下载
     * @param id 标段id
     */
    function downFiles(id) {
        setBidSectionId(id, function () {
            window.location.href = "${ctx}/staff/downFiles";
        })
    }

    /**
     * 签到设置
     */
    function signSet(id) {
        setBidSectionId(id, function () {
            layer.open({
                type: 2,
                title: ['签到设置', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
                id: 'liveInfo' + id,
                content: "${ctx}/staff/signSetPage",
                area: ['600px', '360px'],
                btn: ['确定', '关闭'],
                btnAlign: 'c',
                shade: 0.3,
                btn1: function (index, layero) {
                    var body = layer.getChildFrame('body', index);
                    var iframeWin = window[layero.find('iframe')[0]['name']];
                    iframeWin.saveSignTime();
                },
                btn2: function () {
                    layer.closeAll();
                }
            });
        });
    }

    /**
     * 澄清文件上传
     * @param bidSectionId 标段id
     * @param regId 区划id
     */
    function uploadClarify(bidSectionId, regId) {
        var allowType = "*.pdf;*.PDF";
        var allowFileSize = "500M";
        window.top.layer.open({
            type: 2,
            content: '${ctx!}/fdfs/uploadFilePage',
            title: '澄清文件上传(*.pdf)',
            shadeClose: true,
            area: ['600px', '540px'],
            btn: ['关闭'],
            success: function (layero, index) {
                var body = layer.getChildFrame('body', index);
                var iframeWin = window.top[layero.find('iframe')[0]['name']];
                iframeWin.initUploadParam(allowType, allowFileSize);
                iframeWin.dropzoneInit("", function (uploadFile) {
                    console.log(uploadFile.name);
                    var clarifyFileId = uploadFile.id;
                    window.top.layer.close(index);
                    saveClarifyFile(bidSectionId, regId, clarifyFileId);
                });
            },
            btn1: function (index) {
                window.top.layer.close(index);
            }
        });
    }

    /**
     * 保存澄清文件
     * @param bidSectionId 标段id
     * @param regId 区划id
     * @param clarifyFileId 文件id
     */
    function saveClarifyFile(bidSectionId, regId, clarifyFileId) {
        $.ajax({
            url: "${ctx}/staff/saveClarifyFile",
            type: "POST",
            cache: false,
            data: {
                "id": bidSectionId,
                "regId": regId,
                "clarifyFileId": clarifyFileId,
            },
            success: function (data) {
                if (data.s) {
                    layer.msg("澄清文件上传成功！", {
                        icon: 1, end: function () {
                            parent.window.location.href = window.location.href;
                        }
                    });
                } else {
                    layer.alert("澄清文件上传失败！", {icon: 5})
                }
            },
            error: function () {
                layer.alert("澄清文件上传失败！", {icon: 5})
            }
        })
    }

    /**
     * 开始复会
     * @param bidSectionId
     */
    function startMeeting(bidSectionId) {
        layer.open({
            type: 2,
            title: ['修改复会时间', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
            content: "${ctx}/staff/setMeetingTimePage?bidSectionId=" + bidSectionId,
            area: ['600px', '450px'],
            btnAlign: 'c',
            shade: 0.3,
            btn: ['确认', '取消'],
            btn1: function (index, layero) {
                var body = parent.layer.getChildFrame('body', index);
                var iframeWin = parent.window[layero.find('iframe')[0]['name']];
                iframeWin.modifyResumeTime(function () {
                    layer.close(index);
                    window.location.href = '${ctx}/staff/joinMeeting';
                });
            },
            btn2: function (index) {
                parent.layer.close(index);
            }
        });
    }

    /**
     * 进入复会
     * @param bidSectionId 标段主键
     */
    function resumptionMeeting(bidSectionId){
        // 检查是data否设置复会时间
        setBidSectionId(bidSectionId, function () {
            doLoading();
            $.ajax({
                url: '${ctx}/staff/getBidSection',
                type: 'post',
                cache: false,
                success: function (data) {
                    loadComplete();
                    if (data.code==="2" && !isNull(data.data.resumeTime)) {
                        // 移除复会倒计时缓存
                        localStorage.removeItem("resumeCountDownTime_" + bidSectionId);
                        // 进入复会
                        window.location.href = '${ctx}/staff/joinMeeting';
                    } else {
                        // 选择复会时间
                        layerWarning("请选择复会时间!",function () {
                            startMeeting(bidSectionId);
                        })
                    }
                },
                error: function (data) {
                    loadComplete();
                    console.error(data);
                },
            });
        });
    }

    function preRelated(bidSectionId) {
        window.top.layer.open({
            type: 2,
            title: "预审关联",
            content: "${ctx}/staff/preRelatedPage?bidSectionId=" + bidSectionId,
            offset: 'auto',
            area: ['80%', '65%']
        });
    }

    /**
     * 消息导出
     */
    function msgExport(id) {

        layerLoading();
        setBidSectionId(id, function () {
            initNoShowMesgBox(function () {
                $.ajax({
                    url: "${ctx}/onlineChat/getMessageExport",
                    type: "POST",
                    cache: false,
                    success: function (data) {
                        if (data) {
                            postMessageFun(data);
                            window.addEventListener("message", function (e) {
                                if (!isNull(e.data.importList)) {
                                    msgPdfSynthesis(e.data.importList)
                                } else {
                                    loadComplete();
                                    clearNoShow();
                                    layerWarning("暂无消息记录！");
                                }
                            })
                        }
                    },
                    error: function (e) {
                        console.error(e);
                    }
                });
            });
        })
    }

    /**
     * 合成pdf
     */
    function msgPdfSynthesis(data) {
        $.ajax({
            url: "${ctx}/onlineChat/msgPdfSynthesis",
            type: "POST",
            cache: false,
            data: {
                "data": JSON.stringify(data)
            },
            success: function (result) {
                if (result.code == "1") {
                    var newUrl =result.data + "?filename=" + encodeURI(result.msg, "utf8");
                    window.location.href = encodeURI(newUrl, "utf8");
                } else {
                    layerWarning(result.msg);
                }
                loadComplete();
                clearNoShow();
            },
            error: function (e) {
                console.error(e);
            }
        });
    }
</script>
</html>