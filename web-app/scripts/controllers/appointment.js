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

    $scope.bookAppointment = function( date, jsEvent, view ){
      $window.localStorage.appointmentDate = date;
      $window.localStorage.setItem('isAppointmentSelected', true);
      // If not authenticated, redirect to login page
      if($window.localStorage.isUserLoggedIn == 'true'){
        $state.go('app.custom.confirmAppointment');
      }else {
        $state.go('core.login');
      }

    };
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

    $scope.selectedAppointment = $window.localStorage.selectedAppointment;
    $scope.selectedDoctor = {
      name: 'Dr. Satyanath R V',
      degree : 'M.D.',
      specialties : 'Dermatology, General Medicine',
      hospitals: [
        {
          name: 'Continental Hospitals',
          address : 'Gachibowli',
          phone : '(040) 456-7890'
        }
      ]
    };

    $scope.selectedLab = {
      name: 'VIJAYA DIAGNOSTIC CENTER'
    };
    $scope.confirmAppointment = function() {
      $scope.accessToken = $window.localStorage.accessToken;
      $scope.tokenType = $window.localStorage.tokenType;
      var url = ajax_url_prefix + 'appointment/create';
      var dataToSend = {
        "fromTime":"2016-04-30T10:10:10",
        "toTime":"2016-04-30T12:10:10",
        "doctorId": $window.localStorage.selectedDoctor,
        "hospitalId": $window.localStorage.selectedHospital,
        "notes": $scope.doctorNotes
      };
      $http.post(url,dataToSend,{
        headers: {'Authorization': $scope.tokenType + ' '+ $scope.accessToken}
      }).then(function(response){

        $window.localStorage.removeItem('isAppointmentSelected');
        $window.localStorage.removeItem('appointmentDate');
        $window.localStorage.removeItem('selectedDoctor');
        $window.localStorage.removeItem('selectedLab');
        $window.localStorage.removeItem('selectedDoctor');
        $window.localStorage.removeItem('selectedHospital');

        $state.go('app.custom.myAppointments');
      },function (data){
        alert("something went wrong");
      });
    };
  });
