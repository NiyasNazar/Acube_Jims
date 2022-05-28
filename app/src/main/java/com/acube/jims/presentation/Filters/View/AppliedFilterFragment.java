package com.acube.jims.presentation.Filters.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.presentation.Filters.adapter.AppliedSubcategoryAdapter;
import com.acube.jims.R;
import com.acube.jims.utils.AppUtility;
import com.acube.jims.utils.FilterPreference;
import com.acube.jims.databinding.FragmentAppliedfilterBinding;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppliedFilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppliedFilterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AppliedFilterFragment() {
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
    public static AppliedFilterFragment newInstance(String param1, String param2) {
        AppliedFilterFragment fragment = new AppliedFilterFragment();
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

    FragmentAppliedfilterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_appliedfilter, container, false);

        if (new AppUtility(getActivity()).isTablet(requireActivity())){
            binding.recyappliedsubcat.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            binding.recyappliedkarat.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            binding.recyappliedcolor.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            binding.recyappliedweight.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        }else {
            binding.recyappliedsubcat.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyappliedkarat.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyappliedcolor.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyappliedweight.setLayoutManager(new LinearLayoutManager(getActivity()));
        }


        String pricerange = FilterPreference.retrieveStringPreferences(requireActivity(), "pricenames");
        String gender = FilterPreference.retrieveStringPreferences(requireActivity(), "gender");
        if (!gender.equalsIgnoreCase("")) {
            binding.tvgender.setText(gender);
            binding.laytgender.setVisibility(View.VISIBLE);
        } else {
            binding.laytgender.setVisibility(View.GONE);
        }

        if (!pricerange.equalsIgnoreCase("")) {
            binding.tvprice.setText(pricerange);
            binding.laytprice.setVisibility(View.VISIBLE);
        } else {
            binding.laytprice.setVisibility(View.GONE);
        }


        List<String> datasetsubcategory = getList("subcatnames");
        List<String> datasetcolor = getList("colornames");
        List<String> datasetkarat = getList("karatnames");
        List<String> datasetweight = getList("weightnames");
        binding.tvname.setText(FilterPreference.retrieveStringPreferences(requireActivity(), "catname"));

        binding.recyappliedsubcat.setAdapter(new AppliedSubcategoryAdapter(getActivity(), datasetsubcategory));
        binding.recyappliedkarat.setAdapter(new AppliedSubcategoryAdapter(getActivity(), datasetkarat));
        binding.recyappliedcolor.setAdapter(new AppliedSubcategoryAdapter(getActivity(), datasetcolor));
        binding.recyappliedweight.setAdapter(new AppliedSubcategoryAdapter(getActivity(), datasetweight));
        return binding.getRoot();
    }

    public List<String> getList(String filtername) {
        List<String> mMainCategory = null;
        String serializedObject = FilterPreference.retrieveStringPreferences(requireActivity(), filtername);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            mMainCategory = gson.fromJson(serializedObject, type);
        }
        return mMainCategory;
    }
}