package com.soward.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.soward.db.MySQL;
import com.soward.object.Events;

public class EventsUtil {


    public static Events getEventsForEventCode(String code){
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Events where eventCode = "+code;
        Events event = new Events();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            if ( rset.next() ) {
                event.setEventCode( rset.getString("eventCode" ));
                event.setEventName( rset.getString("eventName"));
                event.setEventKeyWords( rset.getString("eventKeyWords"));
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return event;
    }
    public static List<Events> getAllEvents(){
        Connection con = null;
        MySQL sdb = new MySQL();
        String sql = "select * from Events order by eventName";
        List<Events> eventList = new ArrayList<Events>();
        try {
            con = sdb.getConn();
            PreparedStatement pstmt = null;
            pstmt = con.prepareStatement( sql );
            ResultSet rset = pstmt.executeQuery();
            while ( rset.next() ) {
                Events event = new Events();
                event.setEventCode( rset.getString("eventCode" ));
                event.setEventName( rset.getString("eventName"));
                event.setEventKeyWords( rset.getString("eventKeyWords"));
                eventList.add( event );
            }
            pstmt.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return eventList;
    }
    public static void main (String args[]){
        List<Events> eList = EventsUtil.getAllEvents();
        for(Events event: eList){
            System.out.println(event.getEventName());
        }
    }
}
