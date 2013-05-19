'use strict';

/* Services */


// Demonstrate how to register services
// In this case it is a simple value service.
angular.module('nounouApp.services', ['ngResource']).
  value('version', '0.1').
factory('User', function ($resource) {
    return $resource('/report/services/users/:userId/:action', {
        userId: '@userId'
    }, {
        query: {
            method: 'GET',
            isArray: true
        },
        start: {
             method: 'POST',
             params: {  userId: '@userId', action:"start"}
        },
        stop: {
             method: 'POST',
             params: {  userId: '@userId', action:"stop"}
        }
    });
});