package com.acube.jims.presentation.Filters.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.acube.jims.databinding.FragmentGenderBinding;
import com.acube.jims.presentation.Catalogue.View.FilterBottomSheetFragment;
import com.acube.jims.R;
import com.acube.jims.utils.FilterPreference;
import com.acube.jims.datalayer.models.Filter.Weight;
import com.acube.jims.presentation.Catalogue.adapter.GenderAdapter;
import com.acube.jims.presentation.Catalogue.adapter.Genderesult;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenderFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GenderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KaratFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenderFragment newInstance(String param1, String param2) {
        GenderFragment fragment = new GenderFragment();
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

    RadioButton rdmen, rdwomen, rdunisex;
    RadioGroup radioGroup;
    FragmentGenderBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_gender, container, false);
        List<Genderesult>dataset=new ArrayList<>();

        Genderesult genderesult1=new Genderesult();
        genderesult1.setGendername("MALE");
        dataset.add(genderesult1);
        Genderesult genderesult2=new Genderesult();
        genderesult2.setGendername("FEMALE");
        dataset.add(genderesult2);

        Genderesult genderesult3=new Genderesult();
        genderesult3.setGendername("UNISEX");
        dataset.add(genderesult3);

        String gendervalue = FilterPreference.retrieveStringPreferences(getActivity(), "gender");
        binding.recysubcategory.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        binding.recysubcategory.setAdapter(new GenderAdapter(getActivity(),dataset));
        return binding.getRoot();
    }


    public List<Weight> getList() {
        List<Weight> mMainCategory = null;
        String serializedObject = FilterPreference.retrieveStringPreferences(getActivity(), "weightresults");
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Weight>>() {
            }.getType();
            mMainCategory = gson.fromJson(serializedObject, type);
        }
        return mMainCategory;
    }

    public void refresh() {
        FilterBottomSheetFragment parentFrag = ((FilterBottomSheetFragment) GenderFragment.this.getParentFragment());
        parentFrag.Refresh();
    }
}