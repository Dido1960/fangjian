<!DOCTYPE html>
<html lang="zh">
<head>
    <meta charset="utf-8">
    <title>甘肃省房建市政电子辅助评标系统</title>
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

    <script src="${ctx}/js/base64.js"></script>
    <script src="${ctx}/js/LodopFuncs.js"></script>
    <link rel="stylesheet" href="${ctx}/css/header.css">
    <link rel="stylesheet" href="${ctx}/css/utils.css">
    <link rel="stylesheet" href="${ctx}/css/commitBook.css">
</head>
<body>
<header>
    <div class="text">
        <div class="name">
            <img src="../img/logo_blue.png" alt="">
            甘肃省房建市政电子辅助评标系统
        </div>
        <div class="bao">
            <div class="try" onclick="exitSystem()">
                <b class="username" title="${user.name}">${user.name!}</b>
                <i ></i>
            </div>
        </div>
    </div>
</header>
<section>
    <div class="head">
        <div>
            <p>标段名称：</p><span title="${bidSection.bidSectionName}">${bidSection.bidSectionName}</span>
        </div>
        <div>
            <p>标段编号：</p><span title="${bidSection.bidSectionCode}">${bidSection.bidSectionCode}</span>
        </div>
    </div>
    <div class="document">
        <div class="list">
            <h3>投标单位信息</h3>
            <div class="table">
                <ol>
                    <li class="small">序号</li>
                    <li>投标单位名称</li>
                    <li>投标人统一社会信用代码</li>
                    <li>法定代表人或委托代理人名称</li>
                </ol>
                <div class="detailed">
                    <table cellpadding=0 cellspacing=0>
                        <#if bidders??>
                            <#list bidders as bidder>
                                <tr>
                                    <td class="small">${bidder_index+1}</td>
                                    <td>${bidder.bidderName}</td>
                                    <td>${bidder.bidderOrgCode}</td>
                                    <td>${bidder.bidderOpenInfo.clientName}</td>
                                </tr>
                            </#list>
                        </#if>
                    </table>
                </div>
            </div>
        </div>
        <div class="cont"><span class="printing" onclick="printThis()"></span>
            <h2>专家评标诚信廉政承诺书</h2>
            <h3>作为评标专家,我郑重承诺:</h3>
            <div>一、有下列情形之一的，主动提出回避:<br>
                （一）与投标人的法定代表人或者负责人有夫妻、直系血亲、三代以内旁系血亲或者近姻亲关系；<br>
                （二）参加采购活动前3年内与投标人存在劳动关系；<br>
                （三）参加采购活动前3年内担任投标人的董事、监事；<br>
                （四）参加采购活动前3年内是投标人的控股股东或者实际控制人；<br>
                （五）项目主管部门或者行政监督部门的人员；<br>
                （六）与投标人有利害关系，可能影响对投标公正评审的；<br>
                （七）曾因在招标、评标以及其他与招标投标有关活动中从事违法行为而受过行政处罚或刑事处罚的；<br>
                （八）法律法规规定的其他情形。<br>
                二、严格执行有关招标投标的法律法规和部门规章，依照招标文件和评标办法，公正客观、独立自主
                地进行评审，对自己的评审意见负责。不对外透露对投标文件的评审和比较、中标候选人的推荐情况
                以及与评标有关的其他情况。<br>
                三、在评标过程中，严格遵守评标工作纪律，不与任何投标人或者与招标结果有利害关系的人进行私
                下接触，不收受投标人、中介人、其他利害关系人的财物或者其他好处，不向招标人征询其确定中标
                人的意向，不接受任何单位或者个人明示或者暗示提出的倾向或者排斥特定投标人的要求，不做其他
                不客观、不公正履行职务的行为。
                我将严守以上承诺，自觉接受审计机关、纪检监察机关以及其他有关部门依法实施的监督，并承担相应的法律责任。
            </div>
            <p>承诺人：${user.name!}<span>日期：${now.year}年${now.monthValue}月${now.dayOfMonth}日</span></p>
        </div>
    </div>
    <div class="inp">
        <input type="radio" name="read" id="read" class="my-agree-cb">我已阅读同意遵守《专家评标诚信廉政承诺书》
    </div>
    <div class="btns site-demo-button" id="layerDemo">
        <span class="gray-b agree-btn" onclick="agreeBook()">同意并遵守</span>
        <span class="yellow-b layui-btn" onclick="expertAvoid()">我要回避</span>
    </div>
</section>

<script>
    $(function () {
        readCountDown();
    });

    /**
     * 阅读倒计时
     */
    function readCountDown() {
        var num = 9;
        var countDownInterval = setInterval(function () {
            if (num > 0) {
                $(".agree-btn").text(num + "同意并遵守");
                num--;
            } else {
                window.clearInterval(countDownInterval);
                $(".agree-btn").text("同意并遵守").removeClass("gray-b").addClass("green-b");
            }
        }, 1000);
    }

    /**
     * 专家回避
     */
    function expertAvoid() {
        layer.open({
            type: 2,
            title: ['回避原因', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);padding:0;'],
            content: "${ctx}/selectLeader/avoidPage",
            area: ['620px', '472px'],
            shade: 0.3,//不显示遮罩
            btnAlign: 'c',
            resize: false
        });
    }

    /**
     * 同意诚信承诺书
     */
    function agreeBook() {
        if ($(".agree-btn").hasClass("gray-b")) {
            return;
        }
        if (!$(".my-agree-cb").prop('checked')) {
            layer.msg("请同意遵守《专家评标诚信廉政承诺书》", {icon: 5});
            return;
        }
        updateAgreeBook()
    }

    /**
     * 修改专家状态为已确认
     */
    function updateAgreeBook() {
        var indexLoad = window.top.layer.load(3);
        $.ajax({
            url: '${ctx}/expert/updateAgreeBook',
            type: 'post',
            cache: false,
            success: function (data) {
                layer.close(indexLoad);
                if (data) {
                    window.location.href = "${ctx}/selectLeader/selectLeaderPage";
                } else {
                    layer.msg("操作失败", {icon: 5})
                }
            },
            error: function (data) {
                console.error(data);
                layer.close(indexLoad);
                layer.msg("操作失败", {icon: 5})
            },
        });
    }

    function printThis() {
        LODOP = getLodop();
        LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_按网址打印");
        LODOP.ADD_PRINT_URL(30,20,746,"95%","${base}/expert/promisePrint");
        LODOP.SET_PRINT_STYLEA(0,"HOrient",3);
        LODOP.SET_PRINT_STYLEA(0,"VOrient",3);
        LODOP.PREVIEW();
    }

</script>

</body>

</html>

