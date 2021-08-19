package com.acube.jims.Presentation.Favorites.View;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acube.jims.BaseFragment;
import com.acube.jims.Presentation.Favorites.ViewModel.AddtoFavoritesViewModel;
import com.acube.jims.Presentation.Favorites.ViewModel.FavoritesViewModel;
import com.acube.jims.Presentation.Favorites.adapter.FavoritesItemAdapter;
import com.acube.jims.Presentation.Login.ViewModel.LoginViewModel;
import com.acube.jims.Presentation.ProductDetails.View.ProductDetailsFragment;
import com.acube.jims.R;
import com.acube.jims.Utils.LocalPreferences;
import com.acube.jims.databinding.FragmentFavoritesBinding;
import com.acube.jims.datalayer.constants.AppConstants;
import com.acube.jims.datalayer.models.Favorites.ResponseFavorites;
import com.google.gson.JsonObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends BaseFragment implements FavoritesItemAdapter.DeleteProduct {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    BackHandler backHandler;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavoritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavoritesFragment newInstance(String param1, String param2) {
        FavoritesFragment fragment = new FavoritesFragment();
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

    FragmentFavoritesBinding binding;
    FavoritesViewModel favoritesViewModel;
    AddtoFavoritesViewModel addtoFavoritesViewModel;
    String AuthToken;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            backHandler = (BackHandler) context;
        } catch (ClassCastException castException) {
            /** The activity does not implement the listener. */
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_favorites, container, false);

        binding.recycartitems.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.toolbar.tvFragname.setText("Favorites");
        binding.btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backHandler.backpress();
            }
        });

        favoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        addtoFavoritesViewModel = new ViewModelProvider(this).get(AddtoFavoritesViewModel.class);
        favoritesViewModel.init();
        addtoFavoritesViewModel.init();
        showProgressDialog();
        String customerId = LocalPreferences.retrieveStringPreferences(getActivity(), "GuestCustomerID");
        AuthToken = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.Token);
        favoritesViewModel.ViewCart(AppConstants.Authorization + AuthToken, customerId);
        favoritesViewModel.getLiveData().observe(getActivity(), new Observer<List<ResponseFavorites>>() {
            @Override
            public void onChanged(List<ResponseFavorites> responseFavorites) {
                hideProgressDialog();
                binding.recycartitems.setAdapter(new FavoritesItemAdapter(getActivity(), responseFavorites, FavoritesFragment.this));

            }
        });
        addtoFavoritesViewModel.getLiveData().observe(getActivity(), new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject jsonObject) {
                hideProgressDialog();

                favoritesViewModel.ViewCart(AppConstants.Authorization + AuthToken, customerId);
            }
        });
        return binding.getRoot();


    }

    @Override
    public void removefromcart(String itemid) {
        showProgressDialog();

        String UserId = LocalPreferences.retrieveStringPreferences(getActivity(), AppConstants.UserID);
        String customerId = LocalPreferences.retrieveStringPreferences(getActivity(), "GuestCustomerID");
        addtoFavoritesViewModel.AddtoFavorites(AppConstants.Authorization + AuthToken, customerId, UserId, String.valueOf(itemid), "delete", "");

    }

    public interface BackHandler {
        void backpress();
    }

}