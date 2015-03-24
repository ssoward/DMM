<%@include file="jspsetup.jsp"%>
<%@page import="com.soward.util.TrafficUtil"%>
<%@page import="com.soward.object.Traffic"%>
<SCRIPT LANGUAGE="JavaScript" SRC="css/_CalendarPopup.js"></SCRIPT>
<SCRIPT LANGUAGE="JavaScript"> var cal = new CalendarPopup(); </SCRIPT>
<%
String message = request.getParameter("message");
String dateOne = request.getParameter("dateOne");
String dateTwo = request.getParameter("dateTwo");
String dateThree = request.getParameter("dateThree");

ArrayList<Traffic> max = new ArrayList<Traffic>();
if(dateOne!=null&&dateOne.length()>4){
    System.out.println("1 date");
    TrafficUtil tu = new TrafficUtil();
	max =  tu.getIPS(dateOne);

    
}
else if(dateTwo!=null&&dateTwo.length()>4&&
        dateThree!=null&&dateThree.length()>4){
    System.out.println("2 dates");
    TrafficUtil tu = new TrafficUtil();
	max =  tu.getIPS(dateTwo, dateThree);
    
}
TimeZone tz = TimeZone.getTimeZone("America/Salt_Lake");
Calendar c = Calendar.getInstance();

int year = c.get(Calendar.YEAR);
int mon  = c.get(Calendar.MONTH);
int day  = c.get(Calendar.DAY_OF_MONTH);
//System.out.println(year+" / "+(mon+1)+" / "+day);

String date = year+"-";
if(mon<10){date+="0"+(mon+1);}else{date+=(mon+1);}
if(day<10){date+="-0"+day;}else{date+="-"+day;}

//message = (String)session.getAttribute("message");

%>
<h1> Web Traffic </h1>
<table width="754" border="0" align="center" cellpadding="0" cellspacing="0">
     <tr>
          <td align="" valign="top"><table border=0 width=790 valign="top">
                    <tr>
                         <td valign=top width="260"><table border=0 width=260>
                                   <tr>
                                        <td colspan=5><a href="http://soward.mine.nu:8080/DayMM">Previous Gui</a> <br>
                                             Sort by clicking on header (Date, IP, Hits).
                                             <FORM name="myForm" ACTION="/DMM/viewTraffic.jsp">
                                                  <tr>
                                                            <TD align=left>Get info for: </td>
                                                            <td><A HREF="#" onClick="cal.select(document.forms['<%="myForm"%>'].<%="dateOne"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>"> <img width="25" src="images/calendar.jpg"/></A>&nbsp;</TD>
                                                            <TD align=left><input size=10 type="text" value="<%= date%>" name="dateOne" onfocus="alert('Click on Calender Icon on the left.');document.myForm.repsOne.focus();" >
                                                            </td>
                                                       </tr>
                                                  <tr>
                                                            <td colspan=2>&nbsp;</td>
                                                            <TD align=left><input type=image alt="Enter" src="images/go-bt.gif"  width="83" height="22" border="0" />
                                                            </td>
                                             </form>
                                             <FORM name="myFormTwo" ACTION="/DMM/viewTraffic.jsp">
                                                  <tr>
                                                            <TD align=left>Get total hits from: </td>
                                                            <td><A HREF="#" onClick="cal.select(document.forms['<%="myFormTwo"%>'].<%="dateTwo"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>"> <img width="25" src="images/calendar.jpg"/></A>&nbsp;</TD>
                                                            <TD align=left><input size=10 type="text" value="" name="dateTwo" onfocus="alert('Click on Calender Icon on the left.');document.myFormTwo.repsOne.focus();" >
                                                            </td>
                                                       </tr>
                                                  <tr>
                                                            <TD align=left> to: </td>
                                                            <td><A HREF="#" onClick="cal.select(document.forms['<%="myFormTwo"%>'].<%="dateThree"%>,'<%="anchor"%>','yyyy-MM-dd'); return false;" 
            	NAME="<%="anchor"%>" ID="<%="anchor"%>"> <img width="25" src="images/calendar.jpg"/></A>&nbsp;</TD>
                                                            <TD align=left><input size=10 type="text" value="" name="dateThree" onfocus="alert('Click on Calender Icon on the left.');document.myFormTwo.repsOne.focus();" >
                                                            </td>
                                                       </tr>
                                                  <tr>
                                                            <td colspan=2>&nbsp;</td>
                                                            <TD align=left><input type=image alt="Enter" src="images/go-bt.gif"  width="83" height="22" border="0" />
                                                            </td>
                                                       </tr>
                                             </form>
                                             <!-- 777 -->
                                        </td>
                                   </tr>
                              </table></td>
                         <td width="490"><div class="scrollTable" style="width:490px">
                                   <table id="viewTraffic" class="sortable, common" 
			cellpadding="0" cellspacing="0" style="width:490px;">
                                        <tr>
                                             <th width=260>Date</th>
                                             <th width=1002>IP</th>
                                             <th width="117">Hits</th>
                                        </tr>
                                        <%if(!max.isEmpty()){ %>
                                        <%for (Traffic mm: max){ %>
                                        <tr width="480">
                                             <td>&nbsp; <%=mm.getTime() %></td>
                                             <td>&nbsp; <%=mm.getIP()   %></td>
                                             <td>&nbsp; <%=mm.getHits() %></td>
                                        </tr>
                                        <%} %>
                                        <%} %>
                                   </table>
                              </div>
          </table></td>
          <% if(message!=null){%>
          <td class="message"><%=message %></td>
          <%}%>
     </tr>
</table>
<%@include file="bottomLayout.jsp"%>