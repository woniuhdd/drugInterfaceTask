package com.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "intf")
public class SystemConfig {

    public static String url;

    /**
     * 接口调用凭证请求地址
     */
    public static final String TOKEN_URL="/tps-local-bd/intf/api/accessToken/getAccessEntpToken";

    /**
     * 请求地址
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

}
