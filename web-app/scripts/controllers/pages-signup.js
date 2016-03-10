'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:PagesSignupCtrl
 * @description
 * # PagesSignupCtrl
 * Controller of the minovateApp
 */
app.controller('SignupCtrl', function ($scope,$state, $http) {

    $scope.signUp = function() {
        var dataToSend = {
            "clientId":"vellkare-client",
            "username":$scope.username,
            "email":$scope.email,
            "password":$scope.password,
            "firstName":$scope.firstName,
            "lastName":$scope.lastName,
            "phone":$scope.phone
        };
        $http.post('http://183.82.103.141:8080/vellkare/api/v0/users/signup',dataToSend).success(function(data, status){
            $state.go('app.dashboard');
        }).error(function (data){
            alert("something went wrong");
        });

    };
});
