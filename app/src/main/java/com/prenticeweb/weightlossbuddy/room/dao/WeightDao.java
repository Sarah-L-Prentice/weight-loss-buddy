package com.prenticeweb.weightlossbuddy.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Query;
import androidx.room.Upsert;

import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;

import java.util.List;

@Dao
public interface WeightDao {

    @Upsert
    Long upsertWeight(WeightMeasurement weightMeasurement);

    @Delete
    int deleteWeight(WeightMeasurement weightMeasurement);

    @Query("SELECT * FROM WeightMeasurement ORDER BY DATE ASC")
    LiveData<List<WeightMeasurement>> getWeightsOrderedByDate();
}
