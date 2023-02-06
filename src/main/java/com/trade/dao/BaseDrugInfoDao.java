package com.trade.dao;

import com.common.dao.GenericDao;
import com.trade.model.BaseDrugInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseDrugInfoDao extends GenericDao<BaseDrugInfo, String> {
    int deleteAllDatas();
}
