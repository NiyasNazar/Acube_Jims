package com.acube.jims.Presentation.Catalogue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.Utils.RefreshSelection;
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.acube.jims.datalayer.models.Filter.Weight;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FilterWeightAdapter extends RecyclerView.Adapter<FilterWeightAdapter.ProductViewHolder> {

    RefreshSelection refreshSelection;
    private Context mCtx;
    private int lastSelectedPosition = -1;


    private List<Weight> dataset;

    List<String> weightlist;
    List<String>weightnames;

    public FilterWeightAdapter(Context mCtx, List<Weight> dataset, RefreshSelection refreshSelection) {
        this.mCtx = mCtx;
        this.dataset = dataset;

        weightlist = new ArrayList<>();
        this.refreshSelection = refreshSelection;
        weightnames=new ArrayList<>();

    }

    public FilterWeightAdapter(Context mCtx) {
        this.mCtx = mCtx;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_child_item_karat, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        // ResponseCatalogueListing responseCatalogueListing = dataset.get(position);
        holder.textViewkaratcheckbox.setText("" + dataset.get(position).getGoldWeight()+" g");
        // holder.imageView.setImageResource(homeData.getImage());
        boolean ischecked = FilterPreference.retrieveBooleanPreferences(mCtx, "weight" + position);
        if (ischecked) {
            holder.textViewkaratcheckbox.setChecked(true);
        } else {
            holder.textViewkaratcheckbox.setChecked(false);
        }


    }


    @Override
    public int getItemCount() {
        return dataset==null ? 0 :dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        CheckBox textViewkaratcheckbox;
        ImageView imageView;
        RecyclerView recyclerView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewkaratcheckbox = itemView.findViewById(R.id.checkbox);
            //imageView = itemView.findViewById(R.id.imageView);


            textViewkaratcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        FilterPreference.storeBooleanPreference(mCtx, "weight" + getAdapterPosition(), true);
                        weightlist.add(String.valueOf(dataset.get(getAdapterPosition()).getGoldWeight()));
                        weightnames.add(dataset.get(getAdapterPosition()).getGoldWeight()+"g");
                        setList("weightnames", weightnames);
                        setList("weightfilter", weightlist);
                        refreshSelection.refresh();

                    } else if (!isChecked) {
                        FilterPreference.storeBooleanPreference(mCtx, "weight" + getAdapterPosition(), false);
                        weightlist.remove(String.valueOf(dataset.get(getAdapterPosition()).getGoldWeight()));
                        weightnames.remove(dataset.get(getAdapterPosition()).getGoldWeight()+"g");
                        setList("weightfilter", weightlist);
                        setList("weightnames", weightnames);
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