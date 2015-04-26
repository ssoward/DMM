angular.module("dmm-core").directive("dmmCheckbox", function(){
    return {
        restrict: "E",
        transclude: true,
        templateUrl: './lib/js/directives/DMMCheckBox.html',
        scope: {
            blCheckboxModel:'=ngModel',
            blCheckboxDisabled: '=ngDisabled'
        },
        controller: function($scope){
            $scope.toggleCheckBox = function(){
                if(!$scope.blCheckboxDisabled) {
                    $scope.blCheckboxModel = !$scope.blCheckboxModel;
                }
            };
        }
    };
});
