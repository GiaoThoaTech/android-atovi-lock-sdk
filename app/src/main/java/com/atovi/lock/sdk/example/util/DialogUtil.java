package com.atovi.lock.sdk.example.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.StringRes;

import com.atovi.lock.sdk.example.R;

public class DialogUtil {
    public static void showDiaLogLocationPermission(Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.permission_location)
                .setMessage(R.string.permission_location_message)
                .setCancelable(false)
                .setNegativeButton("Cancel", (dialog, which) -> ((Activity) context).finish())
                .setPositiveButton("Settings", (dialog, which) -> {
                    dialog.dismiss();
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);
                }).show();
    }

    public static void showDialog(Context context, @StringRes int messageID) {
        new AlertDialog.Builder(context)
                .setMessage(messageID)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                }).show();
    }

    public static void showDialog(Context context, @StringRes int titleID, @StringRes int messageID) {
        new AlertDialog.Builder(context)
                .setTitle(titleID)
                .setMessage(messageID)
                .setCancelable(false)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                }).show();
    }
}
