package com.acube.jims.Presentation.Catalogue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.Utils.RefreshSelection;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;
import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ProductViewHolder> implements FilterChildAdapter.Passfilter {


    private Context mCtx;
    private int lastSelectedPosition = 0;

    private List<ResponseFetchFilters> dataset;
    List<SubCategory> sublist;
    List<String> catlist;
    private RadioButton lastCheckedRB = null;
    int selpos;
    RefreshSelection refreshSelection;

    public CategoryAdapter(Context mCtx, List<ResponseFetchFilters> dataset, RefreshSelection refreshSelection) {
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
        holder.textViewItemName.setText(dataset.get(position).getCategoryName());
        // holder.imageView.setImageResource(homeData.getImage());
        //  holder.textViewItemName.setChecked(lastSelectedPosition == position);
        selpos = FilterPreference.retrieveIntegerPreferences(mCtx, "cpos");
        holder.textViewItemName.setChecked(selpos == position);
        Log.d("onBindViewHolder", "onBindViewHolder: " + selpos + position);
        if (selpos == 0) {
            FilterPreference.storeStringPreference(mCtx, "catid", String.valueOf(dataset.get(0).getId()));
            FilterPreference.storeStringPreference(mCtx, "catname", dataset.get(0).getCategoryName());
            setList("subcatresult", dataset.get(selpos).getSubCategories());
            setList("colorresults", dataset.get(selpos).getColors());
            setList("karatresults", dataset.get(selpos).getKarats());
            setList("weightresults", dataset.get(selpos).getWeights());
            refreshSelection.refresh();
        }


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

        RadioButton textViewItemName;
        ImageView arrow;
        RadioGroup rdgrp;
        RecyclerView recyclerView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tv_item_name);
            arrow = itemView.findViewById(R.id.arrow);
            textViewItemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lastSelectedPosition = getAdapterPosition();
                    FilterPreference.storeIntegerPreference(mCtx, "cpos", lastSelectedPosition);
                    FilterPreference.storeStringPreference(mCtx, "catid", String.valueOf(dataset.get(getAdapterPosition()).getId()));
                    FilterPreference.storeStringPreference(mCtx, "catname", dataset.get(getAdapterPosition()).getCategoryName());


                    setList("subcatresult", dataset.get(getAdapterPosition()).getSubCategories());
                    setList("colorresults", dataset.get(getAdapterPosition()).getColors());
                    setList("karatresults", dataset.get(getAdapterPosition()).getKarats());
                    setList("weightresults", dataset.get(getAdapterPosition()).getWeights());
                    refreshSelection.refresh();
                    notifyDataSetChanged();


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