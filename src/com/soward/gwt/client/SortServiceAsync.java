package com.soward.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SortServiceAsync {
    void getMessage(String msg, AsyncCallback<String> async);
}
