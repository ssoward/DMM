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
function makeGetRequest(someVarOne) {
    http.open('get', "AccountSearchAjax?productNum="+someVarOne+"&function=searchAccounts");
    http.onreadystatechange = processResponse;
    http.send(null);
}
function processResponse() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById("description").innerHTML = response;
    }
}
function operSelected(selected){
    var chosenoption=selected.options[selected.selectedIndex];
    document.getElementById("searchString").value = chosenoption.text;
    document.getElementById("selectedAccountNum").value = chosenoption.value;
}
function closeDropDownAccounts(){
    startSpinner();
    var sel = document.getElementById('productOptions');
    var chosenoption=sel.options[sel.selectedIndex];
    //alert('text: '+chosenoption.text+' VALUE: '+chosenoption.value);
    document.getElementById('description').innerHTML = '';
    makeGetRequestFetchAccountForId(chosenoption.value)
}
function runSubmit(e){
    //document.searchAccountName.submit();
    if (window.event) { e = window.event; }

    ///enter
    if (e.keyCode == 13){
        closeDropDownAccounts();
    }
}
function makeGetRequestFetchAccountForId(acctId) {
    http.open('get', "AccountSearchAjax?acctId="+acctId+"&function=fetchAccount");
    http.onreadystatechange = processResponseAcctFetched;
    http.send(null);
}
function processResponseAcctFetched() {
    if(http.readyState == 4){
        var response = http.responseText;
        //document.getElementById("description").innerHTML = response;
        acct = JSON.parse(response, function (key,value){}() );
        //alert(acct.accountName);
        populateAccountDisplay(acct);
        stopSpinner();
    }
}
function populateAccountDisplay(acct){
    document.getElementById('selectedAccountName').value      = acct.accountName;
    var dsi =''
    dsi += '<p>'+   acct.accountName +' ('+acct.accountNum+')';
    if(acct.accountContact){
        dsi += '<br/>'+   acct.accountContact   ;
    }
    if(acct.accountEmail){
        dsi += '<br/>'+   acct.accountEmail     ;
    }
    if(acct.accountPhone1){
        dsi += '<br/>'+   acct.accountPhone1    ;
    }
    if(acct.accountPhone2){
        dsi += '     '+   acct.accountPhone2    ;
    }
    dsi += '<br/>'+   acct.accountStreet    ;
    dsi += '<br/>'+   acct.accountCity      ;
    dsi += '  '+   acct.accountState     ;
    dsi += '  '+   acct.accountZip       ;
    dsi += '</p>';
    dsi += '<input type=\"button\" onclick=\"toggleAcctEdit(2);\" value=\"Edit\"/>';

    document.getElementById('accountDisplay').innerHTML = dsi;

    populateAccountEdit(acct);
}

function toggleAcctEdit(vv){
    if(vv >0){
        document.getElementById('editAccount').style.display='';
        document.getElementById('accountDisplay').style.display='none';
    }else{
        document.getElementById('editAccount').style.display='none';
        document.getElementById('accountDisplay').style.display='';
        var acctId = document.getElementById('accountNum').innerHTML;
    }
}

function populateAccountEdit(acct){
    document.getElementById("ccard").style.display = '';
    document.getElementById("accountNum"        ).innerHTML=acct.accountNum       ;
    document.getElementById("hiddenAccountNum"  ).value=acct.accountNum       ;
    document.getElementById("accountName"       ).value=acct.accountName      ;
    document.getElementById("accountEmail"      ).value=acct.accountEmail     ;
    document.getElementById("accountPhone1"     ).value=acct.accountPhone1    ;
    document.getElementById("accountPhone2"     ).value=acct.accountPhone2    ;
    document.getElementById("accountStreet"     ).value=acct.accountStreet    ;
    document.getElementById("accountCity"       ).value=acct.accountCity      ;
    document.getElementById("accountState"      ).value=acct.accountState     ;
    document.getElementById("accountZip"        ).value=acct.accountZip       ;
    //document.getElementById("accountFax"        ).value=acct.accountFax       ;
    document.getElementById("accountContact"    ).value=acct.accountContact   ;
    //document.getElementById("accountPassword"   ).value=acct.accountPassword  ;
    //document.getElementById("accountPostalCode" ).value=acct.accountPostalCode;
    //document.getElementById("accountCountry"    ).value=acct.accountCountry   ;
    //document.getElementById("accountType1"      ).value=acct.accountType1     ;
    //document.getElementById("accountType2"      ).value=acct.accountType2     ;
    //document.getElementById("accountOpenDate"   ).value=acct.accountOpenDate  ;
    //document.getElementById("accountCloseDate"  ).value=acct.accountCloseDate ;
    //document.getElementById("accountBalance"    ).value=acct.accountBalance   ;
    //document.getElementById("accountTotalSpent" ).value=acct.accountTotalSpent;
    //document.getElementById("accountTotalDisc"  ).value=acct.accountTotalDisc ;
    //document.getElementById("accountStartDate"  ).value=acct.accountStartDate ;
    //document.getElementById("accountEndDate"    ).value=acct.accountEndDate   ;
    //document.getElementById("accountInv"        ).value=acct.accountInv       ;
}
function resetAccount(){
    document.getElementById("ccard").style.display = 'none';
    document.getElementById("accountNum"        ).innerHTML='';
    document.getElementById("accountDisplay"    ).innerHTML='';
    document.getElementById("hiddenAccountNum"  ).value='';
    document.getElementById("searchString"      ).value='';
    document.getElementById("accountName"       ).value='';
    document.getElementById("accountEmail"      ).value='';
    document.getElementById("accountPhone1"     ).value='';
    document.getElementById("accountPhone2"     ).value='';
    document.getElementById("accountStreet"     ).value='';
    document.getElementById("accountCity"       ).value='';
    document.getElementById("accountState"      ).value='';
    document.getElementById("accountZip"        ).value='';
    document.getElementById("accountContact"    ).value='';
}
function makeGetRequestSaveUpdateAccount(acctId) {
    var r = confirm('Are you sure you want save or update this Account?');
    if(r==true){
        startSpinner();
        var accNum = document.getElementById("hiddenAccountNum"  ).value;
        var accNam = escape(document.getElementById("accountName"       ).value);
        var accEma = escape(document.getElementById("accountEmail"      ).value);
        var accPh1 = escape(document.getElementById("accountPhone1"     ).value);
        var accPh2 = escape(document.getElementById("accountPhone2"     ).value);
        var accStr = escape(document.getElementById("accountStreet"     ).value);
        var accCit = escape(document.getElementById("accountCity"       ).value);
        var accSta = escape(document.getElementById("accountState"      ).value);
        var accZip = escape(document.getElementById("accountZip"        ).value);
        var accCon = escape(document.getElementById("accountContact"    ).value);

        http.open('get', "AccountSearchAjax?accountNum="+accNum+"&accountName="+accNam+"&accountEmail="+accEma+"&accountPhone1="+accPh1+"&accountPhone2="+accPh2+"&accountStreet="+accStr+"&accountCity="+accCit+"&accountState="+accSta+"&accountZip="+accZip+"&accountContact="+accCon+"&function=saveUpdateAccount");
        http.onreadystatechange = processResponseAcctSaved;
        http.send(null);
    }
}
function processResponseAcctSaved() {
    if(http.readyState == 4){
        var response = http.responseText;
        acct = JSON.parse(response, function (key,value){}() );
        populateAccountDisplay(acct);
        stopSpinner();
    }
}
function toggleCheck(obj){
    for(var i = 1; i<10000;i++){
        var q = document.getElementById('update'+i);
        if(q){
            if(obj.checked){
                q.checked=true;
            }else{
                q.checked=false;
            }
        }
    }

}
function getCheckedEmails(){

    var arr = new Array();
    var cc = 0;
    for(var i = 1; i<10000;i++){
        var q = document.getElementById('update'+i);
        if(q && q.checked==true){
            var email = document.getElementById('accountSendEmail'+i).value;
            if(email){
                arr[cc] = email;
                cc++;
            }
        }
    }
    if(cc<1){
        //docuemnt.getElementById('modalMessage').innerHTML = 'No account checked and/or the accounts are missing email addresses.';
    }else{
        document.getElementById('bCC').value = arr;
    }

}
function getCreatPDFAddresses(){

    var arr = new Array();
    var cc = 0;
    for(var i = 1; i<10000;i++){
        var q = document.getElementById('update'+i);
        if(q && q.checked==true){
            var email = document.getElementById('accountNum'+i).innerHTML;
            if(email){
                arr[cc] = email;
                cc++;
            }
        }
    }
//    alert('Comming soon\n'+arr);
    window.location = 'AccountAddresses.pdf?reportCollection=AccountAddressesGridReport&function=AccountAddresses&aList='+arr;

}

function updateAccountTypes() {
    var obj =  document.getElementById('updateAcctType');
    var co=obj.options[obj.selectedIndex];
    var arr = new Array();
    var cc = 0;
    for(var i = 1; i<10000;i++){
        var q = document.getElementById('update'+i);
        if(q && q.checked==true){
            arr[cc] = document.getElementById('accountNum'+i).innerHTML;
            cc++;
        }
    }
    if(co.value.length<1){
        alert('No account type has been selected');
    }
    else if(arr.length<1){
        alert('No accounts have been selected.\nTo update the account types,\nplease check the box next to the accounts that need updating.');
    }else{
        var r = confirm('This will update the account types to\n'+co.text+'\nfor all checked accounts ('+arr.length+').\nAre you sure you want to continue with this process?');
        if(r==true){
            document.getElementById("ajaxMessage").innerHTML='';
            startSpinner();
            http.open('get', "AccountSearchAjax?acctType="+co.value+"&accList="+arr+"&function=updateAccountTypes");
            http.onreadystatechange = processResponseUpdateAccountTypes;
            http.send(null);

        }
    }
}
function processResponseUpdateAccountTypes() {
    if(http.readyState == 4){
        var response = http.responseText;
        var v = document.getElementById("ajaxMessage")
        v.innerHTML = response;
        stopSpinner();

    }
}
function saveCC() {
    startSpinner();
    var ccNum   = document.getElementById("ccNum").value;
    var ccPid   = document.getElementById("ccPid").value;
    var ccType   = document.getElementById("ccType").value;
    var ccMonth = document.getElementById("ccMonth").value;
    var ccYear   = document.getElementById("ccYear").value;
    var ccCSC   = document.getElementById("ccCSC").value;
    var accNum = document.getElementById("accountNum").innerHTML;
    var accName = document.getElementById("accountName").value;

    http.open('get', "AccountSearchAjax?accountName="+accName+"&accountNum="+accNum+"&ccPid="+ccPid+"&ccNum="  + ccNum   +"&ccType=" + ccType  +"&ccMonth="+ ccMonth +"&ccYear=" + ccYear  +"&ccCSC="  + ccCSC   +"&function=saveCCInfo");
    http.onreadystatechange = processResponseSaveCC;
    http.send(null);
}

function processResponseSaveCC() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById("ccResults").innerHTML = response;
        //alert(response);
        stopSpinner();
    }
}
function fetchCCInfo() {
    startSpinner();
    var accNum = document.getElementById("accountNum").innerHTML;

    http.open('get', "AccountSearchAjax?accountNum="+accNum+"&function=fetchCCInfo");
    http.onreadystatechange = processResponseFetchCC;
    http.send(null);
}

function processResponseFetchCC() {
    if(http.readyState == 4){
        var response = http.responseText;
        try{
            if(response){
                // document.getElementById("ccResults").innerHTML = response;
                //  alert(response);
                acct = JSON.parse(response, function (key,value){}() );
                populateCCInfo(acct);
            }
        }catch(e){}

        stopSpinner();
    }
}
function populateCCInfo(ccInfo){
    document.getElementById("ccNum").value=ccInfo.number;
    document.getElementById("ccPid").value=ccInfo.pid;
    document.getElementById("ccType").value=ccInfo.type;
    document.getElementById("ccMonth").value=ccInfo.month;
    document.getElementById("ccYear").value=ccInfo.year;
    document.getElementById("ccCSC").value=ccInfo.code;
}

