package com.prenticeweb.weightlossbuddy.room.repository;

import androidx.lifecycle.LiveData;

import com.google.common.util.concurrent.ListenableFuture;
import com.prenticeweb.weightlossbuddy.room.dao.KeyInfoDao;
import com.prenticeweb.weightlossbuddy.room.entity.KeyInfo;

public class KeyInfoRepository {
    
    private final KeyInfoDao keyInfoDao;

    public KeyInfoRepository(KeyInfoDao keyInfoDao) {
        this.keyInfoDao = keyInfoDao;
    }

    public void insertKeyInfo(KeyInfo keyInfo) {
        keyInfo.setIdentity(1);
        keyInfoDao.insertKeyInfo(keyInfo);
    }
    public ListenableFuture<Integer> updateKeyInfo(KeyInfo keyInfo) {
        keyInfo.setIdentity(1);
        return keyInfoDao.updateKeyInfo(keyInfo);
    }
    public ListenableFuture<Integer> deleteKeyInfo(KeyInfo keyInfo) {
        keyInfo.setIdentity(1);
        return keyInfoDao.deleteKeyInfo(keyInfo);
    }
    public LiveData<KeyInfo> getKeyInfo() {
        return keyInfoDao.getKeyInfo();
    }
}
