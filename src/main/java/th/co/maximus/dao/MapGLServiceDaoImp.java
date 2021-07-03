package th.co.maximus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Service;

import th.co.maximus.bean.MapGLBean;

@Service
public class MapGLServiceDaoImp implements MapGLServiceDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void insertMapGLService(MapGLBean mapGLBean) {
		String sql = "INSERT INTO MAP_GL_SERVICE_TYPE (GL_CODE, SERVICE_CODE, PRODUCT_CODE, PRODUCT_NAME, SUB_PRODUCT_CODE, SUB_PRODUCT_NAME, SERVICE_NAME, REVENUE_TYPE_CODE, REVENUE_TYPE_NAME, SEGMENT_CODE, SEGMENT_NAME, STATUS, CREATE_BY, CREATE_DATE, UPDATE_BY, UPDATE_DATE, RECORD_STATUS, SOURCE,ERP_INTERFACE_FLAG)  VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, NOW(),?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pst = con.prepareStatement(sql, new String[] { "id" });
				pst.setString(1, mapGLBean.getGlCode());
				pst.setString(2, mapGLBean.getServiceCode());
				pst.setString(3, mapGLBean.getProductCode());
				pst.setString(4, mapGLBean.getProductName());
				pst.setString(5, mapGLBean.getSubProductCode());
				pst.setString(6, mapGLBean.getSubProductName());
				pst.setString(7, mapGLBean.getServiceName());
				pst.setString(8, mapGLBean.getRevenueTypeCode());
				pst.setString(9, mapGLBean.getRevenueTypeName());
				pst.setString(10, mapGLBean.getSegMentCode());
				pst.setString(11, mapGLBean.getSegMentName());
				pst.setString(12, mapGLBean.getStatus());
//				pst.setString(13, mapGLBean.getRemark());
				pst.setString(13, mapGLBean.getCreateBy());
				pst.setDate(14, null);
				pst.setString(15, mapGLBean.getUpdateBy());
//				pst.setDate(16, null);
				pst.setString(16, mapGLBean.getRecordStatus());
				pst.setString(17, mapGLBean.getSource());
				pst.setString(18, mapGLBean.getErpInterfaceFlag());
				return pst;
			}
		});
	}

	@Override
	public void deleteBeforInsert() {
		String del = "delete from MAP_GL_SERVICE_TYPE";
		jdbcTemplate.update(del);
	}

}
