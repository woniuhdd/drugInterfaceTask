package com.common.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;


/**
 * 接口请求入参
 */
@Data
public class IntfRequestBody {

    /**
     * 交易编号
     */
    private String infno;

    /**
     * 交易输入
     */
    private JSONObject input;
}
