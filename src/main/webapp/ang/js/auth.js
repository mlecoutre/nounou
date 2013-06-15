angular.module('nounouApp.authService', ['ngResource']).factory('Auth', function ($resource) {
  return $resource('/nounou/services/auth',{}, {
    authenticate: {
      method: 'POST',
      isArray: false
    }
  });
}).directive('authbox', function() {
  return {
    restrict: 'E',
    templateUrl: 'partials/auth.html',
    controller: function AuthCtrl($scope, Auth, AppService){
     $scope.token = new Auth();
     $scope.logged = false;
     var localStorageName = "nounou-auth";


     $scope.login = function(){
      console.log("try to login "+$scope.token.uid);

      Auth.authenticate($scope.token, function(token) {
       console.log("User authentified "+token.accountId);
       $scope.token = token;
       $scope.logged = true;
       var data = angular.toJson($scope.token);
       localStorage.setItem(localStorageName, data);

       AppService.displaySuccessMessage("User successfully authentified");
     }, function(msg) {
       return AppService.displayErrorMessage("ERROR during authentication");
     });

    }

    $scope.signOut = function(){
      $scope.logged = false;
      localStorage.setItem(localStorageName, '');
      console.log("Sign out");
    }

    var jsonToken = localStorage.getItem(localStorageName);
    var token = angular.fromJson(jsonToken);
    if (token != null){
      console.log("Existing session in localstorage :"+ token.userName);
      $scope.token.userName = token.userName;
      $scope.token.accountId = token.accountId;
      $scope.token.uid = token.uid;
      $scope.token.userId = token.userId;
      $scope.logged = true;
    }
  }
};
});
