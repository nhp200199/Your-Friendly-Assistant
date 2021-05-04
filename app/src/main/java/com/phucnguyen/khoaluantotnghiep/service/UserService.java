package com.phucnguyen.khoaluantotnghiep.service;

import com.phucnguyen.khoaluantotnghiep.model.response.LogInResponse;
import com.phucnguyen.khoaluantotnghiep.model.response.RegistrationResponse;
import com.phucnguyen.khoaluantotnghiep.model.response.ResetPasswordResponse;
import com.phucnguyen.khoaluantotnghiep.model.response.UserAccountResponse;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserService {
    @FormUrlEncoded
    @POST("api/v1/auth/register")
    Call<RegistrationResponse> createNewUser(@Field("name") String name,
                                             @Field("email") String email,
                                             @Field("password") String password);

    @FormUrlEncoded
    @POST("api/v1/auth/login")
    Call<LogInResponse> loginToUser(@Field("email") String email,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("api/v1/auth/forgot-password")
    Call<ResetPasswordResponse> resetUserPassword(@Field("email") String email);

    @GET("api/v1/user/my-account?include=item")
    Call<UserAccountResponse> getUserAccount(@Header("Authorization") String authorizedString);
}
