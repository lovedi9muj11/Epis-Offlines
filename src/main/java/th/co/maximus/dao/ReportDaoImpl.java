package th.co.maximus.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import th.co.maximus.bean.HistoryReportBean;
import th.co.maximus.bean.InvEpisOfflineOtherBean;
import th.co.maximus.bean.InvEpisOfflineReportBean;
import th.co.maximus.bean.InvPaymentOrderTaxBean;
@Repository("ReportDao")
public class ReportDaoImpl implements ReportDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	
	
	@Override
	public List<InvEpisOfflineReportBean> inqueryEpisOfflineJSONHandler(String documentNo) {
//		List<InvEpisOfflineReportBean> collections = new ArrayList<InvEpisOfflineReportBean>();
		List<InvEpisOfflineReportBean> collectionss = new ArrayList<InvEpisOfflineReportBean>();
		List<Object> param = new LinkedList<Object>();
		
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT py.BRANCH_AREA ,py.BRANCH_CODE, py.SOURCE ,py.ACCOUNT_NO , pim.CUSTOMER_NAME ,py.RECEIPT_NO_MANUAL,pim.AMOUNT ,py.INVOICE_NO,py.CREATE_DATE,pim.CUSTOMER_ADDRESS,pim.TAXNO,py.REMARK,tm.CODE ,pim.VAT_RATE,pay.VAT_AMOUNT, ");
			sql.append(" pay.BEFOR_VAT,tm.METHOD_MANUAL_ID,pay.ACCOUNTSUBNO,pay.DISCOUNT,py.AMOUNT,pim.PERIOD,py.DOCTYPE,tm.MANUAL_ID,pay.CHANG ");
			sql.append(" FROM RECEIPT_MANUAL py");
			sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL pim ON pim.MANUAL_ID = py.MANUAL_ID AND pim.INVOICE_NO = py.INVOICE_NO ");
			sql.append(" INNER JOIN TRSMETHOD_MANUAL tm ON tm.MANUAL_ID = py.MANUAL_ID");
			sql.append(" INNER JOIN PAYMENT_INVOICE pay ON pay.MANUAL_ID = py.MANUAL_ID");
			sql.append(" WHERE py.RECEIPT_NO_MANUAL = ?");
			sql.append(" GROUP BY tm.NAME ");
			
			param.add(documentNo);
			Object[] paramArr = param.toArray();
			collectionss = jdbcTemplate.query(sql.toString(), paramArr, new mapInvEpisOfflineReportBean());
			
//			PreparedStatement preparedStatement = connect.prepareStatement(sql.toString());
//			preparedStatement.setString(1, documentNo);
//			ResultSet resultSet = preparedStatement.executeQuery();
//			while (resultSet.next()) {
//				collections.add(new InvEpisOfflineReportBean(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getBigDecimal(7), 
//						resultSet.getString(8), resultSet.getDate(9), resultSet.getString(10), resultSet.getString(11), resultSet.getString(12), resultSet.getString(13), resultSet.getString(14), resultSet.getBigDecimal(15),
//						resultSet.getBigDecimal(16), resultSet.getLong(17), resultSet.getString(18), resultSet.getBigDecimal(19), resultSet.getBigDecimal(20),resultSet.getString(21),resultSet.getString(22)));		
//						
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return collectionss;
		
	}



	@Override
	public List<InvEpisOfflineOtherBean> inqueryEpisOfflineOtherJSONHandler(String documentNo ,String other){
//		List<InvEpisOfflineOtherBean> collections = new ArrayList<InvEpisOfflineOtherBean>();
		List<InvEpisOfflineOtherBean> collectionss = new ArrayList<InvEpisOfflineOtherBean>();
		List<Object> param = new LinkedList<Object>();
		
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT py.BRANCH_AREA ,py.BRANCH_CODE, pim.SERVICENAME ,py.ACCOUNT_NO , pim.CUSTOMER_NAME ,py.RECEIPT_NO_MANUAL,py.PAID_AMOUNT ,py.INVOICE_NO,py.CREATE_DATE,pim.CUSTOMER_ADDRESS,pim.TAXNO,py.REMARK,tm.CODE ,pim.VAT_RATE,pim.DISCOUNTSPECIAL,pim.AMOUNT,pim.DISCOUNTBEFORVAT,tm.METHOD_MANUAL_ID,pim.MANUAL_ID,");
			sql.append(" py.DOCTYPE");
			sql.append(" FROM RECEIPT_MANUAL py");
			sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL pim ON pim.MANUAL_ID = py.MANUAL_ID ");
			sql.append(" INNER JOIN TRSMETHOD_MANUAL tm ON tm.MANUAL_ID = py.MANUAL_ID");
			sql.append(" WHERE py.RECEIPT_NO_MANUAL = ?");
			
			if(other.equals("OTHER")) {
				sql.append(" GROUP BY method_manual_id ");
			}
			
			param.add(documentNo);
			Object[] paramArr = param.toArray();
			collectionss = jdbcTemplate.query(sql.toString(), paramArr, new mapInvEpisOfflineOtherBean());
			
//			PreparedStatement preparedStatement = connect.prepareStatement(sql.toString());
//			preparedStatement.setString(1, documentNo);
//			ResultSet resultSet = preparedStatement.executeQuery();
//			while (resultSet.next()) {
//				collections.add(new InvEpisOfflineOtherBean(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getBigDecimal(7), 
//						resultSet.getString(8), resultSet.getDate(9), resultSet.getString(10), resultSet.getString(11), resultSet.getString(12), resultSet.getString(13), resultSet.getString(14),resultSet.getBigDecimal(15),resultSet.getBigDecimal(16),resultSet.getBigDecimal(17),resultSet.getLong(18),resultSet.getLong(19),
//						resultSet.getString(20)));		
//						
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return collectionss;
	}



	@Override
	public List<InvPaymentOrderTaxBean> inqueryInvPaymentOrderTaxBeanJSONHandler(HistoryReportBean creteria) {
//		List<InvPaymentOrderTaxBean> collections = new ArrayList<InvPaymentOrderTaxBean>();
		List<InvPaymentOrderTaxBean> collectionss = new ArrayList<InvPaymentOrderTaxBean>();
		List<Object> param = new LinkedList<Object>();
		
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT py.CREATE_DATE ,py.RECEIPT_NO_MANUAL,pim.CUSTOMER_NAME,py.CREATE_BY, pim.TAXNO ,py.INVOICE_NO , py.BRANCH_AREA, py.BRANCH_CODE ,py.PAID_AMOUNT,py.RECORD_STATUS,pim.VAT_RATE, py.VAT_AMOUNT, py.AMOUNT");
			sql.append(" FROM RECEIPT_MANUAL py");
			sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL pim ON pim.MANUAL_ID = py.MANUAL_ID ");
			sql.append(" WHERE 1=1 ");
			sql.append(" and py.DOCTYPE IN( ?,'TX') ");
//			sql.append(" and pim.SERVICE_TYPE = ? ");
			if(StringUtils.isNoneEmpty(creteria.getDateFrom())) {
				
				String dateFrom = convertDateString(creteria.getDateFrom())+ " " + creteria.getDateFromHour()+ ":"+creteria.getDateFromMinute() +":"+"00"+":" +"000000"; 
				sql.append(" AND py.CREATE_DATE >= '").append(" "+dateFrom+" ' ");
				
			}
			if(StringUtils.isNoneEmpty(creteria.getDateTo())) {
				String dateTo = convertDateString(creteria.getDateTo())+ " " + creteria.getDateToHour()+ ":"+creteria.getDateToMinute() +":"+"59"+":" +"999999"; 
				sql.append(" AND py.CREATE_DATE <= ' ").append(" "+dateTo+" ' ");
			}
			
			param.add(creteria.getTypePrint());
			if(StringUtils.isNotEmpty(creteria.getUnserLogin())) {
				sql.append(" and pim.CREATE_BY = ? ");
				
				param.add(creteria.getUnserLogin());
			}
			
			sql.append(" group by py.RECEIPT_NO_MANUAL ");
			sql.append(" ORDER BY py.RECEIPT_NO_MANUAL ASC ");
			
//			param.add(creteria.getFlagPage());
			Object[] paramArr = param.toArray();
			collectionss = jdbcTemplate.query(sql.toString(), paramArr, new mapInvPaymentOrderTaxBean());

//			PreparedStatement preparedStatement = connect.prepareStatement(sql.toString());
//			preparedStatement.setString(1, creteria.getTypePrint());
//			ResultSet resultSet = preparedStatement.executeQuery();
//			while (resultSet.next()) {
//				//collections.add(new HistoryPaymen)	;
//				collections.add(new InvPaymentOrderTaxBean(resultSet.getDate(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getString(8), resultSet.getBigDecimal(9), resultSet.getString(10), resultSet.getInt(11)));
//						
//			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return collectionss;
	}
	
	private static final class mapInvEpisOfflineReportBean implements RowMapper<InvEpisOfflineReportBean> {

		@Override
		public InvEpisOfflineReportBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			InvEpisOfflineReportBean invEpisOfflineReportBean = new InvEpisOfflineReportBean();
			invEpisOfflineReportBean.setBranArea(rs.getString("py.BRANCH_AREA"));
			invEpisOfflineReportBean.setBracnCode(rs.getString("py.BRANCH_CODE"));
			invEpisOfflineReportBean.setSouce(rs.getString("py.SOURCE"));
			invEpisOfflineReportBean.setCustNo(rs.getString("py.ACCOUNT_NO"));
			invEpisOfflineReportBean.setCustName(rs.getString("pim.CUSTOMER_NAME"));
			invEpisOfflineReportBean.setDocumentNo(rs.getString("py.RECEIPT_NO_MANUAL"));
			invEpisOfflineReportBean.setBalanceSummary(rs.getBigDecimal("pim.AMOUNT"));
			invEpisOfflineReportBean.setInvoiceNo(rs.getString("py.INVOICE_NO"));
			invEpisOfflineReportBean.setDocumentDate(rs.getDate("py.CREATE_DATE"));
			invEpisOfflineReportBean.setCustomerAddress(rs.getString("pim.CUSTOMER_ADDRESS"));
			invEpisOfflineReportBean.setTaxId(rs.getString("pim.TAXNO"));
			invEpisOfflineReportBean.setRemark(rs.getString("py.REMARK"));
			invEpisOfflineReportBean.setPaymentCode(rs.getString("tm.CODE"));
			invEpisOfflineReportBean.setVatRate(rs.getString("pim.VAT_RATE"));
			invEpisOfflineReportBean.setVat(rs.getBigDecimal("pay.VAT_AMOUNT"));
			invEpisOfflineReportBean.setBeforeVat(rs.getBigDecimal("pay.BEFOR_VAT"));
			invEpisOfflineReportBean.setMethodId(rs.getLong("tm.METHOD_MANUAL_ID"));
			invEpisOfflineReportBean.setServiceNo(rs.getString("pay.ACCOUNTSUBNO"));
			invEpisOfflineReportBean.setDiscount(rs.getBigDecimal("pay.DISCOUNT"));
			invEpisOfflineReportBean.setAmountPayment(rs.getBigDecimal("py.AMOUNT").subtract(rs.getBigDecimal("pay.CHANG")));
			invEpisOfflineReportBean.setPreiod(rs.getString("pim.PERIOD"));
			invEpisOfflineReportBean.setDocType(rs.getString("py.DOCTYPE"));
			invEpisOfflineReportBean.setManualId(rs.getString("tm.MANUAL_ID"));
			return invEpisOfflineReportBean;
		}

	}
	
	private static final class mapInvEpisOfflineOtherBean implements RowMapper<InvEpisOfflineOtherBean> {

		@Override
		public InvEpisOfflineOtherBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			InvEpisOfflineOtherBean invEpisOfflineOtherBean = new InvEpisOfflineOtherBean();
			invEpisOfflineOtherBean.setBranArea(rs.getString("py.BRANCH_AREA"));
			invEpisOfflineOtherBean.setBracnCode(rs.getString("py.BRANCH_CODE"));
			invEpisOfflineOtherBean.setCustNo(rs.getString("py.ACCOUNT_NO"));
			invEpisOfflineOtherBean.setCustName(rs.getString("pim.CUSTOMER_NAME"));
			invEpisOfflineOtherBean.setDocumentNo(rs.getString("py.RECEIPT_NO_MANUAL"));
			invEpisOfflineOtherBean.setBalanceSummary(rs.getBigDecimal("py.PAID_AMOUNT"));
			invEpisOfflineOtherBean.setInvoiceNo(rs.getString("py.INVOICE_NO"));
			invEpisOfflineOtherBean.setDocumentDate(rs.getDate("py.CREATE_DATE"));
			invEpisOfflineOtherBean.setCustomerAddress(rs.getString("pim.CUSTOMER_ADDRESS"));
			invEpisOfflineOtherBean.setTaxId(rs.getString("pim.TAXNO"));
			invEpisOfflineOtherBean.setRemark(rs.getString("py.REMARK"));
			invEpisOfflineOtherBean.setPaymentCode(rs.getString("tm.CODE"));
			invEpisOfflineOtherBean.setVatRate(rs.getString("pim.VAT_RATE"));
			invEpisOfflineOtherBean.setMethodId(rs.getLong("tm.METHOD_MANUAL_ID"));
			invEpisOfflineOtherBean.setServiceName(rs.getString("pim.SERVICENAME"));
			invEpisOfflineOtherBean.setDiscountSpecial(rs.getBigDecimal("pim.DISCOUNTSPECIAL"));
			invEpisOfflineOtherBean.setAmount(rs.getBigDecimal("pim.AMOUNT"));
			invEpisOfflineOtherBean.setDiscountbeforvat(rs.getBigDecimal("pim.DISCOUNTBEFORVAT"));
			invEpisOfflineOtherBean.setManualId(rs.getLong("pim.MANUAL_ID"));
			invEpisOfflineOtherBean.setDoctype(rs.getString("py.DOCTYPE"));
			return invEpisOfflineOtherBean;
		}

	}
	
	private static final class mapInvPaymentOrderTaxBean implements RowMapper<InvPaymentOrderTaxBean> {

		@Override
		public InvPaymentOrderTaxBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			InvPaymentOrderTaxBean invPaymentOrderTaxBean = new InvPaymentOrderTaxBean();
			invPaymentOrderTaxBean.setDocumentDate(rs.getDate("py.CREATE_DATE"));
			invPaymentOrderTaxBean.setDocumentNo(rs.getString("py.RECEIPT_NO_MANUAL"));
			invPaymentOrderTaxBean.setCustName(rs.getString("pim.CUSTOMER_NAME"));
			invPaymentOrderTaxBean.setEmpName(rs.getString("py.CREATE_BY"));
			invPaymentOrderTaxBean.setTaxId(rs.getString("pim.TAXNO"));
			invPaymentOrderTaxBean.setInvoiceNo(rs.getString("py.INVOICE_NO"));
			invPaymentOrderTaxBean.setBranchArea(rs.getString("py.BRANCH_AREA"));
			invPaymentOrderTaxBean.setBranchCode(rs.getString("py.BRANCH_CODE"));
			invPaymentOrderTaxBean.setSummary(rs.getBigDecimal("py.PAID_AMOUNT"));
			invPaymentOrderTaxBean.setPayType(rs.getString("py.RECORD_STATUS"));
			invPaymentOrderTaxBean.setVatRate(rs.getInt("pim.VAT_RATE")+"");
			invPaymentOrderTaxBean.setVatAmount(rs.getBigDecimal("py.VAT_AMOUNT"));
			invPaymentOrderTaxBean.setAmount(rs.getBigDecimal("py.AMOUNT"));
			return invPaymentOrderTaxBean;
		}

	}

	public static final String convertDateString(String str) {
		return str.replaceAll("([0-9]{2})/([0-9]{2})/([0-9]{4})", "$3-$2-$1");

	}



	@Override
	public List<InvPaymentOrderTaxBean> summarryVay(HistoryReportBean creteria ,boolean groupby) {
//		List<InvPaymentOrderTaxBean> collectionss = new ArrayList<InvPaymentOrderTaxBean>();
		List<Object> param = new LinkedList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT  VAT_RATE, SUM(pim.VAT_AMOUNT) as SUM_VAT_AMOUNT, ");
		sql.append(" SUM(pim.BEFOR_VAT) as SUM_BEFOR_VAT, SUM(pim.AMOUNT) as SUM_AMOUNT ");
		sql.append(" FROM RECEIPT_MANUAL py ");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL pim ON pim.MANUAL_ID = py.MANUAL_ID ");
		sql.append(" WHERE  ");
		sql.append(" py.DOCTYPE = ? ");
		param.add(creteria.getTypePrint());
		if(StringUtils.isNoneEmpty(creteria.getDateFrom())) {
			
			String dateFrom = convertDateString(creteria.getDateFrom())+ " " + creteria.getDateFromHour()+ ":"+creteria.getDateFromMinute() +":"+"00"+":" +"000000"; 
			sql.append(" AND py.CREATE_DATE >= '").append(" "+dateFrom+" ' ");
			
		}
		if(StringUtils.isNoneEmpty(creteria.getDateTo())) {
			String dateTo = convertDateString(creteria.getDateTo())+ " " + creteria.getDateToHour()+ ":"+creteria.getDateToMinute() +":"+"59"+":" +"999999"; 
			sql.append(" AND py.CREATE_DATE <= ' ").append(" "+dateTo+" ' ");
		}
		
		if(groupby) {
			sql.append(" GROUP BY VAT_RATE  ");
		}

	
		Object[] paramArr = param.toArray();
		return 	 jdbcTemplate.query(sql.toString(), paramArr, new mapInvEpisOfflineReportBeanSummarry());
	} 
	private static final class mapInvEpisOfflineReportBeanSummarry implements RowMapper<InvPaymentOrderTaxBean> {

		@Override
		public InvPaymentOrderTaxBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			InvPaymentOrderTaxBean invEpisOfflineReportBean = new InvPaymentOrderTaxBean();
			invEpisOfflineReportBean.setVatRate(rs.getInt("VAT_RATE")+"");
			invEpisOfflineReportBean.setBeforeVatSummary(rs.getBigDecimal("SUM_BEFOR_VAT"));
			invEpisOfflineReportBean.setVatSummary(rs.getBigDecimal("SUM_VAT_AMOUNT"));
			invEpisOfflineReportBean.setSummarySummary(rs.getBigDecimal("SUM_AMOUNT"));
		
			return invEpisOfflineReportBean;
		}

	}

	@Override
	public List<InvEpisOfflineOtherBean> inqueryEpisOfflineOtherJSONHandler(String documentNo) {
		// TODO Auto-generated method stub
		return null;
	}
}
