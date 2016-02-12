package com.hugosama.samalinne.data;

import android.provider.BaseColumns;

/**
 * Created by hgomez on 11/02/2016.
 */
public class SamalinneContract {

    public static class MessagesEntry implements BaseColumns {
        public static String TABLE_NAME ="messages";

        public static String COLUMN_MESSAGE = "message";
        public static String COLUMN_DATE = "date";
    }
}
