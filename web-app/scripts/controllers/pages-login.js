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
    $scope.formErrors = null;
    $scope.userNameError = null;
    $scope.passwordError = null;

    $scope.isAppointmentSelected = false;
    $scope.selectedDoctor = null;
    $scope.appointmentStartTime = null;
    $scope.appointmentEndTime = null;
    // Set isAppointmentSelected
    if($window.localStorage.isAppointmentSelected != null){
      $scope.isAppointmentSelected = $window.localStorage.isAppointmentSelected;
    }
    // Set Appointment date for doctor
    if($window.localStorage.appointmentStartTime != null){
      $scope.appointmentStartTime = $window.localStorage.appointmentStartTime;
    }
      if($window.localStorage.appointmentEndTime != null){
          $scope.appointmentEndTime = $window.localStorage.appointmentEndTime;
      }
    // Set doctor
    if($window.localStorage.selectedDoctor != null){
      $scope.selectedDoctor = $window.localStorage.selectedDoctor;
    }

    $scope.login = function() {
      var dataToSend = {
        "clientId":"velkare-client",
        "username":$scope.username,
        "password":$scope.password
      };
      var url = ajax_url_prefix + 'auth/login';
      $http.post(url,dataToSend).then(function(response){
        $window.localStorage.isUserLoggedIn =  true;
        // Store access Token into Local storage
        $window.localStorage.accessToken = response.data.token['access_token'];
        $window.localStorage.expiresIn = response.data.token['expires_in'];
        $window.localStorage.refreshToken = response.data.token['refresh_token'];
        $window.localStorage.tokenType = response.data.token['token_type'];
        $window.localStorage.scope = response.data.token['scope'];
        // Store user info into Local storage
        $window.localStorage.firstName = response.data.member['firstName'];
        $window.localStorage.lastName = response.data.member['lastName'];
        $window.localStorage.email = response.data.member['email'];
        $window.localStorage.primaryPhone = response.data.member['primaryPhone'];
        $window.localStorage.userId = response.data.member['id'];

        if($scope.isAppointmentSelected){
          $state.go('app.appointment.confirmAppointment');
        }else {
          $state.go('app.dashboard');
        }
      },function (response){
        if(response['status'] == 400){
          $scope.formErrors = response.data['errors'];
          $.each($scope.formErrors, function(key, value) {
            if(value.propertyName == 'username'){
              $scope.userNameError = value.message;
            }
            else if(value.propertyName == 'password'){
              $scope.passwordError = value.message;
            }
          });
        }
      });
    };
  });
