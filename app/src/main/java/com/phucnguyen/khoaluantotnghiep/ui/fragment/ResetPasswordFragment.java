package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
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
import com.phucnguyen.khoaluantotnghiep.utils.DialogUtils;
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
    private boolean isSuccessfullySentResetEmail;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_reset_password, container, false);
        connectViews(v);

        NavController navController = NavHostFragment.findNavController(this);
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        final NavBackStackEntry navBackStackEntry = navController.getBackStackEntry(R.id.reset_password_fragment);
        // Create our observer and add it to the NavBackStackEntry's lifecycle
        final LifecycleEventObserver observer = new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event.equals(Lifecycle.Event.ON_RESUME)
                        && navBackStackEntry.getSavedStateHandle().contains("isRead")) {
                    boolean isRead = navBackStackEntry.getSavedStateHandle().get("isRead");
                    // Do something with the result
                    if (isRead && isSuccessfullySentResetEmail) {
                        Bundle emailBundle = new Bundle();
                        emailBundle.putString("email", edtEmail.getText().toString());
                        NavHostFragment.findNavController(ResetPasswordFragment.this)
                                .navigate(R.id.action_reset_password_fragment_to_log_in_fragment, emailBundle);
                    }
                }
            }
        };
        navBackStackEntry.getLifecycle().addObserver(observer);
        // As addObserver() does not automatically remove the observer, we
        // call removeObserver() manually when the view lifecycle is destroyed
        getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event.equals(Lifecycle.Event.ON_DESTROY)) {
                    navBackStackEntry.getLifecycle().removeObserver(observer);
                }
            }
        });

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
                                processContainer.setVisibility(View.GONE);

                                if (response.isSuccessful()) {
                                    if (response.body().isSuccess()) {
                                        Log.d("RESET-PASSWORD: ", "Email sent");
                                        isSuccessfullySentResetEmail = true;
                                        DialogUtils.navigateToInformationDialog("Thành công",
                                                "Một email dùng để reset mật khẩu đã được gửi đến bạn. Vui lòng kiểm" +
                                                        " tra và làm theo hướng dẫn",
                                                "về màn hình đăng nhập",
                                                ResetPasswordFragment.this,
                                                R.id.action_reset_password_fragment_to_information_dialog);
                                    }
                                } else {
                                    btnResetPassword.setEnabled(true);

                                    DialogUtils.navigateToInformationDialog("Không thể lấy lại mật khẩu",
                                            "Email này chưa đăng kí người dùng",
                                            "ok",
                                            ResetPasswordFragment.this,
                                            R.id.action_reset_password_fragment_to_information_dialog);
                                }
                            }

                            @Override
                            public void onFailure(Call<ResetPasswordResponse> call, Throwable t) {
                                Log.d("LOGIN: ", t.toString());
                                btnResetPassword.setEnabled(true);
                                processContainer.setVisibility(View.VISIBLE);

                                DialogUtils.navigateToInformationDialog("Không thể lấy lại mật khẩu",
                                        "Đã có lỗi xảy ra khi xử lý. Vui lòng thử lại",
                                        "ok",
                                        ResetPasswordFragment.this,
                                        R.id.action_reset_password_fragment_to_information_dialog);
                            }
                        });
            }
        });
    }
}