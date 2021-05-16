package th.co.maximus.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import th.co.maximus.bean.MasterDatasBean;
import th.co.maximus.constants.Constants;

@Service
public class MasterDatasDaoImpl implements MasterDatasDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final class masterData implements RowMapper<MasterDatasBean> {

		@Override
		public MasterDatasBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			MasterDatasBean glBean = new MasterDatasBean();
			glBean.setId(rs.getLong("ID"));
			glBean.setKeyCode(rs.getString("KEYCODE"));
			glBean.setValue(rs.getString("VALUE"));
			glBean.setGroupKey(rs.getString("GROUP_KEY"));
			glBean.setType(rs.getString("TYPE"));
			glBean.setStatus(rs.getString("STATUS"));
			glBean.setOrdered(rs.getString("ORDERED"));
			glBean.setParentId(rs.getString("PARENT_ID"));
			glBean.setRefId(rs.getString("REF_ID"));
			glBean.setProperty1(rs.getString("PROPERTY_1"));
			glBean.setProperty2(rs.getString("PROPERTY_2"));
			glBean.setProperty3(rs.getString("PROPERTY_3"));
			glBean.setProperty4(rs.getString("PROPERTY_4"));
			glBean.setProperty5(rs.getString("PROPERTY_5"));
			glBean.setRemark(rs.getString("REMARK"));
			glBean.setRecordStatus(rs.getString("RECORD_STATUS"));
			
			return glBean;
		}

	}

	@Override
	public List<MasterDatasBean> findByRevenueType() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = 'REVENUE_TYPE'  ");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}
	
//	@Override
//	public List<MasterDatasBean> findByInitProject() {
//		StringBuilder sql = new StringBuilder();
//		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = 'INIT_PROJECT'  ");
//		return jdbcTemplate.query(sql.toString() , new masterData());
//	}

	@Override
	public List<MasterDatasBean> findByProduct() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = 'PRODUCT'  ");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}

	@Override
	public List<MasterDatasBean> findByVat() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = 'VAT'  ");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}

	@Override
	public List<MasterDatasBean> findByBankName() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = 'BANK_TYPE'  ");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}

	@Override
	public MasterDatasBean findByKey(String keyCode) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM MASTER_DATA WHERE KEYCODE = ? ");
		MasterDatasBean master = (MasterDatasBean)jdbcTemplate.queryForObject(sql.toString(), new Object[] { keyCode }, new masterData());
		return master;
	}

	@Override
	public MasterDatasBean findByGrop(String groupCode, String keyCode) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM MASTER_DATA WHERE GROUP_KEY = ? and KEYCODE = ? ");
		MasterDatasBean master = (MasterDatasBean)jdbcTemplate.queryForObject(sql.toString(), new Object[] { groupCode, keyCode }, new masterData());
		return master;
	}

	@Override
	public List<MasterDatasBean> findByBankEDCName() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = '"+Constants.MasterData.EDC_CREDIT_CARD_BANK+"'  ");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}

	@Override
	public String findReasonByCode(String code, String type) {
		StringBuilder sql = new StringBuilder();
		String groupKey = "";
		
		if(Constants.MasterData.IBACSS.equals(type)) {
			groupKey = Constants.MasterData.IBACSS_CANCEL_REASON;
		}else {
			groupKey = Constants.MasterData.OTHER_CANCEL_REASON;
		}
		
		sql.append(" SELECT * FROM MASTER_DATA WHERE 1=1 and GROUP_KEY = '"+groupKey+"' and KEYCODE = '"+code+"'");
		return jdbcTemplate.queryForObject(sql.toString() , new masterData()).getValue();
	}

	@Override
	public List<MasterDatasBean> findByCMSegment() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = '"+Constants.MasterData.CUSTOMER_SEGMENT+"' ");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}

	@Override
	public List<MasterDatasBean> findByCMSegmentByCRM() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = '"+Constants.MasterData.CUSTOMER_SEGMENT);
		sql.append("' AND PROPERTY_1 IN ('"+Constants.MasterData.PROPERTY_1.MS_11+"', '"+Constants.MasterData.PROPERTY_1.MS_12+"', '"+Constants.MasterData.PROPERTY_1.MS_14+"','"+Constants.MasterData.PROPERTY_1.MS_1+"')");
		sql.append(" ORDER BY PROPERTY_1");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}

	@Override
	public MasterDatasBean findByppt1(String ppt1) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = '"+Constants.MasterData.CUSTOMER_SEGMENT+"'");
		sql.append(" and PROPERTY_1 = '"+ppt1+"'");
		
		return jdbcTemplate.queryForObject(sql.toString() , new masterData());
	}
	
	@Override
	public List<MasterDatasBean> findByCussegmentValue(String userGroup) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = '"+Constants.MasterData.CUSTOMER_SEGMENT);
		sql.append("' AND PROPERTY_1 = '"+userGroup+"'");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}

	@Override
	public List<MasterDatasBean> findListByKey(String keyCode) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM MASTER_DATA WHERE GROUP_KEY = ? ");
		List<MasterDatasBean> master = jdbcTemplate.query(sql.toString(), new Object[] { keyCode }, new masterData());
		return master;
	}

	@Override
	public MasterDatasBean getNTBUPLACE() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = '"+Constants.INIT_PROJECT+"'");
		sql.append("and KEYCODE = '"+Constants.INIT_PROJECT_NT_BUPLACE+"'");
		
		return jdbcTemplate.queryForObject(sql.toString() , new masterData());
	}

	@Override
	public MasterDatasBean getNTSHOPNAME() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = '"+Constants.INIT_PROJECT+"'");
		sql.append("and KEYCODE = '"+Constants.INIT_PROJECT_NT_SHOPNAME+"'");
		
		return jdbcTemplate.queryForObject(sql.toString() , new masterData());
	}

	@Override
	public MasterDatasBean getBRANCHAREA() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = '"+Constants.INIT_PROJECT+"'");
		sql.append("and KEYCODE = '"+Constants.INIT_PROJECT_BRANCH_AREA+"'");
		
		return jdbcTemplate.queryForObject(sql.toString() , new masterData());
	}
	
	@Override
	public MasterDatasBean getPOSNAME() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = '"+Constants.INIT_PROJECT+"'");
		sql.append("and KEYCODE = '"+Constants.INIT_PROJECT_POS_NAME+"'");
		
		return jdbcTemplate.queryForObject(sql.toString() , new masterData());
	}

	@Override
	public MasterDatasBean getInitProject(String code) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = '"+Constants.INIT_PROJECT+"'");
		sql.append("and KEYCODE = '"+code+"'");
		
		return jdbcTemplate.queryForObject(sql.toString() , new masterData());
	}

	@Override
	public MasterDatasBean getTAXIDCAT() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE GROUP_KEY = '"+Constants.INIT_PROJECT+"'");
		sql.append("and KEYCODE = '"+Constants.INIT_PROJECT_TAX_ID_CAT+"'");
		
		return jdbcTemplate.queryForObject(sql.toString() , new masterData());
	}
	
	

}
