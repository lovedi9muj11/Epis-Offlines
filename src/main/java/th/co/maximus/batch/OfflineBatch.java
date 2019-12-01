package th.co.maximus.batch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import th.co.maximus.auth.config.ConfigureQuartz;
import th.co.maximus.bean.MasterDataBean;
import th.co.maximus.bean.MasterDatasBean;
import th.co.maximus.bean.PaymentMMapPaymentInvBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.dao.MasterDataDao;
import th.co.maximus.dao.MasterDatasDao;
import th.co.maximus.model.OfflineResultModel;
import th.co.maximus.model.PaymentDTO;
import th.co.maximus.service.CallEpisOnlineService;
import th.co.maximus.service.CancelPaymentService;
import th.co.maximus.service.ClearingPaymentEpisOfflineService;
import th.co.maximus.util.GetMacAddress;

@Component
@DisallowConcurrentExecution
public class OfflineBatch implements Job {

	@Autowired
	private MasterDatasDao masterDatasDao;

	@Autowired
	CallEpisOnlineService callEpisOnlineService;

	@Autowired
	private CancelPaymentService cancelPaymentService;

	@Autowired
	private ClearingPaymentEpisOfflineService clearingPaymentEpisOfflineService;
    @Autowired private MasterDataDao masterDataDao;

	@Value("${url.online}")
	private String url;
	
	private  String posNo;
	
	private  String branCode;

	RestTemplate restTemplate;

	public OfflineBatch() {
		restTemplate = new RestTemplate();
	}
	public void init() {
		 List<MasterDataBean> resultList = masterDataDao.findAllByGropType(Constants.INIT_PROJECT);
        for (MasterDataBean masterDataBean : resultList) {
			if(masterDataBean.getValue().equals("POS")) {
				posNo = masterDataBean.getText();
			}
			if(masterDataBean.getValue().equals("BRANCH_CODE")) {
				branCode = masterDataBean.getText();
			}
		}
	}
	private static final Logger log = Logger.getLogger(OfflineBatch.class.getName());

	SimpleDateFormat format = new SimpleDateFormat(
			Constants.DateTime.DATE_FORMAT.concat(" " + Constants.DateTime.DB_TIME_FORMAT), Constants.localeEN);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			if (Constants.BATCH.JOB_1.equals(context.getTrigger().getKey().getName())) {
				System.out.println("JOB_1");
				 callEpisOnlineService.callOnlineSyncMasterData();
			} else if (Constants.BATCH.JOB_2.equals(context.getTrigger().getKey().getName())) {
				System.out.println("JOB_2");
				 callEpisOnlineService.callOnlineSyncMapGL();
			} else if (Constants.BATCH.JOB_3.equals(context.getTrigger().getKey().getName())) {
				System.out.println("JOB_3");
				 callEpisOnlineService.callOnlineSyncUser();
			} else if (Constants.BATCH.JOB_4.equals(context.getTrigger().getKey().getName())) {
				System.out.println("JOB_4");
				saveBatch();
			} else if (Constants.BATCH.JOB_5.equals(context.getTrigger().getKey().getName())) {
				System.out.println("JOB_5");
				clearingPaymentEpisOfflineService.clearingCencelPayment();
			}
		} catch (Exception e) {
			log.error("Encountered job execution exception!", e);
		}
	}

	@Bean(name = "jobWithSimpleTriggerBean")
	public JobDetailFactoryBean sampleJob() {
		return ConfigureQuartz.createJobDetail(this.getClass());
	}

	@Bean(name = "jobWithSimpleTriggerBeanTrigger")
	public CronTriggerFactoryBean sampleJobTrigger(@Qualifier("jobWithSimpleTriggerBean") JobDetail jobDetail)
			throws Exception {
		MasterDatasBean masterDatas = masterDatasDao.findByGrop(Constants.MasterData.TRIGGER_GOUP,
				Constants.MasterData.KEYCODE.TRIGGER_MS);
		return ConfigureQuartz.createCronTrigger(jobDetail, masterDatas.getValue());
	}

	@Bean(name = "jobWithSimpleTriggerBeanTrigger2")
	public CronTriggerFactoryBean sampleJobTrigger2(@Qualifier("jobWithSimpleTriggerBean") JobDetail jobDetail)
			throws Exception {
		MasterDatasBean masterDatas = masterDatasDao.findByGrop(Constants.MasterData.TRIGGER_GOUP,
				Constants.MasterData.KEYCODE.TRIGGER_GL);
		return ConfigureQuartz.createCronTrigger(jobDetail, masterDatas.getValue());
	}

	@Bean(name = "jobWithSimpleTriggerBeanTrigger3")
	public CronTriggerFactoryBean sampleJobTrigger3(@Qualifier("jobWithSimpleTriggerBean") JobDetail jobDetail)
			throws Exception {
		MasterDatasBean masterDatas = masterDatasDao.findByGrop(Constants.MasterData.TRIGGER_GOUP,
				Constants.MasterData.KEYCODE.TRIGGER_USER);
		return ConfigureQuartz.createCronTrigger(jobDetail, masterDatas.getValue());
	}

	@Bean(name = "jobWithSimpleTriggerBeanTrigger4")
	public CronTriggerFactoryBean sampleJobTrigger4(@Qualifier("jobWithSimpleTriggerBean") JobDetail jobDetail)
			throws Exception {
		MasterDatasBean masterDatas = masterDatasDao.findByGrop(Constants.MasterData.TRIGGER_GOUP,
				Constants.MasterData.KEYCODE.TRIGGER_MINUS);
		return ConfigureQuartz.createCronTrigger(jobDetail, masterDatas.getValue());
	}

	@Bean(name = "jobWithSimpleTriggerBeanTrigger5")
	public CronTriggerFactoryBean sampleJobTrigger5(@Qualifier("jobWithSimpleTriggerBean") JobDetail jobDetail)
			throws Exception {
		MasterDatasBean masterDatas = masterDatasDao.findByGrop(Constants.MasterData.TRIGGER_GOUP,
				Constants.MasterData.KEYCODE.TRIGGER_CLEARING);
		return ConfigureQuartz.createCronTrigger(jobDetail, masterDatas.getValue());
	}

	public void saveBatch() throws Exception {
		List<PaymentMMapPaymentInvBean> result = new ArrayList<>();
		init();
		String mac = GetMacAddress.getMACAddress();
		result = cancelPaymentService.findAllCancelPaymentsActive(Constants.USER.LOGIN_FLAG_N);
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
		if (result != null) {
			
			List<OfflineResultModel> resultClear = clearingPaymentEpisOfflineService.callOnlinePayment(result);
			for (OfflineResultModel offlineResultModel : resultClear) {
				try {
					if (offlineResultModel.getStatus().equals("SUCCESS")) {

						for (PaymentMMapPaymentInvBean payment : result) {
							if (offlineResultModel.getManualId() == payment
									.getManualId()) {
								List<PaymentDTO> dtoList = new ArrayList<>();
								PaymentDTO manualDTO = new PaymentDTO();

								manualDTO.setAccountNo(payment.getAccountNo());
								manualDTO.setBranchCode(branCode);
								manualDTO.setBranchArea(payment.getBrancharea());
								manualDTO.setInvoiceNo(payment.getInvoiceNo());
								manualDTO.setReceiptNoManual(payment
										.getReceiptNoManual());
								manualDTO.setRemark(payment.getRemark());
								manualDTO.setManualId(offlineResultModel
										.getManualIdOnline());
								manualDTO.setCreateBy(payment.getCreateBy());
								manualDTO.setPaidAmount(payment.getPaidAmount());
								manualDTO.setRecordStatus("");
								manualDTO.setSource(payment.getSource());
								manualDTO.setVatAmount(payment.getVatAmount());
								manualDTO.setUserLogin(payment.getCreateBy());
								manualDTO.setUserName(payment.getCreateBy());
								manualDTO.setPaidDate(payment.getPaidDate());
								manualDTO.setPaidDateStr(dateFormat.format(payment.getPaidDate()));
								manualDTO.setPosNo(posNo);
								manualDTO.setBranchAreaCode(payment.getBranchAreaCode());
								dtoList.add(manualDTO);
								if (dtoList.size() > 0) {
									
									String postUrl = url.concat("/paymentManualServiceOnline.json?ap=QUEUE&un="+ payment.getCreateBy()+"&mac="+mac);
									restTemplate.postForEntity(postUrl, dtoList,String.class);
//									for (OfflineResultModel payment : resultClear) {
										if ("SUCCESS".equals(offlineResultModel.getStatus())) {
											clearingPaymentEpisOfflineService.updateStatusClearing(payment.getManualId(),
													Constants.CLEARING.STATUS_Y);
										} else {
											clearingPaymentEpisOfflineService.updateStatusClearing(payment.getManualId(),
													Constants.CLEARING.STATUS_ERROR);
										}
//									}

								}
							}
						}
						

					} 
				} catch (Exception e) {
					// e.printStackTrace();
					System.err.println("SEND DATA EEROR :"
							+ offlineResultModel.getManualId());
					System.err.println("SEND DATA EEROR :"
							+ offlineResultModel.getRecriptNo());
					offlineResultModel.setStatus("ERROR");
				}
			}	
		}

	}

}
