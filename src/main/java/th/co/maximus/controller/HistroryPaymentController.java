package th.co.maximus.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import th.co.maximus.bean.HistoryPaymentRS;
import th.co.maximus.bean.HistoryReportBean;
import th.co.maximus.bean.HistorySubFindBean;
import th.co.maximus.bean.PaymentInvoiceManualBean;
import th.co.maximus.bean.PaymentMMapPaymentInvBean;
import th.co.maximus.bean.PaymentManualBean;
import th.co.maximus.dao.CancelPaymentDTO;
import th.co.maximus.dao.CancelPaymentDTO.Receipt;
import th.co.maximus.dao.PaymentManualDao;
import th.co.maximus.model.OfflineResultModel;
import th.co.maximus.model.PaymentDTO;
import th.co.maximus.service.CancelPaymentService;
import th.co.maximus.service.ClearingPaymentEpisOfflineService;
import th.co.maximus.service.HistoryPaymentService;

@Controller
public class HistroryPaymentController {
	@Value("${url.online}")
	private String url;

	RestTemplate restTemplate;
	
	public HistroryPaymentController() {
		restTemplate = new RestTemplate();
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

		result = paymentManualService.serviceHistroryPaymentFromAccountNo(creteria.getAccountNo(), "IBACSS");

		// }
		return result;
	}
	
	@RequestMapping(value = { "/histroryPaymentByother/find" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<PaymentMMapPaymentInvBean> findByother(@RequestBody PaymentMMapPaymentInvBean creteria) throws Exception {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<>();
		// if("".equals(creteria.getAccountNo())) {
		// result = cancelPaymentService.findAllCancelPayment();
		// }else {

		result = paymentManualService.serviceHistroryPaymentFromAccountNo(creteria.getAccountNo(),"OTHER");

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

			resultRQ = paymentManualService.findPaymentOrder(creteria);

			if (resultRQ.size() > 0) {
				for (int i = 0; i < resultRQ.size(); i++) {
					HistoryPaymentRS hisPay = resultRQ.get(i);
					BigDecimal reVat = new BigDecimal(hisPay.getVatRate());

					BigDecimal amount = hisPay.getPaidAmount();
					BigDecimal vatRQ = amount.multiply(new BigDecimal(hisPay.getVatRate()));
					BigDecimal vat = vatRQ.divide(reVat, 2, RoundingMode.HALF_UP);

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

	@RequestMapping(value = {"/histroryPayment/findInvoiceByManualId" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public PaymentInvoiceManualBean findInvoiceByManualId(@RequestBody PaymentMMapPaymentInvBean creteria)
			throws Exception {
		return paymentManualService.findInvoiceManuleByManualIdService(creteria.getManualId());
	}
	
	@RequestMapping(value = {"/histroryPayment/findInvoiceByManualIdCancel" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public PaymentManualBean findInvoiceByManualIdCancel(@RequestBody PaymentMMapPaymentInvBean creteria) throws Exception {
		List<PaymentManualBean> paymentManual = paymentManualDao.findPaymentManualFromNanualId(creteria.getManualId());
		return paymentManual.get(0);
//		return paymentManualService.findInvoiceManuleByManualIdService(creteria.getManualId());
	}

	@RequestMapping(value = { "/histroryPayment/clearing" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public HashMap<String, Object> clearing(@RequestBody List<PaymentMMapPaymentInvBean> creteria) throws Exception {
		HashMap<String, Object> result = new HashMap<>();
		List<OfflineResultModel> objMessage = clearingPaymentEpisOfflineService.callOnlinePayment(creteria);
		try {
			
			for (OfflineResultModel offlineResultModel : objMessage) {
				if (offlineResultModel.getStatus().equals("SUCCESS")) {
					
					clearingPaymentEpisOfflineService.updateStatusClearing(offlineResultModel.getManualId(),"Y");
				}else {
					clearingPaymentEpisOfflineService.updateStatusClearing(offlineResultModel.getManualId(),"N");
				}
			}
				
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("data", objMessage);
		return result;
	}
	
	@RequestMapping(value = { "/histroryPayment/cancelPaymentOnline" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public HashMap<String, Object> cancelPaymentOnline() throws Exception {
		HashMap<String, Object> result = new HashMap<>();
		List<PaymentMMapPaymentInvBean> list = new ArrayList<>();
		List<PaymentDTO> dtoList = new ArrayList<>();
		list = cancelPaymentService.findAllCancelPayments("C");
		CancelPaymentDTO cancelDTO = new CancelPaymentDTO();
		String postUrl = "";
		List<OfflineResultModel> objMessage = clearingPaymentEpisOfflineService.callOnlinePayment(list);
		try {
			
			for (OfflineResultModel offlineResultModel : objMessage) {
				if (offlineResultModel.getStatus().equals("SUCCESS")) {
					
					for(PaymentMMapPaymentInvBean payment : list) {
						if(offlineResultModel.getManualId() == payment.getManualId()) {
							PaymentDTO manualDTO = new PaymentDTO();
							
							manualDTO.setAccountNo(payment.getAccountNo());
							manualDTO.setBranchCode(payment.getBranchCode());
							manualDTO.setBranchArea(payment.getBrancharea());
							manualDTO.setInvoiceNo(payment.getInvoiceNo());
							manualDTO.setReceiptNoManual(payment.getReceiptNoManual());
							manualDTO.setRemark(payment.getRemark());
							manualDTO.setManualId(payment.getManualId());
							manualDTO.setCreateBy("EPIS5");
							manualDTO.setCreateDate(new Date());
							manualDTO.setPaidAmount(payment.getPaidAmount());
							manualDTO.setPaidDate(payment.getPaidDate());
							manualDTO.setRecordStatus("");
							manualDTO.setSource(payment.getSource());
							manualDTO.setVatAmount(payment.getVatAmount());
							dtoList.add(manualDTO);
							
							cancelDTO = dtoCancel(payment);
						}
					}
					if(dtoList.size()>0) {
						// หักล้าง
						postUrl = url.concat("/offlineCancel/paymentManualCancelOnline");
						restTemplate.postForEntity(postUrl, dtoList, String.class);
						
						// ยกเลิก
						postUrl = url.concat("/offlineCancel/cancelPaymentProductOffline");
						restTemplate.postForEntity(postUrl, cancelDTO, String.class);
					}
					
					
					clearingPaymentEpisOfflineService.updateStatusClearing(offlineResultModel.getManualId(),"Y");
				}else {
					clearingPaymentEpisOfflineService.updateStatusClearing(offlineResultModel.getManualId(),"N");
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
		rpList.add(rp);
		dto.setReceipts(rpList);
		dto.setFlagCancel("Y");
		dto.setFlgNewReceipt(false);
		dto.setUserAuthen(payment.getCreateBy());
		
		return dto;
	}

}
