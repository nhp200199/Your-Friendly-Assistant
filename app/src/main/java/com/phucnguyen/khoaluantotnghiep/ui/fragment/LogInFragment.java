package com.phucnguyen.khoaluantotnghiep.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import com.phucnguyen.khoaluantotnghiep.utils.Contants;
import com.phucnguyen.khoaluantotnghiep.utils.DialogUtils;
import com.phucnguyen.khoaluantotnghiep.utils.FormChecker;
import com.phucnguyen.khoaluantotnghiep.viewmodel.UserViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.phucnguyen.khoaluantotnghiep.utils.Contants.*;

public class LogInFragment extends Fragment implements InforDialogFragment.InforDialogListener {
    private TextInputLayout emailInputLayout;
    private TextInputEditText edtEmail;
    private TextInputEditText editPassword;
    private Button btnLogin;
    private TextView tvResetPassword;
    private TextView tvSignUpHint;
    private LinearLayout processContainer;

    private boolean areAllFieldQualified = true;
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

        userViewModel.getUserLoadingState().observe(getViewLifecycleOwner(), new Observer<UserLoadingState>() {
            @Override
            public void onChanged(UserLoadingState userLoadingState) {
                switch (userLoadingState) {
                    case LOADING:
                        processContainer.setVisibility(View.VISIBLE);
                        break;
                    case INVALID_CREDENTIALS:
                        processContainer.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);

                        //reset user loading's state
                        userViewModel.setUserLoadingState(UserLoadingState.NONE);

                        DialogUtils.navigateToInformationDialog("Đăng nhập không thành công",
                                "Tài khoản hoặc mật khẩu không đúng. Vui lòng thử lại",
                                "ok",
                                LogInFragment.this,
                                R.id.action_log_in_fragment_to_information_dialog);
                        break;
                    case NOT_VERIFIED:
                        processContainer.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);

                        //reset user loading's state
                        userViewModel.setUserLoadingState(UserLoadingState.NONE);

                        DialogUtils.navigateToInformationDialog("Đăng nhập không thành công",
                                "Tài khoản chưa được xác thực. Vui lòng kiểm tra email và" +
                                        " xác thực tài khoản",
                                "ok",
                                LogInFragment.this,
                                R.id.action_log_in_fragment_to_information_dialog);
                        break;
                    case NETWORK_ERROR:
                        processContainer.setVisibility(View.GONE);
                        btnLogin.setEnabled(true);

                        userViewModel.setUserLoadingState(UserLoadingState.NONE);

                        DialogUtils.navigateToInformationDialog("Đăng nhập không thành công",
                                "Đã xảy ra lỗi khi thực hiện đăng nhập. Vui lòng thử lại",
                                "ok",
                                LogInFragment.this,
                                R.id.action_log_in_fragment_to_information_dialog);
                        break;
                    case SUCCESS:
                        processContainer.setVisibility(View.GONE);

                        NavHostFragment.findNavController(LogInFragment.this)
                                .popBackStack();
                        break;
                }
            }
        });
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
                userViewModel.loginUser(edtEmail.getText().toString(),
                        editPassword.getText().toString());
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