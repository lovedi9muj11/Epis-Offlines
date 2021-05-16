package th.co.maximus.payment.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class PaymentBillBean implements Serializable{
	private static final long serialVersionUID = 1L;

	private String inputServiceType;
	private String inputServiceName;
	private String inputServiceCode;
	private String inputServiceDepartment;
	private int inputServiceMoreData;
	private BigDecimal inputServiceAmount;
	private BigDecimal inputServiceDiscount;
	private BigDecimal vatSale;
	private BigDecimal inputSpecialDiscount;
	private BigDecimal summarySale;
	private BigDecimal summaryinvoice;
	private String inputServiceName2;
	
	
	public String getInputServiceName2() {
		return inputServiceName2;
	}
	public void setInputServiceName2(String inputServiceName2) {
		this.inputServiceName2 = inputServiceName2;
	}
	public BigDecimal getSummaryinvoice() {
		return summaryinvoice;
	}
	public void setSummaryinvoice(BigDecimal summaryinvoice) {
		this.summaryinvoice = summaryinvoice;
	}
	public String getInputServiceCode() {
		return inputServiceCode;
	}
	public void setInputServiceCode(String inputServiceCode) {
		this.inputServiceCode = inputServiceCode;
	}
	public String getInputServiceType() {
		return inputServiceType;
	}
	public void setInputServiceType(String inputServiceType) {
		this.inputServiceType = inputServiceType;
	}
	public String getInputServiceName() {
		return inputServiceName;
	}
	public void setInputServiceName(String inputServiceName) {
		this.inputServiceName = inputServiceName;
	}
	public String getInputServiceDepartment() {
		return inputServiceDepartment;
	}
	public void setInputServiceDepartment(String inputServiceDepartment) {
		this.inputServiceDepartment = inputServiceDepartment;
	}
	public int getInputServiceMoreData() {
		return inputServiceMoreData;
	}
	public void setInputServiceMoreData(int inputServiceMoreData) {
		this.inputServiceMoreData = inputServiceMoreData;
	}
	public BigDecimal getInputServiceAmount() {
		return inputServiceAmount;
	}
	public void setInputServiceAmount(BigDecimal inputServiceAmount) {
		this.inputServiceAmount = inputServiceAmount;
	}
	public BigDecimal getInputServiceDiscount() {
		return inputServiceDiscount;
	}
	public void setInputServiceDiscount(BigDecimal inputServiceDiscount) {
		this.inputServiceDiscount = inputServiceDiscount;
	}
	public BigDecimal getVatSale() {
		return vatSale;
	}
	public void setVatSale(BigDecimal vatSale) {
		this.vatSale = vatSale;
	}
	public BigDecimal getInputSpecialDiscount() {
		return inputSpecialDiscount;
	}
	public void setInputSpecialDiscount(BigDecimal inputSpecialDiscount) {
		this.inputSpecialDiscount = inputSpecialDiscount;
	}
	public BigDecimal getSummarySale() {
		return summarySale;
	}
	public void setSummarySale(BigDecimal summarySale) {
		this.summarySale = summarySale;
	}
	
	
}
