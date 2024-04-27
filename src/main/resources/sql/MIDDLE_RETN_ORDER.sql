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

Date: 2024-04-25 16:24:10
*/


-- ----------------------------
-- Table structure for MIDDLE_RETN_ORDER
-- ----------------------------
-- DROP TABLE "NMYP"."MIDDLE_RETN_ORDER";
CREATE TABLE "NMYP"."MIDDLE_RETN_ORDER" (
                                            "ORD_CODE" VARCHAR2(40 BYTE) NULL ,
                                            "RETN_ID" VARCHAR2(100 BYTE) NOT NULL ,
                                            "PROD_NAME" VARCHAR2(255 BYTE) NULL ,
                                            "DRUG_CODE" VARCHAR2(255 BYTE) NULL ,
                                            "DOSFORM" VARCHAR2(255 BYTE) NULL ,
                                            "PROD_SPEC" VARCHAR2(255 BYTE) NULL ,
                                            "PROD_MATL" VARCHAR2(255 BYTE) NULL ,
                                            "PROD_PAC" VARCHAR2(255 BYTE) NULL ,
                                            "PAC_MATL" VARCHAR2(255 BYTE) NULL ,
                                            "PRODENTP_NAME" VARCHAR2(255 BYTE) NULL ,
                                            "PURCPRIC" NUMBER(18,2) NULL ,
                                            "MEDINS_NAME" VARCHAR2(255 BYTE) NULL ,
                                            "MEDINS_CODE" VARCHAR2(255 BYTE) NULL ,
                                            "RETN_CNT" NUMBER(18,2) NULL ,
                                            "PUBONLN_PRIC" NUMBER(18,2) NULL ,
                                            "MEDINS_RETN_REA" VARCHAR2(255 BYTE) NULL ,
                                            "MEDINS_RETN_TIME" DATE NULL ,
                                            "RETURN_INVOICE_ID" VARCHAR2(255 BYTE) NULL ,
                                            "ORD_DETL_ID" VARCHAR2(255 BYTE) NULL ,
                                            "MANU_LOTNUM" VARCHAR2(255 BYTE) NULL ,
                                            "RETN_CHK_STAS" VARCHAR2(255 BYTE) NULL ,
                                            "DELVENTP_PASS_TIME" DATE NULL ,
                                            "DELVENTP_FAIL_REA" VARCHAR2(255 BYTE) NULL ,
                                            "DELVENTP_FAIL_TIME" DATE NULL ,
                                            "RETN_AMT" NUMBER(18,2) NULL ,
                                            "DELVENTP_NAME" VARCHAR2(255 BYTE) NULL ,
                                            "APRVNO" VARCHAR2(255 BYTE) NULL ,
                                            "DCLA_ENTP_NAME" VARCHAR2(255 BYTE) NULL ,
                                            "SHP_ID" VARCHAR2(255 BYTE) NULL ,
                                            "PACMATL" VARCHAR2(255 BYTE) NULL
)
    LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON TABLE "NMYP"."MIDDLE_RETN_ORDER" IS '药品获取退货订单';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."ORD_CODE" IS '订单编码';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."RETN_ID" IS '退货明细编号';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."PROD_NAME" IS '产品名称';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."DRUG_CODE" IS '药品统一编码';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."DOSFORM" IS '剂型';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."PROD_SPEC" IS '规格';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."PROD_MATL" IS '产品材质';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."PROD_PAC" IS '包装';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."PAC_MATL" IS '包装材质';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."PRODENTP_NAME" IS '生产企业';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."PURCPRIC" IS '医院采购价（元）';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."MEDINS_NAME" IS '医疗机构';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."MEDINS_CODE" IS '医疗机构code';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."RETN_CNT" IS '本次退货数量';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."PUBONLN_PRIC" IS '挂网价（元）';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."MEDINS_RETN_REA" IS '退货原因';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."MEDINS_RETN_TIME" IS '申请退货时间';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."RETURN_INVOICE_ID" IS '退货发票';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."ORD_DETL_ID" IS '订单明细ID';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."MANU_LOTNUM" IS '批号';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."RETN_CHK_STAS" IS '退货状态1：待确定 2：已确认3：已退回';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."DELVENTP_PASS_TIME" IS '配送企业通过时间';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."DELVENTP_FAIL_REA" IS '配送机构不通过原因';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."DELVENTP_FAIL_TIME" IS '配送企业不通过时间';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."RETN_AMT" IS '退货总金额';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."DELVENTP_NAME" IS '配送企业名称';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."APRVNO" IS '批准文号';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."DCLA_ENTP_NAME" IS '代理（申报）企业名称';
COMMENT ON COLUMN "NMYP"."MIDDLE_RETN_ORDER"."SHP_ID" IS '发货id';

-- ----------------------------
-- Indexes structure for table MIDDLE_RETN_ORDER
-- ----------------------------

-- ----------------------------
-- Checks structure for table MIDDLE_RETN_ORDER
-- ----------------------------
ALTER TABLE "NMYP"."MIDDLE_RETN_ORDER" ADD CHECK ("RETN_ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table MIDDLE_RETN_ORDER
-- ----------------------------
ALTER TABLE "NMYP"."MIDDLE_RETN_ORDER" ADD PRIMARY KEY ("RETN_ID");
