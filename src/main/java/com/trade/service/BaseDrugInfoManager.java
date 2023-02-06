package com.trade.service;

import com.common.service.GenericManager;
import com.trade.model.BaseDrugInfo;

public interface BaseDrugInfoManager extends GenericManager<BaseDrugInfo, String> {
	// 扩展接口
    boolean deleteAllDatas();
}
