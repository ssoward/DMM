angular.module('hourlySalesApp').config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/home');

    $stateProvider
        .state('home', {
            url: '/home',
            templateUrl: 'lib/js/partials/hourlySales.html',
            controller: 'HourlySalesController'
        });
});
