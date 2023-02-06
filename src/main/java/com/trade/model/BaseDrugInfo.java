package com.trade.model;

import java.util.Date;
import java.math.BigDecimal;


/**
 *
 * @Since 2010-2023
 * @Description: TODO
 * @author ***
 * @date 2023-02-06 13:34:22
 *
 */
public class BaseDrugInfo {

	//alias
	public static final String TABLE_ALIAS = "商品信息";

	//columns START
	/**
	 * @Fields drugCode:药品统一编码
	 */
	private String drugCode;

	/**
	 * @Fields drugInfoId:产品ID
	 */
	private String drugInfoId;

	/**
	 * @Fields drugName:产品名称
	 */
	private String drugName;

	/**
	 * @Fields dosformName:剂型
	 */
	private String dosformName;

	/**
	 * @Fields specName:规格
	 */
	private String specName;

	/**
	 * @Fields convrat:转换比
	 */
	private String convrat;

	/**
	 * @Fields pac:包装
	 */
	private String pac;

	/**
	 * @Fields pacmatl:包装材质
	 */
	private String pacmatl;

	/**
	 * @Fields purcType:采购类别 0：中标药品 1：廉价药品 2：紧张药品 3：低价药品 4：自主采购药品 5：商业补充保险药品
	 */
	private String purcType;

	/**
	 * @Fields pubonlnPric:中标价格
	 */
	private BigDecimal pubonlnPric;

	/**
	 * @Fields hiPayStd:医保支付标准
	 */
	private BigDecimal hiPayStd;

	/**
	 * @Fields prodentpName:生产企业
	 */
	private String prodentpName;

	/**
	 * @Fields dclaEntpName:投标企业
	 */
	private String dclaEntpName;

	/**
	 * @Fields basMednFlag:基药属性 1：是 0：否
	 */
	private String basMednFlag;

	/**
	 * @Fields tenditmName:项目名称
	 */
	private String tenditmName;

	/**
	 * @Fields chkStas:审核状态 1：待审核 2：审核通过 3：审核不通过
	 */
	private String chkStas;

	/**
	 * @Fields pubonlnStas:挂网状态 1：已挂网 2：停止执行 3：已撤废
	 */
	private String pubonlnStas;

	/**
	 * @Fields pubonlnTime:挂网时间
	 */
	private Date pubonlnTime;

	/**
	 * @Fields pubonlnRsltId:挂网结果ID
	 */
	private String pubonlnRsltId;

	/**
	 * @Fields minuntName:最小使用单位
	 */
	private String minuntName;

	/**
	 * @Fields minpacuntName:最小包装单位
	 */
	private String minpacuntName;

	/**
	 * @Fields admdvs:医保区划
	 */
	private String admdvs;

	/**
	 * @Fields admdvsName:医保区划名称
	 */
	private String admdvsName;

	/**
	 * @Fields drugExpy:药品有效期
	 */
	private String drugExpy;

	/**
	 * @Fields linkagePric:联动后价格
	 */
	private BigDecimal linkagePric;

	/**
	 * @Fields linkagePurcPric:联动(采购)价格
	 */
	private BigDecimal linkagePurcPric;

	/**
	 * @Fields maxHiPayStd:最高医保支付标准
	 */
	private BigDecimal maxHiPayStd;

	/**
	 * @Fields prodentpCode:生产厂家CODE
	 */
	private String prodentpCode;

	//columns END

	public BaseDrugInfo(){
	}

	public BaseDrugInfo(String pubonlnRsltId){
		this.pubonlnRsltId = pubonlnRsltId;
	}


	public void setDrugCode(String drugCode){
		this.drugCode = drugCode;
	}

	public String getDrugCode(){
		return drugCode;
	}

	public void setDrugInfoId(String drugInfoId){
		this.drugInfoId = drugInfoId;
	}

	public String getDrugInfoId(){
		return drugInfoId;
	}

	public void setDrugName(String drugName){
		this.drugName = drugName;
	}

	public String getDrugName(){
		return drugName;
	}

	public void setDosformName(String dosformName){
		this.dosformName = dosformName;
	}

	public String getDosformName(){
		return dosformName;
	}

	public void setSpecName(String specName){
		this.specName = specName;
	}

	public String getSpecName(){
		return specName;
	}

	public void setConvrat(String convrat){
		this.convrat = convrat;
	}

	public String getConvrat(){
		return convrat;
	}

	public void setPac(String pac){
		this.pac = pac;
	}

	public String getPac(){
		return pac;
	}

	public void setPacmatl(String pacmatl){
		this.pacmatl = pacmatl;
	}

	public String getPacmatl(){
		return pacmatl;
	}

	public void setPurcType(String purcType){
		this.purcType = purcType;
	}

	public String getPurcType(){
		return purcType;
	}

	public void setPubonlnPric(BigDecimal pubonlnPric){
		this.pubonlnPric = pubonlnPric;
	}

	public BigDecimal getPubonlnPric(){
		return pubonlnPric;
	}

	public void setHiPayStd(BigDecimal hiPayStd){
		this.hiPayStd = hiPayStd;
	}

	public BigDecimal getHiPayStd(){
		return hiPayStd;
	}

	public void setProdentpName(String prodentpName){
		this.prodentpName = prodentpName;
	}

	public String getProdentpName(){
		return prodentpName;
	}

	public void setDclaEntpName(String dclaEntpName){
		this.dclaEntpName = dclaEntpName;
	}

	public String getDclaEntpName(){
		return dclaEntpName;
	}

	public void setBasMednFlag(String basMednFlag){
		this.basMednFlag = basMednFlag;
	}

	public String getBasMednFlag(){
		return basMednFlag;
	}

	public void setTenditmName(String tenditmName){
		this.tenditmName = tenditmName;
	}

	public String getTenditmName(){
		return tenditmName;
	}

	public void setChkStas(String chkStas){
		this.chkStas = chkStas;
	}

	public String getChkStas(){
		return chkStas;
	}

	public void setPubonlnStas(String pubonlnStas){
		this.pubonlnStas = pubonlnStas;
	}

	public String getPubonlnStas(){
		return pubonlnStas;
	}

	public void setPubonlnTime(Date pubonlnTime){
		this.pubonlnTime = pubonlnTime;
	}

	public Date getPubonlnTime(){
		return pubonlnTime;
	}

	public void setPubonlnRsltId(String pubonlnRsltId){
		this.pubonlnRsltId = pubonlnRsltId;
	}

	public String getPubonlnRsltId(){
		return pubonlnRsltId;
	}

	public void setMinuntName(String minuntName){
		this.minuntName = minuntName;
	}

	public String getMinuntName(){
		return minuntName;
	}

	public void setMinpacuntName(String minpacuntName){
		this.minpacuntName = minpacuntName;
	}

	public String getMinpacuntName(){
		return minpacuntName;
	}

	public void setAdmdvs(String admdvs){
		this.admdvs = admdvs;
	}

	public String getAdmdvs(){
		return admdvs;
	}

	public void setAdmdvsName(String admdvsName){
		this.admdvsName = admdvsName;
	}

	public String getAdmdvsName(){
		return admdvsName;
	}

	public void setDrugExpy(String drugExpy){
		this.drugExpy = drugExpy;
	}

	public String getDrugExpy(){
		return drugExpy;
	}

	public void setLinkagePric(BigDecimal linkagePric){
		this.linkagePric = linkagePric;
	}

	public BigDecimal getLinkagePric(){
		return linkagePric;
	}

	public void setLinkagePurcPric(BigDecimal linkagePurcPric){
		this.linkagePurcPric = linkagePurcPric;
	}

	public BigDecimal getLinkagePurcPric(){
		return linkagePurcPric;
	}

	public void setMaxHiPayStd(BigDecimal maxHiPayStd){
		this.maxHiPayStd = maxHiPayStd;
	}

	public BigDecimal getMaxHiPayStd(){
		return maxHiPayStd;
	}

	public void setProdentpCode(String prodentpCode){
		this.prodentpCode = prodentpCode;
	}

	public String getProdentpCode(){
		return prodentpCode;
	}

}
