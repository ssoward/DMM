function applicationNotificaiton(message, messageType){
    document.getElementById("notMessage").innerHTML = message;
    $("#notificationMessage").slideDown(300).delay(3000).slideUp(300);
}