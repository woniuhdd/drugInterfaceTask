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
 * 药品获取收货信息
 * </p>
 *
 * @author hdd
 * @since 2023-08-23
 */
@Data
@TableName("MIDDLE_ORDER_SHP")
public class MiddleOrderShp implements Serializable {

    private static final long serialVersionUID = 1L;

    //发货明细Id
    @TableId(value = "SHP_ID", type = IdType.ID_WORKER_STR)
    private String shpId;

    //(value = "订单编码")
    @TableField("ORD_CODE")
    private String ordCode;

    //(value = "产品名称")
    @TableField("PROD_NAME")
    private String prodName;

    //(value = "剂型")
    @TableField("DOSFORM")
    private String dosform;

    //(value = "规格")
    @TableField("PROD_SPEC")
    private String prodSpec;

    //(value = "包装")
    @TableField("PROD_PAC")
    private String prodPac;

    //(value = "生产企业")
    @TableField("PRODENTP_NAME")
    private String prodentpName;

    //(value = "医疗机构")
    @TableField("MEDINS_NAME")
    private String medinsName;

    //(value = "医院采购价（元）")
    @TableField("PURCPRIC")
    private Double purcpric;

    //(value = "采购数量")
    @TableField("PURC_CNT")
    private Long purcCnt;

    //(value = "发货数量")
    @TableField("SHP_CNT")
    private Long shpCnt;

    //(value = "收货数量")
    @TableField("SHPP_CNT")
    private Long shppCnt;

    //(value = "采购金额（元）")
    @TableField("PURC_AMT")
    private Double purcAmt;

    //(value = "发货金额（元）")
    @TableField("SHP_AMT")
    private Double shpAmt;

    //(value = "收货金额（元）")
    @TableField("SHPP_AMT")
    private Double shppAmt;

    //(value = "挂网价（元）")
    @TableField("PUBONLN_PRIC")
    private Double pubonlnPric;

    //(value = "发送时间")
    @TableField("SEND_TIME")
    private Date sendTime;

    //(value = "阅读时间")
    @TableField("READ_TIME")
    private Date readTime;

    //(value = "备注")
    @TableField("PLAN_DETL_MEMO")
    private String planDetlMemo;

    //(value = "药品统一编码")
    @TableField("PROD_CODE")
    private String prodCode;

    //(value = "订单发货状态 订单发货状态 1.待发货 2.已发货 3.已收货 4已作废")
    @TableField("SHP_STAS")
    private String shpStas;

    //(value = "订单明细ID")
    @TableField("ORD_DETL_ID")
    private String ordDetlId;

    //(value = "配送地址ID")
    @TableField("ADDR_ID")
    private String addrId;

    //(value = "配送地址")
    @TableField("ADDR")
    private String addr;

    //(value = "一票发票号 多个发票号逗号隔开")
    @TableField("SEL_INVO_ONE")
    private String selInvoOne;

    //(value = "二票发票号 多个发票号逗号隔开")
    @TableField("SEL_INVO_SCD")
    private String selInvoScd;

    //(value = "生产批号")
    @TableField("MANU_LOTNUM")
    private String manuLotnum;

    //(value = "有效期")
    @TableField("EXPY_ENDTIME")
    private Date expyEndtime;

    @TableField("ITEM_CODG")
    private String itemCodg;

    @TableField("TWO_INFONO")
    private String twoInfono;

    @TableField("HOSP_LIST_ID")
    private String hospListId;

    @TableField("ESSDRUG_TYPE")
    private String essdrugType;

    @TableField("MINUNT_NAME")
    private String minuntName;

    @TableField("DELVENTP_CODE")
    private String delventpCode;

    @TableField("SHP_TIME")
    private Date shpTime;

    @TableField("SHPP_TIME")
    private Date shppTime;

    @TableField("MINPACUNT_NAME")
    private String minpacuntName;

    @TableField("ITEMNAME")
    private String itemname;

    @TableField("SHP_MEMO")
    private String shpMemo;

    @TableField("RTNB_CNT")
    private Double rtnbCnt;

    @TableField("PROD_TYPE")
    private String prodType;

    @TableField("SEL_LIST")
    private String selList;

    @TableField("CONVRAT")
    private String convrat;

    @TableField("RETN_CNT")
    private Double retnCnt;

    @TableField("PROD_ID")
    private String prodId;

    @TableField("APRVNO")
    private String aprvno;

    @TableField("DCLA_ENTP_CODE")
    private String dclaEntpCode;

    @TableField("SHP_CODE")
    private String shpCode;

    @TableField("SPLM_FLAG")
    private String splmFlag;

    @TableField("DELVENTP_NAME")
    private String delventpName;

    @TableField("PACMATL")
    private String pacmatl;

    @TableField("DCLA_ENTP_NAME")
    private String dclaEntpName;

    @TableField("HOSP_BIDPRCU_ITEM_ID")
    private String hospBidprcuItemId;

    @TableField("MEDINS_CODE")
    private String medinsCode;
}
