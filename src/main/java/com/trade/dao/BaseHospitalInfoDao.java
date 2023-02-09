package com.trade.dao;

import com.common.dao.GenericDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BaseHospitalInfoDao extends GenericDao<BaseHospitalInfoDao, String> {
    /**
     * 清除所有数据
     * @return
     */
    int deleteAllDatas();
}
