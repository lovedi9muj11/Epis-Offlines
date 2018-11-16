
package th.co.maximus.report.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.itextpdf.text.BaseColor;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.DottedLineSeparator;
import com.lowagie.text.pdf.draw.LineSeparator;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRProperties;
import th.co.maximus.auth.model.UserProfile;
import th.co.maximus.bean.ExportPDFOtherReport;
import th.co.maximus.bean.ExportPDFReport;
import th.co.maximus.bean.HistoryReportBean;
import th.co.maximus.bean.InvEpisOfflineOtherBean;
import th.co.maximus.bean.InvEpisOfflineReportBean;
import th.co.maximus.bean.InvPaymentOrderTaxBean;
import th.co.maximus.bean.MasterDatasBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.model.TrsChequerefEpisOffline;
import th.co.maximus.model.TrsCreditrefEpisOffline;
import th.co.maximus.model.TrsMethodEpisOffline;
import th.co.maximus.model.UserBean;
import th.co.maximus.payment.bean.PaymentResultReq;
import th.co.maximus.service.MasterDataService;
import th.co.maximus.service.PaymentOtherService;
import th.co.maximus.service.ReportService;
import th.co.maximus.service.TrsChequeRefManualService;
import th.co.maximus.service.TrscreDitrefManualService;
import th.co.maximus.service.TrsmethodManualService;

@SuppressWarnings("deprecation")
@Controller
public class EpisReportController {
	@Autowired
	ReportService reportService;

	@Autowired
	private TrscreDitrefManualService trscreDitrefManualService;
	@Autowired
	private TrsChequeRefManualService trsChequeRefManualService;
	@Autowired
	private PaymentOtherService paymentOtherService;

	@Autowired
	private TrsmethodManualService trsmethodManualService;

	@Autowired
	private MasterDataService masterDataService;
	private ServletContext context;
	@Value("${text.prefix}")
	private String nameCode;
	@Value("${text.posno}")
	private String posNo;
	@Value("${text.branarea}")
	private String branArea;
	@Value("${text.branCode}")
	private String branCode;
	@Value("${text.taxid.cat}")
	private String taxidCat;

	@Autowired
	public void setServletContext(ServletContext servletContext) {
		this.context = servletContext;
	}

	@RequestMapping(value = { "/previewPaymentEpisOffline/{documentReport}.pdf" })
	public void previewReturnStockBySerialHTML(HttpServletRequest request, HttpServletResponse response, Model model,
			@PathVariable("documentReport") String documentNo) throws Exception {
///		request.setAttribute("documentReport", "-1");
//		String documentNo = request.getParameter("documentNo");
		List<InvEpisOfflineReportBean> collections = reportService.inqueryEpisOfflineJSONHandler(documentNo);

		if (collections != null) {
			previewEpisOffilneprint(request, response, collections);
		}
	}

	private void previewEpisOffilneprint(HttpServletRequest request, HttpServletResponse response,
			List<InvEpisOfflineReportBean> collections) throws Exception {
		UserProfile profile = (UserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<InvEpisOfflineReportBean> printCollections = new ArrayList<InvEpisOfflineReportBean>();
		InvEpisOfflineReportBean invObject = (InvEpisOfflineReportBean) collections.get(0);
		ExportPDFReport exportPDFReport = new ExportPDFReport();
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = new Date();
		String dateDocument = dt.format(date);
		String JASPER_JRXML_FILENAME = "";
		if (Constants.DOCTYPE.RF.equals(invObject.getDocType())) {
			if (invObject.getDiscount().signum() == 0) {
				JASPER_JRXML_FILENAME = "InvEpisPayment";
			} else {
				JASPER_JRXML_FILENAME = "InvEpisPaymentDiscount";
			}
		} else {
			if (invObject.getDiscount().signum() == 0) {
				JASPER_JRXML_FILENAME = "InvEpisPaymentPasaul";
			} else {
				JASPER_JRXML_FILENAME = "InvEpisPaymentDiscountPasaul";
			}
		}

		MasterDatasBean valueBean = masterDataService.findByKeyCode(branArea);
		UserBean bean = masterDataService.findByUsername(profile.getUsername());
		// UserDto resultUser = userService.findByUsername(profile.getUsername());
		exportPDFReport.setBranArea(valueBean.getValue());
		exportPDFReport.setBracnCode(" " + branCode + " ");
		exportPDFReport.setDocumentDate(invObject.getDocumentDate());
		exportPDFReport.setCustNo(invObject.getCustNo());
		exportPDFReport.setDocumentNo(invObject.getDocumentNo());
		exportPDFReport.setBalanceSummary(invObject.getBalanceSummary().setScale(2, RoundingMode.HALF_DOWN));
		exportPDFReport.setRemark(invObject.getRemark());
		exportPDFReport.setDateDocument(dateDocument);
		exportPDFReport.setSurName("(" + bean.getSurName());
		exportPDFReport.setLastname(bean.getLastName() + ")");

		exportPDFReport.setServiceNo(invObject.getServiceNo());
		if (StringUtils.isNotBlank(invObject.getServiceNo())) {
			exportPDFReport.setCheckSubNo("Y");
		} else {
			exportPDFReport.setCheckSubNo("N");
		}

		exportPDFReport.setBeforeVat(invObject.getBeforeVat().setScale(2, RoundingMode.HALF_DOWN));

		if (Integer.parseInt(invObject.getVatRate()) < 0) {
			exportPDFReport.setVatRate("(Non-VAT)");
		} else {
			exportPDFReport.setVatRate("(VAT " + invObject.getVatRate() + "%)");
		}
		exportPDFReport.setVat(invObject.getVat().setScale(2, RoundingMode.HALF_DOWN));

		exportPDFReport.setCustName(invObject.getCustName());
		exportPDFReport.setCustomerAddress(invObject.getCustomerAddress());
		exportPDFReport.setTaxId(invObject.getTaxId());

		if (StringUtils.isNotBlank(invObject.getCustName())) {
			exportPDFReport.setCheckCustomerName("Y");
		} else {
			exportPDFReport.setCheckCustomerName("N");
		}
		if (StringUtils.isNotBlank(invObject.getCustomerAddress())) {
			exportPDFReport.setCheckAddress("Y");
		} else {
			exportPDFReport.setCheckAddress("N");
		}
		if (StringUtils.isNotBlank(invObject.getTaxId())) {
			exportPDFReport.setCheckTaxId("Y");
		} else {
			exportPDFReport.setCheckTaxId("N");
		}

		String preiod = "";
		if (invObject.getPreiod() != null) {
			String preiods = invObject.getPreiod();
			String yearFrist = preiods.substring(0, 4);
			String mountFrist = preiods.substring(4, 6);
			String dayFrist = preiods.substring(6, 8);
			String yearEnd = preiods.substring(8, 12);
			String mountEnd = preiods.substring(12, 14);
			String dayEnd = preiods.substring(14, 16);

			preiod = dayFrist + "/" + mountFrist + "/" + yearFrist + "-" + dayEnd + "/" + mountEnd + "/" + yearEnd;
			exportPDFReport.setPreiod(preiod);
		} else {
			exportPDFReport.setPreiod(preiod);
		}

		String paymentCodeRes = "";
		String checkWT = "";

		List<String> result = new ArrayList<>();
		String payCode = "";
		InvEpisOfflineReportBean stockObject = (InvEpisOfflineReportBean) collections.get(0);
		List<TrsMethodEpisOffline> data = trsmethodManualService
				.TrsmethodManualAll(Long.parseLong(stockObject.getManualId()));
		for (int t = 0; t < data.size(); t++) {
			TrsMethodEpisOffline rs = (TrsMethodEpisOffline) data.get(t);
			if (rs.getCode().equals("CC")) {
				payCode = "เงินสด";
				result.add(payCode);
			} else if (rs.getCode().equals("CR")) {
				List<TrsCreditrefEpisOffline> res = trscreDitrefManualService.findByMethodId(rs.getId());
				for (int f = 0; f < res.size(); f++) {
					String code = res.get(f).getCreditNo();
					payCode = "บัตรเครดิต" + " " + res.get(f).getCardtype() + " " + "เลขที่ : ************"
							+ code.substring(12, 16);
					result.add(payCode);
				}

			} else if (rs.getCode().equals("CH")) {
				List<TrsChequerefEpisOffline> res = trsChequeRefManualService.findTrsCredit(rs.getId());
				for (int f = 0; f < res.size(); f++) {
					payCode = "เช็ค " + res.get(f).getPublisher() + "เลขที่ :" + res.get(f).getChequeNo();
					result.add(payCode);
				}

			}
		}

		for (int i = 0; i < collections.size(); i++) {
			InvEpisOfflineReportBean stockObjects = (InvEpisOfflineReportBean) collections.get(i);
			if (stockObjects.getPaymentCode().equals("DEDUC")) {
				checkWT = "WT";
				result.add(checkWT);
			}

		}
		for (int f = 0; f < result.size(); f++) {
			if (f == 0) {
				paymentCodeRes += result.get(f);
			} else {
				paymentCodeRes += " + " + result.get(f);
			}

		}

		String bran = "";
		if (StringUtils.isNotBlank(invObject.getBracnCode())) {
			if (invObject.getBracnCode().equals("00000")) {
				bran = "สำนักงานใหญ่";
				exportPDFReport.setCheckBran("Y");
				exportPDFReport.setBranAreaCheck("Y");
			} else {
				bran = invObject.getBracnCode();
				exportPDFReport.setCheckBran("Y");
				exportPDFReport.setBranAreaCheck("N");
			}
		} else {
			exportPDFReport.setCheckBran("N");
		}

		exportPDFReport.setPaymentCode(paymentCodeRes);
		exportPDFReport.setSouce(bran);
		if (invObject.getDiscount().signum() == 0) {
			exportPDFReport.setDiscount(invObject.getDiscount());
			exportPDFReport.setCheckDiscount("N");
		} else {
			exportPDFReport.setDiscount(invObject.getDiscount());
			exportPDFReport.setCheckDiscount("Y");
		}
		exportPDFReport.setDiscount(invObject.getDiscount().setScale(2, RoundingMode.HALF_DOWN));
		exportPDFReport.setAmountPayment(invObject.getAmountPayment().setScale(2, RoundingMode.HALF_DOWN));
		exportPDFReport.setInvoiceNo(invObject.getInvoiceNo());

		exportPDFReport
				.setDiscountStr(String.format("%,.2f", invObject.getDiscount().setScale(2, RoundingMode.HALF_DOWN)));
		exportPDFReport.setAmountPaymentStr(
				String.format("%,.2f", invObject.getAmountPayment().setScale(2, RoundingMode.HALF_DOWN)));
		exportPDFReport.setBalanceSummaryStr(
				String.format("%,.2f", invObject.getBalanceSummary().setScale(2, RoundingMode.HALF_DOWN)));
		exportPDFReport
				.setBeforeVatStr(String.format("%,.2f", invObject.getBeforeVat().setScale(2, RoundingMode.HALF_DOWN)));
		exportPDFReport.setVatStr(String.format("%,.2f", invObject.getVat().setScale(2, RoundingMode.HALF_DOWN)));

		parameters.put("ReportSource", exportPDFReport);

		response.setContentType("application/pdf");
		response.setCharacterEncoding("UTF-8");
		JasperReport jasperReport = JasperCompileManager.compileReport(context.getRealPath(Constants.report.repotPathc)
				+ File.separatorChar + JASPER_JRXML_FILENAME + ".jrxml");
		JRDataSource jrDataSource = (printCollections != null && !printCollections.isEmpty())
				? new JRBeanCollectionDataSource(printCollections)
				: new JREmptyDataSource();
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrDataSource);
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}

	@RequestMapping(value = { "/previewPaymentEpisOfflineOther/{documentReport}.pdf" })
	public void previewPaymentEpisOfflineOther(HttpServletRequest request, HttpServletResponse response, Model model,
			@PathVariable("documentReport") String documentNo) throws Exception {
		// String documentNo = "";
		String JASPER_JRXML_FILENAME = "InvEpisPaymentOther";
		request.setAttribute("documentReport", "-1");
//		String documentNo = request.getParameter("documentNo");
		List<InvEpisOfflineOtherBean> collections = reportService.inqueryEpisOfflineOtherJSONHandler(documentNo);

		if (collections != null) {
			previewEpisOffilneprintOther(request, response, collections, JASPER_JRXML_FILENAME);
		}
	}

	private void previewEpisOffilneprintOther(HttpServletRequest request, HttpServletResponse response,
			List<InvEpisOfflineOtherBean> collections, final String JASPER_JRXML_FILENAME) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<InvEpisOfflineOtherBean> printCollections = collections;
		List<PaymentResultReq> invObject = paymentOtherService.findListByid(printCollections.get(0).getManualId());
		UserProfile profile = (UserProfile) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		ExportPDFOtherReport exportPDFReport = new ExportPDFOtherReport();
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		Date date = new Date();
		String dateDocument = dt.format(date);

		if (StringUtils.isNotBlank(printCollections.get(0).getVatRate())) {
			exportPDFReport.setVatRateCheck("Y");

			if (Constants.DOCTYPE.RF.equals(printCollections.get(0).getDoctype())) {
				exportPDFReport.setSentStringHeader("N");
			} else if (Constants.DOCTYPE.RS.equals(printCollections.get(0).getDoctype())) {
				exportPDFReport.setSentStringHeader("Y");
			}

		} else {
			exportPDFReport.setVatRateCheck("N");
			exportPDFReport.setSentStringHeader("N");
			exportPDFReport.setDiscountSpecialCheck("N");
		}

		BigDecimal spDis = new BigDecimal(0);
		for (PaymentResultReq paymentResultReq : invObject) {
			spDis.add(paymentResultReq.getDiscountspacal());
		}

		if (spDis.compareTo(BigDecimal.ZERO) == 0) {
			exportPDFReport.setDiscountSpecialCheck("Y");
			// exportPDFReport.setDiscountSpecial(zro.setScale(2, RoundingMode.HALF_DOWN));
		} else {
			exportPDFReport.setDiscountSpecial(spDis.setScale(2, RoundingMode.HALF_DOWN));
			exportPDFReport.setDiscountSpecialCheck("N");
		}

		MasterDatasBean valueBean = masterDataService.findByKeyCode(printCollections.get(0).getBranArea());
		UserBean bean = masterDataService.findByUsername(profile.getUsername());
		exportPDFReport.setBranArea(valueBean.getProperty1() + " " + valueBean.getValue());
		exportPDFReport.setBracnCode(printCollections.get(0).getBracnCode());
		exportPDFReport.setDocumentDate(printCollections.get(0).getDocumentDate());
		exportPDFReport.setCustNo(printCollections.get(0).getCustNo());
		exportPDFReport.setVatRate(printCollections.get(0).getVatRate());

		if (StringUtils.isNotBlank(printCollections.get(0).getCustName())) {
			exportPDFReport.setCustNameCheck("Y");
		} else {
			exportPDFReport.setCustNameCheck("N");
		}

		if (StringUtils.isNotBlank(printCollections.get(0).getCustomerAddress())) {
			exportPDFReport.setAddressCheck("Y");

		} else {
			exportPDFReport.setAddressCheck("N");
		}

		if (StringUtils.isNotBlank(printCollections.get(0).getTaxId())) {
			exportPDFReport.setTaxIdCheck("Y");
		} else {
			exportPDFReport.setTaxIdCheck("N");
		}

		exportPDFReport.setCustomerAddress(printCollections.get(0).getCustomerAddress());
		exportPDFReport.setCustName(printCollections.get(0).getCustName());
		exportPDFReport.setDocumentNo(printCollections.get(0).getDocumentNo());
		exportPDFReport.setBalanceSummaryStr(String.format("%,.2f", printCollections.get(0).getBalanceSummary()
				.setScale(2, RoundingMode.HALF_DOWN).add(spDis.setScale(2, RoundingMode.HALF_DOWN))));

		exportPDFReport.setTaxId(printCollections.get(0).getTaxId());
		exportPDFReport.setRemark(printCollections.get(0).getRemark());
		exportPDFReport.setDateDocument(dateDocument);

		exportPDFReport.setSurName("(" + bean.getSurName());
		exportPDFReport.setLastname(bean.getLastName() + ")");
		// exportPDFReport.setServiceName(invObject.getServiceName());
		exportPDFReport.setAmount(printCollections.get(0).getAmount());
		// exportPDFReport.setDiscountbeforvat(invObject.getDiscountbeforvat().setScale(2,
		// RoundingMode.HALF_DOWN));

//		if (printCollections.get(0).getBalanceSummary().signum() == 0) {
//			exportPDFReport.setBalanceBeforeCheck("Y");
//			exportPDFReport.setBalanceBeforeStr(String.format("%,.2f",printCollections.get(0).getBalanceSummary()));
//			
//		} else {
//			exportPDFReport.setBalanceBeforeCheck("N");
//			exportPDFReport.setBalanceBeforeStr(String.format("%,.2f",printCollections.get(0).getBalanceSummary()));
//			
//		}
//		exportPDFReport
//				.setBalanceBeforeStr(String.format("%,.2f",printCollections.get(0).getBalanceSummary().setScale(2, RoundingMode.HALF_DOWN)));

		// String nameService = "";
		// nameService = invObject.getBracnCode() + invObject.getBranArea() +
		// invObject.getSouce();

		// String payCode = "";
		// List<String> result = new ArrayList<>();
		// for (int i = 0; i < collections.size(); i++) {
		// InvEpisOfflineOtherBean stockObject = (InvEpisOfflineOtherBean)
		// collections.get(i);
		//
		// result.add(stockObject.getPaymentCode());
		//
		// }
		// for (int f = 0; f < result.size(); f++) {
		// payCode += "-" + result.get(f);
		// }

		String paymentCodeRes = "";
		String checkWT = "";
		List<String> result = new ArrayList<>();
		for (int i = 0; i < collections.size(); i++) {
			String payCode = "";
			InvEpisOfflineOtherBean stockObject = collections.get(i);

			if (stockObject.getPaymentCode().equals("CC")) {
				payCode = "เงินสด";
				result.add(payCode);
			} else if (stockObject.getPaymentCode().equals("CR")) {
				List<TrsCreditrefEpisOffline> res = trscreDitrefManualService.findByMethodId(stockObject.getMethodId());
				String code = res.get(0).getCreditNo();
				payCode = "บัตรเครดิต" + " " + res.get(0).getCardtype() + " " + "เลขที่ : ************"
						+ code.substring(12, 16);
				result.add(payCode);
			} else if (stockObject.getPaymentCode().equals("CH")) {
				List<TrsChequerefEpisOffline> res = trsChequeRefManualService.findTrsCredit(stockObject.getMethodId());
				payCode = "เช็ค " + res.get(0).getPublisher() + "เลขที่ :" + res.get(0).getChequeNo();
				result.add(payCode);
			}

		}
		for (int i = 0; i < collections.size(); i++) {
			InvEpisOfflineOtherBean stockObject = (InvEpisOfflineOtherBean) collections.get(i);
			if (stockObject.getPaymentCode().equals("DEDUC")) {
				checkWT = "WT";
				result.add(checkWT);
			}

		}
		for (int f = 0; f < result.size(); f++) {
			if (f == 0) {
				paymentCodeRes += result.get(f);
			} else {
				paymentCodeRes += paymentCodeRes.equals(result.get(f)) ? "" : " + " + result.get(f);
				// paymentCodeRes += " + " + result.get(f);
			}

		}
		List<InvEpisOfflineOtherBean> printCollections2 = new ArrayList<InvEpisOfflineOtherBean>();
		int i = 1;
		BigDecimal discountSpecial = BigDecimal.ZERO;
		BigDecimal beforeVatStr = BigDecimal.ZERO;
		BigDecimal vats = BigDecimal.ZERO;
		for (PaymentResultReq paymentResultReq : invObject) {
			InvEpisOfflineOtherBean jp = new InvEpisOfflineOtherBean();
			jp.setRunnumber(String.valueOf(i++));
			jp.setServiceNameStr(String.format(paymentResultReq.getServiceName()));
			jp.setBeforeDiscount(String.format("%,.2f",
					paymentResultReq.getBeforeVat().multiply(new BigDecimal(paymentResultReq.getQuantity()))));
			jp.setDiscountbeforvatStr(String.format("%,.2f", paymentResultReq.getDiscount()));
			jp.setAmountStr(String.format("%,.2f",
					paymentResultReq.getBeforeVat().multiply(new BigDecimal(paymentResultReq.getQuantity()))
							.subtract(paymentResultReq.getDiscount())));

			// .add(paymentResultReq.getDiscountspacal())));
			discountSpecial = discountSpecial.add(paymentResultReq.getDiscountspacal());
			beforeVatStr = beforeVatStr
					.add(paymentResultReq.getBeforeVat().multiply(new BigDecimal(paymentResultReq.getQuantity()))
							.subtract(paymentResultReq.getDiscount()).subtract(paymentResultReq.getDiscountspacal()));
			vats = vats.add(paymentResultReq.getVat());
			printCollections2.add(jp);
		}

		if (printCollections.get(0).getVatRate().equals("nonVat")) {
			exportPDFReport.setVatRateCheck("N");
			exportPDFReport.setSentStringHeader("N");
			exportPDFReport.setDiscountSpecialCheck("N");

		} else {
			BigDecimal total = printCollections.get(0).getBalanceSummary().setScale(2, RoundingMode.HALF_DOWN)
					.add(spDis.setScale(2, RoundingMode.HALF_DOWN));
			BigDecimal vatRate = new BigDecimal(
					StringUtils.isNotBlank(printCollections.get(0).getVatRate()) ? printCollections.get(0).getVatRate()
							: BigDecimal.ZERO.toString());
			BigDecimal resVat = vatRate;

			BigDecimal beforeVat = total.multiply(vatRate);

			BigDecimal vat = beforeVat.divide(resVat, 2, RoundingMode.HALF_UP);

			BigDecimal beforeVats = total.subtract(vat);

			BigDecimal vatSum = beforeVats.add(vat);

			// exportPDFReport.setBeforeVatStr(String.format("%,.2f", beforeVats.setScale(2,
			// RoundingMode.HALF_DOWN)));
			exportPDFReport.setBeforeVatStr(String.format("%,.2f", beforeVatStr));
//			exportPDFReport.setVatStr(String.format("%,.2f", vat.setScale(2, RoundingMode.HALF_DOWN)));
			exportPDFReport.setVatStr(String.format("%,.2f", vats));
			exportPDFReport.setVatSum(String.format("%,.2f", vatSum.setScale(2, RoundingMode.HALF_DOWN)));

		}

		// exportPDFReport.setBalanceBeforeStr(String.format("%,.2f",
		// printCollections.get(0).getBalanceSummary()));
		// discountspacial
		if (discountSpecial.signum() == 0) {
			exportPDFReport.setDiscountSpecialCheck("Y");
			exportPDFReport.setDiscountSpecialStr(String.format("%,.2f", discountSpecial));
		} else {
			exportPDFReport.setDiscountSpecialCheck("N");
			exportPDFReport.setDiscountSpecialStr(String.format("%,.2f", discountSpecial));
		}

		String bran = "";
		if (printCollections.get(0).getBracnCode().equals("00000")) {
			bran = "สำนักงานใหญ่";
			exportPDFReport.setCheckBran("N");
		} else if (printCollections.get(0).getBracnCode().equals("")) {
			exportPDFReport.setCheckBran("N");
		} else {
			bran = printCollections.get(0).getBracnCode();
			exportPDFReport.setCheckBran("Y");
		}

		exportPDFReport.setSouce(bran);
		exportPDFReport.setPaymentCode(paymentCodeRes);

		parameters.put("ReportSource", exportPDFReport);

		response.setContentType("application/pdf");
		response.setCharacterEncoding("UTF-8");
		// JRProperties.setProperty("net.sf.jasperreports.default.pdf.font.name",
		// "THSarabun.ttf");
		// JRProperties.setProperty("net.sf.jasperreports.default.pdf.encoding",
		// "UTF-8");
		// JRProperties.setProperty("net.sf.jasperreports.default.pdf.embedded",
		// "true");
		JasperReport jasperReport = JasperCompileManager.compileReport(context.getRealPath(Constants.report.repotPathc)
				+ File.separatorChar + JASPER_JRXML_FILENAME + ".jrxml");
		JRDataSource jrDataSource = (printCollections2 != null && !printCollections2.isEmpty())
				? new JRBeanCollectionDataSource(printCollections2)
				: new JREmptyDataSource();

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrDataSource);
		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
		// exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
	}

	@RequestMapping(value = { "previewPaymentPrintOrder.pdf" })
	public void paymentPrint(HistoryReportBean creteria, HttpServletRequest request, HttpServletResponse response,
			Model model) throws Exception {
		if (creteria != null) {
			String JASPER_JRXML_FILENAME = "InvPaymentOrderTax";
			List<InvPaymentOrderTaxBean> collections = reportService.inqueryInvPaymentOrderTaxBeanJSONHandler(creteria);
			List<InvPaymentOrderTaxBean> summarryVat = reportService.vatSummarry(creteria, true);
			List<InvPaymentOrderTaxBean> summarry = reportService.vatSummarry(creteria, false);
			
			if (collections != null) {
				InvPaymentOrderTaxBean  a =  previewPaymentPrintOrder(request, response, collections, JASPER_JRXML_FILENAME, creteria);
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition", "inline;filename=11222.pdf");
				createPdf(response,summarryVat,summarry,collections,a);
			}

		}


	}

	public void createPdf(HttpServletResponse response , List<InvPaymentOrderTaxBean> summarryVat, List<InvPaymentOrderTaxBean> summarry,List<InvPaymentOrderTaxBean> collections ,InvPaymentOrderTaxBean invPaymentOrderTaxBean) throws IOException, DocumentException {
		// step 1
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		Document document = new Document();
		PdfPTable table;
		Paragraph p;
		PdfWriter.getInstance(document, response.getOutputStream());
		Rectangle one = new Rectangle(595, 1100);
		document.setPageSize(one.rotate());
		// step 2
		LineSeparator ls = new LineSeparator();
		ls.setOffset(-2);

		BaseFont courier = BaseFont.createFont("font/newFL.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
		Font fontNormal = new Font(courier, 14, Font.NORMAL);
		Font fontHeadNormal = new Font(courier, 20, Font.NORMAL);
		Font fontSmall = new Font(courier, 10, Font.NORMAL);
		Font fontTableHead = new Font(courier, 12, Font.NORMAL);
		Font fontSS = new Font(courier, 8, Font.NORMAL);
		Font fontEndTable = new Font(courier, 2, Font.NORMAL);
		Font fontSSS = new Font(courier,4, Font.NORMAL);
		// step 3
		String s = "";
		document.open();
		// step 4
			p = new Paragraph(invPaymentOrderTaxBean.getHeadName(),fontHeadNormal);
		String sum = "       																			                                                    ";
		String sum1 = "          ";
		p.setAlignment(Element.ALIGN_CENTER);
		document.add(p);
		p = new Paragraph("	หน้า "+(document.getPageNumber()+1) +" จาก "+(document.getPageNumber()+1), fontSmall);
		p.setAlignment(Element.ALIGN_RIGHT);
		document.add(p);

		document.add(createParagraphWithSpaces(fontNormal,"บริษัท กสท โทรคมนาคม จำกัด (มหาชน)","%110s", "ระหว่างวันที่ : "+invPaymentOrderTaxBean.getDateForm()+"  ถึง: "+invPaymentOrderTaxBean.getDateTo(),"%110s", "วันเวลาที่พิมพ์ :"+invPaymentOrderTaxBean.getPrintDate()));
		document.add( new Paragraph(" ", fontSS));
//		document.add(createParagraphWithSpaces(fontNormal,"ชื่อสถานประกอบการ :"+invPaymentOrderTaxBean.getBranchArea(),"%84s", "เลขประจำตัวผู้เสียภาษีอากร :"+if(invPaymentOrderTaxBean.getTaxId() != null) {invPaymentOrderTaxBean.getTaxId():"","%70s", ""));
		document.add( new Paragraph(" ", fontSS));
		document.add( new Paragraph("สาขาที่  :  "+invPaymentOrderTaxBean.getBranchCodeEmp(), fontNormal));
		document.add( new Paragraph(" ", fontSS));
		// step 5
		document.add(ls);
		table = createFirstTable(fontTableHead);
		document.add(table);
		document.add(ls);
		for (int i = 0; i < collections.size(); i++) {
			int index = i+1;
			table = createFirstTable2(fontTableHead,index,collections.get(i));
			document.add(table);
		}
	
		document.add(ls);
		document.add( new Paragraph(" ", fontEndTable));
		document.add(ls);
		document.add( new Paragraph(" ", fontSSS));
		document.add(ls);
		p =  new Paragraph("รวมตาม"+ invPaymentOrderTaxBean.getEmpSummaryName(), fontTableHead);
		p.setAlignment(Element.ALIGN_MIDDLE);
	
		for (InvPaymentOrderTaxBean sumvat : summarryVat) {
			document.add(ls);
			p =  new Paragraph("รวมอัตรา   "+ sumvat.getVatRate() +"%"+String.format("%237s", 	String.format("%,.2f", sumvat.getBeforeVatSummary().setScale(2, RoundingMode.HALF_DOWN)))+String.format("%30s", String.format("%,.2f", sumvat.getVatSummary().setScale(2, RoundingMode.HALF_DOWN)))+String.format("%30s", String.format("%,.2f", sumvat.getSummarySummary().setScale(2, RoundingMode.HALF_DOWN))), fontTableHead);
			p.setAlignment(Element.ALIGN_MIDDLE);
			document.add(p);
		} 
		
		document.add(ls);
		p =  new Paragraph("รวมทั้งสิ้น        "+String.format("%239s", 	String.format("%,.2f", summarry.get(0).getBeforeVatSummary().setScale(2, RoundingMode.HALF_DOWN)))+String.format("%30s", String.format("%,.2f", summarry.get(0).getVatSummary().setScale(2, RoundingMode.HALF_DOWN)))+String.format("%30s", String.format("%,.2f", summarry.get(0).getSummarySummary().setScale(2, RoundingMode.HALF_DOWN))), fontTableHead);
		p.setAlignment(Element.ALIGN_MIDDLE);
		document.add(p);
		document.add(ls);
		document.close();
//			 OutputStream os = response.getOutputStream();
//			 out.writeTo(os);
//	            os.flush();
//	            os.close();
//			return out.toByteArray();
	}

	

	public Paragraph createParagraphWithSpaces(Font font, String value1,String setTab1, String value2,String setTab2, String value3) {
		Paragraph p = new Paragraph();
		p.setFont(font);
		p.add(String.format(value1));
		p.add(String.format(setTab1, value2));
		p.add(String.format(setTab2, value3));
		return p;
	}

	/**
	 * Creates our first table
	 * 
	 * @return our first table
	 * @throws DocumentException 
	 */
	public static PdfPTable createFirstTable(Font font) throws DocumentException {
		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 2f, 6f, 6f, 8f, 5f, 5f, 5f, 5f, 5f, 5f });
	
		PdfPCell cell1 = new PdfPCell(new Phrase("ลำดับL",font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase("วันเดือนปีL",font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase("เลขที่ใบกำกับภาษีL",font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase("ชื่อผู้ซื้อสินค้า / ผู้รับบริการ",font));
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase("TAX ID",font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase("สาขาที่",font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase("มูลค่าสินค้าหรือบริการ",font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase("จำนวนเงินภาษีมูลค่าเพิ่มL",font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase("จำนวนเงินรวม",font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase("สถานะ",font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase("ลำดับ",font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);

		return table;
	}
	public static PdfPTable createFirstTable2(Font font,int index,InvPaymentOrderTaxBean data) throws DocumentException {
		PdfPTable table = new PdfPTable(10);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] { 2f, 6f, 6f, 8f, 5f, 5f, 5f, 5f, 5f, 5f });
	
		PdfPCell cell1 = new PdfPCell(new Phrase(index+"",font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(data.getDocumentDateReport(),font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(data.getDocumentNo(),font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(data.getCustName() ,font));
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.setBorder(PdfPCell.NO_BORDER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(data.getTaxId(),font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(data.getBranchCode(),font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(data.getBeforeVatReport(),font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(data.getVatReport(),font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(data.getSummaryReport(),font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
		cell1 = new PdfPCell(new Phrase(data.getPayType(),font));
		cell1.setBorder(PdfPCell.NO_BORDER);
		cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell1.setFixedHeight(40f);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell1);
	

		return table;
	}


//	private static void addCellHead(PdfPTable table, String text, int rowspan,Font font)
//	{
//	  
//	    PdfPCell cell = new PdfPCell(new Phrase(text, font));
//	    cell.Rowspan = rowspan;
//	    cell.HorizontalAlignment = PdfPCell.ALIGN_CENTER;
//	    cell.VerticalAlignment = PdfPCell.ALIGN_MIDDLE;
//	    table.AddCell(cell);
//	}
	private  InvPaymentOrderTaxBean previewPaymentPrintOrder(HttpServletRequest request, HttpServletResponse response,
			List<InvPaymentOrderTaxBean> collections, final String JASPER_JRXML_FILENAME, HistoryReportBean creteria)
			throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<InvPaymentOrderTaxBean> printCollections = new ArrayList<InvPaymentOrderTaxBean>();
		InvPaymentOrderTaxBean invObject = (InvPaymentOrderTaxBean) collections.get(0);
		InvPaymentOrderTaxBean exportPDFReport = new InvPaymentOrderTaxBean();

		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:ss");
		SimpleDateFormat dtt = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		String dateDocument = dt.format(date);
		if (Constants.DOCTYPE.RF.equals(creteria.getTypePrint())) {
			exportPDFReport.setHeadName("รายงานภาษีใบเสร็จรับเงิน/ใบกำกับภาษีเต็มรูป");
			exportPDFReport.setReportStatus("1");
		} else {
			exportPDFReport.setHeadName("รายงานภาษีใบเสร็จรับเงิน/ใบกำกับภาษีอย่างย่อ");
		}
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

		MasterDatasBean valueBean = masterDataService.findByKeyCode(branArea);
		exportPDFReport.setDateForm(fomeDate + " " + creteria.getDateFromHour() + ":" + creteria.getDateFromMinute());
		exportPDFReport.setDateTo(endDate + " " + creteria.getDateToHour() + ":" + creteria.getDateToMinute());
		exportPDFReport.setPrintDate(dateDocument);
		exportPDFReport.setBranchArea(valueBean.getValue());
		exportPDFReport.setInvoiceNo(taxidCat);
		exportPDFReport.setBranchCodeEmp(branCode);
		exportPDFReport.setVatRate(invObject.getVatRate());
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

		for (int i = 0; i < collections.size(); i++) {
			InvPaymentOrderTaxBean colles = new InvPaymentOrderTaxBean();
			colles = collections.get(i);
			colles.setAutoNumber(i + 1);
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

			colles.setSummary(colles.getSummary().setScale(2, RoundingMode.HALF_DOWN));

			// BeforeVat and Vat
			BigDecimal total = colles.getSummary().setScale(2, RoundingMode.HALF_DOWN);
			BigDecimal vatRate = new BigDecimal(colles.getVatRate());
			BigDecimal resVat =vatRate;
			BigDecimal beforeVat = total.multiply(vatRate);
			BigDecimal vat = beforeVat.divide(resVat, 2, RoundingMode.HALF_UP);

			BigDecimal beforeVats = total.subtract(vat);

			colles.setBeforeVat(beforeVats.setScale(2, RoundingMode.HALF_DOWN));
			colles.setVat(vat.setScale(2, RoundingMode.HALF_DOWN));

			if (Constants.Status.ACTIVE.equals(colles.getPayType())) {
				if (colles.getVatRate() == 0) {
					beforeVtzero = beforeVtzero.add(beforeVats).setScale(2, RoundingMode.HALF_DOWN);
					vatRatezero = vatRatezero.add(vat).setScale(2, RoundingMode.HALF_DOWN);
					totalVtratezero = totalVtratezero.add(colles.getSummary()).setScale(2, RoundingMode.HALF_DOWN);
				} else {
					beforeVtDefult = beforeVtDefult.add(beforeVats).setScale(2, RoundingMode.HALF_DOWN);
					vatRateDefult = vatRateDefult.add(vat).setScale(2, RoundingMode.HALF_DOWN);
					totalVtDefult = totalVtDefult.add(colles.getSummary()).setScale(2, RoundingMode.HALF_DOWN);
				}

				summaryBeforeVt = summaryBeforeVt.add(beforeVats).setScale(2, RoundingMode.HALF_DOWN);
				vatSummary = vatSummary.add(vat).setScale(2, RoundingMode.HALF_DOWN);
				summarySummary = summarySummary.add(colles.getSummary()).setScale(2, RoundingMode.HALF_DOWN);
			}

			colles.setAutoNumberReport(String.valueOf(colles.getAutoNumber()));
			colles.setDocumentDateReport(String.valueOf(dtt.format(colles.getDocumentDate()).toString()));
			colles.setBeforeVatReport(
					String.format("%,.2f", colles.getBeforeVat().setScale(2, RoundingMode.HALF_DOWN)));
			colles.setVatReport(String.format("%,.2f", colles.getVat().setScale(2, RoundingMode.HALF_DOWN)));
			colles.setSummaryReport(String.format("%,.2f", colles.getSummary().setScale(2, RoundingMode.HALF_DOWN)));
			if (Constants.Status.ACTIVE.equals(colles.getPayType())) {
				colles.setPayType("-");
			} else {
				colles.setPayType(Constants.Status.ACTIVE_C);
			}
			printCollections.add(colles);

		}

		exportPDFReport.setBeforeVatRq(beforeVtDefult);
		exportPDFReport.setBeforeVatRqStr(String.format("%,.2f", beforeVtDefult.setScale(2, RoundingMode.HALF_DOWN)));
		exportPDFReport.setVatRq(vatRateDefult);
		exportPDFReport.setVatRqStr(String.format("%,.2f", vatRateDefult.setScale(2, RoundingMode.HALF_DOWN)));
		exportPDFReport.setSummaryRq(totalVtDefult);
		exportPDFReport.setSummaryRqStr(String.format("%,.2f", totalVtDefult.setScale(2, RoundingMode.HALF_DOWN)));

		exportPDFReport.setBeforeVatZero(beforeVtzero);
		exportPDFReport.setBeforeVatZeroStr(String.format("%,.2f", beforeVtzero.setScale(2, RoundingMode.HALF_DOWN)));
		exportPDFReport.setVatZero(vatRatezero);
		exportPDFReport.setVatZeroStr(String.format("%,.2f", vatRatezero.setScale(2, RoundingMode.HALF_DOWN)));
		exportPDFReport.setSummaryZero(totalVtratezero);
		exportPDFReport.setSummaryZeroStr(String.format("%,.2f", totalVtratezero.setScale(2, RoundingMode.HALF_DOWN)));

		exportPDFReport.setBeforeVatSummary(summaryBeforeVt);
		exportPDFReport
				.setBeforeVatSummaryStr(String.format("%,.2f", summaryBeforeVt.setScale(2, RoundingMode.HALF_DOWN)));
		exportPDFReport.setVatSummary(vatSummary);
		exportPDFReport.setVatSummaryStr(String.format("%,.2f", vatSummary.setScale(2, RoundingMode.HALF_DOWN)));
		exportPDFReport.setSummarySummary(summarySummary);
		exportPDFReport
				.setSummarySummaryStr(String.format("%,.2f", summarySummary.setScale(2, RoundingMode.HALF_DOWN)));
		collections = printCollections;
		return exportPDFReport;

//		parameters.put("ReportSource", exportPDFReport);

//		response.setContentType("application/pdf");
//		response.setCharacterEncoding("UTF-8");
//		JasperReport jasperReport = JasperCompileManager.compileReport(context.getRealPath(Constants.report.repotPathc)
//				+ File.separatorChar + JASPER_JRXML_FILENAME + ".jrxml");
//		JRDataSource jrDataSource = (printCollections != null && !printCollections.isEmpty())
//				? new JRBeanCollectionDataSource(printCollections)
//				: new JREmptyDataSource();
//		JRProperties.setProperty("net.sf.jasperreports.default.pdf.font.name", "th/co/maximus/report/font/newFL.ttf");
//		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, jrDataSource);
//
//		JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
	}

	public static final String convertDateString(String str) {
		return str.replaceAll("([0-9]{2})/([0-9]{2})/([0-9]{4})", "$3-$2-$1");

	}
}
