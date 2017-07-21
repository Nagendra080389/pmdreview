/**
 * Created by nagesingh on 7/17/2017.
 */

if (!Modernizr.touch || !Modernizr.inputtypes.date) {
    $('input[type=date]')
        .attr('type', 'text')
        .datepicker({
            // Consistent format with the HTML5 picker
            dateFormat: 'yy-mm-dd'
        });
}