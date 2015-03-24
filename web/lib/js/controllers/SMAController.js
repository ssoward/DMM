var app = angular.module('smaApp').controller('SMAController', function ($scope, $log, SMAuthService, $modal){
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



