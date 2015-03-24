
var http = createRequestObject();
var ccc = '1000';

function sendEmail(){
    startSpinner();
    var reList   = document.getElementById("eRecipients").value;
    var eCC      = document.getElementById("eCC").value;
    var eSubject = document.getElementById("eSubject").value;
    var eBody    = document.getElementById("eBody").value;
    var bCC      = document.getElementById("bCC").value;
//    alert(
//            reList+"\n"+
//            eCC+"\n"+
//            eSubject+"\n"+
//            eBody+"\n"
//            )
    eBody = eBody.replace(/\n\r?/g, '<br />');

    var str = 'ajaxUtil?function=sendEmail&'+
              'reList='   +reList  +'&'+
              'eCC='      +eCC     +'&'+
              'bCC='      +bCC     +'&'+
              'eSubject=' +eSubject+'&'+
              'eBody='    +encodeURIComponent(eBody);
    //alert(str);
    closeModal();
    http.open('get', str);
    http.onreadystatechange = processResponse;
    http.send(null);
}
function processResponse() {
    if(http.readyState == 4){
        var response = http.responseText;
        setMessage(response)
        stopSpinner();
    }
}