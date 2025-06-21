package com.prenticeweb.weightlossbuddy.room.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prenticeweb.weightlossbuddy.room.WeightDatabase;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;
import com.prenticeweb.weightlossbuddy.room.repository.WeightRepository;

import java.util.ArrayList;
import java.util.List;

public class WeightViewModel extends AndroidViewModel {

    private LiveData<List<WeightMeasurement>> readAll;

    WeightRepository repo;
    public WeightViewModel(@NonNull Application application) {
        super(application);
        WeightDatabase weightDatabase = WeightDatabase.getInstance(application.getApplicationContext());
        repo = new WeightRepository(weightDatabase.getWeightDao(), application);
        readAll = repo.getWeightsOrderedByDate();
    }
    public LiveData<List<WeightMeasurement>> getReadAll() {
        return readAll;
    }
}
