var app = angular.module('dailySalesApp').controller('DailySalesController', function ($scope, $log, InvoiceTransService, ProductService, $q){
    $scope.greeting = '';
    $scope.data = {};
    $scope.page = {};
    $scope.page.cal1 = false;
    $scope.page.cal2 = false;
    $scope.progressComplete = 0;
    $scope.dateToEval1 = new Date();
    $scope.dateToEval2 = new Date();
    $scope.year = new Date().getFullYear();
    $scope.page.sortOrder = 'products';

    $scope.tHeaders = [
        {name:'Catalog#',val:'productCatalogNum'},
        {name:'Title',val:'productName'},
        {name:'Location',val:'prodLocation'},
        {name:'Supplier',val:'supplier.supplierName'}
    ];

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

        promises.push(InvoiceTransService.getSales($scope.dateToEval1, $scope.dateToEval2, $scope.location)
                .then(function(res){
                    $scope.invoices = res.data;
                    $scope.progressComplete += 20;
                    return res;
                })
        );
        promises.push(InvoiceTransService.getProductSoldHistory($scope.dateToEval1, $scope.dateToEval2, $scope.location)
                .then(function (res){
                    $scope.progressComplete += 10;
                    $scope.productHistory = res.data;
                    return res;
                })
        );
        promises.push(InvoiceTransService.getProductCounts($scope.dateToEval1, $scope.dateToEval2, $scope.location)
                .then(function(res){
                    $scope.progressComplete += 10;
                    $scope.inventory = res.data;
                    return res;
                })
        );
        promises.push(InvoiceTransService.getProductLocationForDate($scope.dateToEval1, $scope.dateToEval2, $scope.location)
                .then(function(res){
                    $scope.progressComplete += 10;
                    $scope.prodLocations = res.data;
                    return res;
                })
        );
        promises.push(InvoiceTransService.getRecentSoldForDate($scope.dateToEval1, $scope.dateToEval2, $scope.location)
                .then(function(res){
                    $scope.progressComplete += 10;
                    $scope.recentlySoldList = res.data;
                    return res;
                })
        );
        promises.push(InvoiceTransService.getInventoryCacheForDate($scope.dateToEval1, $scope.location)
                .then(function(res){
                    $scope.invCache = res.data.DSC;
                    $scope.progressComplete += 10;
                    return res;
                })
        );
        promises.push(InvoiceTransService.getAllSuppliers()
                .then(function(res){
                    $scope.suppliers = res.data;
                    $scope.progressComplete += 10;
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

    $scope.getLocation = function(prod){
        prod.locationLoading = true;
        InvoiceTransService.getLocation(prod.productNum)
            .then(function(res){
                prod.location = res.data.location;
                prod.locationLoading = false;
            })
    };

    $scope.getRecentSold = function(prod){
        prod.recentSoldLoading = true;
        InvoiceTransService.getRecentSold(prod.productNum)
            .then(function(res){
                prod.recent = res.data;
                prod.recentSoldLoading = false;
            })
    };

    $scope.getEstimate = function(prod){
        var hist = $scope.productHistory[prod.productNum];
        var one = hist.yearsCount[$scope.year];
        var two = hist.yearsCount[$scope.year-1];
        var three = hist.yearsCount[$scope.year-2];
        one = one?one:0;
        two = two?two:0;
        three = three?three:0;
        var total = (one+two+three);
        return total>0?(total/36).toFixed(2):0;
    };

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

    $scope.open = function($event, cal) {
        $event.preventDefault();
        $event.stopPropagation();
        if(cal === 'cal1'){$scope.page.cal1 = true;}
        else{$scope.page.cal2 = true;}
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
        _.forEach($scope.productList, function(prod){
            _.forEach($scope.suppliers, function(supplier){
                if(supplier.supplierNum === prod.productSupplier1){
                    prod.supplier = supplier;
                }
            });
        });
        _.forEach($scope.productList, function(prod){
            _.forEach($scope.prodLocations, function(prodLocation){
                if(prodLocation.productNum === prod.productNum){
                    prod.prodLocation = prodLocation.prodLocation;
                }
            });
            if(!prod.prodLocation){
                prod.prodLocation = '--';
            }
        });
        _.forEach($scope.productList, function(prod){
            prod.recent = $scope.recentlySoldList[prod.productNum];
        });
    }

    $scope.toggleSort = function(index) {
        if($scope.sortColumn === $scope.tHeaders[index].val){
            $scope.reverse = !$scope.reverse;
        }
        $scope.sortColumn = $scope.tHeaders[index].val;
    };

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
