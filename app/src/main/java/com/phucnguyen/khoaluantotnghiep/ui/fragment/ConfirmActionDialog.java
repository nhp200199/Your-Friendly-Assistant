package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class ConfirmActionDialog extends DialogFragment {
    interface onConfirmActionListener {
        void onPositiveConfirmed();
        void onNegativeConfirmed();
    }

    private onConfirmActionListener listener;

    private String message;
    private String positiveAction;
    private String negativeAction;

    public ConfirmActionDialog(String message, String positiveAction, String negativeAction) {
        this.message = message;
        this.positiveAction = positiveAction;
        this.negativeAction = negativeAction;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setMessage(message)
                .setPositiveButton(positiveAction, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null)
                            listener.onPositiveConfirmed();
                    }
                })
                .setNegativeButton(negativeAction, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (listener != null)
                            listener.onNegativeConfirmed();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (onConfirmActionListener) getParentFragment();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }
}
