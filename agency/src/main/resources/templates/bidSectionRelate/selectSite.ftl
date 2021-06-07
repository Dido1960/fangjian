<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>选择场地</title>
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
    <link rel="stylesheet" href="${ctx}/layuiAdmin/layui/css/layui.css" media="all">
    <script type="text/javascript" src="${ctx}/layuiAdmin/layui/layui.all.js"></script>
    <style>
        #layerDemoauto {
            overflow-y: hidden;
        }

        html .layui-layer-title {
            padding: 0;
            border-bottom: 1px solid rgba(213, 213, 213, 1);
        }

        .tan {
            width: 80%;
            display: flex;
            justify-content: space-around;
            margin: 51px auto 0;
            line-height: 40px;
            font-weight: 900;
        }

        #test1,
        #test2 {
            width: 160px;
            height: 40px;
            background: rgba(255, 255, 255, 1);
            opacity: 1;
        }

        html .layui-laydate .layui-this {
            background: linear-gradient(263deg, rgb(19, 97, 254) 0%, rgb(78, 138, 255) 100%) !important;
            filter: progid:DXImageTransform.Microsoft.gradient(GradientType=1 ,startColorstr=#1361fe, endColorstr=#4e8aff) !important;
        }

        .layui-laydate-header i:hover,
        .layui-laydate-header span:hover {
            color: rgba(19, 97, 254, 1);
        }

        .laydate-time-list li ol {
            overflow: auto;
        }

        .laydate-time-list li ol::-webkit-scrollbar {
            display: none
        }

        .layui-layer-btn .layui-layer-btn0 {
            background-color: rgba(19, 97, 254, 1);
        }

        .layui-layer-btn,
        .layui-layer-btn-c {
            background: rgba(239, 243, 244, 1);
        }

        html .layui-form-select dl {
            height: 142px;
        }

        html .layui-anim::-webkit-scrollbar,
        html .layui-anim-upbit::-webkit-scrollbar {
            display: none
        }
    </style>
</head>
<body>
<form class="layui-form" method="post" id="dep-form">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    <input type="hidden" name="bidSectionId" value="${bsr.bidSectionId}">
    <input type="hidden" name="id" value="${bsr.id}">
    <div class="layui-form-item" style="margin-top: 40px">
        <label class="layui-form-label">主场地区</label>
        <div class="layui-input-block" style="width: 450px">
            <input type="text" id="reg-name" lay-verify="required"
                   class="layui-input" value="${homeReg.regName}" readonly>
            <input type="hidden" name="regId" value="${homeReg.id}">
        </div>
    </div>
    <div class="layui-form-item" style="margin-top:20px">
        <label class="layui-form-label">
            主场场地
        </label>
        <div class="layui-input-block" style="width: 450px">
            <select name="homeEvalSite" id="homeEvalSite" lay-verify="required">
                <option value=""></option>
                <#list homeSiteList as homeSite>
                    <option value="${homeSite.id}">${homeSite.name}</option>
                </#list>
            </select>
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">
            客场地区
        </label>
        <div class="layui-input-block" id="awayRegDiv" style="width: 450px">
            <select name="awayRegId" id="awayRegId" lay-verify="required" lay-filter="awayRegId">
                <option value=""></option>
                <#list awayRegList as awayReg>
                    <#if awayReg.parentId=-1>
                        <option value="${awayReg.id}">${awayReg.regName}</option>
                        <#else >
                            <option value="${awayReg.id}" >${awayReg.regName}</option>
                    </#if>
                </#list>
            </select>
        </div>
    </div>
    <div class="layui-form-item" style="margin-top: 20px">
        <label class="layui-form-label">客场场地</label>
        <div class="layui-input-block" style="width: 450px">
            <select name="awayEvalSite" id="awayEvalSite" lay-verify="required">
                <option value=""></option>
            </select>
        </div>
    </div>
    <div class="layui-input-block" style="margin-left: 120px;display: none">
        <button lay-submit lay-filter="*" id="formBtnSubmit">提交</button>
    </div>
</form>

<script>
    function getAwaySiteByRegId(regId) {
        $.ajax({
            url: "${ctx}/bidSectionRelate/getAwaySiteByRegId",
            type: "POST",
            cache: false,
            async:false,
            data:{regId:regId},
            success: function (data) {
                if (data) {
                    var str ="<option value=''></option>";
                    for (var i=0;i<data.length;i++){
                        str +="<option value="+data[i].id+" >"+data[i].name+"</option>";
                    }
                    $("#awayEvalSite option").remove();
                    $("#awayEvalSite").append(str);
                    layui.use('form', function(){
                        var form = layui.form
                        form.render('select');
                    });

                }

            },
            error:function (e) {
                console.error(e);
            }
        });
    }

    //添加
    function addBsr() {
        $.ajax({
            url: '${ctx}/bidSectionRelate/addBidSectionRelate',
            type: 'post',
            cache: false,
            async: false,
            data: $("#dep-form").serialize(),
            success: function (data) {
                if (!isNull(data)) {
                    data = JSON.parse(data);
                }
                layer.msg("选择成功！",2000,successFunc());
            },
            error: function (data) {
                console.error(data);
                layer.msg("选择失败！");
            },
        });
    }

    //修改
    function updateBsr() {
        $.ajax({
            url: '${ctx}/bidSectionRelate/updateBidSectionRelate',
            type: 'post',
            cache: false,
            async: false,
            data: $("#dep-form").serialize(),
            success: function (data) {
                if (!isNull(data)) {
                    data = JSON.parse(data);
                }
                layer.msg("选择成功！",2000,successFunc());
            },
            error: function (data) {
                console.error(data);
                layer.msg("选择失败！");
            },
        });
    }
</script>
<script type="text/javascript">
    var successFunc;
    var lock = false;
    layui.use(['form', 'layer'], function () {
        var form = layui.form;

        //如果是修改，加载所有列表数据，并初选
        if('${bsr.id}'!=null&&'${bsr.id}'!=""){
            getAwaySiteByRegId('${bsr.awayRegId}');

            $("#homeEvalSite").val('${bsr.homeEvalSite}');
            $("#awayRegId").val('${bsr.awayRegId}');
            $("#awayEvalSite").val('${bsr.awayEvalSite}');
        }


        //监听客场选择
        form.on('select(awayRegId)', function(data){
            console.log(data);
            $("#awayRegDiv").find("input").eq(0).text()
            getAwaySiteByRegId(data.value);
        });

        form.on('submit(*)', function (data) {
            if (lock) {
                return false;
            }
            lock = true;
            //判断是更新还是新增
            if('${bsr.id}'==null||'${bsr.id}'==""){
                addBsr()
            }else{
                updateBsr()
            }
        });
        form.render();
    });

    /**
     * 新增部门
     *
     * @param successFunc1 成功回调函数
     */
    function selSuccess(successFunc1) {
        successFunc = successFunc1;
        $("#formBtnSubmit").trigger("click");
    }
</script>
</body>
</html>