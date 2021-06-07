<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="utf-8">
    <title>甘肃省市政房建电子开标辅助系统</title>
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
    <link rel="stylesheet" href="${ctx}/css/publishBidder.css">
    <style>
        .caption li:first-child,.sects li>div:first-child{
            width: 10%;
        }
        .caption li:not(:first-child),.sects li>div:not(:first-child) {
            width: 18%;
        }

    </style>
</head>
<body>
<div class="head">当前共有投标人： ${bidderCount}家
    <span onclick="window.top.switchPublishBidderStatus(2)">结束检查</span>
</div>
<ul class="caption">
    <li>序号</li>
    <li>投标人名称</li>
    <li>文件上传情况</li>
    <li>签到情况</li>
    <li>文件是否递交</li>
    <li>操作</li>
</ul>

<ul class="sects">
    <#if allBidders??>
        <#list allBidders as bidder>
            <li>
                <div class="index">
                    <p>${bidder_index + 1}</p>
                </div>
                <div class="name">
                    <p>${bidder.bidderName!}</p>
                </div>
                <div class="case">
                    <#if bidder.bidDocId?if_exists>
                        <span class="green">已上传</span>
                    <#else >
                        <span class="red">未上传</span>
                    </#if>
                </div>

                <#if bidder.bidderOpenInfo.signinTime?if_exists>
                    <div class="case">
                        <span class="green">已签到</span>
                    </div>
                <#else >
                    <div class="case">
                        <span class="yellow">未签到</span>
                    </div>
                </#if>

                <#assign notCheckin = bidder.bidderOpenInfo.notCheckin>
                <#if notCheckin == 3>
                    <div class="submit">
                        <span class="gray site-demo-button unDeterminePush" id="layerDemo" style="margin: 0; margin-right: 10px;display: none">
                            <button data-method="offset" data-type="auto"class="layui-btn layui-btn-normal">未递交</button>
                        </span>
                        <span class="blove determinePush" style="margin: 0;pointer-events:none;">已递交</span>
                    </div>
                    <div class="doing">
                        <span class="lan revoke" onclick="revokeOperate(this)">撤销</span>
                    </div>
                <#elseif  notCheckin == 1 || notCheckin == 2 || notCheckin == 9 >
                    <div class="submit">
                        <span class="gray site-demo-button unDeterminePush" id="layerDemo" style="margin: 0; margin-right: 10px;pointer-events:none;">
                            <button class="layui-btn layui-btn-normal unDeterminePush">未递交</button>
                        </span>
                        <span class="blove determinePush" style="margin: 0;display: none">已递交</span>
                    </div>
                    <div class="doing">
                        <span class="lan revoke" onclick="revokeOperate(this)">撤销</span>
                    </div>
                <#else>
                    <div class="submit">
                        <span class="blove site-demo-button unDeterminePush" onclick="unPushOperate(this)" id="layerDemo" style="margin: 0; margin-right: 10px;">
                            <button class="layui-btn layui-btn-normal" style="color: rgba(19, 97, 254, 1);">未递交</button>
                        </span>
                        <span class="blove determinePush" style="margin: 0;" onclick="pushOperate(this)">已递交</span>
                    </div>
                    <div class="doing">
                        <span class="hui revoke" style="pointer-events:none;">撤销</span>
                    </div>
                </#if>
                <#--投标人主键id -->
                <input type="hidden" name="biderId" value="${bidder.id!}">
                <#--投标人开标信息主键id -->
                <input type="hidden" name="id" value="${bidder.bidderOpenInfo.id!}">
            </li>
        </#list>

    </#if>
</ul>
<div class="kong"></div>

<!--保存总记录数-->
<input type="hidden" id="bidder_total">
<#--设置自定义分页-->
<div id="page-temp"></div>
<div class="kong"></div>

<div class="tan" style="display: none; z-index: 99999;">
    <form action="">
        <input type="radio" name="why" value="1">迟到
        <input type="radio" name="why" value="2">弃标
        <input type="radio" name="why" value="9" checked>其他
    </form>
    <div class="foot">
        <span class="yes" onclick="unPushSureBtn()">确定</span>
        <span class="no" onclick="layer.closeAll();">取消</span>
    </div>
</div>

<script type="text/javascript">
    //标段id
    var bidSectionId = '${bidSection.id}';
    //当前点击的未递交按钮
    var obj;

    var laypage, layer, laytpl, $, form;
    /**
     * 初始化layui
     */
    layui.use(['layer', 'form', 'element', 'laytpl', 'laypage'], function () {
        layer = layui.layer;
        form = layui.form;
        laypage = layui.laypage;
        laytpl = layui.laytpl;
        $ = layui.jquery;

        form.render();
    });

    /**
     *   保存未递交原因
     *   @param id 开标记录表id
     *   @param notCheckin 未递交状态
     *   @param value 未递交原因
     *   @param isPassBidOpen 是否通过开标
     */
    function sureOrRevokePush(id, notCheckin, value, isPassBidOpen) {
        var index = layer.msg("数据加载中...", {icon: 16, time: 0, shade: 0.3});
        var flag = false;
        $.ajax({
            url: '${ctx}/staff/updateBidderInfo',
            type: 'POST',
            cache: false,
            async: false,//这里得用同步
            data: {
                id: id,
                notCheckin: notCheckin,
                notCheckinReason: value,
                isPassBidOpen: isPassBidOpen,
            },
            success: function (data) {
                flag = data;
                if (data) {
                    layer.msg("操作成功！", {icon: 1});
                    setTimeout(function () {
                        window.location.reload();
                    },1000);
                } else {
                    layer.msg("操作失败！", {icon: 5});
                }
                layer.close(index)
            },
            error: function (data) {
                layer.close(index);
                layer.msg("操作失败！", {icon: 5});
                console.error(data);
                if(data.status == 403){
                    console.warn("用户登录失效！！！")
                    window.top.location.href = "/login.html";
                }

            },
        });

        return flag;

    }

    /**
     *   撤销
     */
    function revokeOperate(obj) {
        var id = $(obj).parents('li').children('input[name=id]').val();
        //除1，2，3，9外的数字
        sureOrRevokePush(id, 0, null, 1);
    }

    /**
     *   已递交
     */
    function pushOperate(obj) {
        var id = $(obj).parents('li').children('input[name=id]').val();
        window.top.layer.confirm('确认该投标人已递交吗?', {
            icon: 3,
            title: '提示'
        }, function (index) {
            window.top.layer.close(index);
           sureOrRevokePush(id, 3, null, 1);//3已递交
        });

    }



    /**
     *   未递交窗口
     */
    function unPushOperate(e) {
        var boiId = $(e).parents('li').children('input[name=id]').val();
        obj = $(e);
        window.top.layer.open({
            type: 2,
            title: ['未递交原因', 'height:57px;text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);line-height:57px;padding:0;'],
            content: '${crx}/staff/unPushPage?boiId='+boiId,
            area: ['400px', '275px'],
            //btn: ['确认', '取消'],
            btnAlign: 'c', //按钮居中
            shade: 0.3, //不显示遮罩
            btn1: function () {
            }
        });
    }

    /**
     * 未递交确认操作
     */
    function unPushSureBtn() {
        layer.closeAll();
        var notCheckin = $('input[type="radio"]:checked').val();
        if ($('input:radio[value="9"]:checked').val()) {
            layer.prompt({
                formType: 2,
                title: ['其它原因', 'text-align:center;font-weight:900;font-size:16px;color:rgba(34,49,101,1);'],
                area: ['400px', '150px'],
                btn: ['确认', '取消'],
                btnAlign: 'c',
                // 其他原因字符限制100字
                maxlength: 100,
            }, function (value, index) {
                layer.close(index)
                unPush(bidOpenInfoId, notCheckin, value, obj);
            });
        } else {
            unPush(bidOpenInfoId, notCheckin, null, obj);
        }
    }
</script>
</body>
