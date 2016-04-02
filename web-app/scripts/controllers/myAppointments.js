/**
 * Created by VijayaMachavolu on 11/03/16.
 */
app
    .controller('MyAppointmentsCtrl', function ($scope, $uibModal, $window, $http, $state) {
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
        /*var past = [

                {
                    appointmentId: 21,
                    appointmentType: 'APPOINTMENT_DOCTOR',
                    title: 'Dr. Satyanath',
                    fromTime: '10th March 2016 5pm',
                    toTime: '10th March 2016 5:30pm',
                    specialInstructions: 'Bad cold',
                    status: 'COMPLETE',
                    hasMedicalRecords: false
                }]*/

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
        }
      },function (response){
          if(response['status'] == 401){
              $scope.formErrors = response.data['error'];
              if(response.data['error'] == 'invalid_token'){
                  $state.go('site.login');
              }
          }
      })};

        $scope.cancelAppointment = function(appointmentId) {
            var url = ajax_url_prefix + 'appointment/cancel/'+appointmentId;
            $http.get(url,{
                headers: {'Authorization': $scope.tokenType + ' '+ $scope.accessToken}
            }).then(function(response){
              if(response['status'] == 200 && response.data['cancel'] == 'SUCCESS'){
                $state.go($state.current, {}, {reload: true});
              }
        },function (response){
                if(response['status'] == 401){
                    $scope.formErrors = response.data['error'];
                    if(response.data['error'] == 'invalid_token'){
                        $state.go('site.login');
                    }
                }
            });
        };

        // TODO: Info is repeated. Need to centralize
        // Init upload health records modal.

        $scope.getMyAppointments();



        $scope.openUploadRecordModal = function(size, apptId) {
            $scope.modalInput = {'appointmentId':apptId};
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

    });
