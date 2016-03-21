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

    $scope.user = {
      firstName : $window.localStorage.firstName,
      lastName : $window.localStorage.lastName,
      email: $window.localStorage.email,
      primaryPhone: $window.localStorage.primaryPhone,
      userId : $window.localStorage.userId
    };

    var url_get ='http://183.82.103.141:8080/vellkare/api/v0/users/7';
    $http.jsonp(url_get).success(function(data){
      $scope.data=data;
      //angular.element('.tile.refreshing').removeClass('refreshing');
    });

    $scope.ajaxFaker = function(){
      $scope.data=[];
      var url = 'http://www.filltext.com/?rows=10&fname={firstName}&lname={lastName}&delay=5&callback=JSON_CALLBACK';

      $http.jsonp(url).success(function(data){
        $scope.data=data;
        angular.element('.tile.refreshing').removeClass('refreshing');
      });
    };

    $scope.changeLanguage = function (langKey) {
      $translate.use(langKey);
      $scope.currentLanguage = langKey;
    };
    $scope.currentLanguage = $translate.proposedLanguage() || $translate.use();

    $scope.lgout = function(){
      $state.go('core.login');
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
    }
  });
