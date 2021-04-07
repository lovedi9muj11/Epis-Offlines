package th.co.maximus.dao;

import java.util.List;

import th.co.maximus.bean.MasterDatasBean;

public interface MasterDatasDao {

	public List<MasterDatasBean> findByRevenueType();

	public List<MasterDatasBean> findByProduct();

	public List<MasterDatasBean> findByVat();

	public List<MasterDatasBean> findByBankName();
	
	public List<MasterDatasBean> findByBankEDCName();

	public MasterDatasBean findByKey(String keyCode);
	
	public List<MasterDatasBean> findListByKey(String keyCode);

	public MasterDatasBean findByGrop(String groupCode, String keyCode);
	
	String findReasonByCode(String code, String type);
	
	List<MasterDatasBean> findByCMSegment();
	
	List<MasterDatasBean> findByCMSegmentByCRM();
	
	MasterDatasBean findByppt1(String ppt1);

	List<MasterDatasBean> findByCussegmentValue(String userGroup);
	
	MasterDatasBean getNTBUPLACE();
	
	MasterDatasBean getNTSHOPNAME();
	
	MasterDatasBean getBRANCHAREA();

}
