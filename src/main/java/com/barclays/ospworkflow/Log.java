package com.barclays.ospworkflow;

import java.util.Date;

public class Log {
    private String logString;
    private String sender;
    private Date date;

    public void setLogString(String token){
        this.logString = token;
    }
    public String getLogString() {
        return this.logString;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
