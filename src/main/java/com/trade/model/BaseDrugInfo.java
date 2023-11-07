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
 * 挂网药品信息
 * </p>
 *
 * @author hdd
 * @since 2023-11-07
 */
@Data
@TableName("BASE_DRUG_INFO")
public class BaseDrugInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	//(value = "药品统一编码")
	@TableId(value = "DRUG_CODE", type = IdType.ID_WORKER_STR)
	private String drugCode;

	//(value = "挂网产品 ID")
	@TableField("PUBON_LN_RSLT_ID")
	private String pubonLnRsltId;

	//(value = "产品名称")
	@TableField("DRUG_NAME")
	private String drugName;

	//(value = "剂型")
	@TableField("DOSFORM_NAME")
	private String dosformName;

	//(value = "规格")
	@TableField("SPEC_NAME")
	private String specName;

	//(value = "转换比")
	@TableField("CONVRAT")
	private String convrat;

	//(value = "包装")
	@TableField("PAC")
	private String pac;

	//(value = "包装材质")
	@TableField("PACMATL")
	private String pacmatl;

	//(value = "挂网价")
	@TableField("PUBONLN_PRIC")
	private Double pubonlnPric;

	//(value = "生产企业")
	@TableField("PRODENTP_NAME")
	private String prodentpName;

	//(value = "代理企业")
	@TableField("DCLA_ENTP_NAME")
	private String dclaEntpName;

	//(value = "基药属性 0：否 1：是")
	@TableField("BAS_MEDN_FLAG")
	private String basMednFlag;

	//(value = "项目名称")
	@TableField("TENDITM_NAME")
	private String tenditmName;

	//(value = "挂网状态 0：未挂网1：已挂网")
	@TableField("PUBONLN_STAS")
	private String pubonlnStas;

	//(value = "挂网时间")
	@TableField("PUBONLN_TIME")
	private Date pubonlnTime;

	//(value = "带量批次")
	@TableField("PURC_PROD_TYPE")
	private String purcProdType;

	//(value = "是否集采备选")
	@TableField("SPARE")
	private String spare;

	//(value = "产品来源")
	@TableField("PROD_SOUC_VAL")
	private String prodSoucVal;

	//(value = "仿制药一致性评价药品 0：否1：是")
	@TableField("CONSEVAL_DRUG")
	private String consevalDrug;

	//(value = "批准文号")
	@TableField("APRVNO")
	private String aprvno;

	//(value = "最后更新时间")
	@TableField("UPDT_TIME")
	private Date updtTime;

}

