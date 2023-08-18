package com.trade.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.trade.dao.MiddleInvoiceInfoDao;
import com.trade.model.MiddleInvoiceInfo;
import com.trade.service.MiddleInvoiceInfoService;
import org.springframework.stereotype.Service;

@Service
public class MiddleInvoiceInfoServiceImpl extends ServiceImpl<MiddleInvoiceInfoDao, MiddleInvoiceInfo> implements MiddleInvoiceInfoService {
}
