package com.jawbr.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlaceNotFoundException extends RuntimeException {

    private String message;
    private String timeStamp;

    public PlaceNotFoundException(String message, long timeStamp) {
        this.message = message;
        this.timeStamp = formatTimestamp(timeStamp);
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
