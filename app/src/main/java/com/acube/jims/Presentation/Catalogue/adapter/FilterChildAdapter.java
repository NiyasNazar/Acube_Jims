package com.acube.jims.Presentation.Catalogue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.datalayer.models.Catalogue.ResponseCatalogueListing;
import com.acube.jims.datalayer.models.Filter.SubCategory;

import java.util.ArrayList;
import java.util.List;

public class FilterChildAdapter extends RecyclerView.Adapter<FilterChildAdapter.ProductViewHolder> {


    private Context mCtx;
    Passfilter passfilter;

    //  private final List<ResponseCatalogueListing> dataset;
    List<SubCategory> dataset;
    List<String> sublist;
    List<String> catlist;

    public FilterChildAdapter(Context mCtx, List<SubCategory> dataset,Passfilter passfilter) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        sublist = new ArrayList<>();
        catlist = new ArrayList<>();
        this.passfilter=passfilter;

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_child_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        // ResponseCatalogueListing responseCatalogueListing = dataset.get(position);
        holder.checkBox.setText(dataset.get(position).getSubCategoryName());


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName;
        ImageView imageView;
        RecyclerView recyclerView;
        CheckBox checkBox;

        public ProductViewHolder(View itemView) {
            super(itemView);

          //  textViewItemName = itemView.findViewById(R.id.tv_item_name);
            //imageView = itemView.findViewById(R.id.imageView);
            recyclerView = itemView.findViewById(R.id.sublist);
            checkBox = itemView.findViewById(R.id.checkbox);
            // recyclerView.setLayoutManager(new LinearLayoutManager(mCtx));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        Toast.makeText(mCtx, "gt" + dataset.get(getAdapterPosition()).getId(), Toast.LENGTH_SHORT).show();

                        passfilter.PassId(true,String.valueOf(dataset.get(getAdapterPosition()).getId()));
                        Log.e("onClickk", "onClick: " + catlist.toString());
                    }else if (!isChecked){
                        Log.e("onClickk", "onClick: " + catlist.toString());
                        passfilter.PassId(false,String.valueOf(dataset.get(getAdapterPosition()).getId()));
                    }

                }
            });

    /*        checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);


                    } else {
                        checkBox.setChecked(true);



                    }


                }
            });*/

        }

    }
    public interface  Passfilter{
        void PassId(Boolean flag,String id);
    }
}