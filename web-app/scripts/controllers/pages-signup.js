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
        });
    };
  })
  .controller('ConfirmOTPCtrl', function ($scope,$http, $compile, uiCalendarConfig, $state, $window, $stateParams) {
    $scope.otp;
    $scope.password;
    $scope.passwordInputType = 'password';
    $scope.showSetPassword = false;
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
    $scope.showHidePassword = function(){
      if ($scope.passwordInputType == 'password')
        $scope.passwordInputType = 'text';
      else
        $scope.passwordInputType = 'password';
    };

    $scope.confirmOTP = function() {
      var dataToSend = {
        "clientId":"vellkare-client",
        "uid":$scope.uid,
        "otp":$scope.otp,
        "password":$scope.password
      };
      var url = ajax_url_prefix + 'registration/confirm';
      $http.post(url,dataToSend).then(
        function(response){
          //$window.localStorage['jwtToken'] = response.data.token;
          if($scope.isAppointmentSelected){
            $state.go('app.custom.confirmAppointment');
          }else {
            $state.go('app.dashboard');
          }
        },function (data){
          alert("something went wrong");
        });
    };
    $scope.getVerifyUid = function(){
      var url = ajax_url_prefix + 'registration/verifyUid?uid='+$scope.uid;
      $http.get(url).then(function(response){
        $scope.isValidUid = response.data.isValidUid;
        $scope.isUserRegistered = response.data.isUserRegistered;
        if($scope.isValidUid){
          if(!$scope.isUserRegistered){
            $scope.showSetPassword = true;
          }else {
            $state.go('core.login');
          }
        }else {
          $scope.showSetPassword = false;
        }
      },function(data){
        $scope.showSetPassword = false;
      });
    };

    if($scope.uid != null){
      $scope.getVerifyUid();
    }
  });
