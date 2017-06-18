/**
 * Created by Nagendra on 18-06-2017.
 */

google.charts.load('current', {packages: ['table']});

// Set a callback to run when the Google Visualization API is loaded.
google.setOnLoadCallback(drawChart);

function drawChart() {

    var jsonData = $.ajax({
        url: "http://localhost:8989/getPMDResults",
        dataType: "json",
        crossDomain: true,
        async: false
    }).responseText;

    var parsed = JSON.parse(jsonData);

    var arr = [];

    var reviewMap = {};

    var dataArray = [['Class Name', 'Line Number', 'Code Review Result']];

    function PMDStructure(lineNumber, reviewFeedback){
        this.lineNumber = lineNumber;
        this.reviewFeedback = reviewFeedback;
    }

    for (var Key in parsed) {
        reviewMap.push(Key, new PMDStructure(parsed[Key]))

    }

    dataArray.push(['test'],['test'],['test']);
    /*for (var Key in parsed) {
        //reviewMap[x] = parsed[x];
        dataArray.push(['test'],['test'],['test']);
    }*/


    /*for (var x in parsed) {
        arr.push(parsed[x]);
    }
*/
    /*var dataArray = [['Class Name', 'Line Number', 'Code Review Result']];
    for (var i = 0; i < arr.length; i++) {
        dataArray.push([arr[i]],[arr[i]],[arr[i]]);
    }*/

    var data = new google.visualization.arrayToDataTable(dataArray);
    var chart = new google.visualization.Table(document.getElementById('pmd_reviewResult_div'));
    chart.draw(data, {width: 400, height: 240});
}