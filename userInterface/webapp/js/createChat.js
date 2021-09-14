

$(document).ready(function (){
   const formFieldSet=$("fieldset")[0];
   const chatTypeField=$("#chatType");

   chatTypeField.change(function (){
       changeFields(chatTypeField.val());
   });


   function changeFields(chatType){
       addFieldsToPrivateChat();
       switch (chatType){
           case "privateChat":
               addFieldsToPrivateChat();
               break;
           case "groupChat":
addFieldToGroupChat();
               break;
           case "roomChat":
               break;
       }
    }

    function addFieldsToPrivateChat() {
        if (!hasFields("secondUserName")) {
           addField("text","secondUserName","secondUserName",formFieldSet,"second username");
        }
    }

    function addFieldToGroupChat(){
       if(!hasFields("usersCount")){
           addField("number","usersCount","usersCount",formFieldSet,"max users count");
       }
    }

    function addField(type,id,name,formFieldSet,labelValue){
           $('label').attr({
               for: id
           }).text(labelValue).appendTo(formFieldSet);
           $('input').attr({
               type: type,
               id: id,
               name: name
           }).appendTo(formFieldSet);
    }


    function hasFields(id){
       return $(`#${id}`).length!==0;
    }

    function deleteField(id){
       $(`label[for='${id}'`).remove();
       $(`input[id='${id}']`).remove();
    }


});