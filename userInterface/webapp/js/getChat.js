
$(document).ready(function (){
    $('#chatType').change(function(){
        let chatType = $(this).val();
        setFormFields(chatType);
    });




    const mainFieldSet = $('fieldset')[0];
    console.log(JSON.stringify(mainFieldSet));



    function setFormFields(chatType){
        console.log(JSON.stringify(mainFieldSet));
        switch (chatType){
            case "PrivateChat":
                $("input[name='chatName']").remove();
                $("label[for='chatName']").remove();
                $('<label>').attr({
                    for: "chatId"
                }).text("chat identifier").appendTo(mainFieldSet);
                    $('<input>').attr({
                        type: "number",
                        id: "chatId",
                        name: "chatId"
                    }).appendTo(mainFieldSet);
                break;


            default:
                $("input[name='chatId']").remove();
                $("label[for='chatId']").remove();
                if( !$("input[name='chatName']").length) {
                    $('<label>').attr({
                        for: "chatName",
                        value: "chat name"
                    }).text("chat name").appendTo(mainFieldSet);
                    $('<input>').attr({
                        type: "text",
                        id: "chatName",
                        name: "chatName"
                    }).appendTo(mainFieldSet);
                }
                break;

        }
    }
})

