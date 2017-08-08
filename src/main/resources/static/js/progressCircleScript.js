function progressCircleController($scope,$http){

    var urlString = window.location.href;
    var urlParams = parseURLParams(urlString);

    var date = urlParams['date'];

    var jsonData = $.ajax({
        url: "http://localhost:8989/getPMDResults",
        dataType: "json",
        crossDomain: true,
        async: false
    }).responseText;

    var parsed = JSON.parse(jsonData);
    
    $scope.selectedClassErrDetails = [];
    $scope.selectedClassName = "";
    $scope.showErrorDetails = function(classNameKey){
        $scope.selectedClassName = classNameKey;
        $scope.selectedClassErrDetails = $scope.sampleJSON[classNameKey];
        angular.element('#progressModal').modal('show');
    };
    
    $scope.sampleJSON = parsed;
    
};