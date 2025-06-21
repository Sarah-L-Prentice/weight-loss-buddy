package com.prenticeweb.weightlossbuddy.room.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;
import com.prenticeweb.weightlossbuddy.room.dao.WeightDao;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;

import java.util.List;

public class WeightRepository {
    private final Context context;
    private final WeightDao dao;

    public WeightRepository(WeightDao weightDao, Context context) {
        this.context = context.getApplicationContext();
        this.dao = weightDao;
    }

    public void insertWeight(WeightMeasurement weightMeasurement) {
        dao.insertWeight(weightMeasurement);
    }
    public ListenableFuture<Integer> updateWeight(WeightMeasurement weightMeasurement) {
        return dao.updateWeight(weightMeasurement);
    }
    public ListenableFuture<Integer> deleteWeight(WeightMeasurement weightMeasurement) {
        return dao.deleteWeight(weightMeasurement);
    }
    public LiveData<List<WeightMeasurement>> getWeightsOrderedByDate() {
        return dao.getWeightsOrderedByDate();
    }




}
