angular.module('smaApp').service('SMAuthService', function ($http, $log, $state, $rootScope) {

    this.syncSMA = function (){
        return $http({
            method: 'GET',
            url: './sma',
            params: {
                funct: 'SYNC_INVOICES'
            }
        });
    };
    this.syncedSMA = function (){
        return $http({
            method: 'GET',
            url: './sma',
            params: {
                funct: 'SYNCED_INVOICES',
                date: 'TODAY'
            }
        });
    };
    this.getOriginalInvoice = function (invId){
        return $http({
            method: 'GET',
            url: './sma',
            params: {
                funct: 'ORIGINAL_FOR_INV',
                invoiceId: invId
            }
        });
    };
    this.deleteRecordInvoice = function (invId){
        return $http({
            method: 'GET',
            url: './sma',
            params: {
                funct: 'DELETE_RECORD_INV',
                invoiceId: invId
            }
        });
    };
});