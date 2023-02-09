package com.trade.service;

import com.common.service.GenericManager;
import com.trade.model.BaseCompanyInfo;

public interface BaseCompanyInfoManager extends GenericManager<BaseCompanyInfo, String> {
    /**
     * 清除所有数据
     * @return
     */
    boolean deleteAllDatas();
}
