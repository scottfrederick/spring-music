angular.module('info', ['ngResource']).
    factory('Info', function ($resource) {
        return $resource('info');
    });

function InfoController($scope, Info) {
    $scope.info = Info.get();
}
