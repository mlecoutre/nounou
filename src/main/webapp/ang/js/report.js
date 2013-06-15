function ReportCtrl($scope, Appointment, $filter) { 
    $scope.totalDuration;
    $scope.appointments ;
    listAppointments();

    $scope.tabs = [{label: "report:report.tab.title.chart"},
       {label: "report:report.tab.title.table"},
       {label: "report:report.tab.title.options"}];

    i18n.init(function(t) {
        angular.forEach($scope.tabs, function(s){
            //console.log(">"+t(s.label));
            s.label = t(s.label);
        });
    });

    function listAppointments() {

     Appointment.report({appointmentId:0, filter: 'account', value:  $scope.token.accountId, filter2:'searchType', value2: 'currentMonth'}, function(report, getResponseHeaders){
       $scope.appointments = report.appointments;

       i18n.init(function(t) {
            var legend = t("report:app-template.total-duration");
            $scope.totalDuration = legend + report.totalDuration;
       });

       $scope.totalDuration = report.totalDuration;
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
                var min = Math.round(60 * (decTime - hour));
                var mDate = $filter('date')(new Date(this.x), 'dd/MM/yy');
                return mDate + '  ' + hour + ':' + min;

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
        }]
    });
   });
};

}