function OrderFormController($scope,$http){

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
        angular.element('#myModal').modal('show');
    };
    
    /*$scope.listOfClasses = ['a','b','c'];
    $scope.tryJSONTest = {'a':[{'c1':'pras1','lineNo':12},{'c2':'pras2','lineNo':2}]},
                         {'b':[{'cb1':'pras1','lineNo':12},{'cb2':'pras2','lineNo':2}]},
                         {'c':[{'cc1':'pras1','lineNo':12},{'cc2':'pras2','lineNo':2}]};*/
    
    $scope.sampleJSON = parsed;
    
};