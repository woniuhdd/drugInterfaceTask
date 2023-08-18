package com.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trade.dao.MiddlePurchaseOrderDao;
import com.trade.model.MiddlePurchaseOrder;
import com.trade.service.MiddlePurchaseOrderService;
import org.springframework.stereotype.Service;

@Service
public class MiddlePurchaseOrderServiceImpl extends ServiceImpl<MiddlePurchaseOrderDao, MiddlePurchaseOrder> implements MiddlePurchaseOrderService {
}
