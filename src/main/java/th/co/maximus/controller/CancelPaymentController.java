package th.co.maximus.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import th.co.maximus.auth.model.UserDto;
import th.co.maximus.auth.service.UserService;
import th.co.maximus.bean.PaymentMMapPaymentInvBean;
import th.co.maximus.bean.UserBean;
import th.co.maximus.core.utils.Utils;
import th.co.maximus.payment.bean.PaymentResultReq;
import th.co.maximus.service.CancelPaymentService;
import th.co.maximus.service.PaymentService;

@Controller
public class CancelPaymentController {
	
	@Autowired
	private CancelPaymentService cancelPaymentService;
	
	@Autowired
	private PaymentService paymentService;

	// @Autowired
	// private Md5PasswordEncoder md5PasswordEncoder;

	@Autowired
	private UserService userService;

	@RequestMapping(value = { "/cancalPayment" }, method = RequestMethod.GET)
	public String usermgt(Model model) {
		return "cancel-payment-list";
	}

	@RequestMapping(value = { "/cancelPayment/find" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<PaymentMMapPaymentInvBean> findAll(@RequestBody PaymentMMapPaymentInvBean creteria) throws Exception {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<>();
//		if (!"".equals(creteria.getReceiptNoManual()) || !"".equals(creteria.getInvoiceNo())) {
			result = cancelPaymentService.serviceCriteriaFromInvoiceOrReceiptNo(creteria.getReceiptNoManual(),
					creteria.getInvoiceNo());
//		} else {
//			result = cancelPaymentService.findAllCancelPayment();
//		}
		return result;
	}

	@RequestMapping(value = { "/cancelPayment/findFromId" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<PaymentMMapPaymentInvBean> findAllFromId(@RequestBody PaymentMMapPaymentInvBean creteria)
			throws Exception {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<>();
		if (null != creteria.getManualId()) {
			result = cancelPaymentService.findAllCancelPaymentFromId(creteria.getManualId());
		}
		return result;
	}

	@RequestMapping(value = {
			"/cancelPayment/checkAuthentication" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public boolean checkAuthentication(@RequestBody UserBean user) {
		boolean result = false;
		if (user.getUserName() != "" && user.getPassword() != "") {
			UserDto resultUser = userService.findByUsername(user.getUserName());
			if (resultUser != null) {
				if (Utils.check(user.getPassword(), resultUser.getPassword())) {
					if(resultUser.getRoles().get(0).getName().equals("SUP")) {
						result = true;
					}
					
				}
			}
		}
		return result;
	}

	@RequestMapping(value = {"/cancelPayment/updateStatus" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public PaymentMMapPaymentInvBean cancelPayment(@RequestBody PaymentMMapPaymentInvBean creteria) {
		PaymentMMapPaymentInvBean paymentMMapPaymentInvBean = new PaymentMMapPaymentInvBean();
		PaymentResultReq paymentResultReq = cancelPaymentService.insertAndUpdateCancelPayment(creteria);
		paymentMMapPaymentInvBean.setReceiptNoManual(paymentResultReq.getDocumentNo());
		paymentMMapPaymentInvBean.setChkPaymentType(paymentResultReq.getChkPaymentType());
		return paymentMMapPaymentInvBean;
	}
	
	@RequestMapping(value = {"/searchReceiptNoById" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public PaymentResultReq searchReceiptNoById(@RequestBody PaymentResultReq creteria) {
		PaymentResultReq paymentResultReq = new PaymentResultReq();
		try {
			paymentResultReq = paymentService.findByid(creteria.getManualId());
		} catch (Exception e) {
			e.printStackTrace();
		}
	return paymentResultReq;
		}

}
