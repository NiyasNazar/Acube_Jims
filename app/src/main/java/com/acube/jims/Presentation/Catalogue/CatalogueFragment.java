package com.acube.jims.Presentation.Catalogue;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.os.Handler;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.acube.jims.Presentation.Catalogue.ViewModel.CatalogViewModel;
import com.acube.jims.Presentation.Catalogue.ViewModel.FilterViewModel;
import com.acube.jims.Presentation.Catalogue.adapter.CatalogItemAdapter;
import com.acube.jims.Presentation.Catalogue.adapter.FilterKaratAdapter;
import com.acube.jims.Presentation.Catalogue.adapter.FilterListAdapter;
import com.acube.jims.Presentation.Catalogue.adapter.FilterParentAdapter;
import com.acube.jims.Presentation.DeviceRegistration.ViewModel.DeviceRegistrationViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.AppUtility;
import com.acube.jims.Utils.ExpandableListDataPump;
import com.acube.jims.Utils.PaginationScrollListener;
import com.acube.jims.databinding.FragmentCatalogueBinding;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.Filter.Catresult;
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CatalogueFragment extends Fragment {


    public CatalogueFragment() {
        // Required empty public constructor
    }

    RecyclerView expandableListView, recyclerViewKarat;
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
    FilterViewModel filterViewModel;
    PopupWindow mypopupWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_catalogue, container, false);
        binding.parent.getForeground().setAlpha(0);
        init();


        binding.imvfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mypopupWindow.showAsDropDown(v, -153, 0);
                binding.parent.getForeground().setAlpha(100);


            }
        });

        /*binding.recyvcatalog.addOnScrollListener(new PaginationScrollListener() {
            @Override
            protected void loadMoreItems() {

            }

            @Override
            public int getTotalPageCount() {
                return 0;
            }

            @Override
            public boolean isLastPage() {
                return false;
            }

            @Override
            public boolean isLoading() {
                return false;
            }
        });*/


        viewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseCatalogueListing>>() {
            @Override
            public void onChanged(List<ResponseCatalogueListing> responseCatalogueListings) {
                if (responseCatalogueListings != null) {
                    binding.recyvcatalog.setAdapter(new CatalogItemAdapter(getActivity(), responseCatalogueListings));

                }

            }
        });
        filterViewModel.getLiveData().observe(getActivity(), new Observer<ResponseFetchFilters>() {
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
        });
        viewModel.FetchCatalog("1", "10", "0", "0");
        filterViewModel.FetchFilters();
        setPopUpWindow();
        return binding.getRoot();


    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        filterViewModel = new ViewModelProvider(this).get(FilterViewModel.class);
        viewModel.init();
        filterViewModel.init();
        if (new AppUtility(getActivity()).isTablet(getActivity())) {
            binding.recyvcatalog.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        } else {
            binding.recyvcatalog.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }


    }


    private void setPopUpWindow() {
        LayoutInflater inflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.filter_layout, null);
        ImageView morecat = view.findViewById(R.id.more_cat);
        ImageView morekarat = view.findViewById(R.id.imvkaratmore);

        expandableListView = (RecyclerView) view.findViewById(R.id.recyvcategory);
        recyclerViewKarat = (RecyclerView) view.findViewById(R.id.recyvKarat);

        expandableListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewKarat.setLayoutManager(new LinearLayoutManager(getActivity()));

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

}