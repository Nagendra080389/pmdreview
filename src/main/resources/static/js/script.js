function OrderFormController($scope,$http){

    var urlString = window.location.href;
    var urlParams = parseURLParams(urlString);

    var date = urlParams['date'];

    var jsonData = $.ajax({
        url: "http://localhost:8989/getPMDResultsByDate"+"?date="+date,
        dataType: "json",
        crossDomain: true,
        async: false
    }).responseText;

    if(jsonData === ""){
        window.location.pathname = "../html/noDataFetched.html";
    }
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

function searchFunction() {
    var input, filter, table, tr, td, i;
    input = document.getElementById("searchBar");
    filter = input.value.toUpperCase();
    table = document.getElementById("errorTable");
    tr = table.getElementsByTagName("tr");
    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[1];
        if (td) {
            if (td.innerHTML.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function resetSearchContent() {
    var content = document.getElementById("searchBar");
    content.value = null;
}


function returnHomepage() {
    window.location.pathname = "../index.html";
}