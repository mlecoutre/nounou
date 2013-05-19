'use strict';

// Declare app level module which depends on filters, and services
angular.module('nounouApp', ['nounouApp.filters', 'nounouApp.services', 'nounouApp.directives','ui.bootstrap','i18next']).
  config(['$routeProvider', function($routeProvider) {
    $routeProvider.
    	when('/index', {templateUrl: 'partials/index.html', controller: IndexCtrl}).
    	when('/contacts', {templateUrl: 'partials/contacts.html', controller: ContactsCtrl}).
    	when('/about', {templateUrl: 'partials/about.html', controller: AboutCtrl}).
    	when('/registration', {templateUrl: 'partials/registration.html', controller: RegistrationCtrl}).
    	when('/run', {templateUrl: 'partials/run.html', controller: RunCtrl}).
    	when('/report', {templateUrl: 'partials/report.html', controller: ReportCtrl}).
    	otherwise({redirectTo: '/index'});
  }]).run(function ($rootScope) {
    $rootScope.i18nextOptions = {
        debug:"true",
        resGetPath: 'locales/__lng__/__ns__.json',
        ns : {
            namespaces: ['index', "commons", "registration", "report", "run"],
                defaultNs: 'index'
            }
        }
});
