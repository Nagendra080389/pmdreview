function progressCircleController($scope,$http){


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
        $scope.selectedClassErrDetails = $scope.sampleJSON[classNameKey].pmdStructures;
        angular.element('#progressModal').modal('show');
    };
    
    $scope.sampleJSON = parsed;
    
};


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