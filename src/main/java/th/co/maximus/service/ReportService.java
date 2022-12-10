package th.co.maximus.service;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import th.co.maximus.bean.HistoryReportBean;
import th.co.maximus.bean.InvEpisOfflineOtherBean;
import th.co.maximus.bean.InvEpisOfflineReportBean;
import th.co.maximus.bean.InvPaymentOrderTaxBean;

public interface ReportService {
	
	public List<InvEpisOfflineReportBean> inqueryEpisOfflineJSONHandler(String documentNo)throws SQLException;
	
	public List<InvEpisOfflineOtherBean> inqueryEpisOfflineOtherJSONHandler(String documentNo)throws SQLException;
	
	public List<InvPaymentOrderTaxBean> inqueryInvPaymentOrderTaxBeanJSONHandler(HistoryReportBean creteria)throws SQLException;
	
	public List<InvPaymentOrderTaxBean> vatSummarry(HistoryReportBean creteria ,boolean groupBy)throws SQLException;

	List<InvEpisOfflineOtherBean> inqueryEpisOfflineOtherJSONHandler(String documentNo, String other) throws SQLException;
	
	String isPage(HttpServletRequest request, HttpServletResponse response, List<InvPaymentOrderTaxBean> collections, final String JASPER_JRXML_FILENAME, HistoryReportBean creteria) throws Exception;
}
