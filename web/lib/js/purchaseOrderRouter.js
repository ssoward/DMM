angular.module('purchaseOrderApp').config(function ($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/home');

    $stateProvider
        .state('po', {
            url: '/po',
            templateUrl: 'lib/js/partials/purchaseOrder.html',
            controller: 'PurchaseOrderController'
        })
        .state('home', {
            url: '/home',
            templateUrl: 'lib/js/partials/purchaseOrder.html',
            controller: 'PurchaseOrderController'
        });
});
