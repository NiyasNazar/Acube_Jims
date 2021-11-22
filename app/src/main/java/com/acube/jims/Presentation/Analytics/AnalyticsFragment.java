package com.acube.jims.Presentation.Analytics;

import static com.acube.jims.datalayer.constants.AppConstants.Token;

import android.graphics.Color;
import android.os.Bundle;
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
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.Analytics.ViewModel.AnalyticsViewModel;
import com.acube.jims.Presentation.Analytics.ViewModel.WarehouseViewModel;
import com.acube.jims.Presentation.Analytics.adapter.ItemwiseAnalyticsadapter;
import com.acube.jims.Presentation.Analytics.adapter.PieChartadapter;
import com.acube.jims.Presentation.Audit.ViewModel.AuditViewModel;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.FragmentAnalyticsBinding;
import com.acube.jims.databinding.FragmentDashBoardBinding;
import com.acube.jims.datalayer.models.Analytics.AnalyticsCustomersServed;
import com.acube.jims.datalayer.models.Analytics.AnalyticsDataSingleBar;
import com.acube.jims.datalayer.models.Analytics.AnalyticsItemWiseViewed;
import com.acube.jims.datalayer.models.Analytics.FilterPeriod;
import com.acube.jims.datalayer.models.Analytics.ResponseAnalyticsGraph;
import com.acube.jims.datalayer.models.Analytics.ResponseAnalyticsSummary;
import com.acube.jims.datalayer.models.Analytics.ResponseCustomerServed;
import com.acube.jims.datalayer.models.Analytics.ResponseItemWiseAnalytics;
import com.acube.jims.datalayer.models.DeviceRegistration.ResponseTrayMaster;
import com.acube.jims.datalayer.models.warehouse.ResponseWareHouse;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalyticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalyticsFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AnalyticsFragment() {
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
    public static AnalyticsFragment newInstance(String param1, String param2) {
        AnalyticsFragment fragment = new AnalyticsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_analytics, container, false);
        binding.toolbar.tvFragname.setText("Analytics");
        analyticsViewModel = new ViewModelProvider(this).get(AnalyticsViewModel.class);
        analyticsViewModel.init();
        warehouseViewModel = new ViewModelProvider(this).get(WarehouseViewModel.class);
        warehouseViewModel.init();
        binding.chart1.setClickable(false);
        binding.chart1.setPinchZoom(false);
        x = new ArrayList<BarEntry>();
        y = new ArrayList<String>();
        responseWareHouses = new ArrayList<>();
        fetchAnalyticsSummary();
        fetchGraphData();
        datasetperiod = new ArrayList<>();
        datasetperiod.add(new FilterPeriod(0, "Today"));
        datasetperiod.add(new FilterPeriod(1, "This week"));
        datasetperiod.add(new FilterPeriod(2, "Last week"));
        datasetperiod.add(new FilterPeriod(3, "Last 30 days"));
        datasetperiod.add(new FilterPeriod(4, "This Year"));
        ArrayAdapter<FilterPeriod> arrayAdapter = new ArrayAdapter<FilterPeriod>(getActivity(), android.R.layout.simple_spinner_item, datasetperiod);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.edPeriod.setAdapter(arrayAdapter);
        binding.edCategroyfilter.setAdapter(arrayAdapter);
        binding.edServrdfilter.setAdapter(arrayAdapter);
        binding.edItemfilter.setAdapter(arrayAdapter);
        fetchItemWiseSummary();
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
        binding.recyvitemwisefilter.setLayoutManager(new LinearLayoutManager(getActivity()));
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

        binding.recyvcustomerserved.setLayoutManager(new GridLayoutManager(getActivity(), 3));


        binding.toolbar.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        analyticsViewModel.getLiveData().observe(getActivity(), new Observer<ResponseAnalyticsSummary>() {
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
        analyticsViewModel.getItemwiseLiveData().observe(getActivity(), new Observer<ResponseItemWiseAnalytics>() {
            @Override
            public void onChanged(ResponseItemWiseAnalytics responseItemWiseAnalytics) {
                hideProgressDialog();
                if (responseItemWiseAnalytics != null) {
                    List<AnalyticsItemWiseViewed> analyticsItemWiseViewed = responseItemWiseAnalytics.getAnalyticsItemWiseViewed();
                    binding.recyvitemwisefilter.setAdapter(new ItemwiseAnalyticsadapter(getActivity(), analyticsItemWiseViewed));
                }
            }
        });


        warehouseViewModel.FetchWareHouses(LocalPreferences.getToken(getActivity()));
        warehouseViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseWareHouse>>() {
            @Override
            public void onChanged(List<ResponseWareHouse> dataset) {
                hideProgressDialog();
                if (dataset != null) {
                    responseWareHouses = dataset;
                    ArrayAdapter<ResponseWareHouse> arrayAdapter = new ArrayAdapter<ResponseWareHouse>(getActivity(), android.R.layout.simple_spinner_item, responseWareHouses);
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


        analyticsViewModel.getGraphLiveData().observe(getActivity(), new Observer<ResponseAnalyticsGraph>() {
            @Override
            public void onChanged(ResponseAnalyticsGraph responseAnalyticsGraph) {
                hideProgressDialog();
                if (responseAnalyticsGraph != null) {
                    List<AnalyticsDataSingleBar> dataset = responseAnalyticsGraph.getAnalyticsDataSingleBar();
                    GroupBarChart(dataset);

                }
            }
        });

        analyticsViewModel.customerServedLiveData().observe(getActivity(), new Observer<ResponseCustomerServed>() {
            @Override
            public void onChanged(ResponseCustomerServed responseCustomerServed) {
                if (responseCustomerServed != null) {
                    hideProgressDialog();
                    List<AnalyticsCustomersServed> dataset = responseCustomerServed.getAnalyticsCustomersServed();
                    binding.recyvcustomerserved.setAdapter(new PieChartadapter(getActivity(), dataset));


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
        return binding.getRoot();
    }

    private void fetchGraphData() {
        showProgressDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", 0);
        jsonObject.addProperty("categoryPeriodFilter", categoryfilter);
        jsonObject.addProperty("itemPeriodFilter", 0);
        jsonObject.addProperty("employeePeriodFilter", 0);
        analyticsViewModel.AnalyticGraph(LocalPreferences.getToken(getActivity()), jsonObject);
    }

    private void fetchAnalyticsSummary() {
        showProgressDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", parentfilter);
        jsonObject.addProperty("categoryPeriodFilter", 0);
        jsonObject.addProperty("itemPeriodFilter", 0);
        jsonObject.addProperty("employeePeriodFilter", 0);
        //jsonObject.addProperty("companyID");

        analyticsViewModel.AnalyticSummary(LocalPreferences.getToken(getActivity()), jsonObject);
    }

    private void fetchItemWiseSummary() {
        showProgressDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", parentfilter);
        jsonObject.addProperty("categoryPeriodFilter", 0);
        jsonObject.addProperty("itemPeriodFilter", itemfilter);
        jsonObject.addProperty("employeePeriodFilter", 0);
        //jsonObject.addProperty("companyID");

        analyticsViewModel.ItemWiseAnalytics(LocalPreferences.getToken(getActivity()), jsonObject);
    }

    private void fetchCustomerServedData() {
        showProgressDialog();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("warehouseID", warehouseid);
        jsonObject.addProperty("summaryPeriodFilter", 0);
        jsonObject.addProperty("categoryPeriodFilter", 0);
        jsonObject.addProperty("itemPeriodFilter", 0);
        jsonObject.addProperty("employeePeriodFilter", servedfilter);
        analyticsViewModel.CustomerServed(LocalPreferences.getToken(getActivity()), jsonObject);
    }

    public void GroupBarChart(List<AnalyticsDataSingleBar> dataset) {
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
        xAxis.setGranularity(1); // only intervals of 1 day
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTextSize(8);
        xAxis.setAxisLineColor(Color.BLACK);
        //  xAxis.setAxisMinimum(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        YAxis leftAxis = binding.chart1.getAxisLeft();
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(8);
        leftAxis.setAxisLineColor(Color.WHITE);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(5);
        // leftAxis.setLabelCount(8, true);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);

        //  binding.chart1.getAxisRight().setEnabled(false);
        //  binding.chart1.getLegend().setEnabled(false);

        float[] valOne = {10, 20, 30, 40, 50};


        ArrayList<BarEntry> barOne = new ArrayList<>();

        for (int i = 0; i < dataset.size(); i++) {
            barOne.add(new BarEntry(i, dataset.get(i).getValue()));

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
}







