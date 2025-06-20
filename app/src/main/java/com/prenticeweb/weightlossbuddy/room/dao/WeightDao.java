package com.prenticeweb.weightlossbuddy.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;

import java.util.List;

@Dao
public interface WeightDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeight(WeightMeasurement weightMeasurement);

    @Update
    ListenableFuture<Integer> updateWeight(WeightMeasurement weightMeasurement);

    @Delete
    ListenableFuture<Integer> deleteWeight(WeightMeasurement weightMeasurement);

    @Query("SELECT * FROM WeightMeasurement ORDER BY DATE ASC")
    LiveData<List<WeightMeasurement>> getWeightsOrderedByDate();
}
