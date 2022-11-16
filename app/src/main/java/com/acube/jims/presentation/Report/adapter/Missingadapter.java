package com.acube.jims.presentation.Report.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.datalayer.models.Audit.AuditReportItems;
import com.acube.jims.datalayer.models.Audit.AuditSnapShot;
import com.acube.jims.datalayer.models.Audit.TemDataSerial;
import com.acube.jims.datalayer.models.SelectionHolder;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.LocateProduct.View.LocateProduct;
import com.acube.jims.presentation.ScanItems.ResponseItems;
import com.acube.jims.R;
import com.acube.jims.datalayer.models.Audit.Missing;
import com.acube.jims.utils.OnSingleClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.rscja.team.qcom.deviceapi.S;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import ozaydin.serkan.com.image_zoom_view.ImageViewZoom;

public class Missingadapter extends RecyclerView.Adapter<Missingadapter.ProductViewHolder> {


    private Context mCtx;
    int row_index = -1;
    boolean isSelectedAll;
    private final List<SelectionHolder> dataset;
    List<String> datalist;
    PassId passId;
    boolean visibility = true;

    public Missingadapter(Context mCtx, List<SelectionHolder> dataset, boolean visibility, PassId passId) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.passId = passId;
        this.visibility = visibility;
        datalist = new ArrayList<>();


    }

    public Missingadapter(Context mCtx, List<SelectionHolder> dataset, PassId passId) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.passId = passId;
        datalist = new ArrayList<>();

    }

    public Missingadapter(Context mCtx, List<SelectionHolder> dataset, boolean visibility) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.visibility = visibility;


    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_missing_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        SelectionHolder missing = dataset.get(position);
        holder.textViewItemName.setText("" + missing.getItemCode());
        if (missing.getLocationCode() == null) {

        } else {

        }
        holder.textViewSerialNo.setText(missing.getSerialNumber());
        holder.textViewItemName.setText(missing.getItemCode());
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/Smarttraypics/" + missing.getItemCode() + ".JPG"); //Creates app specific folder

        Log.d("Fileimage", "onBindViewHolder: " + file);
//        holder.tvcategory.setText("Category : " + missing.getCategoryName());
        Glide.with(mCtx)
                .load(missing.getImageUrl() == null ? file : missing.getImageUrl())
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
            holder.removeall();
        } else {
            holder.locatecheckbox.setChecked(true);
            holder.update();
        }


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView textViewItemName, textViewLoccode, textViewSerialNo, tvlocationname, tvkarat, tvcategory, tv_item_code;
        ImageViewZoom imageView;
        CheckBox locatecheckbox;
        ResponseItems responseItems;
        RelativeLayout selection;
        TableRow tableRow4;

        public ProductViewHolder(View itemView) {
            super(itemView);
            tvlocationname = itemView.findViewById(R.id.tvlocationname);
            tvkarat = itemView.findViewById(R.id.tvkarat);
            tvcategory = itemView.findViewById(R.id.tvcategory);
            tv_item_code = itemView.findViewById(R.id.tv_item_code);
            textViewItemName = itemView.findViewById(R.id.tv_item_name);
            //     selection = itemView.findViewById(R.id.layoutparent);
            imageView = itemView.findViewById(R.id.imvitemimage);
            selection = itemView.findViewById(R.id.likelayout);
            if (visibility) {
                selection.setVisibility(View.VISIBLE);
            } else {
                selection.setVisibility(View.GONE);
            }


            textViewLoccode = itemView.findViewById(R.id.tvlocationcode);
            textViewSerialNo = itemView.findViewById(R.id.tv_serialnumber);
            locatecheckbox = itemView.findViewById(R.id.fav_button);
            imageView.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    int pos=getAbsoluteAdapterPosition();
                    if (dataset.get(pos).getImageUrl()!=null){
                        passId.enlargeImage(v, dataset.get(getAbsoluteAdapterPosition()).getImageUrl());

                    }


                }
            });

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
                        passId.compareitems(dataset.get(getAbsoluteAdapterPosition()).getSerialNumber(), isChecked);
                    } else if (!isChecked) {
                        passId.compareitems(dataset.get(getAbsoluteAdapterPosition()).getSerialNumber(), isChecked);
                    }
                }
            });


        }

        public void update() {


            new Thread(() -> {
                DatabaseClient.getInstance(mCtx).getAppDatabase().auditDownloadDao().insertMissingselection(dataset);


            }).start();
        }


        public void removeall() {

            new Thread(() -> {
                DatabaseClient.getInstance(mCtx).getAppDatabase().auditDownloadDao().deleteSelectionHolder();


            }).start();
        }
    }

    public interface PassId {
        void passid(String id, Integer locid);

        void enlargeImage(View view, String imageUrl);

        void compareitems(String serial, boolean checked);

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