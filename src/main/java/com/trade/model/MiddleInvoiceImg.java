package com.trade.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MiddleInvoiceImg {

    @TableId(value = "uuid")
    private String 	uuid	;//	企业主键
    private String 	invoId	;//	发票编号(发票表的INVO_ID)
    private String 	imgUrl	;//	发票图片地址
    private String 	fileName	;//	附件名称
    private String 	fileId	;//	省平台返回文件地址
    private String 	responseState	;//	省平台交互状态(0:未交互  2:上传成功  3:上传失败)
    private String 	responseInfo	;//	省平台交互信息
    private Date responseTime	;//	省平台交互时间
}
