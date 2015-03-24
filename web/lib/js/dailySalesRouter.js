angular.module('dailySalesApp').config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/home');

    $stateProvider
        .state('po', {
            url: '/po',
            templateUrl: 'lib/js/partials/dailySales.html',
            controller: 'DailySalesController'
        })
        .state('home', {
            url: '/home',
            templateUrl: 'lib/js/partials/dailySales.html',
            controller: 'DailySalesController'
        });
});
