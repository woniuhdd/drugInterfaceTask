
-- ----------------------------
-- Table structure for BASE_HOSPITAL_INFO
-- ----------------------------
DROP TABLE "NMYP"."BASE_HOSPITAL_INFO";
CREATE TABLE "NMYP"."BASE_HOSPITAL_INFO" (
  "USCC" VARCHAR2(40 BYTE) NOT NULL,
  "ENTP_CODE" VARCHAR2(100 BYTE),
  "ORG_TYPE_CODE" VARCHAR2(3 BYTE) NULL ,
  "ORG_NAME" VARCHAR2(100 BYTE),
  "CONER" VARCHAR2(100 BYTE),
  "CONER_MOD" VARCHAR2(100 BYTE),
  "ORG_INFO_ID" VARCHAR2(50 BYTE),
  "UPDT_TIME" DATE,
  "FIX_TEL" VARCHAR2(50 BYTE)
);
COMMENT ON COLUMN "NMYP"."BASE_HOSPITAL_INFO"."USCC" IS '统一社会信用代码';
COMMENT ON COLUMN "NMYP"."BASE_HOSPITAL_INFO"."ENTP_CODE" IS '企业代码';
COMMENT ON COLUMN "NMYP"."BASE_HOSPITAL_INFO"."ORG_NAME" IS '医疗机构';
COMMENT ON COLUMN "NMYP"."BASE_HOSPITAL_INFO"."CONER" IS '联系人';
COMMENT ON COLUMN "NMYP"."BASE_HOSPITAL_INFO"."CONER_MOD" IS '联系电话';
COMMENT ON COLUMN "NMYP"."BASE_HOSPITAL_INFO"."ORG_INFO_ID" IS '组织信息ID';
COMMENT ON COLUMN "NMYP"."BASE_HOSPITAL_INFO"."UPDT_TIME" IS '最后一次更新时间';
COMMENT ON COLUMN "NMYP"."BASE_HOSPITAL_INFO"."FIX_TEL" IS '固定电话';
COMMENT ON TABLE "NMYP"."BASE_HOSPITAL_INFO" IS '医疗机构信息';

-- ----------------------------
-- PRIMARY KEY STRUCTURE FOR TABLE BASE_HOSPITAL_INFO
-- ----------------------------
ALTER TABLE "NMYP"."BASE_HOSPITAL_INFO" ADD CONSTRAINT "BASE_HOSPITAL_INFO" PRIMARY KEY ("USCC");
