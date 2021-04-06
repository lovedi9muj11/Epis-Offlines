package th.co.maximus.service.impl;

import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import th.co.maximus.bean.MasterDataBean;
import th.co.maximus.bean.PaymentMMapPaymentInvBean;
import th.co.maximus.bean.TmpInvoiceBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.dao.CancelPaymentDTO;
import th.co.maximus.dao.CancelPaymentDTO.Receipt;
import th.co.maximus.dao.DeductionManualDao;
import th.co.maximus.dao.MasterDataDao;
import th.co.maximus.dao.PaymentInvoiceManualDao;
import th.co.maximus.dao.PaymentManualDao;
import th.co.maximus.dao.TrsChequeRefManualDao;
import th.co.maximus.dao.TrsMethodManualDao;
import th.co.maximus.dao.TrscreDitrefManualDao;
import th.co.maximus.model.DuductionEpisOffline;
import th.co.maximus.model.OfflineResultModel;
import th.co.maximus.model.PaymentDTO;
import th.co.maximus.model.PaymentEpisOfflineDTO;
import th.co.maximus.model.PaymentInvoiceEpisOffline;
import th.co.maximus.model.ReceiptOfflineModel;
import th.co.maximus.model.TranferLogs;
import th.co.maximus.model.TrsChequerefEpisOffline;
import th.co.maximus.model.TrsCreditrefEpisOffline;
import th.co.maximus.model.TrsMethodEpisOffline;
import th.co.maximus.repository.TranferLogsRepository;
import th.co.maximus.service.CancelPaymentService;
import th.co.maximus.service.ClearingPaymentEpisOfflineService;
import th.co.maximus.service.MinusOnlineService;
import th.co.maximus.service.TmpInvoiceService;
import th.co.maximus.util.GetMacAddress;

@Service
public class ClearingPaymentEpisOfflineServiceImpl implements ClearingPaymentEpisOfflineService {

	@Value("${url.online}")
	private String url;

	private String posNo;

	private String branCode;
	private final SSLContext sslContext;
	private final SSLConnectionSocketFactory csf;
	private final HttpComponentsClientHttpRequestFactory requestFactory;
	RestTemplate restTemplate;

	@Autowired
	private MasterDataDao masterDataDao;

	@Autowired
	private PaymentManualDao paymentManualDao;

	@Autowired
	private PaymentInvoiceManualDao paymentInvoiceManualDao;

	@Autowired
	private DeductionManualDao deductionManualDao;

	@Autowired
	private TrsMethodManualDao trsMethodManualDao;

	@Autowired
	private TrsChequeRefManualDao trsChequeRefManualDao;

	@Autowired
	private TrscreDitrefManualDao TrscreDitrefManualDao;

	@Autowired
	private TmpInvoiceService tmpInvoiceService;

	@Autowired
	private CancelPaymentService cancelPaymentService;
	
	@Autowired MinusOnlineService minusOnlineService;
	
	@Autowired TranferLogsRepository tranferLogsRepository;

	public ClearingPaymentEpisOfflineServiceImpl()
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, new TrustSelfSignedStrategy())
				.build();
		csf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		requestFactory = new HttpComponentsClientHttpRequestFactory(
				HttpClients.custom().setSSLSocketFactory(csf).build());
		restTemplate = new RestTemplate(requestFactory);
	}

	public void init() {
		List<MasterDataBean> resultList = masterDataDao.findAllByGropType(Constants.INIT_PROJECT);
		for (MasterDataBean masterDataBean : resultList) {
			if (masterDataBean.getValue().equals("POS")) {
				posNo = masterDataBean.getText();
			}

			if (masterDataBean.getValue().equals("NT_BUPLACE")) {
				branCode = masterDataBean.getText();
			}
		}
	}

	@Override
	public ReceiptOfflineModel findRecipt(Integer manualId) throws SQLException {
		// TODO Auto-generated method stub
		return paymentManualDao.findByManualId(manualId);
	}

	@Override
	public ReceiptOfflineModel findReciptStatus(Integer manualId, String status) throws SQLException {
		// TODO Auto-generated method stub
		return paymentManualDao.findByManualId(manualId, status);
	}

	@Override
	public List<PaymentInvoiceEpisOffline> findPaymentInvoice(Integer manualId) throws SQLException {
		// TODO Auto-generated method stub
		return paymentInvoiceManualDao.findByManualId(manualId);
	}

	@Override
	public List<DuductionEpisOffline> findDeduction(Integer manualId) throws Exception {
		// TODO Auto-generated method stub
		return deductionManualDao.findDeductionManual(manualId);
	}

	@Override
	public List<TrsMethodEpisOffline> findTrsMethod(Integer manualId) throws Exception {
		// TODO Auto-generated method stub
		return trsMethodManualDao.findByManualId(manualId);
	}

	@Override
	public List<TrsCreditrefEpisOffline> findTrsCredit(long methodTrsId) throws Exception {
		// TODO Auto-generated method stub
		return TrscreDitrefManualDao.findByMethodId(methodTrsId);
	}

	@Override
	public List<TrsChequerefEpisOffline> findTrsCheq(long methodTrsId) throws SQLException {
		// TODO Auto-generated method stub
		return trsChequeRefManualDao.findByManualId(methodTrsId);
	}

	@Override
	public void updateStatusClearing(long manualId, String status) throws Exception {
		// TODO Auto-generated method stub
		paymentManualDao.udpateStatus(manualId, status);
	}

	@Override
	public List<OfflineResultModel> callOnlinePayment(List<PaymentMMapPaymentInvBean> creteria ,String system) {
		init();
		List<OfflineResultModel> objMessage = new ArrayList<OfflineResultModel>();
		int errorCount = 0;
		int successCount = 0;
		StringBuilder errorRecript = new StringBuilder();
		List<PaymentEpisOfflineDTO> PaymentEpisOfflineDTOList = new ArrayList<>();
		List<PaymentInvoiceEpisOffline> paymentList = new ArrayList<>();
		List<DuductionEpisOffline> deductionList = new ArrayList<>();
		List<TrsMethodEpisOffline> methodList = new ArrayList<>();
		List<TrsCreditrefEpisOffline> creditList = new ArrayList<>();
		List<TrsChequerefEpisOffline> chequeList = new ArrayList<>();
		TmpInvoiceBean invoid = new TmpInvoiceBean();
		Date startDate = new Date();
		String recNO = "";
		try {
			Boolean isOther = false;
			if (creteria != null) {
				for (PaymentMMapPaymentInvBean payment : creteria) {
					PaymentEpisOfflineDTO paymentEpisOfflineDTO = new PaymentEpisOfflineDTO();
					recNO = payment.getReceiptNoManual();
					Integer manualId = Integer.valueOf(payment.getManualId().toString());
					ReceiptOfflineModel recrip = findReciptStatus(manualId, payment.getRecordStatus());
					paymentEpisOfflineDTO.setManualID(manualId.toString());
					paymentEpisOfflineDTO.setPosNo(posNo);
					if (recrip != null) {
						paymentList = findPaymentInvoice(manualId);
						for (PaymentInvoiceEpisOffline pay : paymentList) {
							if ("OTHER".equals(pay.getServiceType())) {
								isOther = true;
							}
						}
						deductionList = findDeduction(manualId);
						methodList = findTrsMethod(manualId);
						if (!isOther) {
							invoid = tmpInvoiceService.findByManualId(manualId);
						}
						if (methodList.size() > 0 && methodList != null) {
							for (TrsMethodEpisOffline method : methodList) {
								if (method.getCode().equals("CH")) {
									chequeList = findTrsCheq(method.getId());
									method.setTrsChequerefEpisOffline(chequeList);
								} else if (method.getCode().equals("CR")) {
									creditList = findTrsCredit(method.getId());
									method.setTrsCreditrefEpisOffline(creditList);
								}
								// methodList.add(method);

							}
							paymentEpisOfflineDTO.setTrsMethod(methodList);
						}
						paymentEpisOfflineDTO.setAccountNo(recrip.getAccountNo());
						paymentEpisOfflineDTO.setReceiptNo(recrip.getReceiptNo());
						paymentEpisOfflineDTO.setBranchArea(recrip.getBranchArea());
						paymentEpisOfflineDTO.setBranchCode(branCode);
						paymentEpisOfflineDTO.setCustBranchCode(recrip.getBranchCode());
						paymentEpisOfflineDTO.setInvoiceNo(recrip.getInvoiceNo());
						paymentEpisOfflineDTO.setPaidDate(recrip.getPaidDate());
						paymentEpisOfflineDTO.setVatAmount(recrip.getVatAmount());
						paymentEpisOfflineDTO.setCreatBy(recrip.getCreatBy());
						if (!"Non-VAT".equals(paymentList.get(0).getVatRate())
								&& paymentList.get(0).getVatRate() != null) {
							paymentEpisOfflineDTO.setVatRate(new BigDecimal(paymentList.get(0).getVatRate()));
						} else {
							paymentEpisOfflineDTO.setVatRate(null);
						}

						if (isOther) {
							paymentEpisOfflineDTO.setPaidAmount(recrip.getPaidAmount());
						} else {
							paymentEpisOfflineDTO
									.setPaidAmount(recrip.getAmount().add(new BigDecimal(invoid.getDiscount())));
						}
						paymentEpisOfflineDTO.setSource(recrip.getSource());
						paymentEpisOfflineDTO.setRemark(recrip.getRemark());
						paymentEpisOfflineDTO.setManualID(manualId + "");
						List<PaymentInvoiceEpisOffline> paymentList2 = new ArrayList<>();
						if (!isOther) {
							for (PaymentInvoiceEpisOffline data : paymentList) {
								if ("Y".equals(invoid.getIsDiscountFlg())) {
									BigDecimal disVat = (new BigDecimal(invoid.getDiscount())
											.multiply(new BigDecimal(data.getVatRate())))
													.divide(new BigDecimal(data.getVatRate()));
									data.setDiscount(new BigDecimal(invoid.getDiscount()).subtract(disVat));
									data.setDiscountVat(disVat);
								} else {
									if (null != invoid.getDiscount()) {
										data.setDiscount(new BigDecimal(invoid.getDiscount()));
									} else {

										data.setDiscountVat(BigDecimal.ZERO);
									}
								}
								paymentList2.add(data);
							}
							paymentEpisOfflineDTO.setPaymentInvoice(paymentList2);
						} else {
							paymentEpisOfflineDTO.setPaymentInvoice(paymentList);
						}
						if (deductionList.size() > 0) {
							paymentEpisOfflineDTO.setDuduction(deductionList);
						}
						paymentEpisOfflineDTO.setChannel("OFFLINE");
						
						List<MasterDataBean> result = masterDataDao.findAllByGropType(Constants.MasterData.CUSTOMER_SEGMENT);
						
						String custgroup = result.stream().filter(x-> x.getProperty1().equals(recrip.getCustomerGroup()))
						.map(m-> m.getText())
						.findFirst().orElse("");
						
						paymentEpisOfflineDTO.setCustomerGroup(custgroup);
						paymentEpisOfflineDTO.setCreateDate(recrip.getCreateDate());
						paymentEpisOfflineDTO.setUpdateDate(recrip.getUpdateDate());
						paymentEpisOfflineDTO.setApproveBy(recrip.getApproveBy());
						PaymentEpisOfflineDTOList.add(paymentEpisOfflineDTO);
					}
					
				}

			}
			 String	postUrl = url.concat("/offline/paymentManualSaveOffline"); // /offline/insertPayment
			if(PaymentEpisOfflineDTOList.size() >0) {
				try {
					ResponseEntity<String> postResponse = restTemplate.postForEntity(postUrl, PaymentEpisOfflineDTOList,
							String.class);
		
					if (null != postResponse.getBody()) {
						JSONArray jsonArray = new JSONArray(postResponse.getBody());
						for (int i = 0; i < jsonArray.length(); i++) {
							OfflineResultModel obj = new OfflineResultModel();
							System.out.println("manualId :: " + jsonArray.getJSONObject(i).getLong("manualId"));
							obj.setManualId(jsonArray.getJSONObject(i).getLong("manualId"));
							obj.setMessage(jsonArray.getJSONObject(i).getString("message"));
							obj.setStatus(jsonArray.getJSONObject(i).getString("status"));
							obj.setRecriptNo(jsonArray.getJSONObject(i).getString("recriptNo"));
							if (("SUCCESS").equals(obj.getStatus())) {
								obj.setManualIdOnline(jsonArray.getJSONObject(i).getLong("manualIdOnline"));
								successCount++;
							}else {
								errorCount++;
								errorRecript.append(jsonArray.getJSONObject(i).getString("recriptNo")).append("|");
							}
							objMessage.add(obj);
						}
					}
				} catch (Exception e) {
					for (PaymentEpisOfflineDTO paymentEpisOfflineDTO : PaymentEpisOfflineDTOList) {
						errorCount++;
						errorRecript.append(paymentEpisOfflineDTO.getReceiptNo()).append(",");
					}
				}
				
			}
		} catch (Exception e) {

			e.printStackTrace();
			errorCount++;
			errorRecript.append(recNO).append(",");
		}finally {
			if(errorCount != 0 || successCount != 0 ) {
				TranferLogs log = new TranferLogs();
				log.setStartDate(new Timestamp(startDate.getTime()));
				log.setEndDate(new Timestamp(new Date().getTime()));
				log.setSystem(system);
				log.setErrorTask(errorCount);
				log.setSuccessTask(successCount);
				log.setErrorRecript(errorRecript.toString());
				tranferLogsRepository.save(log);
			}
			
		}

		return objMessage;
	}

	@Override
	public HashMap<String, Object> clearingCencelPayment() throws Exception {
		HashMap<String, Object> result = new HashMap<>();
		List<PaymentMMapPaymentInvBean> list = new ArrayList<>();
		List<PaymentDTO> dtoList = new ArrayList<>();
		init();
		String mac = GetMacAddress.getMACAddress();
		list = cancelPaymentService.findAllCancelPayments(Constants.CLEARING.STATUS_N);
		
		CancelPaymentDTO cancelDTO = new CancelPaymentDTO();
		String postUrl = "";
		if(list.size() > 0) {
		List<OfflineResultModel> objMessage = callOnlinePayment(list,"BATCH");
		if(CollectionUtils.isNotEmpty(objMessage))minusOnlineService.updateStatusForMinusOnline(list, Constants.CLEARING.STATUS_W);
		
		ResponseEntity<String> resultA;
		ResponseEntity<String> resultB = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		for (OfflineResultModel offlineResultModel : objMessage) {
			try {
				if (offlineResultModel.getStatus().equals("SUCCESS")) {

					for (PaymentMMapPaymentInvBean payment : list) {
						if (offlineResultModel.getManualId() == payment.getManualId()) {
							PaymentDTO manualDTO = new PaymentDTO();

							manualDTO.setAccountNo(payment.getAccountNo());
							manualDTO.setBranchCode(branCode);
							manualDTO.setBranchArea(payment.getBrancharea());
							manualDTO.setInvoiceNo(payment.getInvoiceNo());
							manualDTO.setReceiptNoManual(payment.getReceiptNoManual());
							manualDTO.setRemark(payment.getRemark());
							manualDTO.setManualId(offlineResultModel.getManualIdOnline());
							manualDTO.setCreateBy(payment.getCreateBy());
							manualDTO.setPaidAmount(payment.getPaidAmount());
							manualDTO.setRecordStatus("");
							manualDTO.setSource(payment.getSource());
							manualDTO.setVatAmount(payment.getVatAmount());
							manualDTO.setUserLogin(payment.getCreateBy());
							manualDTO.setUserName(payment.getCreateBy());
							manualDTO.setPaidDate(payment.getPaidDate());
							manualDTO.setPaidDateStr(dateFormat.format(payment.getPaidDate()));
							manualDTO.setPosNo(posNo);
							manualDTO.setBranchAreaCode(payment.getBranchAreaCode());
							manualDTO.setCencelFlag("Y");
							dtoList.add(manualDTO);
							payment.setCancelflag("F");
							cancelDTO = dtoCancel(payment);

							int listOtherSize = cancelDTO.getReceipts().stream()
									.filter(a -> a.getIsIbaiss().equalsIgnoreCase("OTHER")).collect(Collectors.toList()).size();

							if (dtoList.size() > 0) {
								postUrl = url.concat("/paymentManualServiceOnline.json?ap=OFFLINE&username="
										+ payment.getCreateBy() + "&mac=" + mac);
								resultA = restTemplate.postForEntity(postUrl, dtoList, String.class);
								System.out.println(resultA);
								TimeUnit.SECONDS.sleep(10);
								if (listOtherSize <= 0) {
									System.out.println("CANCEL IBASS");
									postUrl = url.concat("/cancelPaymentProduct2Offline.json?ap=OFFLINE&username="
											+ payment.getCreateBy() + "&mac=" + mac);
									System.out.println(postUrl);
									resultB = restTemplate.postForEntity(postUrl, cancelDTO, String.class);
									System.out.println(resultB);

								} else {
									System.out.println("CANCEL Other");
									postUrl = url.concat("/cancelPaymentOtherOffline.json?ap=OFFLINE&username="
											+ payment.getCreateBy() + "&mac=" + mac);
									System.out.println(postUrl);
									resultB = restTemplate.postForEntity(postUrl, cancelDTO, String.class);
									System.out.println(resultB);

								}

								updateStatusClearing(offlineResultModel.getManualId(), Constants.CLEARING.STATUS_Y);

							}
						}
					}

				} else {
					updateStatusClearing(offlineResultModel.getManualId(), Constants.CLEARING.STATUS_ERROR);
				}
			} catch (Exception e) {
				// e.printStackTrace();
				System.err.println("SEND DAT A EEROR :" + offlineResultModel.getManualId());
				System.err.println("SEND DATA EEROR :" + offlineResultModel.getRecriptNo());
				offlineResultModel.setStatus("ERROR");
				updateStatusClearing(offlineResultModel.getManualId(), Constants.CLEARING.STATUS_ERROR);
			}
		}

		result.put("data", objMessage);
		
		if(Constants.CLEARING.STATUS_W.equals(list.get(0).getClearing())) {
			minusOnlineService.updateStatusForMinusOnline(list, Constants.CLEARING.STATUS_N);
		}
		}
		return result;
	}

	public CancelPaymentDTO dtoCancel(PaymentMMapPaymentInvBean payment) {
		CancelPaymentDTO dto = new CancelPaymentDTO();
		Receipt rp = new Receipt();
		List<Receipt> rpList = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
		rp.setAccountName(payment.getCustomerName());
		rp.setAddrLine1(payment.getAddressNewCancelPayment());
		rp.setNo(payment.getReceiptNoManual());
		rp.setReasonCode(payment.getReasonCode());
		rp.setReasonDesc(payment.getReasonDesc());
		rp.setIsIbaiss(payment.getServiceType());
		rp.setCanceldate(dateFormat.format(payment.getCancleDate()));
		rp.setCancelBy(payment.getCancleBy());
		rpList.add(rp);
		dto.setReceipts(rpList);
		dto.setFlagCancel("Y");
		dto.setFlgNewReceipt(false);
		dto.setUserAuthen(payment.getCreateBy());
		dto.setCancelflagOffline("Y");
		return dto;
	}

}
