'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:PagesSignupCtrl
 * @description
 * # PagesSignupCtrl
 * Controller of the minovateApp
 */
app.controller('SignupCtrl', function ($scope, $state, $http, $window) {
    $scope.signUp = function() {
        $state.go('core.confirmOTP');
        /*var dataToSend = {
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
        });*/

    };
})
  .controller('ConfirmOTPCtrl', function ($scope,$compile,uiCalendarConfig, $state,$window) {
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

      $scope.confirmOTP = function() {

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
