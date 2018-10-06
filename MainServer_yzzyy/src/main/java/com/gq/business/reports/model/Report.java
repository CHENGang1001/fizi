package com.gq.business.reports.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * One sentence description Detailed description
 * 
 * @author Administrator
 * @version [version, 2016年1月5日]
 * @see [relevant class/method]
 * @since [product/module version]
 */
@XmlRootElement(name = "Report")
public class Report
{
    private String date;
    
    private String repoter;
    
    public String getDate()
    {
        return date;
    }
    
    public void setDate(String date)
    {
        this.date = date;
    }
    
    public String getRepoter()
    {
        return repoter;
    }
    
    public void setRepoter(String repoter)
    {
        this.repoter = repoter;
    }
    
}
