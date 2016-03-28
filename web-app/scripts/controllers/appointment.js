'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:CalendarCtrl
 * @description
 * # CalendarCtrl
 * Controller of the minovateApp
 */
app
  .controller('AppointmentCtrl', function ($scope,$compile,uiCalendarConfig, $state,$window) {

    /* BEGIN CALENDAR CHANGES
    $scope.bookAppointment = function( date, jsEvent, view ){
      $window.localStorage.appointmentDate = date;
      $window.localStorage.setItem('isAppointmentSelected', true);
      // If not authenticated, redirect to login page
      if($window.localStorage.isUserLoggedIn == 'true'){
        $state.go('app.appointment.confirmAppointment');
      }else {
        $state.go('site.login');
      }

    };


    $scope.eventSources = [];


    $scope.uiConfig = {
      calendar:{
        defaultView: 'agendaWeek',
        minTime: "09:00:00",
        maxTime: "20:00:00",
        slotLabelInterval: "00:30:00",
        allDaySlot: false,
        height: 350,
        editable: true,
        aspectRatio: 1.5,
        header:{
          left: 'title',
          center: '',
          right: 'today prev,next'
        },
        dayClick: $scope.bookAppointment
      }
    };
     END CALENDAR CHANGES
     */


  })
  .controller('ConfirmAppointmentCtrl', function ($scope, $compile, uiCalendarConfig, $state, $window, $http) {
    $scope.instructions = "";
    $scope.formErrors = null;
    $scope.invalidTokenError = null;

    $scope.getDoctorAndHospital = function(doctorId, hospitalId){
      var url = ajax_url_prefix + 'search/doctorAndHospital?doctorId='+doctorId+'&hospitalId='+hospitalId;
      $http.get(url).then(function(response){
        $scope.doctor = response.data.doctor;
        $scope.hospital = response.data.hospital;
      });
    };

    $scope.getLab = function(labId){
      var url = ajax_url_prefix + 'search/lab?labId='+labId;
      $http.get(url).then(function(response){
        $scope.lab = response.data.lab;
      });
    };

    $scope.selectedAppointment = $window.localStorage.selectedAppointment;

    if($scope.selectedAppointment == 'Doctor'){
      $scope.getDoctorAndHospital($window.localStorage.selectedDoctor, $window.localStorage.selectedHospital);
    }else if($scope.selectedAppointment == 'Lab'){
      $scope.getLab($window.localStorage.selectedLab);
    }

    $scope.fromTime = moment($window.localStorage.appointmentStartTime).format('Do MMM YYYY hh:mm a');
    $scope.toTime = moment($window.localStorage.appointmentEndTime).format('Do MMM YYYY hh:mm a');

    $scope.confirmDoctorAppointment = function() {
      $scope.accessToken = $window.localStorage.accessToken;
      $scope.tokenType = $window.localStorage.tokenType;

      var url = ajax_url_prefix + 'appointment/create';
      var dataToSend = {
        "fromTime":$window.localStorage.appointmentStartTime,
        "toTime":$window.localStorage.appointmentEndTime,
        "doctorId": $window.localStorage.selectedDoctor,
        "hospitalId": $window.localStorage.selectedHospital,
        "notes": $scope.instructions
      };
      $http.post(url,dataToSend,{
        headers: {'Authorization': $scope.tokenType + ' '+ $scope.accessToken}
      }).then(function(response){

        $window.localStorage.removeItem('isAppointmentSelected');
        $window.localStorage.removeItem('appointmentStartTime');
        $window.localStorage.removeItem('appointmentEndTime');
        $window.localStorage.removeItem('selectedDoctor');
        $window.localStorage.removeItem('selectedHospital');

        $state.go('app.myAppointments');
      },function (response){
        if(response['status'] == 401){
          $scope.formErrors = response.data['error'];
          if(response.data['error'] == 'invalid_token'){
            $state.go('site.login');
          }
        }
      });
    };

    $scope.confirmLabAppointment = function() {
      $scope.accessToken = $window.localStorage.accessToken;
      $scope.tokenType = $window.localStorage.tokenType;

      var url = ajax_url_prefix + 'appointment/create';
      var dataToSend = {
        "fromTime":$window.localStorage.appointmentStartTime,
        "toTime":$window.localStorage.appointmentEndTime,
        "labId": $window.localStorage.selectedLab,
        "notes": $scope.instructions
      };
      $http.post(url,dataToSend,{
        headers: {'Authorization': $scope.tokenType + ' '+ $scope.accessToken}
      }).then(function(response){

        $window.localStorage.removeItem('isAppointmentSelected');
        $window.localStorage.removeItem('appointmentStartTime');
        $window.localStorage.removeItem('appointmentEndTime');
        $window.localStorage.removeItem('selectedLab');

        $state.go('app.myAppointments');
      },function (response){
        if(response['status'] == 401){
          $scope.formErrors = response.data['error'];
          if(response.data['error'] == 'invalid_token'){
            $state.go('site.login');
          }
        }
      });
    };
  });
