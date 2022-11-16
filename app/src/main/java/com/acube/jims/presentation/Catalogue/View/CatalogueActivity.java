package com.acube.jims.presentation.Catalogue.View;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.acube.jims.BaseActivity;
import com.acube.jims.datalayer.models.Cart.ResponseAddtoCart;
import com.acube.jims.datalayer.models.SelectionHolder;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.datalayer.remote.dbmodel.Smarttool;
import com.acube.jims.presentation.CartManagment.View.CartViewFragment;
import com.acube.jims.presentation.CartManagment.ViewModel.AddtoCartViewModel;
import com.acube.jims.presentation.Catalogue.ViewModel.CatalogViewModel;
import com.acube.jims.presentation.Catalogue.ViewModel.CatalogViewModelNextPage;
import com.acube.jims.presentation.Catalogue.ViewModel.FilterViewModel;
import com.acube.jims.presentation.Catalogue.adapter.CatalogItemsAdapter;
import com.acube.jims.presentation.Catalogue.adapter.FilterListAdapter;
import com.acube.jims.presentation.Compare.CompareFragment;
import com.acube.jims.presentation.Favorites.View.Favorites;
import com.acube.jims.presentation.Favorites.ViewModel.AddtoFavoritesViewModel;
import com.acube.jims.presentation.PdfGeneration.ShareScannedItems;
import com.acube.jims.presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.R;
import com.acube.jims.presentation.Quotation.InvoiceFragment;
import com.acube.jims.presentation.Quotation.SaleFragment;
import com.acube.jims.utils.AppUtility;
import com.acube.jims.utils.FilterPreference;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.OnSingleClickListener;
import com.acube.jims.utils.PaginationScrollListener;
import com.acube.jims.databinding.ActivityCatalogueBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.Filter.Catresult;
import com.acube.jims.datalayer.models.Filter.Colorresult;
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

public class CatalogueActivity extends BaseActivity implements CatalogItemsAdapter.replaceFregment, FilterBottomSheetFragment.ApplyFilter, CatalogItemsAdapter.AddtoFavorites {
    AddtoCartViewModel addtoCartViewModel;

    CatalogItemsAdapter adapter;
    GridLayoutManager gridLayoutManager;
    String vaSubCatID, vaCatID = "", vaKaratID = "", vaColorID = "", vaWeight = "", vapriceMax = "", vaPriceMin = "", vagender = "", vaWeightMin = "", vaWeightMax = "";

    String AuthToken;
    String CartId = "";


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
    private int TOTAL_PAGES = 0;
    private int currentPage = PAGE_START;
    AddtoFavoritesViewModel addtoFavoritesViewModel;
    ActivityCatalogueBinding binding;
    CatalogViewModel viewModel;
    CatalogViewModelNextPage catalogViewModelNextPage;
    FilterViewModel filterViewModel;
    PopupWindow mypopupWindow;
    String UserId;
    int subcatID;
    int GuestCustomerID;
    int warehouseId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_catalogue);
        initToolBar(binding.toolbarApp, "Catalogue", true);
        binding.imvcart.setVisibility(View.VISIBLE);
        binding.imvcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CartViewFragment.class));
            }
        });

        init();
        boolean salesman = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "salesman");
        if (salesman) {
            binding.imvcart.setVisibility(View.INVISIBLE);
            binding.imvcart.setVisibility(View.INVISIBLE);
        } else {
            binding.imvcart.setVisibility(View.VISIBLE);
            binding.imvcart.setVisibility(View.VISIBLE);
        }
        addtoCartViewModel = new ViewModelProvider(this).get(AddtoCartViewModel.class);
        addtoCartViewModel.init();
        String Customername = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "GuestCustomerName");
        String CustomerCode = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "GuestCustomerCode");
        binding.tvCustomername.setText("Customer : " + Customername + "  -  " + CustomerCode);
        binding.tvCustomercode.setText("Customer Code : " + CustomerCode);
        addtoFavoritesViewModel = new ViewModelProvider(this).get(AddtoFavoritesViewModel.class);
        GuestCustomerID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        UserId = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.UserID);
        addtoFavoritesViewModel.init();
        subcatID = getIntent().getIntExtra("subcatID", 0);

        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().scannedItemsDao().deleteall();
        }).start();

        FilterPreference.clearPreferences(getApplicationContext());
        new Thread(() -> {
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().homeMenuDao().updatefilterstore(0);
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().homeMenuDao().updateColor(0);
            DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().homeMenuDao().updateKaratresult(0);


        }).start();

        // This callback will only be called when MyFragment is at least Started.
      /* OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Toast.makeText(getActivity(),"OnBackPressed",Toast.LENGTH_LONG).show();
                FragmentHelper.replaceFragment(getActivity(), R.id.content, new HomeFragment());


            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(),callback);*/

        //requireActivity().getOnBackPressedDispatcher().addCallback(this,callback);

        binding.layoutSmarttool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(() -> {
                    int count = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().scannedItemsDao().getallcount();
                    runOnUiThread(() -> {
                        if (count > 1) {
                            showPopupWindow(v);

                        } else {


                            showerror("Please select one or more item before proceeding.");
                        }

                    });
                }).start();
            }
        });
        binding.imvfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mypopupWindow.showAsDropDown(v, -153, 0);
                //  binding.parent.getForeground().setAlpha(100);

                FilterBottomSheetFragment bottomSheet = new FilterBottomSheetFragment(getApplicationContext()::getApplicationContext);
                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");
                FilterPreference.storeBooleanPreference(getApplicationContext(), "enableEdit", false);

            }
        });
        addtoCartViewModel.getLiveData().observe(this, new Observer<ResponseAddtoCart>() {
            @Override
            public void onChanged(ResponseAddtoCart responseAddtoCart) {
                hideProgressDialog();
                if (responseAddtoCart != null) {
                    Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                    LocalPreferences.storeStringPreference(getApplicationContext(), AppConstants.CartID, responseAddtoCart.getCartListNo());
                }
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
                if (adapter.getItemCount() == 0) {
                    binding.bottomlayt.setVisibility(View.GONE);
                } else {
                    binding.bottomlayt.setVisibility(View.VISIBLE);
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

                new Thread(() -> {
                    for (int i = 0; i < responseFetchFilters.size(); i++) {

                        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                .homeMenuDao()
                                .insertkaratresults(responseFetchFilters.get(i).getKarats());
                        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                .homeMenuDao()
                                .insertfilterStores(responseFetchFilters.get(i).getStoreList());
                        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                .homeMenuDao()
                                .insertcolorresults(responseFetchFilters.get(i).getColors());
                        DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                                .homeMenuDao()
                                .insertweights(responseFetchFilters.get(i).getWeights());


                    }


                }).start();


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
        filterViewModel.FetchFilters(AppConstants.Authorization + AuthToken,getApplicationContext());
        //setPopUpWindow();


    }

    private void LoadFirstPage() {
        showProgressDialog();
        adapter = new CatalogItemsAdapter(getApplicationContext(), CatalogueActivity.this, CatalogueActivity.this);
        binding.recyvcatalog.setAdapter(adapter);
        warehouseId = FilterPreference.retrieveIntegerPreferences(getApplicationContext(), "warehouseId");
        vaColorID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "colorid");
        vaKaratID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "karatid");
        vaCatID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "catid");
        vaWeightMin = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MinWeight");
        vaWeightMax = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MaxWeight");
        vaPriceMin = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MinValue");
        vapriceMax = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MaxValue");
        Log.d(TAG, "LoadFirstPage: " + vaSubCatID);
        vagender = FilterPreference.retrieveStringPreferences(getApplicationContext(), "gender");
        viewModel.FetchCatalog(AppConstants.Authorization + AuthToken, PAGE_START, AppConstants.Pagesize, vaCatID, vaSubCatID, vaColorID, vaKaratID, vaWeightMin, vaWeightMax, vaPriceMin, vapriceMax, vagender, subcatID, GuestCustomerID, warehouseId,getApplicationContext());

    }

    private int getTotalPagesFromTotalResult(Integer totalCount, Integer pagesize) {
        int totalPages_pre = (totalCount / pagesize);
        return (totalCount % pagesize) == 0 ? totalPages_pre : totalPages_pre + 1;
    }

    private void loadNextPage() {
        vaCatID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "catid");
        warehouseId = FilterPreference.retrieveIntegerPreferences(getApplicationContext(), "warehouseId");
        vaColorID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "colorid");
        vaKaratID = FilterPreference.retrieveStringPreferences(getApplicationContext(), "karatid");
        vaWeightMin = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MinWeight");
        vaWeightMax = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MaxWeight");
        vaPriceMin = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MinValue");
        vapriceMax = FilterPreference.retrieveStringPreferences(getApplicationContext(), "MaxValue");
        vagender = FilterPreference.retrieveStringPreferences(getApplicationContext(), "gender");
        catalogViewModelNextPage.FetchCatalog(AppConstants.Authorization + AuthToken, currentPage, AppConstants.Pagesize, vaCatID, vaSubCatID, vaColorID, vaKaratID, vaWeightMin, vaWeightMax, vaPriceMin, vapriceMax, vagender, subcatID, GuestCustomerID, warehouseId,getApplicationContext());

    }

    private void init() {


        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        catalogViewModelNextPage = new ViewModelProvider(this).get(CatalogViewModelNextPage.class);
        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        viewModel.init();
        catalogViewModelNextPage.init();
        filterViewModel.init();
        if (new AppUtility(CatalogueActivity.this).isTablet(getApplicationContext())) {
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

    @Override
    public void compareitems(String ID, String serial, boolean ischecked) {
        Smarttool smarttool = new Smarttool();
        smarttool.setSerialNumber(serial);
        smarttool.setID(ID);
        new Thread(() -> {
            if (ischecked) {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().scannedItemsDao().insertCheckboxSelection(smarttool);
            } else {
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().scannedItemsDao().DeleteCheckboxSelection(smarttool);
            }


        }).start();


    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(getApplicationContext(), key, json);
    }

    @Override
    public void applyfilter() {
        Toast.makeText(getApplicationContext(), "FilterApplied", Toast.LENGTH_SHORT).show();
        /*   filterViewModel.FetchFilters(AppConstants.Authorization + AuthToken);*/
        LoadFirstPage();

    }

    @Override
    public void addtofav(String id, String serialno) {
        addtoFavoritesViewModel.AddtoFavorites(AppConstants.Authorization + AuthToken, String.valueOf(GuestCustomerID), UserId, id, "add", "", serialno,getApplicationContext());

    }

    public void showPopupWindow(final View view) {

        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pop_up_layout_smarttool, null);
        CardView cdvfavorites = alertLayout.findViewById(R.id.cdvfavorites);
        CardView compare = alertLayout.findViewById(R.id.cdvcompare);
        CardView cdvshare = alertLayout.findViewById(R.id.cdvshare);
        CardView cdvsale = alertLayout.findViewById(R.id.cdvsale);
        CardView cdvquote = alertLayout.findViewById(R.id.cdvquote);
        CardView addtoCart = alertLayout.findViewById(R.id.cdvaddtocart);
        boolean salesman = LocalPreferences.retrieveBooleanPreferences(getApplicationContext(), "salesman");


        //  final TextInputEditText etPassword = alertLayout.findViewById(R.id.tiet_password);


        AlertDialog.Builder alert = new AlertDialog.Builder(CatalogueActivity.this);
        alert.setTitle("");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);

        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        compare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    int count = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().scannedItemsDao().getallcount();
                    runOnUiThread(() -> {
                        if (count > 1) {
                            dialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), CompareFragment.class));

                        } else {

                            dialog.dismiss();
                            showerror("Please add more than one item for comparing.");
                        }

                    });
                }).start();


            }
        });
        cdvshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(), ShareScannedItems.class));
                dialog.dismiss();
            }
        });
        cdvfavorites.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!salesman) {
                    if (GuestCustomerID != 0) {
                        updateFavorites();
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        showerror("Favorites only available  on customer login");
                    }
                } else {
                    showerror("Favorites only available  on customer login");
                }


            }
        });


        addtoCart.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!salesman) {
                    if (GuestCustomerID != 0) {
                        updateCart();
                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        showerror("Shopping cart on customer login");
                    }
                }else{
                    showerror("Shopping cart only available  on customer login");
                }


            }
        });
        cdvsale.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                new Thread(() -> {
                    int count = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().scannedItemsDao().getallcount();
                    runOnUiThread(() -> {
                        if (count > 1) {
                            dialog.dismiss();
                            startActivity(new Intent(getApplicationContext(), SaleFragment.class));

                        } else {

                            dialog.dismiss();
                            showerror("Please select one or more item before proceeding.");
                        }

                    });
                }).start();


            }
        });
        cdvquote.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startActivity(new Intent(getApplicationContext(), InvoiceFragment.class));
            }
        });

    }

    private void updateFavorites() {
        DatabaseClient.getInstance(CatalogueActivity.this).getAppDatabase().scannedItemsDao().getFromSmarttoolList().observe(this, new Observer<List<Smarttool>>() {
            @Override
            public void onChanged(List<Smarttool> strings) {
                JsonArray jsonArray = new JsonArray();
                for (int i = 0; i < strings.size(); i++) {
                    addtoFavoritesViewModel.AddtoFavorites(AppConstants.Authorization + AuthToken, String.valueOf(GuestCustomerID), UserId, strings.get(i).getID(), "add", "", strings.get(i).getSerialNumber(),getApplicationContext());


                }
            }
        });
    }

    private void updateCart() {
        DatabaseClient.getInstance(CatalogueActivity.this).getAppDatabase().scannedItemsDao().getFromSmarttoolList().observe(this, new Observer<List<Smarttool>>() {
            @Override
            public void onChanged(List<Smarttool> strings) {
                JsonArray jsonArray = new JsonArray();
                for (int i = 0; i < strings.size(); i++) {

                    JsonObject items = new JsonObject();
                    items.addProperty("cartListNo", CartId);
                    items.addProperty("customerID", GuestCustomerID);
                    items.addProperty("employeeID", UserId);
                    items.addProperty("serialNumber", strings.get(i).getSerialNumber());
                    items.addProperty("itemID", strings.get(i).getID());
                    items.addProperty("qty", 0);
                    jsonArray.add(items);
                }
                addtoCartViewModel.AddtoCart(AppConstants.Authorization + AuthToken, "add", jsonArray,getApplicationContext());
            }
        });


    }

}
