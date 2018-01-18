var path = "http://evtape.cn/qr/request";

$(document).ready(function () {
    $(".reward-available").hide();
    $(".reward-sent").hide();
    $(".reward-info").hide();
    var token=getToken();
    if(!token){
        createToken();
        token=getToken();
    }
    var body = {};
    body.method = "1001-1";
    body.token = token;
    body.sn = "0";
    body.params = {};
    body.params.userToken = getQueryString("userToken");
    $.ajax({
        type: "POST",
        url: path,
        data: {body: JSON.stringify(body)},
        dataType: "json",
        success: function (data) {
            console.debug(data);
            if(data.errorcode&&data.errorcode!='0'){
                $(".reward-info").empty().append("<img src='/image/"+data.errorcode+".png' />").show();
                return ;
            }
            $(".reward-info").empty().append("<img src='/image/congrat.png' />").show();
            $(".reward-available").show();
        }
    });
});


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
                console.log(data.entity);
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