angular.module("dmm-core").directive("dmmRadio", function(){
    return {
        restrict: "E",
        transclude: true,
        templateUrl: './lib/js/directives/DMMRadio.html',
        scope: {
            name:'@',
            groupName:'@',
            blRadioModel:'=ngModel',
            required:'=',
            blRadioValue:'=ngValue',
            disabled:'=ngDisabled'
        }
    };
});
