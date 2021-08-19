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
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.acube.jims.R;
import com.acube.jims.Utils.FilterPreference;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.Utils.RefreshSelection;
import com.acube.jims.datalayer.models.Filter.Catresult;
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
        holder.textViewkaratcheckbox.setText(dataset.get(position).getKaratName());
        // holder.imageView.setImageResource(homeData.getImage());
        boolean ischecked = FilterPreference.retrieveBooleanPreferences(mCtx, "karat" + position);
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
                        FilterPreference.storeBooleanPreference(mCtx, "karat" + getAdapterPosition(), true);
                        karatlist.add(String.valueOf(dataset.get(getAdapterPosition()).getId()));
                        karatnames.add(dataset.get(getAdapterPosition()).getKaratName());
                        setList("karatfilter", karatlist);
                        Log.d("TAG", "onCheckedChanged: "+karatlist.toString());

                        setList("karatnames", karatnames);
                        refreshSelection.refresh();

                    } else if (!isChecked) {
                        FilterPreference.storeBooleanPreference(mCtx, "karat" + getAdapterPosition(), false);
                        karatlist.remove(String.valueOf(dataset.get(getAdapterPosition()).getId()));
                        karatnames.remove(dataset.get(getAdapterPosition()).getKaratName());
                        setList("karatnames", karatnames);
                        Log.d("TAG", "onCheckedChanged: "+karatlist.toString());
                        setList("karatfilter", karatlist);
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