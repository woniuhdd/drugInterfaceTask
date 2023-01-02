package com.common.entity;

import lombok.Data;

/**
 * 接口请求返参
 */
@Data
public class IntfResponseBody {

    /**
     *  交易状态码（0：成功；-1：失败）
     */
    private int infcode;
    /**
     *  接收方报文ID：接收方返回，接收方医保区划代码(6)+时间(14)+流水号(10)
     *  时间格式：yyyyMMddHHmmss
     */
    private String inf_refmsgid;
    /**
     *  接收报文时间
     *  格式：yyyyMMddHHmmssSSS
     */
    private String refmsg_time;
    /**
     *  响应报文时间
     *  格式：yyyyMMddHHmmssSSS
     */
    private String  respond_time;
    /**
     *  错误信息
     *  交易失败状态下，业务返回的错误信息
     */
    private String err_msg;
    /**
     *  交易输出
     */
    private String output;
}
