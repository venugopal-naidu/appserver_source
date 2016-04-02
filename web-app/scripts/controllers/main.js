'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the minovateApp
 */
app
  .controller('MainCtrl', function ($scope, $http, $translate, $window, $state) {
    $scope.isUserLoggedIn = $window.localStorage.isUserLoggedIn;
    $scope.user = null;
    $scope.main = {
      title: 'Velkare',
      settings: {
        navbarHeaderColor: 'scheme-default',
        sidebarColor: 'scheme-default',
        brandingColor: 'scheme-default',
        activeColor: 'default-scheme-color',
        headerFixed: true,
        asideFixed: true,
        rightbarShow: false
      }
    };
    $scope.lgout = function(){
      $window.localStorage.isUserLoggedIn =  false;
      // Clear access Token from Local storage
      $window.localStorage.accessToken = null;
      $window.localStorage.expiresIn = null;
      $window.localStorage.refreshToken = null;
      $window.localStorage.tokenType = null;
      $window.localStorage.scope = null;
      // Clear user info from Local storage
      $window.localStorage.firstName = null;
      $window.localStorage.lastName = null;
      $window.localStorage.email = null;
      $window.localStorage.primaryPhone = null;
      $window.localStorage.userId = null;

      $state.go('site.login');
    };

    $scope.setUser = function(){
      $scope.user = {
        firstName : $window.localStorage.firstName,
        lastName : $window.localStorage.lastName,
        email: $window.localStorage.email,
        primaryPhone: $window.localStorage.primaryPhone,
        userId : $window.localStorage.userId
      };
    };
    $scope.getUser = function(){
      return $scope.user;
    };

    if($scope.isUserLoggedIn){
      $scope.setUser();
    }
  });
