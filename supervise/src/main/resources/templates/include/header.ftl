<link rel="stylesheet" href="${ctx}/css/utils.css"/>
<link rel="stylesheet" href="${ctx}/css/header.css"/>
<script src="${ctx}/js/common.js"></script>
<script type="text/javascript" src="//api.map.baidu.com/api?v=2.0&ak=Ykswepd0YvdNc5zmAWAkVhY0UCTrdZGE"></script>
<style>
    .back-dot{
        margin: 13px -3px 1px 5px;
    }
</style>
<header>
    <div class="text">
        <div class="name">甘肃省电子开标评标平台<span>（市政房建)</span></div>
        <#if bidSection.id??>
            <div class="bao">
                <div class="off blove-f" onclick="window.location.href='/index'">切换标段</div>
                <div class="try">
                    <b class="username" onclick="exitSystem()">${dep.depName!}</b>
                    <i onclick="exitSystem()"></i>
                </div>
            </div>
            <div class="more">
                <span onclick="toLivePage('${bidSection.id}')">开标大厅</span>
                <#if bidSection.remoteEvaluation == 1>
                    <span onclick="toRemotePage()">远程异地</span>
                </#if>
                <#if bidSection.evalStatus == 2>
                    <span onclick="reEval()">项目复议</span>
                </#if>
                <#if pauseStatus == 1>
                    <span onclick="pauseReasonWin(0)">项目暂停</span>
                <#else >
                    <span onclick="pauseReasonWin(1)">项目启动</span>
                </#if>
                <span onclick="evalBack()">回退审核<#if waitFreeBackApply><i class="layui-badge-dot back-dot"></i></#if></span>

                <#if bidSection?? && bidSection.bidClassifyCode == "A08"  &&  bidSection.ywCode??>
                    <span onclick="goToClearUrl('${bidSection.id}')">清标结果</span>
                    <#--价格分计算完才显示-->
                    <span onclick="priceScorePage('${bidSection.id}')">报价得分</span>
                </#if>

                <span class="still pitch">更多
                    <ol>
                        <#if bidSection?? && bidSection.bidClassifyCode == "A08">
                            <li onclick="checkList()">查看清单</li>
                        </#if>
                        <li onclick="bidFileCompared()" >文件对比</li>
    <#--                    <li onclick="recordFile()">开标记录表</li>-->
                        <li onclick="clarifyFile()">澄清文件</li>
                    </ol>
                </span>
            </div>
        <#else>
            <div class="bao">
                <div class="try" onclick="exitSystem()">
                    <b class="username"></b>
                    <i ></i>
                </div>
            </div>
        </#if>
    </div>
</header>
<script>
    $(function () {
        getUser();
    })

    /**
     * 获取当前用户
     */
    function getUser() {
        $.ajax({
            url: '${ctx}/getUser',
            type: 'post',
            cache: false,
            success: function (data) {
                $('.try .username').append(data.depName).attr("title",data.depName);;
            }
        });
    }


    /**
     * 更多点击展开效果
     */
    $('.pitch').on('click', function () {
        if ($(this).hasClass('choice')) {
            $(this).removeClass('choice')
        } else {
            $(this).addClass('choice')
        }
    })
    // 页面头部的更多的下拉效果
    $('.more').on('click', '.still', function () {
        if ($(this).hasClass('onlist')) {
            $(this).removeClass('onlist')
        } else {
            $(this).addClass('onlist')
        }
    })

    /**
     * 项目暂停、继续
     */
    function pauseReasonWin(val) {
        var title_str = "";
        if (val===0){
            title_str = "项目暂停";
        } else {
            title_str = "项目继续";
        }
        //页面层
        hide_IWeb2018();
        var stopOpenIndex = window.layer.open({
            type: 2,
            title: [title_str, 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            shadeClose: true,
            area: ['1520px', '770px'],
            btn: ['关闭'],
            resize: false,
            move: false,
            offset: 'auto',
            content: "${ctx}/gov/bidEval/pauseProjectPage",
            btn1: function (index) {
                window.layer.close(index);
            },
            end: function () {
                show_IWeb2018();
            }
        });
    }

    //项目复议
    function reEval() {
        //页面层
        hide_IWeb2018();
        var INDEX = layer.open({
            type: 2,
            title: ["项目复议", 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            shadeClose: true,
            area: ['1520px', '770px'],
            resize: false,
            move: false,
            offset: 'auto',
            skin: 'reconsideration-box',
            content: "${ctx}/gov/bidEval/reEvalPage",
            end: function () {
                show_IWeb2018();
                window.top.location.reload();
            }
        });
    }

    /**
     * 回退审核
     */
    function evalBack(){
        //页面层
        hide_IWeb2018();
        var INDEX = layer.open({
            type: 2,
            title: ["回退审核", 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            shadeClose: true,
            area: ['1520px', '770px'],
            btn: ['关闭'],
            resize: false,
            move: false,
            offset: 'auto',
            content: "${ctx}/gov/bidEval/fallbackAuditPage",
            btn1: function (index) {
                layer.close(index);
            },
            end: function () {
                show_IWeb2018();
                window.top.location.reload();
            }
        });
        localStorage.setItem("thisTopIndex",INDEX);
    }

    /**
     * 查看清单
     */
    function checkList() {
        hide_IWeb2018();
        window.layer.open({
            type: 2,
            title: ['查看清单', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            shadeClose: true,
            area: ['1520px', '770px'],
            btn: ['关闭'],
            resize: false,
            move: false,
            offset: 'auto',
            content: "${ctx}/gov/viewPdf/checkListPage",
            btn1: function (index) {
                window.layer.close(index);
            },
            end: function () {
                show_IWeb2018();
            }
        });
    }

    /**
     * 文件对比
     */
    function bidFileCompared() {
        window.layer.open({
            type: 2,
            title: '标书对比',
            shadeClose: true,
            area: ['100%', 'calc(100vh - 80px)'],
            btn: 0,
            resize: false,
            move: false,
            offset: 'rb',
            content: "${ctx}/gov/viewPdf/bidFileComparedPage",
        });
    }

    /**
     * 澄清文件查看
     */
    function clarifyFile() {
        hide_IWeb2018();
        window.layer.open({
            type: 2,
            title: ['澄清文件', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            shadeClose: true,
            area: ['1520px', '770px'],
            btn: ['关闭'],
            resize: false,
            move: false,
            offset: 'auto',
            content: "${ctx}/gov/viewPdf/clarifyFilePage",
            btn1: function (index) {
                window.layer.close(index);
            },
            end: function () {
                show_IWeb2018();
            }
        });
    }

    /**
     * 把投标人主键存入session
     * @param bidderId 投标人主键
     * @returns {保存成功，返回true}
     */
    function setBidderId(bidderId){
        // 保存成功标识
        var isSaveFlag = false;
        var indexLoad= layer.load();
        $.ajax({
            url:'${ctx}/gov/setSessionBidderId',
            type:'post',
            cache:false,
            async: false,
            data:{
                bidderId : bidderId
            },
            success:function(data){
                layer.close(indexLoad)
                isSaveFlag = true;
            },
            error:function(data){
                console.error(data);
                layer.close(indexLoad)
            },
        });
        return isSaveFlag;
    }

    /**
     * 跳转直播查看页面
     */
    function toLivePage(bidSectionId) {
        var nowCity = new BMap.LocalCity();
        var abc = '';
        nowCity.get(function (rs) {
            abc = rs.center.lng + "," + rs.center.lat;
            console.log(abc)
            $.ajax({
                url: '/bidOpenHall/saveLatiLongitude',
                type: 'post',
                cache: false,
                //async: false,
                data: {
                    bidSectionId: bidSectionId,
                    latiLongitude: abc
                },
                success: function (data) {
                    toInformation(bidSectionId);
                },
                error: function (data) {
                    console.log(data);
                },
            });
        })


    }

    /**
     * 进入项目
     */
    function toInformation(bidSectionId) {
        $.ajax({
            url: '${ctx}/bidOpenHall/intoInfoPage',
            type: 'post',
            cache: false,
            data: {'bidSectionId': bidSectionId},
            success: function (data) {
                if ("200" === data.code) {
                    if ("0" === data.msg) {
                        layer.msg("<span style='color: #DE8F35'>项目尚未开标</span>", {icon: 0});
                    } else if ("1" === data.msg) {
                        window.open("${ctx}/bidOpenHall/toVisitorBidHall", "_blank");
                    } else if ("2" === data.msg) {
                        layer.msg("<span style='color: #DE8F35'>项目开标已结束</span>", {icon: 0});
                    }
                }
            }, error: function (data) {
                console.warn(data);
                layerWarning("刷新后重试", function () {
                    window.location.reload();
                })
            },
        });
    }

    function toRemotePage() {
        layerLoading();
        $.ajax({
            url: "/gov/checkAnyChatData",
            type: "POST",
            cache: false,
            success: function (data) {
                setTimeout(function(){
                        loadComplete();
                        if (data.code == "1") {
                            window.open('${ctx}/gov/anyChatScreen');
                        }else {
                            layerLoading(data.msg,2,2);
                        }
                }, 400);
            },
            error:function (e) {
                loadComplete();
                console.error(e);
            }
        });
    }

    /**
     * 跳转到清标V3.0 页面
     * @param bId 标段ID
     */
    function goToClearUrl(bId){
        //跳转至前端页面
        window.location.href = '${ctx}/clearBidV3/toSysFront?bId='+bId+'&backPage=' + encodeURIComponent(window.location.href);
    }

    /**
     * 跳转至清标3.0 -> 报价得分页面
     * @param bId 标段ID
     */
    function priceScorePage(bId) {
        <#if bidSection.priceRecordStatus == 0 >
            layer.alert("价格分尚未计算，请耐心等待!",{icon:4})
        <#elseif bidSection.priceRecordStatus == 1>
            window.location.href = "${ctx}/clearBidV3/toPriceScorePage?bId="+bId+"&backPage=" + encodeURIComponent(window.location.href);
        <#else >
             layer.alert("价格分正在计算中，请耐心等待!",{icon:6})
        </#if>
    }
</script>
