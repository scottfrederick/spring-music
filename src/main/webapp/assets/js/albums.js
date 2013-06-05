angular.module('albums', ['ngResource']).
    factory('Albums', function ($resource) {
        return $resource('albums');
    }).
    factory('Album', function ($resource) {
        return $resource('albums/:id', {id: '@id'});
    }).
    factory("EditorStatus", function () {
        var editorEnabled = {};

        var enable = function (id, fieldName) {
            this.editorEnabled = { 'id': id, 'fieldName': fieldName };
        };

        var disable = function () {
            this.editorEnabled = {};
        };

        var isEnabled = function(id, fieldName) {
            return (this.editorEnabled['id'] == id && this.editorEnabled['fieldName'] == fieldName);
        };

        return {
            isEnabled: isEnabled,
            enable: enable,
            disable: disable
        }
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
                                'ng-required ng-model="newFieldValue" ' +
                                'ui-keyup="{enter: \'save(ipeModel, ipeFieldName)\', esc: \'disableEditor()\'}"/>' +
                            '<button ng-click="save(ipeModel, ipeFieldName)" type="button" class="btn"><i class="icon-ok"></i></button>' +
                            '<button ng-click="disableEditor()" type="button" class="btn"><i class="icon-remove"></i></button>' +
                        '</div>' +
                    '</span>' +
                '</div>',

            controller: 'AlbumEditorController'
        };
    });
