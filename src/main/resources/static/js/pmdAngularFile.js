/**
 * Created by nagesingh on 7/7/2017.
 */
var MainController = function($scope, $http) {

    var promise = $http.get("http://localhost:8989/getPMDResults");
    promise.then(function (response) {
        $scope.pmd_reviewResult_div = response.data;
    })

    var person = {
        firstName: "Nagendra"
    };
    $scope.person = person;
    $scope.message = "Hello Angular!";
};


angular.module('mainApp', ['ui.bootstrap'])
    .controller('MainController', MainController);