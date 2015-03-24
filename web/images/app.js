function toggle_search(divId){
	var ele = $(divId);
   var pos=  ele.style.right == "40px"  ?-234 : 40;
   ele.morph('right:'+pos+'px',{duration:0.8});
}

function stopEventBubble(e){
   var event = e || window.event;
   //for all browser except for IE
   if (event.stopPropagation) {  
      event.stopPropagation();   
     } 
   else {   
      event.cancelBubble = true;
   }
}

function caculateAmount(rateField, unitField, amountField, amountDisplayField){
	
	$(amountField).value = formatCurrency($(rateField).value * $(unitField).value);
	$(rateField).value = formatCurrency($(rateField).value);
	$(amountDisplayField).value= formatCurrency( $(amountField).value ) ;
	if ( $(amountField).type && $(amountField).type.toLowerCase() == 'hidden' )
		$(amountField).value = 'S' + $(amountField).value; // for tapestry
}

function caculateTotal(totalAmountField, amountsContainer, tagName, amountFieldAttr){
	var amount = 0;
	var eles = $(amountsContainer).getElementsByTagName(tagName);
	for(var i = 0; i < eles.length; i++) {
		var ele = eles[i];
			if ( ele.getAttribute && ele.getAttribute(amountFieldAttr) )
				amount += parseFloat(ele.value);
	}
	$(totalAmountField).value = formatCurrency ( amount );
}

function validateRequired(container, attr){
	var noError = true;
	//for all input text
	var eles = $(container).getElementsByTagName("input");
	// for input
	if ( eles ){
	  for(var i =0; i < eles.length; i++){
		  var ele = eles[i];
		if ( ele.getAttribute && ele.getAttribute(attr) == "true" ){
			if ( !ele.value || ele.value.blank()){
				noError = false;
				ele.style.backgroundColor='yellow';
			}else
				ele.style.backgroundColor='';
				
		}//if ( ele.getAttribute && ele.getAttribute("required") == "true" )
	  }
	}
	//for select
	eles = $(container).getElementsByTagName("select");
	if ( eles ){
	  for(var i =0; i < eles.length; i++){
		  var ele = eles[i];
		if ( ele.getAttribute && ele.getAttribute(attr) == "true" ){
			
			var value = ele.options[ele.selectedIndex].value;
			if ( !value || value.blank() ){
				noError = false;
				ele.style.backgroundColor='yellow';
			}	else
				ele.style.backgroundColor='';			
				
		}//if ( ele.getAttribute && ele.getAttribute("required") == "true" )
	  }
	}
	if ( !noError )
		alert("Please fill all required fields");
	return noError;
}

function formatCurrency(amount){
	return parseFloat(amount).toFixed(2);
}

function updateObjectAccount(frObjectAccts, frSelect, transType, transObjectAcctId, serviceObjectAcctId ){
	var name = frSelect.options[frSelect.selectedIndex].value;
	
	var accts = frObjectAccts[name];
   
	if ( !accts )
		return;
	if ( $(transObjectAcctId) && accts[transType])
		$(transObjectAcctId).value = accts[transType];
	
	if ( $(serviceObjectAcctId) && accts["service"])
		$(serviceObjectAcctId).value = accts["service"];
}


