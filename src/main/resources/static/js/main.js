// "use strict";
// $('.column100').on('mouseover',function(){
// 	var table1 = $(this).parent().parent().parent();
// 	var table2 = $(this).parent().parent();
// 	var verTable = $(table1).data('vertable')+"";
// 	var column = $(this).data('column') + "";
//
// 	$(table2).find("."+column).addClass('hov-column-'+ verTable);
// 	$(table1).find(".row100.head ."+column).addClass('hov-column-head-'+ verTable);
// });
//
// $('.column100').on('mouseout',function(){
// 	var table1 = $(this).parent().parent().parent();
// 	var table2 = $(this).parent().parent();
// 	var verTable = $(table1).data('vertable')+"";
// 	var column = $(this).data('column') + "";
//
// 	$(table2).find("."+column).removeClass('hov-column-'+ verTable);
// 	$(table1).find(".row100.head ."+column).removeClass('hov-column-head-'+ verTable);
// });


// $(document).ready(function() {
//     var max_fields      = 10; //maximum input boxes allowed
//     var wrapper   		= $(".input_fields_wrap"); //Fields wrapper
//     var add_button      = $(".add_field_button"); //Add button ID
//
//     var x = 1; //initlal text box count
//     $(add_button).click(function(e){ //on add input button click
//         e.preventDefault();
//         if(x < max_fields){ //max input box allowed
//             x++; //text box increment
//             $(wrapper).append('<div><input type="text" name="mytext[]"/><a href="#" class="remove_field">Remove</a></div>'); //add input box
//         }
//     });
//
//     $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
//         e.preventDefault(); $(this).parent('div').remove(); x--;
//     })
// });


$(document).ready(function () {
    var next = 1;
    $(".add-more").click(function (e) {
        e.preventDefault();
        var addto = "#field" + next;
        var addRemove = "#field" + (next);
        next = next + 1;
        var newIn = '<input autocomplete="off" class="input form-control" id="field' + next + '" name="field' + next + '" type="text">';
        var newInput = $(newIn);
        var removeBtn = '<button id="remove' + (next - 1) + '" class="btn btn-danger remove-me" >-</button></div><div id="field">';
        var removeButton = $(removeBtn);
        $(addto).after(newInput);
        $(addRemove).after(removeButton);
        $("#field" + next).attr('data-source', $(addto).attr('data-source'));
        $("#count").val(next);

        $('.remove-me').click(function (e) {
            e.preventDefault();
            var fieldNum = this.id.charAt(this.id.length - 1);
            var fieldID = "#field" + fieldNum;
            $(this).remove();
            $(fieldID).remove();
        });
    });


});

