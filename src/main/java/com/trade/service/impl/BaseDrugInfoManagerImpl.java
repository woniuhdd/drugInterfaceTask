package com.trade.service.impl;

import com.common.service.impl.GenericManagerImpl;
import com.trade.dao.BaseDrugInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.trade.service.BaseDrugInfoManager;

import com.trade.model.BaseDrugInfo;

@Service
public class BaseDrugInfoManagerImpl extends GenericManagerImpl<BaseDrugInfo, String> implements BaseDrugInfoManager {
	// 扩展接口实现

    @Autowired
    private BaseDrugInfoDao baseDrugInfoDao;

    @Override
    public boolean deleteAllDatas() {
        return baseDrugInfoDao.deleteAllDatas()>0;
    }
}
