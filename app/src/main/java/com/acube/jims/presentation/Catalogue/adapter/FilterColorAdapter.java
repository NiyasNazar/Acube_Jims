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
import com.acube.jims.datalayer.models.Filter.Colorresult;
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

        holder.textViewItemName.setText(dataset.get(position).getColorName());
        // ResponseCatalogueListing responseCatalogueListing = dataset.get(position);
        //   holder.textViewItemName.setText(dataset.get(position).getKaratName());
        // holder.imageView.setImageResource(homeData.getImage());
        holder.cardView.setCardBackgroundColor(dataset.get(position).isSelected() ? Color.parseColor("#18477F") : Color.parseColor("#BF8F3A"));



    }


    @Override
    public int getItemCount() {
        return dataset==null ? 0 :dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName;
        CardView cardView;
        RecyclerView recyclerView;
        CheckBox mcolor;

        public ProductViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.btn_cat_filter);
            textViewItemName = itemView.findViewById(R.id.tv_item_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    dataset.get(position).setSelected(!dataset.get(position).isSelected());
                    if (dataset.get(position).isSelected()) {
                        if (!colorlist.contains(dataset.get(position).getId())) {
                            colorlist.add(String.valueOf(dataset.get(position).getId()));
                            colornames.add(dataset.get(position).getColorName());
                            setList("colorcategoryfilter", colorlist);
                            setList("colornames", colornames);
                        }
                    }else{
                        FilterPreference.storeBooleanPreference(mCtx, "color" + position, false);
                        colorlist.remove(String.valueOf(dataset.get(position).getId()));
                        colornames.remove(dataset.get(position).getColorName());
                        setList("colorcategoryfilter", colorlist);
                        setList("colornames", colornames);
                    }


                    cardView.setCardBackgroundColor(dataset.get(position).isSelected() ? Color.parseColor("#18477F") : Color.parseColor("#BF8F3A"));

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