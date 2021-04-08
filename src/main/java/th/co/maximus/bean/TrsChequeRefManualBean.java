package th.co.maximus.bean;

import java.sql.Timestamp;
import java.util.Date;

public class TrsChequeRefManualBean {
	private  Long   Id;
	private  String   chequeNo;
	private  String   publisherId	;
	private  String  publisher;
	private  String  branch;
	private  double   aMount;
	private  Timestamp   updateDttm;
	private  String   updateSystem;
	private  String   updateUser;
	private  Long   versionStamp;
	private  Timestamp   chequeDate;
	private  Timestamp   bounceChequeDate;
	private  Timestamp   reverseArDate;
	private  String   bounceStatus;
	private  Long   methodManualId;
	private Timestamp cheDate;
	private String createBy;
	private Date createDate;
	private String updateBy;
	private Date updateDate;
	
	
	public Long getId() {
		return Id;
	}
	public void setId(Long id) {
		Id = id;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public String getPublisherId() {
		return publisherId;
	}
	public void setPublisherId(String publisherId) {
		this.publisherId = publisherId;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}

	public double getaMount() {
		return aMount;
	}
	public void setaMount(double aMount) {
		this.aMount = aMount;
	}
	public Timestamp getUpdateDttm() {
		return updateDttm;
	}
	public void setUpdateDttm(Timestamp updateDttm) {
		this.updateDttm = updateDttm;
	}
	public String getUpdateSystem() {
		return updateSystem;
	}
	public void setUpdateSystem(String updateSystem) {
		this.updateSystem = updateSystem;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Long getVersionStamp() {
		return versionStamp;
	}
	public void setVersionStamp(Long versionStamp) {
		this.versionStamp = versionStamp;
	}
	public Timestamp getChequeDate() {
		return chequeDate;
	}
	public void setChequeDate(Timestamp chequeDate) {
		this.chequeDate = chequeDate;
	}
	public Timestamp getBounceChequeDate() {
		return bounceChequeDate;
	}
	public void setBounceChequeDate(Timestamp bounceChequeDate) {
		this.bounceChequeDate = bounceChequeDate;
	}
	public Timestamp getReverseArDate() {
		return reverseArDate;
	}
	public void setReverseArDate(Timestamp reverseArDate) {
		this.reverseArDate = reverseArDate;
	}
	public String getBounceStatus() {
		return bounceStatus;
	}
	public void setBounceStatus(String bounceStatus) {
		this.bounceStatus = bounceStatus;
	}
	public Long getMethodManualId() {
		return methodManualId;
	}
	public void setMethodManualId(Long methodManualId) {
		this.methodManualId = methodManualId;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public Timestamp getCheDate() {
		return cheDate;
	}
	public void setCheDate(Timestamp cheDate) {
		this.cheDate = cheDate;
	}

	
	
}
