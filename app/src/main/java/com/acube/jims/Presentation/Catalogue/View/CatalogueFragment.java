package com.acube.jims.Presentation.Catalogue.View;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.Catalogue.ViewModel.CatalogViewModel;
import com.acube.jims.Presentation.Catalogue.ViewModel.CatalogViewModelNextPage;
import com.acube.jims.Presentation.Catalogue.ViewModel.FilterViewModel;
import com.acube.jims.Presentation.Catalogue.adapter.CatalogItemsAdapter;
import com.acube.jims.Presentation.Catalogue.adapter.FilterListAdapter;
import com.acube.jims.Presentation.Favorites.ViewModel.AddtoFavoritesViewModel;
import com.acube.jims.Presentation.Filters.View.CategoryFilterFragment;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.Presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.AppUtility;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.Utils.PaginationScrollListener;
import com.acube.jims.databinding.FragmentCatalogueBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.Filter.Catresult;
import com.acube.jims.datalayer.models.Filter.Colorresult;
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;


public class CatalogueFragment extends BaseFragment implements CatalogItemsAdapter.replaceFregment, FilterBottomSheetFragment.ApplyFilter, CatalogItemsAdapter.AddtoFavorites {


    CatalogItemsAdapter adapter;
    GridLayoutManager gridLayoutManager;
    String vaSubCatID, vaCatID = "", vaKaratID = "", vaColorID = "", vaWeight = "", vapriceMax = "", vaPriceMin = "", vagender = "";

    String AuthToken;

    public CatalogueFragment() {
        // Required empty public constructor
    }

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


    FragmentCatalogueBinding binding;
    CatalogViewModel viewModel;
    CatalogViewModelNextPage catalogViewModelNextPage;
    FilterViewModel filterViewModel;
    PopupWindow mypopupWindow;
    String GuestCustomerID,UserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_catalogue, container, false);
        binding.parent.setForeground(getResources().getDrawable(R.drawable.shape_window_dim));
        binding.parent.getForeground().setAlpha(0);
        binding.toolbar.tvFragname.setText("Catalogue");
        binding.toolbar.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /// FragmentHelper.replaceFragment(getActivity(), R.id.content, new HomeFragment());
              // getsuppo
                getActivity().onBackPressed();
            }
        });
        init();

        String Customername = LocalPreferences.retrieveStringPreferences(getContext(), "GuestCustomerName");
        String CustomerCode = LocalPreferences.retrieveStringPreferences(getContext(), "GuestCustomerCode");
        binding.tvCustomername.setText("Customer : " + Customername+"  -  "+CustomerCode);
        binding.tvCustomercode.setText("Customer Code : " + CustomerCode);
        addtoFavoritesViewModel = new ViewModelProvider(this).get(AddtoFavoritesViewModel.class);
        GuestCustomerID = LocalPreferences.retrieveStringPreferences(getActivity(), "GuestCustomerID");
        UserId = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.UserID);
        addtoFavoritesViewModel.init();

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
        binding.laytfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  mypopupWindow.showAsDropDown(v, -153, 0);
                //  binding.parent.getForeground().setAlpha(100);

                FilterBottomSheetFragment bottomSheet = new FilterBottomSheetFragment(CatalogueFragment.this::applyfilter);

                bottomSheet.show(getActivity().getSupportFragmentManager(),
                        "ModalBottomSheet");

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
        AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token);
        LoadFirstPage();
        viewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseCatalogueListing>>() {
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
        catalogViewModelNextPage.getLiveData().observe(getActivity(), new Observer<List<ResponseCatalogueListing>>() {
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
        filterViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseFetchFilters>>() {
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
        return binding.getRoot();


    }

    private void LoadFirstPage() {
        showProgressDialog();
        adapter = new CatalogItemsAdapter(getActivity(), CatalogueFragment.this,CatalogueFragment.this);
        binding.recyvcatalog.setAdapter(adapter);
        vaSubCatID = FilterPreference.retrieveStringPreferences(getActivity(), "subcatid");
        vaColorID = FilterPreference.retrieveStringPreferences(getContext(), "colorid");
        vaKaratID = FilterPreference.retrieveStringPreferences(getContext(), "karatid");
        vaCatID = FilterPreference.retrieveStringPreferences(getActivity(), "catid");
        vaWeight = FilterPreference.retrieveStringPreferences(getActivity(), "weightid");
        vaPriceMin = FilterPreference.retrieveStringPreferences(getActivity(), "MinValue");
        vapriceMax = FilterPreference.retrieveStringPreferences(getActivity(), "MaxValue");
        Log.d(TAG, "LoadFirstPage: " + vaSubCatID);
        vagender = FilterPreference.retrieveStringPreferences(getActivity(), "gender");
        viewModel.FetchCatalog(AppConstants.Authorization + AuthToken, PAGE_START, AppConstants.Pagesize, vaCatID, vaSubCatID, vaColorID, vaKaratID, vaWeight, vaPriceMin, vapriceMax, vagender);

    }

    private int getTotalPagesFromTotalResult(Integer totalCount, Integer pagesize) {
        int totalPages_pre = (totalCount / pagesize);
        return (totalCount % pagesize) == 0 ? totalPages_pre : totalPages_pre + 1;
    }

    private void loadNextPage() {
        vaCatID = FilterPreference.retrieveStringPreferences(getActivity(), "catid");
        vaSubCatID = FilterPreference.retrieveStringPreferences(getActivity(), "subcatid");
        vaColorID = FilterPreference.retrieveStringPreferences(getContext(), "colorid");
        vaKaratID = FilterPreference.retrieveStringPreferences(getContext(), "karatid");
        vaWeight = FilterPreference.retrieveStringPreferences(getActivity(), "weightid");
        vaPriceMin = FilterPreference.retrieveStringPreferences(getActivity(), "MinValue");
        vapriceMax = FilterPreference.retrieveStringPreferences(getActivity(), "MaxValue");
        vagender = FilterPreference.retrieveStringPreferences(getActivity(), "gender");

        catalogViewModelNextPage.FetchCatalog(AppConstants.Authorization + AuthToken, currentPage, AppConstants.Pagesize, vaCatID, vaSubCatID, vaColorID, vaKaratID, vaWeight, vaPriceMin, vapriceMax, vagender);

    }

    private void init() {


        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        catalogViewModelNextPage = new ViewModelProvider(this).get(CatalogViewModelNextPage.class);

        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        viewModel.init();
        catalogViewModelNextPage.init();
        filterViewModel.init();
        if (new AppUtility(getActivity()).isTablet(getActivity())) {
            gridLayoutManager = new GridLayoutManager(getActivity(), 3);
            // binding.recyvcatalog.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            binding.recyvcatalog.setLayoutManager(gridLayoutManager);

        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            binding.recyvcatalog.setLayoutManager(gridLayoutManager);

            //binding.recyvcatalog.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }


    }


    private void setPopUpWindow() {
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.filter_layout, null);
        ImageView morecat = view.findViewById(R.id.more_cat);
        ImageView morekarat = view.findViewById(R.id.imvkaratmore);
        ImageView morecolor = view.findViewById(R.id.imvmorecolor);
        Button btncancel = view.findViewById(R.id.btn_cancel);
        Button btnapply = view.findViewById(R.id.btn_apply);

        expandableListView = (RecyclerView) view.findViewById(R.id.recyvcategory);
        recyclerViewKarat = (RecyclerView) view.findViewById(R.id.recyvKarat);
        recyclerViewColor = (RecyclerView) view.findViewById(R.id.recycolor);
        recyclerViewColor.setLayoutManager(new GridLayoutManager(getContext(), 5));


        expandableListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewKarat.setLayoutManager(new LinearLayoutManager(getActivity()));
        morecolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandView(recyclerViewColor, morecolor);
            }
        });

        morekarat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandView(recyclerViewKarat, morekarat);

            }
        });
        morecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandView(expandableListView, morecat);

            }
        });
        //  expandableListDetail = ExpandableListDataPump.getData();
        //  expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        //   expandableListAdapter = new FilterListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        // expandableListView.setAdapter(expandableListAdapter);
        // List<String> dataset = new ArrayList<>();
        //  dataset.add("Category");
        //  dataset.add("Color");
        //  dataset.add("Karat");
        //  dataset.add("Shape");
        //  dataset.add("Certified by");
        //  expandableListView.setAdapter(new FilterParentAdapter(getActivity(), dataset));

        mypopupWindow = new PopupWindow(view, 700, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
        mypopupWindow.setTouchable(true);
        mypopupWindow.setFocusable(false);
        mypopupWindow.setOutsideTouchable(false);
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.parent.getForeground().setAlpha(0);
                mypopupWindow.dismiss();
            }
        });
        btnapply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.parent.getForeground().setAlpha(0);
                // LoadFirstPage();
                mypopupWindow.dismiss();
            }
        });


    }

    private void expandView(RecyclerView hiddenView, ImageView arrow) {
        if (hiddenView.getVisibility() == View.VISIBLE) {

            // The transition of the hiddenView is carried out
            //  by the TransitionManager class.
            // Here we use an object of the AutoTransition
            // Class to create a default transition.
            TransitionManager.beginDelayedTransition(hiddenView,
                    new AutoTransition());
            hiddenView.setVisibility(View.GONE);
            arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
        }

        // If the CardView is not expanded, set its visibility
        // to visible and change the expand more icon to expand less.
        else {

            TransitionManager.beginDelayedTransition(hiddenView,
                    new AutoTransition());
            hiddenView.setVisibility(View.VISIBLE);
            arrow.setImageResource(R.drawable.ic_baseline_expand_less_24);
        }


    }

    @Override
    public void replace(String Id) {
        Log.d(TAG, "replace: " + this.getClass().getSimpleName());
        FragmentHelper.replaceFragment(getActivity(), R.id.content, ProductDetailsFragment.newInstance(Id));


    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(getActivity(), key, json);
    }

    @Override
    public void applyfilter() {
        Toast.makeText(getActivity(), "FilterApplied", Toast.LENGTH_SHORT).show();
        filterViewModel.FetchFilters(AppConstants.Authorization + AuthToken);

        LoadFirstPage();

    }

    @Override
    public void addtofav(String id,String serialno) {
        addtoFavoritesViewModel.AddtoFavorites(AppConstants.Authorization + AuthToken, GuestCustomerID, UserId, id, "add", "",serialno);

    }
}