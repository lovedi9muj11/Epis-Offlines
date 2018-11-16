package th.co.maximus.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.maximus.bean.DropDownBean;
import th.co.maximus.bean.InvoiceBean;
import th.co.maximus.bean.PaymentInvoiceManualBean;
import th.co.maximus.bean.PaymentMMapPaymentInvBean;
import th.co.maximus.bean.PaymentManualBean;
import th.co.maximus.bean.TrsMethodManualBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.core.utils.ReciptNoGenCode;
import th.co.maximus.core.utils.Utils;
import th.co.maximus.dao.CancelDao;
import th.co.maximus.dao.MasterDatasDao;
import th.co.maximus.dao.PaymentInvoiceManualDao;
import th.co.maximus.dao.PaymentManualDao;
import th.co.maximus.dao.TrsChequeRefManualDao;
import th.co.maximus.dao.TrsMethodManualDao;
import th.co.maximus.dao.TrscreDitrefManualDao;
import th.co.maximus.model.TrsChequerefEpisOffline;
import th.co.maximus.model.TrsCreditrefEpisOffline;
import th.co.maximus.model.TrsMethodEpisOffline;
import th.co.maximus.payment.bean.PaymentResultReq;
import th.co.maximus.service.CancelPaymentService;

@Service
public class CancelPaymentServiceImp implements CancelPaymentService {
	
	SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	SimpleDateFormat dateFM = new SimpleDateFormat("dd/MM/yyyy");

	@Autowired
	private PaymentInvoiceManualDao paymentInvoiceManualDao;

	@Autowired
	private PaymentManualDao paymentManualDao;

	@Autowired
	private ReciptNoGenCode reciptNoGenCode;

	@Autowired
	private TrsMethodManualDao trsMethodManualDao;
	
	@Autowired
	private MasterDatasDao masterDatasDao;
	
	@Autowired
	private TrsChequeRefManualDao  trsChequeRefManualDao;
	
	@Autowired
	private TrscreDitrefManualDao  trscreDitrefManualDao;
	
	@Autowired
	private CancelDao cancelDao;

	@Override
	public List<PaymentMMapPaymentInvBean> findAllCancelPayment() throws Exception {
		List<PaymentMMapPaymentInvBean> result = paymentInvoiceManualDao.findPaymentMuMapPaymentInV();
		for (PaymentMMapPaymentInvBean resultBean : result) {
			List<TrsMethodEpisOffline> methodResult = trsMethodManualDao
					.findByManualId(Long.valueOf(resultBean.getManualId()));
			StringBuffer paymentMethod = new StringBuffer();
			for (TrsMethodEpisOffline method : methodResult) {

				if (paymentMethod.toString().equals("")) {
					paymentMethod.append(method.getName());
				} else {
					paymentMethod.append("+" + method.getName());
				}

			}
			resultBean.setPaidDateStr(dt.format(resultBean.getCreateDate()));
			if ("IBACSS".equals(resultBean.getServiceType())) {
				resultBean.setPeriod(Utils.periodFormat(resultBean.getPeriod()));
			} else if ("OTHER".equals(resultBean.getServiceType())) {
				resultBean.setPeriod("-");
			}

			resultBean.setCreateDateStr(dt.format(resultBean.getCreateDate()));
			resultBean.setPaymentMethod(paymentMethod.toString());
		}

//		Collections.sort(result, new Comparator<PaymentMMapPaymentInvBean>() {
//			@Override
//			public int compare(PaymentMMapPaymentInvBean o1, PaymentMMapPaymentInvBean o2) {
//				return o2.getCreateDate().compareTo(o1.getCreateDate());
//			}
//		});
		return result;
	}

	@Override
	public List<PaymentMMapPaymentInvBean> findAllCancelPaymentFromId(Long manualId) throws Exception {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<>();
		for (PaymentMMapPaymentInvBean bean : paymentInvoiceManualDao.findPaymentMuMapPaymentInVFromId(manualId)) {
			if ("N".equals(bean.getClearing()) && "A".equals(bean.getRecordStatus())) {
				List<TrsMethodEpisOffline> methodResult = trsMethodManualDao
						.findByManualId(Long.valueOf(bean.getManualId()));
				StringBuffer paymentMethod = new StringBuffer();
				for (TrsMethodEpisOffline method : methodResult) {

					if (paymentMethod.toString().equals("")) {
						paymentMethod.append(method.getName());
					} else {
						paymentMethod.append("+" + method.getName());
					}

				}
				bean.setCreateDateStr(dateFM.format(bean.getCreateDate()));
				bean.setPaymentMethod(paymentMethod.toString());
				result.add(bean);
			}
		}
		return result;
	}

	@Override
	public List<PaymentMMapPaymentInvBean> serviceCriteriaFromInvoiceOrReceiptNo(String receiptNo, String code, boolean chkCancel) throws Exception {

		List<PaymentMMapPaymentInvBean> result = new ArrayList<PaymentMMapPaymentInvBean>();
		for (PaymentMMapPaymentInvBean resultBean : paymentInvoiceManualDao.findCriteriaFromInvoiceOrReceiptNo(receiptNo, code, chkCancel)) {
			List<TrsMethodEpisOffline> methodResult = trsMethodManualDao.findByManualId(Long.valueOf(resultBean.getManualId()));
			StringBuffer paymentMethod = new StringBuffer();
//			String name = "";
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
				
				
//				if (paymentMethod.toString().equals("")) {
//					paymentMethod.append(method.getName());
//				} else {
//					name = Constants.Status.METHOD_WT_STR.equals(method.getName())?Constants.Status.METHOD_WT:method.getName();
//					paymentMethod.append("+" + name);
//				}

			}
			setCustomerGroupName(resultBean);
			resultBean.setCreateDateStr(dateFM.format(resultBean.getCreateDate()));
			resultBean.setPaymentMethod(paymentMethod.toString().substring(1));
			resultBean.setBrancharea(masterDatasDao.findByKey(resultBean.getBrancharea()).getValue());
			result.add(resultBean);
		}

//		Collections.sort(result, new Comparator<PaymentMMapPaymentInvBean>() {
//			@Override
//			public int compare(PaymentMMapPaymentInvBean o1, PaymentMMapPaymentInvBean o2) {
//				return o2.getCreateDate().compareTo(o1.getCreateDate());
//			}
//		});
		return result;
	}

	@Override
	@Transactional
	public PaymentResultReq insertAndUpdateCancelPayment(PaymentMMapPaymentInvBean paymentMMapPaymentInvBean) {
		PaymentResultReq paymentResultReq = new PaymentResultReq();
		PaymentInvoiceManualBean paymentInvoiceManualBean = new PaymentInvoiceManualBean();
		InvoiceBean invoiceBean = new InvoiceBean(); 
		
//		UserProfile profile = (UserProfile)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String receiptId = "";
		String cancelStatusPayment = masterDatasDao.findReasonByCode(paymentMMapPaymentInvBean.getStatusCancelPayment(), paymentMMapPaymentInvBean.getChkPaymentType());
		int manualID = 0;
		try {
			Date date = new Date();
			// update status
			paymentInvoiceManualDao.updateRecodeStatusFromReceiptNo(Constants.Status.STATUS_CANCEL, paymentMMapPaymentInvBean.getManualId(), cancelStatusPayment, paymentMMapPaymentInvBean.getUserApproved());
			paymentInvoiceManualDao.updateStatusPaymentInvoice(paymentMMapPaymentInvBean.getManualId());
			if ("001".equals(paymentMMapPaymentInvBean.getStatusCancelPayment())) {
				// Insert PaymentManual
				List<PaymentManualBean> paymentManual = paymentManualDao.findPaymentManualFromNanualId(paymentMMapPaymentInvBean.getManualId());
				if (!paymentManual.isEmpty()) {
					PaymentManualBean paymentManualBean = new PaymentManualBean();
					for (PaymentManualBean resultPaymentManual : paymentManual) {
						paymentManualBean = new PaymentManualBean();
						receiptId = resultPaymentManual.getReceiptNoManual();
						paymentManualBean.setInvoiceNo(resultPaymentManual.getInvoiceNo());
						paymentManualBean.setReceiptNoManual(reciptNoGenCode.genCodeRecipt(resultPaymentManual.getDocType()));
//						result = paymentManualBean.getReceiptNoManual();
						paymentResultReq.setDocumentNo(paymentManualBean.getReceiptNoManual());
						paymentManualBean.setPaidDate(resultPaymentManual.getPaidDate());
						paymentManualBean.setBrancharea(resultPaymentManual.getBrancharea());
						paymentManualBean.setBranchCode(resultPaymentManual.getBranchCode());
						paymentManualBean.setPaidAmount(resultPaymentManual.getPaidAmount());
						paymentManualBean.setSource(resultPaymentManual.getSource());
						paymentManualBean.setClearing(resultPaymentManual.getClearing());
						paymentManualBean.setRemark(resultPaymentManual.getRemark());
						paymentManualBean.setCreateBy(resultPaymentManual.getCreateBy());
						// paymentManualBean.setCreateDate(resultPaymentManual.getCreateDate());
						paymentManualBean.setUpdateBy(resultPaymentManual.getUpdateBy());
						// paymentManualBean.setUpdateDate(resultPaymentManual.getUpdateDate());
						paymentManualBean.setRecordStatus("A");
						paymentManualBean.setAccountNo(resultPaymentManual.getAccountNo());
						paymentManualBean.setPaytype(resultPaymentManual.getPaytype());
						paymentManualBean.setDocType(resultPaymentManual.getDocType());
						paymentManualBean.setChange(resultPaymentManual.getChange());
						paymentManualBean.setAmount(resultPaymentManual.getAmount());
//						paymentManualBean.setVatRate(resultPaymentManual.getVatRate());
						paymentManualBean.setVatAmount(resultPaymentManual.getVatAmount());
						manualID = paymentManualDao.insertPayment(paymentManualBean);
					}
				}
				// Insert payment_invoice_manual
				List<PaymentInvoiceManualBean> resultPaymentInvoice = paymentInvoiceManualDao.findPaymentInvoiceFromManualId(paymentMMapPaymentInvBean.getManualId());
				if (!resultPaymentInvoice.isEmpty()) {
					paymentInvoiceManualBean.setManualId(Long.valueOf(manualID));
					paymentInvoiceManualBean.setSource(resultPaymentInvoice.get(0).getSource());
					paymentInvoiceManualBean.setInvoiceNo(resultPaymentInvoice.get(0).getInvoiceNo());
					paymentInvoiceManualBean.setBeforVat(resultPaymentInvoice.get(0).getBeforVat());
					paymentInvoiceManualBean.setVatAmount(resultPaymentInvoice.get(0).getVatAmount());
					paymentInvoiceManualBean.setAmount(resultPaymentInvoice.get(0).getAmount());
					paymentInvoiceManualBean.setVatRate(resultPaymentInvoice.get(0).getVatRate());
					paymentInvoiceManualBean.setCustomerName(paymentMMapPaymentInvBean.getCustomerName());
					paymentInvoiceManualBean.setCustomerAddress(paymentMMapPaymentInvBean.getAddressNewCancelPayment());
					paymentInvoiceManualBean.setCustomerSegment(resultPaymentInvoice.get(0).getCustomerSegment());
					paymentInvoiceManualBean.setCustomerBranch(resultPaymentInvoice.get(0).getCustomerBranch());
					paymentInvoiceManualBean.setTaxNo(resultPaymentInvoice.get(0).getTaxNo());
					paymentInvoiceManualBean.setAccountSubNo(resultPaymentInvoice.get(0).getAccountSubNo());
					paymentInvoiceManualBean.setPeriod(resultPaymentInvoice.get(0).getPeriod());
					paymentInvoiceManualBean.setServiceType(resultPaymentInvoice.get(0).getServiceType());
					paymentInvoiceManualBean.setClearing(resultPaymentInvoice.get(0).getClearing());
					paymentInvoiceManualBean.setPrintReceipt(resultPaymentInvoice.get(0).getPrintReceipt());
					paymentInvoiceManualBean.setRemark("ยกเลิกใบเก่าออกใบใหม่ อ้างอิงเลขที่ :" + receiptId);
					paymentInvoiceManualBean.setCreateBy(resultPaymentInvoice.get(0).getCreateBy());
					paymentInvoiceManualBean.setUpdateBy(resultPaymentInvoice.get(0).getUpdateBy());
					paymentInvoiceManualBean.setRecordStatus("A");
					paymentInvoiceManualBean.setQuantity(resultPaymentInvoice.get(0).getQuantity());
					paymentInvoiceManualBean.setIncometype(resultPaymentInvoice.get(0).getIncometype());
					paymentInvoiceManualBean.setDiscountbeforvat(resultPaymentInvoice.get(0).getDiscountbeforvat());
					paymentInvoiceManualBean.setDiscountspecial(resultPaymentInvoice.get(0).getDiscountspecial());
					paymentInvoiceManualBean.setAmounttype(resultPaymentInvoice.get(0).getAmounttype());
					paymentInvoiceManualBean.setDepartment(resultPaymentInvoice.get(0).getDepartment());
					paymentInvoiceManualBean.setServiceName(resultPaymentInvoice.get(0).getServiceName());
					paymentInvoiceManualBean.setServiceCode(resultPaymentInvoice.get(0).getServiceCode());
					paymentInvoiceManualDao.insert(paymentInvoiceManualBean);
					
					if(true) {
						invoiceBean = paymentInvoiceManualDao.findInvoiceByManualId(paymentMMapPaymentInvBean.getManualId());
						paymentResultReq.setChkPaymentType(Constants.Service.SERVICE_TYPE_OTHER);
						if(null != invoiceBean.getManualId()) {
//							paymentInvoiceManualDao.insertInvoice(invoiceBean);
							paymentResultReq.setChkPaymentType(Constants.Service.SERVICE_TYPE_IBACSS);
						}
					}
				}
				for (TrsMethodEpisOffline method : trsMethodManualDao.findByManualId(paymentMMapPaymentInvBean.getManualId())) {
					TrsMethodManualBean bean = new TrsMethodManualBean();
					bean.setCode(method.getCode());
					bean.setChequeNo(method.getChequeNo());
					bean.setAccountNo(method.getAccountNo());
					bean.setCreditId(method.getCreditNo());
					bean.setName(method.getName());
					bean.setAmount(method.getAmount().doubleValue());

					bean.setUpdateDttm(new Timestamp(new Date().getTime()));
					bean.setVersionStamp(1L);
					bean.setRemark(null);
					bean.setCreateBy(method.getCreateBy());
					bean.setCreateDate(new Timestamp(new Date().getTime()));
					bean.setUpdateBy(method.getCreateBy());
					bean.setUpdateDate(new Timestamp(new Date().getTime()));
					bean.setRecordStatus("A");
					bean.setManualId(Long.valueOf(manualID));
					trsMethodManualDao.insertTrsMethod(bean);
				}
				// insert payment_Invoice
				InvoiceBean resultInvoiceBean = paymentInvoiceManualDao.findInvoiceByManualId(paymentMMapPaymentInvBean.getManualId());
				if(null != resultInvoiceBean) {
					resultInvoiceBean.setManualId(Long.valueOf(manualID));
//					resultInvoiceBean.setCreateDate(new Timestamp(date.getTime()));
					resultInvoiceBean.setUpdateDate(new Timestamp(date.getTime()));
					paymentInvoiceManualDao.insertInvoice(resultInvoiceBean);
				}

			}
		} catch (Exception e) {
//			resultReturn = false;
			e.printStackTrace();

		}
		return paymentResultReq;
	}

	@Override
	public List<PaymentMMapPaymentInvBean> findAllCancelPayments(String clearing) throws Exception {
		List<PaymentMMapPaymentInvBean> result = paymentInvoiceManualDao.findPaymentMuMapPaymentInVs(clearing);
		for (PaymentMMapPaymentInvBean resultBean : result) {
			List<TrsMethodEpisOffline> methodResult = trsMethodManualDao
					.findByManualId(Long.valueOf(resultBean.getManualId()));
			StringBuffer paymentMethod = new StringBuffer();
			for (TrsMethodEpisOffline method : methodResult) {
				if (paymentMethod.toString().equals("")) {
					paymentMethod.append(method.getName());
				} else {
					paymentMethod.append("+" + method.getName());
				}

			}
			resultBean.setPaidDateStr(dt.format(resultBean.getCreateDate()));
			if (null != resultBean.getPeriod()) {
				resultBean.setPeriod(Utils.periodFormat((resultBean.getPeriod())));
			}
			resultBean.setCreateDateStr(dt.format(resultBean.getCreateDate()));
			resultBean.setPaymentMethod(paymentMethod.toString());
			resultBean.setBrancharea(masterDatasDao.findByKey(resultBean.getBrancharea()).getValue());
		}
		
		

//		Collections.sort(result, new Comparator<PaymentMMapPaymentInvBean>() {
//			@Override
//			public int compare(PaymentMMapPaymentInvBean o1, PaymentMMapPaymentInvBean o2) {
//				return o2.getCreateDate().compareTo(o1.getCreateDate());
//			}
//		});
		return result;
	}
	
	public void setCustomerGroupName(PaymentMMapPaymentInvBean resultBean) {
		
		if(Constants.CUSTOMER_GROUP.CUSTOMER_1.equals(resultBean.getCustomerGroup())) {
			resultBean.setCustomerGroup(Constants.CUSTOMER_GROUP.CUSTOMER_NAME_1);
		}else if(Constants.CUSTOMER_GROUP.CUSTOMER_2.equals(resultBean.getCustomerGroup())) {
			resultBean.setCustomerGroup(Constants.CUSTOMER_GROUP.CUSTOMER_NAME_2);
		}else if(Constants.CUSTOMER_GROUP.CUSTOMER_3.equals(resultBean.getCustomerGroup())) {
			resultBean.setCustomerGroup(Constants.CUSTOMER_GROUP.CUSTOMER_NAME_3);
		}else if(Constants.CUSTOMER_GROUP.CUSTOMER_4.equals(resultBean.getCustomerGroup())) {
			resultBean.setCustomerGroup(Constants.CUSTOMER_GROUP.CUSTOMER_NAME_4);
		}else if(Constants.CUSTOMER_GROUP.CUSTOMER_5.equals(resultBean.getCustomerGroup())) {
			resultBean.setCustomerGroup(Constants.CUSTOMER_GROUP.CUSTOMER_NAME_5);
		}else if(Constants.CUSTOMER_GROUP.CUSTOMER_6.equals(resultBean.getCustomerGroup())) {
			resultBean.setCustomerGroup(Constants.CUSTOMER_GROUP.CUSTOMER_NAME_6);
		}else if(Constants.CUSTOMER_GROUP.CUSTOMER_7.equals(resultBean.getCustomerGroup())) {
			resultBean.setCustomerGroup(Constants.CUSTOMER_GROUP.CUSTOMER_NAME_7);
		}else if(Constants.CUSTOMER_GROUP.CUSTOMER_8.equals(resultBean.getCustomerGroup())) {
			resultBean.setCustomerGroup(Constants.CUSTOMER_GROUP.CUSTOMER_NAME_8);
		}else if(Constants.CUSTOMER_GROUP.CUSTOMER_9.equals(resultBean.getCustomerGroup())) {
			resultBean.setCustomerGroup(Constants.CUSTOMER_GROUP.CUSTOMER_NAME_9);
		}
		
	}

	@Override
	public List<DropDownBean> reasonCancelIbacss() {
		return cancelDao.reasonCancelIbacss();
	}

	@Override
	public List<DropDownBean> reasonCancelOther() {
		return cancelDao.reasonCancelOther();
	}

}
