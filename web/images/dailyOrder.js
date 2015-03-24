function storeShipNotes(){ 
  var note = document.getElementById('shipNts').value;
  var va = document.getElementById('shipNotes');
  va.value=note;
  var small = document.getElementById('smallNotes');
  small.innerHTML=note.substring(0,30);
}
function showPickUp(obj){ 
  var co=obj.options[obj.selectedIndex];
  var pick = document.getElementById('showPickUp')
  if(co.value=='pickUp'){
      pick.style.display="";
  }else{
      pick.style.display="none";
  }
}
function newSpecialOrder(){ 
  var page = document.getElementById("specialOrder");
  var tog = document.getElementById("newSpecialOrder");
//  if(page.style.display == 'none'){
//    page.style.display='block';
//    tog.innerHTML='Clear';
//  }else{
//    page.style.display='none';
//    tog.innerHTML='New Special Order';
  //}
    clearSpecialOrder();
}
function loadOrder(){
  startSpinner();
  var gto = document.getElementById("prevOrder");
  var chosenoption=gto.options[gto.selectedIndex];
  var aa = chosenoption.value;
 
  var page = document.getElementById("specialOrder");
  page.style.display='block';
  //need to change text to reflect an EDIT
  setTextForEdit(true);
  http.open('get', "SpecialOrderAjax?specialOrderId="+aa+"&function=recreateOrder");
  http.onreadystatechange = processResponseRecreateOrder;
  http.send(null);
}
function processResponseRecreateOrder() {
  if(http.readyState == 4){
    //try{
      var response = http.responseText;    
      var gto = JSON.parse(response, function (key,value){}() );
      if(gto){
    	  populateSpecialOrderDetails(gto);
      }
      var plist = gto.prodList;
      var currSup;
      var currSupIn = -1;
      var currTab;
      //supplier tabs
      var currSupArr = ['a','b','c','d','e','f','g','h','i','j'];
      if(plist){
        var innIx = 1;
        for(var x=0; x < gto.prodList.length; x++) {
            var ppp = gto.prodList[x];
            //populate supplier fields
            if(currSup != ppp.supNum){
            	currSupIn++;
            	currSup = ppp.supNum;
            	currTab = currSupArr[currSupIn];
            	//alert(qq);
            	innIx = 1;
            	document.getElementById('productSupplier00'+currTab).value=ppp.supNum;
            }
           	populateProdByLocation(ppp, innIx, currTab);
            innIx++;
            //alert('prodNum: '+ppp.prodNum+'\nsupNum: '+ppp.supNum);
        }
        updateAllTabsEstimatedTotals();
      }
    //}catch(e){
    //}
    document.getElementById("accountNum"        ).innerHTML=gto.accountNum       ;
    document.getElementById("hiddenAccountNum"  ).value=gto.accountNum       ;
    document.getElementById("accountName"       ).value=gto.accountName      ;
    document.getElementById("searchString"       ).value=gto.accountName      ;
    stopSpinner();
 
    //makeGetRequestFetchAccountForId(gto.accountNum);
  }
}
function setTextForEdit(t){
  document.getElementById('createOrderBtn').value = 'Save New';
  if(t){
	  document.getElementById('createOrderBtn').value = 'Save Edit';
  }
}
function clearSpecialOrder(){
	  document.getElementById('shipMethod'            ).value     = null;    
	  document.getElementById('droppedShip'           ).value     = null;    
	  document.getElementById('pickUp'                ).value     = null;    
	  document.getElementById('orderStatus'           ).value     = null;    
	  document.getElementById('orderBy'               ).value     = null;    
	  document.getElementById('droppedLocation'       ).value     = null;    
	  document.getElementById('shipNotes'             ).value     = null;    
	  document.getElementById('today'                 ).innerHTML = new Date();
	  document.getElementById('createdBy'             ).innerHTML = document.getElementById('createdByCurrentUser').value;    
	  document.getElementById('selectedAccountNum'    ).value     = null;    
	  document.getElementById('selectedAccountName'   ).value     = null;    
	  document.getElementById('selectedSpecialOrderId').value     = null;
	  document.getElementById('dateOne'               ).innerHTML = null;
	  document.getElementById('accountDisplay'        ).innerHTML = '';
	  document.getElementById('accountDisplay').style.display='none';
	  var currSupArr = ['a','b','c','d','e','f','g','h','i','j'];
		for(var x=0; x < currSupArr.length; x++) {
			var tab = currSupArr[x];
			resetProdDetails(tab);
		}
}
function updateAllTabsEstimatedTotals(){
	var pp = 'priceHide00';
	var tot = 0;
	var currSupArr = ['a','b','c','d','e','f','g','h','i','j'];
	for(var x=0; x < currSupArr.length; x++) {
		var tab = currSupArr[x];
		for(var i = 1; i < 10; i++){
			var m = pp+i+''+tab;
        	tot += document.getElementById(m).innerHTML;
		}
		document.getElementById('priceTotal00'+tab).innerHTML=tot;
		tot = 0;
	}
}


