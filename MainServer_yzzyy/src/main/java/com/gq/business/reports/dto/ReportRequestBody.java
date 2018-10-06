package com.gq.business.reports.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gq.common.request.RequestBody;
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportRequestBody extends RequestBody
{
	private long reportId;
	
    private String date;
    
    public long getReportId() {
    	return reportId;
    }
    
    public void setReportId(long reportId) {
    	this.reportId = reportId;
    }
    
    public String getDate()
    {
        return date;
    }
    
    public void setDate(String date)
    {
        this.date = date;
    }
    
}
