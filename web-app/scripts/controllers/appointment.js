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
     END CALENDAR CHANGES
    */


    $scope.eventSources = [];

    /* config object */
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

  })
  .controller('ConfirmAppointmentCtrl', function ($scope, $compile, uiCalendarConfig, $state, $window, $http) {
    $scope.instructions = "";
    $scope.formErrors = null;
    $scope.invalidTokenError = null;

    $scope.getDoctorAndHospital = function(doctorId, hospitalId){
      var url = ajax_url_prefix + 'doctorAndHospital?doctorId='+doctorId+'&hospitalId='+hospitalId;
      $http.get(url).then(function(response){
        /*$scope.doctor = response.data.doctor;
        $scope.hospitalDetails = response.data.hospital;*/
      });
    };

    $scope.getLab = function(labId){
      var url = ajax_url_prefix + 'doctorAndHospital?labId='+labId;
      $http.get(url).then(function(response){
        $scope.doctor = response.data.doctor;
        $scope.hospitalDetails = response.data.hospital;
      });
    };

    $scope.selectedAppointment = $window.localStorage.selectedAppointment;
    $scope.doctor = {
      name: 'Dr. Satyanath R V',
      degree1 : 'M.D.',
      degree2 : '',
      degree3 : '',
      degree4 : '',
      degree5 : '',
      specialties : ['Dermatology', 'General Medicine']
    };
    $scope.hospital = {
      name: 'GLOBAL HOSPITAL',
      address1: 'L.B NAGAR',
      address2: 'L.B NAGAR'
    };

    $scope.lab = {
      name: 'VIJAYA DIAGNOSTIC CENTER',
      address1: 'L.B NAGAR',
      address2: 'L.B NAGAR'
    };

    if($scope.selectedAppointment == 'Doctor'){
      $scope.getDoctorAndHospital($window.localStorage.selectedDoctor, $window.localStorage.selectedHospital);
    }else if($scope.selectedAppointment == 'Lab'){
      $scope.getLab($window.localStorage.selectedLab);
    }
    $scope.confirmDoctorAppointment = function() {
      $scope.accessToken = $window.localStorage.accessToken;
      $scope.tokenType = $window.localStorage.tokenType;
      $scope.fromTime = $window.localStorage.appointmentDate;
      var url = ajax_url_prefix + 'appointment/create';
      var dataToSend = {
        "fromTime":$window.localStorage.appointmentDate,
        "toTime":"2016-04-30T12:10:10",
        "doctorId": $window.localStorage.selectedDoctor,
        "hospitalId": $window.localStorage.selectedHospital,
        "notes": $scope.instructions
      };
      $http.post(url,dataToSend,{
        headers: {'Authorization': $scope.tokenType + ' '+ $scope.accessToken}
      }).then(function(response){

        $window.localStorage.removeItem('isAppointmentSelected');
        $window.localStorage.removeItem('appointmentDate');
        $window.localStorage.removeItem('selectedDoctor');
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
      $scope.fromTime = $window.localStorage.appointmentDate;
      var url = ajax_url_prefix + 'appointment/create';
      var dataToSend = {
        "fromTime":$window.localStorage.appointmentDate,
        "toTime":"2016-04-30T12:10:10",
        "labId": $window.localStorage.selectedLab,
        "notes": $scope.instructions
      };
      $http.post(url,dataToSend,{
        headers: {'Authorization': $scope.tokenType + ' '+ $scope.accessToken}
      }).then(function(response){

        $window.localStorage.removeItem('isAppointmentSelected');
        $window.localStorage.removeItem('appointmentDate');
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
