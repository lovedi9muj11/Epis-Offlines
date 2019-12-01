package th.co.maximus.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import th.co.maximus.auth.model.UserProfile;
import th.co.maximus.bean.MapGLBean;
import th.co.maximus.bean.MasterDataBean;
import th.co.maximus.bean.MasterDatasBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.dao.MapGLDao;
import th.co.maximus.dao.MasterDatasDao;
import th.co.maximus.payment.bean.PaymentOtherFirstBean;
import th.co.maximus.payment.bean.PaymentResultReq;
import th.co.maximus.service.MasterDataService;
import th.co.maximus.service.PaymentOtherService;

@Controller
public class PayOtherController {

	@Autowired
	private PaymentOtherService paymentOtherService;

	@Autowired
	MasterDataService masterDataService;

	@Autowired
	private MapGLDao mapGLDao;
	
	@Autowired
	MasterDatasDao masterDatasDao;
	

	@RequestMapping(value = { "/payOther" }, method = RequestMethod.GET)
	public String payOther(Model model) {
		UserProfile profile = (UserProfile)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<MasterDataBean> bankCodeList = new ArrayList<>();
		List<MasterDataBean> bankNameList = new ArrayList<>();
		List<MasterDataBean> categoryList = new ArrayList<>();
		List<MasterDataBean> serviceDepartmentList = new ArrayList<>();
		List<MasterDatasBean> bankEDCList = new ArrayList<>();
		List<MasterDatasBean> custSegment = new ArrayList<MasterDatasBean>();

		bankCodeList = masterDataService.findAllByBankCode();
		bankNameList = masterDataService.findAllByBankName();
		categoryList = masterDataService.findAllByCategory();
		serviceDepartmentList = masterDataService.findAllByServiceDepartment();
		bankEDCList = masterDatasDao.findByBankEDCName();
		custSegment = masterDataService.findByCMSegmentByCRM();
		// serviceTypeList = masterDataService.findAllByServiceType();
		List<MapGLBean> serviceTypeList = mapGLDao.findBySource(Constants.MasterData.OTHER);
		

		model.addAttribute("bankCode", bankCodeList);
		model.addAttribute("bankName", bankNameList);
		model.addAttribute("serviceType", serviceTypeList);
		model.addAttribute("serviceDepartment", serviceDepartmentList);
		model.addAttribute("category", categoryList);
		model.addAttribute("costcenter", profile.getCostCenter());
		model.addAttribute("bankEDC", bankEDCList);
		model.addAttribute("custSegment", custSegment);
		return "payOther";
	}

	@RequestMapping(value = { "/getServiceName/{id}" }, method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<MapGLBean> getServiceName(@PathVariable("id") String id) {
		List<MapGLBean> serviceNameList = mapGLDao.findByRevenuType(id);
		return serviceNameList;

	}
	
	@RequestMapping(value = { "/getvatRate" }, method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<MasterDataBean> getVatRate() {
		List<MasterDataBean> vat = masterDataService.findByVat();
		return vat;

	}

	@RequestMapping(value = "/paymenOthertService", method = RequestMethod.POST)
	@ResponseBody
	public String payment(Model model, @RequestBody PaymentOtherFirstBean paymentBean, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int paymentId = 0;

		try {
			paymentId = paymentOtherService.insert(paymentBean);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (paymentId > 0) {
			PaymentResultReq paymentResultReq = new PaymentResultReq();
			paymentResultReq = paymentOtherService.findByid(paymentId);
			request.setAttribute("paymentResultReq", paymentResultReq);
		}
		return String.valueOf(paymentId);
	}

	@RequestMapping(value = "/payOtherSuccess", method = RequestMethod.GET)
	public String paymentSuccess(Model model, int idUser, HttpServletRequest request) throws Exception {
		PaymentResultReq paymentResultReq = new PaymentResultReq();
		// SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");

		if (idUser > 0) {
			paymentResultReq = paymentOtherService.findByid(idUser);
			// paymentResultReq.setBalanceSummaryStr(commaformatter(paymentResultReq.getBalanceSummary()));
			// paymentResultReq.setBalanceOfvatStr(String.format("%,.2f",
			// paymentResultReq.getBalanceOfvat()));
			// paymentResultReq.setVatStr(String.format("%,.2f",
			// paymentResultReq.getVat()));
			// paymentResultReq.setBeforeVatStr(String.format("%,.2f",
			// paymentResultReq.getBeforeVat()));
			// paymentResultReq.setServiceName(paymentResultReq.getServiceName());
			// paymentResultReq.setServiceCode(paymentResultReq.getServiceCode());
			// paymentResultReq.setQuantity(paymentResultReq.getQuantity());
			// paymentResultReq.setPaid_amountStr(String.format("%,.2f",
			// paymentResultReq.getPaid_amount()));
			// paymentResultReq.setDiscountspacalStr(String.format("%,.2f",
			// paymentResultReq.getDiscountspacal()));

			paymentResultReq.setCustName(paymentOtherService.findListByid(new Long(idUser)).get(0).getCustName());
			paymentResultReq.setManualId(idUser);
			
			request.setAttribute("paymentResultReq", paymentResultReq);
		}

		return "payOther_1";
	}

	@RequestMapping(value = "/payOtherDetail/{manualId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<PaymentResultReq> payOtherDetail(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("manualId") int manualId) throws Exception {
		return paymentOtherService.findListByid(new Long(manualId));
	}

	public String commaformatter(BigDecimal bigDecimal) {

		DecimalFormat formatter = new DecimalFormat("#,###.00");
		String moneyformat = formatter.format(bigDecimal);

		return moneyformat;
	}

}
