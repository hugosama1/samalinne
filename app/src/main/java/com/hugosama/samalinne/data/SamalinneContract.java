package com.hugosama.samalinne.data;

import android.provider.BaseColumns;
import android.text.format.Time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by hgomez on 11/02/2016.
 */
public class SamalinneContract {

    public static long normalizeDate(long startDate) {
        // normalize the start date to the beginning of the (UTC) day
        GregorianCalendar date = (GregorianCalendar)GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        date.setTime(new Date(startDate));
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date.getTimeInMillis() -TimeZone.getTimeZone("GMT-7").getOffset(date.getTimeInMillis() );
    }

    public static class MessagesEntry implements BaseColumns {
        public static final String TABLE_NAME ="messages";

        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_DATE = "date";
    }
}
