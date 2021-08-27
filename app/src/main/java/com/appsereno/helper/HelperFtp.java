package com.appsereno.helper;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.OpenableColumns;

public class HelperFtp {

    public static  String getFileName(Activity activity, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor =activity.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static String getImageRealPathFromURI(Activity activity,Uri uri) {

        String result;
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
            cursor.close();
            return result;
        }
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        result = cursor.getString(idx);
        cursor.close();
        return result;
    }

    public static String getVideoRealPathFromURI(Activity activity,Uri uri) {

        String result;
        Cursor cursor = activity.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
            cursor.close();
            return result;
        }
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
        result = cursor.getString(idx);
        cursor.close();
        return result;
    }

    public static String getMediaRealPathFromURI(Activity activity,Uri uri){
        String result;
        result=getImageRealPathFromURI(activity,uri);
        if(result!=null&&result.trim().length()>0){
            return result;
        }else{
            return getVideoRealPathFromURI(activity,uri);
        }
    }
}
