package com.acube.jims.presentation.Report.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseBooleanArray;
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

import com.acube.jims.R;
import com.acube.jims.datalayer.models.SelectionHolder;
import com.acube.jims.datalayer.models.report.ItemWiseReport;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.presentation.ScanItems.ResponseItems;
import com.acube.jims.utils.ICheckChangeListener;
import com.acube.jims.utils.OnSingleClickListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.like.LikeButton;
import com.rscja.team.qcom.deviceapi.S;

import java.util.ArrayList;
import java.util.List;

public class ItemWiseadapter extends RecyclerView.Adapter<ItemWiseadapter.ProductViewHolder> {


    private Context mCtx;
    int row_index = -1;
    boolean isSelectedAll;
    private final List<ItemWiseReport> dataset;
    List<String> datalist;
    PassId passId;
    boolean visibility = true;
    List<SelectionHolder> datselect;
    String status;
    SparseBooleanArray checkedItems;

    public ItemWiseadapter(Context mCtx, List<ItemWiseReport> dataset, boolean visibility, PassId passId, String status) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.passId = passId;
        this.visibility = visibility;
        datalist = new ArrayList<>();
        datselect = new ArrayList<>();
        this.status = status;
        checkedItems = new SparseBooleanArray();
    }

    public ItemWiseadapter(Context mCtx, List<ItemWiseReport> dataset, PassId passId) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.passId = passId;
        datalist = new ArrayList<>();

    }

    public ItemWiseadapter(Context mCtx, List<ItemWiseReport> dataset, boolean visibility) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.visibility = visibility;


    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_item_wise, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        ItemWiseReport missing = dataset.get(position);
        holder.textviewDesc.setText(missing.getItemDesc());
        holder.textViewItemName.setText("" + missing.getItemCode());
        holder.textViewCode.setText(missing.getItemCode());
        holder.tv_weight.setText("" + missing.getWeight() + " g");


        holder.textViewSerialnumber.setText(missing.getSerialNumber());
        if (datalist.contains(missing.getSerialNumber())) {
            holder.selectionlayout.setChecked(true);
        } else {
            holder.selectionlayout.setChecked(false);

        }

      holder.selectionlayout.setOnClickListener(new OnSingleClickListener() {
          @Override
          public void onSingleClick(View v) {
              checkCheckBox(position, !checkedItems.get(position));
          }
      });

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





    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItemName, tv_weight,
                textViewGrossWeight, textViewSerialnumber, textViewPrice,
                textViewKarat, textViewCode, textviewDesc;
        ImageView imageView;
        RelativeLayout likelayt;
        CheckBox selectionlayout;
        private ICheckChangeListener iCheckChangeListener;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textViewItemName = itemView.findViewById(R.id.tv_item_name);
            tv_weight = itemView.findViewById(R.id.tv_weight);
//            mlikebtn = itemView.findViewById(R.id.fav_button);
            textViewCode = itemView.findViewById(R.id.tv_item_code);
            textViewSerialnumber = itemView.findViewById(R.id.tv_serialnumber);
            textViewGrossWeight = itemView.findViewById(R.id.tvgrossweight);
            textViewPrice = itemView.findViewById(R.id.tvprice);
            textviewDesc = itemView.findViewById(R.id.tv_description);
            textViewKarat = itemView.findViewById(R.id.tvkarat);
            textviewDesc = itemView.findViewById(R.id.tv_description);
            imageView = itemView.findViewById(R.id.imageView);
            selectionlayout = itemView.findViewById(R.id.fav_button);
            likelayt = itemView.findViewById(R.id.likelayout);
            if (status.equalsIgnoreCase("missing")) {
                likelayt.setVisibility(View.VISIBLE);

            } else {
                likelayt.setVisibility(View.GONE);

            }


     /*       selectionlayout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        datalist.add(String.valueOf(dataset.get(getAbsoluteAdapterPosition()).getSerialNumber()));

                        passId.compareitems(dataset.get(getAbsoluteAdapterPosition()).getSerialNumber(), isChecked);
                        iCheckChangeListener.onItemChecked(getAbsoluteAdapterPosition(), isChecked);

                    } else if (!isChecked) {
                        iCheckChangeListener.onItemChecked(getAbsoluteAdapterPosition(), isChecked);
                        dataset.get(getAbsoluteAdapterPosition()).setSelected(false);

                        passId.compareitems(dataset.get(getAbsoluteAdapterPosition()).getSerialNumber(), isChecked);
                    }
                }
            });*/
            if (visibility) {
                likelayt.setVisibility(View.VISIBLE);
            } else {
                likelayt.setVisibility(View.GONE);
            }

        }

        public void update() {


            new Thread(() -> {

                DatabaseClient.getInstance(mCtx).getAppDatabase().auditDownloadDao().insertItemWiseReport(dataset);


            }).start();
        }


        public void removeall() {

            new Thread(() -> {
                DatabaseClient.getInstance(mCtx).getAppDatabase().auditDownloadDao().deleteItemWiseReport();


            }).start();

        }

        void setICheckChangeListener(ICheckChangeListener iCheckChangeListener) {
            this.iCheckChangeListener = iCheckChangeListener;
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
    public void removeSelection() {
        checkedItems = new SparseBooleanArray();
        datalist = new ArrayList<>();
        notifyDataSetChanged();
    }
    public void unselectall() {
        isSelectedAll = false;
        notifyDataSetChanged();
    }
    public void checkCheckBox(int position, boolean value) {
        passId.compareitems(dataset.get(position).getSerialNumber(), value);

        if (value) {
            datalist.add(dataset.get(position).getSerialNumber());
            checkedItems.put(position, true);
        } else {
            checkedItems.delete(position);
            datalist.remove(dataset.get(position).getSerialNumber());
        }


        notifyDataSetChanged();
    }
}