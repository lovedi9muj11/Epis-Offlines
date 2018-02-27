package th.co.maximus.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.maximus.bean.HistorySubFindBean;
import th.co.maximus.bean.PaymentMMapPaymentInvBean;
import th.co.maximus.dao.PaymentInvoiceManualDao;
import th.co.maximus.service.HistoryPaymentService;

@Service
public class HistoryPaymentServiceImp implements HistoryPaymentService {
	SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

	@Autowired
	private PaymentInvoiceManualDao paymentInvoiceManualDao;

	@Override
	public List<PaymentMMapPaymentInvBean> servicePaymentHitrory() {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<PaymentMMapPaymentInvBean>();
		for(PaymentMMapPaymentInvBean paymentMMapPaymentInvBean :paymentInvoiceManualDao.findPaymentMuMapPaymentInV()) {
			paymentMMapPaymentInvBean.setPaidDateStr(dt.format(paymentMMapPaymentInvBean.getPaidDate()));
			paymentMMapPaymentInvBean.setCreateDateStr(dt.format(paymentMMapPaymentInvBean.getCreateDate()));
			result.add(paymentMMapPaymentInvBean);
		}
		return result;
	}

	@Override
	public List<PaymentMMapPaymentInvBean> serviceHistroryPaymentFromAccountNo(String accountNo) {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<PaymentMMapPaymentInvBean>();
		for (PaymentMMapPaymentInvBean paymentMMapPaymentInvBean : paymentInvoiceManualDao.findPaymentMuMapPaymentInVAccountId(accountNo)) {
			paymentMMapPaymentInvBean.setPaidDateStr(dt.format(paymentMMapPaymentInvBean.getPaidDate()));
			paymentMMapPaymentInvBean.setCreateDateStr(dt.format(paymentMMapPaymentInvBean.getCreateDate()));
			result.add(paymentMMapPaymentInvBean);
		}
		return result;

	}

	@Override
	public List<PaymentMMapPaymentInvBean> findPayOrder(HistorySubFindBean paymentInvBean) {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<PaymentMMapPaymentInvBean>();
		SimpleDateFormat smp = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		
		try {
			paymentInvBean.setPayDateTo(new java.sql.Date (smp.parse(smp.format((paymentInvBean.getPayDateTo()))).getTime()));
			paymentInvBean.setPayDate(new java.sql.Date (smp.parse(smp.format((paymentInvBean.getPayDate()))).getTime()));
			result = paymentInvoiceManualDao.findPayOrder(paymentInvBean);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public List<PaymentMMapPaymentInvBean> findPayOrderFulln(HistorySubFindBean paymentInvBean) {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<PaymentMMapPaymentInvBean>();
		SimpleDateFormat smp = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
		
		try {
			paymentInvBean.setPayDateTo(new java.sql.Date (smp.parse(smp.format((paymentInvBean.getPayDateTo()))).getTime()));
			paymentInvBean.setPayDate(new java.sql.Date (smp.parse(smp.format((paymentInvBean.getPayDate()))).getTime()));
			result = paymentInvoiceManualDao.findPayOrderFulln(paymentInvBean);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
