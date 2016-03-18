'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:PagesSignupCtrl
 * @description
 * # PagesSignupCtrl
 * Controller of the minovateApp
 */
app.controller('SignupCtrl', function ($scope, $state, $http, $window, $location) {
    $scope.emailError = null;
    $scope.formErrors = null;
    $scope.signUp = function() {
      $scope.emailError = null;
      var dataToSend = {
        "clientId":"vellkare-client",
        "firstName":$scope.firstName,
        "lastName":$scope.lastName,
        "phoneNumber":$scope.phoneNumber,
        "email":$scope.email,
        "tncChecked":$scope.tncChecked
      };
      var registerUrl = ajax_url_prefix + 'registration/create';
      $http.post(registerUrl,dataToSend).then(
        function(response){
          if(response.data != null){
            if(response.data['registration'] == 'success' && response.data['uid'] != null){
              $state.go('core.confirmOTP', { uid: response.data['uid'] });
            }
          }
        },function (response){
          if(response['status'] == 400){
            $scope.formErrors = response.data['errors'];
            $.each($scope.formErrors, function(key, value) {
              if(value.propertyName == 'email'){
                $scope.emailError = value.message;
              }
            });
          }
        })
    };
  })
  .controller('ConfirmOTPCtrl', function ($scope,$compile,uiCalendarConfig, $state, $window, $stateParams) {

    $scope.uid = $stateParams.hasOwnProperty('uid') ? $stateParams['uid'] : null;
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
