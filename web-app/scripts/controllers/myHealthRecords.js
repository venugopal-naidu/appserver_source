'use strict';

/**
 * @ngdoc function
 * @name minovateApp.controller:TablesBootstrapCtrl
 * @description
 * # TablesBootstrapCtrl
 * Controller of the minovateApp
 */
app
  .controller('MyHealthRecordsCtrl', function ($scope, $resource, DTOptionsBuilder, DTColumnDefBuilder) {
    $scope.recordTypes = [
      {id: 1, name: 'Doctor'},
      {id: 2, name: 'Lab'}
    ];
    $scope.recordType = { selected: $scope.recordTypes[0] };

    var vm = $scope;

    function uploadRecord() {
      vm.records.push({ name: 'Record 1', type: 'Otto', issuedDate: '12-10-2016', comments: 'my comments'});
    }
    function modifyPerson(index) {
      vm.persons.splice(index, 1, angular.copy(vm.person2Add));
      vm.person2Add = _buildPerson2Add(vm.person2Add.id + 1);
    }
    function removePerson(index) {
      vm.persons.splice(index, 1);
    }

    vm.records = [
      { name: 'Dr. Satyanath\'s notes', type: 'Doctor notes', issuedDate: 'October 12, 2015', comments: 'Appt notes from Dr.Satyanath'},
      { name: 'Backache prescription', type: 'Prescription', issuedDate: 'November 10, 2016', comments: 'Backache medicines'},
      { name: 'X-ray report', type: 'Lab report', issuedDate: 'January 15, 2016', comments: 'X-ray at Focus diagnostics'},
      { name: 'MRI report', type: 'Lab report', issuedDate: 'February 4, 2016', comments: 'MRI of spine'},
    ];
    vm.dtOptions = DTOptionsBuilder.newOptions().withPaginationType('full_numbers').withBootstrap();
    vm.dtColumnDefs = [
      DTColumnDefBuilder.newColumnDef(0),
      DTColumnDefBuilder.newColumnDef(1),
      DTColumnDefBuilder.newColumnDef(2),
      DTColumnDefBuilder.newColumnDef(3).notSortable()
    ];
    vm.modifyPerson = modifyPerson;
    vm.removePerson = removePerson;

  })
  .controller('RecordUploadCtrl', ['$scope', 'FileUploader', function($scope, FileUploader) {
    var uploader = $scope.uploader = new FileUploader({
      //url: 'scripts/modules/fileupload/upload.php' //enable this option to get f
    });

    // FILTERS

    uploader.filters.push({
      name: 'customFilter',
      fn: function() {
        return this.queue.length < 10;
      }
    });

    uploader.filters.push({
      name: 'imageFilter',
      fn: function(item /*{File|FileLikeObject}*/, options) {
        var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
        return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
      }
    });

    // CALLBACKS

    uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
      console.info('onWhenAddingFileFailed', item, filter, options);
    };
    uploader.onAfterAddingFile = function(fileItem) {
      console.info('onAfterAddingFile', fileItem);
    };
    uploader.onAfterAddingAll = function(addedFileItems) {
      console.info('onAfterAddingAll', addedFileItems);
    };
    uploader.onBeforeUploadItem = function(item) {
      console.info('onBeforeUploadItem', item);
    };
    uploader.onProgressItem = function(fileItem, progress) {
      console.info('onProgressItem', fileItem, progress);
    };
    uploader.onProgressAll = function(progress) {
      console.info('onProgressAll', progress);
    };
    uploader.onSuccessItem = function(fileItem, response, status, headers) {
      console.info('onSuccessItem', fileItem, response, status, headers);
    };
    uploader.onErrorItem = function(fileItem, response, status, headers) {
      console.info('onErrorItem', fileItem, response, status, headers);
    };
    uploader.onCancelItem = function(fileItem, response, status, headers) {
      console.info('onCancelItem', fileItem, response, status, headers);
    };
    uploader.onCompleteItem = function(fileItem, response, status, headers) {
      console.info('onCompleteItem', fileItem, response, status, headers);
    };
    uploader.onCompleteAll = function() {
      console.info('onCompleteAll');
    };

    console.info('uploader', uploader);
  }]);
