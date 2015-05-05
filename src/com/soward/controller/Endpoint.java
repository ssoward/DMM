package com.soward.controller;

/**
 * Created by ssoward on 1/1/15.
 */
public enum Endpoint {
    PROD_CAT_GET,
    PROD_SEARCH,
    COUNT_PUT,
    PROD_SOLD_HISTORY,

    WEIGHT_PUT,
    PROD_WEIGHT,

    SYNC_INVOICES,
    SYNCED_INVOICES,
    ORIGINAL_FOR_INV,

    ACCT_SEARCH,
    ACCT_MERGE,

    INV_CACHE_MOVE_PUT,
    INV_CACHE_DONE_PUT,
    INV_CACHE_GET,
    PROD_COUNT_GET,
    HOLD_BIN_GET,
    PROD_SOLD_FOR_INVOICES,
    PO_PUT,
    SALES_GET,
    TRANS_GET,
    TRANS_PUT,
    TRANS_QTY_PUT,
    INV_DELETE,
    TRANS_DELETE,
    PO_GET;
}
