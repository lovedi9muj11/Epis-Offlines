package th.co.maximus.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.maximus.bean.DeductionManualBean;
import th.co.maximus.bean.TrsChequeRefManualBean;
import th.co.maximus.bean.TrsMethodManualBean;
import th.co.maximus.bean.TrscreDitrefManualBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.dao.DeductionManualDao;
import th.co.maximus.dao.TrsChequeRefManualDao;
import th.co.maximus.dao.TrsMethodManualDao;
import th.co.maximus.dao.TrscreDitrefManualDao;
import th.co.maximus.payment.bean.PaymentOtherFirstBean;
import th.co.maximus.payment.bean.PaymentTaxBean;
import th.co.maximus.payment.bean.PaymentTranPriceBean;
import th.co.maximus.service.TrsmethodOtherManualService;

@Service
public class TrsmethodOtherManualServiceImpl implements TrsmethodOtherManualService{
	@Autowired TrsMethodManualDao trsMethodManualDao;
	@Autowired TrscreDitrefManualDao trscreDitrefManualDao;
	@Autowired TrsChequeRefManualDao trsChequeRefManualDao;
	@Autowired DeductionManualDao deductionManualDao;
	@Override
	public int insertTrsmethodManual(PaymentOtherFirstBean paymentBean,int userId) {
	Date date = new Date();
	Double totalAmount = paymentBean.getBalanceSummary();
		int idTrsMethod = 0;
		if(paymentBean.getPaymentTranPrice().size() >=0){
			for(int i=0; i < paymentBean.getPaymentTranPrice().size();i++){
				
				PaymentTranPriceBean paymentTranPriceBean = new PaymentTranPriceBean();
				TrsMethodManualBean trsMethodManualBean = new TrsMethodManualBean();
				TrscreDitrefManualBean trscreDitrefManualBean = new TrscreDitrefManualBean();
				TrsChequeRefManualBean trsChequeRefManualBean = new TrsChequeRefManualBean();

				paymentTranPriceBean = paymentBean.getPaymentTranPrice().get(i);
				trsMethodManualBean.setCode(paymentTranPriceBean.getTypePayment());
				trsMethodManualBean.setChequeNo(paymentBean.getPaymentTranPrice().get(i).getCheckNo());
				trsMethodManualBean.setAccountNo(paymentBean.getCustNo());
				trsMethodManualBean.setCreditId((paymentBean.getPaymentTranPrice().get(i).getCreditNo()));
				if(paymentTranPriceBean.getTypePayment().equals("CR")){
					totalAmount = totalAmount-paymentTranPriceBean.getCreditPrice();
					trsMethodManualBean.setName("บัตรเครดิต");
					trsMethodManualBean.setAmount(paymentTranPriceBean.getCreditPrice());
				}else if(paymentTranPriceBean.getTypePayment().equals("CH")){
					totalAmount = totalAmount-paymentTranPriceBean.getMoneyCheck();
					trsMethodManualBean.setName("เช็ค");
					trsMethodManualBean.setAmount(paymentTranPriceBean.getMoneyCheck());
				}else{
					totalAmount = totalAmount-paymentTranPriceBean.getMoneyTran();
					if(totalAmount < 0) {
						trsMethodManualBean.setAmount(paymentTranPriceBean.getMoneyTran());
						//trsMethodManualBean.setAmount(paymentTranPriceBean.getMoneyTran()+totalAmount+ paymentBean.getSummaryTax());
					}else {
						
						trsMethodManualBean.setAmount(paymentTranPriceBean.getMoneyTran());
						//trsMethodManualBean.setAmount(paymentTranPriceBean.getMoneyTran()+totalAmount+ paymentBean.getSummaryTax());
					}
					trsMethodManualBean.setName("เงินสด");
				}
				trsMethodManualBean.setUpdateDttm(new Timestamp(date.getTime()));
				trsMethodManualBean.setVersionStamp(1L);
				trsMethodManualBean.setRemark(paymentBean.getRemark());
				trsMethodManualBean.setCreateBy(paymentBean.getUserName());	
				trsMethodManualBean.setCreateDate(new Timestamp(date.getTime()));
				trsMethodManualBean.setUpdateBy(paymentBean.getUserName());
				trsMethodManualBean.setUpdateDate(new Timestamp(date.getTime()));
				trsMethodManualBean.setRecordStatus("A");
				trsMethodManualBean.setManualId(Long.valueOf(userId));
				try {
					 idTrsMethod = trsMethodManualDao.insertTrsMethod(trsMethodManualBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if(idTrsMethod >0){
						if(paymentTranPriceBean.getTypePayment().equals("CR")){
							//insert Credit
							trscreDitrefManualBean.setaMount(paymentTranPriceBean.getCreditPrice());
							trscreDitrefManualBean.setCreditNo(paymentTranPriceBean.getCreditNo());
							trscreDitrefManualBean.setPublisherdec(paymentTranPriceBean.getBankName());
							trscreDitrefManualBean.setCardType(paymentTranPriceBean.getCreditType());
							trscreDitrefManualBean.setUpdateDttm(new Timestamp(date.getTime()));
							trscreDitrefManualBean.setVersionStamp(1L);
							trscreDitrefManualBean.setMethodManualId(String.valueOf(idTrsMethod));
							trscreDitrefManualBean.setCreateBy(paymentBean.getUserName());	
							trscreDitrefManualBean.setUpdateBy(paymentBean.getUserName());
							trscreDitrefManualBean.setEdcCode(paymentTranPriceBean.getEdcCode());
							trscreDitrefManualDao.insertTrscreDitrefManua(trscreDitrefManualBean);
							
						}else if(paymentTranPriceBean.getTypePayment().equals("CH")){
							SimpleDateFormat df = new SimpleDateFormat(Constants.DateTime.DATE_FORMAT, Constants.localeEN);
							Date date1 = new Date();
							try {
								date1 = df.parse(paymentTranPriceBean.getDateCheck());
							} catch (ParseException e) {
								e.printStackTrace();
							}
							trsChequeRefManualBean.setChequeNo(paymentTranPriceBean.getCheckNo());
							trsChequeRefManualBean.setPublisherId(paymentTranPriceBean.getBankNo());
							trsChequeRefManualBean.setPublisher(paymentTranPriceBean.getBankName());
							trsChequeRefManualBean.setBranch(paymentTranPriceBean.getBranchCheck());
							trsChequeRefManualBean.setaMount(paymentTranPriceBean.getMoneyCheck());
							trsChequeRefManualBean.setUpdateDttm(new Timestamp(date.getTime()));
							trsChequeRefManualBean.setVersionStamp(1L);
//							trsChequeRefManualBean.setChequeDate(new Timestamp(paymentTranPriceBean.getDateCheck().getTime()));
							trsChequeRefManualBean.setCheDate(new Timestamp(date1.getTime()));
							trsChequeRefManualBean.setMethodManualId(Long.valueOf(idTrsMethod));
							trsChequeRefManualBean.setCreateBy(paymentBean.getUserName());	
							trsChequeRefManualBean.setUpdateBy(paymentBean.getUserName());
							trsChequeRefManualDao.insert(trsChequeRefManualBean);
							
					
					}
				}

				
				
				
			}
			
		} // END PAY
		
		if(paymentBean.getPaymentTax().size() >0){
			for(int i=0; i < paymentBean.getPaymentTax().size();i++){
				TrsMethodManualBean trsMethodManualBean = new TrsMethodManualBean();
				DeductionManualBean deductionManualBean= new DeductionManualBean();
				PaymentTaxBean paymentTaxBean = new PaymentTaxBean();
				paymentTaxBean = paymentBean.getPaymentTax().get(i);

				trsMethodManualBean.setCode("DEDUC");
				trsMethodManualBean.setName("ภาษีหัก ณ ที่จ่าย");
				trsMethodManualBean.setChequeNo("");
				trsMethodManualBean.setAccountNo(paymentBean.getCustNo());
				trsMethodManualBean.setCreditId("");
				trsMethodManualBean.setAmount(paymentTaxBean.getMoneyDed());
				trsMethodManualBean.setUpdateDttm(new Timestamp(date.getTime()));
				trsMethodManualBean.setVersionStamp(1L);
				trsMethodManualBean.setRemark(paymentBean.getRemark());
				trsMethodManualBean.setCreateBy(paymentBean.getUserName());	
				trsMethodManualBean.setCreateDate(new Timestamp(date.getTime()));
				trsMethodManualBean.setUpdateBy(paymentBean.getUserName());
				trsMethodManualBean.setUpdateDate(new Timestamp(date.getTime()));
				trsMethodManualBean.setRecordStatus("A");
				trsMethodManualBean.setManualId(Long.valueOf(userId));
				try {
					 idTrsMethod = trsMethodManualDao.insertTrsMethod(trsMethodManualBean);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				
				if(idTrsMethod >0){
					deductionManualBean.setDeDuctionNo(paymentTaxBean.getDocDed());
					deductionManualBean.setDeDuctionType(paymentTaxBean.getRadioDed());
					deductionManualBean.setaMount(paymentTaxBean.getMoneyDed());
					deductionManualBean.setPaymentDate(new Timestamp(date.getTime()));
					deductionManualBean.setUpdateDttm(new Timestamp(date.getTime()));
					deductionManualBean.setVersionStamp(1L);
					deductionManualBean.setRemark(paymentBean.getRemark());
					deductionManualBean.setCreateBy(paymentBean.getUserName());
					deductionManualBean.setCreateDate(new Timestamp(date.getTime()));
					deductionManualBean.setUpdateBy(paymentBean.getUserName());
					deductionManualBean.setUpdateDate(new Timestamp(date.getTime()));
					deductionManualBean.setRecordStatus("A");
					deductionManualBean.setManualId(Long.valueOf(userId));
					deductionManualDao.insert(deductionManualBean);
				
				}
				

			}
		}
		return userId;
		
		
	}
	
	@Override
	public List<TrsMethodManualBean> TrsmethodManualAll() {
//		return jdbcTemplate.query("select * from 	", new TrsMethodManualJoin());
		return null;
	}
}
