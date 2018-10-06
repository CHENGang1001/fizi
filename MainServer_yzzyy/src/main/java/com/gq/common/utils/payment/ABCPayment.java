package com.gq.common.utils.payment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.abc.pay.client.Constants;
import com.abc.pay.client.JSON;
import com.abc.pay.client.ebus.PaymentRequest;
import com.gq.common.exception.ServiceException;
import com.gq.common.utils.StringUtils;
import com.gq.config.ReturnCode;

public class ABCPayment {

	/**
	 * 交易类型，Default：0。0：直接支付、1：预授权支付、2：分期支付
	 */
	public static final String[] ABC_PAY_TYPE_ID = { "ImmediatePay", "PreAuthPay", "DividedPay" };
	/**
	 * 交易币种， Default：0。0：人民币
	 */
	public static final String[] ABC_CURRENCY_CODE = { "156" };
	/**
	 * 分期标识， Default：0。0：不分期、1：分期
	 */
	public static final String[] ABC_INSTALLMENT_MARK = { "0", "1" };
	/**
	 * 商品种类，Default：0。0：支付账户充值 、1：虚拟类、2：传统类、3：实名类、4：本行转账、5：他行转账 、6：水费
	 * 、7：电费、8：煤气费、9：有线电视费、10：通讯费、11：物业费、12：保险费、13：行政费用、14：税费
	 * 、15：学费、16：其他、17：基金、18：理财产品、19：其他
	 */
	public static final String[] ABC_COMMODITY_TYPE = { "0101", "0201", "0202", "0203", "0301", "0302", "0401", "0402", "0403",
			"0404", "0405", "0406", "0407", "0408", "0409", "0410", "0499", "0501", "0502", "0599" };
	/**
	 * 支付账户类型， Default：5。0：农行卡支付 、1：国际卡支付 、2：农行贷记卡支付 、3:基于第三方的跨行支付 、4：支付方式合并
	 * 、5：银联跨行支付、6：对公户
	 */
	public static final String[] ABC_PAYMENT_TYPE = { "1", "2", "3", "5", "A", "6", "7" };
	/**
	 * 交易渠道，Default：1。0：internet网络接入 、1：手机网络接入 、2：数字电视网络接入 、3：智能客户端
	 */
	public static final String[] ABC_PAYMENT_LINK_TYPE = { "1", "2", "3", "4" };
	/**
	 * 银联跨行移动支付接入方式，Default：2。0：页面接入、 1：客户端接入
	 * 、如果选择的支付帐户类型为(银联跨行支付)交易渠道为2(手机网络接入)
	 * 
	 */
	public static final String[] ABC_UNION_PAY_LINK_TYPE = { "0", "1", "2" };
	/**
	 * 通知方式，Default：0。0：URL页面通知、 1：服务器通知
	 */
	public static final String[] ABC_NOTIFY_TYPE = { "0", "1" };
	/**
	 * 交易是否分账， Default：0。0：否、1：是
	 */
	public static final String[] ABC_IS_BREAK_ACCOUNT = { "0", "1" };

	@SuppressWarnings("unchecked")
	public String pay(Map<String, Object> param, List<Map<String, String>> items) throws ServiceException {
		// 参数验证
		this.paramVerify(param);
		// 1、生成订单对象
		PaymentRequest pay = new PaymentRequest();
		pay.dicOrder.put("PayTypeID", param.get("PayTypeID")); // 设定交易类型
		pay.dicOrder.put("OrderDate", param.get("OrderDate")); // 设定订单日期 （必要信息 -
																// YYYY/MM/DD）
		pay.dicOrder.put("OrderTime", param.get("OrderTime")); // 设定订单时间 （必要信息 -
																// HH:MM:SS）
		if (null == param.get("orderTimeoutDate")) {
			pay.dicOrder.put("orderTimeoutDate", ""); // 设定订单有效期
		} else {
			pay.dicOrder.put("orderTimeoutDate", param.get("orderTimeoutDate"));
		}
		pay.dicOrder.put("OrderNo", param.get("OrderNo")); // 设定订单编号 （必要信息）
		pay.dicOrder.put("CurrencyCode", param.get("CurrencyCode")); // 设定交易币种
		pay.dicOrder.put("OrderAmount", param.get("OrderAmount")); // 设定交易金额
		if (null == param.get("Fee")) {
			pay.dicOrder.put("Fee", ""); // 设定手续费金额
		} else {
			pay.dicOrder.put("Fee", param.get("Fee"));
		}
		if (null == param.get("OrderDesc")) {
			pay.dicOrder.put("OrderDesc", ""); // 设定订单说明
		} else {
			pay.dicOrder.put("OrderDesc", param.get("OrderDesc"));
		}
		if (null == param.get("OrderURL")) {
			pay.dicOrder.put("OrderURL", ""); // 设定订单地址
		} else {
			pay.dicOrder.put("OrderURL", param.get("OrderURL"));
		}
		if (null == param.get("ReceiverAddress")) {
			pay.dicOrder.put("ReceiverAddress", ""); // 收货地址
		} else {
			pay.dicOrder.put("ReceiverAddress", param.get("ReceiverAddress"));
		}
		pay.dicOrder.put("InstallmentMark", param.get("InstallmentMark")); // 分期标识
		if (param.get("InstallmentMark").toString() == "1" && param.get("PayTypeID").toString() == "DividedPay") {
			pay.dicOrder.put("InstallmentCode", param); // 设定分期代码
			pay.dicOrder.put("InstallmentNum", param); // 设定分期期数
		}
		pay.dicOrder.put("CommodityType", param.get("CommodityType")); // 设置商品种类
		if (null == param.get("BuyIP")) {
			pay.dicOrder.put("BuyIP", ""); // IP
		} else {
			pay.dicOrder.put("BuyIP", param.get("BuyIP"));
		}
		if (null == param.get("ExpiredDate")) {
			pay.dicOrder.put("ExpiredDate", ""); // 设定订单保存时间
		} else {
			pay.dicOrder.put("ExpiredDate", param.get("ExpiredDate"));
		}

		// 2、订单明细
		if (null != items && items.size() > 0) {
			LinkedHashMap<String, String> orderDetail = null;
			for (int i = 0; i < items.size(); i++) {
				orderDetail = new LinkedHashMap<String, String>();
				orderDetail.put("ProductID", items.get(i).get("itemNo"));// 商品代码，预留字段
				orderDetail.put("ProductName", items.get(i).get("itemName"));// 商品名称
				String price = items.get(i).get("price");
				String count = items.get(i).get("count");
				if (!StringUtils.isNullOrEmpty(price) && !StringUtils.isNullOrEmpty(count)) {
					orderDetail.put("UnitPrice", String.valueOf((Double.parseDouble(price) * Double.parseDouble(count))));// 商品总价
				}
				orderDetail.put("Qty", count);// 商品数量
				pay.orderitems.put(i + 1, orderDetail);

				// orderDetail.put("SubMerName", "测试二级商户1"); // 设定二级商户名称
				// orderDetail.put("SubMerId", "12345"); // 设定二级商户代码
				// orderDetail.put("SubMerMCC", "0000"); // 设定二级商户MCC码
				// orderDetail.put("SubMerchantRemarks", "测试"); // 二级商户备注项
				// orderDetail.put("ProductID", "IP000001");// 商品代码，预留字段
				// orderDetail.put("ProductName", "中国联通IP卡");// 商品名称
				// orderDetail.put("UnitPrice", "1.00");// 商品总价
				// orderDetail.put("Qty", "2");// 商品数量
				// orderDetail.put("ProductRemarks", "测试商品"); // 商品备注项
				// orderDetail.put("ProductType", "充值类");// 商品类型
				// orderDetail.put("ProductDiscount", "0.9");// 商品折扣
				// orderDetail.put("ProductExpiredDate", "10");// 商品有效期

			}
		}

		// 3、生成支付请求对象
		String paymentType = param.get("PaymentType").toString();
		pay.dicRequest.put("PaymentType", paymentType); // 设定支付类型，1：农行卡支付
														// 2：国际卡支付 3：农行贷记卡支付
														// 5:基于第三方的跨行支付 A:支付方式合并
														// 6：银联跨行支付，7:对公户
		String paymentLinkType = param.get("PaymentLinkType").toString();
		pay.dicRequest.put("PaymentLinkType", paymentLinkType); // 设定支付接入方式
		if (paymentType.equals(Constants.PAY_TYPE_UCBP) && paymentLinkType.equals(Constants.PAY_LINK_TYPE_MOBILE)) {
			pay.dicRequest.put("UnionPayLinkType", param.get("UnionPayLinkType")); // 当支付类型为6，支付接入方式为2的条件满足时，需要设置银联跨行移动支付接入方式
		}
		if (null == param.get("ReceiveAccount")) {
			pay.dicOrder.put("ReceiveAccount", ""); // 设定收款方账号
		} else {
			pay.dicOrder.put("ReceiveAccount", param.get("ReceiveAccount"));
		}
		if (null == param.get("ReceiveAccName")) {
			pay.dicOrder.put("ReceiveAccName", ""); // 设定收款方户名
		} else {
			pay.dicOrder.put("ReceiveAccName", param.get("ReceiveAccName"));
		}
		pay.dicRequest.put("NotifyType", param.get("NotifyType")); // 设定通知方式
		pay.dicRequest.put("ResultNotifyURL", param.get("ResultNotifyURL")); // 设定通知URL地址
		if (null == param.get("MerchantRemarks")) {
			pay.dicOrder.put("MerchantRemarks", ""); // 设定附言
		} else {
			pay.dicOrder.put("MerchantRemarks", param.get("MerchantRemarks"));
		}
		pay.dicRequest.put("IsBreakAccount", param.get("IsBreakAccount")); // 设定交易是否分账
		if (null == param.get("SplitAccTemplate")) {
			pay.dicOrder.put("SplitAccTemplate", ""); // 分账模版编号
		} else {
			pay.dicOrder.put("SplitAccTemplate", param.get("SplitAccTemplate"));
		}
		// 发送支付请求
		JSON json = pay.postRequest();
		// JSON json = tPaymentRequest.extendPostRequest(1);

		String returnCode = json.GetKeyValue("ReturnCode");
		String errorMessage = json.GetKeyValue("ErrorMessage");
		String paymentURL = json.GetKeyValue("PaymentURL");

		System.out.println("**ABC Payment**** ReturnCode   = [" + returnCode + "]<br/>");
		System.out.println("**ABC Payment**** ErrorMessage = [" + errorMessage + "]<br/>");
		if (returnCode.equals("0000")) {
			// 成功
			System.out.println("**ABC Payment**** PaymentURL   = [" + paymentURL + "]<br/>");
			return paymentURL;
		} else {
			// 失败
			System.out.println("**ABC Payment**** ABC pay fail");
			return null;
		}
	}

	private void paramVerify(Map<String, Object> param) throws ServiceException {
		if (null == param.get("PayTypeID")) {
			param.put("PayTypeID", ABCPayment.ABC_PAY_TYPE_ID[0]);
		}
		if (null == param.get("OrderDate")) {
			throw new ServiceException(ReturnCode.ORDER_INVALID);
		}
		if (null == param.get("OrderTime")) {
			throw new ServiceException(ReturnCode.ORDER_INVALID);
		}
		if (null == param.get("orderTimeoutDate")) {
			// param.put("orderTimeoutDate", "");
		}
		if (null == param.get("OrderNo")) {
			throw new ServiceException(ReturnCode.ORDER_INVALID);
		}
		if (null == param.get("CurrencyCode")) {
			param.put("CurrencyCode", ABCPayment.ABC_CURRENCY_CODE[0]);
		}
		if (null == param.get("OrderAmount")) {
			throw new ServiceException(ReturnCode.ORDER_INVALID);
		}
		if (null == param.get("Fee")) {
			// param.put("Fee", "");
		}
		if (null == param.get("OrderDesc")) {
			// param.put("OrderDesc", "");
		}
		if (null == param.get("OrderURL")) {
			// param.put("OrderURL", "");
		}
		if (null == param.get("ReceiverAddress")) {
			// param.put("ReceiverAddress", "");
		}
		if (null == param.get("InstallmentMark")) {
			param.put("InstallmentMark", ABCPayment.ABC_INSTALLMENT_MARK[0]);
		} else if (param.get("InstallmentMark").toString().equals(ABCPayment.ABC_INSTALLMENT_MARK[1])
				&& param.get("PayTypeID").toString().equals(ABCPayment.ABC_PAY_TYPE_ID[2])) {
			if (null == param.get("InstallmentCode")) {
				throw new ServiceException(ReturnCode.ORDER_INVALID);
			}
			if (null == param.get("InstallmentNum")) {
				throw new ServiceException(ReturnCode.ORDER_INVALID);
			}
		}
		if (null == param.get("CommodityType")) {
			param.put("CommodityType", ABCPayment.ABC_COMMODITY_TYPE[0]);
		}
		if (null == param.get("BuyIP")) {
			// param.put("BuyIP", "");
		}
		if (null == param.get("ExpiredDate")) {
			// param.put("ExpiredDate", "");
		}
		if (null == param.get("PaymentType")) {
			param.put("PaymentType", ABCPayment.ABC_PAYMENT_TYPE[5]);
		}
		if (null == param.get("PaymentLinkType")) {
			param.put("PaymentLinkType", ABCPayment.ABC_PAYMENT_LINK_TYPE[0]);
		}
		if (param.get("PaymentType").toString().equals(Constants.PAY_TYPE_UCBP)
				&& param.get("PaymentLinkType").toString().equals(Constants.PAY_LINK_TYPE_MOBILE)) {
			if (null == param.get("UnionPayLinkType")) {
				param.put("UnionPayLinkType", ABCPayment.ABC_UNION_PAY_LINK_TYPE[2]);
			}
		}
		if (null == param.get("ReceiveAccount")) {
			// param.put("ReceiveAccount", "");
		}
		if (null == param.get("ReceiveAccName")) {
			// param.put("ReceiveAccName", "");
		}
		if (null == param.get("NotifyType")) {
			param.put("NotifyType", ABCPayment.ABC_NOTIFY_TYPE[0]);
		}
		if (null == param.get("ResultNotifyURL")) {
			throw new ServiceException(ReturnCode.ORDER_INVALID);
		}
		if (null == param.get("MerchantRemarks")) {
			// param.put("MerchantRemarks", "");
		}
		if (null == param.get("IsBreakAccount")) {
			param.put("IsBreakAccount", ABCPayment.ABC_IS_BREAK_ACCOUNT[0]);
		}
		if (null == param.get("SplitAccTemplate")) {
			// param.put("SplitAccTemplate", "");
		}

	}
}
