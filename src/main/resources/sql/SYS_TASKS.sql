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

Date: 2023-01-31 16:56:55
*/


-- ----------------------------
-- Table structure for SYS_TASKS
-- ----------------------------
DROP TABLE "NMYP"."SYS_TASKS";
CREATE TABLE "NMYP"."SYS_TASKS" (
"TASK_ID" VARCHAR2(50 BYTE) NOT NULL ,
"TASK_NAME" VARCHAR2(255 BYTE) NULL ,
"TASK_CRON" VARCHAR2(255 BYTE) NULL ,
"TASK_CLASS" VARCHAR2(255 BYTE) NULL ,
"ISUSING" VARCHAR2(2 BYTE) NULL ,
"ADD_TIME" DATE NULL 
)
LOGGING
NOCOMPRESS
NOCACHE

;
COMMENT ON TABLE "NMYP"."SYS_TASKS" IS '定时任务';
COMMENT ON COLUMN "NMYP"."SYS_TASKS"."TASK_ID" IS '任务ID';
COMMENT ON COLUMN "NMYP"."SYS_TASKS"."TASK_NAME" IS '任务名称';
COMMENT ON COLUMN "NMYP"."SYS_TASKS"."TASK_CRON" IS '任务时间';
COMMENT ON COLUMN "NMYP"."SYS_TASKS"."TASK_CLASS" IS '任务类';
COMMENT ON COLUMN "NMYP"."SYS_TASKS"."ISUSING" IS '任务是否启用';
COMMENT ON COLUMN "NMYP"."SYS_TASKS"."ADD_TIME" IS '添加时间';

-- ----------------------------
-- Records of SYS_TASKS
-- ----------------------------

-- ----------------------------
-- Indexes structure for table SYS_TASKS
-- ----------------------------

-- ----------------------------
-- Checks structure for table SYS_TASKS
-- ----------------------------
ALTER TABLE "NMYP"."SYS_TASKS" ADD CHECK ("TASK_ID" IS NOT NULL);

-- ----------------------------
-- Primary Key structure for table SYS_TASKS
-- ----------------------------
ALTER TABLE "NMYP"."SYS_TASKS" ADD PRIMARY KEY ("TASK_ID");
