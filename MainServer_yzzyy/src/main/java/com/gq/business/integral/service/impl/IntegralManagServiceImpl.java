package com.gq.business.integral.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.gq.business.integral.bean.GoodsBean;
import com.gq.business.integral.bean.ImageBean;
import com.gq.business.integral.bean.OrderGoodsBean;
import com.gq.business.integral.mappers.IntegrationManageMapper;
import com.gq.business.integral.service.IIntegralManagService;
import com.gq.common.exception.ServiceException;
import com.gq.config.ReturnCode;

@Service
public class IntegralManagServiceImpl implements IIntegralManagService{
	@Autowired
	private IntegrationManageMapper manageMapper;

	/**
	 * @author liangyuli
	 * @Description 商品兑换管理员登录
	 * @param param
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> exchangeLogin(Map<String, Object> param) throws ServiceException  {
		Map<String, Object> userInfo =new HashMap<String, Object>();
		userInfo = manageMapper.checkAccount(param);
		
		if (null == userInfo) {
				throw new ServiceException(ReturnCode.USER_PWD_ERROR);
		}
		return userInfo;
	}

	/**
	 * @author liangyuli
	 * @Description 发布商品
	 * @param goods
	 * @param image
	 * @throws ServiceException
	 */
	@Override
	public int publishGoods(GoodsBean goods,String image) throws ServiceException{
		int is = 1;
		try {
			if(null != goods){
				String name = goods.getGoods_name();
				int isRepeat = manageMapper.queryNameIsRepeat(name);
				if(isRepeat>=1){
					is=2;
				}else{
					int goodsId;
					if(manageMapper.queryIsHaveGoods()==0){//判断商品表里是否有数据存在
						goodsId = 1;
					}else{
						goodsId = manageMapper.selctMaxGoodsId()+1;
					}
					//#{project}||#{classify}||LPAD (SEQ_T_INTEGRAL_GOODS_INFO_ID.NEXTVAL, 5 , '0')
					System.out.println("max id is:"+ goodsId);
					String project = goods.getProject();
					String classify = goods.getClassify();
					//拼接商品代码
					String goods_code = String.valueOf(goodsId);
					int strLen = goods_code.length();
				    StringBuffer sb = null;
				    while (strLen < 5) {
				           sb = new StringBuffer();
				           sb.append("0").append(goods_code);// 左补0
				           goods_code = sb.toString();
				           strLen = goods_code.length();
				     }
					goods.setGoods_code(project + classify + goods_code);
					manageMapper.insertGoods(goods);
					
					//插入图片
					if(image!=null){
						ImageBean imageBean = new ImageBean();
						System.out.println("insert into succeed!");
						String goodsCode = manageMapper.selectMaxGoodsCode();
						if(image.contains(";")){
							String[] list = image.split(";");
							for(String temp :list){
								imageBean.setSourceName(goods.getGoods_code()==null?goodsCode:goods.getGoods_code());
								imageBean.setSourcePath(temp);
								manageMapper.insertGoodsImage(imageBean);
							}
						}else{
							imageBean.setSourceName(goods.getGoods_code()==null?goodsCode:goods.getGoods_code());
							imageBean.setSourcePath(image);
							manageMapper.insertGoodsImage(imageBean);
						}
					}
				}				
			}
			return is;
		}catch (DuplicateKeyException e) {
			// need record log
			throw new ServiceException(ReturnCode.SYSTEM_ERROR);
		}
	}
	
	/**
	 * @author liangyuli
	 * @Description 兑换商品
	 * @param orderNo
	 * @throws ServiceException
	 */
	@Override
	public String exchangeGoods(String orderNo) throws ServiceException{
		String status = null;
		if(null != orderNo){
			try {
				status  = manageMapper.queryOrderStatus(orderNo);
				if(status.equals("1")){//未兑换
					manageMapper.updateOrder(orderNo);
				}else{
					return status;
				}
			} catch (DuplicateKeyException e) {
				// need record log
				throw new ServiceException(ReturnCode.SYSTEM_ERROR);
			}
		}
		return status;
	}

	/**
	 * @author liangyuli
	 * @Description 更新商品状态
	 * @param goods
	 * @throws ServiceException
	 */
	@Override
	public void uploadGoodsStatus(GoodsBean goods) throws ServiceException {
		if(null != goods){
			try {
				GoodsBean tempGoods = new GoodsBean();
				String goodsId;
				goodsId = goods.getGoods_code();
				if(goodsId.contains(";")){
					String[] list = goodsId.split(";");
					for(String temp :list){
						tempGoods.setGoods_code(temp);
						tempGoods.setGoods_status(goods.getGoods_status());
						manageMapper.uploadGoodsStatus(tempGoods);
					}
				}else{
					manageMapper.uploadGoodsStatus(goods);
				}
				
				
			} catch (DuplicateKeyException e) {
				throw new ServiceException(ReturnCode.SYSTEM_ERROR);
			}
		}
	}

	@Override
	public List<Map<String, Object>> integralGoodsList(Map<String, Object> param) {
		if(!param.containsKey("type")) {
			return null;
		}
		if(!param.containsKey("pages")) {
			return null;
		}
		if(!param.containsKey("num")) {
			return null;
		}
		if(!param.containsKey("name")) {
			return null;
		}
		String name = param.get("name").toString();
		List<Map<String, Object>> list = null;
		Map<String, Object> allPages = null;
		int pages = Integer.parseInt(param.get("pages").toString());
		int num = Integer.parseInt(param.get("num").toString());
		param.put("start", (pages - 1) * num );
		param.put("end", num * pages);
		if(TextUtils.isEmpty(name)) {
			// 获取指定类型的商品列表
			list = manageMapper.integralGoodsListAll(param);
			// 获取商品总页数
			allPages = manageMapper.integralGoodsListQtyAll(param);
		} else {
			// 获取指定类型和搜索条件的商品列表
			list = manageMapper.integralGoodsList(param);
			// 获取指定类型和搜索条件的商品总页数
			allPages = manageMapper.integralGoodsListQty(param);
		}
		list.add(0, allPages);
		return list;
	}

	@Override
	public OrderGoodsBean getOrder(Map<String, Object> param) throws ParseException {
		if(!param.containsKey("order_no")) {
			return null;
		}
		OrderGoodsBean order = new OrderGoodsBean();
		String orderNo = param.get("order_no").toString();
		order = manageMapper.getOrder(orderNo);
		String orderTime = order.getOrder_time();
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		Date date = sdf.parse(orderTime); 
		order.setOrder_time((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date));
		return order;
	}

	/**
	 * 编辑商品库存
	 */
	@Override
	public int editGoods(GoodsBean goods) {
		int res = 0;
		if(goods.getCurrent_amount() != "" && goods.getCurrent_amount() != null){
			 res = manageMapper.editGoods(goods);
		}
		return res;
	}

	@Override
	public String getGoodsPic(String code) {
		
		String url = "";
		if(code != null && code != ""){
			url = manageMapper.getGoodsPic(code);
			if(TextUtils.isEmpty(url)){
				url="";
			}else{
			url = "http://resource.jssecco.com" + url;
			}
		}		
		return url;
	}

	/**
	 * 编辑商品
	 */
	@Override
	public int editGoodsAll(GoodsBean goods, String imageSourse) {
		int result = 0;
		try {
			if(goods.getGoods_code() != null && goods.getGoods_code() != ""){
				int count = manageMapper.findByCode(goods);
				if(count >0){
					int i = manageMapper.updateGoods(goods);
					if(i == 1){
						//插入图片
						if(imageSourse != null){
							ImageBean imageBean = new ImageBean();
							String goodsCode = goods.getGoods_code();
							if(goodsCode != null){
								if(imageSourse.contains(";")){
									String[] list = imageSourse.split(";");
									for(String temp :list){
										imageBean.setSourceName(goodsCode);
										imageBean.setSourcePath(temp);
										int re = manageMapper.updateGoodsImage(imageBean);
										if(re > 0){
											result = 1;
										}
									}
								}else{
									imageBean.setSourceName(goodsCode);
									imageBean.setSourcePath(imageSourse);
									int re = manageMapper.updateGoodsImage(imageBean);
									if(re > 0){
										result = 1;
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public int deleteGoods(String goodsCode) {
		int is = 0;
		is = manageMapper.deleteGoods(goodsCode);
		return is;
	}

	@Override
	public List<Map<String, Object>> findOrderByCode(String goodsCode) {
		List<Map<String, Object>> list = null;
		list = manageMapper.findOrderByOrder(goodsCode);
		return list;
	}
}
