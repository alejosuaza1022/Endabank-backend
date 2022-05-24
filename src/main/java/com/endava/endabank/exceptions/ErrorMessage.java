package com.endava.endabank.exceptions;

import com.endava.endabank.constants.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private int statusCode;
    private String date;
    private String message;
    private String description;

    public ErrorMessage(int statusCode, Date date, String message, String description) {
        this.statusCode = statusCode;
        this.date = new SimpleDateFormat(Strings.DATE_PATTERN).format(date);
        this.message = message;
        this.description = description;
    }

}
