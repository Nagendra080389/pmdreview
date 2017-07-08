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

    for (var i = 0, keys = Object.keys(parsed), ii = keys.length; i < ii; i++) {
        var eachValueAsArray = parsed[keys[i]];
        for (var j = 0; j < eachValueAsArray.length; j++) {
            dataArray.push([keys[i], eachValueAsArray[j].lineNumber , eachValueAsArray[j].reviewFeedback]);
        }
        //console.log('key : ' + keys[i] + ' val : ' + parsed[keys[i]]);
    }



    //dataArray.push(['test','test','test']);
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
    chart.draw(data, {width: 1920, height: 1080});
}