package com.acube.jims.presentation.Report.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.acube.jims.R;
import com.acube.jims.databinding.ItemauditReportSingleBinding;
import com.acube.jims.datalayer.remote.db.DatabaseClient;
import com.acube.jims.datalayer.remote.db.LocalAudit;
import com.acube.jims.presentation.Audit.AuditScanActivity;
import com.acube.jims.utils.OnSingleClickListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;

public class GraphAuditItemReportAdapter extends RecyclerView.Adapter<GraphAuditItemReportAdapter.ProductViewHolder> {


    private Context mCtx;
    boolean isSelectedAll;
    int total, missing = 0, extra = 0, mismatch = 0, found = 0;
    int systemLocationID;
    int categoryId;
    int storeID;
    int itemID;
    List<String> uploadList;
    private final List<LocalAudit> dataset;
    FragmentTransition fragmenttransition;
    private Activity activity;

    public GraphAuditItemReportAdapter(Context mCtx, List<LocalAudit> dataset, FragmentTransition fragmenttransition
            , int systemLocationID, int categoryId, int storeID, int itemID) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.fragmenttransition = fragmenttransition;
        uploadList = new ArrayList<>();
        this.systemLocationID = systemLocationID;
        this.categoryId = categoryId;
        this.storeID = storeID;
        this.itemID = itemID;

    }

    public GraphAuditItemReportAdapter(Context mCtx, List<LocalAudit> dataset) {
        this.mCtx = mCtx;
        this.dataset = dataset;
        this.fragmenttransition = fragmenttransition;
        uploadList = new ArrayList<>();

    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        ItemauditReportSingleBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.itemaudit_report_single, parent, false);

        return new ProductViewHolder(binding, viewType);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, @SuppressLint("RecyclerView") int i) {
        LocalAudit graphReportCount = dataset.get(i);
        holder.binding.tvauditID.setText("Audit ID #." + graphReportCount.getAuditID());
        holder.setPieChart(graphReportCount.getAuditID());


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    class ProductViewHolder extends RecyclerView.ViewHolder {
        ItemauditReportSingleBinding binding;
        ImageView imageView;

        public ProductViewHolder(ItemauditReportSingleBinding binding, int viewType) {
            super(binding.getRoot());
            this.binding = binding;


        }

        private void setPieChart(String auditID) {

            new Thread(() -> {
                total = DatabaseClient.getInstance(mCtx).getAppDatabase().auditDownloadDao().totalcount(auditID, 0, 1, systemLocationID, categoryId, itemID, storeID);
                found = DatabaseClient.getInstance(mCtx).getAppDatabase().auditDownloadDao().getcountgraph(auditID, 1, systemLocationID, categoryId, itemID, storeID);
                missing = DatabaseClient.getInstance(mCtx).getAppDatabase().auditDownloadDao().getcountgraph(auditID, 0, systemLocationID, categoryId, itemID, storeID);
                extra = DatabaseClient.getInstance(mCtx).getAppDatabase().auditDownloadDao().getcountgraph(auditID, 2, systemLocationID, categoryId, itemID, storeID);
                mismatch = DatabaseClient.getInstance(mCtx).getAppDatabase().auditDownloadDao().getcountgraph(auditID, 3, systemLocationID, categoryId, itemID, storeID);

                ((Activity) mCtx).runOnUiThread(() -> {

                    setChart(binding.piechart, total, 0, "TOTAL", "", ColorTemplate.COLORFUL_COLORS);

                });
            }).start();


        }

        private void setChart(PieChart pieChart, int total, int value, String totallabel, String label, int[] colorfulColors) {
            List<PieEntry> pieEntryList = new ArrayList<>();
            pieChart.setUsePercentValues(false);
            if (value > 0) {
                pieChart.setMinAngleForSlices(30f);

            }
            pieChart.setEntryLabelColor(Color.WHITE);


            pieChart.getDescription().setEnabled(false);
            pieChart.getLegend().setTextColor(Color.BLACK);
            pieChart.getLegend().setTextSize(12f);
            pieChart.getLegend().setFormSize(15f);
            pieChart.getLegend().setEnabled(false);
            pieChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            pieChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
            pieChart.getLegend().setOrientation(Legend.LegendOrientation.VERTICAL);
            pieChart.setDrawSliceText(false);
       /* if (label.equalsIgnoreCase("")) {
            pieEntryList.add(new PieEntry(total, totallabel));
        } else {
            pieEntryList.add(new PieEntry(total, totallabel));
            pieEntryList.add(new PieEntry(value, label));
        }
*/
            int[] colors;
            ArrayList<Integer> color = new ArrayList<>();
            if (total != 0) {
                pieEntryList.add(new PieEntry(total, "TOTAL"));
                color.add(ContextCompat.getColor(mCtx, R.color.Total));
            }

            if (missing != 0) {
                pieEntryList.add(new PieEntry(missing, "MISSING"));
                color.add(ContextCompat.getColor(mCtx, R.color.Missing));

            }

            if (found != 0) {
                pieEntryList.add(new PieEntry(found, "FOUND"));
                color.add(ContextCompat.getColor(mCtx, R.color.Found));
            }

            if (extra != 0) {
                pieEntryList.add(new PieEntry(extra, "EXTRA"));
                color.add(ContextCompat.getColor(mCtx, R.color.Unknown));
            }
            if (mismatch != 0) {
                pieEntryList.add(new PieEntry(mismatch, "MISMATCH"));
                color.add(ContextCompat.getColor(mCtx, R.color.Mismatch));
            }

            PieDataSet pieDataSet = new PieDataSet(pieEntryList, "");
            pieDataSet.setColors(color);
            pieDataSet.setValueTextSize(15f);
            pieDataSet.setValueTextColor(Color.WHITE);

            // pieChart.setDrawEntryLabels(true);


            PieData  pieData = new PieData(pieDataSet);
            pieChart.setData(pieData);
            pieData.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value) {
                    return String.valueOf((int) Math.floor(value));
                }
            });

            pieChart.animateX(2000);

            pieChart.invalidate();

        }

    }

    public interface FragmentTransition {


        void addaction(int productId, String batchnumber, String lineitem, int locationID, int warehouseID);


    }


}
