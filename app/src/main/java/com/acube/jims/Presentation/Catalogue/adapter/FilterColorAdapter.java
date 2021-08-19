package com.acube.jims.Presentation.Catalogue.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.Utils.RefreshSelection;
import com.acube.jims.datalayer.models.Filter.Colorresult;
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FilterColorAdapter extends RecyclerView.Adapter<FilterColorAdapter.ProductViewHolder> {


    private Context mCtx;

    private int lastSelectedPosition = -1;

    private List<Colorresult> dataset;
    List<SubCategory> sublist;
    List<String> colorlist;
    List<String> colornames;
    RefreshSelection refreshSelection;

    public FilterColorAdapter(Context mCtx, List<Colorresult> dataset, RefreshSelection refreshSelection) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        sublist = new ArrayList<>();
        colorlist = new ArrayList<>();
        this.refreshSelection = refreshSelection;
        colornames=new ArrayList<>();

    }

    public FilterColorAdapter(Context mCtx) {
        this.mCtx = mCtx;
        sublist = new ArrayList<>();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_color_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        holder.mcolor.setText(dataset.get(position).getColorName());
        // ResponseCatalogueListing responseCatalogueListing = dataset.get(position);
        //   holder.textViewItemName.setText(dataset.get(position).getKaratName());
        // holder.imageView.setImageResource(homeData.getImage());
        boolean ischecked = FilterPreference.retrieveBooleanPreferences(mCtx, "color" + position);
        if (ischecked) {
            holder.mcolor.setChecked(true);
        } else {
            holder.mcolor.setChecked(false);
        }


    }


    @Override
    public int getItemCount() {
        return dataset==null ? 0 :dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        CheckBox textViewItemName;
        ImageView imageView;
        RecyclerView recyclerView;
        CheckBox mcolor;

        public ProductViewHolder(View itemView) {
            super(itemView);
            mcolor = itemView.findViewById(R.id.checkbox);

            mcolor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        FilterPreference.storeBooleanPreference(mCtx, "color" + getAdapterPosition(), true);
                        colorlist.add(String.valueOf(dataset.get(getAdapterPosition()).getId()));
                        colornames.add(dataset.get(getAdapterPosition()).getColorName());
                        setList("colorcategoryfilter", colorlist);
                        setList("colornames", colornames);
                        refreshSelection.refresh();

                    } else if (!isChecked) {
                        FilterPreference.storeBooleanPreference(mCtx, "color" + getAdapterPosition(), false);
                        colorlist.remove(String.valueOf(dataset.get(getAdapterPosition()).getId()));
                        colornames.remove(dataset.get(getAdapterPosition()).getColorName());
                        setList("colorcategoryfilter", colorlist);
                        setList("colornames", colornames);
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