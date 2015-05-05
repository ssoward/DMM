var app = angular.module('accountsApp').controller('AccountsController', function ($scope, $log, AccountService, $modal){
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
        $scope.pageLoaded = false;
    }

    init();

    $scope.getSearchAccounts = function(searchStr){
        return AccountService.getSearchAccounts(searchStr)
            .then(function (res){
                return res.data;
            });
    };

    $scope.openMerge = function(){
        console.log($scope.data.newAccount2.accountNum !== $scope.data.newAccount1.accountNum);
        if($scope.data
            && $scope.data.newAccount1
            && $scope.data.newAccount2
            && $scope.data.newAccount2.accountNum !== $scope.data.newAccount1.accountNum
        ) {
            $scope.lockModal = $modal.open({
                scope: $scope,
                templateUrl: "lockModal.html",
                controller: ModalInstanceCtrl
            });
        }else{
            $scope.showMessage('danger', 'You cannot merge the same account into itself.');
        }
    };

    var ModalInstanceCtrl = ['$scope', '$modalInstance', 'AccountService', function ($scope, $modalInstance, AccountService) {

        $scope.merge = function () {
            AccountService.mergeAccounts($scope.data)
                .then(function(res){
                    $scope.showMessage('success', 'Account '+$scope.data.newAccount1.accountNum+' has been merged and deleted');
                });
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss();
        };

        $scope.return = function () {
            $modalInstance.dismiss('cancel');
        };
    }];

});

