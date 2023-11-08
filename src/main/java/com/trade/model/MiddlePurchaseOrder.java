package com.trade.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class MiddlePurchaseOrder {

    private String 	ordId	;//	订单 ID
    private String 	ordCode	;//	订单 CODE
    @TableId(value = "ORD_DETL_ID", type = IdType.ID_WORKER_STR)
    private String 	ordDetlId	;//	订单明细 ID
    private String 	shpId	;//	发货 ID
    private Double	pubonlnPric	;//	挂网价格
    private Double	purcpric	;//	采购价格
    private Double	purcCnt	;//	采购数量
    private String 	delventpCode	;//	配送企业 USCC
    private String 	delventpName	;//	配送企业名称
    private Date 	sendTime	;//	订单发送时间
    private String 	medinsCode	;//	医疗机构唯一
    private String 	medinsName	;//	医疗机构名称
    private String 	responseStatus	;//	订单响应状态 0：待响应 1：已响应 2：拒绝响应
    private String 	addrName	;//	收货地址
    private String 	conName	;//	收货人
    private String 	conTel	;//	收货人电话
    private String 	memo	;//	下单备注
    private String 	docmker	;//	制单人
    private String 	drugInfoId	;//	产品 ID 老系统的产品id可能为空
    private String 	prodCode	;//	国家唯一编码
    private String 	prodentpName	;//	生产企业名称
    private String 	prodentpCode	;//	生产企业 USCC
    private String 	dclaEntpName	;//	申报企业名称
    private String 	dclaEntpCode	;//	申报企业 USCC
    private String 	dosform	;//	 剂型
    private String 	prodSpec	;//	规格
    private String 	prodPac	;//	包装
    private String 	prodBigPac	;//	大包装
    private String 	pacMatl	;//	包材
    private String 	mol	;//	 型号
    private String 	prodMatl	;//	产品材质
    private String 	prodPacMatl	;//	 产品包装材质
    private String 	aprvno	;//	批准文号(注册证编号)
    private String 	shpStas	;//	发货明细状态 1:待发货 2：已发货 3：已收货 4：已作废
    private String 	shpStasVal	;//	订单明细状态
    private String 	shpMemo	;//	发货备注
    private Double	shpPric	;//	发货价格
    private Double	shpCnt	;//	发货数量
    private Double	shpAmt	;//	发货总金额
    private Double	shppCnt	;//	收货数量
    private Double	shppAmt	;//	收货总金额
    private String 	cnclType	;//	作废类型 1：医疗机构主动作废 2：配送企业超7日未发货 3：补录单作废
    private String 	cnclTypeVal	;//	作废类型注释
    private String 	prodName	;//	产品名称
    private Double	ordDetlCount	;//	订单明细数量
    private String 	addrId	;//	配送地址 ID
    private String 	pubonlnRsltId	;//	药品挂网结果
    private String 	stroomName	;//	库房名称
    private String 	ordDetlStas	;//	订单明细状态 1待发货 2全部发货 3部分发货 4缺货 5作废
    private Double	purcAmt	;//	订单金额
    private Double	ordSumamt	;//	订单总单金额
    private String 	ordDetlMemo	;//	订单明细备注
    private String 	readFlag	;//	是否已读 0未读 1已读
    private Date 	readTime	;//	订单已读时间

}
