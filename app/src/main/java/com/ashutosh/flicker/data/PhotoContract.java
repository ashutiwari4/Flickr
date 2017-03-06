package com.ashutosh.flicker.data;

import android.net.Uri;

/**
 * Created by Reetesh on 3/5/2017.
 */

public class PhotoContract {

    public static final String CONTENT_AUTHORITY = "com.ashutosh.flicker";
    public static final Uri BASE_URI = Uri.parse("content://com.ashutosh.flicker");

    interface PhotoColumns {
        /**
         * Type: INTEGER PRIMARY KEY AUTOINCREMENT
         */
        String _ID = "_id";
        /**
         * Type: TEXT
         */
        String PREDICATE = "title";
        /**
         * Type: TEXT NOT NULL
         */
        String PHOTO_URL = "photo_url";
    }


    public static class Photos implements PhotoColumns {
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.ashutosh.flicker.photos";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.ashutosh.flicker.photos";


        /**
         * Matches: /items/
         */
        public static Uri buildDirUri() {
            return BASE_URI.buildUpon().appendPath("photos").build();
        }

        /**
         * Matches: /items/[_id]/
         */
        public static Uri buildItemUri(long _id) {
            return BASE_URI.buildUpon().appendPath("photos").appendPath(Long.toString(_id)).build();
        }

        /**
         * Read item ID item detail URI.
         */
        public static long getItemId(Uri itemUri) {
            return Long.parseLong(itemUri.getPathSegments().get(1));
        }
    }

    private PhotoContract() {
    }
}
