
<%@page import="com.soward.object.ProdOrderGto"%>
<%@page import="com.soward.util.SpecialOrderUtil"%>
<%@page import="com.soward.object.SpecialOrderGto"%><%@include file="jspsetup.jsp"%>
<%@page import="com.soward.object.User"%>
<%@page import="com.soward.object.DBObj"%>

<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.soward.object.Account"%>

<%@page import="com.soward.util.AccountUtil"%>
<%@page import="com.soward.object.Supplier"%>
<%@page import="com.soward.util.SupplierUtil"%>
<%@page import="org.json.*"%>
<%
  String sorder = request.getParameter("sorder");
  String details = request.getParameter("details");
  String function = request.getParameter("function");

  System.out.println("details  : " + details  );
  System.out.println("sorder  : " + sorder  );  
  
  
  SpecialOrderGto s = SpecialOrderUtil.createSpecialOrder(sorder, details);
  //System.out.println(s.toString());
  //for(ProdOrderGto gtto: s.getProdList()){
  //    System.out.println(gtto.toString());
  //}
  SpecialOrderUtil.saveUpdate(s);
%>
