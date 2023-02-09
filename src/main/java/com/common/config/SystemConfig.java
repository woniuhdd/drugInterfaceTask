package com.common.config;

public class SystemConfig {

    public static final String INTF_URL="http://127.0.0.1:8080";

    /**
     * 接口调用凭证请求地址
     */
    public static final String TOKEN_URL="/tps-local-bd/intf/api/accessToken/getAccessEntpToken";

    /**
     * 请求地址
     */
    public static final String COMMON_INTERFACES_URL="/tps-local-bd/intf/api/commonInterfaces";

    /**
     * 获取采购订单
     */
    public static final String GET_ORDER="ZJ9609";

    /**
     * 获取生产企业
     */
    public static final String GET_COMPANY="ZJ9618";

    /**
     * 获取医疗机构
     */
    public static final String GET_HOSPITAL="ZJ9619";
}
