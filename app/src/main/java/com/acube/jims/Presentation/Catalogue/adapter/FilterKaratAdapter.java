package com.acube.jims.Presentation.Catalogue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.datalayer.models.Filter.Catresult;
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FilterKaratAdapter extends RecyclerView.Adapter<FilterKaratAdapter.ProductViewHolder> {


    private Context mCtx;


    private List<Karatresult> dataset;
    List<SubCategory> sublist;
    List<String> karatlist;

    public FilterKaratAdapter(Context mCtx, List<Karatresult> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        sublist = new ArrayList<>();
        karatlist=new ArrayList<>();

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


    }


    @Override
    public int getItemCount() {
        return dataset.size();
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
                        karatlist.add(String.valueOf(dataset.get(getAdapterPosition()).getId()));
                        setList("karatfilter", karatlist);

                    } else if (!isChecked) {
                        karatlist.remove(String.valueOf(dataset.get(getAdapterPosition()).getId()));
                        setList("karatfilter", karatlist);
                    }
                }
            });

        }
    }
    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(mCtx, key, json);
    }
}