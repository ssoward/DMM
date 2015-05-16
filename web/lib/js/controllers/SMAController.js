var app = angular.module('smaApp').controller('SMAController', function ($scope, $log, SMAuthService, DialogService, $modal){
    $scope.greeting = '';
    $scope.data ={};
    $scope.data.newSupplier;
    $scope.data.poInvoice;
    $scope.showInvoiceDetails = false;
    $scope.matchesLoaded = true;
    $scope.pageLoaded = true;
    $scope.syncedSMALoaded = false;

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

    $scope.syncSMA = function(){
        $scope.pageLoaded = true;
        SMAuthService.syncSMA()
            .then(function(res){
                $scope.invoices = res.data;
                $scope.pageLoaded = false;
                $scope.syncedSMALoaded = true;
            });
    };

    $scope.getOriginalInvoice = function (invId){
        $scope.items = ['item1', 'item2', 'item3'];
        SMAuthService.getOriginalInvoice(invId)
            .then(function(res){
                $scope.details = res.data;
                $scope.openDetailsModal();
            })
    };

    $scope.confirmSync = function(){
        DialogService.open({
            text:'Are you sure you want to sync records from Magento to DMM? This will create invoices that have been sold online.',
            okText: 'Cancel',
            cancelText: 'Yes, Sync',
            enterClick: function(){
                $scope.syncSMA();
            }
        });
    };

    $scope.deleteConfirm = function(inv){
        DialogService.open({
            text:'Are you sure you want to delete this record? This will also delete the invoice '+inv.invoiceNum+'. Syncing again may not recreate this invoice.',
            okText: 'Keep Record',
            cancelText: 'Yes, Delete Record',
            enterClick: function(){
                SMAuthService.deleteRecordInvoice(inv.invoiceNum)
                    .then(function(){
                        _.remove($scope.invoices, function(invoice){
                            if(inv.invoiceNum === invoice.invoiceNum){
                                return invoice;
                            }
                        });
                    })
            }
        });
    };

    $scope.openDetailsModal = function(){
        var modalInstance = $modal.open({
            templateUrl: 'myModalContent.html',
            controller: 'ModalInstanceCtrl',
            size: 'lg',
            resolve: {
                details: function () {
                    return $scope.details;
                }
            }
        });
//        modalInstance.result.then(function (selectedItem) {
//            $scope.selected = selectedItem;
//        }, function () {
//            $log.info('Modal dismissed at: ' + new Date());
//        });
    };
    $scope.syncedSMA = function(){
        $scope.pageLoaded = true;
        SMAuthService.syncedSMA()
            .then(function(res){
                $scope.invoices = res.data;
                $scope.pageLoaded = false;
                $scope.syncedSMALoaded = true;
            });
    };

    function init(){
        $scope.pageLoaded = false;
    }
    init();

});
angular.module('smaApp').controller('ModalInstanceCtrl', function ($scope, $modalInstance, details) {
    $scope.details = details;
    $scope.ok = function () {
        $modalInstance.close();
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
});



