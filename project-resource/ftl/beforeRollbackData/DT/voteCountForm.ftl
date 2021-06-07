<head>
    <title>评委投票统计表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <div class="show">
<#-- 显示的投标人数量-->
    <#assign showBidderCount = 5/>
    <#assign bidderNum =bidders?size/>
    <#assign isShow = true/>
    <#if bidderNum<showBidderCount>
        <#assign endNum=1/>
    <#elseif bidderNum%showBidderCount==0 >
        <#assign endNum=(bidderNum/showBidderCount)/>
    <#else>
        <#assign endNum=((bidderNum/showBidderCount)+1)/>
    </#if>
        <#list 0..(endNum-1) as tableNum>
            <#assign i=(tableNum*showBidderCount) j=(tableNum*showBidderCount+(showBidderCount-1))>
            <div class="panel pagination">
                <table width="100%" class="base-normal-table">
                    <thead>
                    <#assign colspanNum = 3 * 5 +1>
                    <tr>
                        <td colspan="${colspanNum}" align="center">
                            <div style="text-align: center"
                                 class="base-header-mid<#if tableNum ==0 > second-title</#if>">评委投票统计表</div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="${colspanNum}" align="left">标段名称：${bidSection.bidSectionName}</td>
                    </tr>
                    <tr>
                        <td colspan="${colspanNum}" align="right">日期：${date[0]}年${date[1]}月${date[2]}日</td>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="base-border">
                        <td width="15%" align="center">投标人名称</td>
                        <#list i..j as num>
                            <td width="14%" colspan="3" align="center">
                                <#if bidders[num]??>${bidders[num].bidderName}</#if>
                            </td>
                        </#list>
                    </tr>
                    <tr class="base-border">
                        <td align="center">名次</td>
                        <#list i..j as num>
                            <#if bidders[num]>
                                <td align="center">1</td>
                                <td align="center">2</td>
                                <td align="center">3</td>
                            <#else >
                                <td align="center"></td>
                                <td align="center"></td>
                                <td align="center"></td>
                            </#if>
                        </#list>
                    </tr>
                    <tr class="base-border">
                        <td align="center">得票数</td>
                        <#assign count = 3>
                        <#list i..j as num>
                                <td align="center">${bidders[num].oneBidderVotes}</td>
                                <td align="center">${bidders[num].twoBidderVotes}</td>
                                <td align="center">${bidders[num].threeBidderVotes}</td>
                                <#assign count-=3 >
                        </#list>
                        <#--补齐td-->
                        <#if count gt 0 >
                            <td></td>
                            <td></td>
                            <td></td>
                        </#if>
                    </tr>
                    </tbody>
                </table>
            </div>
        </#list>
        </div>
</#escape>