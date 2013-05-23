angular.module('albums', ['ngResource']).
    factory('Albums', function ($resource) {
        return $resource('albums');
    }).
    factory('Album', function ($resource) {
        return $resource('albums/:id', {id: '@id'});
    });

function AlbumsController($scope, Albums, Album) {

    function list() {
        $scope.albums = Albums.query();
    }

    $scope.delete = function (album) {
        Album.delete({id: album.id},
            function () {
                $scope.status = success("Album deleted successfully");
                list();
            },
            function (result) {
                $scope.status = error("Error deleting album: " + result.status);
            }
        );
    };

    $scope.setAlbumsView = function(viewName) {
        $scope.albumsView = "assets/templates/" + viewName + ".html";
    };

    list();
    $scope.setAlbumsView("grid");
}

function success(message) {
    return { isError: false, message: message };
}

function error(message) {
    return { isError: true, message: message };
}