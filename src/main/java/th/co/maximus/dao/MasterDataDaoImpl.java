package th.co.maximus.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import th.co.maximus.bean.MasterDataBean;
import th.co.maximus.bean.MasterDataSyncBean;
import th.co.maximus.constants.Constants;

@Service
public class MasterDataDaoImpl implements MasterDataDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<MasterDataBean> findAllByBankCode() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA ms  ");
		sql.append(" WHERE ms.group_key = 'BANK_TYPE' ");
		return jdbcTemplate.query(sql.toString() , new masterDataCode());
	}

	@Override
	public List<MasterDataBean> findAllByBankName() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA ms  ");
		sql.append(" WHERE ms.group_key = 'BANK_TYPE' ");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}
	
	@Override
	public List<MasterDataBean> findAll() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA  ");
//		sql.append(" WHERE ms.groupType = 'INITVALUE' ");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}
	
	@Override
	public int insertMasterdata(MasterDataBean masterDataBean) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO MASTER_DATA (valueKey, text, groupType)  VALUES (?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql, new String[] { "id" });
				pst.setString(1, masterDataBean.getValue());
				pst.setString(2, masterDataBean.getText());
				pst.setString(3, masterDataBean.getGroup());
				return pst;
			}
		}, keyHolder);
		int newUserId = keyHolder.getKey().intValue();
		return newUserId;
	}
	
	@Override
	public int insertMasterdataGroup(MasterDataBean masterDataBean) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO MASTER_DATA (valueKey, text, groupType)  VALUES (?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql, new String[] { "id" });
				pst.setString(1, masterDataBean.getValue());
				pst.setString(2, masterDataBean.getValue());
				pst.setString(3, Constants.MasterData.MASTERDATA_GROUP);
				return pst;
			}
		}, keyHolder);
		int newUserId = keyHolder.getKey().intValue();
		return newUserId;
	}
	
	@Override
	public List<MasterDataBean> findAllByServiceType() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MAP_GL_SERVICE_TYPE mg  ");
		sql.append(" WHERE mg.SOURCE = '"+Constants.MasterData.OTHER+"'");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}
	
	@Override
	public List<MasterDataBean> findAllByServiceDepartment() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA ms  ");
		sql.append(" WHERE ms.group_key = '"+Constants.MasterData.PROFIT_COST_CENTER+"'");
		sql.append(" ORDER BY KEYCODE ");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}
	
	@Override
	public List<MasterDataBean> findAllByServiceName() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA ms  ");
		sql.append(" WHERE ms.group_key = 'SERVICENAME'");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}
	@Override
	public List<MasterDataBean> findAllByCategory() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA ms  ");
		sql.append(" WHERE ms.group_key = '"+Constants.MasterData.OTHER_PAYMENT_UNIT+"'");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}
	@Override
	public List<MasterDataBean> findByVat() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA ms  ");
		sql.append(" WHERE ms.group_key = '"+Constants.MasterData.VAT+"'");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}

	@Override
	public List<MasterDataBean> findAllByGropType(String groupType) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA ms  ");
		sql.append(" WHERE ms.group_key = ");
		sql.append("'"+groupType+"'");
		return jdbcTemplate.query(sql.toString() , new masterData());
	}

	
	@Override
	public void insertMasterDataSync(MasterDataSyncBean masterDataSyncBean) {
		String sql = "INSERT INTO MASTER_DATA (KEYCODE, VALUE, GROUP_KEY, TYPE, STATUS, ORDERED, PARENT_ID, REF_ID, PROPERTY_1, PROPERTY_2, PROPERTY_3, PROPERTY_4, PROPERTY_5, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		java.util.Date now = new java.util.Date();
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql, new String[] { "id" });
				pst.setString(1, masterDataSyncBean.getKey());
				pst.setString(2, masterDataSyncBean.getValue());
				pst.setString(3, masterDataSyncBean.getGroupKey());
				pst.setString(4, masterDataSyncBean.getType());
				pst.setString(5, masterDataSyncBean.getStatus());
				pst.setString(6, masterDataSyncBean.getOrdered());
				pst.setString(7, masterDataSyncBean.getParentId());
				pst.setString(8, masterDataSyncBean.getRefId());
				pst.setString(9, masterDataSyncBean.getProperty1());
				pst.setString(10, masterDataSyncBean.getProperty2());
				pst.setString(11, masterDataSyncBean.getProperty3());
				pst.setString(12, masterDataSyncBean.getProperty4());
				pst.setString(13, masterDataSyncBean.getProperty5());
//				pst.setString(14, masterDataSyncBean.getRemark());
				pst.setString(14, masterDataSyncBean.getCreateBy());
				pst.setDate(15, null);
				pst.setString(16, masterDataSyncBean.getUpdateBy());
				pst.setDate(17, null);
//				pst.setString(18, masterDataSyncBean.getTaxIdCat());
				return pst;
			}
		});
	}

	@Override
	public void deleteBeforInsertMS() {
		String del = "delete from MASTER_DATA where GROUP_KEY <> '"+Constants.MasterData.TRIGGER_GOUP+"' and GROUP_KEY <> '"+Constants.MasterData.BANK_TYPE_EDC+"' and GROUP_KEY <> '"+Constants.MasterData.USER_GROUP +"' and GROUP_KEY <> '"+Constants.INIT_PROJECT+"'";
		jdbcTemplate.update(del);
	}
	
	private static final class masterData implements RowMapper<MasterDataBean> {

		@Override
		public MasterDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			MasterDataBean masterDataBean = new MasterDataBean();
			masterDataBean.setId(rs.getInt("id"));
			masterDataBean.setValue(rs.getString("KEYCODE"));
			masterDataBean.setText(rs.getString("VALUE"));
			masterDataBean.setGroup(rs.getString("GROUP_KEY"));
			masterDataBean.setOrderBatch(rs.getString("ORDERED"));
			masterDataBean.setProperty2(rs.getString("PROPERTY_2"));
			masterDataBean.setProperty1(rs.getString("PROPERTY_1"));
			masterDataBean.setFlagH(rs.getString("FLAG_H"));
			masterDataBean.setFlagM(rs.getString("FLAG_M"));
			return masterDataBean;
		}

	}
	
	private static final class masterDataCode implements RowMapper<MasterDataBean> {

		@Override
		public MasterDataBean mapRow(ResultSet rs, int rowNum) throws SQLException {
			MasterDataBean masterDataBean = new MasterDataBean();
			masterDataBean.setId(rs.getInt("id"));
			masterDataBean.setValue(rs.getString("KEYCODE"));
			masterDataBean.setText(rs.getString("KEYCODE"));
			masterDataBean.setGroup(rs.getString("GROUP_KEY"));
			
			return masterDataBean;
		}

	}

	@Override
	public List<MasterDataBean> showAllMSNGL() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA ms  ");
		sql.append(" WHERE ms.group_key in ('"+Constants.MasterData.BANK_TYPE+"', '"+Constants.MasterData.BUSINESS_AREA+"', '"+Constants.MasterData.OTHER_PAYMENT_UNIT+"')");
		
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("", "");
//		jdbcTemplate.queryForList(sql.toString(), params, new masterData());
		return jdbcTemplate.query(sql.toString() , new masterData());
	}

	@Override
	public List<MasterDataBean> findBatch(String code) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE 1=1 and GROUP_KEY = '"+code+"'");
		
		return jdbcTemplate.query(sql.toString() , new masterData());
	}

	@Override
	public MasterDataBean findGroupTypeByKeyCode(String groupKey) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE 1=1 and KEYCODE = '"+groupKey+"'");
		
		return jdbcTemplate.queryForObject(sql.toString(), new masterData());
	}

	@Override
	public void insertBatch(MasterDataBean masterDataBean) {
		StringBuilder sql = new StringBuilder();
		List<Object> param = new LinkedList<Object>();
		sql.append(" UPDATE MASTER_DATA set VALUE = ?, FLAG_H = ?, FLAG_M = ? WHERE KEYCODE = ?");
		
		param.add(masterDataBean.getValue());
		param.add(masterDataBean.getFlagH());
		param.add(masterDataBean.getFlagM());
		param.add(masterDataBean.getOrderBatch());
		Object[] paramArr = param.toArray();
		
		jdbcTemplate.update(sql.toString(), paramArr);
	}
	@Override
	public void insertInitProgram(MasterDataBean masterDataBean) {
		StringBuilder sql = new StringBuilder();
		List<Object> param = new LinkedList<Object>();
		sql.append(" UPDATE MASTER_DATA set VALUE = ? WHERE KEYCODE = ? AND GROUP_KEY = 'INIT_PROJECT'");
		
		param.add(masterDataBean.getValue());
		param.add(masterDataBean.getKeyCode());
		Object[] paramArr = param.toArray();
		
		jdbcTemplate.update(sql.toString(), paramArr);
	}


	@Override
	public String findProperty(String code) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE 1=1 and PROPERTY_2 = '"+code+"'");
		
		MasterDataBean data = jdbcTemplate.queryForObject(sql.toString(), new masterData());
		
		return StringUtils.isNotBlank(data.getProperty2())?data.getProperty2():"";
	}
	
	@Override
	public MasterDataBean findByCostCenter() { 
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA ms  ");
		sql.append(" WHERE ms.KEYCODE = '"+Constants.MasterData.COST_CENTER+"'");
		
		return jdbcTemplate.queryForObject(sql.toString() , new masterData());
	}
	
	@Override
	public MasterDataBean findAllByBranchcode() { 
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA ms  ");
		sql.append(" WHERE ms.KEYCODE = 'BRANCH_AREA' and ms.GROUP_KEY ='INIT_PROJECT'");
		
		return jdbcTemplate.queryForObject(sql.toString() , new masterData());
	}

	@Override
	public String findProperty2(String code, String branchArea) {
		StringBuilder posName = new StringBuilder();
		StringBuilder sql = new StringBuilder("");
		sql.append(" SELECT * FROM MASTER_DATA WHERE 1=1 and PROPERTY_2 = '"+code+"'");
//		sql.append(" AND KEYCODE = '"+branchArea+"'");
		sql.append(" AND PROPERTY_1 = '"+branchArea+"'");
		
		List<MasterDataBean> list = jdbcTemplate.query(sql.toString(), new masterData());
		if(CollectionUtils.isNotEmpty(list)) {
			posName .append(" - ");
			posName .append(list.get(0).getText());
		}
		
		return posName.toString();
	}

	@Override
	public List<MasterDataBean> findSegmentOther() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE 1=1 and GROUP_KEY = '"+Constants.MasterData.SEGMENT+"' ");
		
		return jdbcTemplate.query(sql.toString(), new masterData());
	}
	
	@Override
	public List<MasterDataBean> findProductOther() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM MASTER_DATA WHERE 1=1 and GROUP_KEY = '"+Constants.MasterData.PRODUCT+"' ");
		
		return jdbcTemplate.query(sql.toString(), new masterData());
	}

}
