package com.acube.jims.Presentation.Catalogue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.Presentation.Filters.View.AppliedFilterFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.Utils.RefreshSelection;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;
import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ProductViewHolder> implements FilterChildAdapter.Passfilter {
    private int lastSelectedPosition = -1;


    private Context mCtx;


    List<SubCategory> dataset;
    List<String> subcatlist;
    List<String> subcatnames;
    RefreshSelection refreshSelection;

    public SubCategoryAdapter(Context mCtx, List<SubCategory> dataset, RefreshSelection refreshSelection) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.refreshSelection = refreshSelection;
        subcatnames = new ArrayList<>();

        subcatlist = new ArrayList<>();


    }

    public SubCategoryAdapter(Context mCtx) {
        this.mCtx = mCtx;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_sub_category_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        // ResponseCatalogueListing responseCatalogueListing = dataset.get(position);
        holder.textViewItemName.setText(dataset.get(position).getSubCategoryName());
        // holder.imageView.setImageResource(homeData.getImage());
        boolean ischecked = FilterPreference.retrieveBooleanPreferences(mCtx, "sub" + position);
        if (ischecked) {
            holder.textViewItemName.setChecked(true);
        } else {
            holder.textViewItemName.setChecked(false);
        }


    }


    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
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

        //  Log.d("PassId", "PassId: " + catlist.toString() + flag);
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        CheckBox textViewItemName;
        ImageView arrow;
        RecyclerView recyclerView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tv_item_name);

            arrow = itemView.findViewById(R.id.arrow);
            textViewItemName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        FilterPreference.storeBooleanPreference(mCtx, "sub" + getAdapterPosition(), true);
                        subcatlist.add(String.valueOf(dataset.get(getAdapterPosition()).getId()));
                        subcatnames.add(dataset.get(getAdapterPosition()).getSubCategoryName());
                        setList("subcategoryfilter", subcatlist);
                        setList("subcatnames", subcatnames);
                        refreshSelection.refresh();


                    } else if (!isChecked) {
                        FilterPreference.storeBooleanPreference(mCtx, "sub" + getAdapterPosition(), false);
                        subcatlist.remove(String.valueOf(dataset.get(getAdapterPosition()).getId()));
                        subcatnames.remove(dataset.get(getAdapterPosition()).getSubCategoryName());
                        setList("subcategoryfilter", subcatlist);
                        setList("subcatnames", subcatnames);
                        refreshSelection.refresh();


                    }
                }
            });

        }


    }


    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        FilterPreference.storeStringPreference(mCtx, key, json);
    }

}