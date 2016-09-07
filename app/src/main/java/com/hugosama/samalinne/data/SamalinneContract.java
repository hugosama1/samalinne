package com.hugosama.samalinne.data;

import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by hgomez on 11/02/2016.
 */
public class SamalinneContract {

    public static long normalizeDate(long startDate) {
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate,time.gmtoff);
        return time.setJulianDay(julianDay);
    }

    public static class MessagesEntry implements BaseColumns {
        public static final String TABLE_NAME ="messages";

        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_DATE = "date";
    }
}
