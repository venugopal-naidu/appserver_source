'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:DashboardCtrl
 * @description
 * # DashboardCtrl
 * Controller of the minovateApp
 */
app
  .controller('DashboardCtrl', function($scope,$http,$mdBottomSheet,$window, $state){

    $scope.$parent.setUser();

    /* Declare and Init scope variables */
    $scope.doctorsList = [];
    $scope.labsList = [];
    $scope.specialties = [];
    $scope.specialty = { selected: '' };
    $scope.hospitals = [];
    $scope.hospital = {selected: ''};
    $scope.tests = [];
    $scope.test = {};
    $scope.labs = [];
    $scope.lab = {};
    $scope.appUrl = appUrl;
    $scope.noDoctorsFound = false;

    $scope.searchTypes = [
      {id: 1, name: 'Doctor'},
      {id: 2, name: 'Lab'}
    ];
    $scope.searchType = { selected: $scope.searchTypes[0] };


    $scope.searchStarted = false;



    /* Handle drop down changes */
    $scope.searchTypeChanged = function(item){

    };

    $scope.specialtyChanged = function(specialty){
      $scope.getHospitals(specialty);
    };

    $scope.hospitalChanged = function(hospital){
      $scope.getspecialties(hospital);
    };

    $scope.testChanged = function(testName){
      $scope.getLabs(testName);
    };

    $scope.labChanged = function(labName){
      $scope.getTests(labName);
    };


    /* Find and clear search result */

    $scope.findDoctor = function(){
  /*
   TEST DATA
   $scope.doctorsList = [{"awards":"","degree1":"MBBS","degree2":"MD (INTERNAL MEDICINE)","degree3":"ABIM","degree4":"","degree5":"","description":null,"experience":26,"gender":null,"hospitals":[{"address1":"JUBILEE HILLS","address2":"JUBILEE HILLS","address3":"","address4":"","availability":{"MONDAY":[{"from":"09:00:00","to":"21:00:00"}],"TUESDAY":[{"from":"09:00:00","to":"21:00:00"}],"WEDNESDAY":[{"from":"09:00:00","to":"21:00:00"}],"THURSDAY":[{"from":"09:00:00","to":"21:00:00"}],"FRIDAY":[{"from":"09:00:00","to":"21:00:00"}],"SATURDAY":[{"from":"09:00:00","to":"21:00:00"}],"SUNDAY":[{"from":"09:00:00","to":"21:00:00"}]},"id":652,"name":"APOLLO HEALTH CITY"}],
            "id":50,"language":"ENGLISH,HINDI,TELUGU","name":"DR.INDIRA RAMASAHAYAM REDDY","photoUrl":"/images/doctor/50.jpeg","specialties":["Internal medicine"],"univ1":"","univ2":"","univ3":"","univ4":"","univ5":"","velkareVerified":false}];
*/
      $scope.searchStarted = true;
        $scope.noDoctorsFound = false;
      var url = ajax_url_prefix + 'search/doctor';
      var dataToSend = { "location": 'Hyderabad', "specialty": $scope.specialty.selected ,"hospital": $scope.hospital.selected };
      $http.post(url,dataToSend).then(function(response){
        if(response.data.doctors != null && response.data.doctors.length > 0){
          $scope.doctorsList = response.data.doctors;
          $scope.noDoctorsFound = false;
        }else {
          $scope.noDoctorsFound = true;
        }

      },function(response){
        $scope.noDoctorsFound = true;
      });
    };

    $scope.clearDoctorsResult = function(){
      $scope.getspecialtiesAndHospitals();
      $scope.doctorsList = [];
      $scope.specialty.selected = null;
      $scope.hospital.selected = null;
    };

    $scope.findLab = function(){
      var url = ajax_url_prefix + 'search/lab/listLabs';
      var dataToSend = { "location": 'Hyderabad', "testName": $scope.test.selected ,"labName": $scope.lab.selected };
      $http.post(url,dataToSend).success(function(data){
        $scope.labsList = data.labs;
      });
    };
    $scope.clearLabsResult = function(){
      $scope.getTestsAndLabs();
      $scope.labsList = [];
      $scope.test.selected = null;
      $scope.lab.selected = null;
    };



    /* Find Doctors or labs on click find */
    $scope.findDoctorOrLab = function(){

    };




    /* Snippets for all ajax calls */

    $scope.getspecialtiesAndHospitals = function(){
      var location = 'Hyderabad';
      var url = ajax_url_prefix + 'search/specialtiesAndHospitals?location='+location;
       $http.get(url).success(function(data){
         $scope.specialties = data.specialties;
         $scope.hospitals = data.hospitals;
       });
    };

    $scope.getspecialties = function(hospital){
      var location = 'Hyderabad';
      var url = ajax_url_prefix + 'search/hospital/specialties?location='+location+'&hospital='+hospital;
      $http.get(url).success(function(data){
        $scope.specialties = data.specialties;
      });
    };

    $scope.getHospitals = function(specialty){
      var location = 'Hyderabad';
      var url = ajax_url_prefix + 'search/hospitalNames?location='+location+'&specialty='+specialty;
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

    $scope.clearSelectedspecialty = function($event) {
      $scope.specialty.selected = null;
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

      $scope.daysDisplayed=["2016-03-25", "2016-03-26", "2016-03-27"];
      $scope.timeSlots = ["09", "10", "11", "12", "01", "02", "03", "04", "05", "06", "07", "08"];
      $scope.displayCalendarIndex = -1;
    /* Toggle and render calendar view */

    $scope.showDoctorAppointmentCalendar = function(doctor,index,hospitalId){
      if(hospitalId != null && doctor.id != null){
/*
BEGIN CALENDAR CHANGES
        // Hide all other opened calendars
        $('.doctorAppointment.collapse').collapse('hide');
        // Show current calendar
        $('#doctor_calendar_'+index).collapse('toggle');
        // Render calendar view for this screen size
        $('#doctorAppointment_'+index).fullCalendar('render');

        END CALENDAR CHANGES
        */
          $scope.displayCalendarIndex = index;

        $window.localStorage.selectedAppointment = 'Doctor';
        // Save doctor data to local storage
        $window.localStorage.selectedDoctor = doctor.id;
        $window.localStorage.selectedHospital = hospitalId;
      }else {
        alert('Please select a hospital');
      }


    };

    $scope.showLabAppointmentCalendar = function(lab,index){

      if(lab != null && lab.id != null){
  /*
   BEGIN CALENDAR CHANGES
        // Hide all other opened calendars
        $('.labAppointment.collapse').collapse('hide');
        // Show current calendar
        $('#lab_calendar_'+index).collapse('toggle');
        // Render calendar view for this screen size
        $('#labAppointment_'+index).fullCalendar('render');
         END CALENDAR CHANGES
         */
          $scope.displayCalendarIndex = index;
        // Save doctor data to local storage
        $window.localStorage.selectedAppointment = 'Lab';
        $window.localStorage.selectedLab = lab.id;
      }else {
        alert('Please select a Lab');
      }

    };


      $scope.bookAppointment = function( day, timeSlot, startSubTimeSlot, endSubTimeSlot) {
          $window.localStorage.appointmentStartTime = $scope.daysDisplayed[day]+'T'+timeSlot+':'+startSubTimeSlot+':00';
          $window.localStorage.appointmentEndTime = $scope.daysDisplayed[day]+'T'+timeSlot+':'+endSubTimeSlot+':00';
          $window.localStorage.setItem('isAppointmentSelected', true);
          // If not authenticated, redirect to login page
          if($window.localStorage.isUserLoggedIn == 'true'){
              $state.go('app.appointment.confirmAppointment');
          }else {
              $state.go('site.login');
          }

      };

    /* On load calls */
    $scope.getTestsAndLabs();
    $scope.getspecialtiesAndHospitals();



  });
