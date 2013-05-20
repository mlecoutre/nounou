function RegistrationCtrl($scope, User, Nurse, Children, AppService) {
    /*
    $scope.userFirstName;
    $scope.userLastName;
    $scope.userPhoneNumber;
    $scope.userEmail;
    $scope.userPassword    ;
    $scope.secondUserPassword;
    $scope.userType;
    */

    $scope.accountId = 1;

    $scope.user = new User({accountId: $scope.accountId});
    $scope.secondUserPassword;
    $scope.users = listUsers();
    $scope.selectedUser;

    $scope.nurses;
    $scope.selectedNurse;

    $scope.children;
    $scope.selectedChild;

    $scope.addUser = function(){
        console.log("add User: "+$scope.user.firstName);
        User.save($scope.user, function(usr) {
            console.log("add User created "+usr.accountId);
            $scope.user = new User();
            $scope.accountId = usr.accountId;
            listUsers();
            AppService.displaySuccessMessage("User successfully created");
        }, function() {
            return AppService.displayErrorMessage("ERROR, User unsuccessfully created");
        });
    }

    $scope.prepareUpdate = function(usr){
         console.log("prepareUpdate "+usr.firstName);
         $scope.user = usr;
         $("#updateUser").show();
         $("#cancelUser").show();
         $("#addUser").hide();
         return;
    }

    $scope.updateUser = function(){
       $scope.user.$save({userId: $scope.user.userId}, function(){
               listUsers();
       }, function(){
        //error function
       });

       $("#updateUser").hide();
       $("#addUser").show();
       $("#cancelUser").hide();

    }

    $scope.cancelUser = function(){
       $scope.user = new User();
       $("#updateUser").hide();
       $("#addUser").show();
       $("#cancelUser").hide();
    }

    function listUsers() {
          return $scope.users  = User.query({userId:0, filter: 'account', value: $scope.accountId});
    };


}