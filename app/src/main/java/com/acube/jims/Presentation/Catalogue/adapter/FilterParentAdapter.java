package com.acube.jims.Presentation.Catalogue.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.Filter.Catresult;
import com.acube.jims.datalayer.models.Filter.Karatresult;
import com.acube.jims.datalayer.models.Filter.ResponseFetchFilters;
import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FilterParentAdapter extends RecyclerView.Adapter<FilterParentAdapter.ProductViewHolder> implements FilterChildAdapter.Passfilter {


    private Context mCtx;

    private List<ResponseFetchFilters> dataset;
    List<SubCategory> sublist;
    List<String> catlist;


    public FilterParentAdapter(Context mCtx, List<ResponseFetchFilters> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        sublist = new ArrayList<>();
        catlist = new ArrayList<>();


    }

    public FilterParentAdapter(Context mCtx) {
        this.mCtx = mCtx;
        sublist = new ArrayList<>();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_sub_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        // ResponseCatalogueListing responseCatalogueListing = dataset.get(position);
        holder.textViewItemName.setText(dataset.get(position).getCategoryName());
        // holder.imageView.setImageResource(homeData.getImage());


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public void PassId(Boolean flag, String id) {
        if (flag) {
            catlist.add(id);
            setList("subcategoryfilter",catlist);

        } else {
            catlist.remove(id);
            setList("subcategoryfilter",catlist);
        }

        Log.d("PassId", "PassId: " + catlist.toString()+flag);
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        RadioButton textViewItemName;
        ImageView arrow;
        RecyclerView recyclerView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tv_item_name);

            arrow = itemView.findViewById(R.id.arrow);
            recyclerView = itemView.findViewById(R.id.sublist);
            recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    sublist = dataset.get(pos).getSubCategories();
                    recyclerView.setAdapter(new FilterChildAdapter(mCtx, sublist, FilterParentAdapter.this::PassId));
                    if (recyclerView.getVisibility() == View.VISIBLE) {

                        // The transition of the hiddenView is carried out
                        //  by the TransitionManager class.
                        // Here we use an object of the AutoTransition
                        // Class to create a default transition.
                        TransitionManager.beginDelayedTransition(recyclerView,
                                new AutoTransition());
                        recyclerView.setVisibility(View.GONE);
                        arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
                    } else {
                        arrow.setImageResource(R.drawable.ic_baseline_expand_less_24);

                        recyclerView.setVisibility(View.VISIBLE);

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