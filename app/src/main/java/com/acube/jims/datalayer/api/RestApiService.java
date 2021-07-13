package com.acube.jims.datalayer.api;

import com.acube.jims.datalayer.models.Login.ResponseCheckCustomer;
import com.acube.jims.datalayer.models.Login.ResponseLogin;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestApiService {

    @POST("User/authenticate")
    Call<ResponseLogin> doLogin(@Body JsonObject jsonObject);

    @GET("CustomerRegistration/GetCustomer/{phonenumber}/")
    Call<ResponseCheckCustomer> CheckUserExists(@Path("phonenumber") String version);
}
