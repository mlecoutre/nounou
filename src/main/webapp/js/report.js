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
            $('#current').append(Mustache.to_html($('#last-appointments-template').html(), report.appointments));
            $('#totalMonth').text(report.reportTitle +': '+ report.totalDuration);
            var jsonData = $.toJSON(report.data);
            console.log(jsonData);

           chart = new Highcharts.Chart({
               chart: { renderTo: 'container' },
               title: 'Monthly reports',
               yAxis: {
                       title: {
                           text: 'hours per day'
                       }
               },
                rangeSelector : {
                				selected : 1
               },
                xAxis: {
                       type: 'datetime',
                       dateTimeLabelFormats: {day: '%e. %b'}
                   },
               series: [{
                   name: 'azrael',
                   data: report.data,
                   tooltip: {
                   					valueDecimals: 2
                   }
               }]
           });
           /*

             chart = new Highcharts.Chart({
                          chart: { renderTo: 'container', type: 'line' },
                          title: 'Monthly reports',
                          yAxis: {
                                  title: {
                                      text: 'hours per day'
                                  }
                          },
                           xAxis: {
                                  type: 'datetime',
                                  dateTimeLabelFormats: {day: '%e. %b'}
                              },
                          series: [{
                              name: 'azrael',
                              data: report.data

                          }]
                      });
*/
            jsonData = $.toJSON(report.dataRange);
            console.log('dataRange:'+jsonData);
            //var arrayData = [[1351724400000,8.87,18.53],[1351810800000,8.75,18.75],[1352070000000,9.88,18.83],[1352156400000,9,18.58] ,[1352242800000,9.95,17.92],[1352329200000,8.45,18.45],[1352415600000,7.45,17.75],[1352502000000,8.98,18.75],[1352588400000,8.82,18.97]];
            window.chart2 = new Highcharts.StockChart({

            chart: {
                renderTo: 'containerRange',
                type: 'arearange'
            },

            rangeSelector: {
                selected: 2
            },

            title: {
                text: 'Interval by day'
            },

            tooltip: {
                valueSuffix: 't'
            },

            series: [{
                name: 'Temperatures',
                data: report.dataRange
            }]
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