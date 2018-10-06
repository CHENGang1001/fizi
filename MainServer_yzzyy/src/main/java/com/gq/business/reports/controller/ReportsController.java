package com.gq.business.reports.controller;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gq.business.reports.dto.ReportRequest;
import com.gq.business.reports.model.Report;
import com.gq.business.reports.service.IReportsService;
import com.gq.common.utils.AdapterUtils;
import com.gq.common.utils.HttpUtils;

@Controller
public class ReportsController
{
    @Autowired
    private IReportsService rs;
    
    @RequestMapping("getReport")
    public String getReport(@RequestBody ReportRequest rr)
    {
    	long id = rr.getBody().getReportId();
        Report r = rs.getReportById(String.valueOf(id));
        System.out.println(r.getDate());
        System.out.println(r.getRepoter());
        return "aa";
    }
    
    @ResponseBody
    @RequestMapping(value = "getRestReport")
    public Report getRestReport(@RequestBody ReportRequest rr)
    {
        Report r = new Report();
        System.out.println(rr.getBody().getDate());
        r.setDate("aaa");
        r.setRepoter("bb");
        return r;
    }
    
    @RequestMapping("test")
    public void test(PrintWriter writer) {
    	writer.write("asdfasdfasdf");
    }
    
    @RequestMapping("test1")
    public void test1(PrintWriter writer) {
    	String data = "{\"method\":\"GetCenterInfos\",\"data\":{\"hospitalID\":\"1\"}}";
    	Map<String, Object> param = new HashMap<String, Object>();
    	param.put("hospitalID", "1");
    	AdapterUtils.send("GetCenterInfo", param);
//    	HttpUtils.sendPost("http://chuckcoin.gicp.net:8888/server/test", data);
//    	AdapterUtils.send(data);
    }
}
