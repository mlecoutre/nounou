(function ($) {

//TODO JUST FOR TEST
var accountId = 3;


Date.prototype.format = function(format) //author: meizz
{
  var o = {
    "M+" : this.getMonth()+1, //month
    "d+" : this.getDate(),    //day
    "h+" : this.getHours(),   //hour
    "m+" : this.getMinutes(), //minute
    "s+" : this.getSeconds(), //second
    "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
    "S" : this.getMilliseconds() //millisecond
}

if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
    (this.getFullYear()+"").substr(4 - RegExp.$1.length));
  for(var k in o)if(new RegExp("("+ k +")").test(format))
    format = format.replace(RegExp.$1,
      RegExp.$1.length==1 ? o[k] :
      ("00"+ o[k]).substr((""+ o[k]).length));
return format;
}

$(document).ready(function () {//debugger;
    $(document.body).bind("online", checkNetworkStatus);
    $(document.body).bind("offline", checkNetworkStatus);
    checkNetworkStatus();
    initialize();
});


function displayKids(){
    // Get Current Appointment data
    var reqChildren = $.ajax({
        type: 'GET',
        contentType: 'application/json',
        url: '/services/children/account/' + accountId
    });
    reqChildren.done(function (children) {
        $('#kids').html(Mustache.to_html($('#kid-appointment-template').html(), children));
    //selected by default  on live declaration
    $('#kids > .liveKid').each(function () {
        $(this).toggleClass('selected-kid');
    });
    //change for each click
    $('.liveKid').click(function () {
        var kidId = $(this).attr('data-target');
        console.log('click on a kid: ' + kidId);
        $(this).toggleClass('disabled-kid');
        $(this).toggleClass('selected-kid');
    });

});
}

function initialize(){
    console.log("Initialize");
    displayKids();
    var date = new Date();
    var strDate = new Date().format("dd-MM-yyyy hh:mm");
    if(date.getHours() < 12){
        $('#arrival').val(strDate);
    }else{
       $('#departure').val(strDate);
   }
}

function manageOfflineModel(){


}

function checkNetworkStatus() {
    if (navigator.onLine) {
        // Just because the browser says we're online doesn't mean we're online. The browser lies.
        // Check to see if we are really online by making a call for a static JSON resource on
        // the originating Web site. If we can get to it, we'resourcee online. If not, assume we're
        // offline.
        $.ajaxSetup({
            async: true,
            cache: false,
            context: $("#status"),
            dataType: "json",
            error: function (req, status, ex) {
                console.log("Error: " + ex);
                // We might not be technically "offline" if the error is not a timeout, but
                // otherwise we're getting some sort of error when we shouldn't, so we're
                // going to treat it as if we're offline.
                // Note: This might not be totally correct if the error is because the
                // manifest is ill-formed.
                showNetworkStatus(false);
            },
            success: function (data, status, req) {
                showNetworkStatus(true);
            },
            timeout: 5000,
            type: "GET",
            url: "/js/ping.js"
        });
        $.ajax();
    }
    else {
        showNetworkStatus(false);
    }
}

function showNetworkStatus(online) {
    if (online) {
        $("#online_status").html("<img src='/img/online.png' class='online-status' />");
    }
    else {
     $("#online_status").html("<img src='/img/offline.jpeg' class='online-status' />");

 }

 console.log("Online status: " + online);
}

})(jQuery);