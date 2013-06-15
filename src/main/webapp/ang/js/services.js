'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('nounouApp.services', ['ngResource']).
value('version', '1.0.0').
factory('User', function ($resource) {
  return $resource('/nounou/services/users/:userId/:filter/:value', {
    userId: '@userId'
  }, {
    query: {
      method: 'GET',
      isArray: true
    }
  });
}).
factory('Nurse', function ($resource) {
  return $resource('/nounou/services/nurses/:nurseId/:filter/:value', {
    nurseId: '@nurseId'
  }, {
    query: {
      method: 'GET',
      isArray: true
    }
  });
}).
factory('Children', function ($resource) {
  return $resource('/nounou/services/children/:childId/:filter/:value', {
    childId: '@childId'
  }, {
    query: {
      method: 'GET',
      isArray: true
    }
  });
}).
factory('Appointment', function ($resource) {
  return $resource('/nounou/services/appointments/:appointmentId/:filter/:value/:filter2/:value2', {
    appointmentId: '@appointmentId'
  }, {
    query: {
      method: 'GET',
      isArray: true
    },
    report: {
      method : 'GET',
      isArray: false
    }
  });
}).factory('AppService', [ function (Auth) {

      // Create the application service
      function ApplicationService() {

        this.displayErrorMessage = function (msg) {
          $('#msg').html(msg);
          $('#msgBox').removeClass().addClass('alert alert-error');
          $('#msgBox').fadeIn('slow')
          .delay(5000)
          .fadeOut('fast', function() { $(this).hide(); })
        }

        this.displaySuccessMessage =  function (msg) {
          $('#msg').html(msg);
          $('#msgBox').removeClass().addClass('alert alert-success');
          $('#msgBox').fadeIn('slow')
          .delay(5000)
          .fadeOut('fast', function() { $(this).hide(); })
        }

        this.getToken = function () {
         var localStorageName = "nounou-auth";
         var value = localStorage.getItem(localStorageName);
         return angular.fromJson(value);
       }

     }
      // the Factory return an instance of the applicationService
      return new ApplicationService();
    }]);
