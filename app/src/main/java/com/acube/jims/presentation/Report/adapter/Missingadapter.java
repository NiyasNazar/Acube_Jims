package com.acube.jims.presentation.Report.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.datalayer.models.Audit.AuditReportItems;
import com.acube.jims.datalayer.models.Audit.AuditSnapShot;
import com.acube.jims.presentation.ScanItems.ResponseItems;
import com.acube.jims.R;
import com.acube.jims.datalayer.models.Audit.Missing;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Missingadapter extends RecyclerView.Adapter<Missingadapter.ProductViewHolder> {


    private Context mCtx;
    int row_index = -1;
    boolean isSelectedAll;
    private final List<AuditSnapShot> dataset;
    List<String> datalist;
    PassId passId;
    boolean visibility=true;


    public Missingadapter(Context mCtx, List<AuditSnapShot> dataset, PassId passId) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.passId = passId;
        datalist = new ArrayList<>();

    }
    public Missingadapter(Context mCtx, List<AuditSnapShot> dataset,boolean visibility) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.visibility=visibility;


    }
    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_missing_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        AuditSnapShot missing = dataset.get(position);
        holder.tvlocationname.setText("System Location : " + missing.getSystemLocationName());
        holder.textViewItemName.setText("Item : " + missing.getItemCode());
        if (missing.getLocationCode() == null) {
            holder.textViewLoccode.setText("Scanned Location : " + "N/A");

        } else {
            holder.textViewLoccode.setText("Scanned Location : " + missing.getLocationName());

        }
        holder.textViewSerialNo.setText("Sl No. : " + missing.getSerialNumber());


//        holder.tvcategory.setText("Category : " + missing.getCategoryName());
        Glide.with(mCtx)
                .load(missing.getImageUrl())
                .placeholder(R.drawable.jwimage)
                .error(R.drawable.jwimage)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        // log exception
                        Log.e("TAG", "Error loading image", e);
                        return false; // important to return false so the error placeholder can be placed
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.imageView);

        if (!isSelectedAll) {
            holder.locatecheckbox.setChecked(false);
            datalist = new ArrayList<>();
        } else {
            holder.locatecheckbox.setChecked(true);
            datalist.add(String.valueOf(dataset.get(position).getSerialNumber()));
        }


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName, textViewLoccode, textViewSerialNo, tvlocationname, tvkarat, tvcategory;
        ImageView imageView;
        CheckBox locatecheckbox;
        ResponseItems responseItems;
        RelativeLayout selection;
        TableRow tableRow4;

        public ProductViewHolder(View itemView) {
            super(itemView);
            tvlocationname = itemView.findViewById(R.id.tvlocationname);
            tvkarat = itemView.findViewById(R.id.tvkarat);
            tvcategory = itemView.findViewById(R.id.tvcategory);
            textViewItemName = itemView.findViewById(R.id.tvitemname);
            //     selection = itemView.findViewById(R.id.layoutparent);
            imageView = itemView.findViewById(R.id.imvitemimage);
            selection = itemView.findViewById(R.id.checkboxlayt);
            if (visibility){
                selection.setVisibility(View.VISIBLE);
            }else{
                selection.setVisibility(View.GONE);
            }


            textViewLoccode = itemView.findViewById(R.id.tvlocationcode);
            textViewSerialNo = itemView.findViewById(R.id.tv_serialnumber);
            locatecheckbox = itemView.findViewById(R.id.checkBox);
            textViewSerialNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    passId.passid(dataset.get(getAbsoluteAdapterPosition()).getSerialNumber(), dataset.get(getBindingAdapterPosition()).getScanLocationID());
                }
            });

            locatecheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        datalist.add(String.valueOf(dataset.get(getAbsoluteAdapterPosition()).getSerialNumber()));
                        passId.compareitems(dataset.get(getAbsoluteAdapterPosition()).getSerialNumber(),isChecked);


                    } else if (!isChecked) {
                        datalist.remove(String.valueOf(dataset.get(getAbsoluteAdapterPosition()).getSerialNumber()));
                        passId.compareitems(dataset.get(getAbsoluteAdapterPosition()).getSerialNumber(),isChecked);
                    }
                }
            });
        }
    }

    public interface PassId {
        void passid(String id, Integer locid);

        void compareitems(String serial,boolean checked);
    }

    public void selectAll() {
        isSelectedAll = true;
        notifyDataSetChanged();
    }

    public void unselectall() {
        isSelectedAll = false;
        notifyDataSetChanged();
    }
}