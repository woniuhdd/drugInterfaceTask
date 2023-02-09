package com.trade.dao;

import com.common.dao.GenericDao;
import com.trade.model.BaseCompanyInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseCompanyInfoDao extends GenericDao<BaseCompanyInfo, String> {
    /**
     * 清除所有数据
     * @return
     */
    int deleteAllDatas();
}
