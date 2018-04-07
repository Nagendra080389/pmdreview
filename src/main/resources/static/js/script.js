function OrderFormController($scope, $http) {

        var jsonData = $.ajax({
            url: "/getPMDResultsByDateAndSeverity",
            dataType: "json",
            crossDomain: true,
            async: false
        }).responseText;

    if (!jsonData || jsonData === "") {
        window.location.pathname = "../html/noDataFetched.html";
    }

    var parsed = JSON.parse(jsonData);

    if (parsed.error) {
        window.location.pathname = "../html/error.html";
    }

    $scope.selectedClassErrDetails = [];
    $scope.selectedClassName = "";
    $scope.showErrorDetails = function (classNameKey) {
        $scope.selectedClassName = classNameKey;
        $scope.selectedClassErrDetails = $scope.sampleJSON[classNameKey].pmdStructures;
        $('#myModal').modal('show', testAnim('zoomIn'));

    };

    $scope.selectedClassDupErrDetails = [];
    $scope.showDuplicateDetails = function (eachDuplicateData) {
            $scope.selectedClassDupErrDetails = eachDuplicateData;
            $('#myModalDuplicates').modal('show', testAnim('zoomIn'));

        };

    $scope.sampleJSON = parsed.pmdStructureWrapper;
    $scope.sampleJSONDuplicates = parsed.pmdDuplicates;

    $scope.logThisDefect = function() {
        var selClassName = $scope.selectedClassName;
        var selClassErrorDetails = $scope.selectedClassErrDetails;
        console.log("selClassName.........");
        console.log(selClassName);
        console.log(selClassErrorDetails);
    };

};

function testAnim(x) {
    $('.modal .modal-dialog').attr('class', 'modal-dialog  ' + x + '  animated');
};


function parseURLParams(url) {
    var queryStart = url.indexOf("?") + 1,
        queryEnd = url.indexOf("#") + 1 || url.length + 1,
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
        td = tr[i].getElementsByTagName("td")[2];
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