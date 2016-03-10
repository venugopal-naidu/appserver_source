'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:PagesLoginCtrl
 * @description
 * # PagesLoginCtrl
 * Controller of the minovateApp
 */
app
  .controller('LoginCtrl', function ($scope, $state, $http, $window) {
      $scope.selectedDoctor = null;
      $scope.doctorAppointmentDate = null;
      $scope.doctorAppointmentDate = $window.localStorage.doctorAppointmentDate;
      if($window.localStorage.selectedDoctor != null){
          $scope.selectedDoctor = {
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
          };
      }

    $scope.login = function() {
        var dataToSend = {
            "clientId":"vellkare-client",
            "username":$scope.username,
            "password":$scope.password
        };
        $http.post('http://183.82.103.141:8080/vellkare/api/v0/auth/login',dataToSend).success(function(data, status){
            $window.localStorage['jwtToken'] = token;
            $state.go('app.dashboard');
        }).error(function (data){
            alert("something went wrong");
        });
    };
      alert("Selected doctor:" + $window.localStorage.selectedDoctor);
  });
