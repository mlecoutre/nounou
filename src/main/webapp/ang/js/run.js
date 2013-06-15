function RunCtrl($scope, User, Appointment, Children, Nurse, AppService) {


    $scope.token = AppService.getToken();

    $scope.children = listChildren();
    $scope.users    = listUsers();
    $scope.last5    = [];
    $scope.nurses   = listNurses();
    $scope.kids;

    $scope.requestAppointments;

    $scope.appointment = Appointment.get({appointmentId:0, filter: 'account', value: $scope.token.accountId, filter2: 'user', value2:1});
    $scope.appointments;
    $scope.updtAppointement;

    $scope.tabs = [{label: "run:run.tab.live"},{label: "run:run.tab.update"}];

    $scope.selectables = [  {label:"current-week", value:"currentWeek"},
                            {label:"current-month", value:"currentMonth"},
                            {label:"last-week", value:"lastWeek"},
                            {label:"previous-month", value:"lastMonth"},
                            {label:"all", value:"all"}];
    initAppointment();
    $scope.searchDate = $scope.selectables[0];


    //MANAGE Appointments ///////////
    function initAppointment(){
       console.log("init");

        i18n.init(function(t) {
            angular.forEach($scope.selectables, function(s){
                  s.label = t("run:run.search.options." + s.label);
            });

            angular.forEach($scope.tabs, function(s){
                s.label = t(s.label);
            });
        });

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
                return $scope.kids.splice( index, 1 );
              }
              index++;
        });
        if (isToAdd){
            $scope.kids.push(child);
        }
    }

    $scope.prepareUpdateAppointment = function(a){
         console.log("prepareUpdate ");
         $scope.updtAppointement = a;
          if (a.departureUser != null){
             angular.forEach($scope.users, function(u) {
                if(u.userId == a.arrivalUser.userId ) {
                    $scope.updtAppointement.arrivalUser = u;
                }
            });
         }
         if (a.departureUser != null){
            angular.forEach($scope.users, function(u) {
                if(u.userId == a.departureUser.userId ) {
                    $scope.updtAppointement.departureUser = u;
                }
            });
         }
         $("#editUpdate").show();
         //$("#cancelAppointment").show();
         //$("#addAppointment").hide();
         return;
    }

    $scope.updateAppointment = function(){
       $scope.appointment.$save({appointmentId: $scope.appointment.userId}, function(){
            AppService.displaySuccessMessage("Appointment successfully updated");
            listAppointments();
       }, function(){
            return AppService.displayErrorMessage("ERROR, Appointment unsuccessfully updated");
       });

       $("#updateAppointment").hide();
       $("#addAppointment").show();
       $("#cancelAppointment").hide();
    }

    $scope.delAppointment = function(a){
       Appointment.delete({appointmentId: a.appointmentId}, function(){
            AppService.displaySuccessMessage("Appointment successfully deleted");
            $scope.listAppointments ();
       }, function(){
            return AppService.displayErrorMessage("ERROR, Appointment unsuccessfully deleted");
       });
    }

    $scope.cancelAppointment = function(){
       $scope.appointment = new Appointment();
       $("#editUpdate").hide();
      // $("#addAppointment").show();
      // $("#cancelAppointment").hide();
    }

    function last5Appointments(){
        Appointment.report({appointmentId:0, filter: 'account', value: $scope.token.accountId, filter2:'searchType', value2: 'last'}, function(u, getResponseHeaders){
          $scope.last5 = u.appointments;
        });
    }

    $scope.listAppointments =  function() {
        console.log("listAppointments : "+$scope.searchDate.value);
        Appointment.report({appointmentId:0, filter: 'account', value: $scope.token.accountId, filter2:'searchType', value2: $scope.searchDate.value}, function(u, getResponseHeaders){
            $scope.requestAppointments = u.appointments;
        });
    };

    //////// OTHER STUFFS ////////////////////

    function listChildren() {
           return $scope.children  = Children.query({childId:0, filter: 'account', value: $scope.token.accountId}, function(){
                //clone the kids list in order to init the selected list of current appointment
                $scope.kids = $scope.children.slice(0);
           });
    };

    function listNurses() {
          return $scope.nurses  = Nurse.query({nurseId:0, filter: 'account', value: $scope.token.accountId});
    };

    function listUsers() {
          return $scope.users  = User.query({userId:0, filter: 'account', value: $scope.token.accountId});
    };
}