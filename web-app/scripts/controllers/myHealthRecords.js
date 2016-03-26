'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:TablesBootstrapCtrl
 * @description
 * # TablesBootstrapCtrl
 * Controller of the minovateApp
 */
app
  .controller('MyHealthRecordsCtrl', function ($scope, $resource, DTOptionsBuilder, DTColumnDefBuilder, $uibModal, $http, $window, $state) {
    $scope.accessToken = $window.localStorage.accessToken;
    $scope.tokenType = $window.localStorage.tokenType;
    $scope.getMyRecords = function(){
      var url = ajax_url_prefix + 'record/listRecords';
      $http.get(url,{ headers: {'Authorization': $scope.tokenType + ' '+ $scope.accessToken}}).then(function(response){
        $scope.records = response.data.records;
      },function (response){
        if(response['status'] == 401){
          $scope.formErrors = response.data['error'];
          if(response.data['error'] == 'invalid_token'){
            $state.go('site.login');
          }
        }
      });
    };

      /*  $scope.records = [
          { id:1,name: 'Dr. Satyanath\'s notes', type: 'Doctor notes', issuedDate: 'October 12, 2015', description: 'Appt notes from Dr.Satyanath'},
          { id:2,name: 'Backache prescription', type: 'Prescription', issuedDate: 'November 10, 2016', description: 'Backache medicines'},
          { id:3,name: 'X-ray report', type: 'Lab report', issuedDate: 'January 15, 2016', description: 'X-ray at Focus diagnostics'},
          { id:4,name: 'MRI report', type: 'Lab report', issuedDate: 'February 4, 2016', description: 'MRI of spine'},
        ];*/
    $scope.dtOptions = DTOptionsBuilder.newOptions().withPaginationType('full_numbers').withBootstrap();
    $scope.dtColumnDefs = [
      DTColumnDefBuilder.newColumnDef(0),
      DTColumnDefBuilder.newColumnDef(1),
      DTColumnDefBuilder.newColumnDef(2),
      DTColumnDefBuilder.newColumnDef(3),
      DTColumnDefBuilder.newColumnDef(4).notSortable()
    ];

    // Init upload health records modal.

    $scope.modalInput = {'appointmentId': ''};

    $scope.openUploadRecordModal = function(size) {

      var modalInstance = $uibModal.open({
        templateUrl: 'uploadHealthRecordModal',
        controller: 'ModalInstanceCtrl',
        size: size,
        resolve: {
          modalInput: function () {
            return $scope.modalInput;
          }
        }
      });

      modalInstance.result.then(function (selectedItem) {
        $scope.selected = selectedItem;
      }, function () {
        $log.info('Modal dismissed at: ' + new Date());
      });
    };

    $scope.downloadHealthRecord = function(recordId){
      var fileName = "test.pdf";
      var a = document.createElement("a");
      document.body.appendChild(a);
      a.style = "display: none";

      var url = ajax_url_prefix + 'user/my-health-records/view?recordId='+recordId;
      $http.get(url, { responseType: 'arraybuffer',headers: {'Authorization': $scope.tokenType + ' '+ $scope.accessToken} }).then(function (response) {
        var file = new Blob([response.data], {type: 'application/pdf'});
        var fileURL = window.URL.createObjectURL(file);
        a.href = fileURL;
        a.download = fileName;
        a.click();
      },function(response){
        if(response['status'] == 401){
          $scope.formErrors = response.data['error'];
          if(response.data['error'] == 'invalid_token'){
            $state.go('site.login');
          }
        }
      });
    }

    $scope.getMyRecords();

  })
  .controller('ModalInstanceCtrl', function ($scope, $uibModalInstance, modalInput, $http, $window, $state) {

    $scope.recordTypes = ['Consultation notes', 'Hospital admission records', 'Test results', 'X-rays'];
    $scope.recordType = { selected: $scope.recordTypes[0] };
    $scope.recordTypeId = 2;
    $scope.appointmentId = modalInput.appointmentId;

    $scope.clearRecordType = function($event) {
      $scope.recordType.selected = null;
      $event.preventDefault();
      $event.stopPropagation();
    };

    $scope.loadRecordTypes = function() {

      $http.post(url,dataToSend,{
        processData: false,
        transformRequest: angular.identity,
        headers: {'Authorization': $scope.tokenType + ' '+ $scope.accessToken}
      }).then(function(response) {
      });
    };



    $scope.uploadHealthRecord = function () {
      var url = ajax_url_prefix + 'record/upload';
      var  dataToSend = new FormData();
      dataToSend.append( 'file', $( '#recordFile' )[0].files[0] );
      dataToSend.append('recordTypeId', $scope.recordTypeId);

      var forData = $('form#uploadRecordForm').serializeArray();
      $.each(forData,function(key,input){
        dataToSend.append(input.name,input.value);
      });
      dataToSend.append('appointmentId',$scope.appointmentId);
      $scope.accessToken = $window.localStorage.accessToken;
      $scope.tokenType = $window.localStorage.tokenType;

      $http.post(url,dataToSend,{
        processData: false,
        transformRequest: angular.identity,
        headers: {'Authorization': $scope.tokenType + ' '+ $scope.accessToken}
      }).then(function(response){
        alert('Record uploaded');
        $uibModalInstance.dismiss('cancel');
      },function (response){
        $uibModalInstance.dismiss('cancel');
        if(response['status'] == 401){
          $scope.formErrors = response.data['error'];
          if(response.data['error'] == 'invalid_token'){
            $state.go('site.login');
          }
        }
      });

    };

    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };




    // Date picker configs

    $scope.today = function() {
      $scope.dt = new Date();
    };

    $scope.today();

    $scope.clear = function () {
      $scope.dt = null;
    };

    $scope.toggleMin = function() {
      $scope.minDate = $scope.minDate ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function($event) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope.opened = true;
    };

    $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 1,
      'class': 'datepicker'
    };

    $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];
  });
