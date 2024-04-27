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
 * 药品获取退货订单
 * </p>
 *
 * @author hdd
 * @since 2023-08-23
 */
@Data
@TableName("MIDDLE_RETN_ORDER")
public class MiddleOrderRetn implements Serializable {

    private static final long serialVersionUID = 1L;

    //(value = "订单编码")
    @TableField("ORD_CODE")
    private String ordCode;

    //(value = "退货明细编号")
    @TableId(value = "RETN_ID", type = IdType.ID_WORKER_STR)
    private String retnId;

    //(value = "产品名称")
    @TableField("PROD_NAME")
    private String prodName;

    //(value = "药品统一编码")
    @TableField("DRUG_CODE")
    private String drugCode;

    //(value = "剂型")
    @TableField("DOSFORM")
    private String dosform;

    //(value = "规格")
    @TableField("PROD_SPEC")
    private String prodSpec;

    //(value = "产品材质")
    @TableField("PROD_MATL")
    private String prodMatl;

    //(value = "包装")
    @TableField("PROD_PAC")
    private String prodPac;

    //(value = "包装材质")
    @TableField("PAC_MATL")
    private String pacMatl;

    //(value = "生产企业")
    @TableField("PRODENTP_NAME")
    private String prodentpName;

    //(value = "医院采购价（元）")
    @TableField("PURCPRIC")
    private Double purcpric;

    //(value = "医疗机构")
    @TableField("MEDINS_NAME")
    private String medinsName;

    //(value = "医疗机构code")
    @TableField("MEDINS_CODE")
    private String medinsCode;

    //(value = "本次退货数量")
    @TableField("RETN_CNT")
    private Long retnCnt;

    //(value = "挂网价（元）")
    @TableField("PUBONLN_PRIC")
    private Double pubonlnPric;

    //(value = "退货原因")
    @TableField("MEDINS_RETN_REA")
    private String medinsRetnRea;

    //(value = "申请退货时间")
    @TableField("MEDINS_RETN_TIME")
    private Date medinsRetnTime;

    //(value = "退货发票")
    @TableField("RETURN_INVOICE_ID")
    private String returnInvoiceId;

    //(value = "订单明细ID")
    @TableField("ORD_DETL_ID")
    private String ordDetlId;

    //(value = "批号")
    @TableField("MANU_LOTNUM")
    private String manuLotnum;

    //(value = "退货状态1：待确定 2：已确认3：已退回")
    @TableField("RETN_CHK_STAS")
    private String retnChkStas;

    //(value = "配送企业通过时间")
    @TableField("DELVENTP_PASS_TIME")
    private Date delventpPassTime;

    //(value = "配送机构不通过原因")
    @TableField("DELVENTP_FAIL_REA")
    private String delventpFailRea;

    //(value = "配送企业不通过时间")
    @TableField("DELVENTP_FAIL_TIME")
    private Date delventpFailTime;

    //(value = "退货总金额")
    @TableField("RETN_AMT")
    private Double retnAmt;

    //(value = "配送企业名称")
    @TableField("DELVENTP_NAME")
    private String delventpName;

    //(value = "批准文号")
    @TableField("APRVNO")
    private String aprvno;

    //(value = "代理（申报）企业名称")
    @TableField("DCLA_ENTP_NAME")
    private String dclaEntpName;

    //(value = "发货id")
    @TableField("SHP_ID")
    private String shpId;

    @TableField("PACMATL")
    private String pacmatl;

}
