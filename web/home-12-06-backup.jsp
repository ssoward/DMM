<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.soward.object.Pages"%>
<%@page import="java.util.Date"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>DMM Dashboard</title>
<link href="css/stylesheet.css" rel="stylesheet" type="text/css" media="all" />
<META HTTP-EQUIV="imagetoolbar" CONTENT="no">
</head>
<%
   //String ipid =session.getId();
   

%>


<body>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr> <td align="center" valign="top">
    <table width="754" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr> <td align="left" valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> <td align="left" class="header-bg"></td>
          </tr>
          <tr> <td align="left" valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr> <td width="14" align="left" valign="top">&nbsp;</td>
                <td align="center" valign="middle" class="menu_box"><span class="blue_title">Welcome!</span></td>
                <td width="11" align="left" valign="top">&nbsp;</td>
                <td width="585" align="center" valign="top" class="menu_box">
                <table width="98%" border="0" cellspacing="0" cellpadding="0">
                  <tr> <td align="right" valign="middle"><a href="http://www.daymurraymusic.com" class="top_menu" style="border-bottom:1px solid #DA0008;">DMM</a> | <a href="index.jsp" class="top_menu">Home</a> | <a href="logout.jsp" class="top_menu">Logout</a> </td>
                  </tr> </table></td>
              </tr> </table></td>
          </tr> </table></td>
      </tr> <tr>
      
      
        <td align="left" valign="top">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> <td align="left" valign="top" class="body_padding">
            <table width="732" border="0" cellspacing="0" cellpadding="0">
              <tr> <td align="left" valign="top">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr> <td width="154" align="left" valign="top">
                    <table width="154" border="0" cellspacing="0" cellpadding="0">
                      <tr> <td align="center" valign="top">
                        <table width="139" border="0" cellpadding="0" cellspacing="0" class="menu_table">
                        </table></td>
                      </tr>
                      <tr> <td align="left" valign="top">&nbsp;</td> </tr>
                      <tr> <td align="left" valign="top">
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                            <td align="center" valign="top" class="search-bg">
                              <table width="108" border="0" cellspacing="0" cellpadding="4">
                              <tr> <td height="16" align="left" valign="top">&nbsp;</td> </tr>
                              <tr> <td align="center" valign="top" class="blue_title">Admin Login</td> </tr>
                              <%@include file="login.jsp"%>
                            </table>
                            </td>
                          </tr>
                        </table></td>
                      </tr>
                      <tr>
                        <td align="left" valign="top">&nbsp;</td>
                      </tr>
                      <tr>
                        <td align="left" valign="top">
                        <table width="154" border="0" cellspacing="0" cellpadding="0">
                          <tr>
                          </tr>
                        </table></td>
                      </tr>
                      <tr>
                        <td align="left" valign="top">&nbsp;</td>
                      </tr>
                    </table></td>
                    <td width="18" align="left" valign="top">&nbsp;</td>
                    <td align="left" valign="top">
                    <table width="560" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td align="left" valign="top">
                        
                        <%
                        Pages pag = new Pages();
                        Pages currPage = pag.getPageByID( "1" );
                        %>
                        <%=currPage.getPage() %>
                        
                        
                      </tr>
                      <tr>
                        <td align="left" valign="top">&nbsp;</td>
                      </tr>
                    </table>
                    </td>
                  </tr>
                </table></td>
              </tr>
              <tr>
                <td align="left" valign="top">
                <table width="732" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td align="left" valign="top" class="order-box">
                </td>
                  </tr>
                </table></td>
              </tr>
              <tr>
                <td align="left" valign="top">&nbsp;</td>
              </tr>
            </table></td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td align="left" valign="top">
        <table width="732" border="0" cellpadding="0" cellspacing="0" class="footer-margin">
          <tr>
            <td colspan="3" align="left" valign="top" class="footer_box">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <tr>
                <td style="color:#515E85; white-space:pre;" height="34" align="center" valign="middle">
                <!--
                <a 
                href="index.jsp"  class="footer-link"></a> | <a 
                href="index.jsp"  class="footer-link"></a> | <a 
                href="index.jsp"  class="footer-link"></a> | <a 
                href="index.jsp"  class="footer-link"></a> | <a 
                href="index.jsp"  class="footer-link"></a> | <a 
                href="index.jsp"  class="footer-link"></a> | <a 
                href="index.jsp"  class="footer-link"></a> | <a 
                href="index.jsp"  class="footer-link"></a> | <a 
                href="index.jsp"  class="footer-link"></a> | <a 
                href="index.jsp"  class="footer-link"></a>
 

                -->
                </td>
              </tr>
            </table>
            </td>
          </tr>
        </table></td>
      </tr>
    </table></td>
  </tr>
</table>
</body>
</html>
