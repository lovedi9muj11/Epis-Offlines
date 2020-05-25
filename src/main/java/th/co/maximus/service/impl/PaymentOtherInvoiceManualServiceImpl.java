package th.co.maximus.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import th.co.maximus.auth.model.UserProfile;
import th.co.maximus.bean.PaymentInvoiceManualBean;
import th.co.maximus.dao.PaymentInvoiceManualDao;
import th.co.maximus.payment.bean.PaymentBillBean;
import th.co.maximus.payment.bean.PaymentOtherFirstBean;
import th.co.maximus.service.MasterDataService;
import th.co.maximus.service.PaymentOtherInvoiceManualService;

@Service
public class PaymentOtherInvoiceManualServiceImpl implements PaymentOtherInvoiceManualService {

	@Autowired
	PaymentInvoiceManualDao paymentInvoiceManualDao;
	
	@Autowired
	MasterDataService masterDataService;

	@Override
	public void insertPaymentInvoiceManual(PaymentOtherFirstBean paymentBean, int userId) {
		UserProfile profile = (UserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Date date = new Date();
		if (!paymentBean.getDocumentNo().equals("")) {
			for (int i = 0; i < paymentBean.getPaymentBill().size(); i++) {
				PaymentInvoiceManualBean paymentInvoiceManualBean = new PaymentInvoiceManualBean();
				PaymentBillBean paymentBillBean = new PaymentBillBean();
				paymentInvoiceManualBean.setManualId(Long.valueOf(userId));
				paymentInvoiceManualBean.setSource("OFFLINE");
				paymentInvoiceManualBean.setVatRate(paymentBean.getVatrate());
				paymentInvoiceManualBean.setCustomerName(paymentBean.getCustName());
				paymentInvoiceManualBean.setCustomerAddress(paymentBean.getCustAddress());
				paymentInvoiceManualBean.setCustomerSegment("1");
				paymentInvoiceManualBean.setCustomerBranch(paymentBean.getCustBrach());
				paymentInvoiceManualBean.setTaxNo(paymentBean.getTaxId());
				paymentInvoiceManualBean.setServiceType("OTHER");
				paymentInvoiceManualBean.setClearing("N");
				paymentInvoiceManualBean.setPrintReceipt("");
				paymentInvoiceManualBean.setRemark(paymentBean.getRemark());
				paymentInvoiceManualBean.setCreateBy(profile.getUsername());
				paymentInvoiceManualBean.setCreateDate(new Timestamp(date.getTime()));
				paymentInvoiceManualBean.setUpdateBy(profile.getUsername());
				paymentInvoiceManualBean.setUpdateDate(new Timestamp(date.getTime()));
				paymentInvoiceManualBean.setRecordStatus("A");

				
				paymentBillBean = paymentBean.getPaymentBill().get(i);
				paymentInvoiceManualBean.setQuantity(paymentBillBean.getInputServiceMoreData());
				//paymentInvoiceManualBean.setIncometype(paymentBillBean.getInputServiceType());
				paymentInvoiceManualBean.setDiscountbeforvat(paymentBillBean.getInputServiceDiscount());
				paymentInvoiceManualBean.setDiscountspecial(paymentBillBean.getInputSpecialDiscount());
				paymentInvoiceManualBean.setDepartment(paymentBean.getInputServiceDepartment());
				paymentInvoiceManualBean.setVatAmount(paymentBillBean.getVatSale().doubleValue());
				paymentInvoiceManualBean.setAmount(paymentBillBean.getSummaryinvoice().doubleValue());
				paymentInvoiceManualBean.setServiceCode(paymentBillBean.getInputServiceCode());
				paymentInvoiceManualBean.setBeforVat(paymentBillBean.getInputServiceAmount().doubleValue());
				paymentInvoiceManualBean.setAmounttype(paymentBillBean.getInputServiceType());
				paymentInvoiceManualBean.setServiceName(paymentBillBean.getInputServiceName());
				
				paymentInvoiceManualBean.setDepartmentcode(paymentBean.getInputServiceDepartment());
				paymentInvoiceManualBean.setIncomeunit(paymentBean.getIncomeEdit());
				
				paymentInvoiceManualBean.setDepartmentArea(masterDataService.findProperty1ByProperty2(paymentBean.getInputServiceDepartment()));
				paymentInvoiceManualDao.insert(paymentInvoiceManualBean);
			}
			
		}

	}

	@Override
	public List<PaymentInvoiceManualBean> PaymentInvoiceManualAll() {

		return null;
	}

}
