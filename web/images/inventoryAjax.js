function selectedProd(event){
    var code = event.keyCode;
    if (code==13){
        var sel = document.getElementById('productOptions');
        var chosenoption=sel.options[sel.selectedIndex];
        document.getElementById('addNewProd').value=chosenoption.text;
        document.getElementById('hiddenProdId').value=chosenoption.value;
        document.getElementById('newSupProd').innerHTML='';
    }
}
function selectedProdDbClick(){
    var sel = document.getElementById('productOptions');
    var chosenoption=sel.options[sel.selectedIndex];
    document.getElementById('addNewProd').value=chosenoption.text;
    document.getElementById('hiddenProdId').value=chosenoption.value;
    document.getElementById('newSupProd').innerHTML='';
}

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
var ccc = '1000';
function makeGetRequest(
        productNum,
        productName,
        productAuthor,
        productArtist,
        productArranger,
        productDescription,
        category,
        productCost1,
        productCost2,
        productCost3,
        productCost4,
        productBarCode,
        productSKU,
        productCatalogNum,
        productSupplier1,
        productSupplier2,
        productSupplier3,
        productSupplier4,
        numAvailable,
        lastSold,
        lastInvDate,
        DCCatalogNum) {
    http.open('get', "InventoryAjax?productNum="+productNum+"&productName="+productName+"&productAuthor="+productAuthor+"&productArtist="+productArtist+"&productArranger="+productArranger+"&productDescription="+productDescription+"&category="+category+"&productCost1="+productCost1+"&productCost2="+productCost2+"&productCost3="+productCost3+"&productCost4="+productCost4+"&productBarCode="+productBarCode+"&productSKU="+productSKU+"&productCatalogNum="+productCatalogNum+"&productSupplier1="+productSupplier1+"&productSupplier2="+productSupplier2+"&productSupplier3="+productSupplier3+"&productSupplier4="+productSupplier4+"&numAvailable="+numAvailable+"&lastSold="+lastSold+"&lastInvDate="+lastInvDate+"&DCCatalogNum="+DCCatalogNum);
    //http.open('get', "AjaxStats?htstatPid="+wordId+"&oper="+num);
    http.onreadystatechange = processResponse;
    http.send(null);
}
function processResponse() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById("description"+ccc).innerHTML = response;
    }
}
var invent = '';
function makeGetRequest2(instrPid, location, count, funct) {
    invent = count;
    http.open('get', "InstrumentAjax?location="+location+"&instrPid="+instrPid+"&funct="+funct);
    http.onreadystatechange = processResponse2;
    http.send(null);
}

function makeGetRequest4(supNum, prodNum, count, locationName) {
    invent = prodNum;
    http.open('get', "InvUtil?locationName="+locationName+"&supNum="+supNum+"&prodNum="+prodNum+"&count="+count+"&invCount="+true);
    http.onreadystatechange = processResponse4;
    http.send(null);
}

function processResponse4() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById("invCount"+invent).innerHTML = response;
    }
}

function makeGetRequest3(supNum, inventoryIndex, count, locationName, checked) {
    invent = count;
    http.open('get', "InvUtil?locationName="+locationName+"&supNum="+supNum+"&inventoryIndex="+inventoryIndex+"&checked="+checked);
    http.onreadystatechange = processResponse3;
    http.send(null);
}
function makeGetRequest5(accountType, count) {
    invent = count;
    http.open('get', "InvUtil?accountTypeUpdate="+accountType+"&acct="+count);
    http.onreadystatechange = processResponse5;
    http.send(null);
}
function processResponse2() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById("description"+invent).innerHTML = response;
    }
}

function processResponse3() {
    if(http.readyState == 4){
        var response = http.responseText;
        //document.getElementById("description"+invent).innerHTML = response;
    }
}
function processResponse5() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById("description"+invent).innerHTML = response;
    }
}

function processResponsesaveNotes() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById(invent).innerHTML = response;
    }
}
function saveNotes(note, orderNum) {
    invent = 'notes';
    http.open('get', "InvUtil?note="+note+"&orderNum="+orderNum+"&function=saveOrderNotes");
    http.onreadystatechange = processResponsesaveNotes;
    http.send(null);
}

function processResponseNotesModal() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById(invent).innerHTML = response;
    }
}
function makeGetRequestNotesModal(orderNum, userName) {
    invent = 'notes';
    http.open('get', "InvUtil?orderNum="+orderNum+"&userName="+userName+"&function=getOrderNotes");
    http.onreadystatechange = processResponseNotesModal;
    http.send(null);
}

// start supplier data save
function processResponseSupplierDataSave() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById("description"+invent).innerHTML = response;
    }
}
function makeGetRequestSupplierDataSave(supNum, loc, val) {
    invent = 'LOCATION';
    http.open('get', "SupUtil?supNum="+supNum+"&loc="+loc+"&val="+val+"&function=saveSupplierData");
    http.onreadystatechange = processResponseSupplierDataSave;
    http.send(null);
}
// start supplier data end

function processResponseSupplierDataSend() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById("description"+invent).innerHTML = response;
    }
}
function makeGetRequestSupplierDataSend(supNum, locationName) {
    invent = supNum;
    http.open('get', "InvUtil?locationName="+locationName+"&supNum="+supNum+"&function=supSend");
    http.onreadystatechange = processResponseSupplierDataSend;
    http.send(null);
}

// ===start supplier product order count
function makeGetRequestSupplierOrderCountSave(supNum, orderNum, count, locationName) {
    invent = orderNum+'orderCount';
    http.open('get', "InvUtil?locationName="+locationName+"&orderNum="+orderNum+"&count="+count+"&supNum="+supNum+"&function=saveSupProdOrderCount");
    http.onreadystatechange = processResponseSupplierOrderCountSave;
    http.send(null);
}

function processResponseSupplierOrderCountSave() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById(invent).innerHTML = response;
    }
}
// ===end supplier product order count
function makeGetRequestDeleteAllProductSupplierOrder(obj, supName, supNum, locationName) {
    var del = confirm('Are you sure you want to delete all orders for '+supName+'?');
    if(del){
        obj.style.display = 'none';
        invent = 'orderTable';
        http.open('get', "InvUtil?locationName="+locationName+"&supNum="+supNum+"&function=deleteAllSupProdOrderCount");
        http.onreadystatechange = processResponseProductAllSupplierOrder;
        http.send(null);
    }
}

function processResponseProductAllSupplierOrder() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById(invent).innerHTML = response;
    }
}
// ===start delete supplier product order count
function makeGetRequestDeleteProductSupplierOrder(supNum, orderId, locationName) {
    invent = 'orderTable';
    http.open('get', "InvUtil?locationName="+locationName+"&supNum="+supNum+"&orderId="+orderId+"&function=deleteSupProdOrderCount");
    http.onreadystatechange = processResponseProductSupplierOrder;
    http.send(null);
}

function processResponseProductSupplierOrder() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById(invent).innerHTML = response;
    }
}
// ===end delete supplier product order count
// ===start supplier product order count
function makeGetRequestAddNewProduct(supNum, prodNum, locationName){
    invent = 'orderTable';
    http.open('get', "InvUtil?locationName="+locationName+"&prodNum="+prodNum+"&supNum="+supNum+"&function=saveNewProd");
    http.onreadystatechange = processResponseAddNewProduct;
    http.send(null);
}

function processResponseAddNewProduct() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById(invent).innerHTML = response;
    }
}
// ===end supplier product order count

// ===start get select list for prod string
function makeGetRequestShowSupProdOptions(prodStr, supNum) {
    invent = 'newSupProd';
    http.open('get', "InvUtil?prodStr="+prodStr+"&supNum="+supNum+"&function=fetchSupProdStr");
    http.onreadystatechange = processResponseShowSupProdOptions;
    http.send(null);
}

function processResponseShowSupProdOptions() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById(invent).innerHTML = response;
    }
}
// ===end get select list for prod string
// ===start get select list for prod string
function makeGetRequestShowSupOrdersEmail(supNum, locationName) {
    invent = 'basic-modal1-content';
    http.open('get', "InvUtil?locationName="+locationName+"&supNum="+supNum+"&function=showSupOrdersEmail");
    http.onreadystatechange = processResponseShowSupOrdersEmail;
    http.send(null);
}

function processResponseShowSupOrdersEmail() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById(invent).innerHTML = response;
    }
}
// ===end get select list for prod string
//

function makeGetRequestToggleArrivedOrder(orderNum, userName) {
    invent = orderNum;
    http.open('get', "InvUtil?userName="+userName+"&orderNum="+orderNum+"&function=toggleOrder");
    http.onreadystatechange = processResponseToggleArrivedOrder;
    http.send(null);
}

function processResponseToggleArrivedOrder() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById(invent).innerHTML = response;
    }
}

// ===start history fetch daily report
function makeGetRequestFetchAnnualHist(prodNum, loca) {
    invent = prodNum+'history';
    http.open('get', "InvUtil?locationName="+loca+"&prodNum="+prodNum+"&function=fetchProdHist");
    http.onreadystatechange = processResponseFetchAnnualHist;
    http.send(null);
}

function processResponseFetchAnnualHist() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById("description"+invent).innerHTML = response;
    }
}
// ===end history fetch daily report


// ===start move inv count from location to location
function makeGetRequestMoveInv(locationName, prodNum, mCount, locaMove, userName) {
    invent = prodNum;
    //alert('prodNum: '+prodNum+' Count: '+mCount+' locaMove: '+locaMove);
    http.open('get', "InvUtil?locationName="+locationName+"&mCount="+mCount+"&locaMove="+locaMove+"&prodNum="+prodNum+"&function=moveInv&userName="+userName);
    http.onreadystatechange = processResponseMoveInv;
    http.send(null);
}

function processResponseMoveInv() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById("moveInv"+invent).innerHTML = response;
    }
}
// ===start move inv count from location to location

function processResponseSupplierSave() {
    if(http.readyState == 4){
        var response = http.responseText;
        document.getElementById("description"+invent).innerHTML = response;
    }
}
function makeGetRequestSupplierSave(
        supplierNum
        ,supplierName
        ,supplierContact
        ,supplierStreet
        ,supplierCity
        ,supplierState
        ,supplierPostalCode
        ,supplierCountry
        ,supplierPhone
        ,supplierFax
        ,supplierEmail
        ,supplierSite
        ,loc
        ) {


    invent = loc;
    http.open('get', "SupUtil?function=supDataSave&supplierNum="+supplierNum+"&supplierName="+supplierName+"&supplierContact="+supplierContact+"&supplierStreet="+supplierStreet+"&supplierCity="+supplierCity+"&supplierState="+supplierState+"&supplierPostalCode="+supplierPostalCode+"&supplierCountry="+supplierCountry+"&supplierPhone="+supplierPhone+"&supplierFax="+supplierFax+"&supplierEmail="+supplierEmail+"&supplierSite="+supplierSite+"&loc="+loc);

    http.onreadystatechange = processResponseSupplierSave;
    http.send(null);
}

