package com.soward.service;

import com.soward.object.Invoice;

import java.util.List;
import java.util.Map;

/**
 * Created by ssoward on 1/1/15.
 */
public interface SMAService {

    List<Invoice> syncInvoices();

    List<Invoice> getSynced();

    String getOriginalInvoice(String invoiceId);
}
