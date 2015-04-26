angular.module("dmm-core").directive("productSold", function(){
    return {
        restrict: "E",
        transclude: true,
        templateUrl: './lib/js/directives/ProductSold.html',
        scope: {
            prod: '=product',
            history: '=history',
            location: '=location'
        },
        controller: function($scope, $q, ProductService){
            $scope.year = new Date().getFullYear();
            $scope.active = true;

            $scope.loadingSold = true;
        }
    };
});

