package th.co.maximus.controller;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import th.co.maximus.bean.MapGLBean;
import th.co.maximus.bean.ReportPaymentBean;
import th.co.maximus.bean.ReportPaymentCriteria;
import th.co.maximus.constants.Constants;
import th.co.maximus.dao.MapGLDao;
import th.co.maximus.dao.PaymentInvoiceManualDao;
import th.co.maximus.model.PaymentMaualModel;
import th.co.maximus.model.TranferLogs;
import th.co.maximus.repository.TranferLogsRepository;
import th.co.maximus.service.PaymentReportService;

@Controller
public class ReportPaymentController {
	
	@Autowired
	private PaymentReportService paymentReportService;
	@Autowired
	TranferLogsRepository tranferLogsRepository;
	
	@Autowired
	private PaymentInvoiceManualDao paymentInvoiceManualDao;
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Autowired
	private MapGLDao mapGLDao;

	@RequestMapping(value = { "/reportPayment" }, method = RequestMethod.GET)
	public String reportPayment(Model model) {
		return "report-payment";
	}
	
	@RequestMapping(value = { "/reportTranfer" }, method = RequestMethod.GET)
	public String reportTranfer(Model model) {
		return "report-tranfer";
	}
	
	@RequestMapping(value = { "/reportPaymentTax" }, method = RequestMethod.GET)
	public String reportPaymentTax(Model model) {
		return "report-tax";
	}
	
	@RequestMapping(value = { "/reportPayment" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<ReportPaymentBean> reportPaymentCriteria(@RequestBody ReportPaymentCriteria creteria) throws Exception {
//		List<ReportPaymentBean> result = paymentReportService.findPaymnetReportService(creteria, Constants.DOCTYPE.RF);
		List<ReportPaymentBean> result = paymentReportService.findPaymnetReportServicePDF(creteria, Constants.Service.SERVICE_TYPE_IBACSS);
		return result;
	}
	
	
	@RequestMapping(value = { "/reportTranfer" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<TranferLogs> reportTranferCriteria(@RequestBody ReportPaymentCriteria creteria) throws Exception {
//		List<ReportPaymentBean> result = paymentReportService.findPaymnetReportService(creteria, Constants.DOCTYPE.RF);
//		List<ReportPaymentBean> result = paymentReportService.findPaymnetReportServicePDF(creteria, Constants.Service.SERVICE_TYPE_IBACSS);
//		  EntityManagerFactory emf = tranferLogsRepository.get
//		System.out.println(entityManager);
		StringBuilder  sql = new StringBuilder();
		sql.append(" SELECT * FROM TRANFER_LOGS");
		sql.append(" WHERE  ");
		sql.append(" STARTDATE").append(" >= "+"'" +  creteria.getDateFrom() + "'");
		sql.append(" AND ");
		sql.append(" STARTDATE").append(" <= "+"'" + creteria.getDateTo2() +" "+ creteria.getDateTo() + "'");
		
		javax.persistence.Query query = entityManager.createNativeQuery(sql.toString() , TranferLogs.class);
//		query.setParameter("startDate", creteria.getDateFrom());
		System.out.println(creteria.getDateFrom());
//		query.setParameter("endDate", creteria.getDateTo()+" "+creteria.getDateTo2());
		System.out.println(creteria.getDateTo2()+" "+creteria.getDateTo());
//		
		
		
//		return query.getResultList();
		System.out.println(query.getResultList().size());
		return query.getResultList(); 
	}
	@RequestMapping(value = { "/findGL_AccountMaster" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<MapGLBean> findDropdownAccountMaster() {
//		List<MasterDataBean> result = masterDataDao.findAllByGropType("GL_ACCOUNT");
		List<MapGLBean> result = mapGLDao.findAll();
		return result;
	}
	
	@RequestMapping(value = { "/findByRecriptList" }, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public List<PaymentMaualModel> findDropdownAccountMaster(@RequestBody String rec) {
//		List<MasterDataBean> result = masterDataDao.findAllByGropType("GL_ACCOUNT");
//		List<MapGLBean> result = mapGLDao.findAll();
		StringBuilder  sql1 = new StringBuilder();
		sql1.append(" SELECT * FROM TRANFER_LOGS");
		sql1.append(" WHERE  ID = '" + rec+"'" );
		javax.persistence.Query query1= entityManager.createNativeQuery(sql1.toString() , TranferLogs.class);
		TranferLogs  result1 = (TranferLogs) query1.getSingleResult();
		String[] reList = result1.getErrorRecript().split(",");
				
		System.out.println(reList);
		StringBuilder  sql2 = new StringBuilder();

		List<PaymentMaualModel> result = paymentInvoiceManualDao.findCriteriaFromReceiptNo(reList);

		return  result;
	}
}
