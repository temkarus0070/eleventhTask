

$(document).ready(function (){
   const formFieldSet=$("fieldset")[0];
   const chatTypeField=$("#chatType");
   
   chatTypeField.trigger("change");

   chatTypeField.change(function (){
       changeFields(chatTypeField.val());
   });


   function changeFields(chatType){
       switch (chatType){
           case "PRIVATE":
               addFieldsToPrivateChat();
               break;
           case "GROUP":
               addFieldToGroupChat();
               break;
           case "ROOM":
               addFieldsToNameableChat();
               break;
       }
    }

    function addFieldsToPrivateChat(){
        deleteFields(["chatName","usersCount"])
    }

    function addFieldsToNameableChat(){
       deleteField("usersCount");
        if (!hasFields("chatName")) {
            addField("text","chatName","chatName",formFieldSet,"chat name");
        }
    }

    function addFieldToGroupChat(){
       addFieldsToNameableChat();
       if(!hasFields("usersCount")){
           addField("number","usersCount","usersCount",formFieldSet,"max users count");
       }
    }

    function addField(type,id,name,formFieldSet,labelValue){
           $('<label>').attr({
               for: id
           }).text(labelValue).appendTo(formFieldSet);
           $('<input>').attr({
               type: type,
               id: id,
               name: name,
               required:true
           }).appendTo(formFieldSet);
    }


    function hasFields(id){
       return $(`#${id}`).length!==0;
    }

    function deleteFields(idArray){
       for(let id of idArray){
           console.log(id);
           deleteField(id);
       }
    }

    function deleteField(id){
       $(`label[for='${id}'`).remove();
       $(`input[id='${id}']`).remove();
    }


});