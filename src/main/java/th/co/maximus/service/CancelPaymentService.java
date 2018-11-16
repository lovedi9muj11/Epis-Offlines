package th.co.maximus.service;

import java.util.List;

import th.co.maximus.bean.DropDownBean;
import th.co.maximus.bean.PaymentMMapPaymentInvBean;
import th.co.maximus.payment.bean.PaymentResultReq;

public interface CancelPaymentService {
	
	public List<PaymentMMapPaymentInvBean> findAllCancelPayment() throws Exception;
	
	public List<PaymentMMapPaymentInvBean> findAllCancelPaymentFromId(Long manualId) throws Exception;
	
	public List<PaymentMMapPaymentInvBean> serviceCriteriaFromInvoiceOrReceiptNo(String receiptNo, String invoiceNo, boolean chkCancel) throws Exception;
	
	public PaymentResultReq insertAndUpdateCancelPayment(PaymentMMapPaymentInvBean paymentInvBean);
	
	public List<PaymentMMapPaymentInvBean> findAllCancelPayments(String clearing) throws Exception;

	List<DropDownBean> reasonCancelIbacss();
	
	List<DropDownBean> reasonCancelOther();
	
}
