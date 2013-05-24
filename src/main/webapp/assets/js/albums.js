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
                $scope.status = success("Album successfully deleted");
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

    $scope.init = function() {
        list();
        $scope.setAlbumsView("grid");
        $scope.sortField = "name";
        $scope.sortDescending = false;
    };
}

function success(message) {
    return { isError: false, message: message };
}

function error(message) {
    return { isError: true, message: message };
}

function AlbumEditorController($scope, Album) {
    $scope.enableEditor = function (album, fieldName) {
        $scope.disableEditor();
        $scope.newFieldValue = album[fieldName];
        $scope.editorEnabled[fieldName] = true;
    };

    $scope.disableEditor = function () {
        $scope.editorEnabled = {};
    };

    $scope.save = function (album, fieldName) {
        if ($scope.newFieldValue === "") {
            return false;
        }

        album[fieldName] = $scope.newFieldValue;

        $scope.disableEditor();
    };

    $scope.disableEditor();
}