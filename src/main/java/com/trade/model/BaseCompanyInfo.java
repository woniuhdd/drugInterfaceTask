package com.trade.model;

import lombok.Data;

import java.util.Date;

/**
 * 生产企业信息
 */
@Data
public class BaseCompanyInfo {
    /**
     *统一社会信用代码
     */
    private String uscc;
    /**
     *企业代码
     */
    private String entpCode;
    /**
     *生产企业
     */
    private String orgName;
    /**
     *联系人
     */
    private String coner;
    /**
     *联系电话
     */
    private String conerMod;
    /**
     *组织信息ID
     */
    private String orgInfoId;
    /**
     *最后一次更新时间
     */
    private Date updtTime;
    /**
     *固定电话
     */
    private String fixTel;
}
