package th.co.maximus.service.impl;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import th.co.maximus.bean.MapGLBean;
import th.co.maximus.bean.MasterDataBean;
import th.co.maximus.bean.MasterDataSyncBean;
import th.co.maximus.bean.Principal;
import th.co.maximus.bean.UserBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.dao.MasterDataDao;
import th.co.maximus.service.CallEpisOnlineService;
import th.co.maximus.service.MapGLService;
import th.co.maximus.service.MasOfficerService;
import th.co.maximus.service.MasterDataService;
import th.co.maximus.util.GetMacAddress;

@Service
public class CallEpisOnlineServiceImp implements CallEpisOnlineService{
	
	@Value("${url.online}")
	private String url;
	private final SSLContext sslContext;
	private final SSLConnectionSocketFactory csf;
	private final HttpComponentsClientHttpRequestFactory requestFactory;
	RestTemplate restTemplate;
	
	@Autowired private MasterDataDao masterDataDao;

	
	@Autowired
	MasterDataService masterDataService;
	
	@Autowired
	MapGLService mapGLService;
	
	@Autowired
	MasOfficerService masOfficerService;

	
	public CallEpisOnlineServiceImp() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
		csf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		requestFactory = new HttpComponentsClientHttpRequestFactory(HttpClients.custom().setSSLSocketFactory(csf).build());
		restTemplate = new RestTemplate(requestFactory);
	}

	@Override
	public void callOnline() {
		String mac =  GetMacAddress.getMACAddress();
		 String pos ="";
		 String posName="";
		 String branchArea="";
		 String branchCode="";
		 String taxIdCat="";
		 String costCenter="";
		 String ntShopname ="";
		 String ntBuplace ="";
		String postUrl = url.concat("/offline/posByMac/"+ mac +".json"); 
		ResponseEntity<String> postResponse = restTemplate.getForEntity(postUrl, String.class);
		System.out.println("Response for Post Request: " + postResponse.getBody());
		try { 
			JSONArray jsonArray = new JSONArray(postResponse.getBody());
			for(int i=0; i<jsonArray.length(); i++) {
				pos = jsonArray.getJSONObject(i).getString("no");
				posName = jsonArray.getJSONObject(i).getString("name");
				JSONObject  json = new JSONObject(jsonArray.getJSONObject(i).getString("shop"));
					branchCode = json.getString("businessPlace");
					branchArea = json.getString("businessArea");
					costCenter = json.getString("costCenter");
					branchArea = json.getString("businessArea");
					costCenter = json.getString("costCenter");
					taxIdCat = "0107546000229";
					ntShopname = json.getString("ntShopname");
					ntBuplace = json.getString("ntBuplace");
			}
			
			if(StringUtils.isNotBlank(pos) && StringUtils.isNotBlank(posName) && StringUtils.isNotBlank(branchArea) && StringUtils.isNotBlank(branchCode) && StringUtils.isNotBlank(taxIdCat)
					  && StringUtils.isNotBlank(costCenter) && StringUtils.isNotBlank(ntShopname) && StringUtils.isNotBlank(ntBuplace) && StringUtils.isNotBlank(ntBuplace)) {
				MasterDataBean masterDataPos = new MasterDataBean();
				masterDataPos.setKeyCode("POS");
				masterDataPos.setValue(pos);
				masterDataDao.insertInitProgram(masterDataPos);
				MasterDataBean masterDataPosName = new MasterDataBean();
				masterDataPosName.setKeyCode("POS_NAME");
				masterDataPosName.setValue(posName);
				masterDataDao.insertInitProgram(masterDataPosName);
				MasterDataBean masterDataBranchCode = new MasterDataBean();
				masterDataBranchCode.setKeyCode("BRANCH_CODE");
				masterDataBranchCode.setValue(branchCode);
				masterDataDao.insertInitProgram(masterDataBranchCode);
				MasterDataBean masterDataBranchArea = new MasterDataBean();
				masterDataBranchArea.setKeyCode("BRANCH_AREA");
				masterDataBranchArea.setValue(branchArea);
				masterDataDao.insertInitProgram(masterDataBranchArea);
				MasterDataBean masterDataCostCenter = new MasterDataBean();
				masterDataCostCenter.setKeyCode("COST_CENTER");
				masterDataCostCenter.setValue(costCenter);
				masterDataDao.insertInitProgram(masterDataCostCenter);
				MasterDataBean masterDatataxIdCat= new MasterDataBean();
				masterDatataxIdCat.setKeyCode("TAX_ID_CAT");
				masterDatataxIdCat.setValue(taxIdCat);
				masterDataDao.insertInitProgram(masterDatataxIdCat);
				MasterDataBean masterDatanNtbuplace = new MasterDataBean();
				masterDatanNtbuplace.setKeyCode("NT_BUPLACE");
				masterDatanNtbuplace.setValue(ntBuplace);
				masterDataDao.insertInitProgram(masterDatanNtbuplace);
				MasterDataBean masterDataNtshopName = new MasterDataBean();
				masterDataNtshopName.setKeyCode("NT_SHOPNAME");
				masterDataNtshopName.setValue(ntShopname);
				masterDataDao.insertInitProgram(masterDataNtshopName);
				masterDataDao.updateDateBygroupKey(Constants.MasterData.TRIGGER_GOUP);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void callOnlineSyncMasterData() {
		try {
			MasterDataSyncBean masterDataSyncBean = new MasterDataSyncBean();
			List<MasterDataSyncBean> list = new ArrayList<MasterDataSyncBean>();
			
			Set<String> groupKeys = new HashSet<String>();
			groupKeys.add(Constants.MasterData.BANK_TYPE);
			//groupKeys.add(Constants.MasterData.BUSINESS_AREA);
			groupKeys.add(Constants.MasterData.OTHER_PAYMENT_UNIT);
			groupKeys.add(Constants.MasterData.VAT);
			groupKeys.add(Constants.MasterData.WT);
			groupKeys.add(Constants.MasterData.IBACSS_CANCEL_REASON);
			groupKeys.add(Constants.MasterData.OTHER_CANCEL_REASON);
			groupKeys.add(Constants.MasterData.EDC_CREDIT_CARD_BANK);
			groupKeys.add(Constants.MasterData.CUSTOMER_SEGMENT);
			groupKeys.add(Constants.MasterData.SEGMENT);
			groupKeys.add(Constants.MasterData.PRODUCT);
			groupKeys.add(Constants.MasterData.OTHER_TEMP_CUSTOMER_SEGMENT);
			groupKeys.add(Constants.MasterData.PROFIT_COST_CENTER);
			groupKeys.add(Constants.MasterData.CREDIT_CARD_TYPE);
			
			String gettUrl = url.concat("/offline/masterDataSyncByGroupKey.json"); // /offline/insertPayment //masterdatasync1
			ResponseEntity<String> getResponse = restTemplate.postForEntity(gettUrl, groupKeys, String.class);
			JSONArray jsonArray = new JSONArray(getResponse.getBody());
			for(int i=0; i<jsonArray.length(); i++) {
				masterDataSyncBean = new MasterDataSyncBean();
//				masterDataSyncBean.setId( jsonArray.getJSONObject(i).getLong("id") );
				masterDataSyncBean.setKey( isNull2Null(jsonArray.getJSONObject(i).getString("key")));
				masterDataSyncBean.setValue( isNull2Null(jsonArray.getJSONObject(i).getString("value")));
				masterDataSyncBean.setGroupKey( isNull2Null(jsonArray.getJSONObject(i).getString("groupKey")));
				masterDataSyncBean.setType( isNull2Null(jsonArray.getJSONObject(i).getString("type")));
				masterDataSyncBean.setStatus( isNull2Null(jsonArray.getJSONObject(i).getString("status")));
				masterDataSyncBean.setOrdered( isNull2Null(jsonArray.getJSONObject(i).getString("ordered")));
				masterDataSyncBean.setParentId( isNull2Null(jsonArray.getJSONObject(i).getString("parentId")));
				masterDataSyncBean.setRefId( isNull2Null(jsonArray.getJSONObject(i).getString("refId")));
				masterDataSyncBean.setProperty1( isNull2Null(jsonArray.getJSONObject(i).getString("property1")));
				masterDataSyncBean.setProperty2( isNull2Null(jsonArray.getJSONObject(i).getString("property2")));
				masterDataSyncBean.setProperty3( isNull2Null(jsonArray.getJSONObject(i).getString("property3")));
				masterDataSyncBean.setProperty4( isNull2Null(jsonArray.getJSONObject(i).getString("property4")));
				masterDataSyncBean.setProperty5( isNull2Null(jsonArray.getJSONObject(i).getString("property5")));
				masterDataSyncBean.setCreateBy( isNull2Null(jsonArray.getJSONObject(i).getString("createBy")));
//				masterDataSyncBean.setCreateDate(new Date());
				masterDataSyncBean.setUpdateBy( isNull2Null(jsonArray.getJSONObject(i).getString("updateBy")));
//				masterDataSyncBean.setUpdateDate(new Date());
				masterDataSyncBean.setInitialValue( isNull2Null(jsonArray.getJSONObject(i).getString("initialValue")));
//				masterDataSyncBean.setTaxIdCat( isNull2Null(jsonArray.getJSONObject(i).getString("taxIdCat")));
				list.add(masterDataSyncBean);
			}
			
			System.out.println("Response for Post Request: " + getResponse.getBody().length() + " data " + getResponse.getBody());
			
			
			String respone = masterDataService.insertMasterDataSync(list);
			System.out.println(" Return Status for insert Master Data respone :: " + respone);
		}catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void callOnlineSyncMapGL() {
		try {
			MapGLBean glBean = new MapGLBean();
			List<MapGLBean> list = new ArrayList<MapGLBean>();
			String gettUrl = url.concat("/offline/mapGLServiceTypesync.json");
			ResponseEntity<String> getResponse = restTemplate.getForEntity(gettUrl, String.class);
			
			JSONArray jsonArray = new JSONArray(getResponse.getBody());
			for(int i=0; i<jsonArray.length(); i++) {
				glBean = new MapGLBean();
				glBean.setGlCode( isNull2Null(jsonArray.getJSONObject(i).getString("glCode")));
				glBean.setProductCode( isNull2Null(jsonArray.getJSONObject(i).getString("productCode")));
				glBean.setProductName( isNull2Null(jsonArray.getJSONObject(i).getString("productName")));
				glBean.setSubProductCode( isNull2Null(jsonArray.getJSONObject(i).getString("subProductCode")));
				glBean.setSubProductName( isNull2Null(jsonArray.getJSONObject(i).getString("subProductName")));
				glBean.setServiceName( isNull2Null(jsonArray.getJSONObject(i).getString("serviceName")));
				glBean.setRevenueTypeCode( isNull2Null(jsonArray.getJSONObject(i).getString("revenueTypeCode")));
				glBean.setRevenueTypeName( isNull2Null(jsonArray.getJSONObject(i).getString("revenueTypeName")));
				glBean.setSegMentCode( isNull2Null(jsonArray.getJSONObject(i).getString("segmentCode")));
				glBean.setSegMentName( isNull2Null(jsonArray.getJSONObject(i).getString("segmentName")));
				glBean.setSource( isNull2Null(jsonArray.getJSONObject(i).getString("source")));
				glBean.setServiceCode( isNull2Null(jsonArray.getJSONObject(i).getString("serviceCode")));
				glBean.setStatus( isNull2Null(jsonArray.getJSONObject(i).getString("status")));
				glBean.setCreateBy( isNull2Null(jsonArray.getJSONObject(i).getString("createBy")));
				glBean.setErpInterfaceFlag( isNull2Null(jsonArray.getJSONObject(i).getString("erpInterfaceFlag")));
//				glBean.setCreateDate( new Date());
				glBean.setUpdateBy( isNull2Null(jsonArray.getJSONObject(i).getString("updateBy")));
//				glBean.setUpdateDate( new Date());
				list.add(glBean);
			}
			String respone = mapGLService.insertMapGL(list);
			System.out.println(" Return Status for insert GL Data respone :: " + respone);
		}catch (Exception e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void callOnlineSyncUser() {
		 List<MasterDataBean> resultList = masterDataDao.findAllByGropType(Constants.INIT_PROJECT);
		Set<String> branchArea = new HashSet<String>();
		String branArea = "";
		String posno ="";
		 for (MasterDataBean masterDataBean : resultList) {
				if(masterDataBean.getValue().equals("POS")) {
					posno = masterDataBean.getText();
				}
				
				if(masterDataBean.getValue().equals("BRANCH_AREA")) {
					branArea = masterDataBean.getText();
				}
			}
		branchArea.add(branArea);
		
		try {
			UserBean userBean = new UserBean();
			Principal principal = new Principal(); 
			List<UserBean> list = new ArrayList<UserBean>();
			String gettUrl = url.concat("/offline/userSyncByArea/"+posno+".json");
			ResponseEntity<String> getResponse = restTemplate.postForEntity(gettUrl, branchArea, String.class);
			
			JSONArray jsonArray = new JSONArray(getResponse.getBody());
			System.out.println(getResponse.getBody());
			for(int i=0; i<jsonArray.length(); i++) {
				userBean = new UserBean();
				principal = new Principal();
				userBean.setName(jsonArray.getJSONObject(i).getString("givenName"));
				userBean.setSurName(jsonArray.getJSONObject(i).getString("familyName"));
				userBean.setUserName(jsonArray.getJSONObject(i).getString("name"));
				userBean.setPassword(jsonArray.getJSONObject(i).getString("password"));
				
				JSONObject object = jsonArray.getJSONObject(i).getJSONObject("principal");
				principal.setId(object.getLong("id"));
				principal.setName(object.getString("name"));
				
				userBean.setPrincipal(principal);
				userBean.setLoginFlag(Constants.USER.LOGIN_FLAG_Y);
				list.add(userBean);
			}
			String respone = masOfficerService.insertMasOfficerUser(list);
			System.out.println(" Return Status for insert User respone :: " + respone);
		}catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	String isNull2Null(String str) {
		if(!"null".equals(str)) return str;
		
		return null;
	}

}
