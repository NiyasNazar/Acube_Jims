package com.acube.jims.presentation.ItemRequest.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.Utils.AppUtility;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.Utils.PaginationScrollListener;
import com.acube.jims.databinding.ActivityCatalogueBinding;
import com.acube.jims.databinding.ActivityItemreqDetailsBinding;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.Filter.Catresult;
import com.acube.jims.datalayer.models.Filter.Colorresult;
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;
import com.acube.jims.datalayer.models.ItemRequest.ResponseCreateItemrequest;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.datalayer.remote.dbmodel.CustomerHistory;
import com.acube.jims.datalayer.remote.dbmodel.ItemRequestEntry;
import com.acube.jims.presentation.CartManagment.View.CartViewFragment;
import com.acube.jims.presentation.Catalogue.View.FilterBottomSheetFragment;
import com.acube.jims.presentation.Catalogue.View.FilterScreen;
import com.acube.jims.presentation.Catalogue.ViewModel.CatalogViewModel;
import com.acube.jims.presentation.Catalogue.ViewModel.CatalogViewModelNextPage;
import com.acube.jims.presentation.Catalogue.ViewModel.FilterViewModel;
import com.acube.jims.presentation.Catalogue.adapter.FilterListAdapter;
import com.acube.jims.presentation.Compare.CompareFragment;
import com.acube.jims.presentation.Favorites.ViewModel.AddtoFavoritesViewModel;
import com.acube.jims.presentation.ItemRequest.adapter.ItemRequestAdapter;
import com.acube.jims.presentation.PdfGeneration.ShareScannedItems;
import com.acube.jims.presentation.ProductDetails.View.ProductDetailsFragment;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemRequestDetailActivity extends BaseActivity implements ItemRequestAdapter.replaceFregment, FilterBottomSheetFragment.ApplyFilter, ItemRequestAdapter.AddtoList {

    ItemRequestAdapter adapter;
    GridLayoutManager gridLayoutManager;
    String vaSubCatID, vaCatID = "", vaKaratID = "", vaColorID = "", vaWeight = "", vapriceMax = "", vaPriceMin = "", vagender = "", vaWeightMin = "", vaWeightMax = "";

    String AuthToken;


    RecyclerView expandableListView, recyclerViewKarat, recyclerViewColor;
    FilterListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    List<Catresult> catresult;
    List<Karatresult> karatresults;
    List<Colorresult> colorresults;
    HashMap<String, List<String>> expandableListDetail;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    AddtoFavoritesViewModel addtoFavoritesViewModel;
    ActivityItemreqDetailsBinding binding;
    CatalogViewModel viewModel;
    CatalogViewModelNextPage catalogViewModelNextPage;
    FilterViewModel filterViewModel;
    PopupWindow mypopupWindow;
    String UserId, currentDateandTime;
    int itemID, GuestCustomerID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_itemreq_details);
        initToolBar(binding.toolbarApp, "Item Request", true);
        binding.imvcart.setVisibility(View.VISIBLE);

        binding.imvcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CartViewFragment.class));
            }
        });

        binding.requestItemlayt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(ItemRequestDetailActivity.this);
                builder1.setMessage("Are you sure you want request selected items?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                PerformItemrequest();
                            }
                        });

                builder1.setNegativeButton(
                        "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        currentDateandTime = sdf.format(new Date());
        init();

        String Customername = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "GuestCustomerName");
        String CustomerCode = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "GuestCustomerCode");
        binding.tvCustomername.setText("Customer : " + Customername + "  -  " + CustomerCode);
        binding.tvCustomercode.setText("Customer Code : " + CustomerCode);
        addtoFavoritesViewModel = new ViewModelProvider(this).get(AddtoFavoritesViewModel.class);
        GuestCustomerID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        UserId = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.UserID);
        addtoFavoritesViewModel.init();
        itemID = getIntent().getIntExtra("itemID", 0);


        binding.selectlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.selectAll();
                adapter.notifyDataSetChanged();
            }
        });
        binding.imvfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterScreen bottomSheet = new FilterScreen(getApplicationContext()::getApplicationContext);
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");
                FilterPreference.storeBooleanPreference(getApplicationContext(), "enableEdit", false);

            }
        });

        binding.recyvcatalog.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
        AuthToken = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.Token);
        LoadFirstPage();
        viewModel.getLiveData().observe(this, new Observer<List<ResponseCatalogueListing>>() {
            @Override
            public void onChanged(List<ResponseCatalogueListing> responseCatalogueListings) {
                hideProgressDialog();
                if (responseCatalogueListings != null && responseCatalogueListings.size() != 0) {
                    /* binding.recyvcatalog.setAdapter(new CatalogItemAdapter(getActivity(), responseCatalogueListings));*/
                    TOTAL_PAGES = getTotalPagesFromTotalResult(responseCatalogueListings.get(0).getTotalCount(), AppConstants.Pagesize);
                    Log.d("onChanged", "onChanged: " + TOTAL_PAGES);
                    adapter.addAll(responseCatalogueListings);
                    if (currentPage < TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;

                }

            }
        });
        catalogViewModelNextPage.getLiveData().observe(this, new Observer<List<ResponseCatalogueListing>>() {
            @Override
            public void onChanged(List<ResponseCatalogueListing> responseCatalogueListings) {
                hideProgressDialog();
                if (responseCatalogueListings != null && responseCatalogueListings.size() != 0) {
                    /* binding.recyvcatalog.setAdapter(new CatalogItemAdapter(getActivity(), responseCatalogueListings));*/
                    adapter.removeLoadingFooter();
                    isLoading = false;
                    Log.d("getLiveDatanewPage", "onChanged: " + TOTAL_PAGES);
                    adapter.addAll(responseCatalogueListings);
                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;

                }

            }
        });
        filterViewModel.getLiveData().observe(this, new Observer<List<ResponseFetchFilters>>() {
            @Override
            public void onChanged(List<ResponseFetchFilters> responseFetchFilters) {
                setList("catresult", responseFetchFilters);


            }
        });
        /*filterViewModel.getLiveData().observe(getActivity(), new Observer<ResponseFetchFilters>() {
            @Override
            public void onChanged(ResponseFetchFilters responseFetchFilters) {
                if (responseFetchFilters != null) {
                    catresult = new ArrayList<>();
                    karatresults = new ArrayList<>();
                    colorresults = new ArrayList<>();
                    catresult = responseFetchFilters.getCatresult();
                    karatresults = responseFetchFilters.getKaratresult();
                    colorresults = responseFetchFilters.getColorresult();

                    setList("catresult", catresult);
                    setList("karatresults", karatresults);
                    setList("colorresults", colorresults);
                    //  expandableListView.setAdapter(new FilterParentAdapter(getActivity(), catresult));
                    //  recyclerViewKarat.setAdapter(new FilterKaratAdapter(getActivity(), karatresults));
                    //   recyclerViewColor.setAdapter(new FilterColorAdapter(getActivity(), colorresults));

                }
            }
        });*/
        filterViewModel.FetchFilters(AppConstants.Authorization + AuthToken);
        //setPopUpWindow();


    }

    private void PerformItemrequest() {
        showProgressDialog();
        int trayID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "TrayID");
        int deviceID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "DeviceID");
        DatabaseClient.getInstance(this).getAppDatabase().itemRequestDao().gettall().observe(this, new Observer<List<ItemRequestEntry>>() {
            @Override
            public void onChanged(List<ItemRequestEntry> itemRequestEntries) {
                if (itemRequestEntries != null && itemRequestEntries.size() != 0) {
                    /*"itemRequestNo": "string",
                            "customerID": 0,
                            "viewedOn": "datetime",
                            "employeeID": 0,
                            "trayID": 0,
                            "deviceID": 0,*/
                    String itemRequestNo = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "itemRequestNo");

                    JsonObject jsonobject = new JsonObject();
                    jsonobject.addProperty("itemRequestNo", itemRequestNo);
                    jsonobject.addProperty("customerID", GuestCustomerID);
                    jsonobject.addProperty("viewedOn", currentDateandTime);
                    jsonobject.addProperty("employeeID", UserId);
                    jsonobject.addProperty("trayID", trayID);
                    jsonobject.addProperty("deviceID", deviceID);
                    JsonArray jsonArray = new JsonArray();
                    JsonObject itemobject;
                    for (int i = 0; i < itemRequestEntries.size(); i++) {
                        itemobject = new JsonObject();
                        itemobject.addProperty("itemID", itemRequestEntries.get(i).getItemID());
                        itemobject.addProperty("serialNumber", itemRequestEntries.get(i).getSerialNumber());
                        itemobject.addProperty("deviceID", deviceID);
                        itemobject.addProperty("viewedOn", itemRequestEntries.get(i).getViewedOn());
                        jsonArray.add(itemobject);
                    }
                    jsonobject.add("itemRequestSubs", jsonArray);

                    if (itemRequestNo.equalsIgnoreCase("")) {
                        CreatePicklist(jsonobject);
                    } else {
                        UpdatePicklist(jsonobject);
                    }


                }
            }
        });

    }

    private void UpdatePicklist(JsonObject jsonobject) {
        RetrofitInstance.getApiService().UpdateItemRequest(LocalPreferences.getToken(getApplicationContext()), jsonobject)
                .enqueue(new Callback<List<ItemRequestEntry>>() {
                    @Override
                    public void onResponse(Call<List<ItemRequestEntry>> call, Response<List<ItemRequestEntry>> response) {
                        hideProgressDialog();
                        if (response.body() != null && response.code() == 200) {
                            List<ItemRequestEntry> dataset = response.body();
                            if (dataset.size() > 0) {
                                String itemRequestNo = dataset.get(0).getItemRequestNo();
                                LocalPreferences.storeStringPreference(getApplicationContext(), "itemRequestNo", itemRequestNo);
                            }
                            DeleteALL(dataset);
                            customSnackBar(binding.parent, "Success");

                        } else {
                            customSnackBar(binding.parent, "Failed");
                        }

                    }

                    @Override
                    public void onFailure(Call<List<ItemRequestEntry>> call, Throwable t) {
                        hideProgressDialog();
                        customSnackBar(binding.parent, "Failed");

                    }
                });

    }

    private void CreatePicklist(JsonObject jsonobject) {

        RetrofitInstance.getApiService().CreateItemRequest(LocalPreferences.getToken(getApplicationContext()), jsonobject)
                .enqueue(new Callback<List<ItemRequestEntry>>() {
                    @Override
                    public void onResponse(Call<List<ItemRequestEntry>> call, Response<List<ItemRequestEntry>> response) {
                        hideProgressDialog();
                        if (response.body() != null && response.code() == 200) {
                            List<ItemRequestEntry> dataset = response.body();
                            if (dataset.size() > 0) {
                                String itemRequestNo = dataset.get(0).getItemRequestNo();
                                LocalPreferences.storeStringPreference(getApplicationContext(), "itemRequestNo", itemRequestNo);
                            }
                            DeleteALL(dataset);
                            customSnackBar(binding.parent, "Success");

                        } else {
                            customSnackBar(binding.parent, "Failed");
                        }

                    }

                    @Override
                    public void onFailure(Call<List<ItemRequestEntry>> call, Throwable t) {
                        hideProgressDialog();
                        customSnackBar(binding.parent, "Failed");

                    }
                });
    }

    private void LoadFirstPage() {
        showProgressDialog();
        adapter = new ItemRequestAdapter(getApplicationContext(), ItemRequestDetailActivity.this, ItemRequestDetailActivity.this);
        binding.recyvcatalog.setAdapter(adapter);
        vaSubCatID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "subcatid");
        vaColorID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "colorid");
        vaKaratID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "karatid");
        vaCatID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "catid");
        vaWeightMin = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MinWeight");
        vaWeightMax = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MaxWeight");
        vaPriceMin = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MinValue");
        vapriceMax = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MaxValue");
        Log.d(TAG, "LoadFirstPage: " + vaSubCatID);
        vagender = FilterPreference.retrieveStringPreferences(getApplicationContext(), "gender");
        viewModel.FetchCatalog(AppConstants.Authorization + AuthToken, PAGE_START, AppConstants.Pagesize, vaCatID, vaSubCatID, vaColorID, vaKaratID, vaWeightMin, vaWeightMax, vaPriceMin, vapriceMax, vagender, itemID, GuestCustomerID);

    }

    private int getTotalPagesFromTotalResult(Integer totalCount, Integer pagesize) {
        int totalPages_pre = (totalCount / pagesize);
        return (totalCount % pagesize) == 0 ? totalPages_pre : totalPages_pre + 1;
    }

    private void loadNextPage() {
        vaCatID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "catid");
        vaSubCatID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "subcatid");
        vaColorID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "colorid");
        vaKaratID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "karatid");
        vaWeightMin = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MinWeight");
        vaWeightMax = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MaxWeight");
        vaPriceMin = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MinValue");
        vapriceMax = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MaxValue");
        vagender = FilterPreference.retrieveStringPreferences(getApplicationContext(), "gender");
        catalogViewModelNextPage.FetchCatalog(AppConstants.Authorization + AuthToken, currentPage, AppConstants.Pagesize, vaCatID, vaSubCatID, vaColorID, vaKaratID, vaWeightMin, vaWeightMax, vaPriceMin, vapriceMax, vagender, itemID, GuestCustomerID);

    }

    private void init() {


        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        catalogViewModelNextPage = new ViewModelProvider(this).get(CatalogViewModelNextPage.class);

        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        viewModel.init();
        catalogViewModelNextPage.init();
        filterViewModel.init();
        if (new AppUtility(ItemRequestDetailActivity.this).isTablet(getApplicationContext())) {
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
            // binding.recyvcatalog.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            binding.recyvcatalog.setLayoutManager(gridLayoutManager);

        } else {
            gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            binding.recyvcatalog.setLayoutManager(gridLayoutManager);

            //binding.recyvcatalog.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }


    }


    @Override
    public void replace(String Id) {
        Log.d(TAG, "replace: " + this.getClass().getSimpleName());
        startActivity(new Intent(getApplicationContext(), ProductDetailsFragment.class).putExtra("Id", Id));


    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(getApplicationContext(), key, json);
    }

    @Override
    public void applyfilter() {
        Toast.makeText(getApplicationContext(), "FilterApplied", Toast.LENGTH_SHORT).show();
        filterViewModel.FetchFilters(AppConstants.Authorization + AuthToken);

        LoadFirstPage();

    }


    public void showPopupWindow(final View view) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pop_up_layout_smarttool, null);
        CardView compare = alertLayout.findViewById(R.id.cdvcompare);
        CardView cdvshare = alertLayout.findViewById(R.id.cdvshare);



        //  final TextInputEditText etPassword = alertLayout.findViewById(R.id.tiet_password);


        AlertDialog.Builder alert = new AlertDialog.Builder(ItemRequestDetailActivity.this);
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    comparelist = new ArrayList<>();
                for (int i = 0; i < dataset.size(); i++) {
                    comparelist.add(dataset.get(i).getSerialNumber());
                }
                setList("compare", comparelist);
                dialog.dismiss();*/
                startActivity(new Intent(getApplicationContext(), CompareFragment.class));

            }
        });
        cdvshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ShareScannedItems.class));
                dialog.dismiss();
            }
        });

    }

    @Override
    public void addtopicklist(int itemID, String serialNumber, int Id) {
        int deviceID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "DeviceID");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        ItemRequestEntry itemRequestEntry = new ItemRequestEntry();
        itemRequestEntry.setItemID(itemID);
        itemRequestEntry.setDeviceID(deviceID);
        itemRequestEntry.setSerialNumber(serialNumber);
        itemRequestEntry.setViewedOn(currentDateandTime);
        InsertItemRequest(itemRequestEntry);
    }

    @Override
    public void removefrompicklist(int itemID) {
        Delete(itemID);
    }

    private void InsertItemRequest(ItemRequestEntry itemRequestEntry) {
        class SaveItemRequest extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .itemRequestDao()
                        .insert(itemRequestEntry);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }

        SaveItemRequest st = new SaveItemRequest();
        st.execute();
    }

    private void Delete(int itemID) {
        class DeleteItemRequest extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .itemRequestDao()
                        .delete(itemID);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }

        DeleteItemRequest st = new DeleteItemRequest();
        st.execute();
    }

    private void InsertItemRequestFromResponse(List<ItemRequestEntry> itemRequestEntry) {
        class SaveItemRequest extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .itemRequestDao()
                        .insert(itemRequestEntry);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

            }
        }

        SaveItemRequest st = new SaveItemRequest();
        st.execute();
    }

    private void DeleteALL(List<ItemRequestEntry> itemRequestEntry) {
        class SaveItemRequest extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .itemRequestDao()
                        .DeleteAll();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                InsertItemRequestFromResponse(itemRequestEntry);

            }
        }

        SaveItemRequest st = new SaveItemRequest();
        st.execute();
    }

}
