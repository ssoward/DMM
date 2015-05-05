angular.module('dmm-core').service('AccountService', function ($http, $log, $state, $rootScope) {

    this.getSearchAccounts = function (searchStr){
        return $http({
            method: 'GET',
            url: './accounts',
            params: {
                funct: 'ACCT_SEARCH',
                searchStr: searchStr
            }

        });
    };

    this.mergeAccounts = function (data){
        return $http({
            method: 'PUT',
            url: './accounts',
            params: {
                funct: 'ACCT_MERGE',
                acct1:data.newAccount1.accountNum,
                acct2:data.newAccount2.accountNum
            }

        });
    };

});

