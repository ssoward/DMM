
var addTemplateSteps = ["selectTemplateType", "selectTemplateFile", "selectSegmentFiles", "addTemplates"];
var steps ;
var currentStep = 0;
var reviewAbled = false;
var templateHistorys;

/**
 * select action from list
 * @param actionSel
 * @return
 */
function actionSelect(actionSel){
	var action = actionSel.options[actionSel.selectedIndex].value;
	if ( action == "addTemplate" ){
		startAddingTemplate();
	}else if ( action == "viewTemplateUploadingHistroy" ){
		showHistroyView();
	}else if ( action == "uploadTemplateForms" ){
		showUploadForms();
	}else if ( action == "viewUploadedForms"){
		showViewUploadForms();
	}
	actionSel.options[0].selected = true;
}

/**
 * 
 * @param element
 * @return
 */
function setReviewTemplate(element){
	var reviewTemplatesEles = document.getElementsByName(element.name);
	for(var i=0; i < reviewTemplatesEles.length; i++ )
		reviewTemplatesEles[i].checked = element.checked;
	reviewAbled = element.checked;
}

function reviewTemplateOpen(templateName){
	 if ( !reviewAbled )
		 return;
	 var page = '/applyonlinechg/app?page=livecycleTemplateReview&service=page&template=' + templateName;
	var winprops = 'height=600,width=720,scrollbars=yes,status=no,menubar=yes,toolbar=no';
	 var reviewWin = window.open( page, "popup", winprops );
	reviewWin.focus();
}

function collapseDojoTree(treeId){
	try {
	//dijit.byId(treeId).rootNode.collapse();
	dijit.byId(treeId)._onBlur = null;
	} catch (e){ }
}
function startAddingTemplate(){
	steps = new Array();
	   for(var i =0; i < addTemplateSteps.length; i++ )
	       steps[i] = addTemplateSteps[i];
	   
   	currentStep = 0;
    dijit.byId(steps[currentStep]).show();
	document.getElementById("actionHidden").value="SaddTemplate";
	document.getElementById("segmentHidden").value="X";
}
       
function finishAddingTemplate(){
   	dijit.byId(steps[currentStep]).hide();
    currentStep = 0;
    templateToCrate = null;
}
   
function cancelAddingTemplate(){
   	dijit.byId(steps[currentStep]).hide();
    currentStep = 0;
    templateToCrate = null;
}
   
function navigateStep(stepToMove){
   	dijit.byId(steps[currentStep]).hide();
   	currentStep = currentStep + stepToMove;	
    dijit.byId(steps[currentStep]).show();
}


function syncPeerHiddenValue(element){
	var id = element.id + "_Hidden";
	var peer = document.getElementById(id);
	alert(id + " " + peer)
	if ( peer )
		peer.value = "S" + element.value;
	alert(peer.value);
}

function validateTemplateTypeAndNav(stepToMove){
	var newTypeSource = document.getElementById('newTemplateTypeSource');
	var error = "";
	if ( newTypeSource.checked ){
		var documentType = document.getElementById('documentType');
		
		if ( documentType.options[documentType.selectedIndex].text.replace(/ /g, "").length == 0 )
			error = "You must select document type.\n";
		var typeName = document.getElementById('templateTypeName').value;
		if ( typeName.replace(/ /g, "").length == 0  )
			error += "You must input template type name."
	}else{
		var exsitTemplateType = document.getElementById('existTemplateType');
		var text = exsitTemplateType.options[exsitTemplateType.selectedIndex].text;
		
		if ( text.replace(/ /g, "").length == 0  )
			error = "You must select template type.";
	}
	
	if ( error.length > 0  ){
		alert(error);
		return;
	}
	navigateStep(stepToMove);
}

function validateSegmentsAndNav(stepToMove){
	var segments = document.getElementById("segmentHidden").value;
	if (!segments || segments.replace(/ /g, "").length == 0 || segments == 'X') {
		var result = confirm("There is no segment to be added. Do you want to continue?");
		if ( !result )
			return;
	}
	navigateStep(stepToMove);
}

function validateTemplateFileAndNav(stepToMove){
	var templateFile = document.getElementById('templateAdobeFile').value;
	if (!templateFile || templateFile.replace(/ /g, "").length == 0 || templateFile == 'X') {
		alert('You must select one file for template.');
		return;
	}
	navigateStep(stepToMove);
}

function addSegment(){
	var segmentFile = document.getElementById("segmentFile").value; 
	if (!segmentFile || segmentFile.replace(/ /g, "").length == 0 || segmentFile == 'X') {
		alert('You must select one file to add segment.');
		return;
	}
	//clean it
	document.getElementById("segmentFile").value = '';
	
	var segmentName = window.prompt("Enter Segment Name");
	if ( !segmentName )
		return;
	
	var fileName = segmentFile;
	var addedSegmentDiv = document.getElementById("addedSegments");
	var segmentHidden = document.getElementById("segmentHidden");
	var addedSegments = segmentHidden.value;
	if ( !addedSegments || addedSegments == 'X')
		addedSegments = "";
	else 
		addedSegments = addedSegments + "%;%";
	addedSegments +=  "name=" + segmentName + ",file=" + fileName ;	
	
	segmentHidden.value =  addedSegments;
	addedSegmentDiv.innerHTML = buildSegmentHtml(addedSegments, false);
}

function deleteSegment(index){
	if ( index < 0 )
		return;
	var addedSegmentDiv = document.getElementById("addedSegments");
	var segmentHidden = document.getElementById("segmentHidden");
	var addedSegments = segmentHidden.value;
	if ( !addedSegments )
		return;
	var segments = addedSegments.split('%;%');
	if ( index >= segments.length )
		return;
	var newAddedSegments = "";
	for( var i =0 ; i < segments.length; i++ ){
		if ( i == index )
			continue;
		if ( newAddedSegments != "" )
			newAddedSegments += "%;%" + segments[i] ;
		else
			newAddedSegments += segments[i];
	}
	segmentHidden.value =  newAddedSegments;
	addedSegmentDiv.innerHTML = buildSegmentHtml(newAddedSegments, false);
}

function buildSegmentHtml(addedSegments, noDeleteBtn){
	var html = "";
	if ( !addedSegments || addedSegments == "" ||  addedSegments == "X")
		return html;
	var segments = addedSegments.split('%;%');
	for(var i =0; i < segments.length; i++ ){	
		if ( i % 2 == 0 )
			html += "<div style='width:100%;background-color:#ffffff'>" ;
		else
			html += "<div style='width:100%;background-color:#c0c0c0'>";
		
		if ( !noDeleteBtn)
			html += "<input type='button' value='Delete' onclick='deleteSegment(" + i +");' />" ;
		html += "&nbsp; " + segments[i];
		html += "</div>";
	}
	return html;
}

function buildAddingTemplateDetailHtml(){
	var html = "";
	var exsitTemplateType = document.getElementById('existTemplateType');
	var documentType = document.getElementById('documentType');	
	var newTypeSource = document.getElementById('newTemplateTypeSource');
	
	if ( !newTypeSource.checked ){
		html += "<div> <h3>Exsiting Template Type:</h3<br/>" + exsitTemplateType.options[exsitTemplateType.selectedIndex].text + "</div>";
	} else {
		html += "<div> <h3>Document Type:</h3<br/>" + documentType.options[documentType.selectedIndex].text+ "</div>";
		html += "<div> <h3>Template Type Name:</h3<br/>" + document.getElementById('templateTypeName').value + "</div>";
		html += "<div> <h3>Notification Email:</h3<br/>" + document.getElementById('notificationEmail').value + "</div>";
	}
	
	html += "<div> <h3>Template Adobe File Name:</h3<br/>" + document.getElementById('templateAdobeFile').value + "</div>";
	html += "<div> <h3>Added Segments</h3<br/>" + buildSegmentHtml(document.getElementById("segmentHidden").value,true);
	
	document.getElementById('templateDetails').innerHTML = html;
}
   
//Template Uploading history functions
function showHistroyView(){
	dijit.byId("historyUploading").show();
	document.getElementById("actionHidden").value="StemplateHistory";
}

function cancelTemplateHistory(){
	dijit.byId("historyUploading").hide();
	templateHistorys = null;
	document.getElementById("histroyResultDiv").innerHTML="";
}

function cleanHistoryView(){
	document.getElementById("createFromHistoryBtn").style.display="none";
}

function searchHistory(bugId, username){
    var temp = 'commentThreadList'+bugId;
    var tempTwo = 'bugComment'+bugId;
	var xhrArgs = { 
        // The following URL must match that used to test the server.
        url: "", 
        handleAs: "text",

        timeout: 5000, // Time in milliseconds

        // The LOAD function will be called on a successful response.
        load: function(response, ioArgs) { 
        //try{
		 
         //var  mapFromServlet = dojo.fromJson(response);
          //for(var i = 0; i< mapFromServlet.length; i++){
          	
          	//temp = temp + mapFromServlet[i]+ '<br>' ;
          //}
          //dojo.byId('commentThreadList'+bugId).innerHTML = temp;
          //} catch(e){
          //	alert(e.message);
          //}
          dojo.byId(temp).innerHTML = response;
          return response; 
        },

        // The ERROR function will be called in an error case.
        error: function(response, ioArgs) { 
          alert("HTTP status code: ", ioArgs.xhr.status); 
          return response; 
          }
        };
	xhrArgs.url = "AjaxTheadServlet?&bugId=" + bugId  +"&bugCommentReporter="+username;
	if ( document.getElementById(tempTwo).value.replace(/ /g, "").length > 0 )
		xhrArgs.url += "&bugComment=" + document.getElementById(tempTwo).value;
	
	dojo.xhrGet(xhrArgs);

}

function showSearchResult(historys){
	templateHistorys = dojo.fromJson(historys);
	 
	var div = document.getElementById("histroyResultDiv");
	
	if ( templateHistorys.code != 0 ){
		div.innerHTML="<h2>" + templateHistorys.message + "</h2>";
		document.getElementById("createFromHistoryBtn").style.display="none";
		return;
	}
	
	var html = [];
	
	html.push("<div id='searchResults' style='width:100%'><table width='95%' align='center' >")
	html.push("<tr>")
		 html.push("<th/><th width='100' align='left'>Type Name</th>");
		 html.push("<th width='50' align='left'>Document Type</th><th width='20' align='left'>Exsiting Template</th>");
		 html.push("<th width='200' align='left'>File Name</th><th width='100' align='left'>Email</th>");
		 html.push("<th width='200' align='left'>Segments</th><th align='left'>Upload Date</th>");
	html.push("</tr>")
	for(var i = 0 ; i < templateHistorys.result.length; i++){
		var line =  "<tr>\n";
			line += "	<td align='left' valign='middle'><input type='checkbox' name='historySearchResult' value='" + i + "' /></td>\n";
			line += "   <td align='left' valign='middle'>" + templateHistorys.result[i].templateTypeName +"</td>\n";
			line += "   <td align='left' valign='middle'>" + templateHistorys.result[i].templateDocumentType +"</td>\n";
			line += "   <td align='center' valign='middle'>" +  templateHistorys.result[i].exsitTemplateType.toUpperCase().charAt(0) +"</td>\n";
			line += "   <td align='left' valign='middle'>" + templateHistorys.result[i].templateAdobeFileName +"</td>\n";
			if ( templateHistorys.result[i].notificationEmail )
				line += "   <td align='left' valign='middle'>" + templateHistorys.result[i].notificationEmail +"</td>\n";
			else
				line += "   <td align='left' valign='middle'></td>\n";
			if ( templateHistorys.result[i].segmentsString )
				line += "   <td align='left' valign='middle'>" + templateHistorys.result[i].segmentsString +"</td>\n";
			else
				line += "   <td align='left' valign='middle'></td>\n";
			line += "     <td align='left' valign='middle'>" + templateHistorys.result[i].uploadDate +"</td>\n"
			line += "</tr>";
		html.push(line);
	}
	
	html.push("</table></div>");
	
	div.innerHTML = html.join("");
	document.getElementById("createFromHistoryBtn").style.display="inline";
	
}


function createTemplatesFromHistory(){
	var searchResults = document.getElementsByName("historySearchResult");
	if ( !searchResults || !templateHistorys){
		alert("No History!")
		return;
	}
	var checkedItems = new Array();
	var index = 0 ;
	for(var i = 0; i < searchResults.length; i++){
		var item = searchResults[i];
		if ( !item || !item.checked )
			continue;
		checkedItems[index] =  templateHistorys.result[item.value];
		index++;		
	}
	
	if ( index == 0 )
		alert("No history selected.");
	else{
		document.getElementById("actionHidden").value="SaddTemplateFromHistory";
		var wrapper = {items:checkedItems};
		document.getElementById("selectedHistoryHidden").value= dojo.toJson(checkedItems);
		document.getElementById("updateOrAddTemplateSubmit").click();
	}
}

//forms uploading function
function showUploadForms(){
	dijit.byId("formsUploading").show();
}

function cancelUploadForms(){
	dijit.byId("formsUploading").hide();
}

//forms uploading function
function showViewUploadForms(){
	dijit.byId("formsView").show();
}

function cancelViewUploadForms(){
	dijit.byId("formsView").hide();
}




