/*
Navicat Oracle Data Transfer
Oracle Client Version : 11.2.0.1.0

Source Server         : 127nmyp
Source Server Version : 110200
Source Host           : 127.0.0.1:1521
Source Schema         : NMYP

Target Server Type    : ORACLE
Target Server Version : 110200
File Encoding         : 65001

Date: 2023-01-31 16:56:42
*/


-- ----------------------------
-- Table structure for BASE_DRUG_INFO
-- ----------------------------
DROP TABLE "NMYP"."BASE_DRUG_INFO";
CREATE TABLE "NMYP"."BASE_DRUG_INFO" (
"DRUG_CODE" VARCHAR2(40 BYTE) NULL ,
"DRUG_INFO_ID" VARCHAR2(100 BYTE) NULL ,
"DRUG_NAME" VARCHAR2(255 BYTE) NULL ,
"DOSFORM_NAME" VARCHAR2(100 BYTE) NULL ,
"SPEC_NAME" VARCHAR2(100 BYTE) NULL ,
"CONVRAT" VARCHAR2(3 BYTE) NULL ,
"PAC" VARCHAR2(50 BYTE) NULL ,
"PACMATL" VARCHAR2(200 BYTE) NULL ,
"PURC_TYPE" VARCHAR2(7 BYTE) NULL ,
"PUBONLN_PRIC" NUMBER(18,2) NULL ,
"HI_PAY_STD" NUMBER(18,2) NULL ,
"PRODENTP_NAME" VARCHAR2(100 BYTE) NULL ,
"DCLA_ENTP_NAME" VARCHAR2(100 BYTE) NULL ,
"BAS_MEDN_FLAG" VARCHAR2(3 BYTE) NULL ,
"TENDITM_NAME" VARCHAR2(100 BYTE) NULL ,
"CHK_STAS" VARCHAR2(7 BYTE) NULL ,
"PUBONLN_STAS" VARCHAR2(5 BYTE) NULL ,
"PUBONLN_TIME" DATE NULL ,
"PUBONLN_RSLT_ID" VARCHAR2(50 BYTE) NOT NULL ,
"MINUNT_NAME" VARCHAR2(255 BYTE) NULL ,
"MINPACUNT_NAME" VARCHAR2(255 BYTE) NULL ,
"ADMDVS" VARCHAR2(20 BYTE) NULL ,
"ADMDVS_NAME" VARCHAR2(50 BYTE) NULL ,
"DRUG_EXPY" VARCHAR2(20 BYTE) NULL ,
"LINKAGE_PRIC" NUMBER(18,2) NULL ,
"LINKAGE_PURC_PRIC" NUMBER(18,2) NULL ,
"MAX_HI_PAY_STD" NUMBER(18,2) NULL ,
"PRODENTP_CODE" VARCHAR2(50 BYTE) NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON TABLE "NMYP"."BASE_DRUG_INFO" IS '商品信息';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."DRUG_CODE" IS '药品统一编码';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."DRUG_INFO_ID" IS '产品ID';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."DRUG_NAME" IS '产品名称';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."DOSFORM_NAME" IS '剂型';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."SPEC_NAME" IS '规格';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."CONVRAT" IS '转换比';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."PAC" IS '包装';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."PACMATL" IS '包装材质';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."PURC_TYPE" IS '采购类别 0：中标药品 1：廉价药品 2：紧张药品 3：低价药品 4：自主采购药品 5：商业补充保险药品';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."PUBONLN_PRIC" IS '中标价格';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."HI_PAY_STD" IS '医保支付标准';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."PRODENTP_NAME" IS '生产企业';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."DCLA_ENTP_NAME" IS '投标企业';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."BAS_MEDN_FLAG" IS '基药属性 1：是 0：否';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."TENDITM_NAME" IS '项目名称';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."CHK_STAS" IS '审核状态 1：待审核 2：审核通过 3：审核不通过';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."PUBONLN_STAS" IS '挂网状态 1：已挂网 2：停止执行 3：已撤废';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."PUBONLN_TIME" IS '挂网时间';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."PUBONLN_RSLT_ID" IS '挂网结果ID';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."MINUNT_NAME" IS '最小使用单位';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."MINPACUNT_NAME" IS '最小包装单位';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."ADMDVS" IS '医保区划';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."ADMDVS_NAME" IS '医保区划名称';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."DRUG_EXPY" IS '药品有效期';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."LINKAGE_PRIC" IS '联动后价格';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."LINKAGE_PURC_PRIC" IS '联动(采购)价格';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."MAX_HI_PAY_STD" IS '最高医保支付标准';
COMMENT ON COLUMN "NMYP"."BASE_DRUG_INFO"."PRODENTP_CODE" IS '生产厂家CODE';

-- ----------------------------
-- Records of BASE_DRUG_INFO
-- ----------------------------

-- ----------------------------
-- Indexes structure for table BASE_DRUG_INFO
-- ----------------------------

-- ----------------------------
-- Checks structure for table BASE_DRUG_INFO
-- ----------------------------
ALTER TABLE "NMYP"."BASE_DRUG_INFO" ADD CHECK ("PUBONLN_RSLT_ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table BASE_DRUG_INFO
-- ----------------------------
ALTER TABLE "NMYP"."BASE_DRUG_INFO" ADD PRIMARY KEY ("PUBONLN_RSLT_ID");
