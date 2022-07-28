package com.openclassrooms.realestatemanager.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class FileStoring {

    @SuppressWarnings("deprecation")
    public static Bitmap convertUriToBitmap(Uri uri, ContentResolver contentResolver) {
        Bitmap bitmap = null;
        if (uri != null) {
            try {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
                } else {
                    ImageDecoder.Source source = ImageDecoder.createSource(contentResolver, uri);
                    bitmap = ImageDecoder.decodeBitmap(source);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public static Uri createFile(Bitmap bitmap, File directory) {
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        File file = new File(directory, time + ".jpg");
        if (!file.exists()) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
                return Uri.fromFile(file);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
