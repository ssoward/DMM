function createRequestObject() {
    var tmpXmlHttpObject;
    if (window.XMLHttpRequest) {
        tmpXmlHttpObject = new XMLHttpRequest();

    } else if (window.ActiveXObject) {
        tmpXmlHttpObject = new ActiveXObject("Microsoft.XMLHTTP");
    }
    return tmpXmlHttpObject;
}

var http = createRequestObject();

// Start ------------ DELETE INVOICE
function deleteInvoice(){
    startSpinner();
    var text =  document.getElementById("text").value;
    var reas =  document.getElementById("reason").value;
    var inum =  document.getElementById("invoiceNum").value;
    var rinv =  document.getElementById("reverseCheckbox").checked;
    //alert(':'+text+'\n:'+inum+'\n:'+reas+'\n:'+rinv);

    http.open('get', "InvoiceUtilAjax?reverseInv="+rinv+"&reason="+reas+"&text="+text+"&inum="+inum+"&function=deleteInvoice");
    http.onreadystatechange = processResponse;
    http.send(null);
}
function processResponse() {
    if(http.readyState == 4){
        var response = http.responseText;
        closeModal();
        stopSpinner();
        setMessage(response);
        document.getElementById("basic-modal").style.display = 'none';
//        var ajaxResponse = document.getElementById("description");
//        ajaxResponse.innerHTML = response;
//        $('#description').fadeIn(1000);
//        $('#description').fadeOut(6000);
    }
}
function setMessage(str){

    var ajaxResponse = document.getElementById("description");
    ajaxResponse.innerHTML = str;
    $('#description').fadeIn(1000);
    $('#description').fadeOut(6000);

}
function closeModal(){
    $('#closeModalSimple').click();
}
// End ------------ DELETE INVOICE