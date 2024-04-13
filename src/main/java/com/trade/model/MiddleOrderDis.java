package com.trade.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MiddleOrderDis {

    @TableId(value = "ORDER_DIS_ID")
    private String 	orderDisId	;//	企业主键
    private String 	shpId	;//	发货id
    private Integer shpCnt	;//	配送数量
    private String 	manuLotnum	;//	生产批号
    private Date 	expyEndtime	;//	有效期
    private String 	shpMemo	;//	发货备注
    private String 	responseState	;//	省平台交互状态(0:未交互  2:上传成功  3:上传失败)
    private String 	responseInfo	;//	省平台交互信息
    private Date responseTime	;//	省平台交互时间
}
