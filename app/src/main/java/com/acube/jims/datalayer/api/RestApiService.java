package com.acube.jims.datalayer.api;

import com.acube.jims.Presentation.ScanItems.ResponseItems;
import com.acube.jims.datalayer.models.Authentication.ResponseCheckCustomer;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.acube.jims.datalayer.models.Authentication.ResponseLogin;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.models.Cart.ResponseCart;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogDetails;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.Compare.ResponseCompare;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceRegistration;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceUpdation;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseGetRegistered;
import com.acube.jims.datalayer.models.Favorites.ResponseFavorites;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RestApiService {

    @POST("User/authenticate")
    Call<ResponseLogin> doLogin(@Body JsonObject jsonObject);

    @GET("CustomerRegistration/GetCustomer/{phonenumber}")
    Call<ResponseCheckCustomer> CheckUserExists(@Path("phonenumber") String version);

    @POST("CustomerRegistration")
    Call<ResponseCreateCustomer> createUser(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @PUT("CustomerRegistration/{id}")
    Call<JsonObject> updateUser(@Path("id") int id, @Body JsonObject body);

    @POST("DeviceMaster/")
    Call<ResponseDeviceRegistration> registerDevice(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @PUT("DeviceMaster/{key}")
    Call<ResponseDeviceUpdation> updateDeviceRegistration(@Header("Authorization") String Auth, @Path("key") String key, @Body JsonObject body);

    @GET("DeviceMaster/GetDevice/{macaddress}")
    Call<ResponseGetRegistered> getRegisteredDetails(@Header("Authorization") String Auth, @Path("macaddress") String macaddress);

    @GET("AppRoleMenu/{appname}/{rolename}")
    Call<List<HomeData>> getHomeMenu(@Header("Authorization") String Auth, @Path("appname") String appname, @Path("rolename") String rolename);


    @POST("ItemCatalog/GetAllDetails")
    Call<List<ResponseCatalogueListing>> getCatalogueItems(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @GET("ItemCatalog/GetItemFilter")
    Call<List<ResponseFetchFilters>> getFilters(@Header("Authorization") String Auth);

    @GET("ItemCatalog/GetSingle/{id}")
    Call<ResponseCatalogDetails> getItemDetails(@Header("Authorization") String Auth, @Path("id") String id);

    @GET("CustomerRegistration/GetCustomerList/{FilterText}")
    Call<List<ResponseCustomerListing>> getCustomer(@Header("Authorization") String Auth, @Path("FilterText") String FilterText);

    @POST("CatalogCart/SaveCart/{Type}")
    Call<ResponseAddtoCart> AddtoCart(@Header("Authorization") String Auth, @Path("Type") String Type, @Body JsonArray jsonArray);

    @GET("CatalogCart/GetCartDetails/{CustomerID}")
    Call<ResponseCart> ViewCart(@Header("Authorization") String Auth, @Path("CustomerID") String CustomerID);

    @POST("FavouriteList/SaveList/{Type}")
    Call<JsonObject> AddtoFavorites(@Header("Authorization") String Auth, @Path("Type") String Type, @Body JsonObject jsonObject);

    @GET("FavouriteList/GetListDetails/{CustomerID}")
    Call<List<ResponseFavorites>> ViewFavorites(@Header("Authorization") String Auth, @Path("CustomerID") String CustomerID);
    @POST("FavouriteList/GetCompareListDetails")
    Call<List<ResponseCompare> >compareList(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("FavouriteList/GetCompareListDetails")
    Call<List<ResponseItems> >ItemList(@Header("Authorization") String Auth, @Body JsonObject jsonObject);
}
