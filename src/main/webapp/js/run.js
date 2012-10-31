(function ($) {
    var userId=1;
    var accountId=1;

    /** PAGE INITIALIZATION **/
    $(document).ready(function () {
        // Get Current Appointment data
        var reqKid = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/appointments/current/account/'+accountId+'/userId/'+userId,
            //       async: false
        });
        reqKid.done(function (appointment) {
            console.log("appointment");
            $('#userId').append(Mustache.to_html($('#live-appointment-template').html(), appointment));
            $('#kidName').append(Mustache.to_html($('#kid-appointment-template').html(), appointment));

            $('#arrivalDate').val(appointment.arrivalDate);
            $('#departureDate').val(appointment.departureDate);
            $('#kidName').val(appointment.kidName);
            $('#userId').val(appointment.currentUserId);
            $('#declarationType').val(appointment.declarationType);
        });

        // Get Last 5 Appointments data
        var reqA = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/appointments/account/'+accountId+'/searchType/last',

        });
        reqA.done(function (appointments) {
            console.log("get last appointments");
            for (i = 0; i < appointments.length; i++) {
                $('#lastAppointments').append(Mustache.to_html($('#last-appointments-template').html(), appointments[i]));
            }
        });
    });

    /** PAGE NAVIGATION **/
    $('#runTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    })

    /** PAGE ACTIONS **/
    $('#goLive').click(function (e) {
        console.log("[START] goLive");
        var mAppointment = {
            accountId: accountId,
            currentUserId: $('#userId').val(),
            arrivalDate: $('#arrivalDate').val(),
            departureDate: $('#departureDate').val(),
            kidId: $('#kidName').val(),
            declarationType: $('#declarationType').val(),
            //TODO add notes here
        };

        var data = $.toJSON(mAppointment);

        var req = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/services/appointments',
            dataType: "json",
            data: data,
        });

        req.done(function (user) {
            //update last appointment list
            //       $('#whiteAccessList').append(Mustache.to_html($('#whiteAccesUser-template').html(), user));
        });

        console.log("[END] goLive");
    })



})(jQuery);