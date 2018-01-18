function ruleEngineModify($scope, $http) {

        var urlString = window.location.href;
        var urlParams = parseURLParams(urlString);

        var jsonData = $.ajax({
            url: "http://USBLRNAGESINGH1:8989/getRuleEngine",
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

        $scope.ruleEngineJSON = parsed;

        $scope.ruleName = "";
        $scope.priority = "";
        $scope.modifyPriority = function(ruleName) {
                $scope.ruleName = ruleName;
                $scope.priority = $scope.ruleEngineJSON[ruleName];
        };
};

function navigateRuleEngine() {
    window.location.pathname = "../html/modifyRuleEngine.html";
}




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
