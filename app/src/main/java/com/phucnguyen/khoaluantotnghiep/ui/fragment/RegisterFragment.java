package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
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

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private TextInputLayout emailInputLayout;
    private TextInputLayout passwordInputLayout;
    private TextInputLayout passwordRepeatInputLayout;
    private TextInputEditText edtEmail;
    private TextInputEditText edtPassword;
    private TextInputEditText edtName;
    private TextInputEditText edtRepeatPassword;
    private Button btnRegister;
    private LinearLayout processContainer;

    private boolean areAllFieldQualified = false;
    private boolean isEmailFieldQualified = false;
    private boolean isPasswordFieldQualified = false;
    private boolean isRepeatPasswordQualified = false;
    private boolean isSuccessfullyCreatedAccount = false;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        connectViews(v);

        NavController navController = NavHostFragment.findNavController(this);
        // After a configuration change or process death, the currentBackStackEntry
        // points to the dialog destination, so you must use getBackStackEntry()
        // with the specific ID of your destination to ensure we always
        // get the right NavBackStackEntry
        final NavBackStackEntry navBackStackEntry = navController.getBackStackEntry(R.id.register_fragment);
        // Create our observer and add it to the NavBackStackEntry's lifecycle
        final LifecycleEventObserver observer = new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event.equals(Lifecycle.Event.ON_RESUME)
                        && navBackStackEntry.getSavedStateHandle().contains("isRead")) {
                    boolean isRead = navBackStackEntry.getSavedStateHandle().get("isRead");
                    // Do something with the result
                    if (isRead && isSuccessfullyCreatedAccount) {
                        Bundle emailBundle = new Bundle();
                        emailBundle.putString("email", edtEmail.getText().toString());
                        NavHostFragment.findNavController(RegisterFragment.this)
                                .navigate(R.id.action_register_fragment_to_log_in_fragment, emailBundle);
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
        edtPassword = (TextInputEditText) v.findViewById(R.id.edtPassword);
        edtName = (TextInputEditText) v.findViewById(R.id.edtName);
        edtRepeatPassword = (TextInputEditText) v.findViewById(R.id.edtRepeatPassword);
        btnRegister = (Button) v.findViewById(R.id.btnRegister);
        emailInputLayout = (TextInputLayout) v.findViewById(R.id.emailInputLayout);
        passwordInputLayout = (TextInputLayout) v.findViewById(R.id.passwordInputLayout);
        passwordRepeatInputLayout = (TextInputLayout) v.findViewById(R.id.passwordRepeatInputLayout);
        processContainer = v.findViewById(R.id.processContainer);

        passwordInputLayout.setErrorIconDrawable(0);
        passwordRepeatInputLayout.setErrorIconDrawable(0);

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (FormChecker.isEmailValid(edtEmail.getText().toString().trim())) {
                    isEmailFieldQualified = true;
                    emailInputLayout.setErrorEnabled(false);
                    if (isPasswordFieldQualified && isRepeatPasswordQualified)
                        areAllFieldQualified = true;
                } else {
                    isEmailFieldQualified = false;
                    areAllFieldQualified = false;
                    emailInputLayout.setErrorEnabled(true);
                    emailInputLayout.setError("Định dạng email không đúng");
                }
                btnRegister.setEnabled(areAllFieldQualified);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (FormChecker.isPasswordValid(edtPassword.getText().toString())) {
                    isPasswordFieldQualified = true;
                    passwordInputLayout.setErrorEnabled(false);
                    boolean isRepeatPasswordCaughtUp = edtRepeatPassword.getText().toString().equals(edtPassword.getText().toString());
                    if (!isRepeatPasswordCaughtUp) {
                        isRepeatPasswordQualified = false;
                        passwordRepeatInputLayout.setErrorEnabled(true);
                        passwordRepeatInputLayout.setError("Mật khẩu nhập lại chưa khớp");
                    }
                    if (isEmailFieldQualified && isRepeatPasswordQualified)
                        areAllFieldQualified = true;
                } else {
                    isPasswordFieldQualified = false;
                    areAllFieldQualified = false;
                    passwordInputLayout.setErrorEnabled(true);
                    passwordInputLayout.setError("Mật khẩu cần dài có độ dài 8-20 kí tự, có ít nhất 1 chữ hoa, 1 chữ thường, 1 chữ số và không chứa khoảng cách");
                }
                btnRegister.setEnabled(areAllFieldQualified);
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
                    if (isEmailFieldQualified && isPasswordFieldQualified)
                        areAllFieldQualified = true;
                } else {
                    isRepeatPasswordQualified = false;
                    areAllFieldQualified = false;
                    passwordRepeatInputLayout.setErrorEnabled(true);
                    passwordRepeatInputLayout.setError("Mật khẩu nhập lại chưa khớp");
                }
                btnRegister.setEnabled(areAllFieldQualified);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequestToCreateNewUser();
            }

            private void sendRequestToCreateNewUser() {
                btnRegister.setEnabled(false);
                processContainer.setVisibility(View.VISIBLE);

                String userName = edtName.getText().toString().trim();
                if (userName.length() == 0)
                    userName = "Anonymous";
                RetrofitInstance.getInstance().create(UserService.class)
                        .createNewUser(userName,
                                edtEmail.getText().toString().trim(),
                                edtPassword.getText().toString())
                        .enqueue(new Callback<RegistrationResponse>() {
                            @Override
                            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                                btnRegister.setEnabled(true);
                                processContainer.setVisibility(View.GONE);
                                if (response.isSuccessful()) {
                                    isSuccessfullyCreatedAccount = true;
                                    Log.d("REGISTRATION: ", "need to verify email");
                                    //remove all fields when successfully registered
                                    edtName.setText("");
                                    edtEmail.setText("");
                                    edtPassword.setText("");
                                    edtRepeatPassword.setText("");

                                    DialogUtils.navigateToInformationDialog("Thành công",
                                            "Một email dùng để xác thực tài khoản đã được gửi đến bạn. Vui lòng kiểm" +
                                                    " tra và xác thực tài khoản trước khi đăng nhập",
                                            "về màn hình đăng nhập",
                                            RegisterFragment.this,
                                            R.id.action_register_fragment_to_information_dialog);
                                } else {
                                    Log.d("REGISTRATION: ", response.toString());
                                    String errorJsonString;
                                    Gson gson = new Gson();
                                    try {
                                        errorJsonString = response.errorBody().string();
                                        JsonObject jsonObject = gson.fromJson(errorJsonString, JsonObject.class);
                                        if (jsonObject.get("message").getAsString().equals("User already exists")) {
                                            DialogUtils.navigateToInformationDialog("Không thể tạo tài khoản",
                                                    "Tài khoản đã tồn tại",
                                                    "ok",
                                                    RegisterFragment.this,
                                                    R.id.action_register_fragment_to_information_dialog);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        DialogUtils.navigateToInformationDialog("Không thể tạo tài khoản",
                                                "Đã có lỗi xảy ra khi xử lý. Vui lòng thử lại",
                                                "ok",
                                                RegisterFragment.this,
                                                R.id.action_register_fragment_to_information_dialog);
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                                btnRegister.setEnabled(true);
                                processContainer.setVisibility(View.GONE);
                                Log.d("REGISTRATION: ", t.toString());
                                DialogUtils.navigateToInformationDialog("Không thể tạo tài khoản",
                                        "Đã có lỗi xảy ra khi xử lý. Vui lòng thử lại",
                                        "ok",
                                        RegisterFragment.this,
                                        R.id.action_register_fragment_to_information_dialog);
                            }
                        });
            }
        });
    }
}