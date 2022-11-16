package com.acube.jims.presentation.Catalogue.View;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.BaseActivity;
import com.acube.jims.R;
import com.acube.jims.databinding.ActivityCatalogueSummaryBinding;
import com.acube.jims.datalayer.api.ResponseLiveCategory;
import com.acube.jims.datalayer.api.ResponseLiveSubCategory;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.presentation.Catalogue.adapter.MainCategoryAdapter;
import com.acube.jims.presentation.Catalogue.adapter.MainSubCategoryAdapter;
import com.acube.jims.utils.AppUtility;
import com.acube.jims.utils.FilterPreference;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.utils.PaginationScrollListener;
import com.acube.jims.databinding.ActivityCatalogueBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.Filter.Catresult;
import com.acube.jims.datalayer.models.Filter.Colorresult;
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;
import com.acube.jims.presentation.CartManagment.View.CartViewFragment;
import com.acube.jims.presentation.Catalogue.ViewModel.CatalogViewModel;
import com.acube.jims.presentation.Catalogue.ViewModel.CatalogViewModelNextPage;
import com.acube.jims.presentation.Catalogue.ViewModel.FilterViewModel;
import com.acube.jims.presentation.Catalogue.adapter.CatalogSummaryItemsAdapter;
import com.acube.jims.presentation.Catalogue.adapter.FilterListAdapter;
import com.acube.jims.presentation.Favorites.ViewModel.AddtoFavoritesViewModel;
import com.acube.jims.presentation.ProductDetails.View.ProductDetailsFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogueSummaryActivity extends BaseActivity implements CatalogSummaryItemsAdapter.replaceFregment, FilterScreen.ApplyFilter, CatalogSummaryItemsAdapter.AddtoFavorites, MainCategoryAdapter.FragmentTransition,MainSubCategoryAdapter.FragmentTransition {

    CatalogSummaryItemsAdapter adapter;
    GridLayoutManager gridLayoutManager;
    String vaSubCatID, vaCatID = "", vaKaratID = "", vaColorID = "", vaWeight = "", vapriceMax = "", vaPriceMin = "", vagender = "", vaWeightMin = "", vaWeightMax = "";
    ;

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
    private int TOTAL_PAGES = 0;
    private int currentPage = PAGE_START;
    AddtoFavoritesViewModel addtoFavoritesViewModel;


    ActivityCatalogueSummaryBinding binding;
    CatalogViewModel viewModel;
    CatalogViewModelNextPage catalogViewModelNextPage;
    FilterViewModel filterViewModel;
    PopupWindow mypopupWindow;
    String UserId;
    int GuestCustomerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_catalogue_summary);
        initToolBar(binding.toolbarApp, "Catalogue", true);
        binding.imvcart.setVisibility(View.VISIBLE);
        binding.imvfilter.setVisibility(View.GONE);
        binding.imvcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CartViewFragment.class));
            }
        });

        init();

        String Customername = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "GuestCustomerName");
        String CustomerCode = LocalPreferences.retrieveStringPreferences(getApplicationContext(), "GuestCustomerCode");
        binding.tvCustomername.setText("Customer : " + Customername + "  -  " + CustomerCode);
        binding.tvCustomercode.setText("Customer Code : " + CustomerCode);
        addtoFavoritesViewModel = new ViewModelProvider(this).get(AddtoFavoritesViewModel.class);
        GuestCustomerID = LocalPreferences.retrieveIntegerPreferences(getApplicationContext(), "GuestCustomerID");
        UserId = LocalPreferences.retrieveStringPreferences(getApplicationContext(), AppConstants.UserID);
        addtoFavoritesViewModel.init();
        binding.catrecyv.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));

        getCategory();
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

        boolean salesman=LocalPreferences.retrieveBooleanPreferences(getApplicationContext(),"salesman");
        if (salesman){
            binding.imvcart.setVisibility(View.INVISIBLE);
            binding.imvcart.setVisibility(View.INVISIBLE);
        }else{
            binding.imvcart.setVisibility(View.VISIBLE);
            binding.imvcart.setVisibility(View.VISIBLE);
        }
        binding.imvfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mypopupWindow.showAsDropDown(v, -153, 0);
                //  binding.parent.getForeground().setAlpha(100);

                FilterScreen bottomSheet = new FilterScreen(getApplicationContext()::getApplicationContext);

                bottomSheet.show(getSupportFragmentManager(),
                        "ModalBottomSheet");
                FilterPreference.storeBooleanPreference(getApplicationContext(), "enableEdit", true);

            }
        });


        binding.recyvcatalog.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                Log.d("New execution", "loadMoreItems: ");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //    loadNextPage();
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

        binding.bottomlayt.setVisibility(View.GONE);

        viewModel.getLiveDataSummary().observe(this, new Observer<List<ResponseCatalogueListing>>() {
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
        catalogViewModelNextPage.getLiveDataSummary().observe(this, new Observer<List<ResponseCatalogueListing>>() {
            @Override
            public void onChanged(List<ResponseCatalogueListing> responseCatalogueListings) {
                Log.d("getLiveDatanewPage", "onChanged: " + TOTAL_PAGES);
                hideProgressDialog();
                if (responseCatalogueListings != null && responseCatalogueListings.size() != 0) {
                    adapter.removeLoadingFooter();
                    isLoading = false;

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

        //  LoadFirstPage();
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
        adapter = new CatalogSummaryItemsAdapter(getApplicationContext(), CatalogueSummaryActivity.this, CatalogueSummaryActivity.this);
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
        viewModel.FetchCatalogSummary(AppConstants.Authorization + AuthToken, PAGE_START, AppConstants.Pagesize, vaCatID, vaSubCatID, vaColorID, vaKaratID, vaWeightMin, vaWeightMax, vaPriceMin, vapriceMax, vagender, GuestCustomerID,getApplicationContext());

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
        catalogViewModelNextPage.FetchCatalogCatalogueSummary(AppConstants.Authorization + AuthToken, currentPage, AppConstants.Pagesize, vaCatID, vaSubCatID, vaColorID, vaKaratID, vaWeightMin, vaWeightMax, vaPriceMin, vapriceMax, vagender, GuestCustomerID,getApplicationContext());

    }

    private void init() {


        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        catalogViewModelNextPage = new ViewModelProvider(this).get(CatalogViewModelNextPage.class);

        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        viewModel.init();
        catalogViewModelNextPage.init();
        filterViewModel.init();
        if (new AppUtility(CatalogueSummaryActivity.this).isTablet(getApplicationContext())) {
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
    public void replace(int Id) {
        Log.d(TAG, "replace: " + this.getClass().getSimpleName());
        startActivity(new Intent(getApplicationContext(), CatalogueActivity.class).putExtra("itemID", Id));


    }

    @Override
    public void replacestockout(String Id) {
        startActivity(new Intent(getApplicationContext(), ProductDetailsFragment.class).putExtra("Id", Id).putExtra("outofstock", true));

    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(getApplicationContext(), key, json);
    }


    @Override
    public void addtofav(String id, String serialno) {
        addtoFavoritesViewModel.AddtoFavorites(AppConstants.Authorization + AuthToken, String.valueOf(GuestCustomerID), UserId, id, "add", "", serialno,getApplicationContext());

    }

    @Override
    protected void onResume() {

        Log.d("Filterhceck", "onResume: ");
        super.onResume();
    }

    @Override
    public void applyfilter() {
        Toast.makeText(getApplicationContext(), "FilterApplied", Toast.LENGTH_SHORT).show();
        Log.d("TAG", "applyfilter: ");
        filterViewModel.FetchFilters(AppConstants.Authorization + AuthToken,getApplicationContext());
        LoadFirstPage();


    }

    private void getCategory() {

        RetrofitInstance.getApiService(getApplicationContext()).FetchLiveCategory(LocalPreferences.getToken(getApplicationContext())).enqueue(new Callback<List<ResponseLiveCategory>>() {
            @Override
            public void onResponse(Call<List<ResponseLiveCategory>> call, Response<List<ResponseLiveCategory>> response) {
                if (response.body() != null && response.code() == 200) {
                    List<ResponseLiveCategory> stores = response.body();
                    binding.catrecyv.setAdapter(new MainCategoryAdapter(getApplicationContext(), stores, CatalogueSummaryActivity.this));

                }

            }

            @Override
            public void onFailure(Call<List<ResponseLiveCategory>> call, Throwable t) {

            }
        });
    }

    @Override
    public void replaceFragment(int ID) {
        getSubcategory(ID);

    }

    private void getSubcategory(int id) {

            RetrofitInstance.getApiService(getApplicationContext()).FetchLiveSubCategory(LocalPreferences.getToken(getApplicationContext()), id).enqueue(new Callback<List<ResponseLiveSubCategory>>() {
                @Override
                public void onResponse(Call<List<ResponseLiveSubCategory>> call, Response<List<ResponseLiveSubCategory>> response) {
                    if (response.body() != null && response.code() == 200) {
                        List<ResponseLiveSubCategory> dataset = response.body();
                        binding.recyvcatalog.setAdapter(new MainSubCategoryAdapter(getApplicationContext(),dataset,CatalogueSummaryActivity.this));


                    }
                }

                @Override
                public void onFailure(Call<List<ResponseLiveSubCategory>> call, Throwable t) {

                }
            });
        }


    @Override
    public void passData(int ID) {
        startActivity(new Intent(getApplicationContext(), CatalogueActivity.class).putExtra("subcatID", ID));

    }

    @Override
    public void passlist(List<Integer> dataset) {

    }

    @Override
    public void scanaction(int docID, String toBeAuditedOn, String remark) {

    }
}
