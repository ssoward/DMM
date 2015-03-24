package com.soward.ajax;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.soward.object.InstrumentInventory;
import com.soward.util.InstrumentInventoryUtil;

public class InstrumentAjax  extends HttpServlet { 

	private String outputStr="";
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
			HttpSession session = request.getSession();
			String location           = request.getParameter("location");
			String instrPid           = request.getParameter("instrPid");
			String funct           = request.getParameter("funct");




			response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
			response.setHeader("Pragma","no-cache"); //HTTP 1.0
			response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
			response.setContentType("text/html");

			PrintWriter out = response.getWriter();
			List<InstrumentInventory> inList = InstrumentInventoryUtil.searchForLocationInstrPid( location, instrPid );
			InstrumentInventory ii = new InstrumentInventory();
			ii.setCount("0");
			ii.setInstrPid(instrPid);
			ii.setLocation(location);
			if(inList!=null&&!inList.isEmpty()){
				//1 == add 0 == subtract
				ii = inList.get(0);
			}
			if(Integer.parseInt(funct)>0){
				ii.setCount((Integer.parseInt(ii.getCount())+1)+"");
			}else{
				ii.setCount((Integer.parseInt(ii.getCount())-1)+"");
			}
			InstrumentInventoryUtil.saveOrUpdate(ii);
			outputStr = "<center>"+ii.getCount()+"</center>";
			out.println(outputStr);
			out.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}













}
