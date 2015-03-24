var app = angular.module('dailySalesApp').controller('DailySalesController', function ($scope, $log, InvoiceTransService, ProductService, $modal){
    $scope.greeting = '';
    $scope.data ={};

    $scope.alerts = [
//        { type: 'danger', msg: 'Oh snap! Change a few things up and try submitting again.' },
//        { type: 'success', msg: 'Well done! You successfully read this important alert message.' }
    ];

    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };

    $scope.clearMessage = function(){
        $scope.alerts = [];
    };

    $scope.showMessage = function(typee, msgg){
        $scope.alerts = [];
        $scope.alerts.push({type: typee, msg: msgg});
    };

    function init(){
        $scope.pageLoaded = true;
    }

    init();

    //GET REPORT
    $scope.getReport = function(){
        $scope.searchingInvoices = true;
        InvoiceTransService.getSales($scope.dateToEval)
            .then(function(res){
                $scope.invoices = res.data;
                $scope.searchingInvoices = false;
            })
    };
    //GET REPORT


    //datePicker
    $scope.today = function() {
        $scope.dt = new Date();
    };
    $scope.today();

    $scope.clear = function () {
        $scope.dt = null;
    };

    // Disable weekend selection
    $scope.disabled = function(date, mode) {
        return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
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
        startingDay: 1
    };

    $scope.format = 'dd MMM yyyy';
    //datePicker

});

