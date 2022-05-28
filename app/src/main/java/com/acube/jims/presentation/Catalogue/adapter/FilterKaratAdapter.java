package com.acube.jims.presentation.Catalogue.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.utils.FilterPreference;
import com.acube.jims.utils.RefreshSelection;
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FilterKaratAdapter extends RecyclerView.Adapter<FilterKaratAdapter.ProductViewHolder> {


    private Context mCtx;
    private int lastSelectedPosition = -1;


    private List<Karatresult> dataset;
    List<SubCategory> sublist;
    List<String> karatlist;
    RefreshSelection refreshSelection;
    List<String>karatnames;

    public FilterKaratAdapter(Context mCtx, List<Karatresult> dataset, RefreshSelection refreshSelection) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        sublist = new ArrayList<>();
        karatlist = new ArrayList<>();
        this.refreshSelection = refreshSelection;
        karatnames=new ArrayList<>();

    }

    public FilterKaratAdapter(Context mCtx) {
        this.mCtx = mCtx;
        sublist = new ArrayList<>();
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
        holder.textViewItemName.setText(dataset.get(position).getKaratName());
        holder.cardView.setCardBackgroundColor(dataset.get(position).isSelected() ? Color.parseColor("#18477F") : Color.parseColor("#BF8F3A"));


    }


    @Override
    public int getItemCount() {
        return dataset==null ? 0 :dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName;
        CardView cardView;
        CheckBox textViewkaratcheckbox;
        ImageView imageView;
        RecyclerView recyclerView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.btn_cat_filter);

            textViewItemName = itemView.findViewById(R.id.tv_item_name);
            //imageView = itemView.findViewById(R.id.imageView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAbsoluteAdapterPosition();
                    dataset.get(position).setSelected(!dataset.get(position).isSelected());
                    if (dataset.get(position).isSelected()) {
                        if (!karatlist.contains(dataset.get(position).getId())) {
                            karatlist.add(String.valueOf(dataset.get(position).getId()));
                            karatnames.add(dataset.get(position).getKaratName());
                            setList("karatfilter", karatlist);
                            setList("karatnames", karatnames);
                            FilterPreference.storeBooleanPreference(mCtx, "karat" + position, true);

                        }
                    }else if (!dataset.get(position).isSelected()) {
                        FilterPreference.storeBooleanPreference(mCtx, "karat" + position, false);

                        karatlist.remove(String.valueOf(dataset.get(position).getId()));
                        karatnames.remove(dataset.get(position).getKaratName());
                        setList("karatnames", karatnames);
                        Log.d("TAG", "onCheckedChanged: "+karatlist.toString());
                        setList("karatfilter", karatlist);

                    }



                        Log.d("TAG", "onCheckedChanged: "+karatlist.toString());

                      //  refreshSelection.refresh();


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