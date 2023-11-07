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
 * 退货订单响应
 * </p>
 *
 * @author hdd
 * @since 2023-08-23
 */
@Data
@TableName("MIDDLE_ORDER_RETN_RESPONSE")
public class MiddleOrderRetnResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ORDER_RETN_RESPONSE_ID", type = IdType.ID_WORKER_STR)
    private String orderRetnResponseId;

    //(value = "退货明细编码")
    @TableField("RETN_ID")
    private String retnId;

    //(value = "确认状态 1：待确定(待审核)2：已确认(已审核)3：已退回(审核失败)")
    @TableField("RETN_CHK_STAS")
    private String retnChkStas;

    //(value = "拒绝退货时必填")
    @TableField("DELVENTP_FAIL_REA")
    private String delventpFailRea;

    //(value = "省平台交互状态 必填 0:未交互  2:上传成功  3:上传失败")
    @TableField("RESPONSE_STATE")
    private String responseState;

    //(value = "省平台交互信息")
    @TableField("RESPONSE_INFO")
    private String responseInfo;

    //(value = "省平台交互时间")
    @TableField("RESPONSE_TIME")
    private Date responseTime;
}
