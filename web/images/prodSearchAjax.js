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
var locationId ='';

function fetchItemNumbers(ojb, tabNum){
  startSpinner();
  locationId = tabNum;
  var chosenoption=ojb.options[ojb.selectedIndex];
  var aa = chosenoption.text;
  document.getElementById(tabNum).innerHTML='<font size=\'1\'>'+chosenoption.text.substring(0,10)+'</font>';
  if(aa.length<1){
    caseTab(tabNum);
  }
  http.open('get', "AccountSearchAjax?tabNum="+tabNum+"&supNum="+chosenoption.value+"&function=fetchSupplierItemList");
  http.onreadystatechange = processResponseItemDropDown;
  http.send(null);
}
function processResponseItemDropDown() {
  if(http.readyState == 4){
    resetProdDetails(locationId);
    var rsponse = http.responseText;
    var ct = 0;
    var jon = JSON.parse(rsponse);
    for(var q in jon){
      ct++;
    }
    //check if list is less than 100
    //if(ct<100){ //uncomment if you want to use dropdowns only
    if(false){
      showHideTextAndDropdown(false)
      for(i=1;i<10;i++){
        var response = http.responseText;
        var json = JSON.parse(response);
        var s = i +''+locationId;
        var sel = document.getElementById('itemSelec00'+s);
        sel.options.length=0;
        var all = '';
        var cc = 1;
        sel.options[0] = new Option(' -Select Item','');
        for (x in json){
          if(json.hasOwnProperty(x)){
              //all += x+' --> '+json[x]+'\n';
              sel.options[cc] = new Option(json[x],x);
              cc++;
          }
        }
        sortSelect(sel);
        //document.getElementById('itemSelection00'+s).innerHTML = response;
      }
    }else{
      showHideTextAndDropdown(true)
    }
  stopSpinner();
  }
}
function showHideTextAndDropdown(showText){
	if(showText){
		for(i=1;i<10;i++){
	        var s = i +''+locationId;
	        var eee = document.getElementById('itemSelec00'+s);
	        var ddd = document.getElementById('itemTextSelection00'+s);
	        eee.style.display = "none";
	        ddd.style.display = "block";
	      } 
	}else{
		for(i=1;i<10;i++){
			var s = i +''+locationId;
			var eee = document.getElementById('itemSelec00'+s);
			var ddd = document.getElementById('itemTextSelection00'+s);
			eee.style.display = "block";
			ddd.style.display = "none";
		} 
	}
}
function qtySelect(ojb, rowCount, tabNum){
  locationId = '00'+rowCount+''+tabNum;
    var p = "priceHide"+locationId;
    var base = document.getElementById(p).value;
    var co=ojb.options[ojb.selectedIndex];
    var i = co.value;
    var c = "price"+locationId;
    var price = (i*base).toFixed(2);
    document.getElementById(c).innerHTML='$'+price;
    updateTotalPrice(rowCount, tabNum);

}
function updateTotalPrice(rowCount, tabNum){
  var tot = 0;
		for(i=1;i<10;i++){
			var s = i +''+tabNum;
			var eee = document.getElementById('priceHide00'+s).value;
			var ddd = document.getElementById('quanity00'+s).value;
      if(eee && ddd){
        tot = (eee * ddd) +tot;
      }
    }
    tot = tot.toFixed(2);
	document.getElementById('priceTotal00'+tabNum).innerHTML = '$'+tot;
}

function operItemSelected(ojb, rowCount, tabNum){
	locationId = '00'+rowCount+''+tabNum;
	var chosenoption=ojb.options[ojb.selectedIndex];
	operItemSelectedNum(chosenoption.value, rowCount,tabNum); 
}
function operItemSelectedNum(pNum, rowCount, tabNum){
  startSpinner();
  locationId = '00'+rowCount+''+tabNum;
  //var chosenoption=ojb.options[ojb.selectedIndex];
  http.open('get', "AccountSearchAjax?productNum="+pNum+"&function=fetchProductForItem");
  http.onreadystatechange = processResponseOper;
  http.send(null);
}
function processResponseOper() {
  if(http.readyState == 4){
    var response = http.responseText;    
    prod = JSON.parse(response, function (key,value){}() );
    populateProd(prod, false);
    stopSpinner();
  }
}
function populateSpecialOrderDetails(gto){
  document.getElementById('shipMethod'        ).value      = gto.shipMethod      ;    
  document.getElementById('droppedShip'       ).value      = gto.droppedShipped  ;    
  document.getElementById('pickUp'            ).value      = gto.pickUp          ;    
  document.getElementById('orderStatus'       ).value      = gto.orderStatus     ;    
  document.getElementById('orderBy'           ).value      = gto.orderMethod     ;    
  document.getElementById('droppedLocation'   ).value      = gto.droppedLocation ;    
  document.getElementById('shipNotes'         ).value      = (escape(gto.shipNote)).replace(/%20/g, ' ');    
  document.getElementById('today'             ).innerHTML  = gto.orderDate       ;
  document.getElementById('createdBy'         ).innerHTML  = gto.createdBy       ;    
  document.getElementById('selectedAccountNum').value      = gto.accountNum      ;    
  document.getElementById('selectedAccountName').value     = gto.accountName     ;    
  document.getElementById('selectedSpecialOrderId').value  = gto.id              ;    
  document.getElementById('dateOne'           ).innerHTML  = gto.deliveryDate    ;    
}
function populateProdByLocation(prod, rowCount, tabNum){
	locationId = '00'+rowCount+''+tabNum;
	populateProd(prod, true);
}
function populateProd(prod, setVars){
  	var vv = "voicing"+locationId;
    var tt = "title"+locationId;
    var cc = "price"+locationId;
    var pp = "priceHide"+locationId;
    var aa = "prodNum"+locationId;
    var ff = "verified"+locationId;
    var dd = "itemTextSelection"+locationId;
    var zz = "prodOrderId"+locationId;
    try{document.getElementById(vv).value=prod.category         ;}catch(e){}
    try{document.getElementById(tt).value=prod.productName      ;}catch(e){}
    try{document.getElementById(cc).innerHTML='0.00'            ;}catch(e){}
    try{document.getElementById(pp).value=prod.productCost1     ;}catch(e){}
    try{document.getElementById(aa).value=prod.productNum       ;}catch(e){}
    try{document.getElementById(zz).value=prod.id               ;}catch(e){}
    try{document.getElementById(ff).checked=true                ;}catch(e){}
    try{document.getElementById(dd).value=prod.productCatalogNum;}catch(e){}

    if(setVars){ 
      var c = "quanity"+locationId;
      var j = "itemEmailed"+locationId;
      var h = "itemStatus"+locationId;
      try{document.getElementById(c).value=prod.quantity; }catch(e){}
      try{document.getElementById(j).checked=('true'==prod.emailed);}catch(e){}
      try{document.getElementById(ff).checked=('true'==prod.verified);}catch(e){}
      try{document.getElementById(h).value=prod.status;}catch(e){}
      try{
        var price = (prod.quantity*prod.productCost1).toFixed(2);
        document.getElementById(cc).innerHTML=('$'+price);
      }catch(e){}
    }
}
function resetProdDetails(tab){
    for(i=1;i<10;i++){
        var s = i +''+tab;

        var a = "voicing00"+s;
        var b = "title00"+s;
        var c = "quanity00"+s;
        var d = "verified00"+s;
        var e = "itemTextSelection00"+s;
        var f = "priceHide00"+s; 
        var g = "price00"+s;
        var h = "itemStatus00"+s;
        var j = "itemEmailed00"+s;
        var k = "itemDesc00"+s;
        
        document.getElementById(a).value='';
        document.getElementById(b).value='';
        document.getElementById(c).value='0';
        document.getElementById(c).selectedindex=0;
        document.getElementById(d).checked=false;
        document.getElementById(e).value='';
        document.getElementById(f).value='';
        document.getElementById(g).innerHTML='';
        document.getElementById(h).value='';
        document.getElementById(h).selectedindex=0;
        document.getElementById(j).checked=false;
        document.getElementById(k).innerHTML='';
    } 
    document.getElementById('priceTotal00'+tab).innerHTML='';
}
function makeGetRequestItem(someVarOne, rowCount, tabNum){
  locationId = '00'+rowCount+''+tabNum;
  var publisher = document.getElementById('productSupplier00'+tabNum).value;
  http.open('get', "ProdAjax?locationId="+locationId+"&itemStr="+someVarOne+"&function=prodSearchForStr&supNum="+publisher);
  http.onreadystatechange = processResponseItemType;
  http.send(null);
}
function processResponseItemType() {
  if(http.readyState == 4){
    var response = http.responseText;
    $("#itemDesc"+locationId).fadeTo(1, 1);
    if(!response || response.length<1){
      document.getElementById("itemDesc"+locationId).innerHTML = 'No results';
      $("#itemDesc"+locationId).fadeTo(3000, 0,function(){
          document.getElementById("itemDesc"+locationId).innerHTML = null;
          });
    }else{
      document.getElementById("itemDesc"+locationId).innerHTML = response;
    }
  }
}
function runSubmitProd(e, locId){
 if (window.event) { e = window.event; }

  ///enter	 
  if (e.keyCode == 13){
	  closeDropDownProds(locId);
  }
}
function operProdSelected(selected, locId){
  var chosenoption=selected.options[selected.selectedIndex];
  var d = "itemTextSelection"+locId;
  document.getElementById(d).value = chosenoption.text;
  //document.getElementById("selectedAccountNum").value = chosenoption.value;
}
function closeDropDownProds(locId){
  startSpinner();
  var b = 'itemDescSel'+locId;
	var sel = document.getElementById(b);
  var chosenoption=sel.options[sel.selectedIndex];
  //alert('text: '+chosenoption.text+' VALUE: '+chosenoption.value);
  document.getElementById('itemDesc'+locId).innerHTML = '';

  locationId = locId;
  http.open('get', "AccountSearchAjax?productNum="+chosenoption.value+"&function=fetchProductForItem");
  http.onreadystatechange = processResponseOper;
  http.send(null);
}
function createOrder(){
  var c2 = document.getElementById('shipMethod');
  var c1 = document.getElementById('droppedShip');
  var c3 = document.getElementById('pickUp');
  var c4 = document.getElementById('orderStatus');
  var ch1=c1.options[c1.selectedIndex];
  var ch2=c2.options[c2.selectedIndex];
  var ch3=c3.options[c3.selectedIndex];
  var ch4=c4.options[c4.selectedIndex];
  var a = escape(document.getElementById('orderBy'           ).value);
  var b = escape(document.getElementById('droppedLocation'   ).value);
  var d = escape(document.getElementById('shipNotes'         ).value);
  var e = escape(document.getElementById('today'             ).innerHTML);
  var f = escape(document.getElementById('createdBy'         ).innerHTML);
  var g = escape(document.getElementById('selectedAccountNum').value);
  var h = escape(document.getElementById('dateOne'           ).innerHTML);
  var an = escape(document.getElementById('selectedAccountName').value);
  var so = escape(document.getElementById('selectedSpecialOrderId').value);

  var supTabs = '{';
  var tabs = new Array('a','b','c','d','e');
  for(var tt in tabs){
    var tb = (tabs[tt]);
    var pub = document.getElementById('productSupplier00'+tb).value;
    if(pub || 0 != pub.length){
      supTabs+='supplier00'+tb+':'+pub+',';
    }
      //get all the tabs
      for(i=1;i<10;i++){
        var key1 = 'quanity00'+i+tb;
        var key2 = 'itemTextSelection00'+i+tb;//itemNumber
        var key3 = 'verified00'+i+tb;
        var key4 = 'itemEmailed00'+i+tb;
        var key5 = 'prodNum00'+i+tb;
        var key6 = 'itemStatus00'+i+tb;
        var key7 = 'prodOrderId00'+i+tb;

        var val1 = document.getElementById(key1);
        var val2 = document.getElementById(key2);
        var val3 = document.getElementById(key3);
        var val4 = document.getElementById(key4);
        var val5 = document.getElementById(key5);
        var val6 = document.getElementById(key6);
        var val7 = document.getElementById(key7);

        //var prodNum=val2.value;
        
        //this is used when the user cannot edit itemNumber
        //if(val2.style.display=='none'){
        //  val2 = document.getElementById('itemSelec00'+i+tb);
        //  var chosenoption=val2.options[val2.selectedIndex];
        //  prodNum=chosenoption.value;
        //}

        if(clen(val2)){// || 0 != prodNum.length){
          supTabs+=key1+':'+val1.value+',';
          supTabs+=key2+':'+val2.value+',';
          supTabs+=key3+':'+val3.checked+',';
          supTabs+=key4+':'+val4.checked+',';
          supTabs+=key5+':'+val5.value+',';
          supTabs+=key6+':'+val6.value+',';
          supTabs+=key7+':'+val7.value+',';
        }
      }
     //supTabs +='],';
    
  }
  supTabs+='}';
     
//  alert('This page is currently under construction. \nThis is what your order will look like:'
//      +'\norderBy: '+a
//      +'\ndroppedLocation: '+b
//      +'\nshipMethod: '+ch2.value
//      +'\ndroppedShip: '+ch1.value
//      +'\npickUp: '+ch3.value
//      +'\norderStatus: '+ch4.value
//      +'\nshipNotes: '+d
//      +'\ntoday: '+e
//      +'\ncreatedBy: '+f
//      +'\naccountNum: '+g
//      +'\naccountName:'+an
//      +'\ndelievery: '+h
//      +'\nfirstTab: '+supTabs
//  );
  var details = '{';
  if(a        )details += 'orderby:'         +a        ;
  if(b        )details += ',droppedLocation:'+b        ;   
  if(ch2.value)details += ',shipMethod:'     +escape(ch2.value);
  if(ch1.value)details += ',droppedShip:'    +escape(ch1.value);
  if(d        )details += ',shipNotes:\''    +escape(d) +'\'' ;          
  if(e        )details += ',today:\''        +e      +'\'' ;              
  if(f        )details += ',createdBy:'      +f        ;          
  if(g        )details += ',accountNum:'     +g        ;         
  if(h        )details += ',delievery:'      +h        ;          
  if(an       )details += ',accountName:'    +an       ;          
  if(so       )details += ',orderId:'        +so       ;          
  if(ch4.value)details += ',orderStatus:'    +escape(ch4.value);          
  if(ch3.value)details += ',pickUp:'         +escape(ch3.value);          
  details += '}';
  
  var string  = "ordersService.jsp?details="+details+"&sorder="+supTabs+"&function=procSOrder";
  startSpinner();
  http.open('get', string);
  http.onreadystatechange = processResponseSOrder;
  http.send(null);
}
function processResponseSOrder() {
  if(http.readyState == 4){
    var response = http.responseText;    
    stopSpinner();
    location.href = "./dailyOrderUtil.jsp?message=successfulSave";
  }
}
function caseTab(tabNum){
  var bb = '';
    switch(tabNum){
      case 'a': bb='First  '; break;
      case 'b': bb='Second '; break;
      case 'c': bb='Third  '; break;
      case 'd': bb='Forth  '; break;
      case 'e': bb='Fifth  '; break;
      case 'f': bb='Sixth  '; break;
      case 'g': bb='Seventh'; break;
      case 'h': bb='Eighth '; break;
      case 'i': bb='Ninth  '; break;
      case 'j': bb='Tenth  '; break;
    }
  document.getElementById(tabNum).innerHTML=bb;

   
}
function clen(str){
  if(str && str.value && 0 != str.value.length){
    return true;
  }else{
    return false;
  }
}
