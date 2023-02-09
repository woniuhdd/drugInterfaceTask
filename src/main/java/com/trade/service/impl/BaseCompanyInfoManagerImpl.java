package com.trade.service.impl;

import com.common.service.impl.GenericManagerImpl;
import com.trade.dao.BaseCompanyInfoDao;
import com.trade.model.BaseCompanyInfo;
import com.trade.service.BaseCompanyInfoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BaseCompanyInfoManagerImpl extends GenericManagerImpl<BaseCompanyInfo, String>
        implements BaseCompanyInfoManager {

    @Autowired
    private BaseCompanyInfoDao baseCompanyInfoDao;

    @Override
    public boolean deleteAllDatas() {
        return baseCompanyInfoDao.deleteAllDatas()>0;
    }
}
