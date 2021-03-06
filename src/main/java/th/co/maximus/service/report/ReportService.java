package th.co.maximus.service.report;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRException;
import th.co.maximus.bean.HistoryPaymentRS;
import th.co.maximus.bean.HistoryReportBean;
import th.co.maximus.bean.ReportBean;
import th.co.maximus.bean.ReportPaymentBean;
import th.co.maximus.bean.ReportPaymentCriteria;
import th.co.maximus.bean.ReportTaxRSBean;
import th.co.maximus.constants.Constants;

@Service("reportService")
public class ReportService {
	protected Logger log = Logger.getLogger(getClass());
	@Autowired
	private @Qualifier("reportExcelService") ReportExcelService reportExcelService;

	Locale localeTH = new Locale("th", "TH");
	Locale localeEN = new Locale("en", "EN");

	SimpleDateFormat formateYearTH = new SimpleDateFormat("yyyy", localeTH);

	public Workbook controlAllReports(Workbook workbook, String rptCode, ReportBean bean) throws Exception {
		if (rptCode.equals(Constants.report.XXX)) {
			bean.setReportId(1L);
			bean.setName("Ae");
			bean.setPayDate(new Date());
			bean.setPayDateTo(new Date());
			List<ReportBean> entity = xxx(bean);
			workbook = reportExcelService.getReportRptxxx(workbook, entity, bean);
		} 
		return workbook;
	}
	
	public List<ReportBean> xxx(ReportBean bean) {
		List<ReportBean> result = new ArrayList<ReportBean>();
		result.add(bean);
		return result;
	}
	
	public Workbook controlAllReport(Workbook workbook, String rptCode, List<HistoryPaymentRS> entity, HistoryReportBean bean, List<ReportTaxRSBean> rsBeans) throws Exception {
		if (rptCode.equals(Constants.report.RPT_CODE_F)) {
			workbook = reportExcelService.getReportRptFull(workbook, entity, bean);
		}else if(rptCode.equals(Constants.report.RPT_CODE_NF)) {
			workbook = reportExcelService.getReportRptNotFull(workbook, entity, bean, rsBeans);
		}
		return workbook;
	}
	
	public Workbook paymentReportServiceExcel(Workbook workbook, String rptCode, ReportBean bean) throws Exception {
		if (rptCode.equals(Constants.report.XXX)) {
			bean.setReportId(1L);
			bean.setName("Ae");
			bean.setPayDate(new Date());
			bean.setPayDateTo(new Date());
			List<ReportBean> entity = xxx(bean);
			workbook = reportExcelService.getReportRptxxx(workbook, entity, bean);
		} 
		return workbook;
	}
	
	public Workbook reportPayment(Workbook workbook, ReportPaymentCriteria criteria, List<ReportPaymentBean>  result) throws Exception {
		if(Constants.Service.SERVICE_TYPE_OTHER.equals(criteria.getServiceType())) {
			List<ReportPaymentBean>  results = new ArrayList<ReportPaymentBean>();
			for(ReportPaymentBean data : result) {
				ReportPaymentBean dataSet = new ReportPaymentBean();
				dataSet = data;
				dataSet.setBeforVatStr(String.format("%,.2f", data.getBeforVatOther()));
				dataSet.setVatAmountStr(String.format("%,.2f", data.getVatAmountOther()));
				dataSet.setAmountStr(String.format("%,.2f", data.getAmountOther()));
				dataSet.setAmount(data.getAmountOther());
				dataSet.setVatAmount(data.getVatAmountOther());
				dataSet.setBeforVat(data.getBeforVatOther());
				
				results.add(dataSet);
			}
			result = results;
		}
		
		return reportExcelService.reportPaymentExcelService(workbook, criteria, result);
	} 
	
	public void ganeratePaymentPDF(String fileName , ReportPaymentCriteria criteria, List<ReportPaymentBean> date,HttpServletResponse response) throws JRException, ParseException, IOException, SQLException {
		reportExcelService.generationPaymentPDFService(fileName, criteria, date,response);
	}
}