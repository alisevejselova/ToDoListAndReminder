package com.example.ToDoListAndReminder;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

public class ShowDialog {

    public static void show(Context context) {
        AlertDialog.Builder alertbuilder = new AlertDialog.Builder(context);
        alertbuilder.setTitle("No Internet Connection ");
        alertbuilder.setMessage("You need to have Mobile Data or wifi to access your To Do list. Press OK to exit !");
        alertbuilder.setCancelable(false);

        alertbuilder.setNegativeButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        ((Activity) context).finish();  // se povikuva onDestroy
                    }
                });

        AlertDialog alertdialog = alertbuilder.create();
        alertdialog.show();
    }
}
