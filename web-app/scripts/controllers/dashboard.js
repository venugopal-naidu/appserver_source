'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:DashboardCtrl
 * @description
 * # DashboardCtrl
 * Controller of the minovateApp
 */
app
  .controller('DashboardCtrl', function($scope,$http,$mdBottomSheet,$window){

    /* Declare and Init scope variables */
    $scope.doctorsList = [];
    $scope.labsList = [];
    $scope.specialities = [];
    $scope.speciality = {};
    $scope.hospitals = [];
    $scope.hospital = {};
    $scope.tests = [];
    $scope.test = {};
    $scope.labs = [];
    $scope.lab = {};

    $scope.searchTypes = [
      {id: 1, name: 'Doctor'},
      {id: 2, name: 'Lab'}
    ];
    $scope.searchType = { selected: $scope.searchTypes[0] };


    $scope.searchStarted = false;



    /* Handle drop down changes */
    $scope.searchTypeChanged = function(item){

    };

    $scope.specialityChanged = function(speciality){
      $scope.getHospitals(speciality);
    };

    $scope.hospitalChanged = function(hospital){
      $scope.getSpecialities(hospital);
    };

    $scope.testChanged = function(testName){
      $scope.getLabs(testName);
    };

    $scope.labChanged = function(labName){
      $scope.getTests(labName);
    };


    /* Find and clear search result */

    $scope.findDoctor = function(){
      $scope.searchStarted = true;
      var url = ajax_url_prefix + 'search/doctor';
      var dataToSend = { "location": 'Hyderabad', "speciality": $scope.speciality.selected ,"hospital": $scope.hospital.selected };
      $http.post(url,dataToSend).success(function(data){
        $scope.doctorsList = data.doctors;
      });
    };

    $scope.clearDoctorsResult = function(){
      $scope.doctorsList = [];
      $scope.speciality.selected = null;
      $scope.hospital.selected = null;
    };

    $scope.findLab = function(){
      var url = ajax_url_prefix + 'search/lab/listLabs';
      var dataToSend = { "location": 'Hyderabad', "testName": $scope.test.selected ,"labName": $scope.lab.selected };
      $http.post(url,dataToSend).success(function(data){
        $scope.labsList = data.doctors;
      });
    };
    $scope.clearLabsResult = function(){
      $scope.labsList = [];
      $scope.test.selected = null;
      $scope.lab.selected = null;
    };



    /* Find Doctors or labs on click find */
    $scope.findDoctorOrLab = function(){

    };




    /* Snippets for all ajax calls */

    $scope.getSpecialitiesAndHospitals = function(){
      var location = 'Hyderabad';
      var url = ajax_url_prefix + 'search/specialitiesAndHospitals?location='+location;
       $http.get(url).success(function(data){
         $scope.specialities = data.specialties;
         $scope.hospitals = data.hospitals;
       });
    };

    $scope.getSpecialities = function(hospital){
      var url = ajax_url_prefix + 'search/hospital/specialities?location='+location+'&hospital='+hospital;
      $http.get(url).success(function(data){
        $scope.specialities = data.specialities;
      });
    };

    $scope.getHospitals = function(speciality){
      var location = 'Hyderabad';
      var url = ajax_url_prefix + 'search/hospitalNames?location='+location+'&speciality='+speciality;
      $http.get(url).success(function(data){
        $scope.hospitals = data.hospitals;
      });
    };


    $scope.getTestsAndLabs = function(){
      var location = 'Hyderabad';
      var url = ajax_url_prefix + 'search/lab/testsAndLabs?location='+location;
      $http.get(url).success(function(data){
        $scope.tests = data.tests;
        $scope.labs = data.labs;
      });
    };

    $scope.getTests = function(labName){
      var location = 'Hyderabad';
      var url = ajax_url_prefix + 'search/lab/listTests?location='+location+'&labName='+labName;
      $http.get(url).success(function(data){
        $scope.tests = data.tests;
      });
    };

    $scope.getLabs = function(testName){
      var location = 'Hyderabad';
      var url = ajax_url_prefix + 'search/lab/listLabNames?location='+location+'&testName='+testName;
      $http.get(url).success(function(data){
        $scope.labs = data.labs;
      });
    };


    /* Handle clear selected options for all drop downs */

    $scope.clearSelectedSpeciality = function($event) {
      $scope.speciality.selected = null;
      $event.preventDefault();
      $event.stopPropagation();
    };

    $scope.clearSelectedHospital = function($event) {
      $scope.hospital.selected = null;
      $event.preventDefault();
      $event.stopPropagation();
    };

    $scope.clearSelectedTest = function($event) {
      $scope.test.selected = null;
      $event.preventDefault();
      $event.stopPropagation();
    };

    $scope.clearSelectedLab = function($event) {
      $scope.lab.selected = null;
      $event.preventDefault();
      $event.stopPropagation();
    };

    /* Toggle and render calendar view */

    $scope.showDoctorAppointmentCalendar = function(doctor,index){
      // Hide all other opened calendars
      $('.doctorAppointment.collapse').collapse('hide');
      // Show current calendar
      $('#doctor_calendar_'+index).collapse('toggle');
      // Render calendar view for this screen size
      $('#doctorAppointment_'+index).fullCalendar('render');
      // Save doctor data to local storage
      $window.localStorage.selectedDoctor = doctor.id;
    };


    /* On load calls */
    $scope.getTestsAndLabs();
    $scope.getSpecialitiesAndHospitals();



  });
