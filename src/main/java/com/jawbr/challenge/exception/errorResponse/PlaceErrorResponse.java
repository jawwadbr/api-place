package com.jawbr.challenge.exception.errorResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlaceErrorResponse {

    private int status;
    private String message;
    private String timeStamp;

    public PlaceErrorResponse(int status, String message, long timeStamp) {
        this.status = status;
        this.message = message;
        this.timeStamp = formatTimestamp(timeStamp);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    private String formatTimestamp(long timeStamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(timeStamp), java.time.ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
        return dateTime.format(formatter);
    }
}
