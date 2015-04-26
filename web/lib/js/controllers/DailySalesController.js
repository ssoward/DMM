var app = angular.module('dailySalesApp').controller('DailySalesController', function ($scope, $log, InvoiceTransService, ProductService, $modal){
    $scope.greeting = '';
    $scope.data = {};
    $scope.page = {};
    $scope.progressComplete = 0;

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
        $scope.page.searchingInvoices = true;
        InvoiceTransService.getSales($scope.dateToEval, $scope.location)
            .then(function(res){
                $scope.invoices = res.data;
                $scope.progressComplete = 50;
                return res;
            })
            .then(function(res){
                return InvoiceTransService.getProductSoldHistory($scope.dateToEval, $scope.location);
            })
            .then(function (res){
                $scope.progressComplete = 70;
                $scope.productHistory = res.data;
                return res;
            })
            .then(function(){
                return InvoiceTransService.getAllHBHash();
            })
            .then(function(res){
                $scope.holdBin = res.data;
                $scope.progressComplete = 85;
                return res;
            })
            .then(function(){
                return InvoiceTransService.getProductCounts($scope.dateToEval, $scope.location);
            })
            .then(function(res){
                $scope.inventory = res.data;
                $scope.progressComplete = 100;
                $scope.page.searchingInvoices = false;
                consolidateProducts();
            });
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
        return ( mode === 'day' && date.getDay() === 0 );//|| date.getDay() === 7 ) );
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

    //get all products
    function consolidateProducts(){
        $scope.productList = [];
        _.forEach($scope.invoices, function(invoice){
            _.forEach(invoice.transList, function(trans){
                var contains = _.findIndex($scope.productList, function(chr) {
                    return chr.productName == trans.prod.productName;
                });
                if(contains<0) {
                    $scope.productList.push(trans.prod);
                }
            });
        });
    }
    //datePicker

});
