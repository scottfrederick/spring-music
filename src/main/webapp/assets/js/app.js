angular.module('SpringMusic', ['albums']).
    config(function ($routeProvider) {
        $routeProvider.when('/edit/:id', { controller: 'AlbumsController', templateUrl: 'assets/templates/edit.html' });
        $routeProvider.otherwise({ controller: 'AlbumsController', templateUrl: 'assets/templates/list.html' });
    });
