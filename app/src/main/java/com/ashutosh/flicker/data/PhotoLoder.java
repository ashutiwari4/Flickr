package com.ashutosh.flicker.data;

import android.content.Context;
import android.net.Uri;
import android.content.CursorLoader;

/**
 * Created by Reetesh on 3/5/2017.
 */

public class PhotoLoder extends CursorLoader {
    public static int count = 10;

    public static PhotoLoder newAllArticlesInstance(Context context) {
        return new PhotoLoder(context, PhotoContract.Photos.buildDirUri());
    }

    public static PhotoLoder newInstanceForItemId(Context context, long itemId) {
        return new PhotoLoder(context, PhotoContract.Photos.buildItemUri(itemId));
    }



    private PhotoLoder(Context context, Uri uri) {
        super(context, uri, Query.PROJECTION, null, null,null);
    }

    public interface Query {
        String[] PROJECTION = {
                PhotoContract.PhotoColumns._ID,
                PhotoContract.PhotoColumns.PREDICATE,
                PhotoContract.PhotoColumns.PHOTO_URL,

        };

        int _ID = 0;
        int PREDICATE = 1;

    }
}
