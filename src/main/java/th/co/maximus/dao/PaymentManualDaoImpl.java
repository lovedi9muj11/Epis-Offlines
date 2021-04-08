package th.co.maximus.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import th.co.maximus.auth.model.UserRole;
import th.co.maximus.bean.PaymentManualBean;
import th.co.maximus.bean.ReportPaymentBean;
import th.co.maximus.bean.ReportPaymentCriteria;
import th.co.maximus.constants.Constants;
import th.co.maximus.model.ReceiptOfflineModel;
import th.co.maximus.payment.bean.PaymentResultReq;

@Repository("PaymentManualDao")
public class PaymentManualDaoImpl implements PaymentManualDao {


	@Autowired
	private JdbcTemplate jdbcTemplate;

	Locale localEn = new Locale("en", "EN");
	SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DateTime.DB_DATE_FORMAT, localEn);
	SimpleDateFormat dateFormatClearing = new SimpleDateFormat(Constants.DateTime.DB_DATE_FORMAT.concat(" "+Constants.DateTime.TIME_FORMAT), localEn);

	@Override
	public int insertPayment(PaymentManualBean paymentManualBean) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO RECEIPT_MANUAL (INVOICE_NO, RECEIPT_NO_MANUAL, PAID_DATE, BRANCH_AREA, BRANCH_CODE,PAID_AMOUNT,SOURCE,CLEARING,REMARK,CREATE_BY,CREATE_DATE,UPDATE_BY,UPDATE_DATE,RECORD_STATUS,ACCOUNT_NO,PAY_TYPE,DOCTYPE,CHANG,AMOUNT,VAT_AMOUNT,CUSTOMER_GROUP, CANCEL_DATE, CLEARING_DATE)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql, new String[] { "MANUAL_ID" });
				pst.setString(1, paymentManualBean.getInvoiceNo());
				pst.setString(2, paymentManualBean.getReceiptNoManual());
				pst.setTimestamp(3, paymentManualBean.getPaidDate());
				pst.setString(4, paymentManualBean.getBrancharea());
				pst.setString(5, paymentManualBean.getBranchCode());
				pst.setDouble(6, paymentManualBean.getPaidAmount());
				pst.setString(7, paymentManualBean.getSource());
				pst.setString(8, paymentManualBean.getClearing());
				pst.setString(9, paymentManualBean.getRemark());
				pst.setString(10, paymentManualBean.getCreateBy());
				pst.setTimestamp(11, paymentManualBean.getCreateDate());
				pst.setString(12, paymentManualBean.getUpdateBy());
				pst.setTimestamp(13, paymentManualBean.getUpdateDate());
				pst.setString(14, paymentManualBean.getRecordStatus());
				pst.setString(15, paymentManualBean.getAccountNo());
				pst.setString(16, paymentManualBean.getPaytype());
				pst.setString(17, paymentManualBean.getDocType());
				pst.setDouble(18, paymentManualBean.getChange());
				pst.setBigDecimal(19, paymentManualBean.getAmount());
				// pst.setInt(20, paymentManualBean.getVatRate());
				pst.setBigDecimal(20, paymentManualBean.getVatAmount());
				pst.setString(21, paymentManualBean.getCustomerGroup());
				pst.setTimestamp(22, null);
				pst.setTimestamp(23, null);
				return pst;
			}
		}, keyHolder);
		int newUserId = keyHolder.getKey().intValue();
		return newUserId;
	}

	@Override
	public PaymentResultReq findById(int id) throws Exception {

		PaymentResultReq beanReReq = new PaymentResultReq();
		StringBuilder sqlStmt = new StringBuilder();
		sqlStmt.append(
				" SELECT py.ACCOUNT_NO , pim.CUSTOMER_NAME ,py.RECEIPT_NO_MANUAL,py.PAID_AMOUNT ,py.INVOICE_NO,tmp.INVOICE_DATE,py.PAID_DATE ,  SUM(pim.BEFOR_VAT) , SUM(pim.VAT_AMOUNT) ,SUM(pim.AMOUNT), (SELECT SUM(dud.AMOUNT) FROM DEDUCTION_MANUAL dud WHERE dud.MANUAL_ID = py.MANUAL_ID AND dud.INVOICE_NO = py.INVOICE_NO GROUP BY dud.INVOICE_NO ) , py.PAID_AMOUNT , pim.PERIOD,pim.AMOUNT,tmp.DISCOUNT,py.AMOUNT, tmp.DATE_LINE ");
		sqlStmt.append(" FROM RECEIPT_MANUAL py ");
		sqlStmt.append(
				" INNER JOIN PAYMENT_INVOICE_MANUAL pim ON pim.MANUAL_ID =  py.MANUAL_ID AND pim.INVOICE_NO = py.INVOICE_NO ");
		sqlStmt.append(
				" INNER JOIN PAYMENT_INVOICE tmp ON tmp.MANUAL_ID =  py.MANUAL_ID AND tmp.INVOICE_NO = py.INVOICE_NO ");
		sqlStmt.append(" WHERE  py.MANUAL_ID = ? ");

		beanReReq = jdbcTemplate.query(sqlStmt.toString(), new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setInt(1, id);
			}
		}, new ResultSetExtractor<PaymentResultReq>() {
			public PaymentResultReq extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					return new PaymentResultReq(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3),
							resultSet.getBigDecimal(4), resultSet.getString(5), resultSet.getDate(6),
							resultSet.getTimestamp(7), resultSet.getBigDecimal(8), resultSet.getBigDecimal(9),
							resultSet.getBigDecimal(10), resultSet.getBigDecimal(11), resultSet.getBigDecimal(12),
							resultSet.getString(13), resultSet.getBigDecimal(14), resultSet.getBigDecimal(15),
							resultSet.getBigDecimal(16),resultSet.getDate(17));

				}
				return null;
			}
		});

		return beanReReq;
	}

	@Override
	public List<PaymentManualBean> findPaymentManualFromNanualId(long manualId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM RECEIPT_MANUAL payment_m  ");
		sql.append(" WHERE payment_m.MANUAL_ID =  ");
		sql.append(manualId);
		return jdbcTemplate.query(sql.toString(), new PaymentManual());
	}

	private static final class PaymentManual implements RowMapper<PaymentManualBean> {

		@Override
		public PaymentManualBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			PaymentManualBean paymentManual = new PaymentManualBean();
			paymentManual.setManualId(rs.getLong("MANUAL_ID"));
			paymentManual.setPaymentId(rs.getLong("PAYMENT_ID"));
			paymentManual.setInvoiceNo(rs.getString("INVOICE_NO"));
			paymentManual.setReceiptNoManual(rs.getString("RECEIPT_NO_MANUAL"));
			paymentManual.setPaidDate(rs.getTimestamp("PAID_DATE"));
			paymentManual.setBrancharea(rs.getString("BRANCH_AREA"));
			paymentManual.setBranchCode(rs.getString("BRANCH_CODE"));
			paymentManual.setPaidAmount(rs.getLong("PAID_AMOUNT"));
			paymentManual.setSource(rs.getString("SOURCE"));
			paymentManual.setClearing(rs.getString("CLEARING"));
			paymentManual.setRemark(rs.getString("REMARK"));
			paymentManual.setCreateBy(rs.getString("CREATE_BY"));
			paymentManual.setCreateDate(rs.getTimestamp("CREATE_DATE"));
			paymentManual.setUpdateBy(rs.getString("UPDATE_BY"));
			paymentManual.setUpdateDate(rs.getTimestamp("UPDATE_DATE"));
			paymentManual.setRecordStatus(
					rs.getString("RECORD_STATUS").equals(Constants.Status.ACTIVE) ? Constants.Status.ACTIVE_A
							: Constants.Status.ACTIVE_AC);
			paymentManual.setRefid(rs.getLong("REF_ID"));
			paymentManual.setAccountNo(rs.getString("ACCOUNT_NO"));
			paymentManual.setPaytype(rs.getString("PAY_TYPE"));
			paymentManual.setDocType(rs.getString("DOCTYPE"));
			paymentManual.setChange(rs.getDouble("CHANG"));
			paymentManual.setAmount(rs.getBigDecimal("AMOUNT"));
			// paymentManual.setVatRate(rs.getInt("VAT_RATE"));
			paymentManual.setVatAmount(rs.getBigDecimal("VAT_AMOUNT"));
			paymentManual.setCancelReason(rs.getString("CANCEL_REASON"));
			return paymentManual;
		}

	}

	@Override
	public List<ReportPaymentBean> getReportPayment(ReportPaymentCriteria criteria,String serviceType) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT PM.*,PIM.*, m.value branch_name, (SELECT VALUE FROM MASTER_DATA WHERE KEYCODE = 'POS_NAME') pos_name ");
		sql.append(" FROM RECEIPT_MANUAL PM ");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL PIM ON PM.MANUAL_ID = PIM.MANUAL_ID ");
		sql.append(" LEFT JOIN MASTER_DATA m ON PM.BRANCH_AREA = m.KEYCODE ");
//		sql.append(" WHERE PM.CREATE_DATE >=").append("'" + dateFormat.format(new Date())+" "+criteria.getDateFrom() + "'");
		sql.append(" WHERE PM.CREATE_DATE >=").append("'" + criteria.getDateFrom() + "'");
//		sql.append("  AND PM.CREATE_DATE <= ").append("'" + dateFormat.format(new Date())+" "+criteria.getDateTo() + "'");
		sql.append("  AND PM.CREATE_DATE <= ").append("'" + criteria.getDateTo2()+" "+criteria.getDateTo() + "'");
		
//		sql.append(" WHERE PM.CREATE_DATE >=").append("'" + criteria.getDateFrom() + "'");
//		sql.append("  AND PM.CREATE_DATE <= ").append("'" + criteria.getDateTo() + "'");
		
		if (!"".equals(criteria.getUser()) && criteria.getUser() != null) {
			sql.append(" AND PM.CREATE_BY = ").append("'" + criteria.getUser() + "'");
		}
//			sql.append(" AND PIM.SERVICE_TYPE = ").append("'" + serviceType + "'");
		if(StringUtils.isNoneEmpty(serviceType)) {
			sql.append(" AND PIM.SERVICE_TYPE = ").append("'" + serviceType + "'");
			
			if(Constants.Service.SERVICE_TYPE_OTHER.equals(serviceType)) {
				sql.append(" AND PM.DOCTYPE NOT IN ('"+Constants.DOCTYPE.TX+"')");
			}
		}
		
//		sql.append(" GROUP BY PM.RECEIPT_NO_MANUAL ORDER BY PM.DOCTYPE, PM.CREATE_DATE");
		sql.append(" ORDER BY PM.DOCTYPE, PM.CREATE_DATE");
		return jdbcTemplate.query(sql.toString(), new reportPaymentMapper());
	}
	@Override
	public List<ReportPaymentBean> getReportPaymentPDF(ReportPaymentCriteria criteria,String serviceType) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT PM.*,PIM.*, m.value branch_name, (SELECT VALUE FROM MASTER_DATA WHERE KEYCODE = 'POS_NAME') pos_name, ");
		sql.append(" (SELECT VALUE FROM MASTER_DATA WHERE KEYCODE = PM.BRANCH_AREA and property_2 = PIM.DEPARTMENT ) department_name ");
		sql.append(" FROM RECEIPT_MANUAL PM ");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL PIM ON PM.MANUAL_ID = PIM.MANUAL_ID ");
		sql.append(" LEFT JOIN MASTER_DATA m ON PM.BRANCH_AREA = m.KEYCODE ");
//		sql.append(" WHERE PM.CREATE_DATE >=").append("'" + dateFormat.format(new Date())+" "+criteria.getDateFrom() + "'");
		sql.append(" WHERE PM.CREATE_DATE >=").append("'" + criteria.getDateFrom() + "'");
//		sql.append("  AND PM.CREATE_DATE <= ").append("'" + dateFormat.format(new Date())+" "+criteria.getDateTo() + "'");
		sql.append("  AND PM.CREATE_DATE <= ").append("'" + criteria.getDateTo2()+" "+criteria.getDateTo() + "'");
		
//		sql.append(" WHERE PM.CREATE_DATE >=").append("'" + criteria.getDateFrom() + "'");
//		sql.append("  AND PM.CREATE_DATE <= ").append("'" + criteria.getDateTo() + "'");
		
		if (!"".equals(criteria.getUser()) && criteria.getUser() != null) { sql.append(" AND PM.CREATE_BY = ").append("'" + criteria.getUser() + "'"); }
			sql.append(" AND PIM.SERVICE_TYPE = ").append("'" + serviceType + "'");
			
//			sql.append(" AND PM.DOCTYPE = ").append("'" + serviceType + "'");
		
		if(!Constants.Service.SERVICE_TYPE_OTHER.equals(serviceType)) { sql.append(" GROUP BY PM.RECEIPT_NO_MANUAL "); }
		else { sql.append(" AND PM.DOCTYPE NOT IN ('"+Constants.DOCTYPE.TX+"')"); }
		
		sql.append(" ORDER BY PM.DOCTYPE, PM.CREATE_DATE ");
		return jdbcTemplate.query(sql.toString(), new reportPaymentMapper1());
	}
	@Override
	public List<ReportPaymentBean> getReportPaymentOther(ReportPaymentCriteria criteria,String serviceType) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT PM.*,PIM.*, m.value branch_name, (SELECT VALUE FROM MASTER_DATA WHERE KEYCODE = 'POS_NAME') pos_name ");
		sql.append(" FROM RECEIPT_MANUAL PM ");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL PIM ON PM.MANUAL_ID = PIM.MANUAL_ID ");
		sql.append(" LEFT JOIN MASTER_DATA m ON PM.BRANCH_AREA = m.KEYCODE ");
//		sql.append(" WHERE PM.CREATE_DATE >=").append("'" + dateFormat.format(new Date())+" "+criteria.getDateFrom() + "'");
		sql.append(" WHERE PM.CREATE_DATE >=").append("'" + criteria.getDateFrom() + "'");
//		sql.append("  AND PM.CREATE_DATE <= ").append("'" + dateFormat.format(new Date())+" "+criteria.getDateTo() + "'");
		sql.append("  AND PM.CREATE_DATE <= ").append("'" + criteria.getDateTo2()+" "+criteria.getDateTo() + "'");
		
		if (!"".equals(criteria.getUser()) && criteria.getUser() != null) {
			sql.append(" AND PM.CREATE_BY = ").append("'" + criteria.getUser() + "'");
		}
		sql.append(" AND PIM.SERVICE_TYPE = ").append("'" + serviceType + "'");
		sql.append(" AND PM.DOCTYPE NOT IN ('"+Constants.DOCTYPE.TX+"')");
		
//		sql.append(" GROUP BY PM.RECEIPT_NO_MANUAL ");
		sql.append(" ORDER BY  PIM.SERVICECODE, PM.RECEIPT_NO_MANUAL, PIM.DEPARTMENT,  PM.CREATE_DATE ");
		return jdbcTemplate.query(sql.toString(), new reportPaymentMapper());
	}

	private static final class reportPaymentMapper implements RowMapper<ReportPaymentBean> {

		@Override
		public ReportPaymentBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReportPaymentBean reportPayment = new ReportPaymentBean();
			reportPayment.setManualId(rs.getLong("MANUAL_ID"));
			reportPayment.setServiceType(rs.getString("SERVICE_TYPE"));
			reportPayment.setReceiptNoManual(rs.getString("RECEIPT_NO_MANUAL"));
			reportPayment.setAccountSubNo(rs.getString("ACCOUNT_NO"));
			reportPayment.setCustomerName(rs.getString("CUSTOMER_NAME"));
			reportPayment.setDepartment(rs.getString("DEPARTMENT"));
			reportPayment.setInvoiceNo(rs.getString("INVOICE_NO"));
			reportPayment.setServiceName(rs.getString("SERVICENAME"));
			reportPayment.setServiceCode(rs.getString("SERVICECODE"));
			reportPayment.setCreateBy(rs.getString("CREATE_BY"));
			reportPayment.setCreateDate(rs.getTimestamp("CREATE_DATE"));
			reportPayment.setRemake(rs.getString("REMARK"));
			// reportPayment.setNoRefer(rs.getString(""));
			reportPayment.setBeforVat(rs.getBigDecimal("AMOUNT").subtract(rs.getBigDecimal("VAT_AMOUNT")).setScale(2, BigDecimal.ROUND_HALF_UP));
			reportPayment.setBeforVatOther(rs.getBigDecimal("PIM.AMOUNT").subtract(rs.getBigDecimal("PIM.VAT_AMOUNT")).setScale(2, BigDecimal.ROUND_HALF_UP));
			reportPayment.setAmount(rs.getBigDecimal("AMOUNT"));
			reportPayment.setAmountOther(rs.getBigDecimal("PIM.AMOUNT"));
			reportPayment.setVatAmount(rs.getBigDecimal("VAT_AMOUNT"));
			reportPayment.setVatAmountOther(rs.getBigDecimal("PIM.VAT_AMOUNT"));
			reportPayment.setStatus(rs.getString("RECORD_STATUS"));
			reportPayment.setCancelReason(rs.getString("CANCEL_REASON"));
			reportPayment.setVatRate(rs.getString("VAT_RATE"));
			reportPayment.setBranchName(rs.getString("BRANCH_NAME"));
			reportPayment.setBranchCode(rs.getString("BRANCH_AREA"));
			reportPayment.setPosName(rs.getString("POS_NAME"));
			reportPayment.setDepartmentArea(rs.getString("DEPARTMENT_AREA"));
			reportPayment.setGlCode(rs.getString("GL_CODE"));
			return reportPayment;
		}

	}
	
	private static final class reportPaymentMapper1 implements RowMapper<ReportPaymentBean> {
		
		@Override
		public ReportPaymentBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			ReportPaymentBean reportPayment = new ReportPaymentBean();
			reportPayment.setManualId(rs.getLong("MANUAL_ID"));
			reportPayment.setServiceType(rs.getString("SERVICE_TYPE"));
			reportPayment.setReceiptNoManual(rs.getString("RECEIPT_NO_MANUAL"));
			reportPayment.setAccountSubNo(rs.getString("ACCOUNT_NO"));
			reportPayment.setCustomerName(rs.getString("CUSTOMER_NAME"));
			reportPayment.setDepartment(rs.getString("DEPARTMENT"));
			reportPayment.setInvoiceNo(rs.getString("INVOICE_NO"));
			reportPayment.setServiceName(rs.getString("SERVICENAME"));
			reportPayment.setServiceCode(rs.getString("SERVICECODE"));
			reportPayment.setCreateBy(rs.getString("CREATE_BY"));
			reportPayment.setCreateDate(rs.getTimestamp("CREATE_DATE"));
			reportPayment.setRemake(rs.getString("REMARK"));
			// reportPayment.setNoRefer(rs.getString(""));
			reportPayment.setBeforVat(rs.getBigDecimal("AMOUNT").subtract(rs.getBigDecimal("VAT_AMOUNT")).setScale(2, BigDecimal.ROUND_HALF_UP));
			reportPayment.setBeforVatOther(rs.getBigDecimal("PIM.AMOUNT").subtract(rs.getBigDecimal("PIM.VAT_AMOUNT")).setScale(2, BigDecimal.ROUND_HALF_UP));
			reportPayment.setAmount(rs.getBigDecimal("AMOUNT"));
			reportPayment.setAmountOther(rs.getBigDecimal("PIM.AMOUNT"));
			reportPayment.setVatAmount(rs.getBigDecimal("VAT_AMOUNT"));
			reportPayment.setVatAmountOther(rs.getBigDecimal("PIM.VAT_AMOUNT"));
			reportPayment.setStatus(rs.getString("RECORD_STATUS"));
			reportPayment.setCancelReason(rs.getString("CANCEL_REASON"));
			reportPayment.setVatRate(rs.getString("VAT_RATE"));
			reportPayment.setBranchName(rs.getString("BRANCH_NAME"));
			reportPayment.setBranchCode(rs.getString("BRANCH_AREA"));
			reportPayment.setPosName(rs.getString("POS_NAME"));
			reportPayment.setDepartmentName(rs.getString("department_name"));
			reportPayment.setDepartmentArea(rs.getString("DEPARTMENT_AREA"));
			return reportPayment;
		}
		
	}

	@Override
	public ReceiptOfflineModel findByManualId(long manualId) throws SQLException {

		ReceiptOfflineModel beanReReq = new ReceiptOfflineModel();

		StringBuilder sqlStmt = new StringBuilder();
		sqlStmt.append(
				"SELECT py.INVOICE_NO , py.RECEIPT_NO_MANUAL ,py.PAID_DATE,py.BRANCH_AREA ,py.BRANCH_CODE,py.PAID_AMOUNT,py.SOURCE,py.REMARK,py.ACCOUNT_NO,py.MANUAL_ID,py.AMOUNT ,py.VAT_AMOUNT , py.CREATE_BY , py.CREATE_DATE , py.UPDATE_DATE , py.CANCEL_BY , py.CUSTOMER_GROUP ");
		sqlStmt.append(" FROM RECEIPT_MANUAL py ");
		sqlStmt.append(" WHERE  py.MANUAL_ID = ? AND py.CLEARING = 'N' AND py.RECORD_STATUS = 'A' ");
		beanReReq = jdbcTemplate.query(sqlStmt.toString(), new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setLong(1, manualId);
			}
		}, new ResultSetExtractor<ReceiptOfflineModel>() {
			public ReceiptOfflineModel extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					return new ReceiptOfflineModel(resultSet.getString(1), resultSet.getString(2), resultSet.getDate(3),
							resultSet.getString(4), resultSet.getString(5), resultSet.getBigDecimal(6),
							resultSet.getString(7), resultSet.getString(8), resultSet.getString(9),
							resultSet.getString(10),resultSet.getBigDecimal(11),resultSet.getBigDecimal(12),resultSet.getString(13),resultSet.getDate(14),resultSet.getDate(15),resultSet.getString(16),resultSet.getString(17));
				}

				return null;
			}
		});

		return beanReReq;
	}
	@Override
	public ReceiptOfflineModel findByManualId(long manualId, String status) throws SQLException {

		ReceiptOfflineModel beanReReq = new ReceiptOfflineModel();

		StringBuilder sqlStmt = new StringBuilder();
		sqlStmt.append(
				"SELECT py.INVOICE_NO , py.RECEIPT_NO_MANUAL ,py.PAID_DATE,py.BRANCH_AREA ,py.BRANCH_CODE,py.PAID_AMOUNT,py.SOURCE,py.REMARK,py.ACCOUNT_NO,py.MANUAL_ID,py.AMOUNT ,py.VAT_AMOUNT , py.CREATE_BY , py.CREATE_DATE , py.UPDATE_DATE , py.CANCEL_BY , py.CUSTOMER_GROUP ");
		sqlStmt.append(" FROM RECEIPT_MANUAL py ");
		sqlStmt.append(" WHERE  py.MANUAL_ID = ? AND py.CLEARING = 'N' AND py.RECORD_STATUS = ? ");
		beanReReq = jdbcTemplate.query(sqlStmt.toString(), new PreparedStatementSetter() {
			public void setValues(PreparedStatement preparedStatement) throws SQLException {
				preparedStatement.setLong(1, manualId);
				preparedStatement.setString(2, status);
				
			}
		}, new ResultSetExtractor<ReceiptOfflineModel>() {
			public ReceiptOfflineModel extractData(ResultSet resultSet) throws SQLException, DataAccessException {
				if (resultSet.next()) {
					return new ReceiptOfflineModel(resultSet.getString(1), resultSet.getString(2), new java.util.Date(resultSet.getTimestamp(3).getTime()),
							resultSet.getString(4), resultSet.getString(5), resultSet.getBigDecimal(6),
							resultSet.getString(7), resultSet.getString(8), resultSet.getString(9),
							resultSet.getString(10),resultSet.getBigDecimal(11),resultSet.getBigDecimal(12),resultSet.getString(13), new java.util.Date(resultSet.getTimestamp(14).getTime()),new java.util.Date(resultSet.getTimestamp(15).getTime()),resultSet.getString(16),resultSet.getString(17));
				}

				return null;
			}
		});

		return beanReReq;
	}

	@Override
	public void udpateStatus(long manualId ,String status) throws SQLException {

		StringBuilder sqlStmt = new StringBuilder();
		List<Object> param = new LinkedList<Object>();

		sqlStmt.append("UPDATE RECEIPT_MANUAL py SET  py.CLEARING = ?, py.CLEARING_DATE = ? ");
		param.add(status);
		param.add(dateFormatClearing.format(new Date()));
		sqlStmt.append(" WHERE  py.MANUAL_ID = ? ");
		param.add(manualId);
		Object[] paramArr = param.toArray();
		jdbcTemplate.update(sqlStmt.toString(), paramArr);

	}

	@Override
	public Integer checkSup(String userName) throws SQLException {
		
//		Integer result = 0;
			StringBuilder sqlStmt = new StringBuilder();
			sqlStmt.append(" SELECT ur.Role_ID ");
			sqlStmt.append(" FROM USER up ");
			sqlStmt.append(" INNER JOIN USER_ROLE ur  ON ur.User_ID =  up.ID ");
			sqlStmt.append(" WHERE  up.Username = ? ");
			
			List<Object> param = new LinkedList<Object>();
			param.add(userName);
			Object[] paramArr = param.toArray();
			
			UserRole userRoles = jdbcTemplate.queryForObject(sqlStmt.toString(), paramArr, userRole);
			
//			jdbcTemplate.query(sqlStmt.toString(), new PreparedStatementSetter() {
//				public void setValues(PreparedStatement preparedStatement) throws SQLException {
//					preparedStatement.setString(1, userName);
//				}
//			}, new ResultSetExtractor<Integer>() {
//				public Integer extractData(ResultSet resultSet) throws SQLException, DataAccessException {
//					if (resultSet.next()) {
//						return  resultSet.getInt(1);
//					}
//
//					return null;
//				}
//			});
		
		return userRoles.getRoleId();
	}
	
	private static final RowMapper<UserRole> userRole = new RowMapper<UserRole>() {
//	private static final class userRole implements RowMapper<UserRole> {

		@Override
		public UserRole mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserRole userRole = new UserRole();
			userRole.setRoleId(rs.getInt("Role_ID"));
			
			return userRole;
		}

	};

	@Override
	public List<ReportPaymentBean> getReportPaymentB(ReportPaymentCriteria criteria, String serviceType) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT PM.*,PIM.*, m.value branch_name, (SELECT VALUE FROM MASTER_DATA WHERE KEYCODE = 'POS_NAME') pos_name ");
		sql.append(" FROM RECEIPT_MANUAL PM ");
		sql.append(" INNER JOIN PAYMENT_INVOICE_MANUAL PIM ON PM.MANUAL_ID = PIM.MANUAL_ID ");
		sql.append(" LEFT JOIN MASTER_DATA m ON PM.BRANCH_AREA = m.KEYCODE ");
		sql.append(" WHERE PM.CREATE_DATE >=").append("'" + criteria.getDateFrom() + "'");
		sql.append("  AND PM.CREATE_DATE <= ").append("'" + criteria.getDateTo2()+" "+criteria.getDateTo() + "'");
		
		if (!"".equals(criteria.getUser()) && criteria.getUser() != null) {
			sql.append(" AND PM.CREATE_BY = ").append("'" + criteria.getUser() + "'");
		}
			sql.append(" AND PIM.SERVICE_TYPE = ").append("'" + serviceType + "'");
		
		sql.append(" GROUP BY PM.RECEIPT_NO_MANUAL ORDER BY PM.RECEIPT_NO_MANUAL, PM.CREATE_BY, PIM.VAT_RATE, PM.CREATE_DATE");
		return jdbcTemplate.query(sql.toString(), new reportPaymentMapper());
	}

	@Override
	public  List<Map<String, Object>> findBeforVat(long manualId) throws SQLException {
		StringBuilder sqlStmt = new StringBuilder();
		sqlStmt.append(
				"SELECT pm.BEFOR_VAT FROM payment_invoice_manual pm ");
		sqlStmt.append(" WHERE  pm.MANUAL_ID = ? ");
		List<Object> param = new LinkedList<Object>();
		param.add(manualId);
		Object[] paramArr = param.toArray();
	return jdbcTemplate.queryForList(sqlStmt.toString(), paramArr);
	}

}
