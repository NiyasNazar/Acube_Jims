package com.acube.jims.presentation.Catalogue.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.utils.FilterPreference;
import com.acube.jims.utils.RefreshSelection;
import com.acube.jims.datalayer.models.Filter.SubCategory;
import com.google.gson.Gson;
import com.muddzdev.styleabletoast.StyleableToast;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ProductViewHolder> implements FilterChildAdapter.Passfilter {
    private int lastSelectedPosition = -1;


    private Context mCtx;


    List<SubCategory> dataset;
    List<String> subcatlist;
    List<String> subcatnames;
    RefreshSelection refreshSelection;
    boolean enableEdit;
    public SubCategoryAdapter(Context mCtx, List<SubCategory> dataset, RefreshSelection refreshSelection) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.refreshSelection = refreshSelection;
        subcatnames = new ArrayList<>();

        subcatlist = new ArrayList<>();

        enableEdit = FilterPreference.retrieveBooleanPreferences(mCtx, "enableEdit");

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
        int id=FilterPreference.retrieveIntegerPreferences(mCtx,"tempsubcat");

        if (enableEdit){
            holder.cardView.setCardBackgroundColor(dataset.get(position).isSelected() ? Color.parseColor("#18477F") : Color.parseColor("#BF8F3A"));


        }else{
            Log.d("subcategories", "onBindViewHolder: "+id);
            Log.d("subcategoriesssss", "onBindViewHolder: "+dataset.get(position).getId());

            if (dataset.get(position).getId() == id) {
                dataset.get(position).setSelected(true);
                holder.cardView.setCardBackgroundColor(Color.parseColor("#18477F"));
                Log.d("subcategoriesssss", "onBindViewHolder: "+dataset.get(position).getId());

            }
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

        TextView textViewItemName;
        CardView cardView;
        RecyclerView recyclerView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            textViewItemName = itemView.findViewById(R.id.tv_item_name);

            cardView = itemView.findViewById(R.id.btn_cat_filter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (enableEdit){
                        int position = getAbsoluteAdapterPosition();
                        dataset.get(position).setSelected(!dataset.get(position).isSelected());
                        if (dataset.get(position).isSelected()) {
                            if (!subcatlist.contains(dataset.get(position).getId())) {
                                subcatlist.add(String.valueOf(dataset.get(position).getId()));
                                setList("subcategoryfilter", subcatlist);
                                setList("subcatnames", subcatnames);
                            }


                        } else if (!dataset.get(position).isSelected()) {

                            subcatlist.remove(String.valueOf((dataset.get(position).getId())));
                            setList("subcategoryfilter", subcatlist);
                            setList("subcatnames", subcatnames);

                        }

                        cardView.setCardBackgroundColor(dataset.get(position).isSelected() ? Color.parseColor("#18477F") : Color.parseColor("#BF8F3A"));

                    }else{
                        new StyleableToast
                                .Builder(mCtx)
                                .text("Sub category is set to default")
                                .textColor(Color.WHITE)
                                .iconStart(R.drawable.ic_error)
                                .font(R.font.regular)
                                .gravity(Gravity.BOTTOM)
                                .length(Toast.LENGTH_LONG).solidBackground()
                                .backgroundColor(Color.RED)
                                .show();

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