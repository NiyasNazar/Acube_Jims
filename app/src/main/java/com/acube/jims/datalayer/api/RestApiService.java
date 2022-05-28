package com.acube.jims.datalayer.api;

import com.acube.jims.datalayer.models.Audit.AuditResults;
import com.acube.jims.datalayer.models.ItemRequest.ResponseCreateItemrequest;
import com.acube.jims.datalayer.models.ItemRequest.ResponseFetchPickList;
import com.acube.jims.datalayer.models.ItemRequest.ResponseItemRequestDetails;
import com.acube.jims.datalayer.remote.dbmodel.ItemRequestEntry;
import com.acube.jims.presentation.ScanItems.ResponseItems;
import com.acube.jims.datalayer.models.Analytics.ResponseAnalyticsGraph;
import com.acube.jims.datalayer.models.Analytics.ResponseAnalyticsSummary;
import com.acube.jims.datalayer.models.Analytics.ResponseCustomerServed;
import com.acube.jims.datalayer.models.Analytics.ResponseItemWiseAnalytics;
import com.acube.jims.datalayer.models.Audit.AuditScanUpload;
import com.acube.jims.datalayer.models.Audit.ResponseAudit;
import com.acube.jims.datalayer.models.Audit.ResponseLocationList;
import com.acube.jims.datalayer.models.Audit.ResponseReport;
import com.acube.jims.datalayer.models.Authentication.ResponseCheckCustomer;
import com.acube.jims.datalayer.models.Authentication.ResponseCreateCustomer;
import com.acube.jims.datalayer.models.Authentication.ResponseLogin;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.models.Cart.ResponseCart;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogDetails;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.Compare.ResponseCompare;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerHistory;
import com.acube.jims.datalayer.models.CustomerManagment.ResponseCustomerListing;
import com.acube.jims.datalayer.models.Dashboard.ResponseDashBoardGraph;
import com.acube.jims.datalayer.models.Dashboard.ResponseDashboardSummary;
import com.acube.jims.datalayer.models.Dashboard.ResponseDashboardpiechart;
import com.acube.jims.datalayer.models.Dashboard.ResponseTop10ProductsSold;
import com.acube.jims.datalayer.models.Dashboard.ResponseTopCategory;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceRegistration;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseDeviceUpdation;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseGetRegistered;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseTrayMaster;
import com.acube.jims.datalayer.models.Favorites.ResponseFavorites;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;
import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
import com.acube.jims.datalayer.models.Invoice.SaleSuccess;
import com.acube.jims.datalayer.models.ScanHistory.ResponseScanHistory;
import com.acube.jims.datalayer.models.warehouse.ResponseWareHouse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

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


    @POST("ItemCatalog/GetAllSummary")
    Call<List<ResponseCatalogueListing>> getCatalogueSummary(@Header("Authorization") String Auth, @Body JsonObject jsonObject);


    @GET("ItemCatalog/GetItemFilter")
    Call<List<ResponseFetchFilters>> getFilters(@Header("Authorization") String Auth);

    @GET("ItemCatalog/GetSingle/{id}")
    Call<ResponseCatalogDetails> getItemDetails(@Header("Authorization") String Auth, @Path("id") String id);

    @GET("ItemCatalog/GetSingleItemDetails/{ItemID}")
    Call<ResponseCatalogDetails> getItemDetailsoutofstock(@Header("Authorization") String Auth, @Path("ItemID") String ItemID);


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
    Call<List<ResponseCompare>> compareList(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("FavouriteList/GetCompareListDetails")
    Call<List<ResponseItems>> ItemList(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @GET("TrayMaster")
    Call<List<ResponseTrayMaster>> FetchTrayMaster(@Header("Authorization") String Auth);

    @POST("CustomerRegistration/CustomerLogout")
    Call<JsonObject> CustomerLogout(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @GET("CustomerRegistration/GetCustomerHistory/{CustomerID}")
    Call<ResponseCustomerHistory> FetchCustomerHistory(@Header("Authorization") String Auth, @Path("CustomerID") String CustomerID);

    @POST("Sale/GetQuoteDetails")
    Call<List<ResponseInvoiceList>> InvoiceItems(@Header("Authorization") String Auth, @Body String[] data);

    @POST("Audit/GetDocumentForMobile")
    Call<List<ResponseAudit>> AuditDetails(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("Audit/GetDocumentForMobile")
    Call<List<ResponseAudit>> AuditHeader(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("Audit/GetAuditCandidateList")
    Call<List<ResponseLocationList>> AuditLocationList(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("Audit/AuditScanUpload")
    Call<AuditScanUpload> AuditUpload(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("Audit/GetAuditReport")
    Call<ResponseReport> FetchReport(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("Audit/GetAuditHistory")
    Call<List<ResponseScanHistory>> ItemHistory(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("SaleMobile/SaveSale")
    Call<SaleSuccess> markSaleOrQuotation(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("CatalogCart/ClearCart/{CustomerID}")
    Call<JsonObject> ClearCart(@Header("Authorization") String Auth, @Path("CustomerID") String CustomerID);

    @POST("AnalyticsReport/GetAnalyticsSummary")
    Call<ResponseAnalyticsSummary> getAnalyticsSummary(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("AnalyticsReport/GetItemWiseAnalytics")
    Call<ResponseItemWiseAnalytics> getGetItemWiseAnalytics(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("AnalyticsReport/GetEmployeeWiseAnalytics")
    Call<ResponseCustomerServed> getCustomerServed(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("AnalyticsReport/GetCategoryWiseAnalytics")
    Call<ResponseAnalyticsGraph> getAnalyticsGraph(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @GET("warehouse")
    Call<List<ResponseWareHouse>> Fetchwarehouse(@Header("Authorization") String Auth);

    @POST("DashboardReport/GetDashboardSummary")
    Call<ResponseDashboardSummary> getDashboardSummary(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("DashboardReport/GetDashboardGraph")
    Call<ResponseDashBoardGraph> getDashboardGraph(@Header("Authorization") String Auth, @Body JsonObject jsonObject);


    @POST("DashboardReport/GetDashboardEmployeePieChart")
    Call<ResponseDashboardpiechart> getDashboardPiechart(@Header("Authorization") String Auth, @Body JsonObject jsonObject);


    @POST("DashboardReport/GetDashboardItemWiseSummary")
    Call<ResponseTop10ProductsSold> getProductsSold(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("DashboardReport/GetDashboardCategoryWiseSummary")
    Call<ResponseTopCategory> getTopSoldCategory(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("ItemRequest/CreateItemRequest")
    Call<List<ItemRequestEntry>> CreateItemRequest(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

    @POST("ItemRequest/UpdateItemRequest")
    Call<List<ItemRequestEntry>> UpdateItemRequest(@Header("Authorization") String Auth, @Body JsonObject jsonObject);


    @GET("ItemRequest/GetItemRequestHeader")
    Call<List<ResponseFetchPickList>> FetchPickList(@Header("Authorization") String Auth);


    @GET("ItemRequest/GetItemRequestDetails/{RequestNo}/{CustomerID}")
    Call<List<ResponseItemRequestDetails>> GetItemRequestDetailsbyRequestNo(@Header("Authorization") String Auth, @Path("RequestNo") String RequestNo, @Path("CustomerID") String CustomerID);

    @POST("ItemRequest/CloseItemRequest")
    Call<JsonObject> markpicklistCompleted(@Header("Authorization") String Auth, @Body JsonObject jsonObject);

}
