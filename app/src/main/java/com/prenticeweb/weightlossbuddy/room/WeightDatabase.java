package com.prenticeweb.weightlossbuddy.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.prenticeweb.weightlossbuddy.room.dao.WeightDao;
import com.prenticeweb.weightlossbuddy.room.entity.Converters;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;

import lombok.Getter;

@Database(entities = {WeightMeasurement.class}, version = 1)
@TypeConverters({Converters.class})
@Getter
public abstract class WeightDatabase extends RoomDatabase {
    public abstract WeightDao getWeightDao();

    private static volatile WeightDatabase INSTANCE;

    public static WeightDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (WeightDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), WeightDatabase.class, "WEIGHT_DB").build();
                }
            }
        }
        return INSTANCE;
    }
}
