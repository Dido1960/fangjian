<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>人员查看</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="_csrf" content="${_csrf.token}"/>
    <meta name="_csrf_parameterName" content="${ _csrf.parameterName}"/>
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
        .layui-form-item{
            margin-right: 20px;

        }
        .layui-form-label{
            margin-left: -22px;
        }
        .photo{
            display: flex;
        }
        .photo div{
            margin-left: 30px;
            margin-right: 25px;
        }

    </style>
</head>
<body>
<fieldset class="layui-elem-field layui-field-title" style="margin-top: 20px;">
    <legend>人员查看</legend>
</fieldset>


<div class="layui-tab layui-tab-brief" lay-filter="table_box">
    <ul class="layui-tab-title">
        <li class="layui-this">基本信息</li>
        <li>法人信息</li>
        <li>发票信息</li>
        <li>申办人信息</li>
    </ul>
    <div class="layui-tab-content">
        <div class="layui-tab-item layui-show">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="layui-form-item" style="margin-top: 20px">
                    <label class="layui-form-label">主体名称</label>
                    <div class="layui-input-block" style="margin-left: 120px">
                        <input type="text" name="userName" id="user-name" value="${companyUser.name!}" lay-verify="required"
                               autocomplete="off" class="layui-input" readonly>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">登录名</label>
                    <div class="layui-input-block" style="margin-left: 120px">
                        <input type="text" name="loginName" id="login-name" value="${companyUser.loginName!}" lay-verify="required"
                               autocomplete="off" class="layui-input" readonly>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label" style="width: auto">统一社会信用代码</label>
                    <div class="layui-input-block" style="margin-left: 120px">
                        <input type="text" name="regName" id="reg-name" value="${companyUser.code!}" lay-verify="required"
                               autocomplete="off" class="layui-input" readonly>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">手机号码</label>
                    <div class="layui-input-block" style="margin-left: 120px">
                        <input type="text" name="phone" id="phone" value="${companyUser.phone!}" lay-verify="required"
                               autocomplete="off" class="layui-input" readonly>
                    </div>
                </div>
                <div class="layui-form-item">
                    <label class="layui-form-label">企业所在地</label>
                    <div class="layui-input-block" style="margin-left: 120px">
                        <input type="text" name="roleName" id="roleName" value="${companyUser.localDetail!}" lay-verify="required"
                               autocomplete="off" class="layui-input" readonly>
                    </div>
                </div>
            <div class="layui-form-item">
                <label class="layui-form-label">详细地址</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="roleName" id="roleName" value="${companyUser.address!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">信用代码扫描件</label>
                <div class="photo">
                        <#if companyUser.fileUrls!>
                            <#list companyUser.fileUrls as url>
                                <div>
                                    <img src="${url}"  width="160px" height="160px">
                                </div>
                            </#list>
                        </#if>
                </div>
            </div>
    </div>

        <div class="layui-tab-item">
            <div class="layui-form-item" style="margin-top: 20px">
                <label class="layui-form-label">法人姓名</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="userName" id="user-name" value="${legal.lname!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">邮政编号</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="loginName" id="login-name" value="${legal.code!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label" style="width: auto">法人通讯地址</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="regName" id="reg-name" value="${legal.address!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">企业法人手机号</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="phone" id="phone" value="${legal.phone!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">法人身份证</label>
                <div class="photo">
                    <#if legal.idcardFUrl??>
                        <div>
                            <img src="${legal.idcardFUrl}" width="160px" height="160px">
                        </div>
                    <#else >
                        <div></div>
                    </#if>
                    <#if legal.idcardBUrl??>
                        <div>
                            <img src="${legal.idcardBUrl}" width="160px" height="160px">
                        </div>
                    <#else >
                        <div></div>
                    </#if>
                </div>
            </div>

        </div>
        <div class="layui-tab-item">
            <div class="layui-form-item" style="margin-top: 20px">
                <label class="layui-form-label">纳税人识别号</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="userName" id="user-name" value="${invoiceInfo.name!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">企业电话</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="loginName" id="login-name" value="${invoiceInfo.phone!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">开户行</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="regName" id="reg-name" value="${invoiceInfo.bank!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">开户行账号</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="phone" id="phone" value="${invoiceInfo.bankId!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">开户许可证扫描件</label>
                <div class="photo">
                    <#if invoiceInfo.idcardFUrl??>
                        <div>
                            <img src="${invoiceInfo.idcardFUrl}" width="160px" height="160px">
                        </div>
                    <#else >
                        <div></div>
                    </#if>
                    <#if invoiceInfo.idcardBUrl??>
                        <div>
                            <img src="${invoiceInfo.idcardBUrl}" width="160px" height="160px">
                        </div>
                    <#else >
                        <div></div>
                    </#if>
                </div>
            </div>

        </div>
        <div class="layui-tab-item">
            <div class="layui-form-item" style="margin-top: 20px">
                <label class="layui-form-label">申报责任人</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="userName" id="user-name" value="${sponsor.sname!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">联系电话</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="loginName" id="login-name" value="${sponsor.phone!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>

            <div class="layui-form-item">
                <label class="layui-form-label">联系地址</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="loginName" id="login-name" value="${sponsor.address!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">邮政编码</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="phone" id="phone" value="${sponsor.code!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">电子邮箱</label>
                <div class="layui-input-block" style="margin-left: 120px">
                    <input type="text" name="roleName" id="roleName" value="${sponsor.mail!}" lay-verify="required"
                           autocomplete="off" class="layui-input" readonly>
                </div>
            </div>
        </div>
</div>
<script type="text/javascript">
    $(function () {
        layui.use('form','element', function () {
            var form = layui.form;
            var element = layui.element;
            element.render();
            form.render();
        });

    })


    $(function () {
        checkColor();
    })
    function checkColor() {
        $("input:text").addClass("layui-bg-gray");
    }
</script>
</body>
</html>