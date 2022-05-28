package com.acube.jims.presentation.Filters.View;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.acube.jims.presentation.Catalogue.View.FilterBottomSheetFragment;
import com.acube.jims.R;
import com.acube.jims.utils.FilterPreference;
import com.acube.jims.utils.RefreshSelection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeightFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeightFragment extends Fragment implements RefreshSelection {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WeightFragment() {
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
    public static WeightFragment newInstance(String param1, String param2) {
        WeightFragment fragment = new WeightFragment();
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

    EditText tvminWeight, tvMaxWeight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weight, container, false);
        //  RecyclerView recyclerView = view.findViewById(R.id.recysubcategory);
        //  recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //  recyclerView.setAdapter(new FilterWeightAdapter(getActivity(), getList(),WeightFragment.this));
        tvminWeight = view.findViewById(R.id.ed_weightmin);
        tvMaxWeight = view.findViewById(R.id.ed_weightmax);

        tvminWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
               /* FilterPreference.storeStringPreference(requireActivity(), "MinValue", String.valueOf(minValue));
                FilterPreference.storeStringPreference(requireActivity(), "MaxValue", String.valueOf(maxValue));*/
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String minimum;
                if (s.length() > 0) {
                    minimum = s.toString();
                    Log.d("TAG", "afterTextChangedbf: " + minimum);
                    FilterPreference.storeStringPreference(requireActivity(), "MinWeight", minimum);

                } else {
                    minimum = "0";
                    FilterPreference.storeStringPreference(requireActivity(), "MinWeight", minimum);

                }
                Log.d("TAG", "afterTextChanged: " + minimum);

            }
        });

        tvMaxWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
               /* FilterPreference.storeStringPreference(requireActivity(), "MinValue", String.valueOf(minValue));
                FilterPreference.storeStringPreference(requireActivity(), "MaxValue", String.valueOf(maxValue));*/
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String maximum;
                if (s.length() > 0) {
                    maximum = s.toString();
                    Log.d("TAG", "afterTextChangedbf: " + maximum);
                    FilterPreference.storeStringPreference(requireActivity(), "MaxWeight", maximum);

                } else {
                    maximum = "0";
                    FilterPreference.storeStringPreference(requireActivity(), "MaxWeight", maximum);

                }
                Log.d("TAG", "afterTextChanged: " + maximum);

            }
        });


        return view;
    }


    @Override
    public void refresh() {
        FilterBottomSheetFragment parentFrag = ((FilterBottomSheetFragment) WeightFragment.this.getParentFragment());
        parentFrag.Refresh();
    }
}