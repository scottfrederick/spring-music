angular.module('albums', ['ngResource']).
    factory('Albums', function ($resource) {
        return $resource('albums');
    }).
    factory('Album', function ($resource) {
        return $resource('albums/:id', {id: '@id'});
    });

function AlbumsController($scope, Albums, Album, Status) {
    function list() {
        $scope.albums = Albums.query();
    }

    $scope.delete = function (album) {
        Album.delete({id: album.id},
            function () {
                Status.success("Album deleted");
                list();
            },
            function (result) {
                Status.error("Error deleting album: " + result.status);
            }
        );
    };

    $scope.setAlbumsView = function (viewName) {
        $scope.albumsView = "assets/templates/" + viewName + ".html";
    };

    $scope.init = function() {
        list();
        $scope.setAlbumsView("grid");
        $scope.sortField = "name";
        $scope.sortDescending = false;
    };
}

function AlbumEditorController($scope, Albums, Status) {
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

        Albums.save({}, album,
            function () {
                Status.success("Album saved");
                list();
            },
            function (result) {
                Status.error("Error saving album: " + result.status);
            }
        );

        $scope.disableEditor();
    };

    $scope.disableEditor();
}


