package com.example.eduar.brexpress.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.TypedValue;

import com.example.eduar.brexpress.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by eduar on 04/05/2018.
 */

public class Utils {

    private static SharedPreferences.Editor mEditor;

    public static boolean checkIfHasCamera(Context context) {
        PackageManager pm = context.getPackageManager();

        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }
        return false;
    }

    /**
     * Converting dp to pixel
     */
    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static Bitmap convertBitmapToUri(Context context, Uri uri) {
        try {
            InputStream imageStream = context.getContentResolver().openInputStream(uri);
            Bitmap image = BitmapFactory.decodeStream(imageStream);
            return image;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void convertImageToBase64(final Context context, final Bitmap bitmap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();

                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

                Intent intent = new Intent(Constants.CONVERT_IMAGE_TO_BASE64_RECEIVER);
                intent.putExtra(Constants.BASE64_IMAGE, encodedImage);
                context.sendBroadcast(intent);
            }
        }).start();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getStatusText(Context context, int status) {
        switch (status) {
            //Waiting Payment
            case 0:
                return context.getResources().getString(R.string.waiting_payment);
            //Payment Accepted
            case 1:
                return context.getResources().getString(R.string.payment_accept);
            //Shipping Status
            case 2:
                return context.getResources().getString(R.string.shipping_status);
            //Delivered
            case 3:
                return context.getResources().getString(R.string.delivered);
            default:
                return null;
        }
    }

    public static String setFormatDate(long date) {
        Calendar purchaseDate = Calendar.getInstance();
        purchaseDate.setTimeInMillis(date);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        System.out.println(format.format(purchaseDate.getTime()));

        return format.format(purchaseDate.getTime());
    }

    public static void createDialog(final Context context, int title, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Constants.CONFIRMED_ACTION);
                context.sendBroadcast(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent intent = new Intent(Constants.DIALOG_ACTION_CLICK);
//                intent.putExtra(Constants.DENIED_ACTION, false);
//                context.sendBroadcast(intent);
            }
        });

        builder.setTitle(title);
        builder.setMessage(message);

        builder.create();

        builder.show();
    }

    public static boolean getUserType(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        boolean isAdming = prefs.getBoolean(Constants.USER_TYPE, false);
        return isAdming;
    }

    public static String getUserName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        String userName = prefs.getString(Constants.USER_NAME, null);
        return userName;
    }

    public static int getUserId(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        int userId = prefs.getInt(Constants.USER_ID, -1);
        return userId;
    }

    public static void saveUser(Context context, int id, String name, boolean isAdmin) {
        mEditor = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).edit();
        mEditor.putString(Constants.USER_NAME, name);
        mEditor.putInt(Constants.USER_ID, id);
        mEditor.putBoolean(Constants.USER_TYPE, isAdmin);
        mEditor.apply();
    }

    public static void clearUserData(Context context) {
        mEditor = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE).edit();
        mEditor.clear();
        mEditor.apply();
    }
}
