package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.response.RegistrationResponse;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;
import com.phucnguyen.khoaluantotnghiep.service.UserService;
import com.phucnguyen.khoaluantotnghiep.utils.DialogUtils;
import com.phucnguyen.khoaluantotnghiep.utils.FormChecker;
import com.phucnguyen.khoaluantotnghiep.viewmodel.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInformationDialogFragment extends DialogFragment {
    private TextView tvUserEmail;
    private TextView tvUserName;
    private TextView tvActionChangeUserName;
    private TextView tvActionChangePassword;
    private TextInputLayout oldPasswordInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputLayout passwordRepeatInputLayout;
    private TextInputEditText edtOldPassword;
    private TextInputEditText edtPassword;
    private TextInputEditText edtRepeatPassword;
    private Button btnChangePassword;
    private LinearLayout processContainer;

    private boolean areAllFieldQualified = false;
    private boolean isPasswordFieldQualified = false;
    private boolean isRepeatPasswordQualified = false;

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View customView = inflater.inflate(R.layout.dialog_user_information, null);
        connectViews(customView);

        builder.setView(customView);
        return builder.create();
    }

    private void connectViews(View customView) {
        tvUserEmail = customView.findViewById(R.id.tvUserEmail);
        tvUserName = customView.findViewById(R.id.tvUserName);
        tvActionChangePassword = customView.findViewById(R.id.tvActionChangePassword);
        tvActionChangeUserName = customView.findViewById(R.id.tvActionChangeUserName);
        edtOldPassword = (TextInputEditText) customView.findViewById(R.id.oldEdtPassword);
        edtPassword = (TextInputEditText) customView.findViewById(R.id.edtPassword);
        edtRepeatPassword = (TextInputEditText) customView.findViewById(R.id.edtRepeatPassword);
        btnChangePassword = (Button) customView.findViewById(R.id.btnChangePassword);
        oldPasswordInputLayout = (TextInputLayout) customView.findViewById(R.id.oldPasswordInputLayout);
        passwordInputLayout = (TextInputLayout) customView.findViewById(R.id.passwordInputLayout);
        passwordRepeatInputLayout = (TextInputLayout) customView.findViewById(R.id.passwordRepeatInputLayout);
        processContainer = customView.findViewById(R.id.processContainer);

        passwordInputLayout.setErrorIconDrawable(0);
        passwordRepeatInputLayout.setErrorIconDrawable(0);

        tvUserEmail.setText(getArguments().getString("userEmail"));
        tvUserName.setText(getArguments().getString("userName"));

        tvActionChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPasswordInputLayout.setVisibility(View.VISIBLE);
                passwordInputLayout.setVisibility(View.VISIBLE);
                passwordRepeatInputLayout.setVisibility(View.VISIBLE);
                btnChangePassword.setVisibility(View.VISIBLE);
            }
        });
        tvActionChangeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle userBundle = new Bundle();
                userBundle.putString("username", getArguments().getString("userName"));
                Navigation.findNavController(requireActivity(), R.id.setting_navigation_host_fragment)
                        .navigate(R.id.action_user_information_fragment_to_change_username_dialog, userBundle);
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChangePassword.setEnabled(false);
                processContainer.setVisibility(View.VISIBLE);

                String oldPassword = edtOldPassword.getText().toString().trim();
                String newPassword = edtPassword.getText().toString().trim();
                String authorizedString = "Bearer " + getActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
                        .getString("accessToken", null);

                RetrofitInstance.getInstance().create(UserService.class)
                        .changePassword(oldPassword,
                                newPassword,
                                authorizedString)
                        .enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                btnChangePassword.setEnabled(true);
                                processContainer.setVisibility(View.GONE);
                                if (response.isSuccessful()) {
                                    DialogUtils.navigateToInformationDialog("Thành công",
                                            "Mật khẩu của bạn đã được thay đổi",
                                            "ok",
                                            UserInformationDialogFragment.this,
                                            R.id.action_user_information_fragment_to_information_dialog);
                                } else {
                                    DialogUtils.navigateToInformationDialog("Đổi mật khẩu không thành công",
                                            "Mật khẩu cũ của bạn không đúng. Vui lòng kiểm tra lại",
                                            "ok",
                                            UserInformationDialogFragment.this,
                                            R.id.action_user_information_fragment_to_information_dialog);
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                btnChangePassword.setEnabled(true);
                                processContainer.setVisibility(View.GONE);
                                DialogUtils.navigateToInformationDialog("Đổi mật khẩu không thành công",
                                        "Đã có lỗi xảy ra khi xử lý. Vui lòng thử lại",
                                        "ok",
                                        UserInformationDialogFragment.this,
                                        R.id.action_user_information_fragment_to_information_dialog);
                            }
                        });
            }
        });

        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (FormChecker.isPasswordValid(edtPassword.getText().toString())
                        && !edtPassword.getText().toString().equals(edtOldPassword.getText().toString())) {
                    isPasswordFieldQualified = true;
                    passwordInputLayout.setErrorEnabled(false);
                    boolean isRepeatPasswordCaughtUp = edtRepeatPassword.getText().toString().equals(edtPassword.getText().toString());
                    if (!isRepeatPasswordCaughtUp) {
                        isRepeatPasswordQualified = false;
                        passwordRepeatInputLayout.setErrorEnabled(true);
                        passwordRepeatInputLayout.setError("Mật khẩu nhập lại chưa khớp");
                    }
                    if (isRepeatPasswordQualified)
                        areAllFieldQualified = true;
                } else {
                    isPasswordFieldQualified = false;
                    areAllFieldQualified = false;
                    passwordInputLayout.setErrorEnabled(true);
                    passwordInputLayout.setError("Mật khẩu cần dài có độ dài 8-20 kí tự, có ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và không chứa khoảng cách\n"
                            + "Mật khẩu mới cần khác mật khẩu cũ");
                }
                btnChangePassword.setEnabled(areAllFieldQualified);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtRepeatPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtRepeatPassword.getText().toString().equals(edtPassword.getText().toString())) {
                    isRepeatPasswordQualified = true;
                    passwordRepeatInputLayout.setErrorEnabled(false);
                    if (isPasswordFieldQualified)
                        areAllFieldQualified = true;
                } else {
                    isRepeatPasswordQualified = false;
                    areAllFieldQualified = false;
                    passwordRepeatInputLayout.setErrorEnabled(true);
                    passwordRepeatInputLayout.setError("Mật khẩu nhập lại chưa khớp");
                }
                btnChangePassword.setEnabled(areAllFieldQualified);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
