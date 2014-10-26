package net.bryanbergen.Skillinux.Util;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarUtil {

    public static final String STANDARD = "yyyy-MM-dd hh:mm:ss";
    
    /**
     * Returns a formatted date string.
     * 
     * @param format The formatting for the returned string. e.g. "yyyy-MM-dd"
     * @param date The Calendar to be formatted.
     * @return The formatted date string if the Calendar is not null, null otherwise.
     */
    public static String getFormattedDate(String format, Calendar date) {
        if (date == null) {
            return "null";
        }
        SimpleDateFormat cal = new SimpleDateFormat(format);
        return cal.format(date.getTime());
    }
    
    /**
     * Returns an SQL Date wrapper for a calendar.
     * 
     * @param date Calendar to be wrapped.
     * @return SQL friendly date object.
     */
    public static Date getSQLDate(Calendar date) {
        return new Date(date.getTimeInMillis());
    }
    
    /**
     * Unwraps an SQL Date
     * 
     * @param date The <code>Date</code> to be unwrapped.
     * @return A Calendar object representing the unwrapped <code>Date</code> value.
     */
    public static Calendar getCalendarFromSQLDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        return cal;
    }
    
    /**
     * Converts a <code>String</code> to a usable <code>Calendar</code> object.
     * 
     * @param date The <code>String</code> to be parsed.
     * @param df <code>SimpleDateFormat</code> or other to parse <code>date</code>.
     * @return A usable <code>Calendar</code> object.
     */
    public static Calendar getDateFromString(String date, DateFormat df) {
        Calendar c = new GregorianCalendar();
        try {
            c.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c;
    }
}
