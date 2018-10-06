package com.gq.business.inforesources.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gq.common.exception.ServiceException;
import com.gq.common.request.RequestEntity;
import com.gq.common.response.ResponseHeader;
import com.gq.common.utils.HealthListUtils;
import com.gq.common.utils.HttpUtils;
import com.gq.common.utils.NewsListUtils;

/**
 * 此类用作访问和处理数据
 * 中医院官网提供的新闻和养身3个接口的
 * 1新闻列表、2养身列表、3单条详情查询（新闻和养身的查询方法分2个方法体编写）
 * 
 * */
@Controller
@RequestMapping("/newsinfo")
public class GetHospitalNewsController {
    /** 
     * 通过医院官网的链接 接口去获取新闻列表
     * 
     * 方法说明：先获取接口中的数据，用if(newStr.length()>0)判断是否访问成功
     * 1 成功的情况下if(ListUtils.list==null)判断list中是否有数据，
     * 1.1 没有的话就对list加值，然后对从接口中获取的字符串转json对象，返回前端
     * 1.2 有数据的话就比较 list中的第一个id 与  字符串中的第一个id 是否相同if(FristId.equals(fristId))
     * 1.2.1 相同的话直接对从接口中获取的字符串转json对象，返回前端
     * 1.2.2 不同的话，对list重新加值
     * 2访问失败的话，就从list中取值展示
     * 如还是没有数据的话，就是项目被重启数据被重置，需要在接口正常访问时再访问一遍就会自动存下
     * 
     */  
	@RequestMapping(value = "/getNewsList")
	public  @ResponseBody JSONObject SendGETNews() throws ServiceException, IOException{
						//连接url接口
		String rusultJson = HttpUtils.sendGet("http://www.yzszyy.com/newslist_weixin.asp?lmid=2");
						//解决数据乱码问题（官网人员提醒：数据编码为GB2312）
		String newStr=new String(rusultJson.getBytes("ISO-8859-1"),"GBK");
						//判断是否访问成功
		if(newStr.length()>0){
						//判断全局变量list中是否有值
			if(NewsListUtils.newslist==null){
						//在list中无数据的情况下进行加值操作
				 NewsListUtils.List(newStr);
				 		//对newStr进行转json对象的操作
				 JSONObject newslist = JSONObject.fromObject(newStr);
				 return newslist;
			}else{		//如果list有值的情况下
						//找出list中第一个id
				String fristId=  NewsListUtils.newslist.get(0).getId();
						//取出字符串中十条记录中的第一个id
				String FristId= newStr.substring(newStr.indexOf("id")+5,newStr.indexOf("bt")-3); 
						//判断与字符串中取到的id是否相同
				if(FristId.equals(fristId)){
						//相同的话就直接转换返回前端
				    JSONObject newslist = JSONObject.fromObject(newStr);
				    return newslist;
				}else{
						//id不同的话需要对list重置，重新加值
					 NewsListUtils.List(newStr);
					 JSONObject newslist = JSONObject.fromObject(newStr);
					 return newslist;
				}
			}
		}else{			//接口访问失败
						//从list中取值展示（拼出一个完整的json格式）
			String content = JSONArray.fromObject(NewsListUtils.newslist).toString();
			String end = "}";
			String newsList = NewsListUtils.newListHead +content+end;
	    				//转json对象
			JSONObject newslist = JSONObject.fromObject(newsList);
			return newslist;
	    }
	}
	
    /**
     * 通过医院官网的链接 接口去获取养身保健列表
     * 
     * 方法说明：先获取接口中的数据，用if(healthStr.length()>0)判断是否访问成功
     * 1 成功的情况下if(HealthListUtils.healthlist==null)判断list中是否有数据，
     * 1.1 没有的话就对list加值，然后对从接口中获取的字符串转json对象，返回前端
     * 1.2 有数据的话就比较 list中的第一个id 与  字符串中的第一个id 是否相同if(FristId.equals(fristID))
     * 1.2.1 相同的话直接对从接口中获取的字符串转json对象，返回前端
     * 1.2.2 不同的话，对list重新加值
     * 2访问失败的话，就从list中取值展示
     * 如还是没有数据的话，就是项目被重启数据被重置，需要在接口正常访问时再访问一遍就会自动存下
     * 
     */  
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getHealthList")
	public static @ResponseBody JSONObject SendGETHealth(String url) throws UnsupportedEncodingException{
		JSONObject healthlist = new JSONObject();
		try{
					//连接url接口
			String rusultJson = HttpUtils.sendGet("http://www.yzszyy.com/newslist_weixin.asp?lmid=5");
							//解决数据乱码问题（官网人员提醒：数据编码为GB2312）
			String healthStr=new String(rusultJson.getBytes("ISO-8859-1"),"GBK");
							//判断是否访问成功
			if(healthStr.length()>0){
							//判断全局变量list中是否有值
				if(HealthListUtils.healthlist==null){
							//在list中无数据的情况下进行加值操作
					HealthListUtils.List(healthStr);
			 				//对newStr进行转json对象的操作
					healthlist = JSONObject.fromObject(healthStr);
				}else{		//如果list有值的情况下
							//找出list中第一个id
					String fristID =  HealthListUtils.healthlist.get(0).getId();
							//取出字符串中十条记录中的第一个id
					String FristId= healthStr.substring(healthStr.indexOf("id")+5,healthStr.indexOf("bt")-3); 
							//判断与字符串中取到的id是否相同
						if(FristId.equals(fristID)){
							//相同的话就直接转换返回前端
							healthlist = JSONObject.fromObject(healthStr);
						}else{
							//id不同的话需要对list重置，重新加值
							HealthListUtils.List(healthStr);
							healthlist = JSONObject.fromObject(healthStr);
						}
				}
			}else{			//接口访问失败
							//从list中取值展示（拼出一个完整的json格式）
				String content = JSONArray.fromObject(HealthListUtils.healthlist).toString();
				String end = "}";
				String healthList = HealthListUtils.healthHead +content+end;
							//转json对象
				healthlist = JSONObject.fromObject(healthList);
				
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			JSONObject result = new JSONObject();
			if(!healthlist.isEmpty()){
				result = healthlist;
			}
			return result;
		}
	}
	
	/***
	 * 	根据ID查询新闻的详情
	 * 
	 * 方法说明：先用   if(NewsListUtils.selectNr(id)==null)判断list中是否有页面传过来id对应的nr内容
     * 1 的确没有值得情况下，连接查询详情的接口，访问到数据拆分出nr字段内容，放到content，并把content存入list中对应的id下
     * 2 有值的情况下，就从list中取出nr字段的内容
     * 在转义nr字段中的特殊字符，拼接出一个标准的json格式的返回数据
     * 如还是没有数据的话，就是项目被重启数据被重置，需要在接口正常访问时再访问一遍就会自动存下，点击查询详情是根据id存放nr内容
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getNewsDetail")
	public @ResponseBody JSONObject getHospitalNews(@RequestBody RequestEntity requestBody) throws IOException{
		JSONObject newdetail = new JSONObject();
		String url;
		String id;
		String newStr = null;
	    String content;
	    try{
	    	//获取页面传过来的id
	    	 id = requestBody.getContent().get("id").toString();
 			//判断这个id下面的nr字段是否有值
			 if(NewsListUtils.selectNr(id)==null){
			 			//接口地址
				 	url = "http://www.yzszyy.com/newslist_weixinview.asp?id="+id;	
				 			//访问接口
					String rusultJson = HttpUtils.sendGet(url);
							//解决乱码
					newStr = new String(rusultJson.getBytes("ISO-8859-1"),"GBK");
							//调用方法拼出两个全局变量的字段：开头 和 结尾 {[{”nr“：" + 内容 + "}]}
					NewsListUtils.String(newStr);
							//截取出nr字段的内容
					content= newStr.substring(newStr.indexOf("nr")+5,newStr.length()-4); 
							//把content添加到对应的id下的nr字段中
					NewsListUtils.AddNr(id, content);
			 }else{		//如果对应的id下的nr字段有值的话
			 			//直接取出nr内容
			 	content = NewsListUtils.selectNr(id);
			 }
						//转义特殊字符
			content = StringUtils.replace(content, "" , "&apos;" );
			content = StringUtils.replace(content, "\"", "&quot;");
			content = StringUtils.replace(content, "\t", "&nbsp;&nbsp;");// 替换跳格
			content = StringUtils.replace(content, "<", "&lt;");
			content = StringUtils.replace(content, ">", "&gt;");
			content = StringUtils.replace(content, ":", "&#58;");
						//拼接字符串
			String Str = NewsListUtils.newStart3 + content + NewsListUtils.newEnd;
			if(Str.length()>0){
						//在连接超时的情况下减少控制台的报错(会产生转json对象的错误)
						//字符串转json对象
				newdetail = JSONObject.fromObject(Str);
			}
	    }catch (Exception e) {
			e.printStackTrace();
		}finally{
			JSONObject result = new JSONObject();
			if(!newdetail.isEmpty()){
				result = newdetail;
			}
			return result;
		}
	   
	  }
	
	/***
	 * 根据ID查询养身的详情
	 * 
	 * 方法说明：先用   if(NewsListUtils.selectNr(id)==null)判断list中是否有页面传过来id对应的nr内容
     * 1 的确没有值得情况下，连接查询详情的接口，访问到数据拆分出nr字段内容，放到content，并把content存入list中对应的id下
     * 2 有值的情况下，就从list中取出nr字段的内容
     * 在转义nr字段中的特殊字符，拼接出一个标准的json格式的返回数据
     * 如还是没有数据的话，就是项目被重启数据被重置，需要在接口正常访问时再访问一遍就会自动存下，点击查询详情是根据id存放nr内容
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getHealthDetail")
	public @ResponseBody JSONObject getHospital(@RequestBody RequestEntity requestBody) throws IOException{
		JSONObject healthdetail = new JSONObject();
		String url;
		String id;
		String healthStr = null;
	    String content;
	    try{
	    			//获取页面传过来的id
		    id = requestBody.getContent().get("id").toString();
		    			//判断这个id下面的nr字段是否有值
		    if(HealthListUtils.selectNr(id)==null){
		    			//接口地址
			 	url = "http://www.yzszyy.com/newslist_weixinview.asp?id="+id;	
			 			//访问接口
				String rusultJson = HttpUtils.sendGet(url);
						//解决乱码
				healthStr = new String(rusultJson.getBytes("ISO-8859-1"),"GBK");
						//调用方法拼出两个全局变量的字段：开头 和 结尾 {[{”nr“：" + 内容 + "}]}
				HealthListUtils.String(healthStr);
						//截取出nr字段的内容
				content= healthStr.substring(healthStr.indexOf("nr")+5,healthStr.length()-4); 
						//把content添加到对应的id下的nr字段中
				HealthListUtils.AddNr(id, content);
		    }else{		//如果对应的id下的nr字段有值的话
		    			//直接取出nr内容
		    	content = HealthListUtils.selectNr(id);
		    }
						//转义特殊字符
			content = StringUtils.replace(content, "" , "&apos;" );
			content = StringUtils.replace(content, "\"", "&quot;");
			content = StringUtils.replace(content, "\t", "&nbsp;&nbsp;");// 替换跳格
			content = StringUtils.replace(content, "<", "&lt;");
			content = StringUtils.replace(content, ">", "&gt;");
			content = StringUtils.replace(content, ":", "&#58;");
						//拼接字符串
			String Str = HealthListUtils.healthStart3 + content + HealthListUtils.healthEnd;
			if(Str.length()>0){
						//在连接超时的情况下减少控制台的报错(会产生转json对象的错误)
						//字符串转json对象
				healthdetail = JSONObject.fromObject(Str);
				return healthdetail;
			}
	    }catch (Exception e) {
			e.printStackTrace();
		}finally{
			JSONObject result = new JSONObject();
			if(!healthdetail.isEmpty()){
				result = healthdetail;
			}
			return result;
		}
	}
	    
}

