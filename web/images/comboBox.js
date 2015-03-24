    function jsfPopupPage( page, w, h ) {
      var winl = (screen.width - w) / 2;
      var wint = (screen.height - h) / 2;
      winprops = 'height=' + h + ',width=' + w + ',top=' + wint + ',left=' + winl +
        ',scrollbars=yes,status=no,menubar=yes,toolbar=no';
      popup = window.open( page, "popup", winprops );
      popup.focus();
      return false;
    }




function addDists(theSel){
  var selLength = theSel.length;
  var i;
  var getAll = "";
  var dn = document.cdist.distNum.value;
  for(i=selLength-1; i>=0; i--){
    var text = theSel.options[i].name;
    var value= theSel.options[i].value;
    getAll = getAll + i+"update" + "="+ value+"&";
  }
  window.location = "./createDistricts.jsp?distNum="+dn+"&cmpLen="+selLength+"&"+getAll+"newCom=true"
}
function addAssignments(theSel){
  var selLength = theSel.length;
  var i;
  var getAll = "";
  for(i=selLength-1; i>=0; i--){
    var text = theSel.options[i].name;
    var value= theSel.options[i].value;
    getAll = getAll + i+"update" + "="+ value+"&";
  }
  window.location = "./createAssignments.jsp?cmpLen="+selLength+"&"+getAll+"newCom=true"
}
function addArrayBun(theSel){
  var selLength = theSel.length;
  var i;
  var getAll = "";
  for(i=selLength-1; i>=0; i--){
    var text = theSel.options[i].name;
    var value= theSel.options[i].value;
    getAll = getAll + i+"update" + "="+ value+"&";
  }
  window.location = "./createComps.jsp?cmpLen="+selLength+"&"+getAll+"newCom=true"
}


function addOption(theSel, theText, theValue){
  var newOpt = new Option(theText, theValue);
  var selLength = theSel.length;
  theSel.options[selLength] = newOpt;
}

function deleteOption(theSel, theIndex){ 
  var selLength = theSel.length;
  if(selLength>0){
    theSel.options[theIndex] = null;
  }
}

function moveOptions(theSelFrom, theSelTo){
  var selLength = theSelFrom.length;
  var selectedText = new Array();
  var selectedValues = new Array();
  var selectedCount = 0;
  var i;

  // Find the selected Options in reverse order
  // and delete them from the 'from' Select.
  for(i=selLength-1; i>=0; i--){
    if(theSelFrom.options[i].selected){
      var testText =  theSelFrom.options[i].text;
      selectedText[selectedCount] = theSelFrom.options[i].text;
      selectedValues[selectedCount] = theSelFrom.options[i].value;
      deleteOption(theSelFrom, i);
      selectedCount++;
    }
  }

  // Add the selected text/values in reverse order.
  // This will add the Options to the 'to' Select
  // in the same order as they were in the 'from' Select.
  for(i=selectedCount-1; i>=0; i--){
    addOption(theSelTo, selectedText[i], selectedValues[i]);
  }
}

