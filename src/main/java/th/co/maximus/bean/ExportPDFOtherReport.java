package th.co.maximus.bean;

import java.math.BigDecimal;
import java.util.Date;

public class ExportPDFOtherReport {
	private String branArea;
	private String bracnCode;
	private String souce;
	private String custNo;
	private String custName;
	private String documentNo;
	private BigDecimal balanceSummary;
	private String invoiceNo;
	private Date documentDate;
	private String customerAddress;
	private String taxId;
	private String remark;
	private String paymentCode;
	private BigDecimal beforeVat;
	private BigDecimal vat;
	private String dateDocument;
	private BigDecimal discountSpecial;
	private BigDecimal balanceBefore;
	private String serviceName;
	private BigDecimal amount;
	private BigDecimal discountbeforvat;
	
	
	private String discountSpecialStr;
	private String balanceBeforeStr;
	private String beforeVatStr;
	private String vatStr;
	private String balanceSummaryStr;
	
	private String addressCheck;
	private String custNameCheck;
	private String taxIdCheck;
	private String balanceBeforeCheck;
	private String discountSpecialCheck;
	private String checkBran;
	private String checkSouce;
	private String vatRate;
	private String vatRateCheck;
	
	private String surName;
	private String lastname;
	
	private String sentStringHeader;
	private String vatSum;
	private String custNoCheck;
	
	private String title;
	
	private String addressLength;
	private String custNameLength;
	
	
	public String getCustNameLength() {
		return custNameLength;
	}
	public void setCustNameLength(String custNameLength) {
		this.custNameLength = custNameLength;
	}
	public String getAddressLength() {
		return addressLength;
	}
	public void setAddressLength(String addressLength) {
		this.addressLength = addressLength;
	}
	public String getVatSum() {
		return vatSum;
	}
	public void setVatSum(String vatSum) {
		this.vatSum = vatSum;
	}
	public String getVatRateCheck() {
		return vatRateCheck;
	}
	public void setVatRateCheck(String vatRateCheck) {
		this.vatRateCheck = vatRateCheck;
	}
	public String getVatRate() {
		return vatRate;
	}
	public void setVatRate(String vatRate) {
		this.vatRate = vatRate;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getBranArea() {
		return branArea;
	}
	public void setBranArea(String branArea) {
		this.branArea = branArea;
	}
	public String getBracnCode() {
		return bracnCode;
	}
	public void setBracnCode(String bracnCode) {
		this.bracnCode = bracnCode;
	}
	public String getSouce() {
		return souce;
	}
	public void setSouce(String souce) {
		this.souce = souce;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getDocumentNo() {
		return documentNo;
	}
	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}
	public BigDecimal getBalanceSummary() {
		return balanceSummary;
	}
	public void setBalanceSummary(BigDecimal balanceSummary) {
		this.balanceSummary = balanceSummary;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public Date getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	public String getTaxId() {
		return taxId;
	}
	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPaymentCode() {
		return paymentCode;
	}
	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}
	public BigDecimal getBeforeVat() {
		return beforeVat;
	}
	public void setBeforeVat(BigDecimal beforeVat) {
		this.beforeVat = beforeVat;
	}
	public BigDecimal getVat() {
		return vat;
	}
	public void setVat(BigDecimal vat) {
		this.vat = vat;
	}
	public String getDateDocument() {
		return dateDocument;
	}
	public void setDateDocument(String dateDocument) {
		this.dateDocument = dateDocument;
	}
	public BigDecimal getDiscountSpecial() {
		return discountSpecial;
	}
	public void setDiscountSpecial(BigDecimal discountSpecial) {
		this.discountSpecial = discountSpecial;
	}
	public BigDecimal getBalanceBefore() {
		return balanceBefore;
	}
	public void setBalanceBefore(BigDecimal balanceBefore) {
		this.balanceBefore = balanceBefore;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getDiscountbeforvat() {
		return discountbeforvat;
	}
	public void setDiscountbeforvat(BigDecimal discountbeforvat) {
		this.discountbeforvat = discountbeforvat;
	}
	public String getDiscountSpecialStr() {
		return discountSpecialStr;
	}
	public void setDiscountSpecialStr(String discountSpecialStr) {
		this.discountSpecialStr = discountSpecialStr;
	}
	public String getBalanceBeforeStr() {
		return balanceBeforeStr;
	}
	public void setBalanceBeforeStr(String balanceBeforeStr) {
		this.balanceBeforeStr = balanceBeforeStr;
	}
	public String getBeforeVatStr() {
		return beforeVatStr;
	}
	public void setBeforeVatStr(String beforeVatStr) {
		this.beforeVatStr = beforeVatStr;
	}
	public String getVatStr() {
		return vatStr;
	}
	public void setVatStr(String vatStr) {
		this.vatStr = vatStr;
	}
	public String getBalanceSummaryStr() {
		return balanceSummaryStr;
	}
	public void setBalanceSummaryStr(String balanceSummaryStr) {
		this.balanceSummaryStr = balanceSummaryStr;
	}
	public String getAddressCheck() {
		return addressCheck;
	}
	public void setAddressCheck(String addressCheck) {
		this.addressCheck = addressCheck;
	}
	public String getCustNameCheck() {
		return custNameCheck;
	}
	public void setCustNameCheck(String custNameCheck) {
		this.custNameCheck = custNameCheck;
	}
	public String getTaxIdCheck() {
		return taxIdCheck;
	}
	public void setTaxIdCheck(String taxIdCheck) {
		this.taxIdCheck = taxIdCheck;
	}
	public String getBalanceBeforeCheck() {
		return balanceBeforeCheck;
	}
	public void setBalanceBeforeCheck(String balanceBeforeCheck) {
		this.balanceBeforeCheck = balanceBeforeCheck;
	}
	public String getDiscountSpecialCheck() {
		return discountSpecialCheck;
	}
	public void setDiscountSpecialCheck(String discountSpecialCheck) {
		this.discountSpecialCheck = discountSpecialCheck;
	}
	public String getCheckBran() {
		return checkBran;
	}
	public void setCheckBran(String checkBran) {
		this.checkBran = checkBran;
	}
	public String getSurName() {
		return surName;
	}
	public void setSurName(String surName) {
		this.surName = surName;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getSentStringHeader() {
		return sentStringHeader;
	}
	public void setSentStringHeader(String sentStringHeader) {
		this.sentStringHeader = sentStringHeader;
	}
	public String getCheckSouce() {
		return checkSouce;
	}
	public void setCheckSouce(String checkSouce) {
		this.checkSouce = checkSouce;
	}
	public String getCustNoCheck() {
		return custNoCheck;
	}
	public void setCustNoCheck(String custNoCheck) {
		this.custNoCheck = custNoCheck;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
	
}
