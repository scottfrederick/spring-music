angular.module('info', ['ngResource']).
    factory('Info', function ($resource) {
        return $resource('appinfo');
    });

function InfoController($scope, Info) {
    $scope.info = Info.get();
}
