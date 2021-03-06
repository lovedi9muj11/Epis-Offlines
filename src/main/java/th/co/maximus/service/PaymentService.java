package th.co.maximus.service;

import th.co.maximus.payment.bean.PaymentFirstBean;
import th.co.maximus.payment.bean.PaymentResultReq;

public interface PaymentService {
	
	public int insert(PaymentFirstBean paymentBean);
	public void delete(String id);
	public void update(PaymentFirstBean paymentBean);
	public PaymentResultReq findByid(int id)throws Exception;
	public PaymentResultReq findIdGenCode(int id)throws Exception;
}
