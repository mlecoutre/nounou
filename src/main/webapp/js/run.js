(function ($) {

    /** PAGE INITIALIZATION **/
    var value = sessionStorage.getItem('apptoken');
    var accountId = null;
    var userId = null;
    if (value != null && value != "") {
        var token = $.parseJSON(value);
        accountId = token.accountId;
        userId = token.userId;
    }
    $(document).ready(function () {
        // Get Current Appointment data
        var reqUser = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/users/account/' + accountId
        });
        reqUser.done(function (users) {
            for (i = 0; i < users.length; i++) {
                $('#userId').append(Mustache.to_html($('#user-template').html(), users[i]));
                $('#editArrivalUserId').append(Mustache.to_html($('#user-template').html(), users[i]));
                $('#editDepartureUserId').append(Mustache.to_html($('#user-template').html(), users[i]));
            }
        });


        // Get Current Appointment data
        var reqChildren = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/children/account/' + accountId
        });
        reqChildren.done(function (children) {
            for (i = 0; i < children.length; i++) {
                $('#kidName').append(Mustache.to_html($('#kid-appointment-template').html(), children[i]));
                $('#editKidId').append(Mustache.to_html($('#kid-appointment-template').html(), children[i]));

            }
        });

        var reqApp = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/appointments/current/account/' + accountId + '/userId/' + userId
        });
        reqApp.done(function (appointment) {
            console.log("appointment");
            $("select[id='kidName'] option[value='"+appointment.kidId+"']").attr("selected", true);
            $('#arrivalDate').val(appointment.arrivalDate);
            $('#departureDate').val(appointment.departureDate);
            $('#userId').val(appointment.currentUserId);
            $('#declarationType').val(appointment.declarationType);
        });

        // Get Last 5 Appointments data
        var reqA = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/appointments/report/account/' + accountId + '/searchType/last',

        });
        reqA.done(function (report) {
            console.log("get last appointments");
            $('#last5').append(Mustache.to_html($('#last-appointments-template').html(), report.appointments));
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

        req.done(function (appointment) {
            //reload the location in order to reinitialize the content
            $.ajax({
                url: "",
                context: document.body,
                success: function (s, x) {
                    $(this).html(s);
                }
            });

        });
        console.log("[END] goLive");
    })

    $('#search').click(function (e) {
        var searchType = $('#searchDate').val();
        console.log("[START] search on " + searchType);
        var reqA = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/appointments/report/account/' + accountId + '/searchType/' + searchType,
        });
        reqA.done(function (report) {

            $('#appointmentResultList').html(Mustache.to_html($('#appointments-template').html(), report.appointments));

            //DELETE APPOINTMENT
            $(".del").click(function () {
                var appId = $(this).attr('data-target');
                console.log('Del appointment:' + appId);
                var reqDel = $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: '/services/appointments/delete/' + appId
                });
                reqDel.done(function (e) {
                    console.log("appointment deleted");
                    $('#search').click();

                });
            });
            //EDIT APPOINTMENT
            $(".edit").click(function () {
                var appId = $(this).attr('data-target');
                console.log('Edit appointment:' + appId);
                var reqEdit = $.ajax({
                    type: 'GET',
                    contentType: 'application/json',
                    url: '/services/appointments/' + appId
                });
                reqEdit.done(function (app) {
                    console.log("edit appointment");
                    $("select[id='editArrivalUserId'] option[value='" + app.arrivalUserId + "']").attr("selected", true);
                    $('#editArrivalDate').val(app.arrivalDate);
                    $('#editDepartureDate').val(app.departureDate);
                    $("select[id='editDepartureUserId'] option[value='" + app.departureUserId + "']").attr("selected", true);
                    $('#editKidId').val(app.kidId);
                    $('#editAppointmentId').val(app.appointmentId);

                    $('#editUpdate').show();
                });
            });
        });
    });
    $('#editCancel').click(function (e) {
          $('#editUpdate').hide();
        console.log("cancel") ;
    });

    $('#editSave').click(function (e) {

        var mAppointment = {
            appointmentId:  $('#editAppointmentId').val(),
            accountId: accountId,
            currentUserId: $('#userId').val(),
            arrivalDate: $('#editArrivalDate').val(),
            departureDate: $('#editDepartureDate').val(),
            kidId: $('#editKidId').val(),
            departureUserId: $('#editDepartureUserId').val() ,
            arrivalUserId: $('#editArrivalUserId').val()
            //TODO add notes here
        };

        var data = $.toJSON(mAppointment);

        var reqAppSave = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/services/appointments/' +  $('#editAppointmentId').val(),
            data: data,
        });
        reqAppSave.done(function (appointment) {
            console.log("Update is done.");
            $('#search').click();
            $('#editUpdate').hide();

        });

    });

})(jQuery);