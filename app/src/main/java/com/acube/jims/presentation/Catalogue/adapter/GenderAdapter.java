package com.acube.jims.presentation.Catalogue.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.utils.FilterPreference;
import com.acube.jims.utils.RefreshSelection;
import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.ProductViewHolder> {


    private Context mCtx;

    private int lastSelectedPosition = -1;

    private List<Genderesult> dataset;
    List<SubCategory> sublist;
    List<String> colorlist;
    List<String> colornames;
    RefreshSelection refreshSelection;

    public GenderAdapter(Context mCtx, List<Genderesult> dataset, RefreshSelection refreshSelection) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        sublist = new ArrayList<>();
        colorlist = new ArrayList<>();
        this.refreshSelection = refreshSelection;
        colornames = new ArrayList<>();

    }

    public GenderAdapter(Context mCtx, List<Genderesult> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_color_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        holder.textViewItemName.setText(dataset.get(position).getGendername());
        if (lastSelectedPosition == position) {
            holder.textViewItemName.setChecked(true);

        } else {
            holder.textViewItemName.setChecked(false);

        }


    }


    @Override
    public int getItemCount() {
        return dataset == null ? 0 : dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        CheckBox textViewItemName;
        RecyclerView recyclerView;
        CheckBox mcolor;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tv_item_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    lastSelectedPosition = position;
                    dataset.get(position).setSelected(!dataset.get(position).isSelected());
                    FilterPreference.storeStringPreference(mCtx, "gender", dataset.get(position).getGendername());
                    notifyDataSetChanged();

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