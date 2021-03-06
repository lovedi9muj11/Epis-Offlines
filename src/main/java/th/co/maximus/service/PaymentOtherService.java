package th.co.maximus.service;

import java.util.List;

import th.co.maximus.bean.CasualCustomerBean;
import th.co.maximus.payment.bean.PaymentOtherFirstBean;
import th.co.maximus.payment.bean.PaymentResultReq;

public interface PaymentOtherService {
	public int insert(PaymentOtherFirstBean paymentBean);

	public void delete(String id);

	public void update(PaymentOtherFirstBean paymentBean);

	public PaymentResultReq findByid(int id) throws Exception;

	public List<PaymentResultReq> findListByid(Long id) throws Exception;

	public void saveCasualOther(CasualCustomerBean bean) throws Exception;
	
	public CasualCustomerBean findCasualByTaxId(String taxId) throws Exception;
	
	public List<CasualCustomerBean> findCasualByTaxIdNName(String taxId, String name) throws Exception;
}
