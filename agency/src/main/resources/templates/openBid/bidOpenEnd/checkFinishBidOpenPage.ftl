<meta name="_csrf" content="${_csrf.token}"/>
<meta name="_csrf_header" content="${_csrf.headerName}"/>
<link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css">
<link rel="stylesheet" href="${ctx}/css/utils.css">
<script src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
<script src="${ctx}/js/jquery-3.4.1.min.js"></script>
<script src="${ctx}/js/common.js"></script>
<style>
    html .layui-layer-title {
        padding: 0;
        border-bottom: 1px solid rgba(213, 213, 213, 1);
    }

    .layui-laydate-header i:hover,
    .layui-laydate-header span:hover {
        color: rgba(19, 97, 254, 1);
    }
    .schedule{
        padding-top: 40px;
        box-sizing: border-box;
    }
    .process_h3 {
        width: 100%;
        height: 41px;
        font-size: 24px;
        font-family: Microsoft YaHei;
        font-weight: 900;
        text-align: center;
        line-height: 41px;
        color: rgba(34, 49, 101, 1);
        opacity: 1;
    }

    html .layui-progress {
        width: 480px;
        height: 20px;
        background: rgba(230, 237, 244, 1);
        opacity: 1;
        margin: 0 auto;
        border-radius: 0;
    }

    html .layui-progress .layui-progress-bar {
        height: 20px;
        border-radius: 0;
    }

    .tan ul {
        width: 480px;
        margin: 50px auto 0;
    }

    .tan ul li {
        width: 100%;
        height: 20px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 10px;
    }

    .tan ul li p {
        width: 135px;
        display: flex;
        align-items: center;
    }

    .tan ul li p img {
        margin-right: 10px;
    }

    /* .tan ul li span img {
        display: block;
        width: 16px;
        height: 16px;
        margin-left: 10px;
    } */

    .tan ul li span {
        font-size: 14px;
        font-family: Microsoft YaHei;
        font-weight: 400;
        color: rgba(213, 213, 213, 1);
        display: flex;
        align-items: center;
    }

    .tan ul li div {
        width: 100px;
    }

    .tan ul li span div {
        width: 16px;
        height: 16px;
        margin-left: 10px;
        background: url(${ctx}/img/loging.png) no-repeat;
    }
    .tan ul li .watch {
        display: block;
        width: 36px;
        height: 20px;
        font-size: 12px;
        font-family: Microsoft YaHei;
        font-weight: 500;
        line-height: 20px;
        text-align: center;
        color: #F53923;
        cursor: pointer;
    }
    .tlit {
        width: 100%;
        display: flex;
        padding: 0 245px;
        padding-top: 34px;
        box-sizing: border-box;
    }

    .reslut_h3 {
        width: 222px;
        height: 36px;
        font-size: 24px;
        font-family: Microsoft YaHei;
        font-weight: 900;
        line-height: 36px;
        color: rgba(245, 57, 35, 1);
    }

    .tan .tlit span {
        display: block;
        width: 80px;
        height: 36px;
        background: linear-gradient(270deg, rgba(19, 97, 254, 1) 0%, rgba(78, 138, 255, 1) 100%);
        filter: progid:DXImageTransform.Microsoft.gradient(GradientType=1 ,startColorstr=#1361fe, endColorstr=#4e8aff);
        opacity: 1;
        font-size: 14px;
        font-family: Microsoft YaHei;
        text-align: center;
        font-weight: 500;
        line-height: 36px;
        color: rgba(255, 255, 255, 1);
        cursor: pointer;
    }

    .tan .tlit span:hover {
        opacity: 0.7;
        transition: 0.3s;
    }
</style>
<div class="tan">
    <div class="schedule">
        <h3 class="process_h3">正在检查所有环节信息...</h3>
        <div class="layui-progress layui-progress-big" lay-showpercent="true" lay-filter="schedule">
            <div class="layui-progress-bar layui-bg-blue" data-type="setPercent" lay-percent="0%"></div>
        </div>
    </div>
    <div class="tlit problem" style="display: none;">
        <h3 class="reslut_h3">发现5个问题</h3><span onclick="location.reload();">重新检查</span>
    </div>
    <ul class="site-doc-icon site-doc-anim render-ul-li-all">
        <#if bidSection.bidClassifyCode == "A12">
            <li data-process="controlPrice" data-type="setUp">
                <p> <img src="${ctx}/img/left_1.png" alt="">最高投标限价</p>
                <div></div>
                <span></span>
            </li>
        </#if>
        <#if bidSection.bidClassifyCode == "A08">
            <li data-process="controlPrice" data-type="setUp">
                <p> <img src="${ctx}/img/left_1.png" alt="">招标控制价</p>
                <div></div>
                <span></span>
            </li>
            <li data-process="floatPoint" data-type="setUp">
                <p> <img src="${ctx}/img/left_2.png" alt="">抽取浮动点</p>
                <div></div>
                <span></span>
            </li>
        </#if>
        <li data-process="publishBidder" data-type="check">
            <p> <img src="${ctx}/img/left_3.png" alt="">公布投标人名单</p>
            <div></div>
            <span></span>
        </li>
        <li data-process="checkPrincipalIdentity" data-type="check">
            <p> <img src="${ctx}/img/left_4.png" alt="">委托人身份检查</p>
            <div></div>
            <span></span>
        </li>
        <li data-process="bidderFileDecrypt" data-type="check">
            <p> <img src="${ctx}/img/left_5.png" alt="">文件上传及解密</p>
            <div></div>
            <span></span>
        </li>
        <#if bidSection.bidClassifyCode == "A08">
            <li data-process="controlPriceAnalysis" data-type="check">
                <p> <img src="${ctx}/img/left_6.png" alt="">控制价分析</p>
                <div></div>
                <span></span>
            </li>
        </#if>
        <#if bidSection.bidClassifyCode == "A12">
            <li data-process="controlPriceAnalysis" data-type="check">
                <p> <img src="${ctx}/img/left_6.png" alt="">最高投标限价分析</p>
                <div></div>
                <span></span>
            </li>
        </#if>
        <#if bidSection.bidClassifyCode != "A10">
            <li data-process="fileCursor" data-type="check">
                <p> <img src="${ctx}/img/left_7.png" alt="">文件唱标</p>
                <div></div>
                <span></span>
            </li>
        </#if>
        <li data-process="bidOpenRecord" data-type="check">
            <p> <img src="${ctx}/img/left_8.png" alt="">开标记录表</p>
            <div></div>
            <span></span>
        </li>
<#--        <li data-process="bidFileUpload" data-type="check">-->
<#--            <p> <img src="${ctx}/img/left_11.png" alt="">数据上传评标</p>-->
<#--            <div></div>-->
<#--            <span></span>-->
<#--        </li>-->
    </ul>
</div>
<div id="initial_content" style="display: none;">正在进行扫描检查<div class="layui-anim layui-anim-rotate layui-anim-loop"> </div></div>
<div id="finish_content" style="display: none;"><img style="margin: auto" src="${ctx}/img/right.png" alt=""></div>
<script>
    // 检查状态
    var checkStatus = false;
    // 检查的问题数
    var problem = 0;
    /**
     * 加载Layui组件
     */
    var element
    layui.use('element', function () {
        element = layui.element;
    });

    /**
     * 检查是否完成
     */
    function checkProcessStatus(callBack){
        callBack(checkStatus, problem);
    }

    $(function () {
        // 初始化加载的span
        var content = $("#initial_content").html();
        $(".render-ul-li-all li span").html(content);
        listProcess();
    })

    /**
     * 获取开标进行环节的完成情况
     */
    function listProcess() {
        $.ajax({
            type: "post",
            url: "${ctx}/staff/listBidOpenProcessComplete",
            cache: false,
            data: {
                "id": "${bidSection.id}"
            },
            success: function (data) {
                updateProcess(data);
            }
        });
    }

    /**
     * 更新开标流程
     * @param data 完成的流程
     */
    function updateProcess(data) {
        var $li = $(".render-ul-li-all li");
        var liCount = $li.length;
        var finish = $("#finish_content").html();
        var time = 1000;

        $li.each(function (index) {
            var $_this=$(this);
            var times = time * (index + Math.random())
            setTimeout(function () {
                var process = $_this.data("process");
                var type = $_this.data("type");
                var $div = $_this.find("div:eq(0)");
                var $span = $_this.find("span:eq(0)");
                if (data.indexOf(process) === -1) {
                    ++problem;
                    $div.removeClass("green-f").addClass("red-f").text("异常");
                    $span.addClass("watch").addClass("red-s").css("width", "36px");
                    if (type === "setUp") {
                        $span.text("未设置");
                    } else {
                        $span.text("查看");
                    }
                    $span.on("click",function(){
                        goToMessagePage(process, index);
                    });
                } else {
                    $div.removeClass("red-f").addClass("green-f").text("完成");
                    $span.removeClass("watch").removeClass("red-s").css("width", "36px").html(finish);
                }

                // 加载进度条，修改校验状态
                if ((index+1) ===  liCount) {
                    element.progress('schedule', '100%');
                    checkStatus = true;
                    sleep(1000);
                    if (problem === 0) {
                        $(".reslut_h3").css("color", "#02c386").html("检查完成，没有问题");
                    } else {
                        $(".reslut_h3").html("发现"+ problem +"个问题");
                    }
                    $(".schedule").hide();
                    $(".problem").show();
                } else {
                    element.progress('schedule', (times/(liCount*time))*100 + '%');
                }
            }, times);
        });
    }

    /**
     * 跳转异常信息提示页面
     * @param step 环节
     * @param index 环节下标
     */
    function goToMessagePage(step, index) {
        $.ajax({
            type: "post",
            url: "${ctx}/staff/getExceptionMessage",
            cache: false,
            data: {
                "bidSectionId": "${bidSection.id}",
                "step": step
            },
            success: function (data) {
                if (data.code === "1") {
                    var map = data.data;
                    layer.open({
                        type: 1,
                        title: [map.stepName, 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
                        content: "<div style='width: 100%;height: 30px;font-size: 14px;font-family: Microsoft YaHei;" +
                            "font-weight: 900;line-height: 30px;text-align: center;" +
                            "color: rgba(245, 57, 35, 1);margin-top: 80px;'>" +
                            "<h3>" + map.msg +"</h3></div>",
                        btn: ['前往处理'],
                        area: ['600px', '300px'],
                        btnAlign: 'c',
                        shade: 0.3 ,
                        yes: function () {
                            parent.layer.closeAll();
                            parent.$(".left ul li").eq(index).trigger("click");
                        }
                    });
                }
            }
        });
    }
</script>

