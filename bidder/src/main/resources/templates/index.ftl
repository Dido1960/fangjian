<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>项目管理列表</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_header" content="${_csrf.headerName}"/>
    <script src="${ctx}/js/jquery-3.4.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <!--[if lt IE 9]>
    <script src=${ctx}"/js/html5shiv.min.js"></script>
    <script src=${ctx}"/js/respond.min.js"></script>
    <![endif]-->
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <link rel="stylesheet" href="${ctx}/css/header.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/bidderUtils.css">
    <link rel="stylesheet" href="${ctx}/css/list.css">
    <style>
        html, body {
            width: 100%;
            height: 100%;
            min-width: 1200px;
            min-height: 800px;
            background: #eee;
        }

        .gefUploadBox input {
            width: 70%;
            height: 40px;
            border: 1px solid rgba(213, 213, 213, 1);
            outline: none;
            font-size: 14px;
            font-family: "Microsoft YaHei";
            font-weight: 400;
            padding: 0 10px;
            box-sizing: border-box;
            line-height: 40px;
            margin: 20px 0 0 30px;
        }

        .center {
            width: 100%;
            min-height: 96px;
            background: #ffffff;
            opacity: 1;
            text-align: center;
            margin-top: 20px;
            line-height: 96px;
            font-size: 16px;
            font-weight: 500;
        }

        #search-value::-ms-clear {
            display: none;
        }
    </style>
    <#--项目模板。 -->
    <script id="projectTemplate" type="text/html">
        {{# var startNum= ($("#curPage").val()-1)*($("#pageSize").val())}}
        {{# layui.each(d,function(index, bidSection){ }}
        <li>
            <div class="small">{{=startNum+index+1}}</div>
            <div><p>{{bidSection.bidSectionName||''}}</p></div>
            <div><p>{{bidSection.bidSectionCode||''}}</p></div>
            <div>{{bidSection.bidOpenTime||''}}</div>
            <div class="bidOpenStatus">{{bidSection.bidOpenStatusName||''}}</div>
            <#-- 网上开标 -->
            {{#if(bidSection.bidOpenOnline==1){ }}
                    <#-- 项目未开标才能参标 -->
                {{#if(bidSection.dataFrom == 0){ }}
                    {{#if(bidSection.isJoinBid==0 && bidSection.bidOpenStatus == 0){ }}
                        <div>
                            <span class="blove-b" onclick='joinBid("{{bidSection.id}}",this)'>我要参标</span>
                        </div>
                    {{# }else if(bidSection.isJoinBid == 1){ }}
                        {{#if(bidSection.bidOpenStatus == 2){ }}
                            {{#if(!isNull(bidSection.resumeTime) && bidSection.resumeStatus != 2){ }}
                                <div>
                                    <span class="green-b" onclick='joinMeeting("{{bidSection.id}}","{{bidSection.bidderId}}")'>进入复会</span>
                                </div>
                            {{# } }}
                        {{# }else{ }}
                            <div>
                                <span class="green-b" onclick='bidOpenOnline("{{bidSection.id}}","{{bidSection.bidderId}}")'>网上开标</span>
                            </div>
                        {{# } }}
                    {{# } }}
                {{# }else{ }}
                    {{#if(bidSection.isJoinBid==0 && bidSection.bidOpenStatus == 0){ }}
                        <#--<div>
                            <span class="blove-b" onclick='joinBid("{{bidSection.id}}",this)'>我要参标</span>
                        </div>-->
                    {{# }else if(bidSection.isJoinBid == 1){ }}
                        {{#if(bidSection.bidOpenStatus == 2){ }}
                            {{#if(!isNull(bidSection.resumeTime) && bidSection.resumeStatus != 2){ }}
                                <div>
                                    <span class="green-b" onclick='joinMeeting("{{bidSection.id}}","{{bidSection.bidderId}}")'>进入复会</span>
                                </div>
                            {{# } }}
                        {{# }else{ }}
                            <div>
                                <span class="green-b" onclick='bidOpenOnline("{{bidSection.id}}","{{bidSection.bidderId}}")'>网上开标</span>
                            </div>
                        {{# } }}
                    {{# } }}
                {{# } }}
            <#-- 现场开标 -->
            {{# }else{ }}
                <#-- 资格预审 -->
                {{#if(bidSection.bidClassifyCode=="A10" && beforeBidOneDay(bidSection.bidOpenTime)){ }}
                <div>
                    <span class="green-b" onclick='uploadBidFile(this)'>上传文件</span>
                    <input type="hidden" value="{{bidSection.id||''}}">
                </div>
                {{# } }}
            {{# } }}
        </li>
        {{#  }); }}
        {{#  if(d.length === 0){ }}
            <div class="center">暂无数据</div>
        {{#  } }}
    </script>
</head>
<body>
<div class="box" style="overflow: hidden">
    <#assign hideProjectBtn=1 />
    <#include "${ctx}/common/baseHeader.ftl"/>
    <section>
        <#-- 记录招标项目类型-->
        <input id="hidden-bid-type" type="hidden" value="">
        <#-- 记录开标地点-->
        <input id="hidden-bid-place" type="hidden" value="1">
        <ul class="head projectType">
            <li data-type="bid-type" data-id="" class="blove">全部</li>
            <li data-type="bid-type" data-id="A08">施工</li>
            <li data-type="bid-type" data-id="A05">监理</li>
            <li data-type="bid-type" data-id="A03">勘察</li>
            <li data-type="bid-type" data-id="A04">设计</li>
            <li data-type="bid-type" data-id="A11" style="width: 130px;">电梯采购与安装</li>
            <li data-type="bid-type" data-id="A10">资格预审</li>
            <li data-type="bid-type" data-id="A12">工程总承包</li>
        </ul>
        <div class="seach">
            <div class="sea-l projectType">
                <span data-type="bid-open-place" data-id="1" class="blove-s blove-f">网上开标</span>
                <span data-type="bid-open-place" data-id="0" class="gray-s gray-f">现场开标</span>
            </div>
            <div class="sea-r">
                <input type="text" placeholder="搜索关键词" id="search-value">
                <img src="${ctx}/img/seach.png" alt="" id="search-btn">
            </div>
        </div>
        <ul class="tlt">
            <li class="small">序号</li>
            <li>标段名称</li>
            <li>标段编号</li>
            <li>开标时间</li>
            <li>开标状态</li>
            <li>操作</li>
        </ul>
        <ul id="projectContent"></ul>
        <div id="page-temp"></div>
        <input id="curPage" type="hidden" value="1"/>
        <input id="pageSize" type="hidden" value="4"/>
    </section>

    <div class="gefUploadBox" style="display: none;">
        <div style="margin: 20px 0 0 30px;">投标文件（gef）</div>
        <@UploadOneTag name="gefFileId" allowFileSize="1024M"  readonly = "readonly" allowType="*.gef;"
        autocomplete="off" placeholder="请选择gef格式投标文件上传" class="gefInput layui-input">
        </@UploadOneTag>
    </div>
</div>
</body>
<script>

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
    /**
     * 加载Layui组件
     */
    var layer, laytpl, laypage, element;
    layui.use(['layer', 'element', 'laytpl', 'laypage'], function () {
        layer = layui.layer;
        element = layui.element;
        laypage = layui.laypage;
        laytpl = layui.laytpl;
        //初始化 数据分页
        pageShowProject(getProjectTotal());
        //项目类型搜索点击事件加载
        projectTypeClick();

    });

    /**
     *   给开标状态添加样式
     */
    function initBidOpenStatus() {
        var bidOpenStatusDivs = $(".bidOpenStatus");
        bidOpenStatusDivs.each(function () {
            var bidOpenStatus = $(this).text().trim();
            if (bidOpenStatus == "进行中") {
                $(this).addClass("green-f");
            }
            if (bidOpenStatus == "未开始") {
                $(this).addClass("oragen-f");
            }
            if (bidOpenStatus == "结束") {
                $(this).addClass("red-f");
            }
        });


    }

    /**
     *   数据分页
     */
    function pageShowProject() {
        laypage.render({
            elem: 'page-temp',
            curr: 1,//当前页
            limit: 5,//页大小
            count: arguments[0],
            prev: "<i class='layui-icon layui-icon-left'></i>",//上一页图标
            next: "<i class='layui-icon layui-icon-right'></i>",//上一页图标
            limits: [5, 10, 15, 20],
            groups: 5,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'refresh', 'skip'],
            // page: true,
            jump: function (obj, first) {
                // 展示当前页数据
                // 当前页
                var curr = obj.curr;
                // 页大小
                var pageSize = obj.limit;

                $("#curPage").val(curr);
                $("#pageSize").val(pageSize);

                getProjectList(obj.curr, obj.limit);
                initBidOpenStatus();
                //首次不执行
                if (!first) {
                    // 页面加载时，显示全部
                }
            }
        });
    }

    /**
     * 获取数据并渲染(条件查询)
     * @param curr 页码
     * @param pageSize
     */
    function getProjectList(curr, pageSize) {
        doLoading();
        var searchValue = $("#search-value").val().trim();

        $.ajax({
            url: '${ctx}/bidder/bidSection/listBidSection',
            type: 'post',
            cache: false,

            data: {
                page: curr,
                limit: pageSize,
                bidClassifyCode: $("#hidden-bid-type").val(),
                bidOpenOnline: $("#hidden-bid-place").val(),
                bidSectionCode: searchValue,
                bidSectionName: searchValue,
                deleteFlag: 0
            },
            success: function (data) {
                loadComplete();
                if (!isNull(data)) {
                    // var datalist=JSON.parse(data);
                    var dataList = $.parseJSON(data);
                    $(".layui-laypage-count").text(" 共 " + dataList.count + " 条 ");
                    // 获取模版
                    var getTpl = projectTemplate.innerHTML;
                    var view = document.getElementById("projectContent");
                    // 渲染模版
                    laytpl(getTpl).render(dataList.data, function (html) {
                        view.innerHTML = html;
                    });
                }
            },
            error: function (data) {
                console.error(data);
                loadComplete();
            },
        });

    }


    /**
     * 获取显示总条数
     */
    function getProjectTotal() {
        var searchValue = $("#search-value").val().trim();
        var count = 0;
        $.ajax({
            url: '${ctx}/bidder/bidSection/getProjectTotal',
            type: 'post',
            cache: false,
            async: false,
            data: {
                bidClassifyCode: $("#hidden-bid-type").val(),
                bidOpenOnline: $("#hidden-bid-place").val(),
                bidSectionCode: searchValue,
                bidSectionName: searchValue,
                deleteFlag: 0
            },
            success: function (data) {
                if (data) {
                    //$(".layui-laypage-count").text(" 共 " + data + " 条 ");
                    count = data;
                }
            },
            error: function (data) {
                console.error(data);
            },
        });

        return count;
    }

    /**
     * 项目类型、网上（线下）开标、模糊搜索 点击事件绑定
     */
    function projectTypeClick() {
        var bindItem = $(".projectType").children();
        bindItem.bind('click', function () {
            // 点击按钮所属类型
            var type = $(this).attr("data-type");
            switch (type) {
                case "bid-open-place":
                    // 开标地点
                    $("#hidden-bid-place").val($(this).attr("data-id"))
                    $(this).removeClass().addClass("blove-s blove-f")
                        .siblings().removeClass().addClass("gray-s gray-f");
                    break;
                case "bid-type":
                    // 标段类型
                    $("#hidden-bid-type").val($(this).attr("data-id"));
                    $(this).addClass("blove").siblings().removeClass("blove");
                    break;
            }
            // 重新分页渲染模板
            pageShowProject(getProjectTotal());
        });
        $("#search-btn").click(function () {
            // 重新分页渲染模板
            pageShowProject(getProjectTotal());
        });

    }

    /**
     * 我要参标
     * @param id 标段id
     * @param obj 参标按钮
     */
    function joinBid(id, obj) {
        layer.confirm("确认参标吗？", {
            btn: ['确定', '取消']
        }, function () {
            doLoading();
            $.ajax({
                url: '${ctx}/bidder/bidSection/addBidderOpenInfo',
                type: 'post',
                cache: false,
                data: {
                    bidSectionId: id,
                },
                success: function (data) {
                    loadComplete();
                    if (data.code === "1") {
                        layer.msg(data.msg, {
                            icon: data.code, end: function () {
                                $(obj).removeClass().addClass("green-b").attr("onclick",
                                    "bidOpenOnline('" + id + "','" + data.data + "')").text("网上开标");
                                bidOpenOnline(id, data.data);
                            }
                        });
                    } else {
                        layer.msg(data.msg, {icon: 5});
                    }
                },
                error: function (data) {
                    loadComplete();
                    console.error(data);
                    layer.msg('参标失败！', {icon: 5});
                },
            });
        });
    }

    /**
     * 退出登录
     */
    function exitSystem() {
        hide_IWeb2018();
        layer.confirm("确认要退出系统？", {
            icon: 3,
            title: '提示',
            end: function () {
                show_IWeb2018();
            }
        }, function (index) {
            show_IWeb2018();
            layer.close(index);
            doLoading();
            $.ajax({
                url: '${ctx}/logout',
                type: 'post',
                cache: false,
                async: false,
                success: function () {
                    loadComplete();
                    window.location.href = "${ctx}/login.html";
                }
            });
        }, function (index) {
            loadComplete();
            // 取消的回调函数
            layer.msg("已取消!", {icon: 1});
        });
    }

    /**
     * 获取当前用户
     */
    function getUser() {
        $.ajax({
            url: '${ctx}/getUser',
            type: 'post',
            cache: false,
            success: function (data) {
                $('.try .username').append(data.name);
            }
        });
    }

    /**
     * 网上开标
     */
    function bidOpenOnline(sectionId, bidderId) {
        setBidSectionId(sectionId, bidderId, function () {
            window.location.href = '/bidderModel/bidOpenOnline';
        })
    }

    /**
     * 设置操作的标段ID
     * @param bidSectionId
     * @param bidderId 可以为非必穿项
     * ***/
    function setBidSectionId(bidSectionId, bidderId, callback) {
        $.ajax({
            url: '${ctx}/bidderModel/setBidSectionIdAndBidderId',
            type: 'post',
            cache: false,
            data: {
                bidSectionId: bidSectionId,
                bidderId: bidderId
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
     * 是否开标前24小时
     */
    function beforeBidOneDay(bidOpenTime) {
        if (!isNull(bidOpenTime)) {
            //当前时间戳
            var currentStamp = parseInt(new Date().getTime());
            var bidOpenTimeStamp = bidOpenTime.substring(0, 19).replace(/-/g, '/');
            //开标时间戳
            bidOpenTimeStamp = new Date(bidOpenTimeStamp).getTime();
            var timeDiff = (bidOpenTimeStamp - currentStamp) / (1000 * 3600 * 24);

            if (bidOpenTimeStamp > currentStamp && timeDiff < 1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 文件上传
     */
    function uploadBidFile(e) {
        $(".layui-icon-upload-drag").hide();
        $(".layui-icon-close-fill").hide();
        $(".gefInput").val("");
        var bidSectionId = $(e).next().val();
        window.layer.open({
            type: 1,
            offset: 'c',
            title: ['请选择上传的文件', 'text-align:center;'],
            shadeClose: false,
            area: ['25%', '30%'],
            btn: ['确定', '取消'],
            btnAlign: 'c',
            content: $(".gefUploadBox"),
            btn1: function () {
                var fileId = $("input[name=gefFileId]").val();
                if (isNull(fileId)) {
                    layer.msg("请选择上传文件", {icon: 5})
                    return false;
                }
                doLoading();
                $.ajax({
                    url: "${ctx}/bidderModel/dockUploadBidFile",
                    type: "POST",
                    cache: false,
                    data: {
                        fileId: fileId,
                        bidSectionId: bidSectionId
                    },
                    success: function (data) {
                        loadComplete();
                        if (data.code = "1") {
                            layer.msg(data.msg, {icon: 1});
                        } else {
                            layer.msg(data.msg, {icon: 5});
                        }
                    },
                    error: function (data) {
                        loadComplete();
                        console.error(data);
                        layer.msg("上传失败", {icon: 5});
                    }
                });
            }
        });

    }

    /**
     * 投标人进入复会
     * @param bidSectionId
     */
    function joinMeeting(bidSectionId, bidderId) {
        setBidSectionId(bidSectionId, bidderId, function () {
            window.location.href = "${ctx}/bidder/bidSection/joinMeeting"
        });
    }

</script>

</html>