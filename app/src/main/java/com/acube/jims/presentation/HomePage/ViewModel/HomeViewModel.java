package com.acube.jims.presentation.HomePage.ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.acube.jims.datalayer.models.HomePage.HomeData;
import com.acube.jims.datalayer.repositories.HomePage.HomeMenuRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {
    public HomeMenuRepository repository;
    private LiveData<List<HomeData>> liveData;

    public HomeViewModel(@NonNull @NotNull Application application) {
        super(application);
    }

    public void getHomeMenu(String Token, String vaAppname, String vaRole, Context context) {
        repository.getHomeMneu(Token, vaAppname, vaRole,context);

    }

    public void init() {
        repository = new HomeMenuRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<HomeData>> getLiveData() {
        return liveData;
    }
}