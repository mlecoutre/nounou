function RunCtrl($scope, User, Appointment, Children, Nurse, AppService) {


    $scope.accountId = 1;

    $scope.children = listChildren();
    $scope.users = listUsers();
    $scope.last5 = [];
    $scope.nurses = listNurses();

    $scope.requestAppointments;

    $scope.appointment = Appointment.get({appointmentId:0, filter: 'account', value: $scope.accountId, filter2: 'user', value2:1});
    $scope.appointments;

     initAppointment();
    //MANAGE Appointments ///////////
    function initAppointment(){
       console.log("init");
       last5Appointments();

    }

    $scope.addAppointment = function(){
        console.log("add Appointment: ");
        Appointment.save($scope.appointment, function(a) {
            console.log("add Appointment created "+a.accountId);
            $scope.appointment = new Appointment();
            last5Appointments();
            AppService.displaySuccessMessage("Appointment successfully created");
        }, function() {
            return AppService.displayErrorMessage("ERROR, Appointment unsuccessfully created");
        });
    }

    $scope.prepareUpdateAppointment = function(a){
         console.log("prepareUpdate ");
         $scope.appointment = a;
         $("#updateAppointment").show();
         $("#cancelAppointment").show();
         $("#addAppointment").hide();
         return;
    }

    $scope.updateAppointment = function(){
       $scope.appointment.$save({appointmentId: $scope.appointment.userId}, function(){
               listAppointments();
       }, function(){
        //error function
       });

       $("#updateAppointment").hide();
       $("#addAppointment").show();
       $("#cancelAppointment").hide();
    }

    $scope.delAppointment = function(a){
       a.$remove({appointmentId: a.appointmentId}, function(){
             listAppointments();
       }, function(){
             //error function
       });
    }

    $scope.cancelAppointment = function(){
       $scope.appointment = new Appointment();
       $("#updateAppointment").hide();
       $("#addAppointment").show();
       $("#cancelAppointment").hide();
    }

    function last5Appointments(){
        Appointment.report({appointmentId:0, filter: 'account', value: $scope.accountId, filter2:'searchType', value2: 'last'}, function(u, getResponseHeaders){
          $scope.last5 = u.appointments;
        });
    }

    function listAppointments() {
           return $scope.last5 = Appointment.query({appointmentId:0, filter: 'account', value: $scope.accountId, filter2:'searchType', value2: 'last'});
        };

    //////// OTHER STUFFS ////////////////////

    function listChildren() {
          return $scope.children  = Children.query({childId:0, filter: 'account', value: $scope.accountId});
    };

    function listNurses() {
          return $scope.nurses  = Nurse.query({nurseId:0, filter: 'account', value: $scope.accountId});
    };

    function listUsers() {
          return $scope.users  = User.query({userId:0, filter: 'account', value: $scope.accountId});
    };
}