'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:TablesBootstrapCtrl
 * @description
 * # TablesBootstrapCtrl
 * Controller of the minovateApp
 */
app
  .controller('MyHealthRecordsCtrl', function ($scope, $resource, DTOptionsBuilder, DTColumnDefBuilder, $uibModal) {
    $scope.records = [
      { name: 'Dr. Satyanath\'s notes', type: 'Doctor notes', issuedDate: 'October 12, 2015', comments: 'Appt notes from Dr.Satyanath'},
      { name: 'Backache prescription', type: 'Prescription', issuedDate: 'November 10, 2016', comments: 'Backache medicines'},
      { name: 'X-ray report', type: 'Lab report', issuedDate: 'January 15, 2016', comments: 'X-ray at Focus diagnostics'},
      { name: 'MRI report', type: 'Lab report', issuedDate: 'February 4, 2016', comments: 'MRI of spine'},
    ];
    $scope.dtOptions = DTOptionsBuilder.newOptions().withPaginationType('full_numbers').withBootstrap();
    $scope.dtColumnDefs = [
      DTColumnDefBuilder.newColumnDef(0),
      DTColumnDefBuilder.newColumnDef(1),
      DTColumnDefBuilder.newColumnDef(2),
      DTColumnDefBuilder.newColumnDef(3).notSortable()
    ];


    // Init upload health records modal.

    $scope.modalInput = ['item1', 'item2', 'item3'];

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

  })
  .controller('ModalInstanceCtrl', function ($scope, $uibModalInstance, modalInput) {
    $scope.recordType= {};
    $scope.recordTypes = ['Consultation notes', 'Hospital admission records', 'Test results', 'X-rays'];

    $scope.clearRecordType = function($event) {
      $scope.recordType.selected = undefined;
      $event.preventDefault();
      $event.stopPropagation();
    };

    function uploadRecord() {
      vm.records.push({ name: 'Record 1', type: 'Otto', issuedDate: '12-10-2016', comments: 'my comments'});
    }



    $scope.uploadHealthRecord = function () {
    //  TODO : ADD AJAX CALL TO UPLOAD RECORD
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
