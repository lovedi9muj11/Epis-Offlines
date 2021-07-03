package th.co.maximus.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import th.co.maximus.bean.PaymentMMapPaymentInvBean;
import th.co.maximus.model.DuductionEpisOffline;
import th.co.maximus.model.OfflineResultModel;
import th.co.maximus.model.PaymentInvoiceEpisOffline;
import th.co.maximus.model.ReceiptOfflineModel;
import th.co.maximus.model.TrsChequerefEpisOffline;
import th.co.maximus.model.TrsCreditrefEpisOffline;
import th.co.maximus.model.TrsMethodEpisOffline;

public interface ClearingPaymentEpisOfflineService {
	
	public ReceiptOfflineModel findRecipt(Integer manualId) throws SQLException;

	public List<PaymentInvoiceEpisOffline> findPaymentInvoice(Integer manualId) throws SQLException;

	public List<DuductionEpisOffline> findDeduction(Integer manualId) throws Exception;

	public List<TrsMethodEpisOffline> findTrsMethod(Integer manualId) throws Exception;

	public List<TrsCreditrefEpisOffline> findTrsCredit(long methodTrsId) throws Exception;

	public List<TrsChequerefEpisOffline> findTrsCheq(long methodTrsId) throws Exception;

	public void updateStatusClearing(long manualId,String status) throws Exception;
	
	public List<OfflineResultModel>  callOnlinePayment(List<PaymentMMapPaymentInvBean> creteria ,String system, String status);
	
	public ReceiptOfflineModel findReciptStatus(Integer manualId,String status) throws SQLException ;
	
	public HashMap<String, Object> clearingCencelPayment() throws Exception ;
}
