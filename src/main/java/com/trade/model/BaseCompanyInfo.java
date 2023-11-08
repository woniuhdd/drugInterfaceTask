package com.trade.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 企业信息
 * </p>
 *
 * @author hdd
 * @since 2023-11-08
 */
@Data
@TableName("BASE_COMPANY_INFO")
public class BaseCompanyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    //(value = "组织信息 ID")
    @TableId(value = "ORG_INFO_ID", type = IdType.ID_WORKER_STR)
    private String orgInfoId;

    //(value = "统一社会信用代码")
    @TableField("USCC")
    private String uscc;

    //(value = "企业代码")
    @TableField("ENTP_CODE")
    private String entpCode;

    //(value = "生产企业")
    @TableField("ORG_NAME")
    private String orgName;

    //(value = "企业类型")
    @TableField("ORG_TYPEID")
    private String orgTypeid;

    //(value = "联系人")
    @TableField("CONER")
    private String coner;

    //(value = "联系电话")
    @TableField("CONER_MOD")
    private String conerMod;

    //(value = "固定电话")
    @TableField("FIX_TEL")
    private String fixTel;

    //(value = "最后一次更新时间")
    @TableField("UPDT_TIME")
    private Date updtTime;

}
