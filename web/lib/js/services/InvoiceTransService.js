angular.module('dmm-core').service('InvoiceTransService', function ($http, $log, $state, $rootScope, $q) {

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

    this.addProducttoPOrder = function (invoiceNum, productNum){
        return $http({
            method: 'PUT',
            url: './invoices',
            params: {
                funct: 'TRANS_PUT',
                invoiceNum:invoiceNum,
                productNum:productNum
            }
        });
    };

    this.saveProductQty = function (transNum, productQty){
        return $http({
            method: 'PUT',
            url: './invoices',
            params: {
                funct: 'TRANS_QTY_PUT',
                transNum:transNum,
                productQty:productQty
            }
        });
    };

    this.deleteTranscation = function (transNum){
        return $http({
            method: 'DELETE',
            url: './invoices',
            params: {
                funct: 'TRANS_DELETE',
                transNum:transNum
            }
        });
    };

    this.deleteInvoice = function (invoiceNum){
        return $http({
            method: 'DELETE',
            url: './invoices',
            params: {
                funct: 'INV_DELETE',
                invoiceNum:invoiceNum
            }
        });
    };

    var res = ""

    this.getSales = function (date){
        var defer = $q.defer();
        q.resolve(res);
        return q.promise;
        return $http({
            method: 'GET',
            url: './invoices',
            params: {
                date: date,
                funct: 'SALES_GET'
            }
        });
    }


});

