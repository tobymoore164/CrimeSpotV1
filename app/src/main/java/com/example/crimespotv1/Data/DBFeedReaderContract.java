package com.example.crimespotv1.Data;

import android.provider.BaseColumns;

public final class DBFeedReaderContract {

    // Make private so it can't be instantiated
    private DBFeedReaderContract() {}

    // Table Contents
    public static class FeedEntry implements BaseColumns {

        public static final String TABLE_NAME = "tbl_saved_crimes_test";
        public static final String COLUMN_NAME_CATEGORY = "category";
        public static final String COLUMN_NAME_LOCATION_TYPE = "location_type";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_STREET_ID = "street_id";
        public static final String COLUMN_NAME_STREET_NAME = "street_name";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_CONTEXT = "context";
        public static final String COLUMN_NAME_OUTCOME_STATUS = "outcome_status";
        public static final String COLUMN_NAME_PERSISTENT_ID = "persistent_id";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_LOCATION_SUBTYPE = "location_subtype";
        public static final String COLUMN_NAME_MONTH = "month";

        // Create a table (sql) was private
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY, " +
                        FeedEntry.COLUMN_NAME_CATEGORY + " TEXT, " +
                        FeedEntry.COLUMN_NAME_LOCATION_TYPE + " TEXT, " +
                        FeedEntry.COLUMN_NAME_LATITUDE + " TEXT, " +
                        FeedEntry.COLUMN_NAME_STREET_ID + " TEXT, " +
                        FeedEntry.COLUMN_NAME_STREET_NAME + " TEXT, " +
                        FeedEntry.COLUMN_NAME_LONGITUDE + " TEXT, " +
                        FeedEntry.COLUMN_NAME_CONTEXT + " TEXT, " +
                        FeedEntry.COLUMN_NAME_OUTCOME_STATUS + " TEXT, " +
                        FeedEntry.COLUMN_NAME_PERSISTENT_ID + " TEXT, " +
                        FeedEntry.COLUMN_NAME_ID + " TEXT, " +
                        FeedEntry.COLUMN_NAME_LOCATION_SUBTYPE + " TEXT, " +
                        FeedEntry.COLUMN_NAME_MONTH + " TEXT )";



        // Delete a table (sql) was private
        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXIST " + FeedEntry.TABLE_NAME;

    }
}
