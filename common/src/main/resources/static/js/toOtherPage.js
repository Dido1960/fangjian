function otherPage(e) {
    var text = $(e).children("div").find("span").text();
    if(text==="法人信息"){
        window.location.href="/legalPerson/updateLegalPage";
    }else if(text==="基本信息"){
        window.location.href="/companyInfo/companyInfoPage";
    }else if(text==="发票信息"){
        window.location.href="/invoiceInfo/invoiceInfoPage";
    }else if(text==="申办人信息"){
        window.location.href="/sponsor/updateSponsorPage";
    }else if (text==="订单列表"){
        window.location.href="/order/orderListPage";
    }else if (text==="充值记录"){
        window.location.href="/web/onlineRecharge/pagedRecharge";
    }

}

function toMainInfo() {
    window.location.href="/companyInfo/showCompanyPage";
}

function toLegal() {
    window.location.href="/legalPerson/showLegalPage";
}

function toInvoice() {
    window.location.href="/invoiceInfo/showInvoicePage";
}

function toSponsor() {
    window.location.href="/sponsor/showSponsorPage";
}

function toCompanyInfo() {
    window.location.href="/companyInfo/companyInfoPage";
}

function toUpdateLegal() {
    window.location.href="/legalPerson/updateLegalPage";
}

function toUpdateInvoice() {
    window.location.href="/invoiceInfo/invoiceInfoPage";
}

function toUpdateSponsor() {
    window.location.href="/sponsor/updateSponsorPage";
}

function togenerateOrder(){
    window.location.href="/order/orderListPage";
}

function toCartPages() {
    window.location.href="/goodsCart/cartPage";
}

/**
 * 跳转到指定URL
 * @param targetURL
 */
function toTargetUrl(targetURL) {
    window.location.href=targetURL;
}