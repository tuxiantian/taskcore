package com.ai.taskcore.util;

/**
 */
public final class Constants {
    public interface VIRTUAL_USER_ACCT {
        String MANAGE_PORTALS = "1000000000050";
        String ZFB_SYSTEM = "zfb";
        String ZFB_ACCT = "zfb";
        String O2O_SYSTEM = "O2O";
        String O2O_ACCT = "O2O";
        String TASK_ACCT = "queryTerminalComplanyListInfo4Task";
    }

	public interface SYSTEM_PROP_CONFIG {
		String REDIS_ADDR_CFG = "REDIS_ADDR_CFG";// Redis IP地址配置，各ip之间以半角逗号","分隔
		String REDIS_CFG_SPLIT = ",";// 分隔符
		String APP_ARRAY_UNION_URL = "APP_ARRAY_UNION_URL";// ecpcore层地址配置
		String ESB_MCDSGATHR_SERVLET = "ESB_MCDSGATHR_SERVLET";//爬虫servlet
		String FLAG_YES="1";
		String FLAG_NO ="0";
		String FLAG_ERROR ="2";
		/**
		 * 系统默认的时间格式
		 */
		String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	}
	public static final String DEFAULT_START = "0";
	//预约订单状态表
	public interface T_OD_RSVT_ORDER {
		String FLAG_SUCCESS="0";	//处理成功
		String FLAG_NO ="1";	//未处理
		String FLAG_ERROR ="2";	//处理失败
		
		String CHNL_NM_GDYF = "广东云浮";
		String CHNL_NM_JTYDSC = "集团移动商城";
		String CHNL_NM_OTHER = "其它各省渠道";
		
		String CHNL_NM_GDYF_ID = "1017";
		String CHNL_NM_JTYDSC_ID = "1018";
		String CHNL_NM_OTHER_ID = "1019";
	}
	public static final String DEFAULT_LIMIT = "100";
	
	/**
	 * 订单表 中枚举值
	 * @author ZHANGMANG
	 */
	public interface T_OD_ODR {
		/**
		 * 普通订单类型
		 */
		String ODR_TYPE_CD_GEN = "01";
		/**
		 * 没有活动的促销编号;
		 */
		String PMT_ID = "10001";
		/**
		 * 没有活动的营销活动编号 ;
		 */
		String CMPGN_ID = "1001";
		
		//付款方式
		String PAYMN_DLVRY_PRI_TYPE_CD_ONLINE = "01";//在线支付(和包)
		String PAYMN_DLVRY_PRI_TYPE_CD_ALIPAY = "02";//支付宝支付
		String PAYMN_DLVRY_PRI_TYPE_CD_CASH = "03";//货到付款
		String PAYMN_DLVRY_PRI_TYPE_CD_ZYZX = "04";//中移在线支付
		
	}
	
	//订单状态表
	public interface T_OD_ODR_STS {
		//订单状态代码枚举值
		String ODR_STS_CD_INITIALISE = "1000";//初始订单
		String ODR_STS_CD_CANCEL = "1025";//已取消
		String ODR_STS_CD_REFUND = "1030";//已退款
		String ODR_STS_CD_RECEIVE = "1024";//已签收
		String ODR_STS_CD_SEND = "1010";//已发货
		String ODR_STS_CD_REFUNDING = "1031";//退款中
	}
}
