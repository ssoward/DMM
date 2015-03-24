angular.module("dmm-core").directive("spinner",function(){
    return {
        restrict: 'E',
        template: '<div class="bl-spinner">\n    <div class="bl-spinner-block bl-spinner-01">\n    </div>\n    <div class="bl-spinner-block bl-spinner-02">\n    </div>\n    <div class="bl-spinner-block bl-spinner-03">\n    </div>\n    <div class="bl-spinner-block bl-spinner-04">\n    </div>\n    <div class="bl-spinner-block bl-spinner-05">\n    </div>\n    <div class="bl-spinner-block bl-spinner-06">\n    </div>\n    <div class="bl-spinner-block bl-spinner-07">\n    </div>\n    <div class="bl-spinner-block bl-spinner-08">\n    </div>\n</div>'
    };
});