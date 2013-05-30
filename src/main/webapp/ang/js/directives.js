'use strict';

/* Directives */


angular.module('nounouApp.directives', []).
  directive('appVersion', ['version', function(version) {
    return function(scope, elm, attrs) {
      elm.text(version);
    };
  }]).directive('ngKid', function () {
         return {
             link: function (scope, element, attrs) {
                 element.click(function (e) {
                     e.preventDefault();
                      $(element).toggleClass('disabled-kid');
                      $(element).toggleClass('selected-kid');
                 });
             }
         };
}).directive('ngDatetime', function () {
  return function (scope, element, attrs) {
       $(element).mask("99-99-9999 99:99");
    };
  });
