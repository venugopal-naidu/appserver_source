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
      $scope.doctorsList = {
        "location": "Hyderabad",
        "hospital": null,
        "speciality": "General",
        "doctors": [
          {
            "class": "com.vellkare.core.Doctor",
            "id": 1,
            "awards": null,
            "degree1": "MBBS",
            "degree2": "M.D (OBG& GYNE)",
            "degree3": null,
            "degree4": null,
            "degree5": null,
            "description": "",
            "speciality": "General",
            "email": "SOMAJIGUDA@YASHODA.IN",
            "experience": 0,
            "fax": null,
            "gender": "FEMALE",
            "language": null,
            "name": "DR.SUJATHA KANDI",
            "phone": "040 6658 8499 DIAL EXTENSION: 165",
            "univ1": null,
            "univ2": null,
            "univ3": null,
            "univ4": null,
            "univ5": null,
            "velkareVerified": null,
            "website": null,
            "hospitals": [
              {
                "class": "com.vellkare.core.Hospital",
                "id": 240,
                "address1": "KHARKHANA ",
                "address2": "PLOT NO 37\/B, VASAVI NAGAR,NEAR TO KHARKHANA POLICE\nSTATION",
                "address3": "",
                "address4": "",
                "city": "HYDERABAD",
                "country": "",
                "district": "HYDERABAD",
                "email": "",
                "fax": "",
                "hosGeocode": "GH-TS-HYD-411",
                "name": "MAANYA SPECIALITY CLINIC",
                "phone": "040 3951 5134",
                "postalCode": "500009",
                "specialists": "ENT HOSPITALS, ENT SURGEON DOCTORS, PAEDIATRIC ENT DOCTORS",
                "state": "TELANGANA",
                "velkareVerified": "",
                "website": "",
                availability:[{"from": "1288323623006",
                  "to": "1288323623006"},
                  {"from": "1288323623006",
                    "to": "1288323623006"}]
              },
              {
                "class": "com.vellkare.core.Hospital",
                "id": 240,
                "address1": "KHARKHANA ",
                "address2": "PLOT NO 37\/B, VASAVI NAGAR,NEAR TO KHARKHANA POLICE\nSTATION",
                "address3": "",
                "address4": "",
                "city": "HYDERABAD",
                "country": "",
                "district": "HYDERABAD",
                "email": "",
                "fax": "",
                "hosGeocode": "GH-TS-HYD-411",
                "name": "MAANYA SPECIALITY CLINIC",
                "phone": "040 3951 5134",
                "postalCode": "500009",
                "specialists": "ENT HOSPITALS, ENT SURGEON DOCTORS, PAEDIATRIC ENT DOCTORS",
                "state": "TELANGANA",
                "velkareVerified": "",
                "website": "",
                availability:[{"from": "1288323623006",
                  "to": "1288323623006"},
                  {"from": "1288323623006",
                    "to": "1288323623006"}]
              }
            ]
          },
          {
            "class": "com.vellkare.core.Doctor",
            "id": 1,
            "awards": null,
            "degree1": "MBBS",
            "degree2": "M.D (OBG& GYNE)",
            "degree3": null,
            "degree4": null,
            "degree5": null,
            "speciality": "General",
            "description": "",
            "email": "SOMAJIGUDA@YASHODA.IN",
            "experience": 0,
            "fax": null,
            "gender": "FEMALE",
            "language": null,
            "name": "DR.SUJATHA KANDI",
            "phone": "040 6658 8499 DIAL EXTENSION: 165",
            "univ1": null,
            "univ2": null,
            "univ3": null,
            "univ4": null,
            "univ5": null,
            "velkareVerified": null,
            "website": null,
            "hospitals": [
              {
                "class": "com.vellkare.core.Hospital",
                "id": 240,
                "address1": "KHARKHANA ",
                "address2": "PLOT NO 37\/B, VASAVI NAGAR,NEAR TO KHARKHANA POLICE\nSTATION",
                "address3": "",
                "address4": "",
                "city": "HYDERABAD",
                "country": "",
                "district": "HYDERABAD",
                "email": "",
                "fax": "",
                "hosGeocode": "GH-TS-HYD-411",
                "name": "MAANYA SPECIALITY CLINIC",
                "phone": "040 3951 5134",
                "postalCode": "500009",
                "specialists": "ENT HOSPITALS, ENT SURGEON DOCTORS, PAEDIATRIC ENT DOCTORS",
                "state": "TELANGANA",
                "velkareVerified": "",
                "website": "",
                availability:[{"from": "1288323623006",
                  "to": "1288323623006"},
                  {"from": "1288323623006",
                    "to": "1288323623006"}]          },
              {
                "class": "com.vellkare.core.Hospital",
                "id": 240,
                "address1": "KHARKHANA ",
                "address2": "PLOT NO 37\/B, VASAVI NAGAR,NEAR TO KHARKHANA POLICE\nSTATION",
                "address3": "",
                "address4": "",
                "city": "HYDERABAD",
                "country": "",
                "district": "HYDERABAD",
                "email": "",
                "fax": "",
                "hosGeocode": "GH-TS-HYD-411",
                "name": "MAANYA SPECIALITY CLINIC",
                "phone": "040 3951 5134",
                "postalCode": "500009",
                "specialists": "ENT HOSPITALS, ENT SURGEON DOCTORS, PAEDIATRIC ENT DOCTORS",
                "state": "TELANGANA",
                "velkareVerified": "",
                "website": "",
                availability:[{"from": "1288323623006",
                  "to": "1288323623006"},
                  {"from": "1288323623006",
                    "to": "1288323623006"}]          }
            ]
          }
        ]
      }


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
      $scope.specialties = [
        "Anesthesiology",
        "Anterior segment services",
        "Cosmetic surgery",
        "Cosmetologist",
        "Diet"
      ];
      $scope.hospitals = [
        "Bhavya clinic",
        "Chandra clinic",
        "Divya clinic",
        "Sai swapna hospital",
        "Taraporewala nursing home",
        "Walkin clinic",
        "Tanvia hospital",
        "Swarna sai hospital",
        "Ayush hopital"
      ]
      ;
      /*var url = 'http://183.82.103.141:8080/vellkare/api/v0/search/specialitiesAndHospitals';
       var dataToSend = { location: 'Hyderabad'};
       $http.get(url,dataToSend).success(function(data){
       $scope.specialties = data['specialties'];
       $scope.hospitals = data['hospitals'];
       });*/
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
