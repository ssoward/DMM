<%@include file="jspsetup.jsp"%>


<%@page import="com.soward.object.Pages"%>


<script language="Javascript" type="text/javascript">
      function makeGetRequest(){  
	   document.newPage.method='post';
       document.newPage.action="PagesUtil?previewPage=true";      
       document.newPage.submit();      
    }
    function reset(input){
    document.newPage.action='editPages.jsp?page='+input;
	document.newPage.method='post';
	document.newPage.submit();
    }
    </script>





<%
String message = request.getParameter( "message" );
String pageStr = request.getParameter( "page" );
String previous = request.getParameter( "previous" );

Pages currPage = null;
String rightWindow = "";
String leftWindow = "";
if ( page != null ) {
    Pages pag = new Pages();
    currPage = pag.getPageByID( pageStr );
    if ( previous != null ) {
        rightWindow = currPage.getPage_preview();
        leftWindow = currPage.getPage_preview();
    } else {
        rightWindow = currPage.getPage();
        leftWindow = currPage.getPage();
    }
}

//message = (String)session.getAttribute("message");

%>
<h1>Edit Home Page</h1>
<p class="text">Use the code window below to edit the contents of the home page.</p>
      <div class="scrollTable">
 <table width="75%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
      <td>
      <input type="button" class="btn" onclick="makeGetRequest()" value="Preview"/>
      </td>
      </tr>
      <tr> 
      <td width="100%">
      <form name="newPage" method="post" action="PagesUtil">
      <input type="hidden" value="<%=pageStr %>" name="currPageId"/>
       <textarea id="newPageText" name="newPageText" cols=92 rows=10 wrap="off"><%=leftWindow %></textarea>
       </td></tr><tr>
      <td width="100%">
      <br />
      <br />
      <%= rightWindow%>
       </td>
      </tr>
      <tr>
      <td>
      <input type="submit" value="Save"/>
      <input type="button" class="btn" onclick="makeGetRequest()" value="Preview"/>
      <a href="./editPages.jsp?page=<%=pageStr %>">RESET</a>
      </td>
      </tr>
      </form>
</table>
      </div>
      <br>
<%@include file="bottomLayout.jsp"%>
