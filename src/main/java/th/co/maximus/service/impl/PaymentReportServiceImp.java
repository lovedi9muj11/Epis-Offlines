package th.co.maximus.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.maximus.bean.DeductionManualBean;
import th.co.maximus.bean.ReportPaymentBean;
import th.co.maximus.bean.ReportPaymentCriteria;
import th.co.maximus.constants.Constants;
import th.co.maximus.dao.DeductionManualImpl;
import th.co.maximus.dao.PaymentManualDao;
import th.co.maximus.dao.TrsMethodManualDao;
import th.co.maximus.model.TrsChequerefEpisOffline;
import th.co.maximus.model.TrsCreditrefEpisOffline;
import th.co.maximus.model.TrsMethodEpisOffline;
import th.co.maximus.service.PaymentReportService;
import th.co.maximus.service.TrsChequeRefManualService;
import th.co.maximus.service.TrscreDitrefManualService;

@Service
public class PaymentReportServiceImp implements PaymentReportService {
	
	@Autowired
	private PaymentManualDao paymentManualDao;
	
	@Autowired
	private TrsMethodManualDao trsMethodManualDao;
	
	@Autowired
	private DeductionManualImpl deductionManualImpl;
	
	@Autowired
	private TrscreDitrefManualService trscreDitrefManualService;
	@Autowired
	private TrsChequeRefManualService trsChequeRefManualService;

	@Override
	public List<ReportPaymentBean> findPaymnetReportService(ReportPaymentCriteria criteria,String serviceType) throws Exception {
		
		Integer supCh = 0;
		
		if(StringUtils.isNotBlank(criteria.getUser())) {
			if(!"".equals(criteria.getUser())) {
				supCh = paymentManualDao.checkSup(criteria.getUser());
			}else {
				supCh = 2;
			}
		}
		
		if(supCh == 2) {
			criteria.setUser("");
		}
		
		String checkWT = "";
		List<ReportPaymentBean> result = new ArrayList<ReportPaymentBean>();
		
		result = paymentManualDao.getReportPayment(criteria,serviceType);
		
		List<ReportPaymentBean> data = new ArrayList<ReportPaymentBean>();

		for(ReportPaymentBean resultBean : result) {
			String paymentCodeRes = "";
			String deductionNo = "";
		
			boolean chkCC = true;
			boolean chkCH = false;
			boolean chkCR = false;
			List<String> results = new ArrayList<>();
			List<String> refno = new ArrayList<>();
			List<TrsMethodEpisOffline> methodResult = trsMethodManualDao.findByManualId(Long.valueOf(resultBean.getManualId()));
			List<DeductionManualBean> deductionList = deductionManualImpl.findDeductionManualFromManualId(Long.valueOf(resultBean.getManualId()));
				for (int i = 0; i < methodResult.size(); i++) {
					String payCode = "";
					TrsMethodEpisOffline stockObject = (TrsMethodEpisOffline) methodResult.get(i);

					if (stockObject.getCode().equals("CC")) {
						payCode = "เงินสด";
						results.add(payCode);
					
						if(i==0) {resultBean.setRefNoEx(""); chkCC=true;}
					} else if (stockObject.getCode().equals("CR")) {
						List<TrsCreditrefEpisOffline> res = trscreDitrefManualService.findByMethodId(stockObject.getId());
						String code = stockObject.getCreditNo();
//                        payCode = "บัตรเครดิต" + " " + res.get(0).getCardtype() + " " + "เลขที่ : ************" + code.substring(12, 16);
//                        results.add(payCode);
//						refno.add(code.substring(12, 16));
						if(chkCC) {resultBean.setRefNoEx(code.substring(12, 16)); chkCC=false;}
						
						if(CollectionUtils.isNotEmpty(res)) {
							for(int j=0; j<res.size(); j++) {
								payCode = "บัตรเครดิต";
							}
						}
						if(!chkCR){
							results.add(payCode);
						}

						refno.add(res.get(0).getCreditNo().substring(12, 16)); chkCR=true;
					} else if (stockObject.getCode().equals("CH")) {
						List<TrsChequerefEpisOffline> res = trsChequeRefManualService.findTrsCredit(stockObject.getId());
						
						if(chkCC) {resultBean.setRefNoEx(res.get(0).getChequeNo()); chkCC=false;}
						
						if(CollectionUtils.isNotEmpty(res)) {
							for(int j=0; j<res.size(); j++) {
								payCode = "เช็ค";
							}
						}
						if(!chkCH){
							results.add(payCode);
						}

						refno.add(res.get(0).getChequeNo()); chkCH=true;
					}
				}
				
				boolean chkWT = true;
				for (int i = 0; i < methodResult.size(); i++) {
					TrsMethodEpisOffline stockObject = (TrsMethodEpisOffline) methodResult.get(i);
					if (stockObject.getCode().equals("DEDUC") && chkWT) {
						checkWT = "WT";
						results.add(checkWT);
						chkWT = false;
					}

				}
				for (int f = 0; f < results.size(); f++) {
					if (f == 0) {
						paymentCodeRes += results.get(f);
					} else {
						paymentCodeRes += " + " + results.get(f);
					}

				}
				
				int i=0;
				for(DeductionManualBean deductionBean : deductionList) {
					if(i>0)deductionNo += ", ";
					
					deductionNo += deductionBean.getDeDuctionNo();
					i++;
				}
				
			if(paymentCodeRes.indexOf(Constants.PAYTYPE.WT) > 0) {
				refno.add(deductionNo);
			}
			String refnoDis = "";
			for (int a = 0; a < refno.size(); a++) {
				if (a == 0) {
					refnoDis += refno.get(a);
				} else {
					refnoDis += " , " + refno.get(a);
				}

			}
			resultBean.setRefNo(refnoDis);
			resultBean.setDeductionNo(deductionNo);
			resultBean.setPaymentMethod(paymentCodeRes);
			resultBean.setServiceCode(resultBean.getServiceCode());
			data.add(resultBean);
		}
		
		return data;
	}
	
	@Override
	public List<ReportPaymentBean> findPaymnetReportServiceOtherSearch(ReportPaymentCriteria criteria,String serviceType) throws Exception {
		
		Integer supCh = paymentManualDao.checkSup(criteria.getUser());
		if(supCh == 2) {
			criteria.setUser("");
		}
		
		String checkWT = "";
		List<ReportPaymentBean> result = new ArrayList<ReportPaymentBean>();
		
		if(Constants.Service.SERVICE_TYPE_OTHER.equals(serviceType)) {
			result = paymentManualDao.getReportPaymentOther(criteria,serviceType);
		}else {
			result = paymentManualDao.getReportPaymentB(criteria,serviceType);
		}
		
		List<ReportPaymentBean> data = new ArrayList<ReportPaymentBean>();
		
		for(ReportPaymentBean resultBean : result) {
			String paymentCodeRes = "";
			String deductionNo = "";
			boolean chkCC = true;
			boolean chkCR = false;
			boolean chkCH = false;

			List<String> results = new ArrayList<>();
			List<String> refno = new ArrayList<>();
			List<TrsMethodEpisOffline> methodResult = trsMethodManualDao.findByManualId(Long.valueOf(resultBean.getManualId()));
			List<DeductionManualBean> deductionList = deductionManualImpl.findDeductionManualFromManualId(Long.valueOf(resultBean.getManualId()));
			for (int i = 0; i < methodResult.size(); i++) {
				String payCode = "";
				TrsMethodEpisOffline stockObject = (TrsMethodEpisOffline) methodResult.get(i);
				
				if (stockObject.getCode().equals("CC")) {
					payCode = "เงินสด";
					results.add(payCode);
					if(i==0) {resultBean.setRefNo(""); chkCC=true;}
					if (stockObject.getCode().equals("DEDUC")) {}
				} else if (stockObject.getCode().equals("CR")) {
					List<TrsCreditrefEpisOffline> res = trscreDitrefManualService.findByMethodId(stockObject.getId());
//					String code = stockObject.getCreditNo();
//					payCode = "บัตรเครดิต" + " " + res.get(0).getCardtype() + " " + "เลขที่ : ************" + code.substring(12, 16);
//					results.add(payCode);
//					if(chkCC) {refno.add(code.substring(12, 16)); chkCC=false;}
					
					if(CollectionUtils.isNotEmpty(res)) {
						for(int j=0; j<res.size(); j++) {
							payCode = "บัตรเครดิต";
						}
					}
					if(!chkCR){
						results.add(payCode);
					}

					refno.add(res.get(0).getCreditNo().substring(12, 16)); chkCR=true;
				} else if (stockObject.getCode().equals("CH")) {
					List<TrsChequerefEpisOffline> res = trsChequeRefManualService.findTrsCredit(stockObject.getId());
					
//					if(CollectionUtils.isNotEmpty(res)) {
//						for(int j=0; j<res.size(); j++) {
//							payCode = "เช็ค " + res.get(j).getPublisher() + "เลขที่ : ************" + res.get(j).getChequeNo().substring(3);
//						}
//					}
//					
//					results.add(payCode);
//					if(chkCC) {refno.add( res.get(0).getChequeNo()); chkCC=false;}
					
					if(CollectionUtils.isNotEmpty(res)) {
						for(int j=0; j<res.size(); j++) {
							payCode = "เช็ค";
						}
					}
					if(!chkCH){
						results.add(payCode);
					}

					refno.add(res.get(0).getChequeNo()); chkCH=true;
				}
			}
			
			boolean chkWT = true;
			for (int i = 0; i < methodResult.size(); i++) {
				TrsMethodEpisOffline stockObject = (TrsMethodEpisOffline) methodResult.get(i);
				if (stockObject.getCode().equals("DEDUC") && chkWT) {
					checkWT = "WT";
					results.add(checkWT);
					chkWT = false;
				}
				
			}
			for (int f = 0; f < results.size(); f++) {
				if (f == 0) {
					paymentCodeRes += results.get(f);
				} else {
					paymentCodeRes += " + " + results.get(f);
				}
				
			}
			
			int i=0;
			for(DeductionManualBean deductionBean : deductionList) {
				if(i>0)deductionNo += ", ";
				
				deductionNo += deductionBean.getDeDuctionNo();
				i++;
			}
			
			if(paymentCodeRes.indexOf(Constants.PAYTYPE.WT) > 0) {
				resultBean.setRefNo(deductionNo);
			}
			String refnoDis = "";
			for (int a = 0; a < refno.size(); a++) {
				if (a == 0) {
					refnoDis += refno.get(a);
				} else {
					refnoDis += " , " + refno.get(a);
				}

			}
			resultBean.setRefNo(refnoDis);
			resultBean.setDeductionNo(deductionNo);
			resultBean.setPaymentMethod(paymentCodeRes);
			resultBean.setServiceCode(resultBean.getServiceCode());
			data.add(resultBean);
		}
		
		return data;
	}

}
