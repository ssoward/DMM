var app = angular.module('hourlySalesApp').controller('HourlySalesController', function ($scope, $log, InvoiceTransService, ProductService, $q){
    $scope.greeting = '';
    $scope.data = {};
    $scope.page = {};
    $scope.progressComplete = 0;
    $scope.times = [9,10,11,12,13,14,15,16,17,18,19];
    $scope.hoursBuckets = ['9','10','11','12','13','14','15','16','17','18','19', 'OFFHOURS'];
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


    //GET REPORT
    $scope.getReport = function(){
        $scope.page.searchingInvoices = true;

        var promises = [];

        promises.push(InvoiceTransService.getHourlySales($scope.dateToEval, $scope.location)
                .then(function(res){
                    $scope.locInvMap = res.data;
                    $scope.getKeys();
                    return res;
                })
        );
        $q.all(promises)
            .then(function(){
                $scope.page.searchingInvoices = false;
            })
    };

    $scope.getTotalCount = function(location){
        var invList;
        angular.forEach($scope.locInvMap, function(value, key) {
            if(location === key){
                invList = value;
            }
        });
        return invList.length;
    };

    $scope.getTotalOffHours = function(location){
        var invList;
        angular.forEach($scope.locInvMap, function(value, key) {
            if(location === key){
                invList = value;
            }
        });
        var hourList = [];
        if(invList){
            angular.forEach(invList, function(value) {
                var hour = new Date(value.invDate).getHours();
                if($scope.times.indexOf(hour) === -1) {
                    hourList.push(value);
                }
            });
        }
        return hourList;
    };

    $scope.getAllTotalOffHours = function(){

        var hourList = [];
        angular.forEach($scope.locInvMap, function(offHoursList, key) {
            angular.forEach(offHoursList, function(value) {
                var hour = new Date(value.invDate).getHours();
                if($scope.times.indexOf(hour) === -1) {
                    hourList.push(value);
                }
            });
        });
        return hourList;
    };

    $scope.getAllLocationCountForHour = function(time, location){
        if(time === 'OFFHOURS'){
            var offHoursInvList = $scope.getAllTotalOffHours();
            return formatTotal(offHoursInvList);
        }else {
            var hourList = [];;
            angular.forEach($scope.locInvMap, function (locInvList, key) {
                angular.forEach(locInvList, function (value) {
                    var hour = new Date(value.invDate).getHours();
                    if (hour == time) {
                        hourList.push(value);
                    }
                });
            });
            return formatTotal(hourList);
        }
    };

    function formatTotal(invList){
        var total = 0;
        angular.forEach(invList, function(value) {
            var spent = value.invoiceTotal;
            total += Number(spent);
        });
        return '('+invList.length+') ' + total.toFixed(2);
    }

    $scope.getVal = function(time, location){
        if(time === 'OFFHOURS'){
            var offHoursInvList = $scope.getTotalOffHours(location);
            return offHoursInvList.length;
        }else {
            var invList;
            angular.forEach($scope.locInvMap, function (value, key) {
                if (location === key) {
                    invList = value;
                }
            });
            var hourCount = 0;
            if (invList) {
                angular.forEach(invList, function (value) {
                    var hour = new Date(value.invDate).getHours();
                    if (hour == time) {
                        hourCount++;
                    }
                });
            }
            return hourCount;
        }
    };

    $scope.getTotal = function(location){
        var invList;
        angular.forEach($scope.locInvMap, function(value, key) {
            if(location === key){
                invList = value;
            }
        });
        var total = 0;
        if(invList){
            angular.forEach(invList, function(value) {
                var spent = value.invoiceTotal;
                total += Number(spent);
            });
        }
        return total.toFixed(2);
    };

    $scope.getTickets = function(){
        var allTickets = [];
        angular.forEach($scope.locInvMap, function(locInvList, key) {
            angular.forEach(locInvList, function(value) {
                allTickets.push(value);
            });
        });
        return allTickets.length;
    };

    $scope.getGrandTotal = function(){
        var total = 0;
        angular.forEach($scope.locInvMap, function(locInvList, key) {
            angular.forEach(locInvList, function(value) {
                var spent = value.invoiceTotal;
                total += Number(spent);
            });
        });
        return total.toFixed(2);
    };

    $scope.getInvoices = function(time, location){
        if(time === 'OFFHOURS'){
            var offHoursInvList = $scope.getTotalOffHours(location);
            return offHoursInvList;
        }else {

            var invList;
            angular.forEach($scope.locInvMap, function (value, key) {
                if (location === key) {
                    invList = value;
                }
            });
            var hourList = [];
            if (invList) {
                angular.forEach(invList, function (value) {
                    var hour = new Date(value.invDate).getHours();
                    if (hour == time) {
                        hourList.push(value);

                    }
                });
            }
            return hourList;
        }
    };

    $scope.getKeys = function(){
        $scope.locations = [];
        if($scope.locInvMap) {
            angular.forEach($scope.locInvMap, function (value, key) {
                $scope.locations.push(key);
            });
        }
    };

});

//"invoiceNum":"713125","accountNum":"101","invoiceDate":"2015-02-03 12:59:11","locationNum":"5","username2":"jd","invoiceTotal":"9.6200","invoiceTax":"0.6165","invoiceShipTotal":"0.0000","invoicePaid":"9.6200","paymentMethod1":"97","paymentMethod2":null,"invoicePaid1":"9.6200","invoicePaid2":"0.0000","invoiceReceivedBy":null,"invoiceContactNum":null,"invoiceReferenceNum":"003003","invoiceChargeStatus":null,"invoiceChargeDate":null,"invoiceChargePaymentMethod":null,"invoiceDiscount":"0.0000","invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":null,"transList":[],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null},{"invoiceNum":"713126","accountNum":"101","invoiceDate":"2015-02-03 13:03:17","locationNum":"5","username2":"laurel","invoiceTotal":"9.8300","invoiceTax":"0.6302","invoiceShipTotal":"0.0000","invoicePaid":"20.0000","paymentMethod1":"100","paymentMethod2":null,"invoicePaid1":"20.0000","invoicePaid2":"0.0000","invoiceReceivedBy":null,"invoiceContactNum":null,"invoiceReferenceNum":null,"invoiceChargeStatus":null,"invoiceChargeDate":null,"invoiceChargePaymentMethod":null,"invoiceDiscount":"0.0000","invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":null,"transList":[],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null},
