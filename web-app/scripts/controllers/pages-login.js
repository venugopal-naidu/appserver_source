'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:PagesLoginCtrl
 * @description
 * # PagesLoginCtrl
 * Controller of the minovateApp
 */
app
  .controller('LoginCtrl', function ($scope, $state, $http, $window) {
    $scope.isAppointmentSelected = false;
    $scope.selectedDoctor = null;
    $scope.doctorAppointmentDate = null;
    // Set isAppointmentSelected
    if($window.localStorage.isAppointmentSelected != null){
      $scope.isAppointmentSelected = $window.localStorage.isAppointmentSelected;
    }
    // Set Appointment date for doctor
    if($window.localStorage.doctorAppointmentDate != null){
      $scope.doctorAppointmentDate = $window.localStorage.doctorAppointmentDate;
    }
    // Set doctor
    if($window.localStorage.selectedDoctor != null){
      $scope.selectedDoctor = $window.localStorage.selectedDoctor;
    }

    $scope.login = function() {

      if($scope.isAppointmentSelected){
        $state.go('app.custom.confirmAppointment');
      }else {
        $state.go('app.dashboard');
      }

     /* var dataToSend = {
        "clientId":"vellkare-client",
        "username":$scope.username,
        "password":$scope.password
      };
      $http.post('http://183.82.103.141:8080/vellkare/api/v0/auth/login',dataToSend).success(function(data, status){
        $window.localStorage['jwtToken'] = token;
        if($scope.isAppointmentSelected){
          $state.go('app.custom.confirmAppointment');
        }else {
          $state.go('app.dashboard');
        }
      }).error(function (data){
        alert("something went wrong");
      });*/
    };
  });
