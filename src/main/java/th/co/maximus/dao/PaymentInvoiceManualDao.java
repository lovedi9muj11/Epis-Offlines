package th.co.maximus.dao;

import java.util.List;

import th.co.maximus.bean.HistoryPaymentRS;
import th.co.maximus.bean.HistoryReportBean;
import th.co.maximus.bean.HistorySubFindBean;
import th.co.maximus.bean.InvoiceBean;
import th.co.maximus.bean.PaymentInvoiceManualBean;
import th.co.maximus.bean.PaymentMMapPaymentInvBean;
import th.co.maximus.model.PaymentInvoiceEpisOffline;

public interface PaymentInvoiceManualDao {
	
	public List<PaymentMMapPaymentInvBean> findPaymentMuMapPaymentInV();
	
	public List<PaymentMMapPaymentInvBean> findPaymentMuMapPaymentInVs(String clearing);
	
	public void insert(PaymentInvoiceManualBean paymentInvoiceManualBean);

	public List<PaymentMMapPaymentInvBean> findPaymentMuMapPaymentInVAccountId(String accountNo,String payType);
	
	public List<PaymentMMapPaymentInvBean> findPaymentMuMapPaymentInVFromId(long manual_id);
	
	public List<PaymentMMapPaymentInvBean> findCriteriaFromInvoiceOrReceiptNo(String receiptNo, String code, boolean chkCancel);
	
	public void updateRecodeStatusFromReceiptNo(String status, long manualId, String cancel, String  user);
	
	public void updateStatusPaymentInvoice(long manualId);
	
	public List<PaymentInvoiceManualBean> findPaymentInvoiceFromManualId(long manualId);
	
	List<PaymentMMapPaymentInvBean> findPayOrder(HistorySubFindBean historySubFindBean);
	
	List<PaymentMMapPaymentInvBean> findPayOrderFulln(HistorySubFindBean historySubFindBean);
	
	public List<HistoryPaymentRS> findPaymentOrder(HistoryReportBean historyRpt);
	
	public List<PaymentInvoiceEpisOffline> findByManualId(long manualId);
	
	public void insertInvoice(InvoiceBean invoice);
	
	public InvoiceBean findInvoiceByManualId(Long manualId);
	
	public PaymentInvoiceManualBean findInvoiceManualByManualId(Long manualId);
} 
