package com.gq.business.reports.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gq.business.reports.mappers.ReportMapper;
import com.gq.business.reports.model.Report;
import com.gq.business.reports.service.IReportsService;

/**
 * 
 * One sentence description Detailed description
 * 
 * @author Administrator
 * @version [version, 2016年1月5日]
 * @see [relevant class/method]
 * @since [product/module version]
 */
@Service
public class ReportsServiceImpl implements IReportsService
{
    @Autowired
    private ReportMapper reportMapper;
    
    @Override
    public Report getReportById(String id)
    {
        return this.reportMapper.getReportById(id);
    }
    
}
