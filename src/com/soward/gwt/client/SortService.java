package com.soward.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("SortService")
public interface SortService extends RemoteService {
    // Sample interface method of remote interface
    String getMessage(String msg);

    /**
     * Utility/Convenience class.
     * Use SortService.App.getInstance() to access static instance of SortServiceAsync
     */
    public static class App {
        private static SortServiceAsync ourInstance = GWT.create(SortService.class);

        public static synchronized SortServiceAsync getInstance() {
            return ourInstance;
        }
    }
}
