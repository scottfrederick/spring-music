angular.module('SpringMusic', ['albums', 'status', 'info', 'ui.directives']).
    config(function ($routeProvider) {
        $routeProvider.when('/add', { controller: 'AlbumsController', templateUrl: 'assets/templates/add.html' });
        $routeProvider.otherwise({ controller: 'AlbumsController', templateUrl: 'assets/templates/albums.html' });
    });
