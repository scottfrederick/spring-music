angular.module('SpringMusic', ['albums', 'info']).
    config(function ($routeProvider) {
        $routeProvider.when('/edit/:id', { controller: 'AlbumsController', templateUrl: 'cedit.html' });
        $routeProvider.otherwise({ controller: 'AlbumsController', templateUrl: 'assets/templates/albums.html' });
    });
