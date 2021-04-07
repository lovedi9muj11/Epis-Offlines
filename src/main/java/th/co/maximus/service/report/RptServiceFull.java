package th.co.maximus.service.report;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import th.co.maximus.auth.model.UserProfile;
import th.co.maximus.bean.HistoryPaymentRS;
import th.co.maximus.bean.HistoryReportBean;
import th.co.maximus.bean.ReportTaxRSBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.dao.MasterDatasDao;
import th.co.maximus.model.UserBean;
import th.co.maximus.service.MasterDataService;

@Service("rptServiceFull")
public class RptServiceFull extends BaseExcelRptService{
	
	@Autowired
	private MasterDatasDao masterDatasDao;
	
	@Autowired
	MasterDataService masterDataService;

	
	protected Logger log = Logger.getLogger(getClass());
	Locale localeTH = new Locale("th", "TH");
	Locale localeEN = new Locale("en", "EN");
	SimpleDateFormat formate = new SimpleDateFormat("dd/MM/yyyy HH:mm", localeTH);
	SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy ", new Locale("th", "TH"));
	SimpleDateFormat formateEN = new SimpleDateFormat("dd/MM/yyyy", localeEN);
	SimpleDateFormat formateDateTH = new SimpleDateFormat("dd/MM/yyyy", localeTH);
	SimpleDateFormat formateDateEN = new SimpleDateFormat("dd/MM/yyyy", localeEN);
	SimpleDateFormat formateYearTH = new SimpleDateFormat("yyyy", localeTH);
	SimpleDateFormat formateHH = new SimpleDateFormat("HH:mm", localeTH);

	public Workbook getReport(Workbook workbook, List<HistoryPaymentRS> entity, HistoryReportBean bean) throws Exception{
		SimpleDateFormat formatter3 = new SimpleDateFormat(Constants.DateTime.DB_DATE_FORMAT, new Locale("th", "TH"));
		Font fontNormal = createFontTHSarabanPSK(workbook, 14, false);
		Font fontBold = createFontTHSarabanPSK(workbook, 14, true);
		CellStyle txtCentertNoBor = createStyleCellCenter(workbook, fontBold, false);
		CellStyle txtRightNoBor = createStyleCellRight(workbook, fontBold, false);
		CellStyle txtLeft = createCellStyleForTextLeft(workbook, fontNormal, true);
		CellStyle numRightBor = createCellStyleForNumberTwoDecimalBorder(workbook, fontNormal);
		CellStyle txtCenter = createCellStyleForTextCenter(workbook, fontNormal, true);
		UserProfile profile = (UserProfile)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String date = formateDateEN.format(new Date());
		String time = formateHH.format(new Date());
		
		BigDecimal vat7 = BigDecimal.ZERO;
		BigDecimal vat72 = BigDecimal.ZERO;
		BigDecimal vat73 = BigDecimal.ZERO;
		
		BigDecimal vat0 = BigDecimal.ZERO;
		BigDecimal vat02 = BigDecimal.ZERO;
		BigDecimal vat03 = BigDecimal.ZERO;
		
		BigDecimal vat10 = BigDecimal.ZERO;
		BigDecimal vat102 = BigDecimal.ZERO;
		BigDecimal vat103 = BigDecimal.ZERO;
		
		BigDecimal vat8 = BigDecimal.ZERO;
		BigDecimal vat82 = BigDecimal.ZERO;
		BigDecimal vat83 = BigDecimal.ZERO;
		
		int rowNumHead = 1;

		Sheet sh = workbook.getSheetAt(0);

		Footer footer = sh.getFooter();
		footer.setCenter(sh.getFooter().getCenter());
		
		Row rowHead = sh.createRow(rowNumHead++);
		Cell cellH1 = rowHead.createCell(0);
		cellH1.setCellValue(("ระหว่างวันที่  "+" ").concat((formatter2.format(formatter3.parse(bean.getDateFrom())).concat(" "+bean.getDateFromHour()).concat(":"+bean.getDateFromMinute()))).concat(" ถึงวันที่".concat(" ").concat(""+formatter2.format(formatter3.parse(bean.getDateTo()))).concat(" "+bean.getDateToHour()).concat(":"+bean.getDateToMinute())));
		cellH1.setCellStyle(txtCentertNoBor);
		rowNumHead++;
		Row rowHead2 = sh.createRow(rowNumHead++);
		Cell cellH22 = rowHead2.createCell(11);
		cellH22.setCellValue("วันเวลาพิมพ์  : ".concat(date+" "+time));
		cellH22.setCellStyle(txtRightNoBor);
		
		int rowNum = 7;

		if (CollectionUtils.isNotEmpty(entity)) {
			for(int i=0; i<entity.size(); i++) {
				
				if(Constants.Status.ACTIVE.equals(entity.get(i).getRecordStatus())) {
					if(7 == entity.get(i).getVatRate()) {
						vat7 = vat7.add(entity.get(i).getBeforeVat()==null?BigDecimal.ZERO:entity.get(i).getBeforeVat());
						vat72 = vat72.add(entity.get(i).getVat()==null?BigDecimal.ZERO:entity.get(i).getVat());
						vat73 = vat73.add(entity.get(i).getAmount()==null?BigDecimal.ZERO:entity.get(i).getAmount());
					}else if(0 == entity.get(i).getVatRate()) {
						vat0 = vat0.add(entity.get(i).getBeforeVat()==null?BigDecimal.ZERO:entity.get(i).getBeforeVat());
						vat02 = vat02.add(entity.get(i).getVat()==null?BigDecimal.ZERO:entity.get(i).getVat());
						vat03 = vat03.add(entity.get(i).getAmount()==null?BigDecimal.ZERO:entity.get(i).getAmount());
					}else if(10 == entity.get(i).getVatRate()) {
						vat10 = vat10.add(entity.get(i).getBeforeVat()==null?BigDecimal.ZERO:entity.get(i).getBeforeVat());
						vat102 = vat102.add(entity.get(i).getVat()==null?BigDecimal.ZERO:entity.get(i).getVat());
						vat103 = vat103.add(entity.get(i).getAmount()==null?BigDecimal.ZERO:entity.get(i).getAmount());
					}else if(8 == entity.get(i).getVatRate()) {
						vat8 = vat8.add(entity.get(i).getBeforeVat()==null?BigDecimal.ZERO:entity.get(i).getBeforeVat());
						vat82 = vat82.add(entity.get(i).getVat()==null?BigDecimal.ZERO:entity.get(i).getVat());
						vat83 = vat83.add(entity.get(i).getAmount()==null?BigDecimal.ZERO:entity.get(i).getAmount());
					}
				}
		
				Row row1 = sh.createRow(rowNum++);
				Cell cell1 = row1.createCell(0);
				Cell cell2 = row1.createCell(1);
				Cell cell3 = row1.createCell(2);
				Cell cell4 = row1.createCell(3);
				Cell cell5 = row1.createCell(4);
				Cell cell6 = row1.createCell(5);
				Cell cell7 = row1.createCell(6);
				Cell cell8 = row1.createCell(7);
				Cell cell9 = row1.createCell(8);
				Cell cell10 = row1.createCell(9);
				Cell cell11 = row1.createCell(10);
				Cell cell12 = row1.createCell(11);
				Cell cell13 = row1.createCell(12);
				Cell cell14 = row1.createCell(13);
				
				cell1.setCellValue(i+1);
				cell2.setCellValue(formateDateEN.format(entity.get(i).getDocumentDate()).toString());
				cell3.setCellValue(entity.get(i).getDocumentNo());
				cell4.setCellValue(entity.get(i).getCustName());
				cell5.setCellValue(entity.get(i).getTaxId());
				cell6.setCellValue(entity.get(i).getBranCode().equals("00000") ? "สำนักงานใหญ่" : entity.get(i).getBranCode());
				cell7.setCellValue(new Double((entity.get(i).getAmount()==null?BigDecimal.ZERO:entity.get(i).getAmount().subtract(entity.get(i).getVatAmount()==null?BigDecimal.ZERO:entity.get(i).getVatAmount())).toString()));
				cell8.setCellValue(new Double((entity.get(i).getVatAmount()==null?BigDecimal.ZERO:entity.get(i).getVatAmount()).toString()));
				cell9.setCellValue(new Double((entity.get(i).getAmount()==null?BigDecimal.ZERO:entity.get(i).getAmount()).toString()));
				cell10.setCellValue(Constants.Status.ACTIVE.equals(entity.get(i).getRecordStatus())?Constants.Status.ACTIVE_:Constants.Status.ACTIVE_C);
				cell11.setCellValue(profile.getBranchArea());
				cell12.setCellValue(masterDatasDao.getNTSHOPNAME().getValue());
				cell13.setCellValue(entity.get(i).getCreateBy());
				
				// gen name surname
				 try {
					 UserBean userBean =  masterDataService.findByUsername(entity.get(i).getCreateBy());
					 cell14.setCellValue(userBean.getSurName().concat(" ".concat(userBean.getLastName())));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				cell1.setCellStyle(txtCenter);
				cell2.setCellStyle(txtLeft);
				cell3.setCellStyle(txtLeft);
				cell4.setCellStyle(txtLeft);
				cell5.setCellStyle(txtLeft);
				cell6.setCellStyle(txtLeft);
				cell7.setCellStyle(numRightBor);
				cell8.setCellStyle(numRightBor);
				cell9.setCellStyle(numRightBor);
				cell10.setCellStyle(txtCenter);
				cell11.setCellStyle(txtCenter);
				cell12.setCellStyle(txtCenter);
				cell13.setCellStyle(txtCenter);
				cell14.setCellStyle(txtCenter);
				
			}
		}
		return workbook;
	}
	
	public Workbook getReportNotFull(Workbook workbook, List<HistoryPaymentRS> entity, HistoryReportBean bean, List<ReportTaxRSBean> rsBeans) throws Exception{
		SimpleDateFormat formatter3 = new SimpleDateFormat(Constants.DateTime.DB_DATE_FORMAT, new Locale("th", "TH"));
		Font fontNormal = createFontTHSarabanPSK(workbook, 14, false);
		Font fontBold = createFontTHSarabanPSK(workbook, 14, true);
		CellStyle txtCentertNoBor = createStyleCellCenter(workbook, fontBold, false);
		CellStyle txtRightNoBor = createStyleCellRight(workbook, fontBold, false);
		CellStyle txtLeft = createCellStyleForTextLeft(workbook, fontNormal, true);
		CellStyle numRightBor = createCellStyleForNumberTwoDecimalBorder(workbook, fontNormal);
		CellStyle txtCenter = createCellStyleForTextCenter(workbook, fontNormal, true);
		String date = formateDateEN.format(new Date());
		String time = formateHH.format(new Date());
		
		int rowNumHead = 1;
		
		Sheet sh = workbook.getSheetAt(0);
		
		Footer footer = sh.getFooter();
		footer.setCenter(sh.getFooter().getCenter());
		
		Row rowHead = sh.createRow(rowNumHead++);
		Cell cellH1 = rowHead.createCell(0);
		cellH1.setCellValue(("ระหว่างวันที่  "+" ").concat((formatter2.format(formatter3.parse(bean.getDateFrom())).concat(" "+bean.getDateFromHour()).concat(":"+bean.getDateFromMinute()))).concat(" ถึงวันที่".concat(" ").concat(""+formatter2.format(formatter3.parse(bean.getDateTo()))).concat(" "+bean.getDateToHour()).concat(":"+bean.getDateToMinute())));
		cellH1.setCellStyle(txtCentertNoBor);
		rowNumHead++;
		Row rowHead2 = sh.createRow(rowNumHead++);
		Cell cellH22 = rowHead2.createCell(11);
		cellH22.setCellValue("วันเวลาพิมพ์  : ".concat(date+" "+time));
		cellH22.setCellStyle(txtRightNoBor);
		
		int rowNum = 7;
		
		if (CollectionUtils.isNotEmpty(rsBeans)) {
			for(int i=0; i<rsBeans.size(); i++) {
				
				Row row1 = sh.createRow(rowNum++);
				Cell cell1 = row1.createCell(0);
				Cell cell2 = row1.createCell(1);
				Cell cell3 = row1.createCell(2);
				Cell cell4 = row1.createCell(3);
				Cell cell5 = row1.createCell(4);
				Cell cell6 = row1.createCell(5);
				Cell cell7 = row1.createCell(6);
				Cell cell8 = row1.createCell(7);
				Cell cell9 = row1.createCell(8);
				Cell cell10 = row1.createCell(9);
				Cell cell11 = row1.createCell(10);
				Cell cell12 = row1.createCell(11);
				Cell cell13 = row1.createCell(12);
				Cell cell14 = row1.createCell(13);
				
				cell1.setCellValue(i+1);
				cell2.setCellValue(formateDateEN.format(entity.get(i).getDocumentDate()).toString());
				cell3.setCellValue(masterDatasDao.getNTBUPLACE().getValue());
				cell4.setCellValue(masterDatasDao.getBRANCHAREA().getValue());
				cell5.setCellValue(masterDatasDao.getNTBUPLACE().getValue());
				cell6.setCellValue(masterDatasDao.getNTSHOPNAME().getValue());
				cell7.setCellValue(rsBeans.get(i).getUserName());
				
				UserBean user = masterDataService.findByUsername(rsBeans.get(i).getUserName());
				cell8.setCellValue(user.getSurName().concat(" ").concat(user.getLastName()));
				cell9.setCellValue(rsBeans.get(i).getQuantity());
				cell10.setCellValue(rsBeans.get(i).getFrom());
				cell11.setCellValue(rsBeans.get(i).getTo());
				cell12.setCellValue(new Double((rsBeans.get(i).getBeforeVat()==null?BigDecimal.ZERO:rsBeans.get(i).getBeforeVat()).toString()));
				cell13.setCellValue(new Double((rsBeans.get(i).getVat()==null?BigDecimal.ZERO:rsBeans.get(i).getVat()).toString()));
				cell14.setCellValue(new Double((rsBeans.get(i).getPaidAmount()==null?BigDecimal.ZERO:rsBeans.get(i).getPaidAmount()).toString()));
				
				cell1.setCellStyle(txtCenter);
				cell2.setCellStyle(txtLeft);
				cell3.setCellStyle(txtLeft);
				cell4.setCellStyle(txtLeft);
				cell5.setCellStyle(txtLeft);
				cell6.setCellStyle(txtLeft);
				cell7.setCellStyle(txtLeft);
				cell8.setCellStyle(txtLeft);
				cell9.setCellStyle(numRightBor);
				cell10.setCellStyle(txtLeft);
				cell11.setCellStyle(txtLeft);
				cell12.setCellStyle(numRightBor);
				cell13.setCellStyle(numRightBor);
				cell14.setCellStyle(numRightBor);
				
			}
		}
		return workbook;
	}

}