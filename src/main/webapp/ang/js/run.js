function RunCtrl($scope, User, Appointment, Children, Nurse, AppService) {


    $scope.accountId = 1;

    $scope.children = listChildren();
    $scope.users    = listUsers();
    $scope.last5    = [];
    $scope.nurses   = listNurses();
    $scope.kids;

    $scope.requestAppointments;

    $scope.appointment = Appointment.get({appointmentId:0, filter: 'account', value: $scope.accountId, filter2: 'user', value2:1});
    $scope.appointments;
    $scope.updtAppointement;

    $scope.searchDate = "currentWeek";

     initAppointment();
    //MANAGE Appointments ///////////
    function initAppointment(){
       console.log("init");
       last5Appointments();
    }

    $scope.addAppointment = function(){
        console.log("add Appointment: ");
        var kidsArr = new Array();
        angular.forEach($scope.kids, function(kid) {
                console.log (" add kid "+kid.childId);
                kidsArr.push(kid.childId);
        });
        $scope.appointment.kidIds =  kidsArr;
        Appointment.save($scope.appointment, function(a) {
            console.log("add Appointment created "+a.accountId);
            $scope.appointment = new Appointment();
            last5Appointments();
            AppService.displaySuccessMessage("Appointment successfully created");
        }, function() {
            return AppService.displayErrorMessage("ERROR, Appointment unsuccessfully created");
        });
    }

    $scope.clicKid =  function(child){
        var isToAdd = true;
        var index = 0;
        angular.forEach($scope.kids, function(kid) {
              if (kid.childId == child.childId){
                //remove it
                isToAdd = false;
                var value = $scope.kids.splice( index, 1 );
                console.log("Remove child ");
                return;
              }
              index++;
        });
        if (isToAdd){
            console.log("Add child");
            $scope.kids.push(child);
        }

    }

    $scope.prepareUpdateAppointment = function(a){
         console.log("prepareUpdate ");
         $scope.updtAppointement = a;
          angular.forEach($scope.users, function(u) {
            if(u.userId == a.arrivalUser.userId ) {
                $scope.updtAppointement.arrivalUser = u;
            }
         });
         angular.forEach($scope.users, function(u) {
            if(u.userId == a.departureUser.userId ) {
                 $scope.updtAppointement.departureUser = u;
            }
         });
         $("#editUpdate").show();
         //$("#cancelAppointment").show();
         //$("#addAppointment").hide();
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
       $("#editUpdate").hide();
      // $("#addAppointment").show();
      // $("#cancelAppointment").hide();
    }

    function last5Appointments(){
        Appointment.report({appointmentId:0, filter: 'account', value: $scope.accountId, filter2:'searchType', value2: 'last'}, function(u, getResponseHeaders){
          $scope.last5 = u.appointments;
        });
    }

    $scope.listAppointments =  function() {
        console.log("listAppointments : "+$scope.searchDate);
        Appointment.report({appointmentId:0, filter: 'account', value: $scope.accountId, filter2:'searchType', value2: $scope.searchDate}, function(u, getResponseHeaders){
            $scope.requestAppointments = u.appointments;
        });
    };

    //////// OTHER STUFFS ////////////////////

    function listChildren() {
           return $scope.children  = Children.query({childId:0, filter: 'account', value: $scope.accountId}, function(){
                //clone the kids list in order to init the selected list of current appointment
                $scope.kids = $scope.children.slice(0);
           });
    };

    function listNurses() {
          return $scope.nurses  = Nurse.query({nurseId:0, filter: 'account', value: $scope.accountId});
    };

    function listUsers() {
          return $scope.users  = User.query({userId:0, filter: 'account', value: $scope.accountId});
    };
}