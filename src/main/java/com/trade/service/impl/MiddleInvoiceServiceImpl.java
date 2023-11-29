package com.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trade.dao.MiddleInvoiceDao;
import com.trade.model.MiddleInvoice;
import com.trade.service.MiddleInvoiceService;
import org.springframework.stereotype.Service;

@Service
public class MiddleInvoiceServiceImpl extends ServiceImpl<MiddleInvoiceDao, MiddleInvoice> implements MiddleInvoiceService {
}
