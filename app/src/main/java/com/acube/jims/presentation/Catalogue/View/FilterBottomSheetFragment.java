package com.acube.jims.presentation.Catalogue.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.Catalogue.adapter.FilterMasterAdapter;
import com.acube.jims.presentation.Filters.View.AppliedFilterFragment;
import com.acube.jims.presentation.Filters.View.CategoryFilterFragment;
import com.acube.jims.presentation.Filters.View.ColorFilterFragment;
import com.acube.jims.presentation.Filters.View.GenderFragment;
import com.acube.jims.presentation.Filters.View.KaratFragment;
import com.acube.jims.presentation.Filters.View.PriceFragment;
import com.acube.jims.presentation.Filters.View.SubCatFilterFragment;
import com.acube.jims.presentation.Filters.View.WeightFragment;
import com.acube.jims.R;
import com.acube.jims.utils.FilterPreference;
import com.acube.jims.databinding.BottomSheetFilterBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FilterBottomSheetFragment extends BottomSheetDialogFragment implements FilterMasterAdapter.ReplaceFregment {
    BottomSheetFilterBinding binding;
    BottomSheetBehavior bottomSheetBehavior;
    ApplyFilter applyFilter;

    public FilterBottomSheetFragment(ApplyFilter applyFilter) {
        this.applyFilter = applyFilter;

    }

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.bottom_sheet_filter, container, false);
        List<String> dataset = new ArrayList<>();
        dataset.add("Branch");
        dataset.add("Karat");
        dataset.add("Color");
        dataset.add("Weight");
        dataset.add("Price");
        dataset.add("Gender");
        binding.rcylrfiltritems.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcylrfiltritems.setAdapter(new FilterMasterAdapter(getActivity(), dataset, FilterBottomSheetFragment.this));
        binding.closelayt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterPreference.storeStringPreference(requireActivity(), "subcatid", "");
                FilterPreference.storeStringPreference(requireActivity(), "colorid", "");
                FilterPreference.storeStringPreference(requireActivity(), "karatid", "");
                dismiss();
            }
        });
        binding.clrfilterlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryFilterFragment categoryFilterFragment = new CategoryFilterFragment();
                FilterPreference.clearPreferences(requireActivity());
                ReplaceFragment(categoryFilterFragment);
                new Thread(() -> {
                    DatabaseClient.getInstance(requireActivity()).getAppDatabase().homeMenuDao().updatefilterstore(0);
                    DatabaseClient.getInstance(requireActivity()).getAppDatabase().homeMenuDao().updateColor(0);
                    DatabaseClient.getInstance(requireActivity()).getAppDatabase().homeMenuDao().updateKaratresult(0);


                }).start();
                Toast.makeText(getActivity(), "Filter Cleared", Toast.LENGTH_SHORT).show();
            }
        });
        CategoryFilterFragment categoryFilterFragment = new CategoryFilterFragment();
        ReplaceFragment(categoryFilterFragment);
        binding.applyfilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<String> datasetsubcategory = new ArrayList<>();
                List<String> datasetcolor = new ArrayList<>();
                List<String> datasetkarat = new ArrayList<>();
                List<String> datasetCategory = new ArrayList<>();
                List<String> datasetweight = new ArrayList<>();

                datasetsubcategory = getList("subcategoryfilter");
                datasetcolor = getList("colorcategoryfilter");

                datasetweight = getList("weightfilter");
                if (datasetsubcategory != null) {
                    StringBuilder str = new StringBuilder("");
                    for (String eachstring : datasetsubcategory) {
                        str.append(eachstring).append(",");
                    }
                    String commaseparatedlist = str.toString();
                    if (commaseparatedlist.length() > 0)
                        commaseparatedlist
                                = commaseparatedlist.substring(
                                0, commaseparatedlist.length() - 1);
                    Log.d("mycategoryfilter", "onClick: " + commaseparatedlist);
                    FilterPreference.storeStringPreference(requireActivity(), "subcatid", commaseparatedlist);
                }

                if (datasetcolor != null) {
                    StringBuilder str = new StringBuilder("");
                    for (String eachstring : datasetcolor) {
                        str.append(eachstring).append(",");
                    }
                    String commaseparatedlist = str.toString();
                    if (commaseparatedlist.length() > 0)
                        commaseparatedlist
                                = commaseparatedlist.substring(
                                0, commaseparatedlist.length() - 1);
                    Log.d("datasetcolor", "onClick: " + commaseparatedlist);
                    FilterPreference.storeStringPreference(requireActivity(), "colorid", commaseparatedlist);
                }
                if (datasetkarat != null) {
                    StringBuilder str = new StringBuilder("");
                    for (String eachstring : datasetkarat) {
                        str.append(eachstring).append(",");
                    }
                    String commaseparatedlist = str.toString();
                    if (commaseparatedlist.length() > 0)
                        commaseparatedlist
                                = commaseparatedlist.substring(
                                0, commaseparatedlist.length() - 1);
                    Log.d("datasetkarat", "onClick: " + commaseparatedlist);
                    FilterPreference.storeStringPreference(requireActivity(), "karatid", commaseparatedlist);
                }
                if (datasetweight != null) {
                    StringBuilder str = new StringBuilder("");
                    for (String eachstring : datasetweight) {
                        str.append(eachstring).append(",");
                    }
                    String commaseparatedlist = str.toString();
                    if (commaseparatedlist.length() > 0)
                        commaseparatedlist
                                = commaseparatedlist.substring(
                                0, commaseparatedlist.length() - 1);
                    Log.d("datasetweight", "onClick: " + commaseparatedlist);
                    FilterPreference.storeStringPreference(requireActivity(), "weightid", commaseparatedlist);
                }

                ((ApplyFilter) requireActivity()).applyfilter();
                dismiss();
            }


        });


        return binding.getRoot();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dia) {
                BottomSheetDialog dialog = (BottomSheetDialog) dia;
                FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
                BottomSheetBehavior.from(bottomSheet).setSkipCollapsed(true);
                BottomSheetBehavior.from(bottomSheet).setHideable(true);
            }
        });
        return bottomSheetDialog;
    }

    @Override
    public void replace(int pos) {
        if (pos == 0) {
            /// FragmentHelper.replaceFragment(getActivity(), R.id.content, new CategoryFilterFragment(), "");

            CategoryFilterFragment categoryFilterFragment = new CategoryFilterFragment();
            ReplaceFragment(categoryFilterFragment);

        } else if (pos == 1) {
            KaratFragment colorFilterFragment = new KaratFragment();
            ReplaceFragment(colorFilterFragment);
        } else if (pos == 2) {

            ColorFilterFragment colorFilterFragment = new ColorFilterFragment();
            ReplaceFragment(colorFilterFragment);

        } else if (pos == 3) {

            WeightFragment weightFragment = new WeightFragment();
            ReplaceFragment(weightFragment);

        } else if (pos == 4) {

            PriceFragment priceFragment = new PriceFragment();
            ReplaceFragment(priceFragment);

        } else if (pos == 5) {

            GenderFragment genderFragment = new GenderFragment();
            ReplaceFragment(genderFragment);

        }

    }

    public void ReplaceFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment2, fragment);
        fragmentTransaction.commit();
        FragmentTransaction fragmentTransaction2 = getChildFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.fragment3, new AppliedFilterFragment());
        fragmentTransaction2.commit();
    }

    public void Refresh() {
        FragmentTransaction fragmentTransaction2 = getChildFragmentManager().beginTransaction();
        fragmentTransaction2.replace(R.id.fragment3, new AppliedFilterFragment());
        fragmentTransaction2.commit();
    }

    public List<String> getList(String name) {
        List<String> mMainCategory = null;
        String serializedObject = FilterPreference.retrieveStringPreferences(getActivity(), name);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<String>>() {
            }.getType();
            mMainCategory = gson.fromJson(serializedObject, type);
        }
        return mMainCategory;
    }

    public interface ApplyFilter {
        void applyfilter();

    }
}

