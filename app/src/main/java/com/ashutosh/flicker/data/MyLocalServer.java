package com.ashutosh.flicker.data;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.ashutosh.flicker.modals.PhotoModal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Reetesh on 3/5/2017.
 */

public class MyLocalServer {

    private static final int ITEM_COUNT_IN_PAGE = 5;

    public MyLocalServer(Context context, List<PhotoModal> photoListModel) {
        updateDatabase(context, photoListModel);
    }


    public void updateDatabase(Context context, List<PhotoModal> photoListModel) {
        System.out.println("In my local server......................................");


        try {
            ArrayList<ContentProviderOperation> cpo = new ArrayList<ContentProviderOperation>();
            Uri dirUri = PhotoContract.Photos.buildDirUri();
            //TODO
            for (int i = 0; i < photoListModel.size(); i++) {
                ContentValues values = new ContentValues();
                PhotoModal photoModel = photoListModel.get(i);
                values.put(PhotoContract.Photos._ID, photoModel.getId());
                values.put(PhotoContract.Photos.PREDICATE, photoModel.getTitle());
                values.put(PhotoContract.Photos.PHOTO_URL,photoModel.getPhotoUrl());
                cpo.add(ContentProviderOperation.newInsert(dirUri).withValues(values).build());
            }

            context.getContentResolver().applyBatch(PhotoContract.CONTENT_AUTHORITY, cpo);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(getClass().getSimpleName(), "Error updating content.", e);
        }
    }
}
