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

public class InforDialogFragment extends DialogFragment {
    public interface InforDialogListener {
        void onInformationIsRead();
    }

    private String title;
    private String message;
    private String positiveMessage;
    private InforDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Verify that the host activity (or fragment) implements the callback interface
        try {
            // Instantiate the InforDialogListener so we can send events to the host
            listener = (InforDialogListener) getParentFragment();
        } catch (ClassCastException e) {
            // The activity (or fragment) doesn't implement the interface, throw exception
            throw new ClassCastException("must implement InforDialogListener");
        }
    }

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
                                if (listener != null)
                                    listener.onInformationIsRead();
                            }
                        });
        return builder.create();
    }
}
