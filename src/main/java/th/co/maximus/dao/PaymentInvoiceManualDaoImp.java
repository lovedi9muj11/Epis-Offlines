package th.co.maximus.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import th.co.maximus.auth.model.UserProfile;
import th.co.maximus.bean.HistoryPaymentRS;
import th.co.maximus.bean.HistoryReportBean;
import th.co.maximus.bean.HistorySubFindBean;
import th.co.maximus.bean.InvoiceBean;
import th.co.maximus.bean.PaymentInvoiceManualBean;
import th.co.maximus.bean.PaymentMMapPaymentInvBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.model.PaymentInvoiceEpisOffline;
import th.co.maximus.model.PaymentMaualModel;

@Repository("PaymentInvoiceManualDao")
public class PaymentInvoiceManualDaoImp implements PaymentInvoiceManualDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<PaymentMMapPaymentInvBean> findPaymentMuMapPaymentInV() {
		UserProfile profile = (UserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StringBuilder sql = new StringBuilder();
		List<Object> param = new LinkedList<Object>();
		sql.append(" SELECT * FROM RECEIPT_MANUAL payment_m ");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL paument_inv ON payment_m.MANUAL_ID = paument_inv.MANUAL_ID");
		if(!Constants.Role.SUPPERVISOR.equals(profile.getRoles().get(0).getName())) {
			sql.append(" WHERE payment_m.CREATE_BY = ?");
			param.add(profile.getUsername());
		}
		Object[] paramArr = param.toArray();
		return jdbcTemplate.query(sql.toString(), paramArr, PaymentManual);
	}
 
	private static final RowMapper<PaymentMMapPaymentInvBean> PaymentManual = new RowMapper<PaymentMMapPaymentInvBean>() {
		// Utils utils = new Utils();

		@Override
		public PaymentMMapPaymentInvBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			PaymentMMapPaymentInvBean paymentManual = new PaymentMMapPaymentInvBean();
			paymentManual.setManualId(rs.getLong("MANUAL_ID"));
			paymentManual.setPaymentId(rs.getLong("PAYMENT_ID"));
			paymentManual.setInvoiceNo(rs.getString("INVOICE_NO"));
			paymentManual.setReceiptNoManual(rs.getString("RECEIPT_NO_MANUAL"));
			paymentManual.setPaidDate(rs.getTimestamp("PAID_DATE"));
			paymentManual.setBrancharea(rs.getString("BRANCH_AREA"));
			paymentManual.setBranchCode(rs.getString("BRANCH_CODE"));
			paymentManual.setPaidAmount(rs.getBigDecimal("PAID_AMOUNT"));
			paymentManual.setSource(rs.getString("SOURCE"));
			paymentManual.setClearing(rs.getString("CLEARING"));
			paymentManual.setRemark(rs.getString("REMARK"));
			paymentManual.setCreateBy(rs.getString("CREATE_BY"));
			paymentManual.setCreateDate(rs.getTimestamp("CREATE_DATE"));
			paymentManual.setRecordStatus(rs.getString("RECORD_STATUS"));
			paymentManual.setBeforVat(rs.getBigDecimal("BEFOR_VAT"));
			paymentManual.setBeforVatOther(rs.getBigDecimal("paument_inv.BEFOR_VAT"));
			paymentManual.setAmount(rs.getBigDecimal("AMOUNT"));
			paymentManual.setAmountOther(rs.getBigDecimal("paument_inv.AMOUNT"));
			paymentManual.setVatAmount(rs.getBigDecimal("VAT_AMOUNT"));
			paymentManual.setVatAmountOther(rs.getBigDecimal("paument_inv.VAT_AMOUNT"));
			paymentManual.setAccountNo(rs.getString("ACCOUNT_NO"));
			paymentManual.setPeriod(rs.getString("PERIOD"));
			paymentManual.setPayType((rs.getString("PAY_TYPE")));
			paymentManual.setCreateBy(rs.getString("CREATE_BY"));
			paymentManual.setCustomerName((rs.getString("CUSTOMER_NAME")));
			paymentManual.setCustomerAddress(rs.getString("CUSTOMER_ADDRESS"));
			paymentManual.setServiceType(rs.getString("SERVICE_TYPE"));
			paymentManual.setServiceName(rs.getString("SERVICENAME"));
			paymentManual.setCustomerGroup(rs.getString("CUSTOMER_GROUP"));
			paymentManual.setClearingDate(rs.getTimestamp("CLEARING_DATE"));
			
//			paymentManual.setCustomerGroupName(findByppt1(paymentManual.getCustomerGroup()));
			
//			paymentManual.setCustomerGroupName(rs.getString("VALUE"));
			
			paymentManual.setDiscountBeforVat(rs.getBigDecimal("DISCOUNTBEFORVAT"));
			paymentManual.setDiscountSpecial(rs.getBigDecimal("DISCOUNTSPECIAL"));
			paymentManual.setReasonCode(rs.getString("CANCEL_REASON_CODE"));
			paymentManual.setReasonDesc(rs.getString("CANCEL_REASON"));
			paymentManual.setReasonDesc(rs.getString("CANCEL_REASON"));
			paymentManual.setCancleDate(rs.getTimestamp("CANCEL_DATE"));
			paymentManual.setCancleBy(rs.getString("CANCEL_BY"));
			paymentManual.setDepartmentArea(rs.getString("paument_inv.DEPARTMENT_AREA"));
//			paymentManual.setSegmentOther(rs.getString("paument_inv.SEGMENT_OTHER"));
//			paymentManual.setProductOther(rs.getString("paument_inv.PRODUCT_OTHER"));
			return paymentManual;
		}

	};
	
	@Override
	public void insert(PaymentInvoiceManualBean paymentInvoiceManualBean) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"INSERT INTO PAYMENT_INVOICE_MANUAL (MANUAL_ID,SOURCE, INVOICE_NO,BEFOR_VAT,VAT_AMOUNT,AMOUNT,VAT_RATE, CUSTOMER_NAME, CUSTOMER_ADDRESS, CUSTOMER_SEGMENT, CUSTOMER_BRANCH, TAXNO, ACCOUNTSUBNO, PERIOD,SERVICE_TYPE, CLEARING, PRINT_RECEIPT, REMARK, CREATE_BY, CREATE_DATE,UPDATE_BY,UPDATE_DATE,RECORD_STATUS,QUANTITY,INCOMETYPE,DISCOUNTBEFORVAT,DISCOUNTSPECIAL,AMOUNTTYPE,DEPARTMENT,SERVICENAME,INVOICE_DATE,SERVICECODE,DEPARTMENTCODE,INCOMEUNIT,DEPARTMENT_AREA, SEGMENT_CODE, SEGMENT_NAME, PRODUCT_CODE, PRODUCT_NAME, GL_CODE)  ");
		sql.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ");

		jdbcTemplate.update(sql.toString(), paymentInvoiceManualBean.getManualId(),
				paymentInvoiceManualBean.getSource(), paymentInvoiceManualBean.getInvoiceNo(),
				paymentInvoiceManualBean.getBeforVat(), paymentInvoiceManualBean.getVatAmount(),
				paymentInvoiceManualBean.getAmount(), paymentInvoiceManualBean.getVatRate(),
				paymentInvoiceManualBean.getCustomerName(), paymentInvoiceManualBean.getCustomerAddress(),
				paymentInvoiceManualBean.getCustomerSegment(), paymentInvoiceManualBean.getCustomerBranch(),
				paymentInvoiceManualBean.getTaxNo(), paymentInvoiceManualBean.getSubNo(),
				paymentInvoiceManualBean.getPeriod(), paymentInvoiceManualBean.getServiceType(),
				paymentInvoiceManualBean.getClearing(), paymentInvoiceManualBean.getPrintReceipt(),
				paymentInvoiceManualBean.getRemark(), paymentInvoiceManualBean.getCreateBy(),
				paymentInvoiceManualBean.getCreateDate(), paymentInvoiceManualBean.getUpdateBy(),
				paymentInvoiceManualBean.getUpdateDate(), paymentInvoiceManualBean.getRecordStatus(),
				paymentInvoiceManualBean.getQuantity(), paymentInvoiceManualBean.getIncometype(),
				paymentInvoiceManualBean.getDiscountbeforvat(), paymentInvoiceManualBean.getDiscountspecial(),
				paymentInvoiceManualBean.getAmounttype(), paymentInvoiceManualBean.getDepartment(),
				paymentInvoiceManualBean.getServiceName(), paymentInvoiceManualBean.getInvoiceDate(),
				paymentInvoiceManualBean.getServiceCode(),paymentInvoiceManualBean.getDepartmentcode(),
				paymentInvoiceManualBean.getIncomeunit(),
				paymentInvoiceManualBean.getDepartmentArea(),
				paymentInvoiceManualBean.getSegmentCode(),
				paymentInvoiceManualBean.getSegmentName(),
				paymentInvoiceManualBean.getProductCode(),
				paymentInvoiceManualBean.getProductName(),
				paymentInvoiceManualBean.getGlCode());

	}

	@Override
	public List<PaymentMMapPaymentInvBean> findPaymentMuMapPaymentInVAccountIdNoClearing(String accountNo,String payType) {
		StringBuilder sql = new StringBuilder();
		List<Object> param = new LinkedList<Object>();
		sql.append(" SELECT * FROM RECEIPT_MANUAL payment_m ");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL paument_inv ON payment_m.MANUAL_ID = paument_inv.MANUAL_ID ");
		sql.append(" WHERE 1 = 1");
		if (!accountNo.equals("")) {
			sql.append(" AND payment_m.ACCOUNT_NO like ?");
			param.add("%" + accountNo + "%");
		}
		sql.append(" AND paument_inv.SERVICE_TYPE = ?");
		sql.append(" AND payment_m.CLEARING = '"+Constants.CLEARING.STATUS_N+"'");
		param.add(payType);
		sql.append(" GROUP by payment_m.MANUAL_ID  ORDER BY payment_m.CREATE_DATE DESC");
		Object[] paramArr = param.toArray();
		return jdbcTemplate.query(sql.toString(), paramArr, PaymentManual);
	}
	
	@Override
	public List<PaymentMMapPaymentInvBean> findPaymentMuMapPaymentInVAccountIdNoSearch(String accountNo,String payType) {
		StringBuilder sql = new StringBuilder();
		List<Object> param = new LinkedList<Object>();
		sql.append(" SELECT * FROM RECEIPT_MANUAL payment_m ");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL paument_inv ON payment_m.MANUAL_ID = paument_inv.MANUAL_ID ");
		sql.append(" WHERE 1 = 1");
		if (!accountNo.equals("")) {
			sql.append(" AND payment_m.ACCOUNT_NO like ?");
			param.add("%" + accountNo + "%");
		}
		sql.append(" AND paument_inv.SERVICE_TYPE = ?");
//		sql.append(" AND payment_m.CLEARING = '"+Constants.CLEARING.STATUS_N+"'");
		param.add(payType);
		sql.append(" GROUP by payment_m.MANUAL_ID  ORDER BY payment_m.CREATE_DATE DESC");
		Object[] paramArr = param.toArray();
		return jdbcTemplate.query(sql.toString(), paramArr, PaymentManual);
	}
	
	@Override
	public List<PaymentMMapPaymentInvBean> findPaymentMuMapPaymentInVAccountIdNoSearchOther(PaymentMMapPaymentInvBean invBean, String payType) {
		StringBuilder sql = new StringBuilder();
		List<Object> param = new LinkedList<Object>();
		sql.append(" SELECT * FROM RECEIPT_MANUAL payment_m ");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL paument_inv ON payment_m.MANUAL_ID = paument_inv.MANUAL_ID ");
		sql.append(" WHERE 1 = 1");
		
		if (StringUtils.isNotBlank(invBean.getAccountNo())) {
			sql.append(" AND payment_m.ACCOUNT_NO like ?");
			param.add("%" + invBean.getAccountNo() + "%");
		}
		
		if (StringUtils.isNotBlank(invBean.getTaxId())) {
			sql.append(" AND paument_inv.TAXNO = ?");
			param.add(invBean.getTaxId());
		}
		
		if (StringUtils.isNotBlank(invBean.getCustName())) {
			sql.append(" AND paument_inv.CUSTOMER_NAME = ?");
			param.add(invBean.getCustName());
		}
		
		sql.append(" AND paument_inv.SERVICE_TYPE = ?");
//		sql.append(" AND payment_m.CLEARING = '"+Constants.CLEARING.STATUS_N+"'");
		param.add(payType);
		sql.append(" GROUP by payment_m.MANUAL_ID  ORDER BY payment_m.CREATE_DATE DESC");
		Object[] paramArr = param.toArray();
		return jdbcTemplate.query(sql.toString(), paramArr, PaymentManual);
	}

	@Override
	public List<PaymentMMapPaymentInvBean> findPaymentMuMapPaymentInVFromId(long manual_id) {
		StringBuilder sql = new StringBuilder();
		List<Object> param = new LinkedList<Object>();
		sql.append(" SELECT * FROM RECEIPT_MANUAL payment_m ");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL paument_inv ON payment_m.MANUAL_ID = paument_inv.MANUAL_ID ");
		sql.append(" WHERE  payment_m.MANUAL_ID  =  ?");
		param.add(manual_id);
		sql.append(" GROUP by payment_m.MANUAL_ID  ORDER BY payment_m.CREATE_DATE DESC");
		Object[] paramArr = param.toArray();
		System.out.println(sql.toString());
		return jdbcTemplate.query(sql.toString(), paramArr, PaymentManual);
	}

	@Override
	public List<PaymentMMapPaymentInvBean> findCriteriaFromInvoiceOrReceiptNo(String receiptNo, String code, boolean chkCancel) {
		SimpleDateFormat dateFM = new SimpleDateFormat(Constants.DateTime.DB_DATE_FORMAT,Locale.ENGLISH); 
		StringBuilder sql = new StringBuilder();
		List<Object> param = new LinkedList<Object>();
		UserProfile profile = (UserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		sql.append(" SELECT * FROM RECEIPT_MANUAL payment_m ");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL paument_inv ON payment_m.MANUAL_ID = paument_inv.MANUAL_ID ");
		sql.append(" INNER JOIN master_data ms ON ms.PROPERTY_1 = payment_m.CUSTOMER_GROUP ");
		sql.append(" WHERE 1 = 1 ");
		if(chkCancel) {
			if (receiptNo != "" && "".equals(code)) {
				sql.append(" AND payment_m.RECEIPT_NO_MANUAL = ?");
				param.add(receiptNo);
			}
			if (code != "" && "".equals(receiptNo)) {
				sql.append(" AND  payment_m.INVOICE_NO = ?");
				param.add(code);
			}
			sql.append(" AND payment_m.INVOICE_NO IS NOT NULL ");
		}else {
			if (receiptNo != "" && "".equals(code)) {
				sql.append(" AND payment_m.RECEIPT_NO_MANUAL = ?");
				param.add(receiptNo);
			}
			if (code != "" && "".equals(receiptNo)) {
				sql.append(" AND  payment_m.ACCOUNT_NO = ?");
				param.add(code);
			}
			sql.append(" AND payment_m.INVOICE_NO IS NULL ");
		}
		
		if(!Constants.Role.SUPPERVISOR.equals(profile.getRoles().get(0).getName())) {
			sql.append(" AND payment_m.CREATE_BY = ?");
			param.add(profile.getUsername());
		}
		// if (receiptNo != "" && invoiceNo != "") {
		// sql.append(" AND payment_m.RECEIPT_NO_MANUAL = ?");
		// param.add(receiptNo);
		// sql.append(" AND ");
		// sql.append(" payment_m.INVOICE_NO = ?");
		// param.add(invoiceNo);
		// }
		sql.append(" AND payment_m.CREATE_DATE >= '"+dateFM.format(new Date())+" 00:00:00' ");
		
		if(chkCancel) {
			sql.append(" GROUP by payment_m.MANUAL_ID ");
		} else {
			sql.append(" GROUP by payment_m.receipt_no_manual ");
		}
		sql.append(" ORDER BY payment_m.CREATE_DATE DESC ");
		Object[] paramArr = param.toArray();
		return jdbcTemplate.query(sql.toString(), paramArr, PaymentManual);
	}
	
	
	@Override
	public List<PaymentMaualModel> findCriteriaFromReceiptNo(String[] receiptNo) {
//		SimpleDateFormat dateFM = new SimpleDateFormat(Constants.DateTime.DB_DATE_FORMAT,Locale.ENGLISH); 
		StringBuilder sql = new StringBuilder();
//		List<Object> param = new LinkedList<Object>();
//		UserProfile profile = (UserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		sql.append(" SELECT * FROM RECEIPT_MANUAL payment_m ");
//		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL paument_inv ON payment_m.MANUAL_ID = paument_inv.MANUAL_ID ");
//		sql.append(" INNER JOIN master_data ms ON ms.PROPERTY_1 = payment_m.CUSTOMER_GROUP ");
		sql.append(" WHERE 1 = 1  AND ");
		for (int i = 0; i < receiptNo.length; i++) {
			sql.append(" RECEIPT_NO_MANUAL = '"+receiptNo[i]+"'");
			if(i+1 != receiptNo.length) {
				sql.append(" OR ");
			}
		}
		
		
		return jdbcTemplate.query(sql.toString(), PaymentReceipt);
	}
	
	private static final RowMapper<PaymentMaualModel> PaymentReceipt = new RowMapper<PaymentMaualModel>() {
		// Utils utils = new Utils();

		@Override
		public PaymentMaualModel mapRow(ResultSet rs, int rowNum) throws SQLException {
			PaymentMaualModel paymentManual = new PaymentMaualModel();
			paymentManual.setManualId(rs.getLong("MANUAL_ID"));
			paymentManual.setPaymentId(rs.getLong("PAYMENT_ID"));
			paymentManual.setInvoiceNo(rs.getString("INVOICE_NO"));
			paymentManual.setReceiptNoManual(rs.getString("RECEIPT_NO_MANUAL"));
			paymentManual.setPaidDate(rs.getTimestamp("PAID_DATE"));
			paymentManual.setBrancharea(rs.getString("BRANCH_AREA"));
			paymentManual.setBranchCode(rs.getString("BRANCH_CODE"));
//			paymentManual.setPaidAmount(rs.getBigDecimal("PAID_AMOUNT"));
			paymentManual.setSource(rs.getString("SOURCE"));
			paymentManual.setClearing(rs.getString("CLEARING"));
			paymentManual.setRemark(rs.getString("REMARK"));
			paymentManual.setCreateBy(rs.getString("CREATE_BY"));
			paymentManual.setCreateDate(rs.getTimestamp("CREATE_DATE"));
			paymentManual.setAccountNo(rs.getString("ACCOUNT_NO"));
			paymentManual.setCreateBy(rs.getString("CREATE_BY"));

			return paymentManual;
		}

	};

	@Override
	public void updateRecodeStatusFromReceiptNo(String status, long manualId, String cancel, String user, String  reasonCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE RECEIPT_MANUAL payment_m ");
		sql.append(" SET payment_m.RECORD_STATUS =  ? ");
		sql.append(" , payment_m.CANCEL_REASON =  ? ");
		sql.append(" , payment_m.CANCEL_DATE =  ? ");
		sql.append(" , payment_m.CANCEL_BY =  ? ");
		sql.append(" , payment_m.CANCEL_REASON_CODE =  ? ");
		sql.append(" WHERE payment_m.MANUAL_ID = ? ");
		jdbcTemplate.update(sql.toString(), status, cancel, null, user, reasonCode, manualId);

	}

	@Override
	public List<PaymentInvoiceManualBean> findPaymentInvoiceFromManualId(long manualId) {
		StringBuilder sql = new StringBuilder();
		List<Object> param = new LinkedList<Object>();
		sql.append(" SELECT * FROM PAYMENT_INVOICE_MANUAL PAYMENT_INVOICE where PAYMENT_INVOICE.MANUAL_ID = ?");
		param.add(manualId);
		sql.append("  GROUP by PAYMENT_INVOICE.MANUAL_ID ORDER BY PAYMENT_INVOICE.CREATE_DATE DESC");
		Object[] paramArr = param.toArray();
		return jdbcTemplate.query(sql.toString(), paramArr, PaymentInvoice);
	}

	@Override
	public void updateStatusPaymentInvoice(long manualId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" UPDATE PAYMENT_INVOICE_MANUAL payment_m ");
		sql.append(" SET payment_m.RECORD_STATUS =  'C' ");
		sql.append(" WHERE payment_m.MANUAL_ID = ? ");
		jdbcTemplate.update(sql.toString(), manualId);
	}

	@Override
	public List<PaymentMMapPaymentInvBean> findPayOrder(HistorySubFindBean paymentInvBean) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM RECEIPT_MANUAL pm");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL pim on pm.INVOICE_NO = pim.INVOICE_NO ");
		if (paymentInvBean.getPayDate() != null && paymentInvBean.getPayDateTo() != null) {
			sql.append(" where ");
			sql.append(" pm.PAID_DATE BETWEEN '" + paymentInvBean.getPayDate() + "'");
			sql.append(" and '" + paymentInvBean.getPayDateTo() + "'");
		}
		if (StringUtils.isNotBlank(paymentInvBean.getVatRate())) {
			sql.append(" and pm.VAT_RATE = '" + paymentInvBean.getVatRate() + "'");
		} else {
			sql.append(" and pm.VAT_RATE like '%" + paymentInvBean.getVatRate() + "%'");
		}
		if (StringUtils.isNotBlank(paymentInvBean.getUser())) {
			sql.append(" and pm.UPDATE_BY = '" + paymentInvBean.getUser() + "'");
		}
		if (StringUtils.isNotBlank(paymentInvBean.getPayType())) {
			sql.append(" and pim.SERVICE_TYPE like '%" + paymentInvBean.getPayType() + "%'");
		}
		sql.append(" GROUP by pm.MANUAL_ID  ORDER BY pm.CREATE_DATE DESC");
		return jdbcTemplate.query(sql.toString(), PaymentManual);
	}

	@Override
	public List<PaymentMMapPaymentInvBean> findPayOrderFulln(HistorySubFindBean paymentInvBean) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM RECEIPT_MANUAL pm");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL pim on pm.INVOICE_NO = pim.INVOICE_NO ");
		if (paymentInvBean.getPayDate() != null && paymentInvBean.getPayDateTo() != null) {
			sql.append(" where ");
			sql.append(" pm.PAID_DATE BETWEEN '" + paymentInvBean.getPayDate() + "'");
			sql.append(" and '" + paymentInvBean.getPayDateTo() + "'");
		}
		if (StringUtils.isNotBlank(paymentInvBean.getPayType())) {
			sql.append(" and pm.PAY_TYPE like '%" + paymentInvBean.getPayType() + "%'");
		}
		sql.append("  GROUP by pm.MANUAL_ID  ORDER BY pm.CREATE_DATE DESC");
		return jdbcTemplate.query(sql.toString(), PaymentManual);
	}

	private static final RowMapper<PaymentInvoiceManualBean> PaymentInvoice = new RowMapper<PaymentInvoiceManualBean>() {

		@Override
		public PaymentInvoiceManualBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			PaymentInvoiceManualBean paymentInvoice = new PaymentInvoiceManualBean();
			paymentInvoice.setPaymentInvoiceManualId(rs.getLong("PAYMENT_INVOICE_MANUAL_ID"));
			paymentInvoice.setManualId(rs.getLong("MANUAL_ID"));
			paymentInvoice.setSource(rs.getString("SOURCE"));
			paymentInvoice.setInvoiceNo(rs.getString("INVOICE_NO"));
			paymentInvoice.setBeforVat(rs.getDouble("BEFOR_VAT"));
			paymentInvoice.setVatAmount(rs.getDouble("VAT_AMOUNT"));
			paymentInvoice.setAmount(rs.getDouble("AMOUNT"));
			paymentInvoice.setVatRate(rs.getString("VAT_RATE"));
			paymentInvoice.setCustomerName(rs.getString("CUSTOMER_NAME"));
			paymentInvoice.setCustomerSegment(rs.getString("CUSTOMER_SEGMENT"));
			paymentInvoice.setCustomerBranch(rs.getString("CUSTOMER_BRANCH"));
			paymentInvoice.setTaxNo(rs.getString("TAXNO"));
			paymentInvoice.setAccountSubNo(rs.getString("ACCOUNTSUBNO"));
			paymentInvoice.setPeriod(rs.getString("PERIOD"));
			paymentInvoice.setServiceType(rs.getString("SERVICE_TYPE"));
			paymentInvoice.setClearing(rs.getString("CLEARING"));
			paymentInvoice.setPrintReceipt(rs.getString("PRINT_RECEIPT"));
			paymentInvoice.setRemark(rs.getString("REMARK"));
			paymentInvoice.setCreateBy(rs.getString("CREATE_BY"));
			paymentInvoice.setCreateDate(rs.getTimestamp("CREATE_DATE"));
			paymentInvoice.setUpdateBy(rs.getString("UPDATE_BY"));
			paymentInvoice.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
			paymentInvoice.setRecordStatus(rs.getString("RECORD_STATUS"));
			paymentInvoice.setQuantity(rs.getInt("QUANTITY"));
			paymentInvoice.setIncometype(rs.getString("INCOMETYPE"));
			paymentInvoice.setDiscountbeforvat(rs.getBigDecimal("DISCOUNTBEFORVAT"));
			paymentInvoice.setDiscountspecial(rs.getBigDecimal("DISCOUNTSPECIAL"));
			paymentInvoice.setAmounttype(rs.getString("AMOUNTTYPE"));
			paymentInvoice.setDepartment(rs.getString("DEPARTMENT"));
			paymentInvoice.setServiceName(rs.getString("SERVICENAME"));
			paymentInvoice.setServiceCode(rs.getString("SERVICECODE"));
			paymentInvoice.setInvoiceDate(rs.getDate("INVOICE_DATE"));
			return paymentInvoice;
		}

	};

	@Override
	public List<HistoryPaymentRS> findPaymentOrder(HistoryReportBean historyRpt) {
//		List<HistoryPaymentRS> collections = new ArrayList<HistoryPaymentRS>();
		List<HistoryPaymentRS> historyPaymentRSs = new ArrayList<HistoryPaymentRS>();
		List<Object> param = new LinkedList<Object>();

		try {
			StringBuilder sql = new StringBuilder();
			sql.append(
					" SELECT py.CREATE_DATE ,py.INVOICE_NO,pim.CUSTOMER_NAME , pim.TAXNO ,py.BRANCH_CODE , py.RECORD_STATUS ,py.RECEIPT_NO_MANUAL,py.PAID_AMOUNT,pim.VAT_RATE,pim.VAT_AMOUNT,pim.BEFOR_VAT, py.AMOUNT, py.CREATE_BY, py.VAT_AMOUNT, py.VAT_AMOUNT as VAT_AMOUNTx");
			sql.append(" FROM RECEIPT_MANUAL py");
			sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL pim ON pim.MANUAL_ID = py.MANUAL_ID ");
			sql.append(" WHERE 1=1 ");
			sql.append(" and py.DOCTYPE IN "+"('"+historyRpt.getTypePrint()+"',"+"'"+Constants.DOCTYPE.TX+"')");
			if (StringUtils.isNoneEmpty(historyRpt.getDateFrom())
					&& StringUtils.isNoneEmpty(historyRpt.getDateFromHour())
					&& StringUtils.isNoneEmpty(historyRpt.getDateFromMinute())) {

				String dateFrom = convertDateString(historyRpt.getDateFrom()) + " " + historyRpt.getDateFromHour() + ":"
						+ historyRpt.getDateFromMinute() + ":" + "00" + ":" + "000000";
				sql.append(" AND py.CREATE_DATE >= '").append(" " + dateFrom + " ' ");

			}

			if (StringUtils.isNoneEmpty(convertDateString(historyRpt.getDateTo()))
					&& StringUtils.isNoneEmpty(historyRpt.getDateToHour())
					&& StringUtils.isNoneEmpty(historyRpt.getDateToMinute())) {
				String dateTo = historyRpt.getDateTo() + " " + historyRpt.getDateToHour() + ":"
						+ historyRpt.getDateToMinute() + ":" + "59" + ":" + "999999";
				sql.append(" AND py.CREATE_DATE <= ' ").append(" " + dateTo + " ' ");
			}
			
//			param.add(historyRpt.getTypePrint());
			if(StringUtils.isNotEmpty(historyRpt.getUnserLogin())) {
				sql.append(" and pim.CREATE_BY = ? ");
				
				param.add(historyRpt.getUnserLogin());
			}

//			sql.append(" GROUP by py.MANUAL_ID  ORDER BY py.RECEIPT_NO_MANUAL DESC");
			sql.append(" GROUP by py.MANUAL_ID  ORDER BY py.RECEIPT_NO_MANUAL ASC ");
			// sql.append(" GROUP BY tm.NAME ");
			
			Object[] paramArr = param.toArray();
			historyPaymentRSs = jdbcTemplate.query(sql.toString(), paramArr, new mapHistory());
			
//			PreparedStatement preparedStatement = connect.prepareStatement(sql.toString());
//			preparedStatement.setString(1, historyRpt.getTypePrint());
//			ResultSet resultSet = preparedStatement.executeQuery();
//			while (resultSet.next()) {
//				// collections.add(new HistoryPaymen) ;
//				collections.add(new HistoryPaymentRS(resultSet.getDate(1), resultSet.getString(2),
//						resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6),
//						resultSet.getString(7), resultSet.getBigDecimal(8), resultSet.getInt(9),resultSet.getBigDecimal(10),resultSet.getBigDecimal(11)));
//
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		finally {
//			connect.close();
//		}

		return historyPaymentRSs;
	}

	public static final String convertDateString(String str) {
		return str.replaceAll("([0-9]{2})/([0-9]{2})/([0-9]{4})", "$3-$2-$1");

	}

	@Override
	public List<PaymentInvoiceEpisOffline> findByManualId(long manualId){
		List<PaymentInvoiceEpisOffline> beanReReq = new ArrayList<PaymentInvoiceEpisOffline>();
		List<Object> param = new LinkedList<Object>();
//		PaymentInvoiceEpisOffline bean = new PaymentInvoiceEpisOffline();
		try {
			StringBuilder sqlStmt = new StringBuilder();
			sqlStmt.append(
					"SELECT pim.INVOICE_NO ,pim.BEFOR_VAT,pim.VAT_AMOUNT,pim.AMOUNT,(CASE WHEN pim.VAT_RATE = 'Non-VAT' THEN null ELSE pim.VAT_RATE END) AS  VAT_RATE ,pim.CUSTOMER_NAME,pim.CUSTOMER_ADDRESS,pim.CUSTOMER_SEGMENT,pim.CUSTOMER_BRANCH,pim.TAXNO,pim.ACCOUNTSUBNO,pim.PERIOD,pim.SERVICE_TYPE,pim.REMARK,pim.QUANTITY,pim.INCOMETYPE,pim.DISCOUNTBEFORVAT,pim.DISCOUNTSPECIAL,pim.AMOUNTTYPE,pim.DEPARTMENT,pim.SERVICENAME,pim.SERVICECODE,pim.INVOICE_DATE,pim.INCOMEUNIT , pim.DEPARTMENT_AREA ");
			sqlStmt.append(" ,pim.SEGMENT_CODE , pim.PRODUCT_CODE , A.VALUE ");
			sqlStmt.append(" FROM PAYMENT_INVOICE_MANUAL pim ");
			sqlStmt.append(" LEFT JOIN MASTER_DATA A ON A.PROPERTY_1 =  pim.DEPARTMENT_AREA AND A.PROPERTY_2 = pim.DEPARTMENTCODE ");
			sqlStmt.append(" WHERE  pim.MANUAL_ID = ?  ");

			sqlStmt.append(" ORDER BY pim.CREATE_DATE DESC");
			
			param.add(manualId);
			Object[] paramArr = param.toArray();
			beanReReq = jdbcTemplate.query(sqlStmt.toString(), paramArr, new mapPayment());
			
//			PreparedStatement preparedStatement = connect.prepareStatement(sqlStmt.toString());
//			preparedStatement.setLong(1, manualId);
//			ResultSet resultSet = preparedStatement.executeQuery();
//			while (resultSet.next()) {
//				bean = new PaymentInvoiceEpisOffline(resultSet.getString(1), resultSet.getBigDecimal(2),
//						resultSet.getBigDecimal(3), resultSet.getBigDecimal(4), resultSet.getBigDecimal(5),
//						resultSet.getString(6), resultSet.getString(7), resultSet.getString(8), resultSet.getString(9),
//						resultSet.getString(10), resultSet.getString(11), resultSet.getString(12),
//						resultSet.getString(13), resultSet.getString(14), resultSet.getInt(15), resultSet.getString(16),
//						resultSet.getBigDecimal(17), resultSet.getBigDecimal(18), resultSet.getString(19),
//						resultSet.getString(20), resultSet.getString(21), resultSet.getString(22),
//						resultSet.getDate(23));
//				beanReReq.add(bean);
//			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return beanReReq;
	}

	@Override
	public void insertInvoice(InvoiceBean invoice) {
		StringBuilder sql = new StringBuilder();
		sql.append(
				"INSERT INTO `PAYMENT_INVOICE` (`MANUAL_ID`, `INVOICE_NO`, `INVOICE_DATE`, `DATE_LINE`, `BEFOR_VAT`, `VAT_AMOUNT`, `PAID_AMOUNT`, `AMOUNT`, `VAT_RATE`, `CUSTOMER_NAME`, `CUSTOMER_ADDRESS`, `CUSTOMER_SEGMENT`, `CUSTOMER_BRANCH`, "
						+ "`TAXNO`, `ACCOUNTSUBNO`, `PERIOD`, `SERVICENAME`, `SERVICE_TYPE`, `CHANG`, `CREATE_BY`, `CREATE_DATE`, `UPDATE_BY`, `UPDATE_DATE`, `RECORD_STATUS`, `DISCOUNT`) ");
		sql.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)  ");

		jdbcTemplate.update(sql.toString(), invoice.getManualId(), invoice.getInvoiceNo(), invoice.getInvoiceDate(),
				invoice.getDateLine(), invoice.getBeforVat(), invoice.getVatAmount(), invoice.getPaidAmount(),
				invoice.getAmount(), invoice.getVatRate(), invoice.getCustomerName(), invoice.getCustometAddress(),
				invoice.getCustometSegment(), invoice.getCustomerBranch(), invoice.getTaxNo(),
				invoice.getAccountSubNo(), invoice.getPeriod(), invoice.getServiceName(), invoice.getServiceType(),
				invoice.getChang(), invoice.getCreateBy(), invoice.getCreateDate(), invoice.getUpdateBy(),
				invoice.getUpdateDate(), invoice.getRecordStatus(), invoice.getDiscount());

	}

	private static final RowMapper<InvoiceBean> invoice = new RowMapper<InvoiceBean>() {

		@Override
		public InvoiceBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			InvoiceBean invoice = new InvoiceBean();
			invoice.setInvoiceId(rs.getLong("INV_ID"));
			invoice.setManualId(rs.getLong("MANUAL_ID"));
			invoice.setInvoiceNo(rs.getString("INVOICE_NO"));
			invoice.setInvoiceDate(rs.getTimestamp("INVOICE_DATE"));
			invoice.setDateLine(rs.getTimestamp("DATE_LINE"));
			invoice.setBeforVat(rs.getBigDecimal("BEFOR_VAT"));
			invoice.setVatAmount(rs.getBigDecimal("VAT_AMOUNT"));
			invoice.setPaidAmount(rs.getBigDecimal("PAID_AMOUNT"));
			invoice.setAmount(rs.getBigDecimal("AMOUNT"));
			invoice.setVatRate(rs.getString("VAT_RATE"));
			invoice.setCustomerName(rs.getString("CUSTOMER_NAME"));
			invoice.setCustometAddress(rs.getString("CUSTOMER_ADDRESS"));
			invoice.setCustometSegment(rs.getString("CUSTOMER_SEGMENT"));
			invoice.setCustomerBranch(rs.getString("CUSTOMER_BRANCH"));
			invoice.setTaxNo(rs.getString("TAXNO"));
			invoice.setAccountSubNo(rs.getString("ACCOUNTSUBNO"));
			invoice.setPeriod(rs.getString("PERIOD"));
			invoice.setServiceName(rs.getString("SERVICENAME"));
			invoice.setServiceType(rs.getString("SERVICE_TYPE"));
			invoice.setChang(rs.getBigDecimal("CHANG"));
			invoice.setCreateBy(rs.getString("CREATE_BY"));
			invoice.setCreateDate(rs.getTimestamp("CREATE_DATE"));
			invoice.setUpdateBy(rs.getString("UPDATE_BY"));
			invoice.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
			invoice.setRecordStatus(rs.getString("RECORD_STATUS"));
			invoice.setDiscount(rs.getBigDecimal("DISCOUNT"));
			return invoice;
		}

	};

	@Override
	public InvoiceBean findInvoiceByManualId(Long manualId) {
		InvoiceBean result = new InvoiceBean();
		StringBuilder sql = new StringBuilder();
		List<Object> param = new LinkedList<Object>();
		sql.append(" SELECT * FROM PAYMENT_INVOICE invoice WHERE invoice.MANUAL_ID = ?");
		param.add(manualId);
		sql.append(" GROUP by invoice.MANUAL_ID  ORDER BY invoice.CREATE_DATE DESC");
		Object[] paramArr = param.toArray();
		try {
			result = jdbcTemplate.queryForObject(sql.toString(), paramArr, invoice);
		}catch (Exception e) {
			
		}
		return result;
	}

	@Override
	public PaymentInvoiceManualBean findInvoiceManualByManualId(Long manualId) {
		StringBuilder sql = new StringBuilder();
		List<Object> param = new LinkedList<Object>();
		sql.append(" SELECT * FROM PAYMENT_INVOICE_MANUAL invoice WHERE invoice.MANUAL_ID = ?");
		param.add(manualId);
		sql.append(" GROUP by invoice.MANUAL_ID  ORDER BY invoice.CREATE_DATE DESC");
		Object[] paramArr = param.toArray();
		return jdbcTemplate.queryForObject(sql.toString(), paramArr, PaymentInvoice);
	}

	@Override
	public List<PaymentMMapPaymentInvBean> findPaymentMuMapPaymentInVs(String clearing) {
		StringBuilder sql = new StringBuilder();
		List<Object> param = new LinkedList<Object>();
		sql.append(" SELECT * FROM RECEIPT_MANUAL payment_m ");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL paument_inv ON payment_m.MANUAL_ID = paument_inv.MANUAL_ID");
		sql.append(" WHERE payment_m.CLEARING = ? AND payment_m.RECORD_STATUS = 'C' ");
		param.add(clearing);
		sql.append("  GROUP by payment_m.MANUAL_ID  ORDER BY payment_m.CREATE_DATE DESC");
		Object[] paramArr = param.toArray();
		return jdbcTemplate.query(sql.toString(), paramArr, PaymentManual);
	}
	
	@Override
	public List<PaymentMMapPaymentInvBean> findPaymentMuMapPaymentStatusActive(String clearing ,String Order) {
		StringBuilder sql = new StringBuilder();
		List<Object> param = new LinkedList<Object>();
		sql.append(" SELECT * FROM RECEIPT_MANUAL payment_m ");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL paument_inv ON payment_m.MANUAL_ID = paument_inv.MANUAL_ID");
		sql.append(" WHERE (payment_m.CLEARING = ? OR payment_m.CLEARING = 'W') AND payment_m.RECORD_STATUS = '"+Constants.Status.ACTIVE+"' ");
		param.add(clearing);
		sql.append("  GROUP by payment_m.MANUAL_ID  ORDER BY payment_m.CREATE_DATE "+Order+" ");
		Object[] paramArr = param.toArray();
		return jdbcTemplate.query(sql.toString(), paramArr, PaymentManual);
	}
	
	private static final class mapPayment implements RowMapper<PaymentInvoiceEpisOffline> {

		@Override
		public PaymentInvoiceEpisOffline mapRow(ResultSet rs, int rowNum) throws SQLException {
			PaymentInvoiceEpisOffline paymentInvoiceEpisOffline = new PaymentInvoiceEpisOffline();
			paymentInvoiceEpisOffline.setInvoiceNo(rs.getString("pim.INVOICE_NO"));
			paymentInvoiceEpisOffline.setBeforVat(rs.getBigDecimal("pim.BEFOR_VAT"));
			paymentInvoiceEpisOffline.setVatAmount(rs.getBigDecimal("pim.VAT_AMOUNT"));
			paymentInvoiceEpisOffline.setAmount(rs.getBigDecimal("pim.AMOUNT"));
			paymentInvoiceEpisOffline.setVatRate(rs.getString("VAT_RATE"));
			paymentInvoiceEpisOffline.setCustomerName(rs.getString("pim.CUSTOMER_NAME"));
			paymentInvoiceEpisOffline.setCustomerAddress(rs.getString("pim.CUSTOMER_ADDRESS"));
			paymentInvoiceEpisOffline.setCustomerSegment(rs.getString("pim.CUSTOMER_SEGMENT"));
			paymentInvoiceEpisOffline.setCustomerBranch(rs.getString("pim.CUSTOMER_BRANCH"));
			paymentInvoiceEpisOffline.setTaxNo(rs.getString("pim.TAXNO"));
			paymentInvoiceEpisOffline.setAccountSubNo(rs.getString("pim.ACCOUNTSUBNO"));
			paymentInvoiceEpisOffline.setPeriod(rs.getString("pim.PERIOD") == null ? "" : formatDateDoc(rs.getString("pim.PERIOD")));
			paymentInvoiceEpisOffline.setServiceType(rs.getString("pim.SERVICE_TYPE"));
			paymentInvoiceEpisOffline.setRemark(rs.getString("pim.REMARK"));
			paymentInvoiceEpisOffline.setQuantity(rs.getInt("pim.QUANTITY"));
			paymentInvoiceEpisOffline.setIncomeType(rs.getString("pim.INCOMETYPE"));
			paymentInvoiceEpisOffline.setDiscountBeforvat(rs.getBigDecimal("pim.DISCOUNTBEFORVAT"));
			paymentInvoiceEpisOffline.setDiscountSpecial(rs.getBigDecimal("pim.DISCOUNTSPECIAL"));
			paymentInvoiceEpisOffline.setAmountType(rs.getString("pim.AMOUNTTYPE"));
			paymentInvoiceEpisOffline.setDepartment(rs.getString("pim.DEPARTMENT"));
			paymentInvoiceEpisOffline.setServiceName(rs.getString("pim.SERVICENAME"));
			paymentInvoiceEpisOffline.setServiceCode(rs.getString("pim.SERVICECODE"));
			paymentInvoiceEpisOffline.setInvoiceDate(rs.getDate("pim.INVOICE_DATE"));
			paymentInvoiceEpisOffline.setIncomeUnit(rs.getString("pim.INCOMEUNIT"));
			paymentInvoiceEpisOffline.setDepartmentArea(rs.getString("A.VALUE"));
			paymentInvoiceEpisOffline.setSegmentCode(rs.getString("pim.SEGMENT_CODE"));
			paymentInvoiceEpisOffline.setProductCode(rs.getString("pim.PRODUCT_CODE"));
			
			return paymentInvoiceEpisOffline;
		}

	}
	
	private static final class mapHistory implements RowMapper<HistoryPaymentRS> {

		@Override
		public HistoryPaymentRS mapRow(ResultSet rs, int rowNum) throws SQLException {
			HistoryPaymentRS historyPaymentRS = new HistoryPaymentRS();
			historyPaymentRS.setBeforeVat(rs.getBigDecimal("pim.BEFOR_VAT"));
			historyPaymentRS.setBranCode(rs.getString("py.BRANCH_CODE"));
			historyPaymentRS.setCustName(rs.getString("pim.CUSTOMER_NAME"));
			historyPaymentRS.setDocumentDate(rs.getDate("py.CREATE_DATE"));
			historyPaymentRS.setDocumentNo(rs.getString("py.RECEIPT_NO_MANUAL"));
			historyPaymentRS.setInvoice(rs.getString("py.INVOICE_NO"));
			historyPaymentRS.setNumberRun("");
			historyPaymentRS.setPaidAmount(rs.getBigDecimal("py.PAID_AMOUNT"));
			historyPaymentRS.setAmount(rs.getBigDecimal("py.AMOUNT"));
			historyPaymentRS.setRecordStatus(rs.getString("py.RECORD_STATUS"));
			historyPaymentRS.setTaxId(rs.getString("pim.TAXNO"));
			historyPaymentRS.setVat(rs.getBigDecimal("pim.VAT_AMOUNT"));
			historyPaymentRS.setVatAmount(rs.getBigDecimal("VAT_AMOUNTx"));
			historyPaymentRS.setVatRate(rs.getInt("pim.VAT_RATE"));
			historyPaymentRS.setCreateBy(rs.getString("py.CREATE_BY"));
			return historyPaymentRS;
		}

	}
	public static String formatDateDoc(String date) {
		String dateResult = "";
		if (date.length() == 16) {
			String docDatess = date.substring(8);
			String docDates = date.substring(0, 8);
			dateResult = docDates.substring(6) + "/" + docDates.substring(4, 6) + "/" + docDates.substring(0, 4) + "-"
					+ docDatess.substring(6) + "/" + docDatess.substring(4, 6) + "/" + docDatess.substring(0, 4);
		}
		return dateResult;
	}

}
