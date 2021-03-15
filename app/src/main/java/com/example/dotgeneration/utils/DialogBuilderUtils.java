package com.example.dotgeneration.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;


public class DialogBuilderUtils {
    static public ProgressDialog buildProgressDialog(Context ctx){
        ProgressDialog progressDialog =  ProgressDialog.show(ctx, "", "Loading...", true);
        TextView messageView = progressDialog.findViewById(android.R.id.message);
        messageView.setTextSize(100);
        return progressDialog;
    }
    static public AlertDialog getCommonDialog(Context ctx){
        AlertDialog.Builder builder  = new AlertDialog.Builder(ctx);
        builder.setTitle("Notice");
        builder.setPositiveButton("OK", null);
        return builder.create();
    }
    static public AlertDialog getEmptyInputDialogBuilder(Context ctx){
        AlertDialog alertDialog  = getCommonDialog(ctx);
        alertDialog.setMessage("NRIC Field should not be empty");
        return alertDialog;
    }
    static public AlertDialog getInvalidInputDialogBuilder(Context ctx){
        AlertDialog alertDialog  = getCommonDialog(ctx);
        alertDialog.setMessage("Your NRIC is invalid!");
        return alertDialog;
    }
}
