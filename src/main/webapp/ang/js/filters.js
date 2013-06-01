'use strict';

/* Filters */

angular.module('nounouApp.filters', []).
  filter('interpolate', ['version', function(version) {
    return function(text) {
      return String(text).replace(/\%VERSION\%/mg, version);
    }
  }]).
  filter('translate', function() {
          return function(text) {
            return "test";
          }
  });
