
function startSpinner(){
    startSpinner(0);
}

function startSpinner(b){
    var dd = document.getElementById("ajaxLoader");
    dd.className = '';
    if(b){
      document.getElementById("dialogBackground").className = 'dialogBackgroundShow';
    }
}

function stopSpinner(){
    stopSpinner(0);
}
function stopSpinner(b){
    var dd = document.getElementById("ajaxLoader");
    dd.className = 'hideAjaxLoader';
    if(b){
      document.getElementById("dialogBackground").className = '';
    }
}

function sortSelect(selElem) {
    var tmpAry = new Array();
    for (var i=0;i<selElem.options.length;i++) {
        tmpAry[i] = new Array();
        tmpAry[i][0] = selElem.options[i].text;
        tmpAry[i][1] = selElem.options[i].value;
    }
    tmpAry.sort();
    while (selElem.options.length > 0) {
        selElem.options[0] = null;
    }
    for (var i=0;i<tmpAry.length;i++) {
        var op = new Option(tmpAry[i][0], tmpAry[i][1]);
        selElem.options[i] = op;
    }
    return;
}


