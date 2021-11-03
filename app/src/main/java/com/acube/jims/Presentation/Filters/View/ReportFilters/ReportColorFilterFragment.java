package com.acube.jims.Presentation.Filters.View.ReportFilters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.Presentation.Catalogue.View.FilterBottomSheetFragment;
import com.acube.jims.Presentation.Catalogue.adapter.FilterColorAdapter;
import com.acube.jims.Presentation.Report.View.ReportFilterBottomSheetFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.Utils.RefreshSelection;
import com.acube.jims.datalayer.models.Filter.Colorresult;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportColorFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportColorFilterFragment extends Fragment implements RefreshSelection {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportColorFilterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ColorFilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportColorFilterFragment newInstance(String param1, String param2) {
        ReportColorFilterFragment fragment = new ReportColorFilterFragment();
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
        View view= inflater.inflate(R.layout.fragment_color_filter, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recysubcategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new FilterColorAdapter(getActivity(), getList(), ReportColorFilterFragment.this));
        return view;
    }
    public List<Colorresult> getList() {
        List<Colorresult> mMainCategory = null;
        String serializedObject = FilterPreference.retrieveStringPreferences(getActivity(), "colorresults");
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Colorresult>>() {
            }.getType();
            mMainCategory = gson.fromJson(serializedObject, type);
        }
        return mMainCategory;
    }

    @Override
    public void refresh() {
        ReportFilterBottomSheetFragment parentFrag = ((ReportFilterBottomSheetFragment) ReportColorFilterFragment.this.getParentFragment());
        parentFrag.Refresh();
    }
}