package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
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
import com.phucnguyen.khoaluantotnghiep.model.response.LogInResponse;
import com.phucnguyen.khoaluantotnghiep.service.RetrofitInstance;
import com.phucnguyen.khoaluantotnghiep.service.UserService;
import com.phucnguyen.khoaluantotnghiep.utils.FormChecker;
import com.phucnguyen.khoaluantotnghiep.viewmodel.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInFragment extends Fragment implements InforDialogFragment.InforDialogListener {
    private TextInputLayout emailInputLayout;
    private TextInputEditText edtEmail;
    private TextInputEditText editPassword;
    private Button btnLogin;
    private TextView tvResetPassword;
    private TextView tvSignUpHint;
    private LinearLayout processContainer;

    private boolean areAllFieldQualified = false;
    private UserViewModel userViewModel;
    private SharedPreferences.Editor userSharedPreferenceEditor;
    private String email = null; //this email only has value if there is 'email' argument coming from
    //some fragment

    public LogInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSharedPreferenceEditor = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
                .edit();
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        email = getArguments().getString("email");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_log_in, container, false);
        connectViews(v);

        // Inflate the layout for this fragment
        return v;
    }

    private void connectViews(View v) {
        edtEmail = (TextInputEditText) v.findViewById(R.id.edtEmail);
        editPassword = (TextInputEditText) v.findViewById(R.id.edtPassword);
        btnLogin = (Button) v.findViewById(R.id.btnLogin);
        tvResetPassword = (TextView) v.findViewById(R.id.tvResetPassword);
        tvSignUpHint = (TextView) v.findViewById(R.id.tvSignUpHint);
        emailInputLayout = (TextInputLayout) v.findViewById(R.id.emailInputLayout);
        processContainer = v.findViewById(R.id.processContainer);

        edtEmail.setText(email);

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
                btnLogin.setEnabled(editPassword.getText().toString().length() != 0 &&
                        edtEmail.getText().toString().length() != 0 && areAllFieldQualified);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                btnLogin.setEnabled(editPassword.getText().toString().length() != 0 &&
                        edtEmail.getText().toString().length() != 0 && areAllFieldQualified);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogin.setEnabled(false);
                loginToUser();
            }

            private void loginToUser() {
                processContainer.setVisibility(View.VISIBLE);

                RetrofitInstance.getInstance().create(UserService.class)
                        .loginToUser(edtEmail.getText().toString(),
                                editPassword.getText().toString())
                        .enqueue(new Callback<LogInResponse>() {
                            @Override
                            public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                                processContainer.setVisibility(View.GONE);

                                if (response.isSuccessful()) {
                                    if (response.body().isSuccess()) {
                                        userSharedPreferenceEditor.putString("accessToken", response.body().getAccessToken())
                                                .apply();
                                        userSharedPreferenceEditor.putString("refreshToken", response.body().getRefreshToken())
                                                .apply();
                                        userViewModel.setNewTokenId(response.body().getAccessToken());
                                        Log.d("LOGIN: ", "Logged In");
                                        Log.d("LOGIN-ACCESS-TOKEN: ", response.body().getAccessToken());
                                        NavHostFragment.findNavController(LogInFragment.this)
                                                .navigate(R.id.action_log_in_fragment_to_setting_fragment);
                                    } else {
                                        Bundle errorBundle = new Bundle();
                                        if (response.body().getMessage().equals("Invalid credentials")) {
                                            Log.d("LOGIN: ", "Invalid credentials");
                                            errorBundle.putString("title", "Đăng nhập không thành công");
                                            errorBundle.putString("message",
                                                    "Tài khoản hoặc mật khẩu không đúng. Vui lòng thử lại");
                                            errorBundle.putString("posMessage",
                                                    "ok");
                                            NavHostFragment.findNavController(LogInFragment.this)
                                                    .navigate(R.id.action_log_in_fragment_to_information_dialog,
                                                            errorBundle);
                                        } else {
                                            Log.d("LOGIN: ", response.body().getMessage());
                                            errorBundle.putString("title", "Đăng nhập không thành công");
                                            errorBundle.putString("message",
                                                    "Tài khoản hoặc mật khẩu không đúng. Vui lòng thử lại");
                                            errorBundle.putString("posMessage",
                                                    "ok");
                                            NavHostFragment.findNavController(LogInFragment.this)
                                                    .navigate(R.id.action_log_in_fragment_to_information_dialog,
                                                            errorBundle);
                                        }
                                    }
                                } else {
                                    Bundle errorBundle = new Bundle();
                                    Log.d("LOGIN: ", response.toString());
                                    errorBundle.putString("title", "Đăng nhập không thành công");
                                    errorBundle.putString("message",
                                            "Tài khoản hoặc mật khẩu không đúng. Vui lòng thử lại");
                                    errorBundle.putString("posMessage",
                                            "ok");
                                    NavHostFragment.findNavController(LogInFragment.this)
                                            .navigate(R.id.action_log_in_fragment_to_information_dialog,
                                                    errorBundle);
                                    btnLogin.setEnabled(true);
                                }
                            }

                            @Override
                            public void onFailure(Call<LogInResponse> call, Throwable t) {
                                Log.d("LOGIN: ", t.toString());
                                processContainer.setVisibility(View.GONE);
                                btnLogin.setEnabled(true);
                            }
                        });
            }
        });
        tvSignUpHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LogInFragment.this)
                        .navigate(R.id.action_log_in_fragment_to_register_fragment);
            }
        });
        tvResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(LogInFragment.this)
                        .navigate(R.id.action_log_in_fragment_to_reset_password_fragment);
            }
        });

        String registerHint = "Chưa có tài khoản? Đăng kí ngay";
        SpannableString spannableString = new SpannableString(registerHint);
        spannableString.setSpan(new UnderlineSpan(),
                registerHint.indexOf('?') + 2,
                registerHint.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),
                registerHint.indexOf('?') + 2,
                registerHint.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(getActivity().getColor(R.color.blue_500)),
                registerHint.indexOf('?') + 2,
                registerHint.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvSignUpHint.setText(spannableString);
    }

    @Override
    public void onInformationIsRead() {
        //do nothing for this Login fragment
    }
}