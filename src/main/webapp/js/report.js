(function ($) {

    /** PAGE INITIALIZATION **/
    $.i18n.init( {
        resGetPath: '/locales/__lng__/__ns__.json',
        ns : {
                namespaces: ['report', "commons"],
                defaultNs: 'report'
             }
        }
    ).done(function(){
          $(".report").i18n();
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
        var reqA = $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: '/services/appointments/report/account/' + accountId + '/searchType/currentMonth',

        });
        reqA.done(function (report) {
            console.log("get last appointments");
            $('#tableReports').append(Mustache.to_html($('#appointments-template').html(), report.appointments));
            $(".app-template").i18n({ totDuration:  report.totalDuration });

           chart = new Highcharts.Chart({
                chart: {
                    renderTo: 'container'
                },
                title: 'Monthly reports',
                yAxis: {
                    title: {
                        text: 'hours per day'
                    }
                },
                xAxis: {
                    type: 'datetime',
                },
                tooltip: {
                    formatter: function () {
                        var decTime = this.y;
                        var hour = Math.floor(decTime);
                        var min = Math.round(60*(decTime-hour));
                        return $.datepicker.formatDate('yy-mm-dd',(new Date(this.x))) + '=> ' + hour+':'+min;
                    }
                },
                series: [{
                    type: 'arearange',
                    name: 'Time range',
                    data: report.dataRange
                }, {
                    type: 'spline',
                    name: 'azrael',
                    data: report.data,
                    marker: {
                        lineWidth: 2,
                    }
                }
                ]
            });

        });
    });

    /** PAGE NAVIGATION **/
    $('#reportTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    })

    /** PAGE ACTIONS **/

})(jQuery);