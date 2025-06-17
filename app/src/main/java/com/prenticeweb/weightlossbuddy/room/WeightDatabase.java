package com.prenticeweb.weightlossbuddy.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.prenticeweb.weightlossbuddy.room.dao.WeightDao;
import com.prenticeweb.weightlossbuddy.room.entity.Converters;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;

@Database(entities = {WeightMeasurement.class}, version = 1)
@TypeConverters({Converters.class})
abstract class WeightDatabase extends RoomDatabase {

    abstract WeightDao getWeightDao();
}
