package com.soward.ajax;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.soward.object.Account;
import com.soward.object.Product;
import com.soward.util.AccountUtil;
import com.soward.util.ProductUtils;

public class ProdAjax extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2123121175540820070L;
	public int count = 0;
	public void service( final HttpServletRequest request, HttpServletResponse response ) {
		try {                        
			count++;
			String productNum = (request.getParameter( "productNum" ));
			String supNum = (request.getParameter( "supNum" ));
			String acctId = (request.getParameter( "acctId" ));
			String itemStr = (request.getParameter( "itemStr" ));
			String locationId = (request.getParameter( "locationId" ));
			String function = request.getParameter( "function" )!=null?request.getParameter( "function" ):"";


			if(function.equals("prodSearchForStr")){
				if(!StringUtils.isBlank(itemStr)){
					List<Product> pL = ProductUtils.searchForItemNum(itemStr, supNum);
					if(pL!=null && !pL.isEmpty()){
						StringBuffer cList = new StringBuffer("<select  id=\"itemDescSel"+locationId+"\" onkeypress=\"runSubmitProd(event, '"+locationId+"');\" onchange=\"operProdSelected(this, '"+locationId+"')\" ondblclick=\"closeDropDownProds('"+locationId+"');\"size=\"5\" name=\"sval\" value=\"\">");
						for(Product aa: pL){
							cList.append( "<option value=\""+aa.getProductNum()+"\">"+
									aa.getProductCatalogNum()+"</option>");
						}
						cList.append( "</select>");
						response.getWriter().print( cList.toString() );
					}else{
					    String na = "<div id=\"status\">No Results</div>";
					    response.getWriter().print( "na" );
					}
					
				}
			}

		} catch ( Exception e ) {
			try {
				response.sendRedirect("SystemError.html");
			} catch ( IOException e1 ) {
				e1.printStackTrace();
			}
		}
	}
}
