package com.example.cem.hotel;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by cemg on 1/6/16.
 */
public class YesNoDialog extends DialogFragment {

    public YesNoDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();
        String title = "Delete";
        String message = "Are you sure?";

        return new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HotelDatabaseHelper hotelDB = new HotelDatabaseHelper(getActivity());
                        hotelDB.clearHotels();
                        Toast.makeText(getActivity(),"All hotels are deleted",Toast.LENGTH_SHORT).show();
                        hotelDB.close();
                        dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
    }
}