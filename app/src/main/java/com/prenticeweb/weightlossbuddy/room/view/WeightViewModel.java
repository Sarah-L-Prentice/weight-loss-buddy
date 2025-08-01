package com.prenticeweb.weightlossbuddy.room.view;

import static com.prenticeweb.weightlossbuddy.common.Constants.SIMPLE_DATE_FORMAT;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.prenticeweb.weightlossbuddy.common.Constants;
import com.prenticeweb.weightlossbuddy.room.WeightDatabase;
import com.prenticeweb.weightlossbuddy.room.entity.WeightMeasurement;
import com.prenticeweb.weightlossbuddy.room.repository.WeightRepository;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WeightViewModel extends AndroidViewModel {

    private LiveData<List<WeightMeasurement>> readAll;

    private WeightRepository repo;

    public WeightViewModel(@NonNull Application application) {
        super(application);
        WeightDatabase weightDatabase = WeightDatabase.getInstance(application.getApplicationContext());
        repo = new WeightRepository(weightDatabase.getWeightDao(), application);
        readAll = repo.getWeightsOrderedByDate();
    }

    public LiveData<List<WeightMeasurement>> getReadAll() {
        return readAll;
    }

    public void insert(String date, String kg, String lb) {
        try {
            WeightMeasurement wm = new WeightMeasurement();
                    wm.setDate(SIMPLE_DATE_FORMAT.parse(date));
                    wm.setWeightKg(new BigDecimal(kg));
                    wm.setWeightLb(new BigDecimal(lb));
            performTask(() -> repo.insertWeight(wm));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    void performTask(Runnable task) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(task);
    }


    public void delete(String dateString) {
        try {
            Date date = SIMPLE_DATE_FORMAT.parse(dateString);
            var weightMeasurement = readAll.getValue().stream().filter(wm -> wm.getDate().equals(date)).findFirst();
            weightMeasurement.ifPresent(measurement -> repo.deleteWeight(measurement));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
