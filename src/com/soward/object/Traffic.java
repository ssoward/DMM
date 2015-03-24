package com.soward.object;

public class Traffic {
    public String time;

    public String info;

    public String session;

    public String ip;

    public String id;

    public String hits;

    public String getHits() {
        return this.hits;
    }

    public String getTime() {
        return this.time;
    }

    public String getInfo() {
        return this.info;
    }

    public String getSession() {
        return this.session;
    }

    public String getIP() {
        return this.ip;
    }

    public String getID() {
        return this.id;
    }

    public void setTime( String string ) {
        this.time = string;
    }

    public void setInfo( String string ) {
        this.info = string;
    }

    public void setSession( String string ) {
        this.session = string;
    }

    public void setIP( String string ) {
        this.ip = string;
    }

    public void setID( String string ) {
        this.id = string;
    }

    public void setHits( String string ) {
        this.hits = string;

    }

}
