package com.acube.jims.Presentation.Catalogue.View;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.Presentation.Catalogue.adapter.FilterMasterAdapter;
import com.acube.jims.Presentation.Filters.View.CategoryFilterFragment;
import com.acube.jims.Presentation.Filters.View.ColorFilterFragment;
import com.acube.jims.Presentation.Filters.View.KaratFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.BottomSheetFilterBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FilterBottomSheetFragmentbcp extends BottomSheetDialogFragment implements FilterMasterAdapter.ReplaceFregment {
    BottomSheetFilterBinding binding;
    BottomSheetBehavior bottomSheetBehavior;
    ApplyFilter applyFilter;

    public FilterBottomSheetFragmentbcp(ApplyFilter applyFilter) {
        this.applyFilter = applyFilter;

    }

    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.bottom_sheet_filter, container, false);
        List<String> dataset = new ArrayList<>();
        dataset.add("Category");
        dataset.add("Color");
        dataset.add("Karat");
        binding.rcylrfiltritems.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcylrfiltritems.setAdapter(new FilterMasterAdapter(getActivity(), dataset, FilterBottomSheetFragmentbcp.this));
        binding.closelayt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalPreferences.storeStringPreference(getContext(), "subcatid", "");
                LocalPreferences.storeStringPreference(getContext(), "colorid", "");
                LocalPreferences.storeStringPreference(getContext(), "karatid", "");


                dismiss();
            }
        });
        binding.clrfilterlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalPreferences.storeStringPreference(getContext(), "subcatid", "");
                LocalPreferences.storeStringPreference(getContext(), "colorid", "");
                LocalPreferences.storeStringPreference(getContext(), "karatid", "");
                LocalPreferences.removePreferences(getActivity(), "subcategoryfilter");
                LocalPreferences.removePreferences(getActivity(), "colorcategoryfilter");
                LocalPreferences.removePreferences(getActivity(), "karatfilter");
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
                datasetsubcategory = getList("subcategoryfilter");
                datasetcolor = getList("colorcategoryfilter");
                datasetkarat = getList("karatfilter");
                if (datasetsubcategory != null && datasetsubcategory.size() != 0) {
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
                    LocalPreferences.storeStringPreference(getContext(), "subcatid", commaseparatedlist);
                }

                if (datasetcolor != null && datasetcolor.size() != 0) {
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
                    LocalPreferences.storeStringPreference(getContext(), "colorid", commaseparatedlist);
                }
                if (datasetkarat != null && datasetkarat.size() != 0) {
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
                    LocalPreferences.storeStringPreference(getContext(), "karatid", commaseparatedlist);
                }

                applyFilter.applyfilter();
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
            ColorFilterFragment colorFilterFragment = new ColorFilterFragment();
            ReplaceFragment(colorFilterFragment);
        } else if (pos == 2) {
            KaratFragment colorFilterFragment = new KaratFragment();
            ReplaceFragment(colorFilterFragment);
        }

    }

    public void ReplaceFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.fragment2, fragment);
        fragmentTransaction.commit();
    }

    public List<String> getList(String name) {
        List<String> mMainCategory = null;
        String serializedObject = LocalPreferences.retrieveStringPreferences(getActivity(), name);
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

