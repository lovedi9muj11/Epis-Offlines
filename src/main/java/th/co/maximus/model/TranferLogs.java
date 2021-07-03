package th.co.maximus.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TRANFER_LOGS")
public class TranferLogs {
	private Long id;
	@Column(name="START_DATE")
	private  Timestamp   startdate;
	@Column(name="END_DATE")
	private  Timestamp   enddate;
	@Column(name="SUCCESS_TASK")
	private  Integer   successTask;
	@Column(name="ERROR_TASK")
	private  Integer   errorTask;
	@Column(name="END_DATE")
	private  String   system;
	
	@Column(name="ERROR_RECRIPT")
	private  String   errorRecript;
	@Column(name="STATUS")
	private  String   status;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Timestamp getStartDate() {
		return startdate;
	}
	public void setStartDate(Timestamp startDate) {
		this.startdate = startDate;
	}
	public Timestamp getEndDate() {
		return enddate;
	}
	public void setEndDate(Timestamp endDate) {
		this.enddate = endDate;
	}
	public Integer getSuccessTask() {
		return successTask;
	}
	public void setSuccessTask(Integer successTask) {
		this.successTask = successTask;
	}
	public Integer getErrorTask() {
		return errorTask;
	}
	public void setErrorTask(Integer errorTask) {
		this.errorTask = errorTask;
	}

	public String getSystem() {
		return system;
	}
	public void setSystem(String system) {
		this.system = system;
	}
	public String getErrorRecript() {
		return errorRecript;
	}
	public void setErrorRecript(String errorRecript) {
		this.errorRecript = errorRecript;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
