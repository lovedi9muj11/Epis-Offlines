package th.co.maximus.service.impl;

import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import th.co.maximus.auth.model.GroupTypeDropdown;
import th.co.maximus.bean.DropDownBean;
import th.co.maximus.bean.MapGLBean;
import th.co.maximus.bean.MasterDataBean;
import th.co.maximus.bean.MasterDataSyncBean;
import th.co.maximus.bean.MasterDatasBean;
import th.co.maximus.constants.Constants;
import th.co.maximus.dao.MapGLDao;
import th.co.maximus.dao.MasterDataDao;
import th.co.maximus.dao.MasterDatasDao;
import th.co.maximus.dao.UserDao;
import th.co.maximus.model.UserBean;
import th.co.maximus.service.MasterDataService;

@Service
public class MasterDataServiceImpl implements MasterDataService {

	@Autowired
	private MasterDataDao masterDataDao;

	@Autowired
	private MasterDatasDao masterDatasDao;

	@Autowired
	MasterDataService masterDataService;

	@Autowired
	private UserDao userDao;

	@Autowired
	private MapGLDao mapGLDao;

	List<GroupTypeDropdown> groupTypeDropdownList;
	GroupTypeDropdown groupTypeDropdown;

	@Override
	public List<MasterDataBean> findAllByBankCode() {
		List<MasterDataBean> masterDataList = masterDataDao.findAllByBankCode();

		return masterDataList;
	}

	@Override
	public List<MasterDataBean> findAllByBankName() {
		List<MasterDataBean> masterDataList = masterDataDao.findAllByBankName();
		return masterDataList;
	}

	@Override
	public List<GroupTypeDropdown> findAll() {
		groupTypeDropdownList = new ArrayList<GroupTypeDropdown>();
		groupTypeDropdown = new GroupTypeDropdown();

		List<MasterDataBean> list = masterDataDao.findAll();

		if (CollectionUtils.isNotEmpty(list)) {
			groupTypeDropdown.setName(Constants.MasterData.SELECT_DROPDOWN);
			groupTypeDropdown.setValue("");
			groupTypeDropdownList.add(groupTypeDropdown);
			for (int i = 0; i < list.size(); i++) {
				groupTypeDropdown = new GroupTypeDropdown();
				groupTypeDropdown.setName(list.get(i).getText());
				groupTypeDropdown.setValue(list.get(i).getValue());
				groupTypeDropdownList.add(groupTypeDropdown);
			}
		}

		return groupTypeDropdownList;
	}

	@Override
	public int insert(MasterDataBean masterDataBean) {

		int masterId = 0;

		MasterDataBean bean = new MasterDataBean();
		bean.setValue(masterDataBean.getValue());
		bean.setText(masterDataBean.getText());
		bean.setGroup(masterDataBean.getGroup());

		try {

			masterId = masterDataDao.insertMasterdata(masterDataBean);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return masterId;
	}

	@Override
	public int insertGroup(MasterDataBean masterDataBean) {

		int masterId = 0;

		MasterDataBean bean = new MasterDataBean();
		bean.setValue(masterDataBean.getValue());
		bean.setText(masterDataBean.getValue());

		try {

			masterId = masterDataDao.insertMasterdataGroup(masterDataBean);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return masterId;
	}

	@Override
	public List<MasterDataBean> findAllByServiceType() {
		List<MasterDataBean> masterDataList = masterDataDao.findAllByServiceType();
		return masterDataList;
	}

	@Override
	public List<MasterDataBean> findAllByServiceDepartment() {
		List<MasterDataBean> masterDataList = masterDataDao.findAllByServiceDepartment();
		return masterDataList;
	}

	@Override
	public List<MasterDataBean> findAllByServiceName() {
		List<MasterDataBean> masterDataList = masterDataDao.findAllByServiceName();
		return masterDataList;
	}

	@Override
	public List<MasterDataBean> findAllByCategory() {
		List<MasterDataBean> masterDataList = masterDataDao.findAllByCategory();
		return masterDataList;
	}

	@Override
	public List<MasterDataBean> findByVat() {
		List<MasterDataBean> masterDataList = masterDataDao.findByVat();
		return masterDataList;
	}

	@Override
	public String insertMasterDataSync(List<MasterDataSyncBean> masterDataSyncBean) {
		String statusResult = "";
		try {
			statusResult = Constants.MasterData.STATUS_SUCCESS;
			masterDataDao.deleteBeforInsertMS();
			for (int i = 0; i < masterDataSyncBean.size(); i++) {
				masterDataDao.insertMasterDataSync(masterDataSyncBean.get(i));
			}
		} catch (Exception e) {
			statusResult = Constants.MasterData.STATUS_FAIL;
			e.printStackTrace();
		}
		return statusResult;
	}

	@Override
	public MasterDatasBean findByKeyCode(String keyCode) {
		// TODO Auto-generated method stub
		return masterDatasDao.findByKey(keyCode);
	}

	@Override
	public UserBean findByUsername(String username) throws SQLException {

		return userDao.findByUsername(username);
	}

	@Override
	public List<MasterDataBean> showAllMSNGL() {
		List<MasterDataBean> masterDataBeans = masterDataDao.showAllMSNGL();
		setMSNGL(masterDataBeans, mapGLDao.findAll(), mapGLDao.findBySource(Constants.MasterData.OTHER));
		return masterDataBeans;
	}

	void setMSNGL(List<MasterDataBean> masterDataBeans, List<MapGLBean> glBeans, List<MapGLBean> glBeans2) {
		MasterDataBean msBean = new MasterDataBean();
		for (MapGLBean beanGL : glBeans) {
			msBean = new MasterDataBean();
			msBean.setValue(beanGL.getRevenueTypeCode());
			msBean.setText(beanGL.getRevenueTypeName());
			masterDataBeans.add(msBean);
		}

		for (MapGLBean beanGL2 : glBeans2) {
			msBean = new MasterDataBean();
			msBean.setValue(beanGL2.getServiceCode());
			msBean.setText(beanGL2.getServiceName());
			masterDataBeans.add(msBean);
		}
	}

	@Override
	public List<GroupTypeDropdown> findBatch(String code) {
		groupTypeDropdownList = new ArrayList<GroupTypeDropdown>();
		groupTypeDropdown = new GroupTypeDropdown();

		List<MasterDataBean> list = masterDataDao.findBatch(code);

		if (CollectionUtils.isNotEmpty(list)) {
			groupTypeDropdown.setName(Constants.MasterData.SELECT_DROPDOWN);
			groupTypeDropdown.setValue("");
			groupTypeDropdownList.add(groupTypeDropdown);
			for (int i = 0; i < list.size(); i++) {
				groupTypeDropdown = new GroupTypeDropdown();
				groupTypeDropdown.setName(list.get(i).getOrderBatch());
				groupTypeDropdown.setValue(list.get(i).getValue());
				groupTypeDropdownList.add(groupTypeDropdown);
			}
		}

		return groupTypeDropdownList;
	}

	@SuppressWarnings("deprecation")
	@Override
	public MasterDataBean findGroupTypeByKeyCode(String groupKey) {
		List<DropDownBean> months = new ArrayList<DropDownBean>();
		MasterDataBean masterDataBean = new MasterDataBean();
		masterDataBean = masterDataDao.findGroupTypeByKeyCode(groupKey);
		Date dd = new Date();

		if (null != masterDataBean) {
			setTime(masterDataBean);

			monthDropdown(months);
		}
		masterDataBean.setDropDownMonths(months);

		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", new Locale("en", "EN"));

		try {
			if (!"*".equals(masterDataBean.getMonth()) && !"L".equals(masterDataBean.getMonth())) {
				dd = df.parse("1/" + masterDataBean.getMonth() + "/" + df.format(new Date().getYear()));

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dd);
				int lastDate = calendar.getActualMaximum(Calendar.DATE);

				// calendar.set(Calendar.DATE, lastDate);
				// int lastDay = calendar.get(Calendar.DAY_OF_WEEK);

				masterDataBean.setMonthNow(lastDate + "");
			}
			
			if("2".equals(masterDataBean.getFlagH())) {
				if("*".equals(masterDataBean.getHour()))masterDataBean.setHour("1");
				else if(!"0".equals(masterDataBean.getHour())) masterDataBean.setHour(masterDataBean.getHour().split("/")[1]);
			}
			
			if("2".equals(masterDataBean.getFlagM())) {
				if("*".equals(masterDataBean.getMinute()))masterDataBean.setMinute("1");
				else if("0".equals(masterDataBean.getMinute())) masterDataBean.setMinute(masterDataBean.getMinute().split("/")[1]);
			}
			
			if("*".equals(masterDataBean.getDay()) || "?".equals(masterDataBean.getDay())) {
				masterDataBean.setDay("*");
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}

		// set date local
		getLocalDateTime(masterDataBean);

		return masterDataBean;
	}

	public void setTime(MasterDataBean masterDataBean) {
		if (StringUtils.isNotBlank(masterDataBean.getText())) {
			String[] list = masterDataBean.getText().split(" ");
			
			if("L".equals(list[3])) {
				masterDataBean.setMonth(list[3]);
				masterDataBean.setDay("*");
			}else {
				masterDataBean.setMonth(list[4]);
				masterDataBean.setDay(list[3]);
			}
			
			masterDataBean.setHour(list[2]);
			masterDataBean.setMinute(list[1]);
		} else {
			masterDataBean.setMonth("");
			masterDataBean.setDay("");
			masterDataBean.setHour("");
			masterDataBean.setMinute("");
		}
	}

	public void monthDropdown(List<DropDownBean> months) {
		DropDownBean downBean = new DropDownBean();
		for (int i = 0; i < 12; i++) {
			downBean = new DropDownBean();
			String monthName = getMonthForInt(i);
			downBean.setKey((i + 1) + "");
			downBean.setValue(monthName);
			months.add(downBean);
		}
	}

	// month th
	String getMonthForInt(int num) {
		String month = "wrong";
		DateFormatSymbols dfs = new DateFormatSymbols(new Locale("th", "TH"));
		String[] months = dfs.getMonths();
		if (num >= 0 && num <= 11) {
			month = months[num];
		}
		return month;
	}

	public void getLocalDateTime(MasterDataBean masterDataBean) {
		// LocalDateTime now = LocalDateTime.now();
		// int year = now.getYear();
		// String yearTH = now.format(DateTimeFormatter.ofPattern("yyyy", new
		// Locale("th", "TH")));
		// int month = now.getMonthValue();
		// int day = now.getDayOfMonth();
		// int hour = now.getHour();
		// int minute = now.getMinute();
		// int second = now.getSecond();
		// int millis = now.get(ChronoField.MILLI_OF_SECOND);

		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy", new Locale("th", "TH"));
		String yearTH = df.format(date);

		masterDataBean.setYearNow(yearTH);
	}

	@Override
	public void insertBatch(MasterDataBean masterDataBean) {
		StringBuilder valueSet = new StringBuilder();
		valueSet.append("0");

		if (StringUtils.isNotBlank(masterDataBean.getMinute())) {
			valueSet.append(" ");
			if(masterDataBean.getMinute().equals("0")) {
				valueSet.append(masterDataBean.getMinute());
			}else {
				if("2".equals(masterDataBean.getFlagM())) {
					if("1".equals(masterDataBean.getMinute())) {
						valueSet.append("*");
					}else {
						valueSet.append("*/");
						valueSet.append(masterDataBean.getMinute());
					}
				}
			}
		} else {
			valueSet.append(" 0");
		}

		if(StringUtils.isNotBlank(masterDataBean.getHour())) {
			valueSet.append(" ");
			if(masterDataBean.getHour().equals("0")) {
				valueSet.append(masterDataBean.getHour());
			}else {
				if("2".equals(masterDataBean.getFlagH())) {
					if("1".equals(masterDataBean.getHour())) {
						valueSet.append("*");
					}else {
						valueSet.append("*/");
						valueSet.append(masterDataBean.getHour());
					}
				}
				
			}
		}
		
		if(!"L".equals(masterDataBean.getMonth())) {
			if (StringUtils.isNotBlank(masterDataBean.getDay())) {
				valueSet.append(" ");
				valueSet.append(masterDataBean.getDay());
			} else {
				valueSet.append(" *");
			}
		}

		if (StringUtils.isNotBlank(masterDataBean.getMonth())) {
			valueSet.append(" ");
			valueSet.append(masterDataBean.getMonth());
			
			if("L".equals(masterDataBean.getMonth())) {
				valueSet.append(" *");
			}
		} else {
			valueSet.append("*");
		}

		valueSet.append(" ?");

		if (StringUtils.isNotBlank(valueSet.toString()))
			masterDataBean.setValue(valueSet.toString());

		if (StringUtils.isNotBlank(masterDataBean.getValue()))
			masterDataDao.insertBatch(masterDataBean);
	}

	@Override
	public UserBean findByUsernameFromRole(Long id) throws SQLException {

		return userDao.findByUsernameFromRole(id);
	}

	@Override
	public List<MasterDatasBean> findByCMSegment() {
		List<MasterDatasBean> custSegment = masterDatasDao.findByCMSegment();
		return custSegment;
	}

	@Override
	public List<MasterDatasBean> findByCMSegmentByCRM() {
		List<MasterDatasBean> custSegment = masterDatasDao.findByCMSegmentByCRM();
		return custSegment;
	}

	@Override
	public MasterDatasBean findByGrop(String groupCode, String keyCode) {
		// TODO Auto-generated method stub
		return masterDatasDao.findByGrop(groupCode, keyCode);
	}

	@Override
	public MasterDatasBean findByppt1(String ppt1) {
		if(StringUtils.isNotBlank(ppt1))return masterDatasDao.findByppt1(ppt1);
		
		return null;
		
	}
	
	@Override
	public List<MasterDatasBean> findByCussegment(String userGroup) {
		List<MasterDatasBean> custSegment = masterDatasDao.findByCussegmentValue(userGroup);
		return custSegment;
	}

	@Override
	public List<MasterDatasBean> findByCMSegmentOther(String keyCode) {
		List<MasterDatasBean> custSegment = masterDatasDao.findListByKey(keyCode);
		return custSegment;
	}

}
