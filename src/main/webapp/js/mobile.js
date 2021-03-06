(function ($) {

var token = null;

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

/** DOCUMENT READY - INITIALIZATION **/
$(document).ready(function () {
    $('#status').checkNetworkStatus();
    token = $(this).getToken('nounou-auth');
    if ( token != null ){
        console.log("User authentified "+ token.userName);
    } else {
        console.log("Please log first");
        return ;
    }
    initialize( token );
});

function initialize(token){
    displayKids(token);
    fillAppointmentForm(token);
    displayLocalAppointments(token);
}

function displayKids(token){
    // Get Current kids data from localStorage
    var data = localStorage.getItem("nounou-config");
    var children = $.parseJSON(data);
    $('#kids').html(Mustache.to_html($('#kid-appointment-template').html(), children));

    //selected by default on live declaration
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
}

/**
 * Show local appointements locally stored
 */
function displayLocalAppointments(token){
     var data = localStorage.getItem("nounou-appointments");
     if ( data == null ){
        $('#appointments').html("No local appointments. Everything sync with the server.");
     }else{
        var appointments = $.parseJSON(data);
        var parameters ={
            length: appointments.length,
            appointments: appointments
        }
        $('#appointments').html(Mustache.to_html($('#local-appointments-template').html(), parameters));
        $('#appointments').effect("bounce", { direction:'down', times:5 }, 500);
    }
}

/**
 * Initialize the  appointment form
 */
function fillAppointmentForm(token){
    $('#titleUser').append(token.userName);
    var date = new Date();
    //TODO check if an existing appointment is open at this date.

   
     var strDate = new Date().format("dd-MM-yyyy hh:mm");
    if(date.getHours() < 12){
        $('#arrival').val(strDate);
    }else{
       $('#departure').val(strDate);
   }
}

$('#push').click(function(){
    console.log("push appointment");
    var kids = [];
    $("#kids > .selected-kid").each(function (i, value) {
        var selKidId = $(this).attr("data-target");
        kids[i] = selKidId;
    });
    var mAppointment = {
            appointmentId: null,
            accountId: token.accountId,
            currentUserId: token.userId,
            arrivalDate: $('#arrival').val(),
            departureDate: $('#departure').val(),
            kidIds: kids,
    };

     var data = localStorage.getItem("nounou-appointments");
     var length = 0;
     var appointments = [];
     if ( data != null ){
        appointments = $.parseJSON(data);
        length = appointments.length;
    }
    appointments[length] =  mAppointment;
    localStorage.setItem("nounou-appointments", $.toJSON(appointments));
    displayLocalAppointments();
});

/**
 * Allow to synchronize appointments with the server
 */
$('#synchronize').click(function(){
    $( "#progressbar" ).progressbar({
            value: 0
    });
    var data = localStorage.getItem("nounou-appointments");
    var length = 0;
    var appointments = [];
    if ( data != null ){
        appointments = $.parseJSON(data);        
        length = appointments.length;
    }
    sessionStorage.setItem("nounou-local-app", length - 1);
    for (var i = appointments.length - 1; i >= 0; i--) {
        var data   = $.toJSON(appointments[i]);
        var appUrl = '/services/appointments';
        var appId  =  appointments[i].appointmentId;

        if (appId != null) {
            appUrl = appUrl + '/' + appId;
        }
        var req = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: appUrl,
            dataType: "json",
            data: data,
        });
        req.done(function () {
           var i = sessionStorage.getItem("nounou-local-app");
           console.log("Appointment "+i+'/'+appointments.length +' synchronized');
           var percent = ((appointments.length - i) / appointments.length) * 100;
           console.log("Set progress bar to " + percent);
            $( "#progressbar" ).progressbar({
                value: percent
            });
            sessionStorage.setItem("nounou-local-app", i - 1);
            //Once finished
            if(i == 0){
                //clear local appointments
                localStorage.removeItem("nounou-appointments");
                displayLocalAppointments();
            }
        });
    };
});

})(jQuery);