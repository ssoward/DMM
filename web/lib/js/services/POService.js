angular.module('purchaseOrderApp').service('POService', function ($http, $log, $state, $rootScope) {

    this.getSearchSuppliers = function (searchStr){
        return $http({
            method: 'GET',
            url: './suppliers',
            params: {
                searchStr: searchStr
            }
        });
    };

    this.getSearchPOInvoices = function (searchStr){
        return $http({
            method: 'GET',
            url: './invoices',
            params: {
                searchStr: searchStr,
                funct: 'PO_GET'
            }
        });
    };

    this.getSearchProductsForCatalog = function (searchStr){
        return $http({
            method: 'GET',
            url: './products',
            params: {
                searchStr: searchStr,
                funct: 'PROD_CAT_GET'
            }
        });
    };

    this.fetchPOrders = function (){
        return $http({
            method: 'GET',
            url: './invoices',
            params: {
                funct: 'PO_GET'
            }
        });
    };

    this.createPOrder = function (supplierNum){
        return $http({
            method: 'PUT',
            url: './invoices',
            params: {
                funct: 'PO_PUT',
                supplierNum:supplierNum
            }
        });
    };


});

