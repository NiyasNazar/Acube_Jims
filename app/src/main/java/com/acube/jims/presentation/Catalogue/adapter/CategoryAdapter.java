package com.acube.jims.presentation.Catalogue.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.datalayer.models.Filter.FilterStore;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.utils.FilterPreference;
import com.acube.jims.utils.RefreshSelection;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;
import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ProductViewHolder> implements FilterChildAdapter.Passfilter {


    private Context mCtx;
    private int lastSelectedPosition = -1;

    private List<FilterStore> dataset;
    List<SubCategory> sublist;
    List<String> catlist;
    private RadioButton lastCheckedRB = null;
    int selpos;
    RefreshSelection refreshSelection;
    boolean isSelected;

    public CategoryAdapter(Context mCtx, List<FilterStore> dataset, RefreshSelection refreshSelection) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        sublist = new ArrayList<>();
        catlist = new ArrayList<>();
        this.refreshSelection = refreshSelection;


    }

    public CategoryAdapter(Context mCtx) {
        this.mCtx = mCtx;
        sublist = new ArrayList<>();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_category_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        // ResponseCatalogueListing responseCatalogueListing = dataset.get(position);
        holder.tvcatname.setText(dataset.get(position).getWarehouseName());
        // holder.imageView.setImageResource(homeData.getImage());
        //  holder.textViewItemName.setChecked(lastSelectedPosition == position);

        holder.tvcatname.setChecked(dataset.get(position).isIsselected());


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public void PassId(Boolean flag, String id) {
       /* if (flag) {
            catlist.add(id);
            setList("subcategoryfilter", catlist);

        } else {
            catlist.remove(id);
            setList("subcategoryfilter", catlist);
        }*/

        Log.d("PassId", "PassId: " + catlist.toString() + flag);
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        CardView catfilter;
        ImageView arrow;
        RadioGroup rdgrp;
        RecyclerView recyclerView;
        CheckBox tvcatname;

        public ProductViewHolder(View itemView) {
            super(itemView);

            catfilter = itemView.findViewById(R.id.btn_cat_filter);
            tvcatname = itemView.findViewById(R.id.tvcatname);
            arrow = itemView.findViewById(R.id.arrow);
            tvcatname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean enableEdit = FilterPreference.retrieveBooleanPreferences(mCtx, "enableEdit");

                    lastSelectedPosition = getAbsoluteAdapterPosition();
                    FilterPreference.storeIntegerPreference(mCtx, "cpos", lastSelectedPosition);
                    FilterPreference.storeIntegerPreference(mCtx, "warehouseId", dataset.get(getAbsoluteAdapterPosition()).getId());
                    // FilterPreference.storeStringPreference(mCtx, "catname", dataset.get(getAbsoluteAdapterPosition()).getCategoryName());
                    Log.d("onBindViewHolder", "onBindViewHolder: " + selpos + dataset.get(getAbsoluteAdapterPosition()).getId());
                      /*  setList("subcatresult", dataset.get(getAbsoluteAdapterPosition()).getSubCategories());
                        setList("colorresults", dataset.get(getAbsoluteAdapterPosition()).getColors());
                        setList("karatresults", dataset.get(getAbsoluteAdapterPosition()).getKarats());
                        setList("weightresults", dataset.get(getAbsoluteAdapterPosition()).getWeights());*/

                    new Thread(() -> {
                        DatabaseClient.getInstance(mCtx).getAppDatabase().homeMenuDao().update(dataset.get(getAbsoluteAdapterPosition()).getId());

                        ((Activity) mCtx).runOnUiThread(() -> {
                            notifyDataSetChanged();


                        });
                    }).start();
                    refreshSelection.refresh();


                       /* new StyleableToast
                                .Builder(mCtx)
                                .text("Category is set to default")
                                .textColor(Color.WHITE)
                                .iconStart(R.drawable.ic_error)
                                .font(R.font.regular)
                                .gravity(Gravity.BOTTOM)
                                .length(Toast.LENGTH_LONG).solidBackground()
                                .backgroundColor(Color.RED)
                                .show();*/

                }


            });
        /*    rdgrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton checked_rb = (RadioButton) group.findViewById(checkedId);
                    if (lastCheckedRB != null) {
                        lastCheckedRB.setChecked(false);
                    }
                    lastCheckedRB = checked_rb;
                   Log.d("TAG", "onCheckedChanged: "+checkedId);
                  //  LocalPreferences.storeIntegerPreference(mCtx, "catchecked", lastCheckedRB);
                }
            });*/


        }
    }

    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        FilterPreference.storeStringPreference(mCtx, key, json);
    }
}