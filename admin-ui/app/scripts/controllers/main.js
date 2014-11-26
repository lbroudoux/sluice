'use strict';

/**
 * @ngdoc function
 * @name sluiceApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the sluiceApp
 */
angular.module('sluiceApp')
  .controller('MainCtrl', function ($scope) {
    $scope.awesomeThings = [
      'HTML5 Boilerplate',
      'AngularJS',
      'Karma'
    ];
  });
