package th.co.maximus.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import th.co.maximus.auth.model.UserProfile;
import th.co.maximus.bean.HistoryReportBean;
import th.co.maximus.bean.InvEpisOfflineOtherBean;
import th.co.maximus.bean.InvEpisOfflineReportBean;
import th.co.maximus.bean.InvPaymentOrderTaxBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.dao.PaymentManualDao;
import th.co.maximus.dao.ReportDao;
import th.co.maximus.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService{

	@Autowired ReportDao reportDao;
	
	@Autowired
	private PaymentManualDao paymentManualDao;
	
	private ServletContext context;
	
	@Autowired
	public void setServletContext(ServletContext servletContext) {
		this.context = servletContext;
	}
	
	@Override
	public List<InvEpisOfflineReportBean> inqueryEpisOfflineJSONHandler(String documentNo) throws SQLException {
		
		return reportDao.inqueryEpisOfflineJSONHandler(documentNo);

	}

	@Override
	public List<InvEpisOfflineOtherBean> inqueryEpisOfflineOtherJSONHandler(String documentNo ,String other) throws SQLException {
		// TODO Auto-generated method stub
		return reportDao.inqueryEpisOfflineOtherJSONHandler(documentNo , other);
	}

	@Override
	public List<InvPaymentOrderTaxBean> inqueryInvPaymentOrderTaxBeanJSONHandler(HistoryReportBean creteria)
			throws SQLException {
		
		UserProfile profile = (UserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Integer supCh = 0;
		
		if(StringUtils.isNotBlank(profile.getUsername())) {
			if(!"".equals(profile.getUsername())) {
				supCh = paymentManualDao.checkSup(profile.getUsername());
			}else {
				supCh = 2;
			}
		}
		
		if(supCh == 2) {
			creteria.setUnserLogin("");
		}else {
			creteria.setUnserLogin(profile.getUsername());
		}
		
		return reportDao.inqueryInvPaymentOrderTaxBeanJSONHandler(creteria);
	}

	@Override
	public List<InvPaymentOrderTaxBean> vatSummarry(HistoryReportBean creteria ,boolean groupBy) throws SQLException {
		// TODO Auto-generated method stub
		return reportDao.summarryVay(creteria,groupBy);
	}

	@Override
	public List<InvEpisOfflineOtherBean> inqueryEpisOfflineOtherJSONHandler(String documentNo) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String isPage(HttpServletRequest request, HttpServletResponse response, List<InvPaymentOrderTaxBean> collections, String JASPER_JRXML_FILENAME, HistoryReportBean creteria) throws Exception {
		List<InvPaymentOrderTaxBean> printCollections = new ArrayList<InvPaymentOrderTaxBean>();
		InvPaymentOrderTaxBean invObject = (InvPaymentOrderTaxBean) collections.get(0);
		InvPaymentOrderTaxBean exportPDFReport = new InvPaymentOrderTaxBean();
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
		
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:ss");
		SimpleDateFormat dtt = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		
		String dateDocument = dt.format(date);
		String oldHeadName = "";
		if (Constants.DOCTYPE.RF.equals(creteria.getTypePrint())) {
			exportPDFReport.setHeadName("รายงานภาษีใบเสร็จรับเงิน/ใบกำกับภาษีเต็มรูป");
			exportPDFReport.setReportStatus("1");
		} else {
			exportPDFReport.setHeadName("รายงานภาษีใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ");
		}
		oldHeadName = exportPDFReport.getHeadName();
		String fomeDate = "";
		String endDate = "";
		if (StringUtils.isNotBlank(creteria.getDateFrom())) {
			String dateForm = creteria.getDateFrom();
			String[] res = dateForm.split("-");
			fomeDate = res[2] + "/" + res[1] + "/" + res[0];
		}
		if (StringUtils.isNotBlank(creteria.getDateTo())) {
			String dateEnd = creteria.getDateTo();
			String[] res = dateEnd.split("-");
			endDate = res[2] + "/" + res[1] + "/" + res[0];
		}

		exportPDFReport.setDateForm(fomeDate + " " + creteria.getDateFromHour() + ":" + creteria.getDateFromMinute());
		exportPDFReport.setDateTo(endDate + " " + creteria.getDateToHour() + ":" + creteria.getDateToMinute());
		exportPDFReport.setPrintDate(dateDocument);
		exportPDFReport.setEmpSummaryName(invObject.getEmpName());

		BigDecimal summaryBeforeVt = new BigDecimal(0);
		BigDecimal vatSummary = new BigDecimal(0);
		BigDecimal summarySummary = new BigDecimal(0);

		BigDecimal beforeVtDefult = new BigDecimal(0);
		BigDecimal vatRateDefult = new BigDecimal(0);
		BigDecimal totalVtDefult = new BigDecimal(0);

		BigDecimal beforeVtzero = new BigDecimal(0);
		BigDecimal vatRatezero = new BigDecimal(0);
		BigDecimal totalVtratezero = new BigDecimal(0);
		
		String userCreBy = "";
		String vatRate = "";
		int autoNumber = 1;
		String vatBefore = "";

		if(CollectionUtils.isNotEmpty(collections)) {
			userCreBy = collections.get(0).getEmpName();
			vatRate = collections.get(0).getVatRate()+"";
			vatBefore = collections.get(0).getVatRate();
			
			for (int i = 0; i < collections.size(); i++) {
				
				if(userCreBy.equals(collections.get(i).getEmpName())) {
					
					if(vatRate.equals(collections.get(i).getVatRate())) {
						vatRate = collections.get(i).getVatRate()+" % ";
					}else {
						if(!vatBefore.equals(collections.get(i).getVatRate())) {
							vatRate = vatRate.concat((collections.get(i).getVatRate()+" % "));
						}
					}
					vatBefore = collections.get(i).getVatRate();
					
					
					InvPaymentOrderTaxBean colles = new InvPaymentOrderTaxBean();
					colles = collections.get(i);
					colles.setAutoNumber(autoNumber);
					colles.setDocumentDate(colles.getDocumentDate());
					colles.setDocumentNo(colles.getDocumentNo());
					colles.setCustName(colles.getCustName());
					colles.setEmpName(colles.getEmpName());
					colles.setTaxId(colles.getTaxId());
					if (colles.getBranchCode().equals("00000")) {
						colles.setBranchCode("สำนักงานใหญ่");
					} else {
						colles.setBranchCode(colles.getBranchCode());
					}

					colles.setSummary(colles.getAmount().setScale(2, RoundingMode.HALF_DOWN));

					// BeforeVat and Vat
					BigDecimal total = colles.getAmount().setScale(2, RoundingMode.HALF_DOWN);
					BigDecimal vat = colles.getVatAmount();

					BigDecimal beforeVats = total.subtract(vat);

					colles.setBeforeVat(beforeVats.setScale(2, RoundingMode.HALF_DOWN));
					colles.setVat(vat.setScale(2, RoundingMode.HALF_DOWN));

					if (Constants.Status.ACTIVE.equals(colles.getPayType())) {
						if ("0".equals(colles.getVatRate())) {
							beforeVtzero = beforeVtzero.add(beforeVats).setScale(2, RoundingMode.HALF_DOWN);
							vatRatezero = vatRatezero.add(vat).setScale(2, RoundingMode.HALF_DOWN);
							totalVtratezero = totalVtratezero.add(colles.getAmount()).setScale(2, RoundingMode.HALF_DOWN);
						} else {
							beforeVtDefult = beforeVtDefult.add(beforeVats).setScale(2, RoundingMode.HALF_DOWN);
							vatRateDefult = vatRateDefult.add(vat).setScale(2, RoundingMode.HALF_DOWN);
							totalVtDefult = totalVtDefult.add(colles.getAmount()).setScale(2, RoundingMode.HALF_DOWN);
						}

						summaryBeforeVt = summaryBeforeVt.add(beforeVats).setScale(2, RoundingMode.HALF_DOWN);
						vatSummary = vatSummary.add(vat).setScale(2, RoundingMode.HALF_DOWN);
						summarySummary = summarySummary.add(colles.getAmount()).setScale(2, RoundingMode.HALF_DOWN);
					}

					colles.setAutoNumberReport(String.valueOf(colles.getAutoNumber()));
					colles.setDocumentDateReport(String.valueOf(dtt.format(colles.getDocumentDate()).toString()));
					colles.setBeforeVatReport(
							String.format("%,.2f", colles.getBeforeVat().setScale(2, RoundingMode.HALF_DOWN)));
					colles.setVatReport(String.format("%,.2f", colles.getVat().setScale(2, RoundingMode.HALF_DOWN)));
					colles.setSummaryReport(String.format("%,.2f", colles.getAmount().setScale(2, RoundingMode.HALF_DOWN)));
					if (Constants.Status.ACTIVE.equals(colles.getPayType())) {
						colles.setPayType("-");
					} else {
						colles.setPayType(Constants.Status.ACTIVE_C);
					}
					printCollections.add(colles);
					
				}else {
					parameters.put("ReportSource", exportPDFReport);

					response.setContentType("application/pdf");
					response.setCharacterEncoding("UTF-8");
					JasperReport jasperReport = JasperCompileManager.compileReport(context.getRealPath(Constants.report.repotPathc)
							+ File.separatorChar + JASPER_JRXML_FILENAME + ".jrxml");
					JRDataSource jrDataSource = (printCollections != null && !printCollections.isEmpty())
							? new JRBeanCollectionDataSource(printCollections)
							: new JREmptyDataSource();
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrDataSource);
					jasperPrints.add(jasperPrint);
					
					userCreBy = collections.get(i).getEmpName();
					printCollections = new ArrayList<InvPaymentOrderTaxBean>();
					exportPDFReport = new InvPaymentOrderTaxBean();
					exportPDFReport.setHeadName(oldHeadName);
					exportPDFReport.setDateForm(fomeDate + " " + creteria.getDateFromHour() + ":" + creteria.getDateFromMinute());
					exportPDFReport.setDateTo(endDate + " " + creteria.getDateToHour() + ":" + creteria.getDateToMinute());
					exportPDFReport.setPrintDate(dateDocument);
					exportPDFReport.setEmpSummaryName(invObject.getEmpName());
					vatRate = collections.get(i).getVatRate()+"";
					
					autoNumber = 1;
					
					summaryBeforeVt = new BigDecimal(0);
					vatSummary = new BigDecimal(0);
					summarySummary = new BigDecimal(0);
					beforeVtDefult = new BigDecimal(0);
					vatRateDefult = new BigDecimal(0);
					totalVtDefult = new BigDecimal(0);
					beforeVtzero = new BigDecimal(0);
					vatRatezero = new BigDecimal(0);
					totalVtratezero = new BigDecimal(0);
					
					InvPaymentOrderTaxBean colles = new InvPaymentOrderTaxBean();
					colles = collections.get(i);
					colles.setAutoNumber(autoNumber);
					colles.setDocumentDate(colles.getDocumentDate());
					colles.setDocumentNo(colles.getDocumentNo());
					colles.setCustName(colles.getCustName());
					colles.setEmpName(colles.getEmpName());
					colles.setTaxId(colles.getTaxId());
					if (colles.getBranchCode().equals("00000")) {
						colles.setBranchCode("สำนักงานใหญ่");
					} else {
						colles.setBranchCode(colles.getBranchCode());
					}

					colles.setSummary(colles.getAmount().setScale(2, RoundingMode.HALF_DOWN));

					// BeforeVat and Vat
					BigDecimal total = colles.getAmount().setScale(2, RoundingMode.HALF_DOWN);
					BigDecimal vat = colles.getVatAmount();

					BigDecimal beforeVats = total.subtract(vat);

					colles.setBeforeVat(beforeVats.setScale(2, RoundingMode.HALF_DOWN));
					colles.setVat(vat.setScale(2, RoundingMode.HALF_DOWN));

					if (Constants.Status.ACTIVE.equals(colles.getPayType())) {
						if ("0".equals(colles.getVatRate())) {
							beforeVtzero = beforeVtzero.add(beforeVats).setScale(2, RoundingMode.HALF_DOWN);
							vatRatezero = vatRatezero.add(vat).setScale(2, RoundingMode.HALF_DOWN);
							totalVtratezero = totalVtratezero.add(colles.getAmount()).setScale(2, RoundingMode.HALF_DOWN);
						} else {
							beforeVtDefult = beforeVtDefult.add(beforeVats).setScale(2, RoundingMode.HALF_DOWN);
							vatRateDefult = vatRateDefult.add(vat).setScale(2, RoundingMode.HALF_DOWN);
							totalVtDefult = totalVtDefult.add(colles.getAmount()).setScale(2, RoundingMode.HALF_DOWN);
						}

						summaryBeforeVt = summaryBeforeVt.add(beforeVats).setScale(2, RoundingMode.HALF_DOWN);
						vatSummary = vatSummary.add(vat).setScale(2, RoundingMode.HALF_DOWN);
						summarySummary = summarySummary.add(colles.getAmount()).setScale(2, RoundingMode.HALF_DOWN);
					}

					colles.setAutoNumberReport(String.valueOf(colles.getAutoNumber()));
					colles.setDocumentDateReport(String.valueOf(dtt.format(colles.getDocumentDate()).toString()));
					colles.setBeforeVatReport(
							String.format("%,.2f", colles.getBeforeVat().setScale(2, RoundingMode.HALF_DOWN)));
					colles.setVatReport(String.format("%,.2f", colles.getVat().setScale(2, RoundingMode.HALF_DOWN)));
					colles.setSummaryReport(String.format("%,.2f", colles.getAmount().setScale(2, RoundingMode.HALF_DOWN)));
					if (Constants.Status.ACTIVE.equals(colles.getPayType())) {
						colles.setPayType("-");
					} else {
						colles.setPayType(Constants.Status.ACTIVE_C);
					}
					printCollections.add(colles);
					
				}
				
				if(i==(collections.size()-1)) {
					parameters.put("ReportSource", exportPDFReport);

					response.setContentType("application/pdf");
					response.setCharacterEncoding("UTF-8");
					JasperReport jasperReport = JasperCompileManager.compileReport(context.getRealPath(Constants.report.repotPathc)
							+ File.separatorChar + JASPER_JRXML_FILENAME + ".jrxml");
					JRDataSource jrDataSource = (printCollections != null && !printCollections.isEmpty())
							? new JRBeanCollectionDataSource(printCollections)
							: new JREmptyDataSource();
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrDataSource);
					jasperPrints.add(jasperPrint);
					
				}
				autoNumber++;
			}
		}
		
		System.out.println(jasperPrints.size());
		return "";
	}

}
