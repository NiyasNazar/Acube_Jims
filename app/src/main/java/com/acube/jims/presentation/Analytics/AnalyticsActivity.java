package com.acube.jims.presentation.Analytics;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.BaseActivity;
import com.acube.jims.presentation.Analytics.ViewModel.AnalyticsViewModel;
import com.acube.jims.presentation.Analytics.ViewModel.WarehouseViewModel;
import com.acube.jims.presentation.Analytics.adapter.ItemwiseAnalyticsadapter;
import com.acube.jims.presentation.Analytics.adapter.PieChartadapter;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.FragmentAnalyticsBinding;
import com.acube.jims.datalayer.models.Analytics.AnalyticsCustomersServed;
import com.acube.jims.datalayer.models.Analytics.AnalyticsDataSingleBar;
import com.acube.jims.datalayer.models.Analytics.AnalyticsItemWiseViewed;
import com.acube.jims.datalayer.models.Analytics.FilterPeriod;
import com.acube.jims.datalayer.models.Analytics.ResponseAnalyticsGraph;
import com.acube.jims.datalayer.models.Analytics.ResponseAnalyticsSummary;
import com.acube.jims.datalayer.models.Analytics.ResponseCustomerServed;
import com.acube.jims.datalayer.models.Analytics.ResponseItemWiseAnalytics;
import com.acube.jims.datalayer.models.warehouse.ResponseWareHouse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AnalyticsActivity extends BaseActivity {


    FragmentAnalyticsBinding binding;
    AnalyticsViewModel analyticsViewModel;
    private BarChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;
    ArrayList<BarEntry> x;
    ArrayList<String> y;
    WarehouseViewModel warehouseViewModel;
    List<FilterPeriod> datasetperiod;
    int parentfilter = 0, categoryfilter = 0, servedfilter = 0, warehouseid = 0, itemfilter = 0;
    List<ResponseWareHouse> responseWareHouses;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        binding = DataBindingUtil.setContentView(
                this, R.layout.fragment_analytics);
        initToolBar(binding.toolbarApp.toolbar, "Analytics", true);
        analyticsViewModel = new ViewModelProvider(this).get(AnalyticsViewModel.class);
        analyticsViewModel.init();

        showProgressDialog();

        warehouseViewModel = new ViewModelProvider(this).get(WarehouseViewModel.class);
        warehouseViewModel.init();

        x = new ArrayList<BarEntry>();
        y = new ArrayList<String>();
        initializeBarChart(binding.categorychart);
        responseWareHouses = new ArrayList<>();
        fetchAnalyticsSummary();
        fetchGraphData();
        datasetperiod = new ArrayList<>();
        datasetperiod.add(new FilterPeriod(0, "Today"));
        datasetperiod.add(new FilterPeriod(1, "This week"));
        datasetperiod.add(new FilterPeriod(2, "Last week"));
        datasetperiod.add(new FilterPeriod(3, "Last 30 days"));
        datasetperiod.add(new FilterPeriod(4, "This Year"));
        ArrayAdapter<FilterPeriod> arrayAdapter = new ArrayAdapter<FilterPeriod>(getApplicationContext(), android.R.layout.simple_spinner_item, datasetperiod);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.edPeriod.setAdapter(arrayAdapter);
        binding.edCategroyfilter.setAdapter(arrayAdapter);
        binding.edServrdfilter.setAdapter(arrayAdapter);
        binding.edItemfilter.setAdapter(arrayAdapter);
        fetchItemWiseSummary();
        binding.edStoreselection.setTitle("Select Store");

        binding.edPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parentfilter = datasetperiod.get(position).getId();
                Log.d("TAG", "onItemSelected: " + datasetperiod.get(position).getValue());
                fetchAnalyticsSummary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.edItemfilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itemfilter = datasetperiod.get(position).getId();
                fetchItemWiseSummary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.recyvitemwisefilter.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.edCategroyfilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryfilter = datasetperiod.get(position).getId();
                fetchGraphData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.edServrdfilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                servedfilter = datasetperiod.get(position).getId();
                fetchCustomerServedData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.recyvcustomerserved.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));


        analyticsViewModel.getLiveData().observe(this, new Observer<ResponseAnalyticsSummary>() {
            @Override
            public void onChanged(ResponseAnalyticsSummary responseAnalyticsSummary) {
                hideProgressDialog();
                if (responseAnalyticsSummary != null) {
                    binding.tvnotviewedcount.setText("" + responseAnalyticsSummary.getProductsNotViewed());
                    binding.tvtotalcustviewed.setText("" + responseAnalyticsSummary.getTotalCustomersServed());
                    binding.tvviewcount.setText("" + responseAnalyticsSummary.getProductsViewed());

                }
            }
        });
        analyticsViewModel.getItemwiseLiveData().observe(this, new Observer<ResponseItemWiseAnalytics>() {
            @Override
            public void onChanged(ResponseItemWiseAnalytics responseItemWiseAnalytics) {
                hideProgressDialog();
                if (responseItemWiseAnalytics != null) {
                    List<AnalyticsItemWiseViewed> analyticsItemWiseViewed = responseItemWiseAnalytics.getAnalyticsItemWiseViewed();
                    if (analyticsItemWiseViewed.size() != 0) {
                        binding.recyvitemwisefilter.setAdapter(new ItemwiseAnalyticsadapter(getApplicationContext(), analyticsItemWiseViewed));
                        binding.noItemLayout2.layoutnoitem.setVisibility(View.GONE);
                    } else {
                        binding.noItemLayout2.layoutnoitem.setVisibility(View.VISIBLE);

                    }
                }
            }
        });


        warehouseViewModel.FetchWareHouses(LocalPreferences.getToken(getApplicationContext()));
        warehouseViewModel.getLiveData().observe(this, new Observer<List<ResponseWareHouse>>() {
            @Override
            public void onChanged(List<ResponseWareHouse> dataset) {
                hideProgressDialog();
                if (dataset != null) {
                    responseWareHouses = dataset;
                    ResponseWareHouse wh = new ResponseWareHouse();
                    wh.setWarehouseName("All");
                    wh.setId(0);
                    responseWareHouses.add(wh);
                    Collections.sort(responseWareHouses, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            ResponseWareHouse p1 = (ResponseWareHouse) o1;
                            ResponseWareHouse p2 = (ResponseWareHouse) o2;
                            return p1.getWarehouseName().compareToIgnoreCase(p2.getWarehouseName());
                        }
                    });
                    ArrayAdapter<ResponseWareHouse> arrayAdapter = new ArrayAdapter<ResponseWareHouse>(getApplicationContext(), android.R.layout.simple_spinner_item, responseWareHouses);
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.edStoreselection.setAdapter(arrayAdapter);
                }

            }
        });

        binding.edStoreselection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                warehouseid = responseWareHouses.get(position).getId();
                fetchAnalyticsSummary();
                fetchGraphData();
                fetchItemWiseSummary();
                fetchCustomerServedData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fetchCustomerServedData();


        analyticsViewModel.getGraphLiveData().observe(this, new Observer<ResponseAnalyticsGraph>() {
            @Override
            public void onChanged(ResponseAnalyticsGraph responseAnalyticsGraph) {
                hideProgressDialog();
                if (responseAnalyticsGraph != null) {
                    List<AnalyticsDataSingleBar> dataset = responseAnalyticsGraph.getAnalyticsDataSingleBar();

                    if (dataset != null && dataset.size() != 0) {
                        createBarChart(dataset, binding.categorychart, false);
                    } else {
                        binding.categorychart.clear();
                    }

                }
            }
        });

        analyticsViewModel.customerServedLiveData().observe(this, new Observer<ResponseCustomerServed>() {
            @Override
            public void onChanged(ResponseCustomerServed responseCustomerServed) {
                if (responseCustomerServed != null) {
                    hideProgressDialog();
                    List<AnalyticsCustomersServed> dataset = responseCustomerServed.getAnalyticsCustomersServed();

                    if (dataset.size()!=0){
                        binding.noItemLayout3.layoutnoitem.setVisibility(View.GONE);
                        binding.recyvcustomerserved.setAdapter(new PieChartadapter(getApplicationContext(), dataset));
                    }else{
                        binding.noItemLayout3.layoutnoitem.setVisibility(View.VISIBLE);

                    }


                }
            }
        });

        binding.webview.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Handle the error
                Log.e("error", "ReceivedError on WebView. ERROR CODE is " + errorCode);
                Log.e("error", "description is " + description);
                Log.e("error", "failingUrl is " + failingUrl);
                try {
                    view.loadUrl("file:///android_asset/www/error.html?errorCode=" + errorCode + "&errorDescription=" + description);
                } catch (Exception e) {
                    Log.e("error", e.toString());
                }

            }
        });
    }

    private void fetchGraphData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", 0);
        jsonObject.addProperty("categoryPeriodFilter", categoryfilter);
        jsonObject.addProperty("itemPeriodFilter", 0);
        jsonObject.addProperty("employeePeriodFilter", 0);
        analyticsViewModel.AnalyticGraph(LocalPreferences.getToken(getApplicationContext()), jsonObject);
    }

    private void fetchAnalyticsSummary() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", parentfilter);
        jsonObject.addProperty("categoryPeriodFilter", 0);
        jsonObject.addProperty("itemPeriodFilter", 0);
        jsonObject.addProperty("employeePeriodFilter", 0);
        //jsonObject.addProperty("companyID");

        analyticsViewModel.AnalyticSummary(LocalPreferences.getToken(getApplicationContext()), jsonObject);
    }

    private void fetchItemWiseSummary() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", parentfilter);
        jsonObject.addProperty("categoryPeriodFilter", 0);
        jsonObject.addProperty("itemPeriodFilter", itemfilter);
        jsonObject.addProperty("employeePeriodFilter", 0);
        //jsonObject.addProperty("companyID");

        analyticsViewModel.ItemWiseAnalytics(LocalPreferences.getToken(getApplicationContext()), jsonObject);
    }

    private void fetchCustomerServedData() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", 0);
        jsonObject.addProperty("categoryPeriodFilter", 0);
        jsonObject.addProperty("itemPeriodFilter", 0);
        jsonObject.addProperty("employeePeriodFilter", servedfilter);
        analyticsViewModel.CustomerServed(LocalPreferences.getToken(getApplicationContext()), jsonObject);
    }


    private void createBarChart(List<AnalyticsDataSingleBar> chartValues, BarChart chart, Boolean isTrim) {
        ArrayList<BarEntry> values = new ArrayList<>();
        ArrayList<String> severityStringList = new ArrayList<>();


        for (int i = 0; i < chartValues.size(); i++) {
            AnalyticsDataSingleBar dataObject = chartValues.get(i);
            values.add(new BarEntry(i, dataObject.getValue()));
            String value = chartValues.get(i).getName();
            if (isTrim) {
                if (value.length() > 10) {
                    severityStringList.add(value.substring(0, 9));

                } else {


                }
            } else {
                severityStringList.add(value);
            }

        }

        BarDataSet set1;

      /*  if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
            chart.invalidate();
        } else {*/
        set1 = new BarDataSet(values, "Data Set");

        set1.setDrawValues(true);
        set1.setColor(Color.parseColor("#BF8F3A"));
        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);

        chart.setData(data);
        chart.setMaxVisibleValueCount(5);
        chart.moveViewToX(2);
        chart.setVisibleXRange(1, 5);
        chart.setFitBars(false);
        chart.setHorizontalScrollBarEnabled(true);
        XAxis xAxis = chart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        //    chart.setXAxisRenderer(new CustomXAxisRenderer(chart.getViewPortHandler(), chart.getXAxis(), chart.getTransformer(YAxis.AxisDependency.LEFT)));

        xAxis.setValueFormatter(new IndexAxisValueFormatter(severityStringList));//setting String values in Xaxis
        for (IDataSet set : chart.getData().getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

        chart.invalidate();
        //    }
    }

    private void initializeBarChart(BarChart chart) {
        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.getXAxis().setTextColor(R.color.black);
        chart.getXAxis().setDrawGridLines(false);
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        chart.setExtraOffsets(5, 5, 5, 40);
        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(false);

        xAxis.setLabelRotationAngle(-20);
        chart.getAxisLeft().setTextColor(Color.BLACK); // left y-axis
        chart.getXAxis().setTextColor(Color.BLACK);
        chart.getLegend().setTextColor(Color.BLACK);
        chart.getDescription().setTextColor(Color.BLACK);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setEnabled(true);
        chart.getXAxis().setDrawGridLines(false);
        chart.animateY(1500);

        // chart.setXAxisRenderer(new CustomXAxisRenderer(chart.getViewPortHandler(), chart.getXAxis(), chart.getTransformer(YAxis.AxisDependency.LEFT)));
        chart.getLegend().setEnabled(false);

        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisLeft().setDrawLabels(true);
        chart.setTouchEnabled(true);
        chart.setDoubleTapToZoomEnabled(false);
        chart.getXAxis().setEnabled(true);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.invalidate();

    }

}







