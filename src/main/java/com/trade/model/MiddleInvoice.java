package com.trade.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MiddleInvoice {

    @TableId(value = "INVO_ID")
    private String 	invoId	;//	发票编号(接口返回)
    private String 	invoType	;//	发票类型(1：第一票 2：第二票 3：销售清单4：消退清单5：消退发票)
    private String 	invoCode	;//	发票代码
    private String 	invono	;//	发票号
    private Double 	billAmt	;//	票据金额
    private Date 	billTime	;//	开票时间
    private String 	invottl	;//	发票抬头
    private String entpCode; //企业代码
    private String 	invoMemo	;//	发票备注
}
