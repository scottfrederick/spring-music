angular.module('SpringMusic', ['albums', 'info']).
    config(function ($routeProvider) {
        $routeProvider.when('/add', { controller: 'AlbumsController', templateUrl: 'assets/templates/add.html' });
        $routeProvider.otherwise({ controller: 'AlbumsController', templateUrl: 'assets/templates/albums.html' });
    }).
    directive('ngcFocus', ['$parse', function ($parse) {
        return function (scope, element, attr) {
            var fn = $parse(attr['ngcFocus']);
            element.bind('focus', function (event) {
                scope.$apply(function () {
                    fn(scope, {$event: event});
                });
            });
        }
    }]).
    directive('ngcBlur', ['$parse', function ($parse) {
        return function (scope, element, attr) {
            var fn = $parse(attr['ngcBlur']);
            element.bind('blur', function (event) {
                scope.$apply(function () {
                    fn(scope, {$event: event});
                });
            });
        }
    }]);
