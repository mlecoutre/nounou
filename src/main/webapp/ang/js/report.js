function ReportCtrl($scope, Appointment) {

    $scope.accountId = 1;
    $scope.appointments ;

        function listAppointments() {
              return $scope.appointments  = Appointment.query({appointmentId:0, filter: 'account', value: $scope.accountId});
        };

}