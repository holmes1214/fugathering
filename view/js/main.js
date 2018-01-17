var path = "http://evtape.cn/qr/request";

$(document).ready(function () {
    $(".char").hide();
    $(".char-x").hide();
    $(".char-x-0").hide();
    $(".char-x-desc").hide();
    $(".qr-code").hide();
    $(".qr-code-desc").hide();
    $(".qr-code-rescan").hide();
    var token=getToken();
    if(!token){
        createToken();
        token=getToken();
    }
    var body = {};
    body.method = "1000-1";
    body.token = token;
    body.sn = "0";
    body.params = {};
    body.params.qrNumber = getQueryString("qrNumber");
    $.ajax({
        type: "POST",
        url: path,
        data: {body: JSON.stringify(body)},
        dataType: "json",
        success: function (data) {
            console.debug(data);
            if(data.errorcode&&data.errorcode!='0'){
                alert(data.entity);
                return ;
            }
            if (data.entity) {
                if(data.entity.fuLists){
                    var list=data.entity.fuLists
                    for(var i=0;i<list.length;i++){
                        $(".char-"+list[i]).show();
                    }
                    if(list.length==5){
                        setTimeout(getQrCodeImg,2000);
                    }
                }
                if(data.entity.luckyDraw>=0){
                    var luckyDraw=data.entity.luckyDraw;
                    $(".char-x-"+luckyDraw).show();
                    if(luckyDraw==0){
                        $(".char-fu-center").hide();
                        $(".char-x-desc").show();
                    }
                }else if(luckyDraw==-1){
                    $(".qr-code-rescan").show();
                }
            }
        }
    });
});

function getQrCodeImg() {
    var token=getToken();
    var body = {};
    body.method = "1004-1";
    body.token = token;
    body.sn = "0";
    body.params = {};
    $.ajax({
        type: "POST",
        url: path,
        data: {body: JSON.stringify(body)},
        dataType: "json",
        success: function (data) {
            console.debug(data);
            if(data.errorcode&&data.errorcode!='0'){
                alert(data.entity)
                return ;
            }
            if (data.entity) {
                var qrPic=data.entity;
                $(".char-x-desc").hide();
                $(".qr-code").empty().append("<img src='"+qrPic+"'/>").show();
                $(".qr-code-desc").show();
            }
        }
    });
}

function getQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)","i");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  decodeURI(r[2]); return null;
}

function createToken() {
    var body = {};
    body.method = "1002-1";
    body.token = "";
    body.sn = "0";
    body.params = {};
    $.ajax({
        type: "POST",
        url: path,
        data: {body: JSON.stringify(body)},
        dataType: "json",
        success: function (data) {
            console.debug(data);
            if(data.errorcode&&data.errorcode!='0'){
                alert(data.entity)
                return ;
            }
            localStorage.setItem(path+"token",data.entity);
        }
    });
}

var sessionToken="";
function getToken() {
    if(sessionToken.length>0){
        return sessionToken;
    }
    var exp=localStorage.getItem(path+"token");
    return exp;
}