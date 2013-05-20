'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('nounouApp.services', ['ngResource']).
  value('version', '0.1').
factory('User', function ($resource) {
    return $resource('/nounou/services/users/:userId/:filter/:value', {
        userId: '@userId'
    }, {
        query: {
            method: 'GET',
            isArray: true
        },
        linkedUsers: {
             method: 'GET',
             params: {  userId: '@userId', filter:"account"}
        }
    });
}).
  factory('Nurse', function ($resource) {
      return $resource('/nounou/services/nurses/:nurseId/:action', {
          nurseId: '@nurseId'
      }, {
          query: {
              method: 'GET',
              isArray: true
          },
          start: {
               method: 'POST',
               params: {  nurseId: '@nurseId', action:"start"}
          },
          stop: {
               method: 'POST',
               params: {  nurseId: '@nurseId', action:"stop"}
          }
      });
}).
factory('Children', function ($resource) {
      return $resource('/nounou/services/children/:childId/:action', {
          childId: '@childId'
      }, {
          query: {
              method: 'GET',
              isArray: true
          },
          start: {
               method: 'POST',
               params: {  childId: '@childId', action:"start"}
          },
          stop: {
               method: 'POST',
               params: {  childId: '@childId', action:"stop"}
          }
      });
}).
  factory('Appointment', function ($resource) {
      return $resource('/nounou/services/appointments/:appointmentId/:action', {
          appointmentId: '@appointmentId'
      }, {
          query: {
              method: 'GET',
              isArray: true
          },
          start: {
               method: 'POST',
               params: {  appointmentId: '@appointmentId', action:"start"}
          },
          stop: {
               method: 'POST',
               params: {  appointmentId: '@appointmentId', action:"stop"}
          }
      });
}).factory('AppService', [ function () {

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
      }
      // the Factory return an instance of the applicationService
      return new ApplicationService();
  }]);
