package com.phucnguyen.khoaluantotnghiep.utils;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class DialogUtils {

    public static void navigateToInformationDialog(String title,
                                                   String message, String posMessage,
                                                   Fragment originFragment,
                                                   int actionId) {
        Bundle informationBundle = new Bundle();
        informationBundle.putString("title", title);
        informationBundle.putString("message", message);
        informationBundle.putString("posMessage", posMessage);
        NavHostFragment.findNavController(originFragment)
                .navigate(actionId, informationBundle);
    }
}
