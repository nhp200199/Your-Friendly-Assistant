package com.phucnguyen.khoaluantotnghiep.service;

import com.google.gson.JsonObject;
import com.phucnguyen.khoaluantotnghiep.model.response.LogInResponse;
import com.phucnguyen.khoaluantotnghiep.model.response.RegistrationResponse;
import com.phucnguyen.khoaluantotnghiep.model.response.ResetPasswordResponse;
import com.phucnguyen.khoaluantotnghiep.model.response.UserAccountResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @FormUrlEncoded
    @POST("api/v1/auth/register")
    Call<RegistrationResponse> createNewUser(@Field("name") String name,
                                             @Field("email") String email,
                                             @Field("password") String password);

    @FormUrlEncoded
    @POST("api/v1/auth/login")
    Call<LogInResponse> loginToUser(@Field("email") String email,
                                    @Field("password") String password,
                                    @Field("deviceToken") String deviceToken);

    @GET("api/v1/auth/logout")
    Call<JsonObject> logoutUser(@Header("Authorization") String authorizedString);

    @FormUrlEncoded
    @POST("api/v1/auth/forgot-password")
    Call<ResetPasswordResponse> resetUserPassword(@Field("email") String email);

    @GET("api/v1/user/my-account?include=item")
    Call<UserAccountResponse> getUserAccount(@Header("Authorization") String authorizedString);

    @FormUrlEncoded
    @POST("api/v1/auth/token")
    Call<LogInResponse> createNewAccessToken(@Field("refreshToken") String refreshTokenIdSharePref);

    @FormUrlEncoded
    @POST("api/v1/user/tracking-items")
    Call<JsonObject> trackProduct(@FieldMap Map<String, String> productInfoMap,
                                  @Header("Authorization") String authorizedString);

    @DELETE("api/v1/user/tracking-items/{productId}")
    Call<JsonObject> deleteTrackedProduct(@Path("productId") String productId,
                                          @Query("platform") String platform,
                                          @Header("Authorization") String authString);

    @FormUrlEncoded
    @PUT("api/v1/user/change-password")
    Call<JsonObject> changePassword(@Field("currentPassword") String currentPassword,
                                    @Field("newPassword") String newPassword,
                                    @Header("Authorization") String authorizedString);

    @FormUrlEncoded
    @PUT("api/v1/user/update-account")
    Call<JsonObject> changeUsername(@Field("name") String name,
                                    @Header("Authorization") String authorizedString);
}
