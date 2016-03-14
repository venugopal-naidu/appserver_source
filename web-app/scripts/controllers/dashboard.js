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

    $scope.doctorsList = [];
    $scope.labsList = [];
    $scope.specialties = [];
    $scope.specialty = null;
    $scope.hospitals = [];
    $scope.hospital = null;

    $scope.testCategories = [];
    $scope.testCategory = null;
    $scope.testCenters = [];
    $scope.testCenter = null;



    $scope.searchTypes = [
      {id: 1, name: 'Doctor'},
      {id: 2, name: 'Lab'}
    ];
    $scope.searchType = { selected: $scope.searchTypes[0] };
      $scope.searchStarted = false;
    $scope.searchTypeChanged = function(item){
      /*alert(item.name);*/
    };

    $scope.testCategories = ['Image Hospital','Pacific Hospitals','Fernandez Hospital','Apollo Hospitals','GNRC Hospitals'];
    $scope.testCategory = {};

    $scope.testCenters = ['Image Hospital','Pacific Hospitals','Fernandez Hospital','Apollo Hospitals','GNRC Hospitals'];
    $scope.testCenter = {};


    $scope.findDoctor = function(){
        $scope.searchStarted = true;
            $scope.doctorsList = [
        {
          id: 1,
          name: 'Dr. Satyanath R V',
          degree : 'M.D.',
          specialities : 'Dermatology , General Medicine',
          hospitals: [
            {
              name: 'Continental Hospitals',
              address : 'Gachibowli',
              phone : '(040) 456-7890'
            }
          ]
        },
        {
          id: 2,
          name: 'Dr. Vineet K',
          degree : 'M.D., FRCS',
          specialities : 'Dermatology',
          hospitals: [
            {
              name: 'Apollo Hospital',
              address : 'Jubilee Hills, Hyderabad',
              phone : '(040) 788-9988'
            }
          ]
        },
          {
              id: 3,
              name: 'Dr. Aparana',
              degree : 'M.D.',
              specialities : 'Pediatric Dermatology',
              hospitals: [
                  {
                      name: 'Rainbow Hospitals',
                      address : 'Madhapur, Hyderabad',
                      phone : '(040) 456-7890'
                  }
              ]
          }
      ];
    };

    $scope.clearDoctorsResult = function(){
      $scope.doctorsList = [];
      $scope.specialty = null;
      $scope.hospital = null;
    };

    $scope.findLab = function(){
      $scope.labsList = [
        {
          name: 'Vijaya Diagnostic Centre',
          contact: {
            address : 'Ameerpet, Hyderabad',
            phone : '(040) 956-7890'
          }
        },
        {
          name: 'Focus Diagnostics',
          contact: {
            address : 'Nacharam, Hyderabad',
            phone : '(040) 456-7890'
          }
        }
      ];
    };
    $scope.clearLabsResult = function(){
      $scope.labsList = [];
      $scope.testCategory = null;
      $scope.testCenter = null;
    };



    /* Find Doctors or labs on click find */
    $scope.findDoctorOrLab = function(){

    };
    $scope.getSpecialitiesAndHospitals = function(){
      $scope.data=[];
      $scope.specialties = ['Dentist','Dermatology‎','Anesthesiology','Emergency Medicine','Hand Surgery'];
      $scope.hospitals = ['Image Hospital','Pacific Hospitals','Fernandez Hospital','Apollo Hospitals','GNRC Hospitals'];
      var url = 'http://183.82.103.141:8080/vellkare/api/v0/search/specialitiesAndHospitals';
      var dataToSend = { location: 'Hyderabad'};
      $http.get(url,dataToSend).success(function(data){
        $scope.specialties = ['Dentist','Dermatology‎','Anesthesiology','Emergency Medicine','Hand Surgery'];
        $scope.hospitals = ['Image Hospital','Pacific Hospitals','Fernandez Hospital','Apollo Hospitals','GNRC Hospitals'];
      });
    };

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
    $scope.getSpecialitiesAndHospitals();
  });
