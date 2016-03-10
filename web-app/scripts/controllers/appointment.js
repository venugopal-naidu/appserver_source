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
      alert('Clicked on: ' + date);
      $window.localStorage.doctorAppointmentDate = date;
      // If not authenticated user redirect to login page
      $state.go('core.login');

    };

    /* config object */
    $scope.uiConfig = {
      calendar:{
        defaultView: 'agendaWeek',
        selectHelper: true,
        editable: true,
        allDaySlot: false,
        height: 350,
        header:false,
        dayClick: $scope.bookAppointment
      }
    };
  });
