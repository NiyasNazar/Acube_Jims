package com.acube.jims.Presentation.Catalogue;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.acube.jims.Presentation.Catalogue.ViewModel.CatalogViewModel;
import com.acube.jims.Presentation.Catalogue.adapter.CatalogItemAdapter;
import com.acube.jims.Presentation.Catalogue.adapter.FilterListAdapter;
import com.acube.jims.Presentation.Catalogue.adapter.FilterParentAdapter;
import com.acube.jims.Presentation.DeviceRegistration.ViewModel.DeviceRegistrationViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.AppUtility;
import com.acube.jims.Utils.ExpandableListDataPump;
import com.acube.jims.databinding.FragmentCatalogueBinding;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CatalogueFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CatalogueFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CatalogueFragment() {
        // Required empty public constructor
    }

    RecyclerView expandableListView;
    FilterListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CatalogueFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CatalogueFragment newInstance(String param1, String param2) {
        CatalogueFragment fragment = new CatalogueFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FragmentCatalogueBinding binding;
    CatalogViewModel viewModel;
    PopupWindow mypopupWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_catalogue, container, false);
        init();
        setPopUpWindow();
        binding.imvfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mypopupWindow.showAsDropDown(v, -153, 0);


            }
        });

        viewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseCatalogueListing>>() {
            @Override
            public void onChanged(List<ResponseCatalogueListing> responseCatalogueListings) {
                if (responseCatalogueListings != null) {
                    binding.recyvcatalog.setAdapter(new CatalogItemAdapter(getActivity(), responseCatalogueListings));

                }

            }
        });
        viewModel.FetchCatalog("1", "10", "0", "0");

        View view = binding.getRoot();
        return view;


    }

    private void init() {
        viewModel = new ViewModelProvider(this).get(CatalogViewModel.class);
        viewModel.init();
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
        expandableListView = (RecyclerView) view.findViewById(R.id.expandableListView);
        expandableListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //  expandableListDetail = ExpandableListDataPump.getData();
        //  expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        //   expandableListAdapter = new FilterListAdapter(getActivity(), expandableListTitle, expandableListDetail);
        // expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setAdapter(new FilterParentAdapter(getActivity()));

        mypopupWindow = new PopupWindow(view, 500, RelativeLayout.LayoutParams.WRAP_CONTENT, true);
    }
}