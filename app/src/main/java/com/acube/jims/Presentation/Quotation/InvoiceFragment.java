package com.acube.jims.Presentation.Quotation;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.Presentation.Quotation.adapter.InvoiceAdapter;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.InvoiceFragmentBinding;
import com.acube.jims.datalayer.models.Invoice.ResponseInvoiceList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InvoiceFragment extends Fragment {

    private InvoiceViewModel mViewModel;
    RecyclerView mRecyinvoice;

    public static InvoiceFragment newInstance() {
        return new InvoiceFragment();
    }

    InvoiceFragmentBinding binding;
    double total = 0.0;
    double percentage = 0.0;
    double totalItemtax = 0.0;
    double netamount = 0.0;
    double discount = 0.0;
    List<ResponseInvoiceList>dataset;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(
                inflater, R.layout.invoice_fragment, container, false);


        binding.recyvinvoiceitems.setLayoutManager(new LinearLayoutManager(getActivity()));
        String Customername = LocalPreferences.retrieveStringPreferences(getContext(), "GuestCustomerName");
        String CustomerMobile = LocalPreferences.retrieveStringPreferences(getContext(), "CustomerMobile");
        binding.tvcustomername.setText("Customer Name : " + Customername);
        binding.tvcustomercontactnumber.setText("Contact Number: " + CustomerMobile);
        String date = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());
        binding.tvdate.setText(date);
        binding.btnGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PdfCreatorExampleActivity.class));
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InvoiceViewModel.class);
        mViewModel.init();
        ;
        JsonArray jsonObject = new JsonArray();
        String[] datset = {"10001", "10002"};

        // jsonObject
        mViewModel.FetchInvoice(LocalPreferences.getToken(getActivity()), datset);
        mViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseInvoiceList>>() {
            @Override
            public void onChanged(List<ResponseInvoiceList> responseInvoiceLists) {
                dataset=new ArrayList();
                dataset=responseInvoiceLists;
                setList("invoicelist",dataset);
                if (responseInvoiceLists != null) {
                    for (int i = 0; i < responseInvoiceLists.size(); i++) {

                        total += responseInvoiceLists.get(i).getPriceWithoutTax();
                        totalItemtax += (responseInvoiceLists.get(i).getPriceWithoutTax() / 100.0f) * responseInvoiceLists.get(i).getItemTax();
                    }
                    binding.tvsalesamount.setText("Total " + total);
                    binding.tvtax.setText("Total Tax " + totalItemtax);
                    binding.tvDiscount.setText("Discount : " + discount);
                    netamount = total + totalItemtax;
                    binding.tvtotal.setText("Total " + netamount);
                    binding.recyvinvoiceitems.setAdapter(new InvoiceAdapter(getActivity(), responseInvoiceLists));


                }
            }
        });
    }
    public <T> void setList(String key, List<T> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        LocalPreferences.storeStringPreference(getActivity(), key, json);
    }
}