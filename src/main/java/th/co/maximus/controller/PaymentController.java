package th.co.maximus.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import th.co.maximus.bean.MasterDatasBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.core.utils.Utils;
import th.co.maximus.dao.MasterDatasDao;
import th.co.maximus.payment.bean.PaymentFirstBean;
import th.co.maximus.payment.bean.PaymentResultReq;
import th.co.maximus.service.MasterDataService;
import th.co.maximus.service.PaymentService;

@Controller
public class PaymentController {
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	MasterDataService masterDataService;
	
	@Autowired
	private MasterDatasDao masterDatasDao;

	@RequestMapping(value = "/gotoPayment", method = RequestMethod.GET)
	public String registration(Model model) {
		List<MasterDatasBean> bankCodeList = new ArrayList<>();
		List<MasterDatasBean> bankEDCList = new ArrayList<>();
		List<MasterDatasBean> vatRate = new ArrayList<>();
		List<MasterDatasBean> custSegment = new ArrayList<MasterDatasBean>();
		List<MasterDatasBean> creditTypeList = new ArrayList<MasterDatasBean>();

		bankCodeList = masterDatasDao.findByBankName();
		bankEDCList = masterDatasDao.findByBankEDCName();
		vatRate = masterDatasDao.findByVat();
//		custSegment = masterDataService.findByCMSegment();
		creditTypeList = masterDatasDao.findListByKey(Constants.MasterData.CREDIT_CARD_TYPE);

		model.addAttribute("bank", bankCodeList);
		model.addAttribute("bankEDC", bankEDCList);
		model.addAttribute("vat", vatRate);
		model.addAttribute("custSegment", custSegment);
		model.addAttribute("creditTypeList", creditTypeList);

		return "payment";
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/paymentSuccess", method = RequestMethod.GET)
	public String paymentSuccess(Model model, int idUser, HttpServletRequest request) throws Exception {
		PaymentResultReq paymentResultReq = new PaymentResultReq();
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		Utils utils = new Utils();
		if (idUser > 0) {
			paymentResultReq = paymentService.findByid(idUser);

			paymentResultReq.setBalanceSummary(paymentResultReq.getBalanceSummary().setScale(2, RoundingMode.HALF_DOWN));
			paymentResultReq.setBalanceOfvat(paymentResultReq.getBalanceOfvat().setScale(2, RoundingMode.HALF_DOWN));
			paymentResultReq.setVat(paymentResultReq.getVat().setScale(2, RoundingMode.HALF_DOWN));
			paymentResultReq.setBeforeVat(paymentResultReq.getBeforeVat().setScale(2, RoundingMode.HALF_DOWN));
			if (paymentResultReq.getDeduction() == null) {
				paymentResultReq.setDeduction(new BigDecimal(0).setScale(2, RoundingMode.HALF_DOWN));
				paymentResultReq.setDeductionStr("0.00");
			} else {
				paymentResultReq.setDeduction(paymentResultReq.getDeduction().setScale(2, RoundingMode.HALF_DOWN));
				paymentResultReq.setDeductionStr(
						String.format("%,.2f", paymentResultReq.getDeduction().setScale(2, RoundingMode.HALF_DOWN)));
			}

			BigDecimal price = paymentResultReq.getBalanceOfvat().setScale(2, RoundingMode.HALF_DOWN)
					.subtract(paymentResultReq.getBalanceSummary().setScale(2, RoundingMode.HALF_DOWN));

			if (price.compareTo(new BigDecimal("0.00")) < 0) {
				price = new BigDecimal("0.00");
			}

			paymentResultReq.setBalancePrice(price);
			paymentResultReq.setPeriod(utils.periodFormat(paymentResultReq.getPeriod()));

			Date date = paymentResultReq.getInvoiceDate();
			String invoiceDate = dt.format(date);

			Date dateLineDate = paymentResultReq.getDateLine();
			String dateLineSt = dt.format(dateLineDate);

			paymentResultReq.setInvoiceDateRS(invoiceDate);
			paymentResultReq.setDateLineRS(dateLineSt);

			paymentResultReq.setBalanceSummaryStr(
					String.format("%,.2f", paymentResultReq.getPaidAmount().setScale(2, RoundingMode.HALF_DOWN)));
			paymentResultReq.setBeforeVatStr(
					String.format("%,.2f", paymentResultReq.getBeforeVat().setScale(2, RoundingMode.HALF_DOWN)));
			paymentResultReq
					.setVatStr(String.format("%,.2f", paymentResultReq.getVat().setScale(2, RoundingMode.HALF_DOWN)));
			paymentResultReq.setBalanceOfvatStr(
					String.format("%,.2f", paymentResultReq.getBalanceOfvat().setScale(2, RoundingMode.HALF_DOWN)));
			paymentResultReq.setBalancePriceStr(String.format("%,.2f", price));
			paymentResultReq.setDiscountStr(
					String.format("%,.2f", paymentResultReq.getDiscount().setScale(2, RoundingMode.HALF_DOWN)));
			paymentResultReq.setManualId(idUser);
			request.setAttribute("paymentResultReq", paymentResultReq);
		}

		return "payment-success";
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/getDetailBilling/{manualId}", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody PaymentResultReq getBillingDetail(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("manualId") int manualId) throws Exception {
		Utils utils = new Utils();
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		PaymentResultReq paymentResultReq = new PaymentResultReq();
		paymentResultReq = paymentService.findByid(manualId);
		paymentResultReq.setBalanceSummary(paymentResultReq.getBalanceSummary().setScale(2, RoundingMode.HALF_DOWN));
		paymentResultReq.setBalanceOfvat(paymentResultReq.getBalanceOfvat().setScale(2, RoundingMode.HALF_DOWN));
		paymentResultReq.setVat(paymentResultReq.getVat().setScale(2, RoundingMode.HALF_DOWN));
		paymentResultReq.setBeforeVat(paymentResultReq.getBeforeVat().setScale(2, RoundingMode.HALF_DOWN));
		if (paymentResultReq.getDeduction() == null) {
			paymentResultReq.setDeduction(new BigDecimal(0).setScale(2, RoundingMode.HALF_DOWN));
			paymentResultReq.setDeductionStr("0.00");
		} else {
			paymentResultReq.setDeduction(paymentResultReq.getDeduction().setScale(2, RoundingMode.HALF_DOWN));
			paymentResultReq.setDeductionStr(
					String.format("%,.2f", paymentResultReq.getDeduction().setScale(2, RoundingMode.HALF_DOWN)));
		}

		BigDecimal price = paymentResultReq.getBalanceOfvat().setScale(2, RoundingMode.HALF_DOWN)
				.subtract(paymentResultReq.getBalanceSummary().setScale(2, RoundingMode.HALF_DOWN));

		if (price.compareTo(new BigDecimal("0.00")) < 0) {
			price = new BigDecimal("0.00");
		}

		paymentResultReq.setBalancePrice(price);
		paymentResultReq.setPeriod(utils.periodFormat(paymentResultReq.getPeriod()));

		Date date = paymentResultReq.getInvoiceDate();
		String invoiceDate = dt.format(date);

//		Date dateLineDate = paymentResultReq.getDateLine();
		Date dateLineDate = paymentResultReq.getDateLinePay();
		String dateLineSt = dt.format(dateLineDate);

		paymentResultReq.setInvoiceDateRS(invoiceDate);
		paymentResultReq.setDateLineRS(dateLineSt);

		if(null!=paymentResultReq.getDiscount() && paymentResultReq.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
			paymentResultReq.setBalanceSummaryStr(String.format("%,.2f", (paymentResultReq.getPaidAmount().subtract(paymentResultReq.getDiscount())).setScale(2, RoundingMode.HALF_DOWN)));
		}else {
			paymentResultReq.setBalanceSummaryStr(String.format("%,.2f", paymentResultReq.getPaidAmount().setScale(2, RoundingMode.HALF_DOWN)));
		}
		
		paymentResultReq.setBeforeVatStr(String.format("%,.2f", paymentResultReq.getBeforeVat().setScale(2, RoundingMode.HALF_DOWN)));
		paymentResultReq.setVatStr(String.format("%,.2f", paymentResultReq.getVat().setScale(2, RoundingMode.HALF_DOWN)));
		paymentResultReq.setBalanceOfvatStr(String.format("%,.2f", paymentResultReq.getBalanceOfvat().setScale(2, RoundingMode.HALF_DOWN)));
		paymentResultReq.setBalancePriceStr(String.format("%,.2f", price));
		paymentResultReq.setDiscountStr(String.format("%,.2f", paymentResultReq.getDiscount().setScale(2, RoundingMode.HALF_DOWN)));
		paymentResultReq.setManualId(manualId);
		return paymentResultReq;
	}

    @RequestMapping(value = "/paymentService", method = RequestMethod.POST)
	@ResponseBody
	public String payment(Model model, @RequestBody PaymentFirstBean paymentBean, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int paymentId = 0;
		try {
			paymentId = paymentService.insert(paymentBean);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return String.valueOf(paymentId);
	}
    
    @RequestMapping(value = { "/ibacss/find/usergroup" }, method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody List<MasterDatasBean> findUsergroup() {
		try {
			return  masterDataService.findByCMSegment();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
