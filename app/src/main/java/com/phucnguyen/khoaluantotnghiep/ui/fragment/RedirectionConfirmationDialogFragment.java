package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

public class RedirectionConfirmationDialogFragment extends DialogFragment {
    private String title;
    private String message;
    private String positiveMessage;
    private String negativeMessage;
    private int directionActionId;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle inforBundle = getArguments();
        if (inforBundle.containsKey("title")) {
            title = inforBundle.getString("title");
            message = inforBundle.getString("message");
            positiveMessage = inforBundle.getString("posMessage");
            negativeMessage = inforBundle.getString("negMessage");
            directionActionId = inforBundle.getInt("actionId");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveMessage,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dismiss();
                                NavHostFragment.findNavController(RedirectionConfirmationDialogFragment.this)
                                        .navigate(directionActionId);
                            }
                        })
                .setNegativeButton(negativeMessage,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
        return builder.create();
    }
}
