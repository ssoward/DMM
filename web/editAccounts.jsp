
<%@page import="com.soward.util.AccountTypesUtil"%>
<%@page import="com.soward.object.AccountType"%>
<%@page import="com.soward.util.Utils"%><%@include file="popupSetup.jsp"%>
<%@page import="com.soward.object.DBObj"%>
<SCRIPT LANGUAGE="JavaScript">                       
  function checkValid(){                                                                  
    if(document.editUserForm.userPass1.value!=document.editUserForm.userPass2.value||
       document.editUserForm.userPass1.value==''||
       document.editUserForm.userPass1.value.length<4||
       document.editUserForm.loginName.value==''||
       document.editUserForm.loginName.value.length<4){
      alert('Invalid Input. Possible problems:\n 1. No login name provided.\n 2. No password provided. \n 3. Passwords dont match or are less than 4 characters.');
    }
    else{                               
       document.editUserForm.submit();      
    }
  }                                                                                       
</script>



<%
  List<AccountType> accTListAll = AccountTypesUtil.getAccountTypes();
    List<AccountType> accTList = new ArrayList<AccountType>();
    List<AccountType> taxStatus = new ArrayList<AccountType>();
    for(AccountType al: accTListAll){
      try{
      Integer.parseInt(al.getTypeCode());
      accTList.add(al);
      }catch(Exception e){
      /*not numeric*/
        taxStatus.add(al);
      }
    }

	String updateAccount = request.getParameter("updateAccount");
	String message = request.getParameter("message");
	String editUserPid = request.getParameter("pid");
	DBObj dbobj = new DBObj();
	ArrayList<DBObj> acct = dbobj.getAccount(editUserPid);
	try {
		ArrayList<String> colValue = acct.get(0).getRow();
		if (colValue.size() > 16) {

			//message = (String)session.getAttribute("message");
%>
<br>
    &nbsp;&nbsp;&nbsp;<input style="margin-right:-20px" type="button" value="X" onClick="javascript:window.close();" class="btn">
<br>
<table width="754" border="1" align="center" cellpadding="5" cellspacing="0" class="simple">
<tr><td>
	<form name="editAcctForm" method="post" action="UpdateAccount">
	<table cellspacing="3" cellpadding="3" border="0" align=center>
    <tr><th colspan="5"><font size="4">Update Account</font></th></tr>

    <tr><td>Num         </td> <td><input size="35" type="text"     name="accountNum"      value="<%=Utils.pS(colValue.get(0) )%>" readonly>  </td><td>&nbsp;&nbsp;&nbsp;</td><td> Name        </td> <td><input size="35" type="text" name="accountName"       value="<%=Utils.pS(colValue.get(1) )%>" ></td></tr>
    <tr><td>Password    </td> <td><input size="35" type="password" name="accountPassword" value="<%=Utils.pS(colValue.get(2) )%>" readonly>  </td><td>&nbsp;&nbsp;&nbsp;</td><td> Contact     </td> <td><input size="35" type="text" name="accountContact"    value="<%=Utils.pS(colValue.get(3) )%>" ></td></tr>
    <tr><td>Email       </td> <td><input size="35" type="text"     name="accountEmail"    value="<%=Utils.pS(colValue.get(4) )%>" >          </td><td>&nbsp;&nbsp;&nbsp;</td><td> Phone1      </td> <td><input size="35" type="text" name="accountPhone1"     value="<%=Utils.pS(colValue.get(5) )%>" ></td></tr>
    <tr><td>Phone2      </td> <td><input size="35" type="text"     name="accountPhone2"   value="<%=Utils.pS(colValue.get(6) )%>" >          </td><td>&nbsp;&nbsp;&nbsp;</td><td> Fax         </td> <td><input size="35" type="text" name="accountFax"        value="<%=Utils.pS(colValue.get(7) )%>" ></td></tr>
    <tr><td>Street      </td> <td><input size="35" type="text"     name="accountStreet"   value="<%=Utils.pS(colValue.get(8) )%>" >          </td><td>&nbsp;&nbsp;&nbsp;</td><td> City        </td> <td><input size="35" type="text" name="accountCity"       value="<%=Utils.pS(colValue.get(9) )%>" ></td></tr>
    <tr><td>State       </td> <td><input size="35" type="text"     name="accountState"    value="<%=Utils.pS(colValue.get(10))%>" >          </td><td>&nbsp;&nbsp;&nbsp;</td><td> PostalCode  </td> <td><input size="35" type="text" name="accountPostalCode" value="<%=Utils.pS(colValue.get(11))%>" ></td></tr>
    <tr><td>Country     </td> <td><input size="35" type="text"     name="accountCountry"  value="<%=Utils.pS(colValue.get(12))%>" >          </td>
    <td>&nbsp;&nbsp;&nbsp;</td><td> Acct Type       </td> 
    <td align="left" colspan="1">
	<select name="accountType1">
		<option value=""></option>
		<%
		String accType1 = colValue.get(13); 
		  for ( AccountType accT : accTList ) {
               String selected = "";
               if(accType1!=null&&accType1.equals(accT.getTypeCode())){
               selected = "SELECTED";
             }
		%>
        	<option <%=selected%> value="<%=accT.getTypeCode()%>"><%=accT.getTypeDescription()%></option>
		<%
		  }
		%>
	</select>
    
    <tr>    
    <td>Tax Status       </td> 
    
    <td align="left" colspan="1">
	<select name="accountType2">
		<option value=""></option>
		<%
		String accType = colValue.get(14); 
		  for ( AccountType accT : taxStatus ) {
               String selected = "";
               if(accType!=null&&accType.equals(accT.getTypeCode())){
               selected = "SELECTED";
             }
		%>
        				<option <%=selected%> value="<%=accT.getTypeCode()%>"><%=accT.getTypeDescription()%></option>
		<%
		  }
		%>
	</select>
</td>
    
    <td>&nbsp;&nbsp;&nbsp;</td><td> OpenDate    </td> <td><input size=35 type="text" name="accountOpenDate" value="<%=Utils.pS(colValue.get(15))%>" ></td></tr>
    <tr><td>CloseDate   </td> <td><input size=35 type="text"     name="accountCloseDate" value="<%=Utils.pS(colValue.get(16))%>" >        </td><td>&nbsp;&nbsp;&nbsp;</td><td> Balance     </td> <td><input size=35 type="text" name="accountBalance" value="<%=Utils.pS(colValue.get(17))%>" readonly></td></tr>
    </td></tr>
    <tr align=right><td colspan="5">
 <input type="submit" value="Update" class="btn">
 </form>
 </td>
 <!--
 <td>
		  <form action="./viewAccountInvoices.jsp" name="getAccountNum">
			<input type=submit value="Invoice" class="btn"/>
		  <input type="hidden" name="pid" value="<%=editUserPid%>">
		  </form>
  </td>
  -->
</tr>
    </td></tr>
		<%
			} else {
		%>
		No data found for Account.
		<%
			}
			} catch (Exception e) {
				//no account found
		%><center> No account found for <%=" " + editUserPid%>, please consider searching for the account number.<%
			response
						.sendRedirect("searchAccounts.jsp?message=No account found for '"
								+ editUserPid
								+ "' please consider searching for the account number.");
			}
			if (message != null) {
		%>
		<td><%=message%></td>
		<%
			}
		%>
	</tr>
</table>
