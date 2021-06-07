<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
<script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
<script src="${ctx}/js/LodopFuncs.js"></script>
<#--<link rel="stylesheet" href="${ctx}/css/printing.css">-->
<style id="css">
    section {
        width: 90%;
        margin: 0 auto;
        padding: 20px 0;
        box-sizing: border-box;
    }
    .cont {
        width: 100%;
        /*min-height: 854px;*/
        background: #ffffff;
        opacity: 1;
        border-radius: 10px;
        padding: 0 30px;
        box-sizing: border-box;
    }
    .cont h3 {
        width: 100%;
        height: 81px;
        text-align: center;
        font-size: 16px;
        font-family: "Microsoft YaHei";
        font-weight: bold;
        line-height: 81px;
        color: #1a2539;
        position: relative;
    }
    .cont h3 img {
        position: absolute;
        right: 20px;
        top: 20px;
        cursor: pointer;
    }
    .cont .title {
        width: 100%;
        min-height: 40px;
    }
    .cont .title p {
        display: inline-block;
        width: 80px;
        height: 40px;
        font-size: 14px;
        font-family: "Microsoft YaHei";
        font-weight: bold;
        line-height: 40px;
        color: #333333;
        vertical-align: middle;
    }
    .cont .title>span {
        display: inline-block;
        width: calc(100% - 90px);
        font-size: 14px;
        font-family: "Microsoft YaHei";
        font-weight: bold;
        color: #333333;
        vertical-align: middle;
    }
    .cont .title>span .tow-line {
        overflow: visible;
        word-break:break-all;
        width: 100%;
        line-height: 24px;
        max-height: 48px;
        display:-webkit-box;/**对象作为伸缩盒子模型展示**/
        -webkit-box-orient:vertical;/**设置或检索伸缩盒子对象的子元素的排列方式**/
        -webkit-line-clamp:2;/**显示的行数**/
        overflow:hidden;/**隐藏超出的内容**/
        white-space: pre-wrap;
    }
    .cont-document{
        width: 90%;
        height: 841px;
        margin: 0 auto;
    }
    .cont-document ol{
        width: 100%;
        height: 60px;
        background-color: rgba(207, 218, 229, 1);
        list-style: none;
    }
    .cont-document ol li{
        width: 25%;
        height: 60px;
        color: #333;
        font-: 16px Microsoft YaHei bold;
        text-align: center;
        float: left;
        line-height: 60px;
    }
    .cont-document .cont-table{
        width: 100%;
        height: calc(100% - 60px);
        overflow-x: hidden;
        overflow-y: auto;
    }
    .cont-table table{
        width: 100%;
    }
    .cont-table table tr{
        width: 100%;
        height: 60px;
        min-height: 60px;
    }
    .cont-table table tr td{
        width: 25%;
        height: 60px;
        min-height: 60px;
        font-size: 16px;
        font-weight: 400;
        text-align: center;
        box-sizing: border-box;
        border-bottom: 1px solid #ccc;
    }
    .foot {
        width: 100%;
        height: 36px;
        font-size: 14px;
        font-family: "Microsoft YaHei";
        font-weight: 400;
        line-height: 36px;
        color: #333333;
    }
    .foot img {
        float: left;
        margin-top: 10px;
        margin-right: 5px;
        background: url("/img/sigh.png") no-repeat;
        content: url("/img/sigh.png");
    }
</style>
<section>
    <div class="cont">
        <h3>评标专家密码打印
            <img class="printBtn" src="${ctx}/img/printing.png" alt="" onclick="printExpert()">
        </h3>
        <div class="title">
            <p>标段名称：</p><span><div class="tow-line">${bidSection.bidSectionName}</div></span>
        </div>
        <div class="title">
            <p>标段编号：</p><span><div class="tow-line">${bidSection.bidSectionCode}</div></span>
        </div>
        <div class="cont-document">
            <ol>
                <li>序号</li>
                <li>评委姓名</li>
                <li>工作单位</li>
                <li>评标密码</li>
            </ol>
            <div class="cont-table">
                <table cellpadding="0" cellspacing="0">
                <#list list as expert>
                    <tr>
                        <td>${expert_index + 1}</td>
                        <td>${expert.expertName}</td>
                        <td>${expert.company}</td>
                        <td>${expert.passWord}</td>
                    </tr>
                </#list>
                </table>
            </div>
        </div>
        <div class="foot">
            <img src="${ctx}/img/sigh.png" alt="">注意：登录账户名为专家本人身份证号或者手机号
        </div>
    </div>
</section>

<script>
    function printExpert() {
        //移除打印按钮
        $(".printBtn").remove();
        $(".foot").remove();
        var LODOP = getLodop();
        LODOP.PRINT_INIT("注意：登录账户名为专家本人身份证号或者手机号");
        LODOP.ADD_PRINT_HTM(0,0,"100%","100%",document.documentElement.innerHTML);
        //
        // LODOP.PRINT_INIT("注意：登录账户名为专家本人身份证号或者手机号");
        // LODOP.ADD_PRINT_HTM(0,0,"100%","100%",document.getElementById("form1").innerHTML);

        // LODOP.PRINT();
        LODOP.PREVIEW();
        parent.layer.closeAll();
    }
</script>
