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
    var ccc = '';
    function makeGetRequest(parameters) {
          http.open('get', "AjaxBills"+parameters);
          http.onreadystatechange = processResponse;
          http.send(null);
    }

    function processResponse() {
        if(http.readyState == 4){
            var response = http.responseText;
            document.getElementById("newBill").innerHTML = response;
        }
    }
    function makeGetRequestStatsSum() {
          http.open('get', "AjaxBillSum");
          http.onreadystatechange = processResponseStatsSum;
          http.send(null);
    }

    function processResponseStatsSum() {
        if(http.readyState == 4){
            var response = http.responseText;
            document.getElementById("viewBills").innerHTML = response;
        }
    }
    
    //collect up all the form data and pass it to servlet.
    function get(obj) {
      var getstr = "?";
      for (i=0; i<obj.elements.length; i++) {
      //alert(obj.elements[i].tagName+' '+obj.elements[i].type);
         if (obj.elements[i].tagName == "INPUT") {
            if (obj.elements[i].type == "text") {
               getstr += obj.elements[i].name + "=" + obj.elements[i].value + "&";
                
            }
            if (obj.elements[i].type == "checkbox") {
               if (obj.elements[i].checked) {
                  getstr += obj.elements[i].name + "=" + obj.elements[i].value + "&";
               } else {
                  getstr += obj.elements[i].name + "=&";
               }
            }
            if (obj.elements[i].type == "radio") {
               if (obj.elements[i].checked) {
                  getstr += obj.elements[i].name + "=" + obj.elements[i].value + "&";
               }
            }
         }   
         if (obj.elements[i].tagName == "SELECT") {
            var sel = obj.elements[i];
            getstr += sel.name + "=" + sel.options[sel.selectedIndex].value + "&";
         }
         
      }
      makeGetRequest(getstr);
   }
   

    
