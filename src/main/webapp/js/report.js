(function ($) {

    /** PAGE INITIALIZATION **/
    var value = sessionStorage.getItem('apptoken');
    var accountId = null;
    var userId = null;
    if(value != null && value != ""){
            var token = $.parseJSON(value);
            accountId = token.accountId;
            userId  = token.userId;
    }

    $(document).ready(function () {

        // Get Last 5 Appointments data
        var reqA = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/appointments/report/account/'+accountId+'/searchType/currentMonth',

        });
        reqA.done(function (report) {
            console.log("get last appointments");
            $(totalMonth).text(report.reportTitle +': '+ report.totalDuration);
            for (i = 0; i < report.appointments.length; i++) {
                $('#lastAppointments').append(Mustache.to_html($('#last-appointments-template').html(), report.appointments[i]));
            }
        });
    });

    /** PAGE NAVIGATION **/
    $('#reportTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    })

    /** PAGE ACTIONS **/

})(jQuery);