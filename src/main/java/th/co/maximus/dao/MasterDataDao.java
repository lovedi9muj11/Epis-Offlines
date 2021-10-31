package th.co.maximus.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import th.co.maximus.bean.MasterDataBean;
import th.co.maximus.bean.MasterDataSyncBean;

@Repository
public interface MasterDataDao {
	
	public List<MasterDataBean> findAllByBankCode() ;
	
	public List<MasterDataBean> findAllByBankName() ;
	
	List<MasterDataBean> findAll();
	
	List<MasterDataBean> findBatch(String code);
	
	public int insertMasterdata(MasterDataBean masterDataBean);
	
	public int insertMasterdataGroup(MasterDataBean masterDataBean);
	
	public List<MasterDataBean> findAllByServiceType();
	
	public List<MasterDataBean> findAllByServiceDepartment();
	
	public List<MasterDataBean> findAllByServiceName();
	
	public List<MasterDataBean> findAllByCategory();
	
	public List<MasterDataBean> findAllByGropType(String groupType);
	
	void insertMasterDataSync(MasterDataSyncBean masterDataSyncBean);
	
	void deleteBeforInsertMS();
	
	List<MasterDataBean> showAllMSNGL();
	
	MasterDataBean findGroupTypeByKeyCode(String groupKey);
	
	String findProperty(String code);
	
	String findProperty2(String code, String branchArea);
	
	void insertBatch(MasterDataBean masterDataBean);

	public List<MasterDataBean> findByVat();

	void insertInitProgram(MasterDataBean masterDataBean);

	MasterDataBean findByCostCenter();

	MasterDataBean findAllByBranchcode();


	public List<MasterDataBean> findSegmentOther();
	
	public List<MasterDataBean> findProductOther();
	
	void updateDateBygroupKey(String groupkey);
	


}
