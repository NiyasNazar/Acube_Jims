package com.acube.jims.presentation.Filters.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.presentation.Catalogue.View.FilterBottomSheetFragment;
import com.acube.jims.presentation.Catalogue.adapter.SubCategoryAdapter;
import com.acube.jims.R;
import com.acube.jims.utils.FilterPreference;
import com.acube.jims.utils.RefreshSelection;
import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubCatFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubCatFilterFragment extends Fragment implements RefreshSelection {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubCatFilterFragment() {
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
    public static SubCatFilterFragment newInstance(String param1, String param2) {
        SubCatFilterFragment fragment = new SubCatFilterFragment();
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
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4));
        recyclerView.setAdapter(new SubCategoryAdapter(getActivity(), getList(), SubCatFilterFragment.this));
        return view;
    }

    public List<SubCategory> getList() {
        List<SubCategory> mMainCategory = null;
        String serializedObject = FilterPreference.retrieveStringPreferences(requireActivity(), "subcatresult");
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
        FilterBottomSheetFragment parentFrag = ((FilterBottomSheetFragment) SubCatFilterFragment.this.getParentFragment());
        parentFrag.Refresh();


    }
}