package com.gq.config;
public enum ReturnCode {
	//(00000 - 00999) System or common code
	SERVICE_NOT_FOUND            ("0",     "找不到系统服务"),
	OK                           ("1",     "成功"),
	NO_DATA            			 ("2",     "没有数据"),
	SYSTEM_ERROR                 ("999",   "系统错误"),
	ILLEGAL_ACCESS               ("3",     "非法访问"),

	//(01000 - 01499) User code
	USER_USERNAME_EXISTS         ("1000",  "用户名已存在"),
	USER_USERNAME_INVALID        ("1001",  "用户名无效"),
	USER_PWD_INVALID             ("1002",  "密码无效"),
	USER_NEWPWD_INVALID          ("10021",  "新密码不能为空"),
	USER_USERID_INVALID          ("1003",  "用户ID无效"),
	USER_REALNAME_INVALID        ("1004",  "真实用户名无效"),
	USER_MSISDN_INVALID          ("1005",  "手机号码无效"),
	USER_VERIFYCODE_INVALID      ("1006",  "验证码无效,请重新获取"),
	USER_VERIFYCODE_OVERTIME     ("10062",  "验证码超时"),
	USER_ACCOUNTISCLOSE          ("10063",  "账号被禁用"),
	USER_ACCOUNTUSERTYPE         ("10064",  "请正确的区分用户角色"),
	USER_AUTHORIZEFAIL          	("10065",  "三方授权失败"),
	USER_AUTHORIZEFAIL_NAME      ("10066",  "三方授权失败_昵称信息"),
	USER_AUTHORIZEFAIL_HEEADPIC  ("10067",  "三方授权失败_头像信息"),
	USER_AUTHORIZEFAIL_NOUSER    ("10068",  "首次三方登录，需绑定手机号码"),
	USER_AUTHORIZEFAIL_EXIST     ("1101",  "该手机号码已经被绑定"),
	USER_LOGINTYPEFAIL           ("1102",  "登录类型传错了"),
	USER_MSISDN_isEmpty          ("1103",  "手机号码不能为空"),
	USER_MSISDN_NO        	     ("1104",  "该用户不存在，需要填写密码注册信息"),//短信验证
	USER_MSISDN_YES        	     ("1105",  "该用户已存在，可绑定该类型三方信息，无需填写密码"),//短信验证
	USER_MSISDN_EXIST        	 ("1106",  "该用户已存在，不可绑定该类型三方信息"),

	USER_VERIFYCODE_NULL         ("10061",  "请输入验证码"),
	USER_CLIENTTYPE_INVALID      ("1007",  "客户端类型无效"),
	USER_SESSIONID_INVALID       ("1008",  "会话无效"),
	USER_NEWPASSWORD_INVALID     ("1009",  "新密码无效"),
	USER_VERIFYTYPE_INVALID      ("1010",  "验证类型无效"),
	USER_PHONENUM_NOT_REGISTER   ("1011",  "手机号码尚未注册"),
	USER_PWD_ERROR               ("1012",  "用户名或密码错误"),
	USER_CHECK_VERIFYCODE_FAIL   ("1013",  "手机验证码校验失败"),
	USER_MSISDN_REGISTERED       ("1014",  "手机号码已经被注册"),
	USER_PWD_ERROR_CHANGE        ("1015",  "原密码错误"),
	USER_PASSWORDLEGHTERROR   	 ("1016",  "密码至少6位字符"),
	USER_LOGININVALID       	    ("10017",  "登录失效,请重新登录"),
	USER_VERIFYCODE_INVALIDAGAIN      ("10018",  "短信验证码已验证过，再次验证失效"),
	USER_ERIFYCODE_TIMEOVER      ("10019",  "短信验证码过期无效"),

	//(02000 - 02499) Patient code
	PATIENT_CARD_INVALID         ("2000",  "就诊人卡号无效"),
	PATIENT_NAME_INVALID         ("2001",  "就诊人姓名无效"),
	PATIENT_ID_INVALID           ("2002",  "就诊人编号无效"),
	PATIENT_IDCARD_INVALID       ("2003",  "身份证无效"),
	PATIENT_PHONENUM_INVALID     ("2004",  "手机号无效"),

	//(03000 - 03499) Order code
	ORDER_INVALID                ("3000",  "订单无效"),
	ORDER_PAYING                ("3001",  "正在处理订单"),
	PREPAY_ID_FAIL				("3002", "获取prepay_id失败"),
	REFUND_FAIL				    ("3003", "退款失败"),
	UNLOCK_FAIL				    ("3004", "错误代码3004"),
	UNLOCK_SUCCESS				("3005", "预约失败，已取消锁号"),
	
	//(01500 - 01999) Info resources
	INFO_BODY_ERROR              ("1500",  "获取身体列表异常"),
	INFO_PARTID_ERROR            ("1501",  "入参不能为空"),
	INFO_PARTIDDB_ERROR          ("1502",  "数据库数据为空"),

	//(10000 - 10000) Last code
	PAYING                       ("30011",  "支付正在处理"),
	PAYPARAMERROR                ("30012",  "该号码已注册"),
	HEALTHCARD_IDEN              ("30013",  "该身份已存在健康卡号"),
	INTEGRAL_NOT_ENOUGH			 ("30014",	"当前用户积分不足"),
	TODAY_CANNOT_EXCHANGE		 ("30015",	"库存不足或已达当日兑换上限"),
	REST_GOODS_NOT_ENOUGH		 ("30016",	"可兑换数量不足"),
	CANNOT_DELETE_GOODS			 ("30017",	"未使用商品不可删除"),
	HAS_EXCHANGED			     ("30018",	"商品已兑换"),
	GOODSNAME_ISREPEAT			 ("30019",	"商品重名"),
	GOODS_EDIT_FAILE             ("30020",  "更新库存失败"),
	
	LAST_CODE                    ("99999", "Last code"),

	INTEGRSTION_NOTFUND                  ("40001", "没有相应的增加积分规则"),
	INTEGRSTION_TIMEOUT                  ("40002", "该积分规则还没有到生效时间"),
	INTEGRSTION_TIMEOVER                  ("40003", "该积分规则已经过时间"),
	//doctoruser code
	DOCTOR_USERNAME_EXISTS         ("5000",  "该医生已注册"),
	DOCTOR_USERNAME_NOTEXISTS      ("5001",  "医生信息不存在"),
	DOCTOR_REGISTER_FAILE          ("5002",  "医生注册失败"),
	DOCTOR_USERINF_INVALID        ("5003",  "医生工号或密码无效"),
	DOCTOR_USERINF_SAVE           ("5004",  "保存医生信息失败"),
	DOCTOR_USERINF_NOMATCH        ("5005",  "信息不匹配"),
	DOCTOR_CARDID_EXISTS          ("5006",  "该身份证号已被注册"),
	
	//(001100 - 001199) 公告错误码
		BULLETIN_NO_STUDIOID         ("001100", "缺少工作室ID"),
		BULLETIN_NO_CREATORID        ("001101", "缺少创建者ID"),
		BULLETIN_NO_TITLE            ("001102", "缺少标题"),
		BULLETIN_NO_EXPIRETIME       ("001103", "缺少公告到期时间"),
		BULLETIN_NO_BULLETINID       ("001103", "缺少公告ID");

	ReturnCode(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	private String code;
	private String msg;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	//根据返回码获取返回对象
	public static ReturnCode getReturnCodeByCode(String code){
		for(ReturnCode returnCode : ReturnCode.values())
		{
			if(returnCode.getCode().equalsIgnoreCase(code))
				return returnCode;
		}			
		return null;
	}

}
