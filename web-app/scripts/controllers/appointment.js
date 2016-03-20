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
      // If not authenticated user redirect to login page
      $state.go('core.login');
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
  .controller('ConfirmAppointmentCtrl', function ($scope,$compile,uiCalendarConfig, $state,$window) {

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
      $window.localStorage.removeItem('isAppointmentSelected');
      $window.localStorage.removeItem('appointmentDate');
      $window.localStorage.removeItem('selectedDoctor');
      $window.localStorage.removeItem('selectedLab');

      $state.go('app.calendar');

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
