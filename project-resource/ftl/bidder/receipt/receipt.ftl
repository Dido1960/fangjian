<head>
    <title>文件上传回执单</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css">
</head>
<#escape x as x!"">
    <div class="show">
        <div class="panel">
            <table width="100%" class="base-normal-table">
                <thead>
                <tr>
                    <td colspan="6" style="text-align: right">
                        <#if bidderFileInfo.gefId?? && bidderFileInfo.gefId != ''>
                            <img width="70px" height="70px" src="${gefQrPathtoPdf}">
                        </#if>
                        <img width="70px" height="70px" src="${sgefQrPathtoPdf}">
                    </td>
                </tr>
                <tr>
                    <td colspan="6" style="text-align: right">
                        <p style="font-size: 10px">请扫码查看区块链信息</p>
                    </td>
                </tr>
                <tr>
                    <td colspan="6">
                        <div class="base-header first-title">文件上传回执单</div>
                    </td>
                </tr>
                <tr>
                    <td colspan="6">标段名称：${bidSection.bidSectionName}</td>
                </tr>
                <tr>
                    <td colspan="6">
                        <div style="width: 50%;float: left">标段编号：${bidSection.bidSectionCode }</div>
                    </td>
                </tr>
                </thead>
                <tbody>
                <tr class="base-border" style="">
                    <td colspan="6">
                        <p>${bidder.bidderName}:</p>
                        <p style="text-indent: 2em">你单位已在网上开标大厅上传招标标段名称：${bidSection.bidSectionName}(标段编号：${bidSection.bidSectionCode})的投标文件，具体情况如下：</p>
                        <p style="text-indent: 2em">投标文件已于${bidderOpenInfo.upfileTime}成功上传。</p>
                        <#if bidderFileInfo.gefId?? && bidderFileInfo.gefId != ''>
                            <p style="text-indent: 4em"><#if bidSection.paperEval?? && bidSection.paperEval == "1">YCK<#else>GEF</#if>文件:${gefFile.name}</p>
                            <p style="text-indent: 4em">文件大小:${gefFile.readSize}</p>
                            <p style="text-indent: 4em"><#if bidSection.paperEval?? && bidSection.paperEval == "1">YCK<#else>GEF</#if>文件HASH值:</p>
                            <p style="text-indent: 5em">${bidderFileInfo.gefHash}</p>
                        </#if>
                        <p style="text-indent: 4em"><#if bidSection.paperEval?? && bidSection.paperEval == "1">SYCK<#else>SGEF</#if>文件:${sgefFile.name}</p>
                        <p style="text-indent: 4em">文件大小:${sgefFile.readSize}</p>
                        <p style="text-indent: 4em"><#if bidSection.paperEval?? && bidSection.paperEval == "1">SYCK<#else>SGEF</#if>文件HASH值:</p>
                        <p style="text-indent: 5em">${bidderFileInfo.sgefHash}</p>
                        <#if bidderFileInfo.certId??>
                            <p style="text-indent: 4em">加密数字证书锁号：${bidderFileInfo.certId}</p>
                        </#if>
                        <p>温馨提示：</p>
                        <p>此回执作为网上成功上传的依据，请务必打印并保存</p>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</#escape>