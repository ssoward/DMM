angular.module("dmm-core").directive("productSold", function(){
    return {
        restrict: "E",
        transclude: true,
        templateUrl: './lib/js/directives/ProductSold.html',
        scope: {
            trans: '=transaction',
            history: '=history',
            location: '=location'
        },
        controller: function($scope, $q, ProductService){
            $scope.year = new Date().getFullYear();
            $scope.active = true;
            console.log($scope.trans);

            $scope.loadingSold = true;
            //ProductService.productSoldHistory($scope.trans.productNum, $scope.location)
            //    .then(function(res){
            //        $scope.loadingSold = false;
            //        $scope.productSoldHistory = res.data;
            //    })
        }
    };
});

