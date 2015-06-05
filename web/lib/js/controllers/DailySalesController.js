var app = angular.module('dailySalesApp').controller('DailySalesController', function ($scope, $log, InvoiceTransService, ProductService, $q){
    $scope.greeting = '';
    $scope.data = {};
    $scope.page = {};
    $scope.progressComplete = 0;
    $scope.dateToEval = new Date();

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
        $scope.progressComplete = 0;
        $scope.page.searchingInvoices = true;

        var promises = [];

        promises.push(InvoiceTransService.getSales($scope.dateToEval, $scope.location)
                .then(function(res){
                    $scope.invoices = res.data;
                    $scope.progressComplete += 20;
                    return res;
                })
        );
        promises.push(InvoiceTransService.getProductSoldHistory($scope.dateToEval, $scope.location)
                .then(function (res){
                    $scope.progressComplete += 20;
                    $scope.productHistory = res.data;
                    return res;
                })
        );
        promises.push(InvoiceTransService.getAllHBHash()
                .then(function(res){
                    $scope.holdBin = res.data;
                    $scope.progressComplete += 20;
                    return res;
                })
        );
        promises.push(InvoiceTransService.getProductCounts($scope.dateToEval, $scope.location)
                .then(function(res){
                    $scope.progressComplete += 20;
                    $scope.inventory = res.data;
                    return res;
                })
        );
        promises.push(InvoiceTransService.getInventoryCacheForDate($scope.dateToEval, $scope.location)
                .then(function(res){
                    $scope.invCache = res.data.DSC;
                    $scope.progressComplete += 20;
                    return res;
                })
        );
        $q.all(promises)
            .then(function(){
            updateProdCache();
            consolidateProducts();
            $scope.page.searchingInvoices = false;
        })
    };

    $scope.shiftCount = function(store, prod){

        switch (store) {
            case "MURRAY":
                $scope.inventory[prod.productNum].MURRAY++;
                $scope.inventory[prod.productNum].LEHI--;
                break;
            case "LEHI":
                $scope.inventory[prod.productNum].LEHI++;
                $scope.inventory[prod.productNum].MURRAY--
                break;
        }
        ProductService.saveProductCount(prod.productNum, $scope.inventory[prod.productNum].LEHI, "LEHI");
        ProductService.saveProductCount(prod.productNum, $scope.inventory[prod.productNum].MURRAY, "MURRAY");
    };

    $scope.saveDone = function(prod){
        InvoiceTransService.saveDoneCache($scope.invCache.id, prod.done, prod.productNum);
    };

    $scope.saveMove = function(prod){
        InvoiceTransService.saveMoveCache($scope.invCache.id, prod.move, prod.productNum);
    };

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

    //datePicker
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

    function updateProdCache(){
        _.forEach($scope.invoices, function(invoice){
            _.forEach(invoice.transList, function(trans){
                var prodMap = $scope.invCache.map[trans.prod.productNum];
                if(prodMap) {
                    trans.prod.done = prodMap.done;
                    trans.prod.move = prodMap.move;
                }
            });
        });
    }

});
