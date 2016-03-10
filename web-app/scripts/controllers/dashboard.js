'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:DashboardCtrl
 * @description
 * # DashboardCtrl
 * Controller of the minovateApp
 */
app
  .controller('DashboardCtrl', function($scope,$http,$mdBottomSheet){

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

    $scope.searchTypeChanged = function(item){
      /*alert(item.name);*/
    };

    $scope.testCategories = ['Image Hospital','Pacific Hospitals','Fernandez Hospital','Apollo Hospitals','GNRC Hospitals'];
    $scope.testCategory = {};

    $scope.testCenters = ['Image Hospital','Pacific Hospitals','Fernandez Hospital','Apollo Hospitals','GNRC Hospitals'];
    $scope.testCenter = {};


    $scope.findDoctor = function(){
      $scope.doctorsList = [
        {
          name: 'Dr. Satyanath R V',
          degree : 'M.D., Skin & STD',
          specialities : 'Homeopath , General Physician',
          hospitals: [
            {
              name: 'Revive Multi-Specialty Clinics & Fertility Centre',
              address : '795 Folsom Ave, Suite 600, San Francisco, CA 94107',
              phone : '(123) 456-7890'
            },
            {
              name: 'Revive Multi-Specialty Clinics & Fertility Centre',
              address : '795 Folsom Ave, Suite 600, San Francisco, CA 94107',
              phone : '(123) 456-7890'
            }
          ]
        },
        {
          name: 'Dr. Satyanath R V',
          degree : 'M.D., Skin & STD',
          specialities : 'Homeopath , General Physician',
          hospitals: [
            {
              name: 'Revive Multi-Specialty Clinics & Fertility Centre',
              address : '795 Folsom Ave, Suite 600, San Francisco, CA 94107',
              phone : '(123) 456-7890'
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
            address : '795 Folsom Ave, Suite 600, San Francisco, CA 94107',
            phone : '(123) 456-7890'
          }
        },
        {
          name: 'Vijaya Diagnostic Centre',
          contact: {
            address : '795 Folsom Ave, Suite 600, San Francisco, CA 94107',
            phone : '(123) 456-7890'
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

      $scope.showCalendar = function(index){
          $('#doctorAppointment_'+index).fullCalendar('render');
      }

    $scope.getSpecialitiesAndHospitals();
  });
