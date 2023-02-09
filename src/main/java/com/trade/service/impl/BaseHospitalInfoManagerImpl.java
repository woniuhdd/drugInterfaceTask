package com.trade.service.impl;

import com.common.service.impl.GenericManagerImpl;
import com.trade.dao.BaseHospitalInfoDao;
import com.trade.model.BaseHospitalInfo;
import com.trade.service.BaseHospitalInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseHospitalInfoManagerImpl extends GenericManagerImpl<BaseHospitalInfo, String>
        implements BaseHospitalInfoManager {

    @Autowired
    private BaseHospitalInfoDao baseHospitalInfoDao;

    @Override
    public boolean deleteAllDatas() {
        return baseHospitalInfoDao.deleteAllDatas()>0;
    }
}
