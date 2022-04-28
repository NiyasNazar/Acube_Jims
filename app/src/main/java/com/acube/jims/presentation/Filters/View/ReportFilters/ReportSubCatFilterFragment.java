package com.acube.jims.presentation.Filters.View.ReportFilters;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.presentation.Catalogue.adapter.SubCategoryAdapter;
import com.acube.jims.presentation.Report.View.ReportFilterBottomSheetFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.Utils.RefreshSelection;
import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportSubCatFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportSubCatFilterFragment extends Fragment implements RefreshSelection {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportSubCatFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportSubCatFilterFragment newInstance(String param1, String param2) {
        ReportSubCatFilterFragment fragment = new ReportSubCatFilterFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sub_filter, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recysubcategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new SubCategoryAdapter(getActivity(), getList(), ReportSubCatFilterFragment.this));
        Log.d("onCreateView", "onCreateView: " + getList().size());
        return view;
    }

    public List<SubCategory> getList() {
        List<SubCategory> mMainCategory = null;
        String serializedObject = FilterPreference.retrieveStringPreferences(getActivity(), "subcatresult");
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<SubCategory>>() {
            }.getType();
            mMainCategory = gson.fromJson(serializedObject, type);
        }
        return mMainCategory;
    }

    @Override
    public void refresh() {
        ReportFilterBottomSheetFragment parentFrag = ((ReportFilterBottomSheetFragment) ReportSubCatFilterFragment.this.getParentFragment());
        parentFrag.Refresh();


    }
}