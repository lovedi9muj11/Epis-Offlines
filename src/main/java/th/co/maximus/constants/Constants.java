package th.co.maximus.constants;

import java.util.Locale;

public class Constants {
	
	public static final String CENTER_SERVICE_NAME = "ศูนย์บริการลูกค้า นนทบุรี";
	
	public static final String TEST = "TEST";
	public static final Locale localeTH = new Locale("th", "TH");
	public static final Locale localeEN = new Locale("en", "EN");
	public static final String SUCCESS = "SUCCESS";
	
	public static final String INIT_PROJECT = "INIT_PROJECT";
	public static final String INIT_PROJECT_NT_BUPLACE = "NT_BUPLACE";
	public static final String INIT_PROJECT_NT_SHOPNAME = "NT_SHOPNAME";
	public static final String INIT_PROJECT_BRANCH_AREA = "BRANCH_AREA";
	public static final String INIT_PROJECT_POS_NAME = "POS_NAME";
	public static final String INIT_PROJECT_TAX_ID_CAT = "TAX_ID_CAT";
	public static final String INIT_PROJECT_GL = "11149102";
	
	/** The Constant BLANK. */
	public static final String BLANK = "";
	
	/** The Constant COLON. */
	public static final String COLON = " : ";
	
	/** The Constant DASH. */
	public static final String DASH = " - ";
	
	/** The Constant KEY_TXNID. */
	public static final String KEY_TXNID = "TXNID";
	
	/** The Constant XPATH_TXNID. */
	public static final String XPATH_TXNID = "//transcationId";
	
	/** The Constant ENTRY. */
	public static final String ENTRY = "Entry";
	
	/** The Constant EXIT. */
	public static final String EXIT = "Exit";
	
	public static final String EVERY = "*";
	
	
	public static class DateTime {
		public static final String LOCALE = "TH";
		public static final String DATE_FORMAT = "dd/MM/yyyy";
		public static final String DATE_LONG_FORMAT = "dd MMMM yyyy";
		public static final String TIME_FORMAT = "HH:mm:ss";
		public static final String DB_DATE_FORMAT = "yyyy-MM-dd";
		public static final String D_DB_DATE_FORMAT = "dd-MM-yyyy";
		public static final String DB_TIME_FORMAT = "HH:mm:ss";
		public static final String OFFLINE = "OFFLINE";
	}

	public static class Status {
		public static final String ACTIVE = "A";
		public static final String ACTIVE_A = "ปกติ";
		public static final String ACTIVE_C = "ยกเลิก";
		public static final String ACTIVE_AC = "รอหักล้าง";
		public static final String ACTIVE_ = " - ";
		
		public static final String METHOD_WT_STR = "ภาษีหัก ณ ที่จ่าย";
		public static final String METHOD_WT = "WT";
		
		public static final String CC_WT = "เงินสด + WT";
		public static final String CC = "เงินสด";
		
		public static final String STATUS_CANCEL = "C";
	}
	
	public static class report {
		public static final String repotPathc = "/report/jasper/pdf";
		public static final String EXCELFULL = "ExcelFull";
		public static final String XXX = "RPTxxx";
		public static final String REPORT_FULL = "Report";
		public static final String REPORT_NOT_FULL = "ReportTax";
		public final static String RPT_EXCEL = "EXCEL";
		public final static String RPT_CODE_F = "ExcelFull";
		public final static String RPT_CODE_NF = "ExcelNotFull";
		
		public final static String HEARDER_DATE = "ประจำวันที่";
		public final static String HEARDER_DATE_TIME = "วันเวลาที่พิมพ์";
		public final static String SUM_TH = "รวมทั้งสิ้น : ";
	}
	public static class dataUser {
		public static final String BRANCHAREA = "1704";
		public static final String SOURCE = "OFFLINE";
		public static final String NAME_USER = "ADMIN";
	}
	
	public static class Service {
		public static final String SERVICE_TYPE_IBACSS = "IBACSS";
		public static final String SERVICE_TYPE_OTHER = "OTHER";
		
		public static final String CENTER_SERVICE = "ศูนย์บริการลูกค้า";
		public static final String CENTER_SERVICE_ = "ศบล.";
	}
	
	public static class MasterData {
		public static final String SELECT_DROPDOWN = "กรุณาเลือก";
		public static final String MASTERDATA_GROUP = "INITVALUE";
		public static final String STATUS_SUCCESS = "SUCCESS";
		public static final String STATUS_FAIL = "FAIL";
		public static final String BANK_TYPE = "BANK_TYPE";
		public static final String VAT = "VAT";
		public static final String OTHER = "OTHER";
		public static final String IBACSS = "IBACSS";
		public static final String BUSINESS_AREA = "BUSINESS_AREA";
		public static final String COST_CENTER = "COST_CENTER";
		public static final String OTHER_PAYMENT_UNIT = "OTHER_PAYMENT_UNIT";
		public static final String IBACSS_CANCEL_REASON = "OFFLINE_IBACSS_WOIBACSS_CANCEL_REASON";
		public static final String OTHER_CANCEL_REASON = "OFFLINE_OTHER_CANCEL_REASON";
		public static final String CANCEL_REASON_INDAY = "INDAY";
		public static final String EDC_CREDIT_CARD_BANK = "EDC_CREDIT_CARD_BANK";
		public static final String CUSTOMER_SEGMENT = "CUSTOMER_SEGMENT";
		public static final String CREDIT_CARD_TYPE = "CREDIT_CARD_TYPE";
		public static final String SEGMENT = "SEGMENT";
		public static final String PRODUCT = "PRODUCT";
		public static final String OTHER_TEMP_CUSTOMER_SEGMENT = "OTHER_TEMP_CUSTOMER_SEGMENT";
		public static final String PROFIT_COST_CENTER = "PROFIT_COST_CENTER";
		public static final String TRIGGER_GOUP = "TriggerGoup";
		public static final String BANK_TYPE_EDC = "BANK_TYPE_EDC";
		public static final String USER_GROUP = "UserGoup";
		public static final String WT = "WT";
		public static class KEYCODE {
			public static final String TRIGGER_MS = "Trigger_MS";
			public static final String TRIGGER_GL = "Trigger_GL";
			public static final String TRIGGER_USER = "Trigger_User";
			public static final String TRIGGER_MINUS = "Trigger_Minus";
			public static final String TRIGGER_CLEARING = "Trigger_Clearing";
			public static final String BANK_TYPE_EDC = "BANK_TYPE_EDC";
			
			public static final String MS_14 = "3";
			public static final String MS_12 = "2";
			public static final String MS_11 = "1";
		}
		
		public static class PROPERTY_1 {
			public static final String MS_14 = "14";
			public static final String MS_12 = "12";
			public static final String MS_11 = "11";
			public static final String MS_1 = "1";
		}
		
	}
	
	public static class GL_SERVICE {
		public static final String OTHER = "OTHER";
	}
	
	public static class CLEARING {
		public static final String STATUS_Y = "Y";
		public static final String STATUS_N = "N";
		public static final String STATUS_W = "W";
		public static final String STATUS_ERROR = "E";
		public static final String STATUS_PAYMENT = "PAYMENT";
		public static final String STATUS_CANCEL = "CANCEL";
	}
	
	public static class USER {
		public static final String LOGIN_FLAG_Y = "Y";
		public static final String LOGIN_FLAG_N = "N";
	}
	
	public static class Role {
		public static final String SUPPERVISOR = "SUPERVISOR";
		public static final String ADMIN = "ADMIN";
		public static final String USER = "USER";
		
		public static final String SUPPERVISOR_2 = "2";
		public static final String ADMIN_1 = "1";
		public static final String USER_3 = "3";
		
		public static class RoleOnline {
			public static final String SUPPERVISOR = "SUPERVISOR";
			public static final String ADMIN = "ADMIN";
			public static final String USER = "BACKOFFICE";
			public static final long SUPPERVISOR_8 = 8;
			public static final long USER_3 = 3;
			
			
		}
	}
	
	public static class CANCEL {
		public static final String CANCEL_SERVICE_01 = "01";
		public static final String CANCEL_ADDR_02 = "02";
		public static final String CANCEL_SERVICE = "รับชำระผิดบริการ";
		public static final String CANCEL_ADDR = "ชื่อ-ที่อยู่ ไม่ถูกต้อง";
	}
	
	public static class BATCH {
		public static final String JOB_1 = "jobWithSimpleTriggerBeanTrigger";
		public static final String JOB_2 = "jobWithSimpleTriggerBeanTrigger2";
		public static final String JOB_3 = "jobWithSimpleTriggerBeanTrigger3";
		public static final String JOB_4 = "jobWithSimpleTriggerBeanTrigger4";
		public static final String JOB_5 = "jobWithSimpleTriggerBeanTrigger5";
	}
	
	public static class PAYMENT_OTHER {
		public static final String NON_VATE = "nonVat";
	}
	
	public static class VATRATE {
		public static final String VATE_WORD = "Vat";
		public static final String NON_VATE = "Non-VAT";
		public static final String TEN = "10";
		public static final String EIGHT = "8";
		public static final String SEVEN = "7";
		public static final String ZERO = "0";
	}
	
	public static class PAYTYPE {
		public static final String WT = "WT";
	}
	
	public static class DOCTYPE {
		public static final String RO = "RO";
		public static final String RS = "RS";
		public static final String RF = "RF";
		public static final String TX = "TX";
	}
	
	public static class CUSTOMER_GROUP {
		public static final String CUSTOMER_1 = "1";
		public static final String CUSTOMER_2 = "2";
		public static final String CUSTOMER_3 = "3";
		public static final String CUSTOMER_4 = "4";
		public static final String CUSTOMER_5 = "5";
		public static final String CUSTOMER_6 = "6";
		public static final String CUSTOMER_7 = "7";
		public static final String CUSTOMER_8 = "8";
		public static final String CUSTOMER_9 = "9";
		
		public static final String CUSTOMER_NAME_1 = "ธุรกิจทั่วไป";
		public static final String CUSTOMER_NAME_2 = "หน่วยงานรัฐ";
		public static final String CUSTOMER_NAME_3 = "บุคคลทั่วไป";
		public static final String CUSTOMER_NAME_4 = "Carrier/Operator/NON POTs";
		public static final String CUSTOMER_NAME_5 = "Mkt.Arm";
		public static final String CUSTOMER_NAME_6 = "ISP";
		public static final String CUSTOMER_NAME_7 = "Reseller/Agent";
		public static final String CUSTOMER_NAME_8 = "ธุรกิจ กสท";
		public static final String CUSTOMER_NAME_9 = "สถานฑูต/องค์กรระหว่างประเทศ";
	}

}
