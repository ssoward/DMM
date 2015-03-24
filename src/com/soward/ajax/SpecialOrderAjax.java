package com.soward.ajax;


import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.soward.object.ProdOrderGto;
import com.soward.object.SpecialOrderGto;
import com.soward.util.SpecialOrderUtil;
public class SpecialOrderAjax extends HttpServlet{
    public void service( final HttpServletRequest request, HttpServletResponse response ) {
        try {   
            String function = request.getParameter( "function" )!=null?request.getParameter( "function" ):"";
            String id = request.getParameter( "specialOrderId" );
            if(function.equals("recreateOrder") && id != null){
                Long idL = Long.parseLong(id);
                SpecialOrderGto gto = SpecialOrderUtil.fetchForId( idL );
                List<ProdOrderGto> pList =  SpecialOrderUtil.fetchSPOForId( idL );
                if(pList!=null && !pList.isEmpty()){
                    gto.setProdList( pList );
                }
                System.out.println(new JSONObject(gto).toString());
                response.getWriter().print( new JSONObject(gto).toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
