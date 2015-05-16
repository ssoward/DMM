angular.module('dmm-core').service('DialogService', function ($modal, $rootScope, $q) {
    return {
        open: function(options){
            _.defaults(options,{okText:'OK',cancelText:'Cancel'});

            var diagScope = $rootScope.$new();

            diagScope.title = options.title;
            diagScope.text = options.text;
            diagScope.okText = options.okText;
            diagScope.cancelText = options.cancelText;


            var modalInstance = $modal.open({
                template: '<div class="modal-header" ng-if="title">\n    <h3>{{title}}</h3>\n</div>\n<div class="modal-body">\n{{text}}\n</div>\n<div class="modal-footer">\n    <button class="btn btn-default btn-sm pull-left" ng-show="okText.length > 0" ng-click="okClick()"><bl-loading ng-if="okWorking"></bl-loading> {{okText}}</button>\n    <button class="btn btn-danger btn-sm pull-right" ng-show="cancelText.length > 0" ng-click="enterClick()"><bl-loading ng-if="cancelWorking"></bl-loading> {{cancelText}}</button>\n</div>',
                scope: diagScope
            });

            diagScope.okClick = function(){
                diagScope.okWorking = true;

                var deferred = $q.defer();
                deferred.promise
                    .then(function(){
                        diagScope.okWorking = false;
                        modalInstance.close();
                    }, function(failureText){
                        diagScope.okWorking = false;
                        modalInstance.close();
                    });

                if(angular.isFunction(options.okClick)){
                    deferred.resolve(options.okClick());
                } else {
                    deferred.resolve();
                }
            };


            diagScope.enterClick = function(){
                diagScope.cancelWorking = true;

                var deferred = $q.defer();
                deferred.promise
                    .then(function(){
                        diagScope.cancelWorking = false;
                        modalInstance.dismiss();
                    },function(){
                        diagScope.cancelWorking = false;
                        modalInstance.dismiss();
                    });

                if(angular.isFunction(options.enterClick)){
                    deferred.resolve(options.enterClick());
                } else {
                    deferred.resolve();
                }
            };


            return modalInstance.result;
        }
    };
});