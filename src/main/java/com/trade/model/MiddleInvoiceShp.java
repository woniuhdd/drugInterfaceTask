package com.trade.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MiddleInvoiceShp {

    @TableId(value = "MIDDLE_INVOICE_SHP_ID")
    private String 	middleInvoiceShpId	;//	企业主键
    private String 	invoId	;//	发票编号
    private String 	invoType	;//	发票类型(1：第一票 2：第二票 3：销售清单4：消退清单5：消退发票)
    private String 	shpId	;//	发票代码
    private String 	responseState	;//	省平台交互状态(0:未交互  2:上传成功  3:上传失败)
    private String 	responseInfo	;//	省平台交互信息
    private Date responseTime	;//	省平台交互时间
}
