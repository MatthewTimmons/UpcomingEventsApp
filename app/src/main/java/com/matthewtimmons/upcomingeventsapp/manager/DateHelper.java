package com.matthewtimmons.upcomingeventsapp.manager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    public static DateFormat dateFormatHumanReadable = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    public static DateFormat dateFormatDatabaseFriendly = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    public static SimpleDateFormat formatForJSONReturnValue = new SimpleDateFormat("dd MMM yyyy", Locale.US);

    public static String getHumanReadableFormat(String dateAsString) {
        try {
            Date date = dateFormatDatabaseFriendly.parse(dateAsString);
            return dateFormatHumanReadable.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "Error";
    }
}
