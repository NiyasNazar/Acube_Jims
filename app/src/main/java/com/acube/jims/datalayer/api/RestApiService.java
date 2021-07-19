package com.acube.jims.datalayer.api;

import com.acube.jims.datalayer.models.Authentication.ResponseCheckCustomer;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.acube.jims.datalayer.models.Authentication.ResponseLogin;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceRegistration;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseGetRegistered;
import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestApiService {

    @POST("User/authenticate")
    Call<ResponseLogin> doLogin(@Body JsonObject jsonObject);

    @GET("CustomerRegistration/GetCustomer/{phonenumber}")
    Call<ResponseCheckCustomer> CheckUserExists(@Path("phonenumber") String version);

    @POST("CustomerRegistration/")
    Call<ResponseCreateCustomer> createCustomer(@Body JsonObject jsonObject);

    @PUT("CustomerRegistration/{id}")
    Call<JsonObject> updateUser(@Path("id") int id, @Body JsonObject body);

    @POST("DeviceMaster/")
    Call<ResponseDeviceRegistration> registerDevice(@Body JsonObject jsonObject);

    @PUT("DeviceMaster/{key}")
    Call<JsonObject> updateDeviceRegistration(@Path("key") int id, @Body JsonObject body);

    @GET("DeviceMaster/GetDevice/{macaddress}")
    Call<ResponseGetRegistered> getRegisteredDetails(@Path("macaddress") String macaddress);
}
