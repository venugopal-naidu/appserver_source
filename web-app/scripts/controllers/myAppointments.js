/**
 * Created by VijayaMachavolu on 11/03/16.
 */
app
    .controller('MyAppointmentsCtrl', function ($scope, $uibModal, $window, $http) {
    $scope.upcomingAppointments;
    $scope.pastAppointments;
    $scope.accessToken = $window.localStorage.accessToken;
    $scope.tokenType = $window.localStorage.tokenType;
    $scope.getMyAppointments = function(){
      var url = ajax_url_prefix + 'appointment/list';
      $http.get(url,{
        headers: {'Authorization': $scope.tokenType + ' '+ $scope.accessToken}
      }).then(function(response){
        $scope.upcomingAppointments = response.data.upcoming;
        $scope.pastAppointments = response.data.past;
      });
    };

    /*$scope.upcomingAppointments = [
        {row:[
            {
            id: 23,
            appointmentType: 'APPOINTMENT_DOCTOR',
            title: 'Dr. Satyanath',
            fromTime: '20th April 2016 7pm',
            toTime: '20th April 2016 7pm',
            specialInstructions:'Bad cold',
            status: 'APPOINTMENT_STATUS_PENDING',
            },
            {
            id: 27,
            title: 'Dr. Vishal',
            fromTime: '22nd May 2016 8pm',
            toTime: '22nd May 2016 8:15pm',
            specialInstructions:'Pain in right shoulder',
            status: 'APPOINTMENT_STATUS_CONFIRMED'
            },]}
    ];


    $scope.pastAppointments = [
        { row:[
            {
            id: 21,
            appointmentType: 'APPOINTMENT_DOCTOR',
            title: 'Dr. Satyanath',
            fromTime: '10th March 2016 5pm',
            toTime: '10th March 2016 5:30pm',
            specialInstructions: 'Bad cold',
            status: 'APPOINTMENT_STATUS_DONE',
            recordsUploaded: false
            },
            {
                id: 17,
                appointmentType: 'APPOINTMENT_LAB',
                title: 'Apollo Diagnostics',
                fromTime: '2nd February 2016 8am',
                toTime: '2nd February 2016 8:45am',
                specialInstructions: 'ECG',
                status: 'APPOINTMENT_STATUS_DONE',
                recordsUploaded: true
            },
            {
                id: 22,
                appointmentType: 'APPOINTMENT_LAB',
                title: 'Vijaya Diagnostics',
                fromTime: '2nd January 2016 8am',
                toTime: '2nd January 2016 8:45am',
                specialInstructions: 'X ray of forearm',
                status: 'APPOINTMENT_STATUS_NOSHOW',
                recordsUploaded: false
            }]},
        { row:[
            {
                id: 22,
                appointmentType: 'APPOINTMENT_DOCTOR',
                title: 'Dr.Anitha K',
                fromTime: '21st December 2015 8am',
                toTime: '21st December 2015 8:45am',
                specialInstructions: 'Pain in left shoulder',
                status: 'APPOINTMENT_STATUS_DONE',
                recordsUploaded: false
            }
        ]}
    ];*/
  /*      row: {appointments:
            [{
                id: 21,
                appointmentType: 'APPOINTMENT_DOCTOR',
                title: 'Dr. Satyanath',
                fromTime: '10th March 2016 5pm',
                toTime: '10th March 2016 5:30pm',
                status: 'APPOINTMENT_STATUS_DONE',
                recordsUploaded: 0
            },
            {
                id: 17,
                appointmentType: 'APPOINTMENT_LAB',
                title: 'Apollo Diagnostics',
                fromTime: '2nd February 2016 8am',
                toTime: '2nd February 2016 8:45am',
                status: 'APPOINTMENT_STATUS_DONE',
                recordsUploaded: 1
            },
            {
                id: 22,
                appointmentType: 'APPOINTMENT_LAB',
                title: 'Vijaya Diagnostics',
                fromTime: '2nd January 2016 8am',
                toTime: '2nd January 2016 8:45am',
                status: 'APPOINTMENT_STATUS_NOSHOW',
                recordsUploaded: 0
            }]},
        row:{appointments:[
            {
            id: 22,
            appointmentType: 'APPOINTMENT_DOCTOR',
            title: 'Dr.Anitha K',
            fromTime: '21st December 2015 8am',
            toTime: '21st December 2015 8:45am',
            status: 'APPOINTMENT_STATUS_DONE',
            recordsUploaded: 1
        }]}
    };*/


        // TODO: Info is repeated. Need to centralize
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

      $scope.getMyAppointments();
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


    });