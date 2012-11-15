(function ($) {

    /**  Utilities functions  **/

    function contains(a, obj) {
        var i = a.length;
        while (i--) {
            if (a[i] == obj) {
                return true;
            }
        }
        return false;
    }

    var last5 = function () {
        if (accountId == null) return;
        // Get Last 5 Appointments data
        var reqA = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/appointments/report/account/' + accountId + '/searchType/last',
        });
        reqA.done(function (report) {
            $('#last5').html(Mustache.to_html($('#last-appointments-template').html(), report.appointments));
            $('.last5-template').i18n();
        });
    }

    var initAppointment = function () {
          if (accountId == null) return;
        // Initialize an appointment or get an open appointment.
        var reqApp = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/appointments/current/account/' + accountId + '/userId/' + userId
        });
        reqApp.done(function (appointment) {
            console.log("Retrieve Appointment: " + $.toJSON(appointment));
            //   $("select[id='kidName'] option[value='"+appointment.kidId+"']").attr("selected", true);
            // TODO ON UPDATE SELECT CHILDS WITHIN Appointment list
            $('#arrivalDate').val(appointment.arrivalDate);
            $('#departureDate').val(appointment.departureDate);
            $('#userId').val(appointment.currentUserId);
            $('#appointmentId').val(appointment.appointmentId);
            // $('#declarationType').val(appointment.declarationType);
        });

    }

    /** PAGE INITIALIZATION **/
    $.i18n.init({
        resGetPath: '/locales/__lng__/__ns__.json',
        ns: {
            namespaces: ['run', "commons"],
            defaultNs: 'run'
        }
    }).done(function () {
        $(".run").i18n();
        $(".navbar").i18n();
    });
    var value = sessionStorage.getItem('apptoken');
    var accountId = null;
    var userId = null;
    if (value != null && value != "") {
        var token = $.parseJSON(value);
        accountId = token.accountId;
        userId = token.userId;
    }
    $(document).ready(function () {

        $("#arrivalDate").mask("99-99-9999 99:99");
        $("#departureDate").mask("99-99-9999 99:99");
        $("#editArrivalDate").mask("99-99-9999 99:99");
        $("#editDepartureDate").mask("99-99-9999 99:99");
        // Get Current Appointment data
        if (accountId == null) return;
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

        // Get Children linked to the Account
        var reqChildren = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/children/account/' + accountId
        });
        reqChildren.done(function (children) {
            $('#kidName').append(Mustache.to_html($('#kid-appointment-template').html(), children));
            $('#editKids').append(Mustache.to_html($('#kid-appointment-template').html(), children));
            //selected by default  on live declaration
            $('#kidName > .liveKid').each(function () {
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
        initAppointment();
        last5();
    });

    /** PAGE NAVIGATION **/
    $('#runTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    })

    /** PAGE ACTIONS **/
    $('#goLive').click(function (e) {
        console.log("[START] goLive");
        var kids = [];
        $("#kidName > .selected-kid").each(function (i, value) {
            var selKidId = $(this).attr("data-target");
            kids[i] = selKidId;
        });
        var appId = $('#appointmentId').val();
        var appUrl = '/services/appointments'
        if (appId != null) {
            appUrl = appUrl + '/' + appId;
        }
        var mAppointment = {
            appointmentId: appId,
            accountId: accountId,
            currentUserId: $('#userId').val(),
            arrivalDate: $('#arrivalDate').val(),
            departureDate: $('#departureDate').val(),
            kidIds: kids,
            //declarationType: $('#declarationType').val(),
            //TODO add notes here
        };

        var data = $.toJSON(mAppointment);
        var req = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: appUrl,
            dataType: "json",
            data: data,
        });

        initAppointment();
        last5();
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
            $(".appointmentsContent").i18n();
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
                    //build an array of childId
                    var childIdArr = [];
                    for (i = 0; i < app.children.length; i++) {
                        childIdArr[i] = app.children[i].childId;
                    }
                    $('#editKids > .liveKid').each(function () {
                        var kidsId = $(this).attr('data-target');
                        if (contains(childIdArr, kidsId)) {
                            $(this).addClass('selected-kid');
                            $(this).removeClass('disabled-kid');
                        } else {
                            $(this).addClass('disabled-kid');
                            $(this).removeClass('selected-kid');
                        }
                    });
                    $('#editAppointmentId').val(app.appointmentId);

                    $('#editUpdate').show();
                });
            });
        });
    });
    $('#editCancel').click(function (e) {
        $('#editUpdate').hide();
        console.log("cancel");
    });

    $('#editSave').click(function (e) {

        var kids = [];
        $("#editKids > .selected-kid").each(function (i, value) {
            var selKidId = $(this).attr("data-target");
            kids[i] = selKidId;
        });

        var mAppointment = {
            appointmentId: $('#editAppointmentId').val(),
            accountId: accountId,
            currentUserId: $('#userId').val(),
            arrivalDate: $('#editArrivalDate').val(),
            departureDate: $('#editDepartureDate').val(),
            kidIds: kids,
            departureUserId: $('#editDepartureUserId').val(),
            arrivalUserId: $('#editArrivalUserId').val()
            //TODO add notes here
        };

        var data = $.toJSON(mAppointment);

        var reqAppSave = $.ajax({
            type: 'POST',
            contentType: 'application/json',
            url: '/services/appointments/' + $('#editAppointmentId').val(),
            data: data,
        });
        reqAppSave.done(function (appointment) {
            console.log("Update is done.");
            $('#search').click();
            $('#editUpdate').hide();

        });

    });

})(jQuery);