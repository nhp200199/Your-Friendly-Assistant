package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;
import com.phucnguyen.khoaluantotnghiep.service.UserService;
import com.phucnguyen.khoaluantotnghiep.utils.DialogUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeUserNameDialogFragment extends DialogFragment {
    private Button btnChangeUserName;
    private TextView tvLoadingResult;
    private LinearLayout processContainer;
    private TextInputEditText edtUsername;
    private String mCurrentUsername;

    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View customView = inflater.inflate(R.layout.dialog_change_username, null);
        connectViews(customView);

        builder.setView(customView);
        return builder.create();
    }

    private void connectViews(View customView) {
        tvLoadingResult = customView.findViewById(R.id.tvLoadingResult);
        btnChangeUserName = customView.findViewById(R.id.btnChangeUsername);
        processContainer = customView.findViewById(R.id.processContainer);
        edtUsername = customView.findViewById(R.id.edtName);

        mCurrentUsername = getArguments().getString("username", "Thierry Henry");
        edtUsername.setText(mCurrentUsername);
        edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnChangeUserName.setEnabled(!edtUsername.getText().toString().equals(mCurrentUsername));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnChangeUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChangeUserName.setEnabled(false);
                processContainer.setVisibility(View.VISIBLE);

                String newUsername = edtUsername.getText().toString().trim();
                String authorizedString = "Bearer " + getActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
                        .getString("accessToken", null);

                RetrofitInstance.getInstance().create(UserService.class)
                        .changeUsername(newUsername,
                                authorizedString)
                        .enqueue(new Callback<JsonObject>() {
                            @Override
                            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                btnChangeUserName.setEnabled(true);
                                processContainer.setVisibility(View.GONE);
                                if (response.isSuccessful()) {
                                    tvLoadingResult.setText("Tên tài khoản đã được thay đổi. Bạn cần đăng nhập lại để hoàn thành thay đổi");
                                    tvLoadingResult.setTextColor(getResources().getColor(R.color.blue_500));

                                    mCurrentUsername = newUsername;
                                } else {
                                    tvLoadingResult.setText("Đã có lỗi xảy ra. Vui lòng thử lại");
                                    tvLoadingResult.setTextColor(getResources().getColor(R.color.red_sad));
                                }
                            }

                            @Override
                            public void onFailure(Call<JsonObject> call, Throwable t) {
                                btnChangeUserName.setEnabled(true);
                                processContainer.setVisibility(View.GONE);
                                tvLoadingResult.setText("Đã có lỗi xảy ra. Vui lòng thử lại");
                                tvLoadingResult.setTextColor(getResources().getColor(R.color.red_sad));
                            }
                        });
            }
        });
    }
}
