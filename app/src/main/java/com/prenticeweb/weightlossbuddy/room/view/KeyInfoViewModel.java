package com.prenticeweb.weightlossbuddy.room.view;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.prenticeweb.weightlossbuddy.room.WeightDatabase;
import com.prenticeweb.weightlossbuddy.room.entity.KeyInfo;
import com.prenticeweb.weightlossbuddy.room.repository.KeyInfoRepository;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KeyInfoViewModel extends AndroidViewModel {

    private LiveData<KeyInfo> readAll;

    private KeyInfoRepository repo;

    public KeyInfoViewModel(@NonNull Application application) {
        super(application);
        WeightDatabase weightDatabase = WeightDatabase.getInstance(application.getApplicationContext());
        repo = new KeyInfoRepository(weightDatabase.getKeyInfoDao());
        readAll = repo.getKeyInfo();
    }

    public LiveData<KeyInfo> getReadAll() {
        return readAll;
    }

    public void insert(BigDecimal heightCm, BigDecimal heightInches, BigDecimal targetWeightLb, BigDecimal targetWeightKilo, KeyInfo.PreferredWeightUnit preferredWeightUnit) {
        KeyInfo keyInfo = new KeyInfo();
        keyInfo.setHeightInCm(heightCm);
        keyInfo.setHeightInInches(heightInches);
        keyInfo.setTargetWeightKg(targetWeightKilo);
        keyInfo.setTargetWeightLb(targetWeightLb);
        keyInfo.setPreferredWeightUnit(preferredWeightUnit);
        performTask(() -> repo.insertKeyInfo(keyInfo));
    }

    void performTask(Runnable task) {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(task);
    }
}
