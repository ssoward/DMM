package com.soward.ajax;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.soward.object.Supplier;
import com.soward.object.SupplierData;
import com.soward.util.SupplierUtil;

public class SupplierAjax  extends HttpServlet { 

	public void service(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {

		String supNum = request.getParameter("supNum");
		String loc = request.getParameter("loc");
		String val = request.getParameter("val");
		String supDataId = request.getParameter("supDataId");

		String  supplierNum       = request.getParameter("supplierNum");
		String  function       = request.getParameter("function");
		String  supplierName      = request.getParameter("supplierName");
		String  supplierContact   = request.getParameter("supplierContact");
		String  supplierStreet    = request.getParameter("supplierStreet");
		String  supplierCity      = request.getParameter("supplierCity");
		String  supplierState     = request.getParameter("supplierState");
		String  supplierPostalCode= request.getParameter("supplierPostalCode");
		String  supplierCountry   = request.getParameter("supplierCountry");
		String  supplierPhone     = request.getParameter("supplierPhone");
		String  supplierFax       = request.getParameter("supplierFax");
		String  supplierEmail     = request.getParameter("supplierEmail");
		String  supplierSite      = request.getParameter("supplierSite");

		SupplierData sData = new SupplierData();

		//do supplier save/edit
		if(!StringUtils.isBlank(loc)&&loc.equals("SUPPLIER")){
			Supplier sup = new Supplier();
			sup.setSupplierNum       (supplierNum         );
			sup.setSupplierName      (supplierName        );
			sup.setSupplierContact   (supplierContact     );
			sup.setSupplierStreet    (supplierStreet      );
			sup.setSupplierCity      (supplierCity        );
			sup.setSupplierState     (supplierState       );
			sup.setSupplierPostalCode(supplierPostalCode  );
			sup.setSupplierCountry   (supplierCountry     );
			sup.setSupplierPhone     (supplierPhone       );
			sup.setSupplierFax       (supplierFax         );
			sup.setSupplierEmail     (supplierEmail       );
			sup.setSupplierSite      (supplierSite        );
			boolean success = SupplierUtil.saveUpdateSupplier(sup);
			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			if(success){
				out.println("<font color=\"blue\">Successfully saved "+supplierName+"</font>");
			}
			else{
				out.println("<font color=\"red\">Failed to save "+supplierName+"</font>");
			}
			out.flush();
			out.close();
		}
		// do supplier data save
		else if(!StringUtils.isBlank(function)&&function.equals("saveSupplierData")){
			String savedLoc = "*";
			if(supNum!=null){
				try{
					sData.setSupplierNum(Integer.parseInt(supNum));
				}catch(Exception e){
					//not a valid value
				}
			}
			if(supDataId!=null){
				try{
					sData.setId(Integer.parseInt(supDataId));
				}catch(Exception e){
					//not a valid value
				}
			}
			
			if(!StringUtils.isBlank(loc)&&!StringUtils.isBlank(val)){
				try{
					if(loc.equals("MURRAY")){
						sData.setMurrayThr(Double.parseDouble(val));
						savedLoc = "Saved Murray";
					}
					if(loc.equals("LEHI")){
						sData.setLehiThr(Double.parseDouble(val));
						savedLoc = "Saved Lehi";
					}
					if(loc.equals("OREM")){
						sData.setOremThr(Double.parseDouble(val));
						savedLoc = "Saved Orem";
					}
					if(loc.equals("EContent")){
						sData.setEContent(val);
						savedLoc = "Saved Email Text";
					}
				}catch(Exception e){
					e.printStackTrace();
					//bad double value
				}
			}
			if(supNum!=null){
				SupplierUtil.saveUpdateSupplierData(sData);
			}

			response.setContentType("text/xml");
			PrintWriter out = response.getWriter();
			//out.println("<img src=\"images/blueCheck.gif\"/>");
			out.println("<font color=\"blue\">"+savedLoc+"</font>");
			out.flush();
			out.close();
		}

	}
}
