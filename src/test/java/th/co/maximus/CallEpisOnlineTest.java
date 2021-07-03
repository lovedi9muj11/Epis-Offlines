package th.co.maximus;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import th.co.maximus.batch.CallEpisOnline;
import th.co.maximus.bean.MapGLBean;
import th.co.maximus.bean.MasterDataSyncBean;
import th.co.maximus.dao.MapGLServiceDao;
import th.co.maximus.dao.MasterDataDao;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CallEpisOnlineTest {
	@Autowired
	private CallEpisOnline callEpisOnline;
	
//	@Autowired
//    private OfflineBatch tasks;
	
//	@Autowired
//	private ClearingPaymentEpisOffline clearingPaymentEpisOffline;
	
	@Autowired private MasterDataDao masterDataDao;
	
	@Autowired private MapGLServiceDao mapGLServiceDao;
	
	@Test
	@Ignore
	public void callRest() {
		callEpisOnline.callOnline();
	}
	
	@Test
	@Ignore
	public void callRestGet() {
		callEpisOnline.callOnlineSyncMasterData();
	}
	
	@Test
	@Ignore
	public void callRestGetGL() {
		callEpisOnline.callOnlineSyncMapGL();
	}
	
	@Test
	@Ignore
	public void test() throws Exception{
		//clearingPaymentEpisOffline.callOnlinePayment(1);
	}
	
	@Test
	@Ignore
	public void callRestGetUser() throws Exception{
		callEpisOnline.callOnlineSyncUser();
	}
	
    @Test
    @Ignore
    public void contextLoads() {
//        assertThat(tasks).isNotNull();
    }
    
    @Test
    @Ignore
    public void insertMasterDataSync() {
    	MasterDataSyncBean bean = new MasterDataSyncBean();
    	bean.setKey("NT_SHOPNAME");
    	bean.setValue("xxx");
    	bean.setGroupKey("xxx");
    	bean.setType("xxx");
    	bean.setStatus("xxx");
    	
    	masterDataDao.insertMasterDataSync(bean);
    }
    
    @Test
    @Ignore
    public void insertMapGLService() {
    	MapGLBean bean = new MapGLBean();
    	bean.setGlCode("xxx");
    	bean.setServiceCode("xxx");
    	bean.setProductCode("xxx");
    	bean.setProductName("xxx");
    	bean.setStatus("xxx");
    	
    	mapGLServiceDao.insertMapGLService(bean);
    }

}

