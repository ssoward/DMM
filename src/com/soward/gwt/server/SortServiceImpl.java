package com.soward.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.soward.gwt.client.SortService;

public class SortServiceImpl extends RemoteServiceServlet implements SortService {

    public String getMessage(String msg) {
        if(msg!=null&& msg.contains("Hello")){
            return "Client said: \"" + msg + "\"<br>Server answered: \"Hi\"";
        }

        String noMessage = "Please provide a message";
        if(msg!=null && msg.length()>0){
            noMessage = "<b>Client said: \"" + msg + "\"<br>Server answered: \"Hey Dude! I am your server.\"</b>";
        }
        return noMessage;
    }
}