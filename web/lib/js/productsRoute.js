angular.module('productsApp').config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/home');

    $stateProvider
        .state('po', {
            url: '/po',
            templateUrl: 'lib/js/partials/products.html',
            controller: 'ProductsController'
        })
        .state('home', {
            url: '/home',
            templateUrl: 'lib/js/partials/products.html',
            controller: 'ProductsController'
        });
});
