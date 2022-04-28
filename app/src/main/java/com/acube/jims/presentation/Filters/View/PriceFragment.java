package com.acube.jims.presentation.Filters.View;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.acube.jims.presentation.Catalogue.View.FilterBottomSheetFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.datalayer.models.Filter.Weight;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PriceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText tvminPrice, tvMaxprice;

    public PriceFragment() {
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
    public static PriceFragment newInstance(String param1, String param2) {
        PriceFragment fragment = new PriceFragment();
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
        View view = inflater.inflate(R.layout.fragment_price, container, false);
        // RecyclerView recyclerView = view.findViewById(R.id.recysubcategory);
        tvminPrice = view.findViewById(R.id.ed_pricemin);
        tvMaxprice = view.findViewById(R.id.ed_pricemax);
        tvminPrice.addTextChangedListener(new TextWatcher() {
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
                     minimum =s.toString();
                    Log.d("TAG", "afterTextChangedbf: "+minimum);
                    FilterPreference.storeStringPreference(requireActivity(), "MinValue",minimum );

                }else{
                    minimum="0";
                    FilterPreference.storeStringPreference(requireActivity(), "MinValue",minimum );

                }
                Log.d("TAG", "afterTextChanged: "+minimum);

            }
        });

        tvMaxprice.addTextChangedListener(new TextWatcher() {
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
                    maximum =s.toString();
                    Log.d("TAG", "afterTextChangedbf: "+maximum);
                    FilterPreference.storeStringPreference(requireActivity(), "MaxValue",maximum );

                }else{
                    maximum="0";
                    FilterPreference.storeStringPreference(requireActivity(), "MaxValue",maximum );

                }
                Log.d("TAG", "afterTextChanged: "+maximum);

            }
        });


// Get noticed while dragging



       /* RangeSlider rangeSlider = view.findViewById(R.id.sliderRange);
        rangeSlider.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
                currencyFormat.setCurrency(Currency.getInstance("SAR"));
                return currencyFormat.format(value);
            }
        });

        rangeSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                Log.d("onValueChange", "onValueChange: " + slider.getValues());

            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {
                Log.d("onValueChange", "onValueChange: " + slider.getValues());
                tvminPrice.setText("Price (Min) : " + slider.getValues().get(0));
                int MinValue = (int) Math.round(slider.getValues().get(0));
                int MaxValue = (int) Math.round(slider.getValues().get(1));
                FilterPreference.storeStringPreference(getActivity(), "MinValue", String.valueOf(MinValue));
                FilterPreference.storeStringPreference(getActivity(), "MaxValue", String.valueOf(MaxValue));
                String pricerange = slider.getValues().get(0) + " - " + slider.getValues().get(1);
                FilterPreference.storeStringPreference(getActivity(), "pricenames", pricerange);

                tvMaxprice.setText("Price (Max) : " + slider.getValues().get(1));
                refresh();
            }
        });
*/

        return view;
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
        FilterBottomSheetFragment parentFrag = ((FilterBottomSheetFragment) PriceFragment.this.getParentFragment());
        parentFrag.Refresh();
    }
}