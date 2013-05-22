angular.module('SpringMusic', ['albums', 'info']).
    config(function ($routeProvider) {
        $routeProvider.when('/edit/:id', { controller: 'AlbumsController', templateUrl: 'assets/templates/edit.html' });
        $routeProvider.otherwise({ controller: 'AlbumsController', templateUrl: 'assets/templates/list.html' });
    });
