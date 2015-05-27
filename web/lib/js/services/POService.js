angular.module('purchaseOrderApp').service('POService', function ($http, $log, $state, $rootScope, $q) {

    this.getSearchSuppliers = function (searchStr){
        return $http({
            method: 'GET',
            url: './suppliers',
            params: {
                searchStr: searchStr
            }
        });
    };
    var test = false;

    //var poInvoice = {'status': 200,'config':{},'data':
        //[{"invoiceNum":"724439","accountNum":"300","invoiceDate":"2015-05-20 01:09:57","invDate":1432105797000,"locationNum":"1","username2":"bonita","invoiceTotal":"0.0000","invoiceTax":"0.0000","invoiceShipTotal":"0.0000","invoicePaid":"0.0000","paymentMethod1":null,"paymentMethod2":null,"invoicePaid1":null,"invoicePaid2":null,"invoiceReceivedBy":"27","invoiceContactNum":"PO","invoiceReferenceNum":null,"invoiceChargeStatus":null,"invoiceChargeDate":null,"invoiceChargePaymentMethod":null,"invoiceDiscount":null,"invoiceUnixDate":null,"payStatus":"","dateOne":"","dateTwo":"","invoiceTotalSum":"0.0","invoicePaidSum":"0.0","invoicePaid1Sum":"0.0","invoicePaid2Sum":"0.0","invoiceDiscountSum":"0.0","invoiceTaxSum":"","account":{"pid":null,"accountNum":"","accountName":"","accountPassword":"","accountContact":"","accountEmail":"","accountPhone1":"","accountPhone2":"","accountFax":"","accountStreet":"","accountCity":"","accountState":"","accountPostalCode":"","accountCountry":"","accountType1":"","accountType2":"","accountOpenDate":"","accountCloseDate":"","accountBalance":"","accountTotalSpent":"","accountTotalDisc":"","accountStartDate":"","accountEndDate":"","accountInv":null,"accountZip":"","accountAddress":"","accountAddressFormated":""},"transList":[],"colNames":[],"invoiceTotalBD":null,"invoiceTotalTaxBD":null,"invoiceTotalShipBD":null}]
    //};

    this.getSearchPOInvoices = function (searchStr){
        if(test){
            var defer = $q.defer();
            defer.resolve(poInvoice);
            return defer.promise;
        }
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

