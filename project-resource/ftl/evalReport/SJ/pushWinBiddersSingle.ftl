<head>
    <title>评委推荐表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <link rel="stylesheet" href="css/base.css"/>
</head>
<#escape x as x!"">
    <div class="show">
<#-- 显示的投标人数量-->
    <#assign showBidderCount = 7/>
    <#assign bidderNum =listCandidateResults?size/>
    <#assign isShow = true/>
    <#if bidderNum<showBidderCount>
        <#assign endNum=1/>
    <#elseif bidderNum%showBidderCount==0 >
        <#assign endNum=(bidderNum/showBidderCount)/>
    <#else>
        <#assign endNum=((bidderNum/showBidderCount)+1)/>
    </#if>
    <#list expertUsers as expert>
            <div class="panel pagination">
                <table width="100%" class="base-normal-table">
                    <thead>
                    <tr>
                        <td colspan="4" align="left">
                            评标报告附件六
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" align="center">
                            <div style="text-align: center"class="base-header-mid<#if expert_index ==0> first-title</#if>">评委推荐表</div>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="4" align="left">标段名称：${bidSection.bidSectionName}</td>
                    </tr>
                    <tr>
                        <td colspan="4" align="left">
                            <div style="width: 79%; float: left; text-align: left">标段编号：${bidSection.bidSectionCode}</div>
                            <div style="width: 20%; float: left; text-align: right">日期：${date[0]}年${date[1]}月${date[2]}日</div>
                        </td>
                    </tr>
                    <tr class="base-border">
                        <td rowspan="2" style="width: 10%;text-align: center">名次</td>
                        <td rowspan="2" style="width: 30%;text-align: center">
                            第一名
                        </td>
                        <td rowspan="2" style="width: 30%;text-align: center">
                            第二名
                        </td>
                        <td rowspan="2" style="width: 30%;text-align: center">
                            第三名
                        </td>
                    </tr>
                    </thead>
                    <tbody>
                    <tr class="base-border">
                        <#--推荐投标人-->
                        <#assign oneBidder ="" >
                        <#assign twoBidder ="" >
                        <#assign threeBidder ="" >
                        <#--推荐投标人意见-->
                        <#assign oneBidderIdea ="" >
                        <#assign twoBidderIdea ="" >
                        <#assign threeBidderIdea ="" >

                        <#list listCandidateResults as result>
                            <#if expert.id == result.expertId>
                                <#if result.ranking == 1>
                                    <#assign oneBidder = result.bidderName/>
                                    <#assign oneBidderIdea = result.reason/>
                                <#elseif result.ranking == 2>
                                      <#assign twoBidder = result.bidderName/>
                                      <#assign twoBidderIdea = result.reason/>
                                <#elseif result.ranking == 3>
                                    <#assign threeBidder = result.bidderName/>
                                    <#assign threeBidderIdea = result.reason/>
                                </#if>
                            </#if>
                        </#list>
                        <td>投标人名称</td>
                        <td>
                            ${oneBidder}
                        </td>
                        <td>
                            ${twoBidder}
                        </td>
                        <td>${threeBidder}</td>
                    </tr>
                    <tr class="base-border">
                        <td>评审意见</td>
                        <td align="center">${oneBidderIdea}</td>
                        <td align="center">${twoBidderIdea}</td>
                        <td align="center">${threeBidderIdea}</td>
                    </tr>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="4" style="padding-top: 20px;">
                                <div style="width: 79%; float: left; text-align: left">评审专家 ${expert.expertName} 签字：</div>
                                <div style="width: 20%; text-align: left">监标人签字：</div>
                            </td>
                        </tr>
                    </tfoot>
                </table>
            </div>
    </#list>
</#escape>