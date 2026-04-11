package com.prenticeweb.weightlossbuddy.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.prenticeweb.weightlossbuddy.room.entity.KeyInfo;

@Dao
public interface KeyInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertKeyInfo(KeyInfo keyInfo);

    @Update
    ListenableFuture<Integer> updateKeyInfo(KeyInfo keyInfo);

    @Delete
    ListenableFuture<Integer> deleteKeyInfo(KeyInfo keyInfo);

    @Query("SELECT * FROM KeyInfo LIMIT 1")
    LiveData<KeyInfo> getKeyInfo();


}
