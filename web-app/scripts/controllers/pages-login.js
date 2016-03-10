'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:PagesLoginCtrl
 * @description
 * # PagesLoginCtrl
 * Controller of the minovateApp
 */
app
  .controller('LoginCtrl', function ($scope, $state, $http) {
    $scope.login = function() {
        var dataToSend = {
            "clientId":"vellkare-client",
            "username":$scope.username,
            "password":$scope.password
        };
        $http.post('http://183.82.103.141:8080/vellkare/api/v0/auth/login',dataToSend).success(function(data, status){
            $window.localStorage['jwtToken'] = token;
            $state.go('app.dashboard');
        }).error(function (data){
            alert("something went wrong");
        });
    };
  });
