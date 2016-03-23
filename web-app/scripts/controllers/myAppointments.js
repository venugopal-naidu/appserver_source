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
          // We need to do a bit of re-arranging to make it into rows
        var upcoming = response.data.upcoming;

        // COMMENT ME - stub data
/*        var upcoming = [
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
            }];
            */
        // COMMENT ME TILL HERE

        $scope.upcomingAppointments = [];
        var newRow = [];
        $scope.upcomingAppointments.push(newRow);
        if(upcoming.length >0)
            newRow.push(upcoming[0]);
          for(var i=1; i<upcoming.length; i++)
          {
              if(i%3 == 0)
              {
                  newRow = [];
                  $scope.upcomingAppointments.push(newRow);
              }
              newRow.push(upcoming[i]);
          }


        // COMMENT ME - stub data
/*        var past = [

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
                },
                {
                    id: 22,
                    appointmentType: 'APPOINTMENT_DOCTOR',
                    title: 'Dr.Anitha K',
                    fromTime: '21st December 2015 8am',
                    toTime: '21st December 2015 8:45am',
                    specialInstructions: 'Pain in left shoulder',
                    status: 'APPOINTMENT_STATUS_DONE',
                    recordsUploaded: false
                },
           {
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
         }]
         */
        // COMMENT ME TILL HERE

        var past = response.data.past;
        $scope.pastAppointments = [];
        var newRowPast = [];
        $scope.pastAppointments.push(newRowPast);
        if(past.length > 0)
            newRowPast.push(past[0]);
        for(var i=1; i<past.length; i++)
        {
            if(i%3 == 0)
            {
                newRowPast = [];
                $scope.pastAppointments.push(newRowPast);
            }
            newRowPast.push(past[i]);
        }})};

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

    });
