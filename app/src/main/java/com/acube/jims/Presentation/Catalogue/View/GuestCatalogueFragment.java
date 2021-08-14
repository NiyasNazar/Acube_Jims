package com.acube.jims.Presentation.Catalogue.View;

import android.content.Context;
import android.os.Bundle;
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

import androidx.activity.OnBackPressedCallback;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.Catalogue.ViewModel.CatalogViewModel;
import com.acube.jims.Presentation.Catalogue.ViewModel.CatalogViewModelNextPage;
import com.acube.jims.Presentation.Catalogue.ViewModel.FilterViewModel;
import com.acube.jims.Presentation.Catalogue.adapter.CatalogItemsAdapter;
import com.acube.jims.Presentation.Catalogue.adapter.FilterColorAdapter;
import com.acube.jims.Presentation.Catalogue.adapter.FilterKaratAdapter;
import com.acube.jims.Presentation.Catalogue.adapter.FilterListAdapter;
import com.acube.jims.Presentation.Catalogue.adapter.FilterParentAdapter;
import com.acube.jims.Presentation.HomePage.View.HomeFragment;
import com.acube.jims.Presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.AppUtility;
import com.acube.jims.Utils.FragmentHelper;
import com.acube.jims.Utils.PaginationScrollListener;
import com.acube.jims.databinding.FragmentCatalogueBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.Filter.Catresult;
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GuestCatalogueFragment extends BaseFragment  implements CatalogItemsAdapter.replaceFregment{


    CatalogItemsAdapter adapter;
    GridLayoutManager gridLayoutManager;
    String vaSubCatID = "0", vaCatID = "0", vaKaratID = "0";


    public GuestCatalogueFragment() {
        // Required empty public constructor
    }

    RecyclerView expandableListView, recyclerViewKarat, recyclerViewColor;
    FilterListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    List<Catresult> catresult;
    List<Karatresult> karatresults;
    HashMap<String, List<String>> expandableListDetail;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;


    FragmentCatalogueBinding binding;
    CatalogViewModel viewModel;
    CatalogViewModelNextPage catalogViewModelNextPage;
    FilterViewModel filterViewModel;
    PopupWindow mypopupWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_catalogue, container, false);
        binding.parent.setForeground(getResources().getDrawable(R.drawable.shape_window_dim));

        binding.parent.getForeground().setAlpha(0);
        init();

        // This callback will only be called when MyFragment is at least Started.


        //requireActivity().getOnBackPressedDispatcher().addCallback(this,callback);
        binding.imvfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mypopupWindow.showAsDropDown(v, -153, 0);
                binding.parent.getForeground().setAlpha(100);


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

        LoadFirstPage();
        viewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseCatalogueListing>>() {
            @Override
            public void onChanged(List<ResponseCatalogueListing> responseCatalogueListings) {
                hideProgressDialog();
                if (responseCatalogueListings != null) {
                    /* binding.recyvcatalog.setAdapter(new CatalogItemAdapter(getActivity(), responseCatalogueListings));*/
                    TOTAL_PAGES = getTotalPagesFromTotalResult(responseCatalogueListings.get(0).getTotalCount(), AppConstants.Pagesize);
                    Log.d("onChanged", "onChanged: " + TOTAL_PAGES);
                    adapter.clear();
                    adapter.addAll(responseCatalogueListings);

                    if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                    else isLastPage = true;

                }

            }
        });
        catalogViewModelNextPage.getLiveData().observe(getActivity(), new Observer<List<ResponseCatalogueListing>>() {
            @Override
            public void onChanged(List<ResponseCatalogueListing> responseCatalogueListings) {
                if (responseCatalogueListings != null) {
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
       /* filterViewModel.getLiveData().observe(getActivity(), new Observer<ResponseFetchFilters>() {
            @Override
            public void onChanged(ResponseFetchFilters responseFetchFilters) {
                if (responseFetchFilters != null) {
                    catresult = new ArrayList<>();
                    karatresults = new ArrayList<>();
                    catresult = responseFetchFilters.getCatresult();
                    karatresults = responseFetchFilters.getKaratresult();
                    expandableListView.setAdapter(new FilterParentAdapter(getActivity(), catresult));
                    recyclerViewKarat.setAdapter(new FilterKaratAdapter(getActivity(), karatresults));


                }
            }
        });*/
       /// filterViewModel.FetchFilters();
        setPopUpWindow();
        return binding.getRoot();


    }

    private void LoadFirstPage() {
        showProgressDialog();

      //  viewModel.FetchCatalog(PAGE_START, AppConstants.Pagesize, vaCatID, vaSubCatID, "0", vaKaratID);

    }

    private int getTotalPagesFromTotalResult(Integer totalCount, Integer pagesize) {
        int totalPages_pre = (totalCount / pagesize);
        return (totalCount % pagesize) == 0 ? totalPages_pre : totalPages_pre + 1;
    }

    private void loadNextPage() {


       // catalogViewModelNextPage.FetchCatalog(currentPage, AppConstants.Pagesize, vaCatID, vaSubCatID, "0", vaKaratID);

    }

    private void init() {
        adapter = new CatalogItemsAdapter(getActivity(),GuestCatalogueFragment.this);

        binding.recyvcatalog.setAdapter(adapter);

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
        recyclerViewColor.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewColor.setAdapter(new FilterColorAdapter(getActivity()));

        expandableListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewKarat.setLayoutManager(new LinearLayoutManager(getActivity()));
        morecolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandView(recyclerViewColor);
            }
        });

        morekarat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandView(recyclerViewKarat);

            }
        });
        morecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandView(expandableListView);

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

        mypopupWindow = new PopupWindow(view, 500, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
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
                LoadFirstPage();
                mypopupWindow.dismiss();
            }
        });


    }

    private void expandView(RecyclerView hiddenView) {
        if (hiddenView.getVisibility() == View.VISIBLE) {

            // The transition of the hiddenView is carried out
            //  by the TransitionManager class.
            // Here we use an object of the AutoTransition
            // Class to create a default transition.
            TransitionManager.beginDelayedTransition(hiddenView,
                    new AutoTransition());
            hiddenView.setVisibility(View.GONE);
            //arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
        }

        // If the CardView is not expanded, set its visibility
        // to visible and change the expand more icon to expand less.
        else {

            TransitionManager.beginDelayedTransition(hiddenView,
                    new AutoTransition());
            hiddenView.setVisibility(View.VISIBLE);
            // arrow.setImageResource(R.drawable.ic_baseline_expand_less_24);
        }


    }

    @Override
    public void replace(String Id) {

        FragmentHelper.replaceFragment(getActivity(), R.id.content, ProductDetailsFragment.newInstance(Id));
    }
}