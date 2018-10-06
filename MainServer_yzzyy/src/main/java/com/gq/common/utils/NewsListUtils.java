package com.gq.common.utils;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.gq.business.inforesources.model.NewsListBean;

	/**
	 * 此类用作中医院官网新闻数据的处理使用
	 * 把数据存放到全局变量list中做备份
	 * */
public class NewsListUtils {

	public static ArrayList<NewsListBean> newslist;
	public static String newHead;
	public static String newListHead;
	public static String news;
	public static String newEnd;
	public static String newStart3;
	
	//把新闻列表数据放到list中
	public static void List(String html){
		newListHead = html.substring(html.indexOf("{"),html.indexOf("[")); 
		String i= html.substring(html.indexOf("["),html.indexOf("]")+1); 
        JSONArray jsonarray = JSONArray.fromObject(i);  
        
        newslist = (ArrayList<NewsListBean>) JSONArray.toList(jsonarray, new NewsListBean(), new JsonConfig());
	}
	
	//把nr字段的数据添加到对应的id下面的nr字段中
	public static void AddNr(String i , String nr){
        for(int j =0;j<newslist.size();j++){
        	if(newslist.get(j).getId().equals(i)){
        		newslist.get(j).setNr(nr);
        	}
        }
	}
	
	//根据id找出对应的nr字段内容
	public static String selectNr(String i){
        for(int j =0;j<newslist.size();j++){
        	if(newslist.get(j).getId().equals(i)){
        		news = newslist.get(j).getNr();
        	}
        }
		return news;
	}
	
	//拼出标准json数据的头部和尾部两个字段
	public static void String(String string){
	String start1;
	String start;
	String start2;
	
	//取整个字符串的最后几位
	newEnd = string.substring(string.length()-4);
	//取”nr“字段之前的内容，包括”nr“
	 start = string.substring(0,string.indexOf("nr")+6);
	//取 {[{ 这段字符
	 start1 = start.substring(0,start.indexOf("[")+2);
	//取 “nr”：“ 这段字符
	 start2 = start.substring(start.indexOf("nr")-1,start.indexOf("nr")+5);
	//拼出{[{”nr“：" "}]}这样完整的json格式
	 newStart3 = start1 + start2;
	}
}
