package com.example.qrcodescanner.db;

import android.provider.BaseColumns;

public class EventContract {

    public static class CatEntry implements BaseColumns {
        public static final String TABLE_EVENT = "table_ev";
        // tasks Table Columns names
        public static final String EVENT_ID = "evid";
        public static final String EVENT_NAME = "event";
        public static final String EVENT_START_DATE = "startdate";
        public static final String EVENT_END_DATE = "enddate";
    }
}
