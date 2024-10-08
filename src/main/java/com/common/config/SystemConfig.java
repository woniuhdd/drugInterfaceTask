package com.common.config;

public class SystemConfig {

    public static final String url="http://60.31.22.187:9100";

    /**
     * 接口调用凭证请求地址
     */
    public static final String TOKEN_URL="/tps-local-bd/web/interface/accessToken/getAccessToken";

    /**
     * 通用请求地址
     */
    public static final String COMMON_INTERFACES_URL="/tps-local-bd/web/interface/commonInterface";

    /**
     * 获取药品
     */
    public static final String GET_DRUG="C1001";

    /**
     * 获取生产企业
     */
    public static final String GET_COMPANY="C1002";

    /**
     * 获取医疗机构
     */
    public static final String GET_HOSPITAL="C1003";

    /**
     * 获取采购订单
     */
    public static final String GET_ORDER="C1004";

    /**
     * 获取发票信息
     */
    public static final String GET_INVOICE_INFO="C1005";

    /**
     * 维护发票信息
     */
    public static final String SEND_INVOICE_INFO="C1006";

    /**
     * 发票附件上传
     */
    public static final String SEND_INVOICE_IMG="C1007";

    /**
     * 药品发货信息上传
     */
    public static final String SEND_ORDER_DIS="C1008";

    /**
     * 药品获取收货信息
     */
    public static final String GET_ORDER_SHP="C1009";

    /**
     * 药品获取退货订单
     */
    public static final String GET_ORDER_RETN="C1010";

    /**
     * 药品退货订单响应
     */
    public static final String SEND_ORDER_RETN="C1011";

    /**
     * 药品设置发票
     */
    public static final String SEND_INVOICE_SHP="C1012";

}
