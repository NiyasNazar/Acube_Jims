package com.acube.jims.presentation.DashBoard;

import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.acube.jims.BaseActivity;
import com.acube.jims.presentation.DashBoard.adapter.DashBoardPieChartadapter;
import com.acube.jims.presentation.DashBoard.adapter.ToptenSoldAdapter;
import com.acube.jims.presentation.DashBoard.adapter.ToptenSoldCategoryAdapter;
import com.acube.jims.R;
import com.acube.jims.utils.LocalPreferences;
import com.acube.jims.databinding.FragmentDashBoardBinding;
import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Analytics.FilterPeriod;
import com.acube.jims.datalayer.models.Dashboard.DashboardCategoryWiseSold;
import com.acube.jims.datalayer.models.Dashboard.DashboardDataSingleBar;
import com.acube.jims.datalayer.models.Dashboard.DashboardEmployeeAnalytic;
import com.acube.jims.datalayer.models.Dashboard.DashboardItemWiseSold;
import com.acube.jims.datalayer.models.Dashboard.ResponseDashBoardGraph;
import com.acube.jims.datalayer.models.Dashboard.ResponseDashboardSummary;
import com.acube.jims.datalayer.models.Dashboard.ResponseDashboardpiechart;
import com.acube.jims.datalayer.models.Dashboard.ResponseTop10ProductsSold;
import com.acube.jims.datalayer.models.Dashboard.ResponseTopCategory;
import com.acube.jims.datalayer.models.warehouse.ResponseWareHouse;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashBoardActivity extends BaseActivity {


    FragmentDashBoardBinding binding;
    RestApiService retrofitInstance;
    List<ResponseWareHouse> responseWareHouses;
    int parentperiodfilter = 0, categoryfilter = 0, employeePeriodFilter = 0, warehouseid = 0, itemWiseSoldFilter = 0, soldcategoryfilter;
    List<FilterPeriod> datasetperiod, datasetperiod2, datasetperiod3;
    List<DashboardItemWiseSold> dashboardItemWiseSolds;
    List<DashboardCategoryWiseSold> dashboardCategoryWiseSolds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout for this fragment
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_dash_board);
        initToolBar(binding.toolbarApp.toolbar, "Dashboard", true);
        showProgressDialog();
        retrofitInstance = RetrofitInstance.getApiService(getApplicationContext());
        responseWareHouses = new ArrayList<>();
        doFetchDashBoardSummary();
        FetchWareHouses();
        FetchDashboardpiechart();
        FethchTopProductsSold();
        FethchTopSoldCategory();
        binding.recyvcustomerserved.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        binding.recyvitemwisefilter.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyvitemcatwisefilter.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


        datasetperiod = new ArrayList<>();
        dashboardItemWiseSolds = new ArrayList<>();
        dashboardCategoryWiseSolds = new ArrayList<>();
        datasetperiod2 = new ArrayList<>();
        datasetperiod3 = new ArrayList<>();
        datasetperiod.add(new FilterPeriod(0, "Today"));
        datasetperiod.add(new FilterPeriod(1, "This week"));
        datasetperiod.add(new FilterPeriod(2, "Last week"));
        datasetperiod.add(new FilterPeriod(3, "Last 30 days"));
        datasetperiod.add(new FilterPeriod(4, "This Year"));


        datasetperiod2.add(new FilterPeriod(0, "Last 6 Year"));
        datasetperiod2.add(new FilterPeriod(1, "Last Year vs Currenr Year"));
        datasetperiod2.add(new FilterPeriod(2, "Current Year"));
        datasetperiod2.add(new FilterPeriod(3, "Last 30 days"));

        datasetperiod3.add(new FilterPeriod(0, "This week"));
        datasetperiod3.add(new FilterPeriod(1, "Last week"));
        datasetperiod3.add(new FilterPeriod(2, "Last 30 days"));
        datasetperiod3.add(new FilterPeriod(3, "This Year"));
        binding.edStoreselection.setTitle("Select Store");


        ArrayAdapter<FilterPeriod> arrayAdapter = new ArrayAdapter<FilterPeriod>(getApplicationContext(), android.R.layout.simple_spinner_item, datasetperiod);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<FilterPeriod> arrayAdapter3 = new ArrayAdapter<FilterPeriod>(getApplicationContext(), android.R.layout.simple_spinner_item, datasetperiod3);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<FilterPeriod> arrayAdaptersales = new ArrayAdapter<FilterPeriod>(getApplicationContext(), android.R.layout.simple_spinner_item, datasetperiod2);
        arrayAdaptersales.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.edPeriod.setAdapter(arrayAdapter);
        binding.edCategroyfilter.setAdapter(arrayAdaptersales);
        binding.edServrdfilter.setAdapter(arrayAdapter);
        binding.edItemfilter.setAdapter(arrayAdapter3);
        binding.edCatfilter.setAdapter(arrayAdapter3);

        binding.edStoreselection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                warehouseid = responseWareHouses.get(position).getId();
                doFetchDashBoardSummary();
                fetchGraphData();
                binding.edPeriod.setAdapter(arrayAdapter);
                binding.edCategroyfilter.setAdapter(arrayAdaptersales);
                binding.edServrdfilter.setAdapter(arrayAdapter);
                binding.edItemfilter.setAdapter(arrayAdapter3);
                binding.edCatfilter.setAdapter(arrayAdapter3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.edPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parentperiodfilter = datasetperiod.get(position).getId();
                doFetchDashBoardSummary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.edItemfilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemWiseSoldFilter = datasetperiod.get(position).getId();
                FethchTopProductsSold();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.edCatfilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                soldcategoryfilter = datasetperiod3.get(position).getId();
                FethchTopSoldCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.edServrdfilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                employeePeriodFilter = datasetperiod.get(position).getId();
                FetchDashboardpiechart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void FethchTopProductsSold() {


        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", 0);
        jsonObject.addProperty("categoryWiseSoldFilter", 0);
        jsonObject.addProperty("itemWiseSoldFilter", itemWiseSoldFilter);
        jsonObject.addProperty("employeePeriodFilter", 0);
        jsonObject.addProperty("graphFilter", 0);
        jsonObject.addProperty("warehouseSelected", true);
        RestApiService restApiService = RetrofitInstance.getApiService(getApplicationContext());
        Call<ResponseTop10ProductsSold> call = restApiService.getProductsSold(LocalPreferences.getToken(getApplicationContext()), jsonObject);
        call.enqueue(new Callback<ResponseTop10ProductsSold>() {
            @Override
            public void onResponse(Call<ResponseTop10ProductsSold> call, Response<ResponseTop10ProductsSold> response) {
                hideProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ResponseTop10ProductsSold responseTop10ProductsSold = response.body();
                    dashboardItemWiseSolds = responseTop10ProductsSold.getDashboardItemWiseSold();
                    Log.d("onResponsesize", "onResponse: " + dashboardItemWiseSolds.size());
                    if (dashboardItemWiseSolds.size() != 0) {
                        binding.recyvitemwisefilter.setAdapter(new ToptenSoldAdapter(getApplicationContext(), dashboardItemWiseSolds));
                        binding.noItemLayout2.layoutnoitem.setVisibility(View.GONE);

                    } else {
                        binding.noItemLayout2.layoutnoitem.setVisibility(View.VISIBLE);

                    }

                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseTop10ProductsSold> call, Throwable t) {

            }
        });
    }

    private void FethchTopSoldCategory() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", 0);
        jsonObject.addProperty("categoryWiseSoldFilter", soldcategoryfilter);
        jsonObject.addProperty("itemWiseSoldFilter", 0);
        jsonObject.addProperty("employeePeriodFilter", 0);
        jsonObject.addProperty("graphFilter", 0);
        jsonObject.addProperty("warehouseSelected", true);
        RestApiService restApiService = RetrofitInstance.getApiService(getApplicationContext());
        Call<ResponseTopCategory> call = restApiService.getTopSoldCategory(LocalPreferences.getToken(getApplicationContext()), jsonObject);
        call.enqueue(new Callback<ResponseTopCategory>() {
            @Override
            public void onResponse(Call<ResponseTopCategory> call, Response<ResponseTopCategory> response) {
                hideProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ResponseTopCategory responseTopCategory = response.body();
                    dashboardCategoryWiseSolds = responseTopCategory.getDashboardCategoryWiseSold();
                    if (dashboardCategoryWiseSolds.size() != 0) {
                        binding.noItemLayout3.layoutnoitem.setVisibility(View.GONE);
                        binding.recyvitemcatwisefilter.setAdapter(new ToptenSoldCategoryAdapter(getApplicationContext(), dashboardCategoryWiseSolds));

                    } else {
                        binding.noItemLayout3.layoutnoitem.setVisibility(View.VISIBLE);

                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseTopCategory> call, Throwable t) {

            }
        });
    }

    private void FetchDashboardpiechart() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", 0);
        jsonObject.addProperty("categoryWiseSoldFilter", 0);
        jsonObject.addProperty("itemWiseSoldFilter", 0);
        jsonObject.addProperty("employeePeriodFilter", employeePeriodFilter);
        jsonObject.addProperty("graphFilter", 0);
        jsonObject.addProperty("warehouseSelected", true);
        RestApiService restApiService = RetrofitInstance.getApiService(getApplicationContext());
        Call<ResponseDashboardpiechart> call = restApiService.getDashboardPiechart(LocalPreferences.getToken(getApplicationContext()), jsonObject);
        call.enqueue(new Callback<ResponseDashboardpiechart>() {
            @Override
            public void onResponse(Call<ResponseDashboardpiechart> call, Response<ResponseDashboardpiechart> response) {
                hideProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ResponseDashboardpiechart responseDashboardpiechart = response.body();
                    List<DashboardEmployeeAnalytic> dataset = responseDashboardpiechart.getDashboardEmployeeAnalytics();
                    if (dataset.size() != 0) {
                        binding.noItemLayout1.layoutnoitem.setVisibility(View.GONE);
                        binding.recyvcustomerserved.setAdapter(new DashBoardPieChartadapter(getApplicationContext(), dataset));

                    } else {
                        binding.noItemLayout1.layoutnoitem.setVisibility(View.VISIBLE);

                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseDashboardpiechart> call, Throwable t) {

            }
        });
    }

    public void GroupBarChart(List<DashboardDataSingleBar> dataset) {
        binding.chart1.setDrawGridBackground(false);
        binding.chart1.setTouchEnabled(false);
        binding.chart1.setDragEnabled(false);
        binding.chart1.setMaxVisibleValueCount(5);
        binding.chart1.setScaleEnabled(false);
        binding.chart1.setPinchZoom(false);
        binding.chart1.setDrawBarShadow(false);
        binding.chart1.getDescription().setEnabled(false);
        binding.chart1.setPinchZoom(false);
        binding.chart1.setDrawGridBackground(false);
        BarChartCustomRenderer barChartCustomRenderer = new BarChartCustomRenderer( binding.chart1,  binding.chart1.getAnimator(),    binding.chart1.getViewPortHandler());
        binding.chart1.setRenderer(barChartCustomRenderer);

        // empty labels so that the names are spread evenly
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < dataset.size(); i++) {
            labels.add(dataset.get(i).getName());

        }
        XAxis xAxis = binding.chart1.getXAxis();
        xAxis.setCenterAxisLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(0.5f);
        xAxis.setGranularityEnabled(true);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.BLACK);
        //  xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis leftAxis = binding.chart1.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.BLACK);
        leftAxis.setDrawGridLines(false);
        leftAxis.setEnabled(false);//for enabling twosides




        ArrayList<BarEntry> barOne = new ArrayList<>();

        for (int i = 0; i < dataset.size(); i++) {
            barOne.add(new BarEntry(i, dataset.get(i).getValue().floatValue()));

        }

        BarDataSet barDataSet = new BarDataSet(barOne, "");
        barDataSet.setColor(Color.parseColor("#BF8F3A"));
        barDataSet.setDrawValues(true);

        BarData data = new BarData(barDataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.BLACK);
        data.setBarWidth(0.5f);


        binding.chart1.setData(data);
        binding.chart1.invalidate();

    }

    private void fetchGraphData() {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", 0);
        jsonObject.addProperty("categoryWiseSoldFilter", categoryfilter);
        jsonObject.addProperty("itemWiseSoldFilter", 0);
        jsonObject.addProperty("employeePeriodFilter", 0);
        jsonObject.addProperty("graphFilter", 0);
        jsonObject.addProperty("warehouseSelected", 0);
        jsonObject.addProperty("companyID", 0);
        RestApiService restApiService = RetrofitInstance.getApiService(getApplicationContext());
        Call<ResponseDashBoardGraph> call = restApiService.getDashboardGraph(LocalPreferences.getToken(getApplicationContext()), jsonObject);
        call.enqueue(new Callback<ResponseDashBoardGraph>() {
            @Override
            public void onResponse(Call<ResponseDashBoardGraph> call, Response<ResponseDashBoardGraph> response) {
                if (response.body() != null && response.code() == 200) {
                    List<DashboardDataSingleBar> dashboardDataSingleBar = response.body().getDashboardDataSingleBar();
                    GroupBarChart(dashboardDataSingleBar);
                }
            }

            @Override
            public void onFailure(Call<ResponseDashBoardGraph> call, Throwable t) {

            }
        });


    }

    private void FetchWareHouses() {

        RestApiService restApiService = RetrofitInstance.getApiService(getApplicationContext());
        Call<List<ResponseWareHouse>> call = restApiService.Fetchwarehouse(LocalPreferences.getToken(getApplicationContext()));
        call.enqueue(new Callback<List<ResponseWareHouse>>() {
            @Override
            public void onResponse(Call<List<ResponseWareHouse>> call, Response<List<ResponseWareHouse>> response) {
                hideProgressDialog();
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    responseWareHouses = response.body();
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
                } else {


                }

            }

            @Override
            public void onFailure(Call<List<ResponseWareHouse>> call, Throwable t) {

            }
        });


    }

    private void doFetchDashBoardSummary() {
        ;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", parentperiodfilter);
        jsonObject.addProperty("categoryWiseSoldFilter", 0);
        jsonObject.addProperty("itemWiseSoldFilter", 0);
        jsonObject.addProperty("employeePeriodFilter", employeePeriodFilter);
        jsonObject.addProperty("graphFilter", 0);
        jsonObject.addProperty("warehouseSelected", true);
        Call<ResponseDashboardSummary> call = retrofitInstance.getDashboardSummary(LocalPreferences.getToken(getApplicationContext()), jsonObject);
        call.enqueue(new Callback<ResponseDashboardSummary>() {
            @Override
            public void onResponse(Call<ResponseDashboardSummary> call, Response<ResponseDashboardSummary> response) {
                hideProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    binding.tvinverntory.setText(String.valueOf(new DecimalFormat("#").format(response.body().getTotalInventory())));
                    binding.tvtotalsales.setText(String.valueOf(response.body().getTodaySales()));
                    binding.tvproductssold.setText(String.valueOf(new DecimalFormat("#").format(response.body().getTotalProductSold())));
                    binding.tvnoofsales.setText(String.valueOf(new DecimalFormat("#").format(response.body().getTodayTotalSales())));

                }
            }


            @Override
            public void onFailure(Call<ResponseDashboardSummary> call, Throwable t) {
                Log.d("TAG", "onFailure: " + t.getMessage());

            }
        });


    }

}




