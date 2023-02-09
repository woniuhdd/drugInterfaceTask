package com.trade.service;

import com.common.service.GenericManager;
import com.trade.model.BaseHospitalInfo;

public interface BaseHospitalInfoManager extends GenericManager<BaseHospitalInfo, String> {
    /**
     * 清除所有数据
     * @return
     */
    boolean deleteAllDatas();
}
