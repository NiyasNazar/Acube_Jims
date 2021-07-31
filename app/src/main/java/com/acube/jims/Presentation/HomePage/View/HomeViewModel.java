package com.acube.jims.Presentation.HomePage.View;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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

    public void getHomeMenu(String Token, String vaAppname, String vaRole) {
        repository.getHomeMneu(Token, vaAppname, vaRole);

    }

    public void init() {
        repository = new HomeMenuRepository();
        liveData = repository.getResponseLiveData();
    }


    public LiveData<List<HomeData>> getLiveData() {
        return liveData;
    }
}