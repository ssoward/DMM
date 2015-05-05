angular.module('accountsApp').config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/home');

    $stateProvider
        .state('po', {
            url: '/po',
            templateUrl: 'lib/js/partials/accounts.html',
            controller: 'AccountsController'
        })
        .state('home', {
            url: '/home',
            templateUrl: 'lib/js/partials/accounts.html',
            controller: 'AccountsController'
        });
});
