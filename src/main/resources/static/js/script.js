function OrderFormController($scope,$http){

    $('#img').show();
    var urlString = window.location.href;
    var urlParams = parseURLParams(urlString);

    var date = urlParams['date'];

    var jsonData = $.ajax({
        url: "http://localhost:8989/getPMDResultsByDate"+"?date="+date,
        dataType: "json",
        crossDomain: true,
        async: false
    }).responseText;

    $('#img').hide();
    var parsed = JSON.parse(jsonData);
    $scope.selectedClassErrDetails = [];
    $scope.selectedClassName = "";
    $scope.showErrorDetails = function(classNameKey){
        $scope.selectedClassName = classNameKey;
        $scope.selectedClassErrDetails = $scope.sampleJSON[classNameKey];
        angular.element('#myModal').modal('show');
    };
    
    $scope.sampleJSON = parsed;
    
};

function parseURLParams(url) {
    var queryStart = url.indexOf("?") + 1,
        queryEnd   = url.indexOf("#") + 1 || url.length + 1,
        query = url.slice(queryStart, queryEnd - 1),
        pairs = query.replace(/\+/g, " ").split("&"),
        parms = {}, i, n, v, nv;

    if (query === url || query === "") return;

    for (i = 0; i < pairs.length; i++) {
        nv = pairs[i].split("=", 2);
        n = decodeURIComponent(nv[0]);
        v = decodeURIComponent(nv[1]);

        if (!parms.hasOwnProperty(n)) parms[n] = [];
        parms[n].push(nv.length === 2 ? v : null);
    }
    return parms;
}