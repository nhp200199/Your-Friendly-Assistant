package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.phucnguyen.khoaluantotnghiep.R;

public class InforDialogFragment extends DialogFragment {
    public interface InforDialogListener {
        void onInformationIsRead();
    }

    private String title;
    private String message;
    private String positiveMessage;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle inforBundle = getArguments();
        if (inforBundle.containsKey("title")) {
            title = inforBundle.getString("title");
            message = inforBundle.getString("message");
            positiveMessage = inforBundle.getString("posMessage");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveMessage,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavHostFragment.findNavController(InforDialogFragment.this)
                                        .getPreviousBackStackEntry()
                                        .getSavedStateHandle()
                                        .set("isRead", true);
                            }
                        })
                .setCancelable(false);
        return builder.create();
    }
}
