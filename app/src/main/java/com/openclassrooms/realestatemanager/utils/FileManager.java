package com.openclassrooms.realestatemanager.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FileManager {

    //@SuppressWarnings("deprecation")
    public static Bitmap convertUriToBitmap(Uri uri, ContentResolver contentResolver) {
        Bitmap bitmap = null;
        if (uri != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri);
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

    public static File createImageFile(File storageDir) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    public static boolean deleteFileFromUri(Uri uri) {
        File file = new File(uri.getPath());
        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }
}
