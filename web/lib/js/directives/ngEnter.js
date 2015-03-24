angular.module("dmm-core").directive('ngEnter', function () {
    return {
        link: function ($scope, element, attrs) {
            element.bind("keydown keypress", function (event) {
                if (event.which === 13) {
//                    element.find('input').focus();
//                    element.parent().children().nextAll('input').first().focus();
                    $scope.$apply(function () {
                        $scope.$eval(attrs.ngEnter);
                    });
                    //console.log(element);
                    //var elementToFocus = element.next('td').find('input')[1];
                    //if(angular.isDefined(elementToFocus)) { //addProdsByCatNum
                    //    elementToFocus.focus();
                    //}
                    event.preventDefault();
                }
            });
        }
    };
});