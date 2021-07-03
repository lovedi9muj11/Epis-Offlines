package th.co.maximus.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import th.co.maximus.auth.model.UserProfile;
import th.co.maximus.bean.HistoryPaymentRS;
import th.co.maximus.bean.HistoryReportBean;
import th.co.maximus.bean.HistorySubFindBean;
import th.co.maximus.bean.PaymentInvoiceManualBean;
import th.co.maximus.bean.PaymentMMapPaymentInvBean;
import th.co.maximus.bean.PaymentManualBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.dao.CancelPaymentDTO;
import th.co.maximus.dao.CancelPaymentDTO.Receipt;
import th.co.maximus.dao.PaymentManualDao;
import th.co.maximus.model.OfflineResultModel;
import th.co.maximus.model.PaymentDTO;
import th.co.maximus.service.CancelPaymentService;
import th.co.maximus.service.ClearingPaymentEpisOfflineService;
import th.co.maximus.service.HistoryPaymentService;
import th.co.maximus.service.MinusOnlineService;
import th.co.maximus.util.GetMacAddress;

@Controller
public class HistroryPaymentController {
	@Value("${url.online}")
	private String url;
	private final SSLContext sslContext;
	private final SSLConnectionSocketFactory csf;
	private final HttpComponentsClientHttpRequestFactory requestFactory;
//	
	RestTemplate restTemplate;

	public HistroryPaymentController() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
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

	@Autowired
	private HistoryPaymentService paymentManualService;

	@Autowired
	private ClearingPaymentEpisOfflineService clearingPaymentEpisOfflineService;

	// @Autowired
	// private CancelPaymentService cancelPaymentService;

	@Autowired
	private PaymentManualDao paymentManualDao;

	@Autowired
	private CancelPaymentService cancelPaymentService;
	
	@Autowired MinusOnlineService minusOnlineService;

	@RequestMapping(value = { "/gotoHistroryPayment" }, method = RequestMethod.GET)
	public String gotoHistroryPayment(Model model) {
		return "history-payment";
	}

	@RequestMapping(value = { "/gotoHistroryPaymentother" }, method = RequestMethod.GET)
	public String gotoHistroryPaymentother(Model model) {
		return "history-payment-other";
	}

	@RequestMapping(value = { "/histroryPayment/find" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<PaymentMMapPaymentInvBean> find(@RequestBody PaymentMMapPaymentInvBean creteria) throws Exception {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<>();
		// if("".equals(creteria.getAccountNo())) {
		// result = cancelPaymentService.findAllCancelPayment();
		// }else {

		result = paymentManualService.serviceHistroryPaymentFromAccountNoSearch(creteria, Constants.Service.SERVICE_TYPE_IBACSS);

		// }
		return result;
	}

	@RequestMapping(value = {"/histroryPaymentByother/find" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<PaymentMMapPaymentInvBean> findByother(@RequestBody PaymentMMapPaymentInvBean creteria)
			throws Exception {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<>();
		// if("".equals(creteria.getAccountNo())) {
		// result = cancelPaymentService.findAllCancelPayment();
		// }else {

		result = paymentManualService.serviceHistroryPaymentFromAccountNoSearch(creteria, Constants.Service.SERVICE_TYPE_OTHER);

		// }
		return result;
	}

	@RequestMapping(value = { "/histroryPayment/payOrder" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<PaymentMMapPaymentInvBean> payOrder(@RequestBody HistorySubFindBean creteria) {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<PaymentMMapPaymentInvBean>();
		if (creteria != null) {
			result = paymentManualService.findPayOrder(creteria);
		}
		return result;
	}

	@RequestMapping(value = {"/histroryPayment/paymentPrint" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<HistoryPaymentRS> paymentPrint(@RequestBody HistoryReportBean creteria) throws SQLException {
		List<HistoryPaymentRS> resultRQ = new ArrayList<HistoryPaymentRS>();
		List<HistoryPaymentRS> result = new ArrayList<HistoryPaymentRS>();

		if (creteria != null) {
			UserProfile profile = (UserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			creteria.setChkReportTax(true);
			
			Integer supCh = 0;
			
			if(StringUtils.isNotBlank(profile.getUsername())) {
				if(!"".equals(profile.getUsername())) {
					supCh = paymentManualDao.checkSup(profile.getUsername());
				}else {
					supCh = 2;
				}
			}
			
			if(supCh == 2) {
				creteria.setUnserLogin("");
			}else {
				creteria.setUnserLogin(profile.getUsername());
			}
			
			resultRQ = paymentManualService.findPaymentOrder(creteria);

			if (resultRQ.size() > 0) {
				for (int i = 0; i < resultRQ.size(); i++) {
					HistoryPaymentRS hisPay = resultRQ.get(i);
//					BigDecimal reVat = new BigDecimal(hisPay.getVatRate());

					BigDecimal amount = hisPay.getAmount();
//					BigDecimal vatRQ = amount.multiply(new BigDecimal(hisPay.getVatRate()));
					BigDecimal vat = hisPay.getVatAmount();
					
//					if (vatRQ.compareTo(BigDecimal.ZERO) > 0) {
//						vat = vatRQ.divide(reVat, 2, RoundingMode.HALF_UP);
//					}

					BigDecimal beforeVat = amount.subtract(vat);

					hisPay.setVat(vat.setScale(2, RoundingMode.HALF_DOWN));
					hisPay.setBeforeVat(beforeVat.setScale(2, RoundingMode.HALF_DOWN));
					hisPay.setPaidAmount(amount.setScale(2, RoundingMode.HALF_DOWN));
					hisPay.setNumberRun(String.valueOf(i + 1));
					hisPay.setVatRate(hisPay.getVatRate());

					result.add(hisPay);

				}

			}
		}
		return result;
	}

	@RequestMapping(value = {
			"/histroryPayment/findInvoiceByManualId" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public PaymentInvoiceManualBean findInvoiceByManualId(@RequestBody PaymentMMapPaymentInvBean creteria)
			throws Exception {
		return paymentManualService.findInvoiceManuleByManualIdService(creteria.getManualId());
	}

	@RequestMapping(value = {
			"/histroryPayment/findInvoiceByManualIdCancel" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public PaymentManualBean findInvoiceByManualIdCancel(@RequestBody PaymentMMapPaymentInvBean creteria)
			throws Exception {
		List<PaymentManualBean> paymentManual = paymentManualDao.findPaymentManualFromNanualId(creteria.getManualId());
		return paymentManual.get(0);
//		return paymentManualService.findInvoiceManuleByManualIdService(creteria.getManualId());
	}

	@RequestMapping(value = { "/histroryPayment/clearing" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public HashMap<String, Object> clearing(@RequestBody List<PaymentMMapPaymentInvBean> creteria) throws Exception {
		HashMap<String, Object> result = new HashMap<>();
		UserProfile profile = (UserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(CollectionUtils.isNotEmpty(creteria)) {
			List<OfflineResultModel> objMessage = clearingPaymentEpisOfflineService.callOnlinePayment(creteria,profile.getUsername(), Constants.CLEARING.STATUS_PAYMENT);
			if(CollectionUtils.isNotEmpty(objMessage))minusOnlineService.updateStatusForMinusOnline(creteria, Constants.CLEARING.STATUS_W);
			
			try {
	
				for (OfflineResultModel offlineResultModel : objMessage) {
					if (offlineResultModel.getStatus().equals("SUCCESS")) {
	
						clearingPaymentEpisOfflineService.updateStatusClearing(offlineResultModel.getManualId(), "Y");
					} else {
						clearingPaymentEpisOfflineService.updateStatusClearing(offlineResultModel.getManualId(), "N");
					}
				}
	
			} catch (Exception e) {
				e.printStackTrace();
			}
			result.put("data", objMessage);
			
			if(Constants.CLEARING.STATUS_W.equals(creteria.get(0).getClearing())) {
				minusOnlineService.updateStatusForMinusOnline(creteria, Constants.CLEARING.STATUS_N);
			}
		}
		
		return result;
	}

	@RequestMapping(value = {
			"/histroryPayment/cancelPaymentOnline" }, method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public HashMap<String, Object> cancelPaymentOnline() throws Exception {
		HashMap<String, Object> result = new HashMap<>();
		List<PaymentMMapPaymentInvBean> list = new ArrayList<>();
		list = cancelPaymentService.findAllCancelPayments("N");
		String mac = GetMacAddress.getMACAddress();
		String postUrl = "";
		UserProfile profile = (UserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<OfflineResultModel> objMessage = clearingPaymentEpisOfflineService.callOnlinePayment(list,profile.getUsername(), Constants.CLEARING.STATUS_CANCEL);
		try {

			for (OfflineResultModel offlineResultModel : objMessage) {
				if (offlineResultModel.getStatus().equals("SUCCESS")) {

					for (PaymentMMapPaymentInvBean payment : list) {
						if (offlineResultModel.getManualId() == payment.getManualId()) {
							List<PaymentDTO> dtoList = new ArrayList<>();
							CancelPaymentDTO cancelDTO = new CancelPaymentDTO();
							PaymentDTO manualDTO = new PaymentDTO();

							manualDTO.setAccountNo(payment.getAccountNo());
							manualDTO.setBranchCode(payment.getBranchCode());
							manualDTO.setBranchArea(payment.getBrancharea());
							manualDTO.setInvoiceNo(payment.getInvoiceNo());
							manualDTO.setReceiptNoManual(payment.getReceiptNoManual());
							manualDTO.setRemark(payment.getRemark());
							manualDTO.setManualId(offlineResultModel.getManualIdOnline());
							manualDTO.setCreateBy(payment.getCreateBy());
//							manualDTO.setCreateDate(new Date());
							manualDTO.setPaidAmount(payment.getPaidAmount());
//							manualDTO.setPaidDate(payment.getPaidDate());
							manualDTO.setRecordStatus("");
							manualDTO.setSource(payment.getSource());
							manualDTO.setVatAmount(payment.getVatAmount());
//							manualDTO.setBranchCode(branchCode);
//							manualDTO.setBranchName(branchName);
//							manualDTO.setOfficerId(officerId);
//							manualDTO.setPosNo(posNo);
//							manualDTO.setPosId(posId);
							manualDTO.setUserLogin(payment.getCreateBy());
							manualDTO.setUserName(payment.getCreateBy());
							dtoList.add(manualDTO);

							cancelDTO = dtoCancel(payment);
							if (dtoList.size() > 0) {
//								cancelPaymentDTO.set
//								cancelOfflineDTO.setManualDTO(dtoList);

//								cancelOfflineDTO.setUserLogin(userLogin);
//								cancelOfflineDTO.setUserName(userName);
								// หักล้าง
								postUrl = url
										.concat("/offlineCancel/paymentManualCancelOnline.json?ap=OFFLINE&username="+ payment.getCreateBy()+"&mac="+mac);
//								restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("EPIS5", "password"));
//								ResponseEntity<String> clearing = restTemplate.postForEntity(postUrl, dtoList, String.class);
								restTemplate.postForEntity(postUrl, dtoList, String.class);
								// ยกเลิก
								postUrl = url
										.concat("/offlineCancel/cancelPaymentProductOffline.json?ap=OFFLINE&username="+ payment.getCreateBy()+"&mac="+mac);
//								ResponseEntity<String> cancel = restTemplate.postForEntity(postUrl, cancelDTO, String.class);
								restTemplate.postForEntity(postUrl, cancelDTO, String.class);
								clearingPaymentEpisOfflineService.updateStatusClearing(offlineResultModel.getManualId(), "Y");

							}
						}
					}
					

				} else {
					clearingPaymentEpisOfflineService.updateStatusClearing(offlineResultModel.getManualId(), "N");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("data", objMessage);
		return result;
	}

	public CancelPaymentDTO dtoCancel(PaymentMMapPaymentInvBean payment) {
		CancelPaymentDTO dto = new CancelPaymentDTO();
		Receipt rp = new Receipt();
		List<Receipt> rpList = new ArrayList<>();
		rp.setAccountName(payment.getCustomerName());
		rp.setAddrLine1(payment.getAddressNewCancelPayment());
		rp.setNo(payment.getReceiptNoManual());
		rp.setReasonCode(payment.getReasonCode());
		rp.setIsIbaiss(payment.getServiceType());
		rpList.add(rp);
		dto.setReceipts(rpList);
		dto.setFlagCancel("Y");
		dto.setFlgNewReceipt(false);
		dto.setUserAuthen(payment.getCreateBy());

		return dto;
	}

	@RequestMapping(value = { "/histroryPayment/test" }, method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<String> test() throws Exception {

//		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor("backofficer01", "password"));
		ResponseEntity<String> clearing = restTemplate.getForEntity(
				"https://epis.cattelecom.com:8081/EpisWeb/findPosDetailById.json?ap=SSO&un=backofficer01&pw=password", String.class);
		return clearing;
	}
	// http://localhost:8080/EpisWeb/findPosDetailById.json

}
