package com.soward.service;

import com.soward.db.MySQL;
import com.soward.object.Invoice;
import com.soward.util.InvoiceUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by ssoward on 1/1/15.
 */
public class SMAServiceImpl implements SMAService {
    public static Logger log = Logger.getLogger("SMAServiceImpl");

    public List<Invoice> syncInvoices() {
        return MagentoSyncServiceImp.syncMagento();
    }

    public List<Invoice> getSynced() {
        return InvoiceUtil.getSynced();
    }

    public String getOriginalInvoice(String invoiceId) {
        Map<String, Object> hash = new HashMap<String, Object>();
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from sm_sales_order where dmm_invoice_id = ?";
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            pstmt.setString(1, invoiceId);
            ResultSet rset = pstmt.executeQuery();
            while ( rset.next() ) {
                hash.put("id", rset.getString("id"));
                hash.put("order", rset.getString("sm_sales_order_entity_json"));
                hash.put("createdDt", rset.getString("created_dt"));
                hash.put("invoiceId", rset.getString("dmm_invoice_id"));
            }
            rset.close();
            con.close();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return hash!=null?(String)hash.get("order"):null;
    }
}
