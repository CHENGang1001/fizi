package com.gq.business.integral.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gq.business.integral.service.IIntegrationRuleService;
import com.gq.business.integral.service.IntegralService;
import com.gq.common.log.Level;
import com.gq.common.log.LogManager;
import com.gq.common.request.RequestEntity;
import com.gq.common.response.ResponseEntity;
import com.gq.common.response.ResponseHeader;
import com.gq.config.ReturnCode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;

@Controller
public class IntegralMallController {
	
	@Autowired
	private IntegralService integralService;

	/**
	 * 日志
	 */
	private Logger logger = LogManager.getDebugLog();


	@Autowired
	private IIntegrationRuleService integration;
	/**
	 * 积分商城获取用户积分信息
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/getIntegralerInfo")
	@ResponseBody
	public ResponseEntity getIntegralerInfo(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		Map<String, Object> returnParam = new HashMap<String, Object>();
		try {
			if (null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				returnParam = integralService.getIntegralerInfo(param);
				if(null == returnParam || returnParam.isEmpty()) {
					returnCode = ReturnCode.NO_DATA;
				} else {
					returnCode = ReturnCode.OK;
				}
			}
		} catch (Exception e) {
			logger.error("获取周边商户分类列表", e);
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(returnParam);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getIntegralerInfo", requestBody.toString(), responseEntity.toString(),
				System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 积分商城获取兑换商品列表
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/getOnSaleGoodsList")
	@ResponseBody
	public ResponseEntity getGoodsList(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		List<Map<String, Object>> goodsList = new ArrayList<Map<String,Object>>();
		try {
			// 获得在售商品列表
			goodsList = integralService.getOnSaleGoodsList();
			if(null == goodsList){
				returnCode = ReturnCode.NO_DATA;
			}
			else {
				if(goodsList.size() > 0) {
					returnCode = ReturnCode.OK;
				} else if(goodsList.size() == 0) {
					returnCode = ReturnCode.NO_DATA;
				}
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(goodsList);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "getOnSaleGoodsList", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 积分商城兑换商品
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/exchangeUserGoods")
	@ResponseBody
	public ResponseEntity exchangeUserGoods(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			if (null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				returnMap = integralService.exchangeUserGoods(param);
				if(returnMap.containsKey("returnCode")) {
					String key = returnMap.get("returnCode").toString();
					if("0".equals(key)) {
						returnCode = ReturnCode.OK;
					}else if ("-2".equals(key)) {
						returnCode = ReturnCode.INTEGRAL_NOT_ENOUGH;
					} else if ("-3".equals(key)) {
						returnCode = ReturnCode.TODAY_CANNOT_EXCHANGE;
					} else if ("-4".equals(key)) {
						returnCode = ReturnCode.REST_GOODS_NOT_ENOUGH;
					} else {
						returnCode = ReturnCode.SYSTEM_ERROR;
					}
					returnMap.remove("returnCode");
				}
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(returnMap);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "exchangeUserGoods", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 积分商城指定用户积分流水记录
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/integralRecord")
	@ResponseBody
	public ResponseEntity integralRecord(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		List<Map<String, Object>> recordList = new ArrayList<Map<String,Object>>();
		try {
			if(null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				recordList = integralService.getIntegralRecordList(param);
				if(recordList.size() > 0) {
					returnCode = ReturnCode.OK;
				} else if(recordList.size() == 0) {
					returnCode = ReturnCode.NO_DATA;
				}
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(recordList);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "integralRecord", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 积分商城积分榜（日、周、月）
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/integralRank")
	@ResponseBody
	public ResponseEntity integralRank(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		List<Map<String, Object>> rankList = new ArrayList<Map<String,Object>>();
		try {
			if(null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				rankList = integralService.getRankingList(param);
			}
			if(rankList.size() > 0) {
				returnCode = ReturnCode.OK;
			} else {
				returnCode = ReturnCode.NO_DATA;
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(rankList);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "integralRank", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 积分商城兑换记录列表
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/integralOrderList")
	@ResponseBody
	public ResponseEntity integralOrderList(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		List<Map<String, Object>> orderList = new ArrayList<Map<String,Object>>();
		try {
			if(null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				orderList = integralService.getIntegralOrderList(param);
			}
			if(orderList.size() > 0) {
				returnCode = ReturnCode.OK;
			} else {
				returnCode = ReturnCode.NO_DATA;
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(orderList);
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "integralOrderList", requestBody.toString(), responseEntity.toString(),
				System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	/**
	 * 将已兑换的订单状态改为已删除
	 * 
	 * @param requestBody
	 * @return
	 */
	@RequestMapping(value = "/deleteIntegralOrder")
	@ResponseBody
	public ResponseEntity deleteIntegralOrder(@RequestBody RequestEntity requestBody) {
		ReturnCode returnCode = ReturnCode.SYSTEM_ERROR;
		long startTime = System.currentTimeMillis();
		ResponseEntity responseEntity = null;
		try {
			if (null != requestBody) {
				Map<String, Object> param = requestBody.getContent();
				returnCode = integralService.deleteIntegralOrder(param);
			}
		} catch (Exception e) {
			returnCode = ReturnCode.SYSTEM_ERROR;
			returnCode.setMsg(e.getMessage());
		} finally {
			// 构造返回应答对象
			responseEntity = new ResponseEntity();
			ResponseHeader header = new ResponseHeader();
			header.setResultCode(returnCode.getCode());
			header.setResultMsg(returnCode.getMsg());
			responseEntity.setHeader(header);
			responseEntity.setContent(new HashMap<String, Object>());
		}
		// 接口日志
		LogManager.interfaceLog(Level.INFO, "APK", "MHSERVER", "deleteIntegralOrder", requestBody.toString(),
				responseEntity.toString(), System.currentTimeMillis() - startTime);
		return responseEntity;
	}

	public static String UPLOAD_DIR = "/uploads";

	public static String FILE_DIR = "/WEB-INF/uploads";

	public static String RESOURCE_URL = "http://resource.jssecco.com/upload";

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public void upload(HttpServletRequest request, HttpServletResponse response, PrintWriter writer) {
		// System.out.println("request in");
		String filename = null;
		int chunk = 0; // 当前正在处理的文件分块序号
		int chunks = 0; // 分块上传总数
		File file = null;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		// 判断当前表单是否为"multipart/form-data"
		if (isMultipart) {
			ServletFileUpload upload = new ServletFileUpload();
			String uploadDir = request.getServletContext().getRealPath(FILE_DIR);
			try {
				FileItemIterator iter = upload.getItemIterator(request);
				while (iter.hasNext()) {
					FileItemStream item = iter.next();
					String name = item.getFieldName();
					System.out.println("field name : " + name);
					InputStream input = item.openStream();
					if ("chunk".equals(name)) {
						chunk = Integer.valueOf(Streams.asString(input));
						// System.out.println("chunk:" + chunk);
					}
					if ("chunks".equals(name)) {
						chunks = Integer.valueOf(Streams.asString(input));
						// System.out.println("chunks:" + chunks);
					}
					if ("name".equals(name)) {
						filename = Streams.asString(input); // 这个文件名比较可靠
						// System.out.println(String.valueOf(filename));
					}
					if ("file".equals(name) && !item.isFormField()) {
						// 文件名
						// filename = item.getName(); //这里可能获取到"blob"
						// 保存文件目录绝对路径
						File dir = new File(uploadDir);
						if (!dir.isDirectory() || !dir.exists()) {
							dir.mkdir();
						}
						// 保存文件绝对路径
						String path = uploadDir + File.separator + filename;
						if (chunks > 0) {
							path += "." + chunk + ".part";
						}
						file = new File(path);
						// if (file.exists() && chunk == 0) {
						// file.delete();
						// }
						// 上传文件
						byte[] buffer = new byte[4096];
						FileOutputStream fos = new FileOutputStream(file, true);
						int len = 0;
						while ((len = input.read(buffer)) != -1) {
							fos.write(buffer, 0, len);
						}
						fos.close();
					}
					input.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				writer.write(e.getMessage());
			}
			// 如果是分片上传的文件，需要合并
			if (chunks > 0) {
				// 检查是否全部分片都已完成
				boolean uploadDone = true;
				for (int i = 0; i < chunks; i++) {
					File partFile = new File(uploadDir, filename + "." + i + ".part");
					if (!partFile.exists()) {
						uploadDone = false;
						break;
					}
				}
				if (uploadDone) {
					// 转发文件
					for (int i = 0; i < chunks; i++) {
						File partFile = new File(uploadDir, filename + "." + i + ".part");
						MultipartEntityBuilder meb = MultipartEntityBuilder.create();
						meb.setBoundary("----------298rfhda9fvh09ahw349prhq9whgf");
						meb.setCharset(Charsets.UTF_8);
						meb.setContentType(ContentType.MULTIPART_FORM_DATA);
						meb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

						FormBodyPartBuilder pb = null;
						// chunks
						pb = FormBodyPartBuilder.create();
						pb.setName("chunks");
						pb.setBody(new StringBody(String.valueOf(chunks), ContentType.TEXT_PLAIN));
						meb.addPart(pb.build());
						// chunk
						pb = FormBodyPartBuilder.create();
						pb.setName("chunk");
						pb.setBody(new StringBody(String.valueOf(i), ContentType.TEXT_PLAIN));
						meb.addPart(pb.build());
						// name区
						pb = FormBodyPartBuilder.create();
						pb.setName("name");
						pb.setBody(new StringBody(filename, ContentType.TEXT_PLAIN));
						meb.addPart(pb.build());
						// file区
						pb = FormBodyPartBuilder.create();
						pb.setName("file");
						pb.setBody(new FileBody(partFile, ContentType.APPLICATION_OCTET_STREAM, partFile.getName()));
						meb.addPart(pb.build());
						// 发送http请求到资源服务器
						HttpPost hp = new HttpPost(RESOURCE_URL);
						hp.setEntity(meb.build());
						HttpClient client = HttpClientBuilder.create().build();
						try {
							HttpResponse hr = client.execute(hp);
							InputStream is = hr.getEntity().getContent();
							InputStreamReader isr = new InputStreamReader(is, CharEncoding.UTF_8);
							StringBuffer sb = new StringBuffer();
							int r = isr.read();
							while (r != -1) {
								sb.append((char) r);
								r = isr.read();
							}
							isr.close();
							is.close();
							System.out.println("发送消息收到的返回：" + sb.toString());
							writer.write(sb.toString());
							partFile.delete();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} else if (filename != null) {
				// 转发文件
				// File partFile = new File(uploadDir, filename + "." + i +
				// ".part");
				MultipartEntityBuilder meb = MultipartEntityBuilder.create();
				meb.setBoundary("----------298rfhda9fvh09ahw349prhq9whgf");
				meb.setCharset(Charsets.UTF_8);
				meb.setContentType(ContentType.MULTIPART_FORM_DATA);
				meb.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

				FormBodyPartBuilder pb = null;
				// name区
				pb = FormBodyPartBuilder.create();
				pb.setName("name");
				pb.setBody(new StringBody(filename, ContentType.TEXT_PLAIN));
				meb.addPart(pb.build());
				// file区
				pb = FormBodyPartBuilder.create();
				pb.setName("file");
				pb.setBody(new FileBody(file, ContentType.APPLICATION_OCTET_STREAM, file.getName()));
				meb.addPart(pb.build());
				// 发送http请求到资源服务器
				HttpPost hp = new HttpPost(RESOURCE_URL);
				hp.setEntity(meb.build());
				HttpClient client = HttpClientBuilder.create().build();
				try {
					HttpResponse hr = client.execute(hp);
					InputStream is = hr.getEntity().getContent();
					InputStreamReader isr = new InputStreamReader(is, CharEncoding.UTF_8);
					StringBuffer sb = new StringBuffer();
					int r = isr.read();
					while (r != -1) {
						sb.append((char) r);
						r = isr.read();
					}
					isr.close();
					is.close();
					System.out.println("发送消息收到的返回：" + sb.toString());
					writer.write(sb.toString());
					file.delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			// 普通form提交的文件
			// request.getContentType();
			// System.out.println(request.getContentLengthLong());
			writer.write("only accept multipart/form-data");
		}
	}
}
