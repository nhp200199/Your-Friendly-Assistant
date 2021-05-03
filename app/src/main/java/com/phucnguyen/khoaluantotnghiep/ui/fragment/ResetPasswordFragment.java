package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.phucnguyen.khoaluantotnghiep.R;
import com.phucnguyen.khoaluantotnghiep.model.response.ResetPasswordResponse;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;
import com.phucnguyen.khoaluantotnghiep.service.UserService;
import com.phucnguyen.khoaluantotnghiep.utils.FormChecker;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordFragment extends Fragment {

    private TextInputLayout emailInputLayout;
    private TextInputEditText edtEmail;
    private Button btnResetPassword;
    private LinearLayout processContainer;

    private boolean areAllFieldQualified = false;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reset_password, container, false);
        connectViews(v);

        // Inflate the layout for this fragment
        return v;
    }

    private void connectViews(View v) {
        edtEmail = (TextInputEditText) v.findViewById(R.id.edtEmail);
        btnResetPassword = (Button) v.findViewById(R.id.btnResetPassword);
        emailInputLayout = (TextInputLayout) v.findViewById(R.id.emailInputLayout);
        processContainer = v.findViewById(R.id.processContainer);

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (FormChecker.isEmailValid(edtEmail.getText().toString().trim())) {
                    areAllFieldQualified = true;
                    emailInputLayout.setErrorEnabled(false);
                } else {
                    areAllFieldQualified = false;
                    emailInputLayout.setErrorEnabled(true);
                    emailInputLayout.setError("Định dạng email không đúng");
                }
                btnResetPassword.setEnabled(edtEmail.getText().toString().length() != 0
                        && areAllFieldQualified);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnResetPassword.setEnabled(false);
                sendRequestToResetPassword(edtEmail.getText().toString());
            }

            private void sendRequestToResetPassword(String email) {
                processContainer.setVisibility(View.VISIBLE);

                RetrofitInstance.getInstance().create(UserService.class)
                        .resetUserPassword(email)
                        .enqueue(new Callback<ResetPasswordResponse>() {
                            @Override
                            public void onResponse(Call<ResetPasswordResponse> call, Response<ResetPasswordResponse> response) {
                                if (response.isSuccessful()) {
                                    if (response.body().isSuccess()) {
                                        Log.d("RESET-PASSWORD: ", "Email sent");
                                        Bundle emailBundle = new Bundle();
                                        emailBundle.putString("email", email);
                                        NavHostFragment.findNavController(ResetPasswordFragment.this)
                                                .navigate(R.id.action_reset_password_fragment_to_log_in_fragment, emailBundle);
                                    }
                                } else {
                                    btnResetPassword.setEnabled(true);
                                    if (response.body().getMessage().equals("No user with that email found.")) {
                                        Log.d("RESET-PASSWORD: ", "No user with that email found.");
                                    } else {
                                        Log.d("RESET-PASSWORD: ", response.body().getMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                                btnResetPassword.setEnabled(true);
                                Log.d("LOGIN: ", t.toString());
                            }
                        });
            }
        });
    }
}