function RegistrationCtrl($scope, User, Nurse, Children, AppService) {

    $scope.accountId = 1;

    $scope.user = new User({accountId: $scope.accountId});
    $scope.secondUserPassword;
    $scope.users = listUsers();

    $scope.nurses = listNurses();
    $scope.nurse =  new Nurse({accountId: $scope.accountId});

    $scope.child =  new Children({accountId: $scope.accountId});
    $scope.children = listChildren();

        $scope.tabs = [{label: "registration:registration.tab.what-title"},
           {label: "registration:registration.tab.you-title"},
           {label: "registration:registration.tab.nurse-title"},
           {label: "registration:registration.tab.kids-title"}];

        i18n.init(function(t) {
            angular.forEach($scope.tabs, function(s){
                s.label = t(s.label);
            });
        });

    //MANAGE USERS ///////////

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

    $scope.prepareUpdateUser = function(usr){
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

    $scope.delUser = function(u){
       u.$remove({userId: u.userId}, function(){
             listUsers();
       }, function(){
             //error function
       });
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

    // MANAGE NURSES ///////////////////

        $scope.addNurse = function(){
            console.log("add Nurse: "+$scope.nurse.firstName);
            Nurse.save($scope.nurse, function(n) {
                console.log("add Nurse created for account "+n.accountId);
                $scope.nurse = new Nurse({accountId: $scope.accountId});
                listNurses();
                AppService.displaySuccessMessage("Nurse successfully created");
            }, function() {
                return AppService.displayErrorMessage("ERROR, nurse unsuccessfully created");
            });
        }

        $scope.prepareUpdateNurse = function(n){
             console.log("prepareUpdate "+n.firstName);
             $scope.nurse = n;
             $("#updateNurse").show();
             $("#cancelNurse").show();
             $("#addNurse").hide();
             return;
        }

        $scope.updateNurse = function(){
           $scope.nurse.$save({nurseId: $scope.nurse.nurseId}, function(){
                   listNurses();
           }, function(){
            //error function
           });

           $("#updateNurse").hide();
           $("#addNurse").show();
           $("#cancelNurse").hide();
        }

        $scope.delNurse = function(n){
           n.$remove({nurseId: n.nurseId}, function(){
                 listNurses();
           }, function(){
                 //error function
           });
        }

        $scope.cancelNurse = function(){
           $scope.nurse = new Nurse();
           $("#updateNurse").hide();
           $("#addNurse").show();
           $("#cancelNurse").hide();
        }

        function listNurses() {
              return $scope.nurses  = Nurse.query({nurseId:0, filter: 'account', value: $scope.accountId});
        };

    // MANAGE CHILDREN ///////////////////

        $scope.addChild = function(){
            console.log("add Child: "+$scope.child.firstName);
            Children.save($scope.child, function(n) {
                console.log("add Child created for account "+n.accountId);
                $scope.child = new Children({accountId: $scope.accountId});
                listChildren();
                AppService.displaySuccessMessage("Child successfully created");
            }, function() {
                return AppService.displayErrorMessage("ERROR, child unsuccessfully created");
            });
        }

        $scope.prepareUpdateChild = function(c){
             console.log("prepareUpdate "+c.firstName);
             $scope.child = c;
             $("#updateChild").show();
             $("#cancelChild").show();
             $("#addChild").hide();
             return;
        }

        $scope.updateChild = function(){
           $scope.child.$save({childId: $scope.child.childId}, function(){
                   listChildren();
           }, function(){
            //error function
           });

           $("#updateChild").hide();
           $("#addChild").show();
           $("#cancelChild").hide();
        }

        $scope.delChild= function(c){
           c.$remove({childId: c.childId}, function(){
                 listChildren();
           }, function(){
                 //error function
           });
        }

        $scope.cancelChild = function(){
           $scope.child = new Children();
           $("#updateChild").hide();
           $("#addChild").show();
           $("#cancelChild").hide();
        }

        function listChildren() {
              return $scope.children  = Children.query({childId:0, filter: 'account', value: $scope.accountId});
        };

}