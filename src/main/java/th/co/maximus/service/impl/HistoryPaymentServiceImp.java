package th.co.maximus.service.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.maximus.bean.HistoryPaymentRS;
import th.co.maximus.bean.HistoryReportBean;
import th.co.maximus.bean.HistorySubFindBean;
import th.co.maximus.bean.PaymentInvoiceManualBean;
import th.co.maximus.bean.PaymentMMapPaymentInvBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.core.utils.Utils;
import th.co.maximus.dao.MasterDatasDao;
import th.co.maximus.dao.PaymentInvoiceManualDao;
import th.co.maximus.dao.TrsChequeRefManualDao;
import th.co.maximus.dao.TrsMethodManualDao;
import th.co.maximus.dao.TrscreDitrefManualDao;
import th.co.maximus.model.TrsChequerefEpisOffline;
import th.co.maximus.model.TrsCreditrefEpisOffline;
import th.co.maximus.model.TrsMethodEpisOffline;
import th.co.maximus.service.HistoryPaymentService;

@Service
public class HistoryPaymentServiceImp implements HistoryPaymentService {
	Locale localeTH = new Locale("th", "TH");
	SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", localeTH);

	@Autowired
	private PaymentInvoiceManualDao paymentInvoiceManualDao;

	@Autowired
	private TrsMethodManualDao trsMethodManualDao;
	
	@Autowired
	private TrsChequeRefManualDao  trsChequeRefManualDao;
	
	@Autowired
	private TrscreDitrefManualDao  trscreDitrefManualDao;
	
	@Autowired
	private MasterDatasDao masterDatasDao;

	@Override
	public List<PaymentMMapPaymentInvBean> servicePaymentHitrory() {

		return paymentInvoiceManualDao.findPaymentMuMapPaymentInV();
	}

	@Override
	public List<PaymentMMapPaymentInvBean> serviceHistroryPaymentFromAccountNo(String accountNo,String payType) throws Exception {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<>();
		for (PaymentMMapPaymentInvBean bean : paymentInvoiceManualDao.findPaymentMuMapPaymentInVAccountId(accountNo,payType)) {
			if ("N".equals(bean.getClearing()) && "A".equals(bean.getRecordStatus())) {
				List<TrsMethodEpisOffline> methodResult = trsMethodManualDao.findByManualId(Long.valueOf(bean.getManualId()));
				StringBuffer paymentMethod = new StringBuffer();
				for (TrsMethodEpisOffline method : methodResult) {

					if("CH".equals(method.getCode())) {
						List<TrsChequerefEpisOffline> list = trsChequeRefManualDao.findByManualId(method.getId());
						
						if(CollectionUtils.isNotEmpty(list)) {
							for(int i=0; i<list.size(); i++) {
								paymentMethod.append("+ " + method.getName()+" "+list.get(i).getPublisher()+" "+list.get(i).getChequeNo());
							}
						}
						
					}else if("CR".equals(method.getCode())) {
						List<TrsCreditrefEpisOffline> list = trscreDitrefManualDao.findByMethodId(method.getId());
						
						if(CollectionUtils.isNotEmpty(list)) {
							for(int i=0; i<list.size(); i++) {
								paymentMethod.append("+ " + method.getName()+" "+list.get(i).getCardtype().toUpperCase()+" ************"+list.get(i).getCreditNo().substring(12));
							}
						}
						
					}else {
						String methodName = method.getName();
						if(Constants.Status.METHOD_WT_STR.equalsIgnoreCase(method.getName())) {
							methodName = Constants.Status.METHOD_WT;
						}
						paymentMethod.append("+ " + methodName);
					}
				}
				if (null != bean.getPeriod()) {
					bean.setPeriod(Utils.periodFormat(bean.getPeriod()));
				}
				bean.setBrancharea(masterDatasDao.findByKey(bean.getBrancharea()).getValue());
				bean.setCreateDateStr(dt.format(bean.getCreateDate()));
				bean.setPaymentMethod(paymentMethod.toString().substring(1));
				result.add(bean);
			}
		}
		return result;

	}

	@Override
	public List<PaymentMMapPaymentInvBean> findPayOrder(HistorySubFindBean paymentInvBean) {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<PaymentMMapPaymentInvBean>();
		SimpleDateFormat smp = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

		try {
			paymentInvBean
					.setPayDateTo(new java.sql.Date(smp.parse(smp.format((paymentInvBean.getPayDateTo()))).getTime()));
			paymentInvBean
					.setPayDate(new java.sql.Date(smp.parse(smp.format((paymentInvBean.getPayDate()))).getTime()));
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
			paymentInvBean
					.setPayDateTo(new java.sql.Date(smp.parse(smp.format((paymentInvBean.getPayDateTo()))).getTime()));
			paymentInvBean
					.setPayDate(new java.sql.Date(smp.parse(smp.format((paymentInvBean.getPayDate()))).getTime()));
			result = paymentInvoiceManualDao.findPayOrderFulln(paymentInvBean);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	public List<HistoryPaymentRS> findPaymentOrder(HistoryReportBean historyRpt) throws SQLException {
		List<HistoryPaymentRS> result = new ArrayList<HistoryPaymentRS>();
		result = paymentInvoiceManualDao.findPaymentOrder(historyRpt);

		return result;
	}

	@Override
	public PaymentInvoiceManualBean findInvoiceManuleByManualIdService(Long manualId) {
		return paymentInvoiceManualDao.findInvoiceManualByManualId(manualId);
	}

}
