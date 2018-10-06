package com.gq.common.utils;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.gq.business.inforesources.model.HealthListBean;
/**
 * 此类用作中医院官网养身保健数据的处理使用
 * 把数据存放到全局变量list中做备份
 * */
public class HealthListUtils {
		public static ArrayList<HealthListBean> healthlist;
		public static String healthHead;
		public static String healthListHead;
		public static String healths;
		public static String healthEnd;
		public static String healthStart3;
		
		//把新闻列表数据放到list中
		public static void List(String html){
			healthHead = html.substring(html.indexOf("{"),html.indexOf("[")); 
			String i= html.substring(html.indexOf("["),html.indexOf("]")+1); 
	        JSONArray jsonarray = JSONArray.fromObject(i);  
	        healthlist = (ArrayList<HealthListBean>) JSONArray.toList(jsonarray, new HealthListBean(), new JsonConfig());
		}
		
		//把nr字段的数据添加到对应的id下面的nr字段中
		public static void AddNr(String i , String nr){
	        for(int j =0;j<healthlist.size();j++){
	        	if(healthlist.get(j).getId().equals(i)){
	        		healthlist.get(j).setNr(nr);
	        	}
	        }
		}
		
		//根据id找出对应的nr字段内容
		public static String selectNr(String i){
	        for(int j =0;j<healthlist.size();j++){
	        	if(healthlist.get(j).getId().equals(i)){
	        		healths = healthlist.get(j).getNr();
	        	}
	        }
			return healths;
		}
		
		//拼出标准json数据的头部和尾部两个字段
		public static void String(String string){
		String start1;
		String start;
		String start2;
		
		//取整个字符串的最后几位
		healthEnd = string.substring(string.length()-4);
		//取”nr“字段之前的内容，包括”nr“
		 start = string.substring(0,string.indexOf("nr")+6);
		//取 {[{ 这段字符
		 start1 = start.substring(0,start.indexOf("[")+2);
		//取 “nr”：“ 这段字符
		 start2 = start.substring(start.indexOf("nr")-1,start.indexOf("nr")+5);
		//拼出{[{”nr“：" "}]}这样完整的json格式
		 healthStart3 = start1 + start2;
		}
}
