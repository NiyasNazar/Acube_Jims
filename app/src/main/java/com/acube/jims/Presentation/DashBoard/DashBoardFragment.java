package com.acube.jims.Presentation.DashBoard;

import android.graphics.Color;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.Analytics.adapter.PieChartadapter;
import com.acube.jims.Presentation.DashBoard.adapter.DashBoardPieChartadapter;
import com.acube.jims.Presentation.DashBoard.adapter.ToptenSoldAdapter;
import com.acube.jims.Presentation.DashBoard.adapter.ToptenSoldCategoryAdapter;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.FragmentDashBoardBinding;
import com.acube.jims.datalayer.api.RestApiService;
import com.acube.jims.datalayer.api.RetrofitInstance;
import com.acube.jims.datalayer.models.Analytics.AnalyticsDataSingleBar;
import com.acube.jims.datalayer.models.Analytics.FilterPeriod;
import com.acube.jims.datalayer.models.Dashboard.DashboardCategoryWiseSold;
import com.acube.jims.datalayer.models.Dashboard.DashboardDataSingleBar;
import com.acube.jims.datalayer.models.Dashboard.DashboardEmployeeAnalytic;
import com.acube.jims.datalayer.models.Dashboard.DashboardEmployeePieChart;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    FragmentDashBoardBinding binding;
    RestApiService retrofitInstance;
    List<ResponseWareHouse> responseWareHouses;
    int parentperiodfilter = 0, categoryfilter = 0, employeePeriodFilter = 0, warehouseid = 0, itemWiseSoldFilter = 0;
    List<FilterPeriod> datasetperiod, datasetperiod2;
    List<DashboardItemWiseSold> dashboardItemWiseSolds;
    List<DashboardCategoryWiseSold> dashboardCategoryWiseSolds;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_dash_board, container, false);
        binding.toolbar.tvFragname.setText("Dashboard");
        retrofitInstance = RetrofitInstance.getApiService();
        responseWareHouses = new ArrayList<>();
        doFetchDashBoardSummary();
        FetchWareHouses();
        FetchDashboardpiechart();
        FethchTopProductsSold();
        FethchTopSoldCategory();
        binding.recyvcustomerserved.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.recyvitemwisefilter.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyvitemcatwisefilter.setLayoutManager(new LinearLayoutManager(getActivity()));



        datasetperiod = new ArrayList<>();
        dashboardItemWiseSolds = new ArrayList<>();
        dashboardCategoryWiseSolds=new ArrayList<>();
        datasetperiod2 = new ArrayList<>();
        datasetperiod.add(new FilterPeriod(0, "Today"));
        datasetperiod.add(new FilterPeriod(1, "This week"));
        datasetperiod.add(new FilterPeriod(2, "Last week"));
        datasetperiod.add(new FilterPeriod(3, "Last 30 days"));
        datasetperiod.add(new FilterPeriod(4, "This Year"));


        datasetperiod2.add(new FilterPeriod(0, "Last 6 Year"));
        datasetperiod2.add(new FilterPeriod(1, "Last Year vs Currenr Year"));
        datasetperiod2.add(new FilterPeriod(2, "Current Year"));
        datasetperiod2.add(new FilterPeriod(3, "Last 30 days"));



        ArrayAdapter<FilterPeriod> arrayAdapter = new ArrayAdapter<FilterPeriod>(getActivity(), android.R.layout.simple_spinner_item, datasetperiod);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<FilterPeriod> arrayAdaptersales = new ArrayAdapter<FilterPeriod>(getActivity(), android.R.layout.simple_spinner_item, datasetperiod2);
        arrayAdaptersales.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.edPeriod.setAdapter(arrayAdapter);
        binding.edCategroyfilter.setAdapter(arrayAdaptersales);
        binding.edServrdfilter.setAdapter(arrayAdapter);
        binding.edItemfilter.setAdapter(arrayAdapter);
        binding.edCatfilter.setAdapter(arrayAdapter);

        binding.edStoreselection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                warehouseid = responseWareHouses.get(position).getId();
                doFetchDashBoardSummary();
                fetchGraphData();
                binding.edPeriod.setAdapter(arrayAdapter);
                binding.edCategroyfilter.setAdapter(arrayAdaptersales);
                binding.edServrdfilter.setAdapter(arrayAdapter);
                binding.edItemfilter.setAdapter(arrayAdapter);
                binding.edCatfilter.setAdapter(arrayAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.edPeriod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                parentperiodfilter=datasetperiod.get(position).getId();
                doFetchDashBoardSummary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.edItemfilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemWiseSoldFilter=datasetperiod.get(position).getId();
                FethchTopProductsSold();
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

        return binding.getRoot();
    }

    private void FethchTopProductsSold() {
        showProgressDialog();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", 0);
        jsonObject.addProperty("categoryWiseSoldFilter", 0);
        jsonObject.addProperty("itemWiseSoldFilter", itemWiseSoldFilter);
        jsonObject.addProperty("employeePeriodFilter", 0);
        jsonObject.addProperty("graphFilter", 0);
        jsonObject.addProperty("warehouseSelected", true);
        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<ResponseTop10ProductsSold> call = restApiService.getProductsSold(LocalPreferences.getToken(getActivity()), jsonObject);
        call.enqueue(new Callback<ResponseTop10ProductsSold>() {
            @Override
            public void onResponse(Call<ResponseTop10ProductsSold> call, Response<ResponseTop10ProductsSold> response) {
                hideProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ResponseTop10ProductsSold responseTop10ProductsSold = response.body();
                    dashboardItemWiseSolds = responseTop10ProductsSold.getDashboardItemWiseSold();
                    binding.recyvitemwisefilter.setAdapter(new ToptenSoldAdapter(getActivity(),dashboardItemWiseSolds));

                }
            }

            @Override
            public void onFailure(Call<ResponseTop10ProductsSold> call, Throwable t) {

            }
        });
    }
    private void FethchTopSoldCategory() {
        showProgressDialog();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", 0);
        jsonObject.addProperty("categoryWiseSoldFilter", 0);
        jsonObject.addProperty("itemWiseSoldFilter", itemWiseSoldFilter);
        jsonObject.addProperty("employeePeriodFilter", 0);
        jsonObject.addProperty("graphFilter", 0);
        jsonObject.addProperty("warehouseSelected", true);
        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<ResponseTopCategory> call = restApiService.getTopSoldCategory(LocalPreferences.getToken(getActivity()), jsonObject);
        call.enqueue(new Callback<ResponseTopCategory>() {
            @Override
            public void onResponse(Call<ResponseTopCategory> call, Response<ResponseTopCategory> response) {
                hideProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ResponseTopCategory responseTopCategory = response.body();
                    dashboardCategoryWiseSolds = responseTopCategory.getDashboardCategoryWiseSold();
                    binding.recyvitemcatwisefilter.setAdapter(new ToptenSoldCategoryAdapter(getActivity(),dashboardCategoryWiseSolds));

                }
            }

            @Override
            public void onFailure(Call<ResponseTopCategory> call, Throwable t) {

            }
        });
    }
    private void FetchDashboardpiechart() {
        showProgressDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", 0);
        jsonObject.addProperty("categoryWiseSoldFilter", 0);
        jsonObject.addProperty("itemWiseSoldFilter", 0);
        jsonObject.addProperty("employeePeriodFilter", employeePeriodFilter);
        jsonObject.addProperty("graphFilter", 0);
        jsonObject.addProperty("warehouseSelected", true);
        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<ResponseDashboardpiechart> call = restApiService.getDashboardPiechart(LocalPreferences.getToken(getActivity()), jsonObject);
        call.enqueue(new Callback<ResponseDashboardpiechart>() {
            @Override
            public void onResponse(Call<ResponseDashboardpiechart> call, Response<ResponseDashboardpiechart> response) {
                hideProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    ResponseDashboardpiechart responseDashboardpiechart = response.body();
                    List<DashboardEmployeeAnalytic> dataset = responseDashboardpiechart.getDashboardEmployeeAnalytics();
                    binding.recyvcustomerserved.setAdapter(new DashBoardPieChartadapter(getActivity(), dataset));

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
        binding.chart1.setScaleEnabled(false);
        binding.chart1.setPinchZoom(false);
        binding.chart1.setDrawBarShadow(false);
        binding.chart1.getDescription().setEnabled(false);
        binding.chart1.setPinchZoom(false);
        binding.chart1.setDrawGridBackground(true);
        // empty labels so that the names are spread evenly
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < dataset.size(); i++) {
            labels.add(dataset.get(i).getName());

        }
        XAxis xAxis = binding.chart1.getXAxis();
        xAxis.setCenterAxisLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(12);
        xAxis.setAxisLineColor(Color.BLACK);
        //  xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis leftAxis = binding.chart1.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(2);
        // leftAxis.setLabelCount(8, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        //  binding.chart1.getAxisRight().setEnabled(false);
        //  binding.chart1.getLegend().setEnabled(false);

        float[] valOne = {10, 20, 30, 40, 50};


        ArrayList<BarEntry> barOne = new ArrayList<>();

        for (int i = 0; i < dataset.size(); i++) {
            barOne.add(new BarEntry(i, dataset.get(i).getValue().floatValue()));

        }

        BarDataSet barDataSet = new BarDataSet(barOne, "");
        barDataSet.setColor(Color.MAGENTA);


        BarData data = new BarData(barDataSet);
        data.setBarWidth(0.5f);
        float groupSpace = 0.4f;
        float barSpace = 0f;
        float barWidth = 0.3f;
        // (barSpace + barWidth) * 2 + groupSpace = 1


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
        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<ResponseDashBoardGraph> call = restApiService.getDashboardGraph(LocalPreferences.getToken(getActivity()), jsonObject);
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
        showProgressDialog();

        RestApiService restApiService = RetrofitInstance.getApiService();
        Call<List<ResponseWareHouse>> call = restApiService.Fetchwarehouse(LocalPreferences.getToken(getActivity()));
        call.enqueue(new Callback<List<ResponseWareHouse>>() {
            @Override
            public void onResponse(Call<List<ResponseWareHouse>> call, Response<List<ResponseWareHouse>> response) {
                hideProgressDialog();
                if (response.body() != null && response.code() == 200 || response.code() == 201) {
                    responseWareHouses = response.body();
                    ArrayAdapter<ResponseWareHouse> arrayAdapter = new ArrayAdapter<ResponseWareHouse>(getActivity(), android.R.layout.simple_spinner_item, responseWareHouses);
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
        showProgressDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", parentperiodfilter);
        jsonObject.addProperty("categoryWiseSoldFilter", 0);
        jsonObject.addProperty("itemWiseSoldFilter", 0);
        jsonObject.addProperty("employeePeriodFilter", employeePeriodFilter);
        jsonObject.addProperty("graphFilter", 0);
        jsonObject.addProperty("warehouseSelected", true);
        Call<ResponseDashboardSummary> call = retrofitInstance.getDashboardSummary(LocalPreferences.getToken(getActivity()), jsonObject);
        call.enqueue(new Callback<ResponseDashboardSummary>() {
            @Override
            public void onResponse(Call<ResponseDashboardSummary> call, Response<ResponseDashboardSummary> response) {
                hideProgressDialog();
                if (response.body() != null && response.code() == 200) {
                    binding.tvinverntory.setText(String.valueOf(response.body().getTotalInventory()));
                    binding.tvtotalsales.setText(String.valueOf(response.body().getTodayTotalSales()));
                    binding.tvproductssold.setText(String.valueOf(response.body().getTotalProductSold()));
                    binding.tvnoofsales.setText(String.valueOf(response.body().getTodaySales()));

                }
            }


            @Override
            public void onFailure(Call<ResponseDashboardSummary> call, Throwable t) {

            }
        });


    }
}





