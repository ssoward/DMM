var app = angular.module('purchaseOrderApp').controller('PurchaseOrderController', function ($scope, $log, POService, InvoiceTransService, ProductService, $modal){
    $scope.greeting = '';
    $scope.data ={};
    $scope.data.newSupplier;
    $scope.data.poInvoice;
    $scope.showInvoiceDetails = false;
    $scope.matchesLoaded = true;
    $scope.pageLoaded = true;

    $scope.sort = {
        column: '',
        descending: false
    };

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
        $scope.pageLoaded = false;
    }

    $scope.$watch('poInvoice',function(newVal){
        if(newVal == ''){
            $scope.poInvoice = null;
            $scope.showInvoiceDetails = false;
        }
    });

    $scope.clearProduct = function(){
        $scope.data.addProd = null;
        $scope.data.setAddProduct = null;
    };

    $scope.setSupplier = function(mem) {
        $scope.data.newSupplier = mem;
        clearInvoice();
    };

    $scope.setPOInvoice = function(mem) {
        $scope.data.poInvoice = mem;
        $scope.getTranscations();
        $scope.showInvoiceDetails = true;
    };

    $scope.setAddProduct = function(mem) {
        $scope.data.setAddProduct = mem;
        $scope.addProductToPOrder();
    };

    $scope.addProductToPOrder = function() {
        if($scope.data.setAddProduct){
            return InvoiceTransService.addProducttoPOrder($scope.data.poInvoice.invoiceNum, $scope.data.setAddProduct.productNum)
                .then(function (res){
                    return res;
                })
                .then(function(){
                    return $scope.getTranscations();
                })
                .then(function(){
                    $scope.clearProduct();
                });
        }
    };

    function clearInvoice(){
        $scope.data.poInvoice = null;
        $scope.showInvoiceDetails = false;
        $scope.poInvoice = null;
    }

    $scope.createPOrder = function() {
        if($scope.data.newSupplier){
            clearInvoice();
            return POService.createPOrder($scope.data.newSupplier.supplierNum)
                .then(function (res){
                    $scope.data.poInvoice = res.data;
                    $scope.poInvoice = res.data.invoiceNum +' --- '+ res.data.invoiceDate;
                    $scope.showInvoiceDetails = true;
                    return res;
                })
                .then(function(){
                    $scope.data.newSupplier = null;
                })
                .then(function(){
                    $scope.getTranscations();
                });
        }
    };

    $scope.getTranscations = function(){
        if($scope.data.poInvoice){
            $scope.data.transactions = null;
            $scope.transLoaded = true;
            return InvoiceTransService.getTranscations($scope.data.poInvoice.invoiceNum)
                .then(function (res){
                    $scope.data.transactions = res.data;
                    _.forEach($scope.data.transactions, function(trans){
                        trans.editS = false;
                        trans.editC = false;
                    });
                    return res.data;
                })
                .then(function(){
                    $scope.transLoaded = false;
                });
        }
    };

    $scope.getSearchPOInvoices = function(searchStr){
        $scope.fetchingInvoices = true;
        return POService.getSearchPOInvoices(searchStr)
            .then(function (res){
                $scope.data.poInvoice = res.data;
                $scope.fetchingInvoices = false;
                return res.data;
            });
    };

    $scope.fetchPOrders = function(){
        return POService.fetchPOrders()
            .then(function (res){
                $scope.pOrders = res.data;
            });
    };

    $scope.getSearchSuppliers = function(val) {
        $scope.fetchingSuppliers = true;
        return POService.getSearchSuppliers(val)
            .then(function (res) {
                $scope.members = [];
                angular.forEach(res.data, function(item) {
                    $scope.members.push(item);
                });
                $scope.fetchingSuppliers = false;
                return $scope.members;
            });
    };

    $scope.getSearchProductsForCatalog = function(val) {
        $scope.fetchingProducts = true;
        return POService.getSearchProductsForCatalog(val)
            .then(function (res) {
                $scope.products = [];
                $scope.fetchingProducts = false;
                return $scope.products = res.data;
            });
    };

    $scope.editShelf = function(trans){
        trans.editS = true;
    };

    $scope.saveShelf = function(trans){
        trans.editS = false;
        var id = trans.prod.productCatalogNum;
        $scope.sendFocus('BBB'+id)
        return ProductService.saveProductCount(trans.productNum, trans.prod.numAvailable, "MURRAY")
            .then(function(res){
            });
    };

    $scope.editCount = function(trans){
        trans.editC = true;
    };

    $scope.sendFocus = function(eleId){
        var ele = document.getElementById(eleId);
        setTimeout(function(){
            if(ele) {
                ele.focus();
            }
        }, 500);
    };

    $scope.saveCount = function(trans){
        trans.editC = false;
        $scope.sendFocus("addProdsByCatNum");
        return InvoiceTransService.saveProductQty(trans.transNum, trans.productQty)
            .then(function(res){
            });
    };

    $scope.confirmDeleteInvoice = function(){
        var modalInstance = $modal.open({
            templateUrl: 'myModalContent.html',
            controller: 'ModalInstanceCtrl',
            size: 'sm',
            resolve: {
                invoice: function () {
                    return $scope.data.poInvoice;
                }
            }
        });

        modalInstance.result.then(function (poInv) {
            if(poInv && poInv.delete){
                InvoiceTransService.deleteInvoice(poInv.invoiceNum)
                clearInvoice();
            }
        }, function () {
            $log.info('Modal dismissed at: ' + new Date());
        });
    };

    $scope.confirmDeleteTransaction = function(trans){
        var modalInstance = $modal.open({
            templateUrl: 'confirmDeleteTransaction.html',
            controller: 'ModalInstanceTransCtrl',
            size: 'sm',
            resolve: {
                trans: function () {
                    return trans;
                }
            }
        });

        modalInstance.result.then(function (trans) {
            if(trans){
//               console.log(trans);
                InvoiceTransService.deleteTranscation(trans.transNum);
                $scope.getTranscations();
            }
        }, function () {
            $log.info('Modal dismissed at: ' + new Date());
        });
    };

    $scope.changeSorting = function(column) {

        var sort = $scope.sort;

        if (sort.column == column) {
            sort.descending = !sort.descending;
        } else {
            sort.column = column;
            sort.descending = false;
        }
    };

    init();
});

app.controller('ModalInstanceCtrl', function ($scope, $modalInstance, invoice) {

    $scope.poInvoice = invoice;

    $scope.ok = function () {
        $scope.poInvoice.delete = 'true';
        $modalInstance.close($scope.poInvoice);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
});

app.controller('ModalInstanceTransCtrl', function ($scope, $modalInstance, trans) {

    $scope.trans = trans;

    $scope.ok = function () {
        $scope.trans.delete = 'true';
        $modalInstance.close($scope.trans);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
});

app.directive('ngEnter', function () {
    return {
        link: function ($scope, element, attrs) {
            element.bind("keydown keypress", function (event) {
                if (event.which === 13) {
//                    element.find('input').focus();
//                    element.parent().children().nextAll('input').first().focus();
                    $scope.$apply(function () {
                        $scope.$eval(attrs.ngEnter);
                    });
                    //console.log(element);
                    //var elementToFocus = element.next('td').find('input')[1];
                    //if(angular.isDefined(elementToFocus)) { //addProdsByCatNum
                    //    elementToFocus.focus();
                    //}
                    event.preventDefault();
                }
            });
        }
    };
});
