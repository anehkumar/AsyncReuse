package com.sampleasyncreuse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckInternet {
    public boolean isConnected;

    public void isNetworkAvailable(final Context context, final Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if (!isConnected) {
            AlertDialog.Builder notification = new AlertDialog.Builder(context);
            notification.setMessage("No internet connection!");
            notification.setCancelable(true);
            notification.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    activity.finish();
                }
            });
            AlertDialog alt = notification.create();
            alt.show();
        }
    }
}
