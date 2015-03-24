var app = angular.module('productsApp').controller('ProductsController', function ($scope, $log, ProductService, $modal){
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
        $scope.getWeights();
    }

    $scope.getWeights = function(){
        ProductService.getProductWeight()
            .then(function (res){
                $scope.productWeights = res.data;
            })
    };

    init();

    $scope.getSearchProducts = function(searchStr){
        return ProductService.getSearchProducts(searchStr)
            .then(function (res){
                return res.data;
            });
    };

    $scope.setProduct = function(product){
        $scope.product = product;
    };

    $scope.saveWeight = function(prod){
        $scope.updatingWeight = true;
        ProductService.saveWeight(prod)
            .then(function(res){
                $scope.updatingWeight = false;
                $scope.updatedWeight = true;
            });
    }

});

