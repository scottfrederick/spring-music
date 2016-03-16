angular.module('albums', ['ngResource', 'ui.bootstrap']).
    factory('Albums', function ($resource) {
        return $resource('albums');
    }).
    factory('Album', function ($resource) {
        return $resource('albums/:id', {id: '@id'});
    }).
    factory("EditorStatus", function () {
        var editorEnabled = {};

        var enable = function (id, fieldName) {
            editorEnabled = { 'id': id, 'fieldName': fieldName };
        };

        var disable = function () {
            editorEnabled = {};
        };

        var isEnabled = function(id, fieldName) {
            return (editorEnabled['id'] == id && editorEnabled['fieldName'] == fieldName);
        };

        return {
            isEnabled: isEnabled,
            enable: enable,
            disable: disable
        }
    });

function AlbumsController($scope, $modal, Albums, Album, Status) {
    function list() {
        $scope.albums = Albums.query();
    }

    function clone (obj) {
        return JSON.parse(JSON.stringify(obj));
    }

    function saveAlbum(album) {
        Albums.save(album,
            function () {
                Status.success("Album saved");
                list();
            },
            function (result) {
                Status.error("Error saving album: " + result.status);
            }
        );
    }

    $scope.addAlbum = function () {
        var addModal = $modal.open({
            templateUrl: 'templates/albumForm.html',
            controller: AlbumModalController,
            resolve: {
                album: function () {
                    return {};
                },
                action: function() {
                    return 'add';
                }
            }
        });

        addModal.result.then(function (album) {
            saveAlbum(album);
        });
    };

    $scope.updateAlbum = function (album) {
        var updateModal = $modal.open({
            templateUrl: 'templates/albumForm.html',
            controller: AlbumModalController,
            resolve: {
                album: function() {
                    return clone(album);
                },
                action: function() {
                    return 'update';
                }
            }
        });

        updateModal.result.then(function (album) {
            saveAlbum(album);
        });
    };

    $scope.deleteAlbum = function (album) {
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
        $scope.albumsView = "templates/" + viewName + ".html";
    };

    $scope.init = function() {
        list();
        $scope.setAlbumsView("grid");
        $scope.sortField = "name";
        $scope.sortDescending = false;
    };
}

function AlbumModalController($scope, $modalInstance, album, action) {
    $scope.albumAction = action;
    $scope.yearPattern = /^[1-2]\d{3}$/;
    $scope.album = album;

    $scope.ok = function () {
        $modalInstance.close($scope.album);
    };

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };
};

function AlbumEditorController($scope, Albums, Status, EditorStatus) {
    $scope.enableEditor = function (album, fieldName) {
        $scope.newFieldValue = album[fieldName];
        EditorStatus.enable(album.id, fieldName);
    };

    $scope.disableEditor = function () {
        EditorStatus.disable();
    };

    $scope.isEditorEnabled = function (album, fieldName) {
        return EditorStatus.isEnabled(album.id, fieldName);
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

angular.module('albums').
    directive('inPlaceEdit', function () {
        return {
            restrict: 'E',
            transclude: true,
            replace: true,

            scope: {
                ipeFieldName: '@fieldName',
                ipeInputType: '@inputType',
                ipeInputClass: '@inputClass',
                ipePattern: '@pattern',
                ipeModel: '=model'
            },

            template:
                '<div>' +
                    '<span ng-hide="isEditorEnabled(ipeModel, ipeFieldName)" ng-click="enableEditor(ipeModel, ipeFieldName)">' +
                        '<span ng-transclude></span>' +
                    '</span>' +
                    '<span ng-show="isEditorEnabled(ipeModel, ipeFieldName)">' +
                        '<div class="input-append">' +
                            '<input type="{{ipeInputType}}" name="{{ipeFieldName}}" class="{{ipeInputClass}}" ' +
                                'ng-required ng-pattern="{{ipePattern}}" ng-model="newFieldValue" ' +
                                'ui-keyup="{enter: \'save(ipeModel, ipeFieldName)\', esc: \'disableEditor()\'}"/>' +
                            '<div class="btn-group btn-group-xs" role="toolbar">' +
                                '<button ng-click="save(ipeModel, ipeFieldName)" type="button" class="btn"><span class="glyphicon glyphicon-ok"></span></button>' +
                                '<button ng-click="disableEditor()" type="button" class="btn"><span class="glyphicon glyphicon-remove"></span></button>' +
                            '</div>' +
                        '</div>' +
                    '</span>' +
                '</div>',

            controller: 'AlbumEditorController'
        };
    });
