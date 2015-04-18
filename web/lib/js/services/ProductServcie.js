angular.module('dmm-core').service('ProductService', function ($http, $log, $state, $rootScope) {

    this.getTranscations = function (invoiceNum){
        return $http({
            method: 'GET',
            url: './invoices',
            params: {
                invoiceNum: invoiceNum,
                funct: 'TRANS_GET'
            }
        });
    };

    this.saveProductCount = function (productNum, numAvailable){
        return $http({
            method: 'PUT',
            url: './products',
            params: {
                funct: 'COUNT_PUT',
                numAvailable:numAvailable,
                productNum:productNum
            }

        });
    };

    this.saveWeight = function (product){
        return $http({
            method: 'PUT',
            url: './products',
            params: {
                funct: 'WEIGHT_PUT',
                productNum:product.productNum,
                weight:product.weight
            }

        });
    };

    this.getSearchProducts = function (searchStr){
        return $http({
            method: 'GET',
            url: './products',
            params: {
                funct: 'PROD_SEARCH',
                searchStr: searchStr
            }

        });
    };

    this.getProductWeight = function (){
        return $http({
            method: 'GET',
            url: './products',
            params: {
                funct: 'PROD_WEIGHT'
            }
        });
    };

    this.productSoldHistory = function (pId, location){
        return $http({
            method: 'GET',
            url: './products',
            params: {
                funct: 'PROD_SOLD_HISTORY',
                productNum:pId,
                location:location
            }
        });
    };
});

