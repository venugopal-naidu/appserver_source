/**
 * Created by VijayaMachavolu on 11/03/16.
 */
app.
Controller('MyAppointmentsCtrl', function ($scope) {
    $scope.upcomingAppointments;
    $scope.pastAppointments;

    $scope.upcomingAppointments = [
    {
        type:'APPOINTMENT_LAB',
        name: 'Vijaya Diagnostics',
        date_time: '10th April 2016 10:30 AM',
        description: 'Blood profile'
    },
    {
        type:'APPOINTMENT_DOCTOR',
        name: 'Dr.Tulasi',
        date_time: '14th March 2016 11:30 AM',
        description: 'Eye exam'

    }
    ];

    $scope.pastAppointments = [
        {
            type:'APPOINTMENT_LAB',
            name: 'Apollo Diagnostics',
            date_time: '10th February 2016 10:00 AM',
            description: 'Lipid profile'
        },
        {
            type:'APPOINTMENT_DOCTOR',
            name: 'Dr.Pingle',
            date_time: '14th January 2016 8:00 AM',
            description: 'Orthopaedic consultation'

        },
        {
            type:'APPOINTMENT_DOCTOR',
            name: 'Dr.Aparana',
            date_time: '14th January 2016 8:00 AM',
            description: 'Peadiatric consultation'

        },
        {
            type:'APPOINTMENT_LAB',
            name: 'Dr.Pingle',
            date_time: '14th January 2016 8:00 AM',
            description: 'Orthopaedic consultation'

        }
    ];
});
